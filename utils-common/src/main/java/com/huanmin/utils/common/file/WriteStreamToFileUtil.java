package com.huanmin.utils.common.file;

import com.huanmin.utils.common.base.UniversalException;

import java.io.*;
import java.nio.file.Files;

public class WriteStreamToFileUtil {
    
    
    //将InputStream写入到文件中
    public static void writeInputStreamToFile(InputStream inputStream, File file) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
             BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));) {
            byte[] data = new byte[4096];
            int len;
            while ((len = bis.read(data)) != -1) {
                bos.write(data, 0, len); // 写入数据
            }
        } catch (IOException e) {
             UniversalException.logError(e);
        }
    }
    
}
