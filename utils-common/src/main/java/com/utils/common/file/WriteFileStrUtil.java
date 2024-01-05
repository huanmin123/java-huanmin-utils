package com.utils.common.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class WriteFileStrUtil {
    
    
    /**
     * 将字符串写入文本中,(追加方式)
     *
     * @param path 写入的文件
     * @param str  需要追加的字符串
     */
    public static void writeStrAppend(File path, String str) {
        writeStr(path, str, true);
    }
    
    
    /**
     * 将字符串写入文本中,(覆盖方式)
     *
     * @param path 写入的文件
     * @param str  需要追加的字符串
     */
    public static void writeStrCover(File path, String str) {
        writeStr(path, str, false);
    }
    
    private static void writeStr(File path, String str, boolean type) {
        try (BufferedWriter bw =
                     new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, type)));) {
            bw.write(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
