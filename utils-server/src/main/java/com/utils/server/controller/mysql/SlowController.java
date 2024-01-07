package com.utils.server.controller.mysql;

import com.alibaba.fastjson.JSON;

import com.utils.common.file.FileUtil;
import com.utils.common.file.FileWebUpload;
import com.utils.common.spring.HttpJsonResponse;
import org.huanmin.file_tool.mysql_log.conditionfilter.FilterProcessorSlow;
import org.huanmin.file_tool.mysql_log.entity.SlowEntity;
import org.huanmin.file_tool.mysql_log.slowsql.SlowSqlAnalyse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("slow")
public class SlowController {
    private static final Logger logger = LoggerFactory.getLogger(SlowController.class);

    @Autowired
    private FilterProcessorSlow filterProcessorSlow;


    @GetMapping("slowList")
    public HttpJsonResponse< Map<String, List<SlowEntity>>> slowList(SlowEntity slowEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(String.valueOf(slowEntity));
        String path = ContextVariableMsql.slowLogPath;
        if (!new File(path).exists()) {
            return HttpJsonResponse.success();
        } else {
            Map<String, List<SlowEntity>> map = SlowSqlAnalyse.toSlowEntitys(path);
            Map<String, List<SlowEntity>> processor = filterProcessorSlow.Processor(map, slowEntity);
            //如果map有key但是value都是null那么在序列化后,map就会消失 ,解决办法就是自己手动转为json格式,然后前端在进行JSON.parse(ref.data);转换为JSON对象进行操作
            return  HttpJsonResponse.success(processor);
        }

    }


    @PostMapping("uploads-slow")
    public HttpJsonResponse<Void> fileuploadSlow(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //上传的位置   这句代码就是将文件上传到当前服务器的根目录下uploads文件夹里添加
        String path =ContextVariableMsql.slowLogPath;
        FileWebUpload.fileUpload(path, request);
        return HttpJsonResponse.success();
    }

    //获取全部文件列表
    @GetMapping("getFiles-slow")
    public HttpJsonResponse< List<String>> getFilesSlow(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path =ContextVariableMsql.slowLogPath;
        List<String> filesAll = FileUtil.getFilesAll(path);
        List<String> fileNmaes=new ArrayList<>();
        filesAll.forEach((filePath)->{
            fileNmaes.add(new File(filePath).getName());
        });
        return HttpJsonResponse.success(fileNmaes);
    }
    @GetMapping("delFiles-slow/{fileName}")
    public HttpJsonResponse<Boolean> delFilesSlow(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path =ContextVariableMsql.slowLogPath;
        boolean delete = new File(path + File.separator + fileName).delete();
        return HttpJsonResponse.success(delete);
    }



}
