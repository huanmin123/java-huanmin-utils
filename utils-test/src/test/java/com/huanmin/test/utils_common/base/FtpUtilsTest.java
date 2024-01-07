package com.huanmin.test.utils_common.base;

import com.utils.common.file.FtpUtil;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class FtpUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtilsTest.class);
    FtpUtil ftpUtils = new FtpUtil(
            "106.12.174.220",21,"root","hu123456!",
            "/home/ftp","D:\\project\\utils\\file-direction\\ftp\\src\\main\\resources\\file");
    //----------------------SftpUtil----------------------------------
    @Test
    public void get() throws Exception {
        ftpUtils.connect();
        boolean download = ftpUtils.download("b.txt");
        ftpUtils.close();
        
    }
    
    @Test
    public void put() throws Exception {
        
        ftpUtils.connect();
        boolean upload = ftpUtils.upload("a.txt");
        ftpUtils.close();
    }
    
    @Test
    public void def() throws Exception {
        ftpUtils.connect();
//        boolean upload = ftpUtils.deleteFile("a.txt");

//        ftpUtils.deleteDirectory("xxx")
        
        ftpUtils.delFtpAllFile("xxxx");
        ftpUtils.close();
    }
    
    @Test
    public void create() throws Exception {
        ftpUtils.connect();
        ftpUtils.createDirectory("xxxx");
        ftpUtils.close();
    }
    
    
    
    
    @Test
    public void ls() throws Exception {
        
        ftpUtils.connect();
        List<FTPFile> list = ftpUtils.list();
        for (FTPFile ftpFile : list) {
            System.out.println(ftpFile.getName());
        }
        
        ftpUtils.close();
        
    }
    @Test
    public void getfile() throws IOException {
    
    
    }
    @Test
    public void getfileByte() throws IOException {
    
    
    }
}
