package org.huanmin.utils.common.file;

import org.huanmin.utils.common.base.UniversalException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteFileBytesUtil {
    
    //将全部字节写入到文件中
    public static void writeByte(byte[] b, File writePath, boolean append) {
        try (
                FileOutputStream fos1 = new FileOutputStream(writePath, append);
                BufferedOutputStream fos = new BufferedOutputStream(fos1);) {
            fos.write(b, 0, b.length); // 写入数据
        } catch (IOException e) {
             UniversalException.logError(e);
        }
    }
    
    public static void writeByte(byte[] b, File writePath) {
        writeByte(b, writePath, false);
    }
}
