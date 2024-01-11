package com.huanmin.test.utils.utils_tools.file_tool.file;

import com.huanmin.utils.common.file.ResourceFileUtil;
import com.huanmin.utils.common.file.RandomAccessFileUtil;
import org.junit.Test;

import java.io.File;

public class RandomAccessFileUtilTest {
    @Test
    public  void randomAccessFileReadByte(){
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/aaa.txt");
        RandomAccessFileUtil.randomAccessFileReadBytes(file, 1024, (b, r) -> {
            System.out.println(new String(b));
            return "continue".getBytes();
        });
    }
    @Test
    public  void  randomAccessFileReadWritByte(){
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/aaa.txt");
        File file1 = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/aaa1.txt");
        RandomAccessFileUtil.randomAccessFileReadWritBytes(file, file1, 1024, (b, r) -> {
            //读字节处理
            return b;
        }, (b, w) -> {
            //写之前字节处理
            return b;
        });
    }
}
