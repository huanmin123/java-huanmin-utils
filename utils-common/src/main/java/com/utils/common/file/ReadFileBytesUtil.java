package com.utils.common.file;

import com.utils.common.base.UniversalException;
import com.utils.common.container.ArrayByteUtil;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ReadFileBytesUtil {
    //读取一个文件的全部字节
    public static byte[] readByte(File read) {
        byte[] data = new byte[(int) read.length()];
        try (BufferedInputStream fis = new BufferedInputStream(Files.newInputStream(read.toPath()));) {
            fis.read(data);
        } catch (IOException e) {
             UniversalException.logError(e);
        }
        return data;
    }
    // 读取开头指定的字节
    @SneakyThrows
    public static byte[] reaStartByte(File fIle, int size) {
        return reaStartByte(new FileInputStream(fIle), size);
    }
    
    public static byte[] reaStartByte(FileInputStream fileInputStream, int size) {
        byte[] data = new byte[size];
        try (BufferedInputStream fis = new BufferedInputStream(fileInputStream);) {
            fis.read(data);
        } catch (IOException e) {
             UniversalException.logError(e);
        }
        // 调整data的大小为实际的大小
        data = ArrayByteUtil.getActualBytes(data);
        return data;
    }
}
