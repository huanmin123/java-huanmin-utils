package org.huanmin.utils.common.file;

import org.huanmin.utils.common.base.UniversalException;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TransitionFileStreamUtil {
    /**
     * 把文件流转成字符串
     *
     * @param is
     * @return
     */
    public static String readIoToStr(InputStream is) {
        byte[] bytes = inputStreamTobyte(is);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    // 文件流InputStream转byte
    @SneakyThrows
    public static byte[] inputStreamTobyte(InputStream inputStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int available = inputStream.available();
        byte[] buff = new byte[available]; //buff用于存放循环读取的临时数据
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, available)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }
    
    /*
       字符串转文件流
    */
    public static InputStream strToIo(String str) {
        return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
    }
    
    
    // 字节转文件流
    public static InputStream byteToIo(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }





    
    //将输入流转换为输出流
    public static void inToOut(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }
    
    //文件转换为InputStream
    public static InputStream fileToInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
             UniversalException.logError(e);
        }
        return null;
    }
    

    //文件流FileInputStream转byte
    @SneakyThrows
    public static byte[] fileInputStreamTobyte(FileInputStream fileInputStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int available = fileInputStream.available();
        byte[] buff = new byte[available]; //buff用于存放循环读取的临时数据
        int rc = 0;
        while ((rc = fileInputStream.read(buff, 0, available)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }
    
    
    
}
