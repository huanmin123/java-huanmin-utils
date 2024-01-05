package com.utils.common.obj.dynamicbean;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;

@Slf4j
public class DynamicBeanFile   {


    /**
     * 字符串文件版   通过java字符串生成java源文件然后编译成class类  ,   生成文件的位置->缓存目录,不同系统路径都不一样
     * @param javaCode     类的字符串代码
     * @param fullBeanName   类的全路径名称  : com.obj1.TestB1
     * @return
     */
    @SneakyThrows
    public static   Class loadBean(String javaCode, String fullBeanName)  {
        String[] split = fullBeanName.split("\\.");
        String filename = split[split.length - 1];
        log.info("loadBean，compile {} start",filename);
        File file = createTempFileWithFileNameAndContent(filename, ".java",javaCode.getBytes());
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null, file.getAbsolutePath());
        if(result==0){
            log.info("{} {}",fullBeanName,"-编译成功");
        }else{
            throw new Exception(String.format("动态编译失败，className %s", fullBeanName));
        }

        log.info("loadBean，loadClass {} start, to {}",fullBeanName,file.getParent());
        //装载class到jvm内存中
        URL[] urls = new URL[]{new URL("file:/"+file.getParent()+"/")};
        URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        Class c = loader.loadClass(fullBeanName);
        log.info("loadBean，loadClass {} end",fullBeanName);

       return c;

    }



    public static File createTempFileWithFileNameAndContent(String beanName, String suffix, byte[] content) throws IOException {
        String tempDir=System.getProperty("java.io.tmpdir"); //缓存目录,不同系统路径都不一样
        File file = new File(tempDir+"/"+beanName+suffix);
        OutputStream os = new FileOutputStream(file);
        os.write(content, 0, content.length);
        os.flush();
        os.close();
        return file;
    }


}
