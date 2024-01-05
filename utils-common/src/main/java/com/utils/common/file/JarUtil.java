package com.utils.common.file;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/24 12:54
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class JarUtil {
    // 获取当前项目打包后jar的绝对路径
    public static File getCurrentProjectJarAbsolutePath() {
        String path = JarUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return new File(path);
    }


    /**
     * 读取jar包中的所有文件,并且返回文件的名称和文件的输入流
     *
     * @param jarPath jar包的路径
     * @return
     * @throws IOException
     */
    public static Map<JarEntry, InputStream> readJarFile(String jarPath) throws IOException {
        Map<JarEntry, InputStream> map = new HashMap<>();
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            InputStream inputStream = jarFile.getInputStream(jarEntry);
            map.put(jarEntry, inputStream);
        }
        return map;
    }


    public static Map<JarEntry, InputStream> readJarFileRecursion(String jarPath) throws IOException {
        Map<JarEntry, InputStream> jarEntryInputStreamMap = new HashMap<>();
        readJarFileRecursion(jarPath, jarEntryInputStreamMap);
        return jarEntryInputStreamMap;
    }

    //递归读取jar包中的所有jar包的所有文件
    public static void readJarFileRecursion(String jarPath, Map<JarEntry, InputStream> map) throws IOException {
        Map<JarEntry, InputStream> jarEntryInputStreamMap = readJarFile(jarPath);
        for (Map.Entry<JarEntry, InputStream> entry : jarEntryInputStreamMap.entrySet()) {
            JarEntry key = entry.getKey();
            InputStream value = entry.getValue();
            String name = key.getName();
            if (name.endsWith(".jar")) {
                //将jar包复制到临时目录
                String tempPath = System.getProperty("java.io.tmpdir");
                File file = new File(tempPath + File.separator + name);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = value.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);
                }
                fileOutputStream.close();
                value.close();
                readJarFileRecursion(file.getAbsolutePath(), map);
                FileUtil.delFilesAllReview(file.getAbsolutePath(), true);
            }
            map.put(key, value);
        }
    }

    /**
     * @param jarPath jar包的路径
     * @param regex   正则表达式 ,用来匹配文件名
     *                比如: ^scenery/.+   获取scenery文件夹下的所有文件    ,比如.*\.jar$  获取所有jar文件
     * @return
     * @throws IOException
     */
    public static Map<JarEntry, InputStream> readJarFile(String jarPath, String regex) throws IOException {
        Map<JarEntry, InputStream> jarEntryInputStreamMap = readJarFile(jarPath);
        jarEntryInputStreamMap = jarEntryInputStreamMap.entrySet().stream().filter(entry -> {
            String name = entry.getKey().getName();
            return name.matches(regex);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return jarEntryInputStreamMap;
    }

    public static Map<JarEntry, InputStream> readJarFileRecursion(String jarPath, String regex) throws IOException {
        Map<JarEntry, InputStream> jarEntryInputStreamMap = readJarFileRecursion(jarPath);
        jarEntryInputStreamMap = jarEntryInputStreamMap.entrySet().stream().filter(entry -> {
            String name = entry.getKey().getName();
            return name.matches(regex);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return jarEntryInputStreamMap;
    }


    //将jar指定的文件复制到指定的目录

    /**
     *
     * @param jarPath jar包的路径
     * @param regex 正则表达式 ,用来匹配文件名
     * @param targetPath 目标路径(自己本地的路径)
     * @throws IOException
     */
    public static void copyJarFile(String jarPath, String regex, String targetPath) throws IOException {
        Map<JarEntry, InputStream> jarEntryInputStreamMap = readJarFile(jarPath, regex);
        for (Map.Entry<JarEntry, InputStream> entry : jarEntryInputStreamMap.entrySet()) {
            JarEntry key = entry.getKey();
            InputStream value = entry.getValue();
            String name = key.getName();
            File file = new File(targetPath + File.separator + name);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = value.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);
            }
            fileOutputStream.close();
            value.close();
        }
    }

    //获取jar包中的所有jar包
    public static Map<JarEntry, InputStream> getJarAllFile(String jarPath) throws IOException {
        Map<JarEntry, InputStream> map = readJarFileRecursion(jarPath, ".*\\.jar$");
        return map;
    }


    //向jar包中指定位置添加文件

    /**
     *
     * @param jarPath jar包的路径
     * @param filePath 文件的路径
     * @param targetPath 文件在jar包中的路径    ( targetPath+ +File.separator+filePath.getName)
     * @throws IOException
     */
    public static void addFileToJar(String jarPath, String filePath, String targetPath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        List<JarEntry> jarEntryList = new ArrayList<>();
        File jarFile1 = new File(jarPath);
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            jarEntryList.add(jarEntry);
        }
        File file1 = new File(jarFile1.getAbsolutePath() + ".back");
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(file1));
        for (JarEntry jarEntry : jarEntryList) {
            jarOutputStream.putNextEntry(jarEntry);
            InputStream inputStream = jarFile.getInputStream(jarEntry);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                jarOutputStream.write(bytes, 0, len);
            }
            inputStream.close();
        }
        JarEntry jarEntry = new JarEntry(targetPath + File.separator + file.getName());
        ZipEntry entry = jarFile.getEntry(jarEntry.getName());
        if (entry == null) {
            jarOutputStream.putNextEntry(jarEntry);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bytes)) != -1) {
                jarOutputStream.write(bytes, 0, len);
            }
            fileInputStream.close();
        }else {
            System.out.println("已经存在:"+jarEntry.getName());
        }
        jarFile.close();
        jarOutputStream.close();
        FileUtil.delFileGc(jarFile1);//删除原jar包
        file1.renameTo(jarFile1);

    }
    //删除jar包中的指定文件
    /**
     *
     * @param jarPath jar包的路径
     * @param targetPath 文件在jar包中的路径  (可以用压缩包工具查看具体路径格式)
     * @throws IOException
     */
    public static void delFileFromJar(String jarPath, String targetPath) throws IOException {
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        List<JarEntry> jarEntryList = new ArrayList<>();
        File jarFile1 = new File(jarPath);
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            jarEntryList.add(jarEntry);
        }
        File file1 = new File(jarFile1.getAbsolutePath() + ".back");
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(file1));
        for (JarEntry jarEntry : jarEntryList) {
            if (!jarEntry.getName().equals(targetPath)) {//不等于要删除的文件
                jarOutputStream.putNextEntry(jarEntry);
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = inputStream.read(bytes)) != -1) {
                    jarOutputStream.write(bytes, 0, len);
                }
                inputStream.close();
            }
        }
        jarFile.close();
        jarOutputStream.close();
        FileUtil.delFileGc(jarFile1);//
        file1.renameTo(jarFile1);

    }




}
