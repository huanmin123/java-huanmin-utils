package com.huanmin.utils.common.file;


import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Paths;

/**
 * 项目静态资源文件路径
 * @Author: huanmin
 * @Date: 2019/1/4
 */
public class ResourceFileUtil {


    //获取资源文件流, 项目打包后也能拿到jar包里的文件, 而通过路径是拿不到的
    public static InputStream getFileStream(String path) throws IOException {
        path = FileUtil.cutFirstSeparatorAdaptation(path);
        ClassPathResource classPathResource = new ClassPathResource(path);
        return classPathResource.getInputStream();
    }

    //获取当前项目的绝对路径 ,F:\\java-project\\java-huanmin-utils\\utils-test
    public static String getCurrentProjectAbsolutePath() {
        String userDir = System.getProperty("user.dir");
        return userDir;
    }

    //获取当前项目resources的绝对路径 ,F:\\java-project\\java-huanmin-utils\\utils-test\\src\\main\\resources
    public static String getCurrentProjectResourcesAbsolutePath() {
        String userDir = System.getProperty("user.dir");
        String path = userDir + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return path;
    }

    //获取当前项目resources下的文件的绝对路径 ,F:\\java-project\\java-huanmin-utils\\utils-test\\src\\main\\resources\\database.properties
    public static String getCurrentProjectResourcesAbsolutePath(String fileName) {
        String userDir = System.getProperty("user.dir");
        fileName = FileUtil.cutFirstSeparatorAdaptation(fileName);
        fileName = FileUtil.cutLastSeparatorAdaptation(fileName);
        String s = userDir + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + fileName;
        return Paths.get(s).toString();
    }

    public static File getCurrentProjectResourcesAbsoluteFile(String fileName) {
        String currentProjectResourcesFileAbsolutePath = getCurrentProjectResourcesAbsolutePath(fileName);
        return new File(currentProjectResourcesFileAbsolutePath);
    }
    //获取classpath下的文件的绝对路径 ,F:\\java-project\\java-huanmin-utils\\utils-test\\target\\classes\\abc\\database.properties
    // 只用于IDEA测试, 项目打包后, 这种方式就不能用了
    public   static  String getCurrentProjectTargetClassAbsolutePath(String filePath) {
        String userDir = System.getProperty("user.dir");
        filePath = FileUtil.cutFirstSeparatorAdaptation(filePath);
        filePath = FileUtil.cutLastSeparatorAdaptation(filePath);
        String s = userDir + File.separator + "target" + File.separator + "classes" + File.separator + filePath;
        return Paths.get(s).toString();
    }
    public   static  String getCurrentProjectTargetTestClassAbsolutePath(String filePath) {
        String userDir = System.getProperty("user.dir");
        filePath = FileUtil.cutFirstSeparatorAdaptation(filePath);
        filePath = FileUtil.cutLastSeparatorAdaptation(filePath);
        String s = userDir + File.separator + "target" + File.separator + "test-classes" + File.separator + filePath;
        return Paths.get(s).toString();
    }

}
