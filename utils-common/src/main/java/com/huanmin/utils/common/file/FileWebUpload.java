package com.huanmin.utils.common.file;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

/**
 * 游览器上传文件到服务器中
 */
public class FileWebUpload {
    private static final Logger logger = LoggerFactory.getLogger(FileWebUpload.class);



    @SneakyThrows
    public static  void  fileUpload(String directory,HttpServletRequest request)  {
        fileUpload(directory,null,request);
    }
    /**
     *
     * @param directory 文件上传的目录
     * @param fileName  文件名称  ,如果为null 那么默认为上传的文件名称
     * @param request
     */
    @SneakyThrows
    public static  void  fileUpload(String directory,String fileName,HttpServletRequest request)  {
        //上传的位置   这句代码就是将文件上传到当前服务器的根目录下uploads文件夹里添加
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
        //拿到所有上传的文件对象
        for (Map.Entry<String, MultipartFile> stringMultipartFileEntry : multiFileMap.toSingleValueMap().entrySet()) {
            MultipartFile uploadFile = stringMultipartFileEntry.getValue();
            //判断文件上传表单是否是空
            boolean empty = uploadFile.isEmpty();
            //如果是空 的file表单那么 跳过
            if (!empty) {
                String filename = uploadFile.getOriginalFilename();
                if (fileName!=null){
                    filename=fileName;
                }
                //完成文件上传
                uploadFile.transferTo(new File(directory, filename));
                logger.info("文件上传后的路径" + new File(directory, filename).getAbsolutePath());
            } else {
                throw new Exception("上传的文件是空的");
            }
        }

    }





}
