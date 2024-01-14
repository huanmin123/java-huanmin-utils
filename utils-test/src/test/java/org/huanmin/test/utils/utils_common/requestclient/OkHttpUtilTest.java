package org.huanmin.test.utils.utils_common.requestclient;

import org.huanmin.utils.common.requestclient.okhttp.OkHttpUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class OkHttpUtilTest {
    //注意: addHeader和addParam的必须在get或者post之前调用
    @Test
    public void get(){
        String sync = OkHttpUtil.builder()
                .url("http://localhost:7001/http")
                              .addHeader("token", "xxx")
                              .addParam("name", "123")
                .get()
                .sync();
    }

    @Test
    public void post(){
        String sync = OkHttpUtil.builder()
                .url("http://localhost:7001/http")
                              .addHeader("token", "xxx")
                              .addParam("name", "123")
                .post("json")
 
                .sync();
    }
    //上传文件 ,路径方式
    @Test
    public void upload() throws IOException {
        String sync = OkHttpUtil.builder()
                              .url("http://localhost:7003/testfile/upload")
                              .addParam("token", "xxx")
                              .addParam("filePath", "F:\\file\\1.png")
                              .addParam("fileKey", "file")
                              .post("file")
                              .sync();
    
        System.out.println(sync);
    }
    //上传多个文件 ,路径方式
    @Test
    public void uploadMore() throws IOException {
        String sync = OkHttpUtil.builder()
                              .url("http://localhost:7003/testfile/uploads")
                              .addParam("token", "xxx")
                              .addParam("filePaths", new String[]{"F:\\file\\1.png","F:\\file\\2.png"})
                              .addParam("fileKey","files" )
                              .post("file")
                              .sync();
    
        System.out.println(sync);
    }
    //上传文件 ,文件流方式
    @Test
    public void upload2() throws IOException {
        //读取文件
        File file = new File("F:\\file\\1.png");
        //将文件内容转换为字节数组
        byte[] bytes = FileUtils.readFileToByteArray(file);
        String sync = OkHttpUtil.builder()
                              .url("http://localhost:7003/testfile/upload")
                              .addParam("token", "xxx")
                              .addParam("fileByte", bytes)
                              .addParam("fileByteName", "1.png")
                              .addParam("fileKey", "file")
                              .post("file")
                              .sync();
        System.out.println(sync);
    }
    //上传多个文件 ,文件流方式
    @Test
    public void uploadMore2() throws IOException {
        //读取文件
        File file = new File("F:\\file\\1.png");
        File file2 = new File("F:\\file\\2.png");
        //将文件内容转换为字节数组
        byte[] bytes = FileUtils.readFileToByteArray(file);
        byte[] bytes2 = FileUtils.readFileToByteArray(file2);
        String sync = OkHttpUtil.builder()
                              .url("http://localhost:7003/testfile/uploads")
                              .addParam("token", "xxx")
                              .addParam("fileBytes", new byte[][]{bytes,bytes2})
                              .addParam("fileBytesNames", new String[]{"1.png","2.png"})
                              .addParam("fileKey","files" )
                              .post("file")
                              .sync();
        System.out.println(sync);
    }
    
    
    
    
    //下载文件
    @Test
    public void download() throws IOException {
      OkHttpUtil.builder()
                .url("https://img-home.csdnimg.cn/images/1.png")
                .addParam("token", "xxx")
                .get()
                .downloadFile("F:\\file\\dow\\1.png");
      System.in.read();
    }





}
