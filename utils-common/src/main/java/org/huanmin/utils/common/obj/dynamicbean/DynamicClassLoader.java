package org.huanmin.utils.common.obj.dynamicbean;

import org.huanmin.utils.common.base.UniversalException;

import javax.tools.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class DynamicClassLoader {

    /**
     * 字符串class版本  装载字符串成为java可执行文件也就像xx.class文件,
     * 这个版本是没有java源文件而是直接生成class文件到包路径下,如果包路径不存在是会自动创建的
     * @param className className
     * @param javaCodes javaCodes
     * @return Class
     */
    public   static Class<?> compile(String className, String javaCodes) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null,                 null, null);
        StrSrcJavaObject srcObject = new StrSrcJavaObject(className, javaCodes);
        Iterable<? extends JavaFileObject> fileObjects = Collections.singletonList(srcObject);
        String flag = "-d";
        String outDir = "";
        try {
            File classPath = new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).toURI());
            outDir = classPath.getAbsolutePath() + File.separator;
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        Iterable<String> options = Arrays.asList(flag, outDir);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, fileObjects);
        boolean result = task.call();
        if (result) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                 UniversalException.logError(e);
            }
        }
        return null;
    }

    private static class StrSrcJavaObject extends SimpleJavaFileObject {
        private String content;
        StrSrcJavaObject(String name, String content) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }

}
