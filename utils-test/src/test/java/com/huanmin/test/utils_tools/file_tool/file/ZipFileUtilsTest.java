package com.huanmin.test.utils_tools.file_tool.file;

import com.utils.common.file.condense.ZipFileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZipFileUtilsTest {

    @Test
    public  void show1() throws IOException {
        //目标文件
        File destFile = new File("e:\\myFile.zip");
        //压缩文件
        List<File> list = new ArrayList<>();
        list.add(new File("e:\\1.txt"));
        list.add(new File("e:\\2.txt"));
        list.add(new File("e:\\a"));
        ZipFileUtil.zip(list, destFile);


    }

    @Test
    public void show2(){
        File destFile = new File("e:\\myFile.zip");
        //解压缩文件
        ZipFileUtil.unzip(destFile, new File("e:\\myunzip"));
    }
}
