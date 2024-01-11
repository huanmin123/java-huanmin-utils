package com.huanmin.utils.server.controller.file;

import com.huanmin.utils.common.base.UniversalException;
import com.huanmin.utils.common.container.ArrayByteUtil;
import com.huanmin.utils.common.encryption.hash.HashUtil;
import com.huanmin.utils.common.file.FileUtil;
import com.huanmin.utils.common.file.FileWebUpload;
import com.huanmin.utils.common.file.ResourceFileUtil;
import com.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import com.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import com.huanmin.utils.common.spring.HttpJsonResponse;
import com.huanmin.utils.common.string.PatternCommon;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fileslice")
public class FIleSliceUploadController {

    private  final  String identification="-slice-";
    private  final  String uploadslicedir="uploads"+File.separator+"slice"+File.separator;//分片目录
    private  final  String uploaddir="uploads"+File.separator+"real"+File.separator;//实际文件目录
    //获取分片
    @GetMapping("/testing/{fileName}/{fileSlicSize}/{fileSize}")
    public HttpJsonResponse<String> testing(@PathVariable String fileName, @PathVariable long fileSlicSize, @PathVariable long fileSize  ) throws Exception {
        String dir = fileNameMd5Dir(fileName,fileSize);
        String absoluteFilePathAndCreate = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath(uploadslicedir)+File.separator+dir;
        File file = new File(absoluteFilePathAndCreate);
        if (file.exists()) {
            List<String> filesAll = FileUtil.getFilesAll(file.getAbsolutePath());

            if (filesAll.size()<2){
                //分片缺少 删除全部分片文件 ,从新上传
                FileUtil.delFilesAllReview(absoluteFilePathAndCreate,true);
                return HttpJsonResponse.failed();
            }

            //从小到大文件进行按照序号排序,和判断分片是否损坏
            List<String> collect = fileSliceIsbadAndSort(file, fileSlicSize);
            //获取最后一个分片
            String fileSliceName = collect.get(collect.size() - 1);
            fileSliceName = new File(fileSliceName).getName();
            int code = fileId(fileSliceName);
            //服务器的分片总大小必须小于或者等于文件的总大小
            if ((code*fileSlicSize)<=fileSize) {

                String finalFileSliceName = fileSliceName;
                String str = PatternCommon.renderString("{\"code\":\"${code}\",\"fileSliceName\":\"${fileSliceName}\"}", new HashMap<String, String>() {{
                    put("code", String.valueOf(code));
                    put("fileSliceName", finalFileSliceName);
                }});
                return HttpJsonResponse.success(str);
            }else {
                //分片异常 ,删除全部分片文件,从新上传
                FileUtil.delFilesAllReview(absoluteFilePathAndCreate,true);
                return HttpJsonResponse.failed();
            }
        }
        //不存在
       return  HttpJsonResponse.failed();
    }


    @PostMapping(value = "/uploads")
    public HttpJsonResponse<Integer> uploads(HttpServletRequest request)  {
        String fileSliceName = request.getParameter("fileSliceName");
        long fileSize = Long.parseLong(request.getParameter("fileSize")); //文件大小
        String dir = fileSliceMd5Dir(fileSliceName,fileSize);
        String absoluteFilePathAndCreate = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath(uploadslicedir+dir);
        FileWebUpload.fileUpload(absoluteFilePathAndCreate,fileSliceName,request);
        int i = fileId(fileSliceName); //返回上传成功的文件id,用于前端计算进度

        return  HttpJsonResponse.success(i);
    }


    // 合并分片
    @GetMapping(value = "/merge-file-slice/{fileSlicNamee}/{fileSlicSize}/{fileSize}")
    public HttpJsonResponse mergeFileSlice(@PathVariable String fileSlicNamee,@PathVariable long fileSlicSize,@PathVariable long fileSize ) throws Exception {
        int l =(int) Math.ceil((double) fileSize / fileSlicSize); //有多少个分片
        String dir = fileSliceMd5Dir(fileSlicNamee,fileSize); //分片所在的目录
       String absoluteFilePathAndCreate = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath(uploadslicedir+dir);
        File file=new File(absoluteFilePathAndCreate);
        if (file.exists()){
            List<String> filesAll = FileUtil.getFilesAll(file.getAbsolutePath());

            //阻塞循环判断是否还在上传  ,解决前端进行ajax异步上传的问题
            int beforeSize=filesAll.size();

            while (true){
                 Thread.sleep(1000);
                 //之前分片数量和现在分片数据只差,如果大于1那么就在上传,那么继续
                 filesAll = FileUtil.getFilesAll(file.getAbsolutePath());
                if (filesAll.size()-beforeSize>=1){
                    beforeSize=filesAll.size();
                    //继续检测
                    continue;
                }
                //如果是之前分片和现在的分片相等的,那么在阻塞2秒后检测是否发生变化,如果还没变化那么上传全部完成,可以进行合并了
                //当然这不是绝对的,只能解决网络短暂的波动,因为有可能发生断网很长时间,网络恢复后文件恢复上传, 这个问题是避免不了的,所以我们在下面的代码进行数量的效验
                // 因为我们不可能一直等着他网好,所以如果1~3秒内没有上传新的内容,那么我们默认判定上传完毕
                if (beforeSize==filesAll.size()){
                    Thread.sleep(2000);
                    filesAll = FileUtil.getFilesAll(file.getAbsolutePath());
                    if (beforeSize==filesAll.size()){
                        break;
                    }
                }
            }
            //分片数量效验
            if (filesAll.size()!=l){
                //分片缺少 ,删除全部分片文件,从新上传
                FileUtil.delFilesAllReview(absoluteFilePathAndCreate,true);
                return  HttpJsonResponse.failed();
            }
            //获取实际的文件名称,组装路径
            String realFileName = realFileName(fileSlicNamee);
            String realFileNamePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath(uploaddir+ realFileName);
            //从小到大文件进行按照序号排序 ,和检查分片文件是否有问题
            List<String> collect = fileSliceIsbadAndSort(file, fileSlicSize);
            int fileSliceSize = collect.size();

            List<Future<?>> futures = new ArrayList<>();
            // 将文件按照序号进行合并 ,算出Runtime.getRuntime().availableProcessors()个线程 ,每个线程需要读取多少分片, 和每个线程需要读取多少字节大小
            //有人会说一个分片一个线程不行吗,你想想如果上千或者上万分片的话,你创建这么多的线程需要多少时间,以及线程切换上下文切换和销毁需要多少时间?
            // 就算使用线程池,也顶不住啊,你内存又有多大,能存下多少队列?,并发高的话直接怼爆
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            //每个线程读取多少文件
            int readFileSize = (int)Math.ceil((double)fileSliceSize / availableProcessors);
            //每个线程需要读取的文件大小
            long readSliceSize = readFileSize * fileSlicSize;
            for (int i = 0; i < availableProcessors; i++) {
                int finalI = i;
                Future<?> future =   ExecutorUtil.createFuture(ThreadFactoryUtil.ThreadConfig.FIleSliceUploadController,()->{
                    //每个线程需要读取多少字节
                    byte[] bytes=new byte[(int) readSliceSize];
                    int index=0;
                    for (int i1 = finalI *readFileSize,i2 = readFileSize*(finalI+1)>fileSliceSize?fileSliceSize:readFileSize*(finalI+1); i1 < i2; i1++) {
                        try ( RandomAccessFile r = new RandomAccessFile(collect.get(i1), "r");){
                            r.read(bytes, (int)(index*fileSlicSize),(int)fileSlicSize);
                        } catch (IOException e) {
                            UniversalException.logError(e);
                        }
                        index++;
                    }

                    if(finalI==availableProcessors-1){
                        //需要调整数组
                        bytes = ArrayByteUtil.getActualBytes(bytes);
                    }

                    try ( RandomAccessFile w = new RandomAccessFile(realFileNamePath, "rw");){
                        //当前文件写入的位置
                        w.seek(finalI*readSliceSize);
                        w.write(bytes);
                    } catch (IOException e) {
                        UniversalException.logError(e);
                    }
                });
                futures.add(future);
            }
            //阻塞到全部线程执行完毕后
            ExecutorUtil.waitComplete(futures);
            //删除全部分片文件
            FileUtil.delFilesAllReview(absoluteFilePathAndCreate,true);
        }else {
            //没有这个分片相关的的目录
            return  HttpJsonResponse.failed();
        }

        return  HttpJsonResponse.success();

    }






    //获取分片文件的目录
    private String fileSliceMd5Dir(String fileSliceName,long fileSize){
        int i = fileSliceName.indexOf(identification) ;
        String substring = fileSliceName.substring(0, i);
        String dir = HashUtil.md5(substring+fileSize);
        return dir;
    }
    //通过文件名称获取文件目录
    private String fileNameMd5Dir(String fileName,long fileSize){
        return HashUtil.md5(fileName+fileSize);
    }
    //获取分片的实际文件名
    private String realFileName(String fileSliceName){
        int i = fileSliceName.indexOf(identification) ;
        String substring = fileSliceName.substring(0, i);
        return substring;

    }
    //获取文件序号
    private  int fileId(String fileSliceName){
        int i = fileSliceName.indexOf(identification)+identification.length() ;
        String fileId = fileSliceName.substring(i);
        return Integer.parseInt(fileId);
    }



  //判断是否损坏
  private List<String>  fileSliceIsbadAndSort(File file,long fileSlicSize) throws Exception {
        String absolutePath = file.getAbsolutePath();
        List<String> filesAll = FileUtil.getFilesAll(absolutePath);
        if (filesAll.size()<1){
            //分片缺少,删除全部分片文件 ,从新上传
            FileUtil.delFilesAllReview(absolutePath,true);
            throw  new Exception("分片损坏");
        }
        //从小到大文件进行按照序号排序
        List<String> collect = filesAll.stream().sorted((a, b) -> fileId(a) - fileId(b)).collect(Collectors.toList());
        //判断文件是否损坏,将文件排序后,进行前后序号相差大于1那么就代表少分片了
        for (int i = 0; i < collect.size()-1; i++) {
            //检测分片的连续度
            if (fileId(collect.get(i)) - fileId(collect.get(i+1))!=-1) {
                //分片损坏 删除全部分片文件 ,从新上传
                FileUtil.delFilesAllReview(absolutePath,true);
                throw  new Exception("分片损坏");
            }
            //检测分片的完整度
            if (new File(collect.get(i)).length()!=fileSlicSize) {
                //分片损坏 删除全部分片文件 ,从新上传
                FileUtil.delFilesAllReview(absolutePath,true);
                throw  new Exception("分片损坏");
            }
        }
        return  collect;
    }
}
