package com.huanmin.test.utils.utils_common.base;


import com.huanmin.utils.common.file.SftpUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * @ClassName: FtpOperationUtil
 * @Description:sftp 工具类，使用的ECS上ftp需要sftp协议才能连接。
 * @date: 2020年4月6日 下午6:09:49
 * @Copyright:
 */

public class SftpUtilTest  {


    private static final Logger logger = LoggerFactory.getLogger(SftpUtilTest.class);
 
    private SftpUtil sftpOperationUtil = new  SftpUtil(
            "106.12.174.220",22,"root","hu123456!",
            "/home/ftp","D:\\project\\utils\\file-direction\\ftp\\src\\main\\resources\\file");;
    
    //----------------------SftpUtil----------------------------------
    @Test
    public void get() throws IOException {
        boolean connection = sftpOperationUtil.connection();
        boolean result = sftpOperationUtil.get("a.txt");
        sftpOperationUtil.close();
        
        boolean connection1 = sftpOperationUtil.connection();
        boolean b = sftpOperationUtil.get("b.txt");
        sftpOperationUtil.close();
        
    }
    
    @Test
    public void put() throws IOException {
        boolean connection = sftpOperationUtil.connection();
        boolean result = sftpOperationUtil.put("c.txt");
        
    }
    
    @Test
    public void def() throws IOException {
        boolean connection = sftpOperationUtil.connection();
        boolean result = sftpOperationUtil.delFile("c.txt");
        
    }
    @Test
    public void ls() throws IOException {
        boolean connection = sftpOperationUtil.connection();
        Vector ls = sftpOperationUtil.ls("/");
        
        for (Object l : ls) {
            
            System.out.println((String) l);
        }
        
        
    }
    @Test
    public void getfile() throws IOException {
        boolean connection = sftpOperationUtil.connection();
        List<String> fileLines = sftpOperationUtil.getFileLines("a.txt", "utf-8");
        
        for (String fileLine : fileLines) {
            
            System.out.println(fileLine);
        }
        sftpOperationUtil.close();
        
    }
    @Test
    public void getfileByte() throws IOException {
        boolean connection = sftpOperationUtil.connection();
        byte[] fileByte = sftpOperationUtil.getFileByte("a.txt");
        String s = new String(fileByte,"utf-8");
        
        System.out.println(s);
        sftpOperationUtil.close();
        
    }


}