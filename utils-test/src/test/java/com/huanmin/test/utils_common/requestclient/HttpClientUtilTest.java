package com.huanmin.test.utils_common.requestclient;

import com.utils.common.base.ResourceFileUtil;
import com.utils.common.requestclient.httpclient.HttpClientUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class HttpClientUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtilTest.class);
// ----------------------------------------  一下代码 都是测试代码

    @Test
    public void get(){
        String s = HttpClientUtil.httpClient("Get","http://localhost:7001/http?name=11&age=123", null);
        System.out.println("返回的结果");
        System.out.println(s);
    }

    @Test
    public void post(){
        String json="{\"name\":\"你好\"}";

        String s =HttpClientUtil.httpClient("Post", "http://localhost:7001/http", json);
        System.out.println("返回的结果");
        System.out.println(s);
    }


    @Test
    public void put(){
        String json="{\"num\":\"12\"}";

        String s =HttpClientUtil.httpClient("Put", "http://localhost:7001/http", json);
        System.out.println("返回的结果");
        System.out.println(s);
    }

    @Test
    public void delete(){
        String json="{\"num\":\"15\"}";
        String s =HttpClientUtil.httpClient("Delete", "http://localhost:7001/http", json);
        System.out.println("返回的结果");
        System.out.println(s);
    }

    @Test
    public void getMap() throws Exception {
        Map  map=new HashMap();
        map.put("name","123");
        map.put("name1","1233");
        String s =HttpClientUtil.doGet("http://localhost:7001/http", map);
        System.out.println("返回的结果");
        System.out.println(s);
    }
    @Test
    public void PostMap() throws Exception {
        Map  map=new HashMap();
        map.put("name","123");
        map.put("name1","1233");
        String s =HttpClientUtil.doPost("http://localhost:7001/http", map);
        System.out.println("返回的结果");
        System.out.println(s);
    }
    @Test
    public void PutMap() throws Exception {
        Map  map=new HashMap();
        map.put("bbb","123");
        map.put("aaaa","1233");
        String s =HttpClientUtil.doPut("http://localhost:7001/http", map);
        System.out.println("返回的结果");
        System.out.println(s);
    }

    @Test
    public void DeleteMap() throws Exception {
        Map  map=new HashMap();
        map.put("ccc","123");
        map.put("eee","1233");
        String s =HttpClientUtil.doDelete("http://localhost:7001/http", map);
        System.out.println("返回的结果");
        System.out.println(s);
    }




    //文件下载

    @Test
    public void httpClientDownloadFile(){
        String url = "http://192.168.66.70:8088/group1/M00/00/00/wKhCQ1_HS9-AMVDnAGajh_YmPtc012.jpg";
        String filepath = "D:\\a1.jpg";
        HttpClientUtil.httpClientDownloadFile(url, filepath);
    }



    //  文件上传测试
    @Test
    public void httpClientUploadFile() throws FileNotFoundException {
        String url="http://localhost:7001/http/upload";
        String absolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("1.jpg");   //本地文件路径也行....
        String s =HttpClientUtil.httpClientUploadFile(url, new File(absolutePath));
        System.out.println(s);
    }

    //  文件上传测试
    @Test
    public void httpClientUploadFiles() throws FileNotFoundException {
        String url="http://localhost:7001/http/uploads";
        String absolutePath1 = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("1.jpg");   //本地文件路径也行....
        String absolutePath2 = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("2.jpg");   //本地文件路径也行....
        String absolutePath3 = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("3.jpg");   //本地文件路径也行....
        File[] files ={new File(absolutePath1),new File(absolutePath2),new File(absolutePath3)};
        String s =HttpClientUtil.httpClientUploadFiles(url, files);
        System.out.println(s);
    }

}
