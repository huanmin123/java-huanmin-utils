package com.utils.common.base;


import com.utils.common.file.FileUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * @Description: 项目静态资源文件路径
 * @Author: huanmin
 * @Date: 2019/1/4
 */
public class ResourceFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResourceFileUtil.class);

    /**
     * 获取资源绝对路径
     *
     * @param relativePath 资源文件相对路径(相对于 (resources路径|classpath ),路径 + 文件名)
     *                     eg: "templates/pdf_export_demo.ftl"
     *                     eg: "/templates/pdf_export_demo.ftl"
     * @return
     * @throws FileNotFoundException
     */
    @SneakyThrows
    public static String getAbsolutePath(String relativePath)  {
        File file = getFile(relativePath);
        if(file==null){
            return  null;
        }
        return file.getAbsolutePath();
    }
    /**
     * 获取资源文件
     *
     * @param relativePath 资源文件相对路径(相对于 (resources路径|classpath) 路径,路径 + 文件名)
     *                   列: "templates/pdf_export_demo.ftl"
     *                  eg: "/templates/pdf_export_demo.ftl"
     * @return
     * @throws FileNotFoundException
     */
    @SneakyThrows
    public static File getFile(String relativePath)  {
        if (relativePath == null || relativePath.length() == 0) {
            return null;
        }
        relativePath= FileUtil.cutFirstSeparatorAdaptation(relativePath); //截取首个分割符
        File file = null;
        try {
            file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX
                    + relativePath);
        } catch (FileNotFoundException e) {
            System.out.println("没有这个文件");
        }
        return file;
    }

    /**
     * 获取资源父级目录
     *
     * @param relativePath 资源文件相对路径(相对于 (resources路径|classpath)路径,路径 + 文件名)
     *                     eg: "templates/pdf_export_demo.ftl"
     * @return
     * @throws FileNotFoundException
     */
    public static String getParent(String relativePath) throws FileNotFoundException {
        return getFile(relativePath).getParent();
    }

    /**
     * 获取资源文件名
     *
     * @param relativePath 资源文件相对路径(相对于 (resources路径|classpath)路径,路径 + 文件名)
     *                     eg: "templates/pdf_export_demo.ftl"
     * @return
     * @throws FileNotFoundException
     */
    public static String getFileName(String relativePath) throws FileNotFoundException {
        return getFile(relativePath).getName();
    }


    /**
     * 获取resources路径|classpath  目录下的指定文件或者目录的路径  ,没有那么就自动创建 (会创建空文件,和空目录(支持多级))
     * ResourceFileUtil.getAbsoluteFilePathAndCreate("/test");
     * ResourceFileUtil.getAbsoluteFilePathAndCreate("/test/a/a.txt");
     * @param path
     * @return
     */
    public  static  String getAbsoluteFileOrDirPathAndCreate(String path) {
        if(path.length()<1){
            throw  new IllegalArgumentException("path长度不够 : path>1");
        }
        boolean file1 = FileUtil.isFile(path);
        if(file1){
            return  getAbsoluteFilePathAndCreate(path);
        }

        return  getAbsoluteDirPathAndCreate(path);
    }
    public  static  File getAbsoluteFileOrDirPathAndCreateFile(String path) {
        return  new File(getAbsoluteFileOrDirPathAndCreate(path));
    }



    //获取resources路径|classpath 根目录的路径   D:\\project\\utils\\common-utils\\target\\classes或者test-classes,根据代码运行的环境
    public  static  String getAbsolutePath() {
        try {
            String path = ClassLoader.getSystemResource( "/").getPath();
            if (path == null || "".equals(path)) {
                return getThreadProjectClassPath();
            }
            return path+File.separator ;
        } catch (Exception ignored) {
        }
        return getThreadProjectClassPath();
    }













    private static String getThreadProjectClassPath() {
        String outDir = "";
        try {
            File classPath = new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).toURI());
            outDir = classPath.getAbsolutePath()+File.separator ;
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        return outDir;
    }


    //获取resources路径|classpath  目录下的指定目录的路径  ,没有那么就自动创建
    //ResourceFileUtil.getAbsoluteDirPathAndCreate("/test");
    // classes/test/
    private  static  String getAbsoluteDirPathAndCreate(String dir) {
        dir = FileUtil.cutFirstSeparatorAdaptation(dir);
        dir = FileUtil.cutLastSeparatorAdaptation(dir);
        String path=null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath() + dir+File.separator;
            //判断  路径是否存在  如果没有此目录那么就创建一个
            File file = new File(path);
            if (!file.exists()) {
                //创建该文件夹
                file.mkdirs();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return   path.substring(1);
    }
    //获取resources路径|classpath  目录下的指定文件的路径  ,没有那么就自动创建
    //ResourceFileUtil.getAbsoluteFilePathAndCreate("/test.txt");
    //ResourceFileUtil.getAbsoluteFilePathAndCreate("/test/a/a.txt");
    private   static  String getAbsoluteFilePathAndCreate(String filePath) {
        String replace =null;
        replace= FileUtil.cutFirstSeparatorAdaptation(filePath); //截取首个分割符

        String path=null;
        try {
            if(replace.length()==0){ // ""  或者 /
                path = ResourceUtils.getURL("classpath:").getPath() ;
            }else{
                path = ResourceUtils.getURL("classpath:").getPath() +  replace;
            }

            File file = new File(path);
            //1.先判断路径是否存在,如果不存在那么就创建
            if (!file.exists()){
                //2. 判断路径是否有点如果有点,并且点后面的长度不能超过6字符,那么代表是文件类型 否则代表是目录
                boolean pd = FileUtil.isFile(path);
                //是目录
                if (!pd){
                    //创建文件夹多级创建
                    file.mkdirs();
                } else { //是文件
                    //判断文件的上级目录是否存在,如果不存在那么就需要先创建上级目录然后在创建文件
                    File fileParent = file.getParentFile();
                    if (!fileParent.exists()) {
                        //创建文件夹
                        fileParent.mkdirs();
                    }
                    //创建文件
                    file.createNewFile();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert path != null;
        return   path.substring(1);
    }



}
