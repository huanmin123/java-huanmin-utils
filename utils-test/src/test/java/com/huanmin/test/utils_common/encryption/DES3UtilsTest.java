package com.huanmin.test.utils_common.encryption;


import com.utils.common.encryption.des3.DES3Util;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class DES3UtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(DES3UtilsTest.class);
    //加密 ,解密
    @Test
    public void show1(){
            //秘钥  必须是大于24长度
        String str= DES3Util.DESEncrypt("1217", "dddaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(str);
        String str2=DES3Util.DESDecrypt(str, "dddaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(str2);

    }





    String keyFile = "1111111111111111111111111"; //秘钥  必须是大于24长度
    String workFolder = System.getProperty("user.dir")+"\\src\\main\\resources";//获取当前项目的根
    @Test
    public void show3(){
        System.out.println("开始测试DES3加密");
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(workFolder + "file\\DES3Input.txt")), "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(workFolder + "file\\DES3EncodeOutput-Java.txt")), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String encryptedData = DES3Util.DESEncrypt(lineTxt,keyFile);
                bw.write(encryptedData);
                bw.newLine();
                bw.flush();
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        System.out.println("结束测试DES3加密");
    }

    //解密,件

    @Test
    public void show4(){

        System.out.println("开始测试DES3解密"+workFolder);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(workFolder + "file\\DES3EncodeOutput-Java.txt")), "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(workFolder + "file\\DES3DecodeOutput-Java.txt")), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                String decryptedData = DES3Util.DESDecrypt(lineTxt,keyFile);
                System.out.println(decryptedData);
                bw.write(decryptedData);
                bw.newLine();
                bw.flush();
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        System.out.println("结束测试DES3解密");

        System.out.println("完成");
    }


}
