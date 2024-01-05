package com.utils.common.file.condense;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *   打包为zip压缩文件,和解压缩
 */
public class ZipFileUtils {
    /**
     * 压缩文件方法
     *
     * @param fileList 需要压缩的文件列表
     * @param destFile 目标文件
     * @throws IOException 异常信息
     */
    public static void zip(List<File> fileList, File destFile) throws IOException {
        if (fileList == null || fileList.isEmpty()) {
            return;
        }
        //zip输出流对象
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)));
        //设置压缩等级，level (0-9)
        zipOutputStream.setLevel(5);
        //遍历文件列表
        zip( zipOutputStream,fileList);
        zipOutputStream.close();
    }



    public static void zip( ZipOutputStream zipOutputStream,List<File> fileList) throws IOException {
        //遍历文件列表
        for (File file : fileList) {
            if (file.isDirectory()) {
                zip( zipOutputStream, Arrays.asList(Objects.requireNonNull(file.listFiles())));
            }
            if (file.isFile()){
                //zip文件条目对象
                String name = file.getParentFile().getName();
                String s = name.length()>0?name+File.separator + file.getName():file.getName();
                ZipEntry zipEntry = new ZipEntry(s);
                //将文件条目对象放入zip流中
                zipOutputStream.putNextEntry(zipEntry);
                //将文件信息写入zip流中
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = fileInputStream.read(bytes)) != -1) {
                    zipOutputStream.write(bytes, 0, len);
                }
                zipOutputStream.closeEntry();
                fileInputStream.close();
            }

        }
    }

    /**
     * 解压缩zip文件
     *
     * @param zipFile  待解压的文件
     * @param destFile 解压到哪个目录
     */
    public static void unzip(File zipFile, File destFile) {
        //目标文件必须是一个目录
        if (destFile.isFile()) {
            return;
        }
        //文件夹不存在要先创建文件夹
        if (!destFile.exists()) {
            destFile.mkdirs();
        }
        //创建一个zip读取流，在try中直接写会自动关闭流对象，无需手动关闭
        try (ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry zipEntry;
            //遍历读取文件条目，读取下一个 ZIP文件条目并将流定位在条目数据的开头。
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String name = zipEntry.getName();
                //构建一个新文件对象，创建在destFile目录下
                File f = new File(destFile, name);
                //判断目录是否存在如果不存在那么就创建
                File parentFile = f.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                //将文件条目数据写入到新的文件对象中，
                FileOutputStream fileOutputStream = new FileOutputStream(f, false);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = zipInputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);
                }
                zipInputStream.closeEntry();
                fileOutputStream.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
