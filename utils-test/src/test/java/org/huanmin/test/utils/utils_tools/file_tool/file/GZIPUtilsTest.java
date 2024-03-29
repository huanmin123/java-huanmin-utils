package org.huanmin.test.utils.utils_tools.file_tool.file;


import org.huanmin.utils.common.file.GZIPUtil;
import org.huanmin.utils.common.file.ResourceFileUtil;
import org.huanmin.utils.common.file.ReadFileBytesUtil;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GZIPUtilsTest {

    @Test
    public void write()  {
        File file1 = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/DES3DecodeOutput-Java.txt");
        System.out.println(file1.getAbsolutePath());
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/file/bac.gz");
        GZIPUtil.writeGzFile(absoluteFileOrDirPathAndCreateNewFile,file1);
    }
    @Test
    public void read() throws FileNotFoundException {
        File file1 = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/bac.gz");
        GZIPUtil.readGzFile(file1,(len)->{
            System.out.println(len);
        });
    }

    @Test
    public void targz1()  {
        String bac111 = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/file/bac111.tar.gz")  ;
        List<File> file=new ArrayList<>();
        file.add(ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/DES3DecodeOutput-Java.txt"));
        file.add(ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/DES3EncodeOutput-Java.txt"));
        GZIPUtil.compressTarGz(file,bac111);
    }
    @SneakyThrows
    @Test
    public void unTarGz(){
        String bac111 = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/file/bac111.tar.gz")  ;
        String outbac111 = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/file/bac/")  ;
        GZIPUtil.unTarGz(bac111,outbac111);
    }







    @SneakyThrows
    @Test
    public void compress(){
        String str = "111111111111111111111111111111111111111111111111111111111111111111111";

        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/file/compress.gz");
        GZIPUtil.compressFileWrite(absoluteFileOrDirPathAndCreateNewFile,str,true);


    }
    @SneakyThrows
    @Test
    public void compr3ess1(){
        String str = "2222222222222222222222222222222222222222222222222";
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/file/compress.gz");
        GZIPUtil.compressFileWrite(absoluteFileOrDirPathAndCreateNewFile,str,true);

    }
    @SneakyThrows
    @Test
    public void compress1(){
        String str = "3333333333333333333333333333333";
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/file/compress.gz");
        GZIPUtil.compressFileWrite(absoluteFileOrDirPathAndCreateNewFile,str,true);


    }


    @Test
    public void show(){
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/file/compress.gz");
        byte[] bytes = ReadFileBytesUtil.readByte(absoluteFileOrDirPathAndCreateNewFile);
        System.out.println("解压缩后字符串：" + GZIPUtil.uncompressToString(bytes));
    }
}
