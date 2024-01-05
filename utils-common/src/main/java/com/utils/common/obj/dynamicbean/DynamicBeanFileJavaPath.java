package com.utils.common.obj.dynamicbean;

import com.utils.common.file.FileSortUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
@Slf4j
public class DynamicBeanFileJavaPath {
    /**
     *  文件版
     * @param fullBeanName  类的全路径名称  : com.obj1.TestB1
     * @param javaPath  本地java文件
     */
    @SneakyThrows
    public  static  Class   loadBean(String fullBeanName ,String javaPath) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        File file=new File(javaPath);
        int result = compiler.run(null, null, null, file.getAbsolutePath());
        System.out.println("result = " + result);
        if(result==0){
            log.info("{} {}",fullBeanName,"-编译成功");
        }else{
            throw new Exception(String.format("动态编译失败，className %s", fullBeanName));
        }
        //装载class到jvm内存中
        URL[] urls = new URL[]{new URL("file:/"+file.getParent()+"/")};
        //获取系统的ClassLoader
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader(); //获取当前线程上下文的ClassLoader
        URLClassLoader loader = new URLClassLoader(urls, contextClassLoader);
        Class c = loader.loadClass(fullBeanName);

        //因为class已经装载到jvm中了那么本地的class文件也就没用用了,我们可以删除class文件 (可选择)
        String deleClass= javaPath.substring(0, javaPath.indexOf(".")) + ".class";
        File delefile=new File(deleClass);
        delefile.delete();
        return  c;
    }



}
