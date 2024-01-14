package org.huanmin.utils.common.obj.dynamicbean;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DynamicCompiler {
    /**
     * 动态生成类,内存版,  不会生成类文件,不会生成class文件而是直接编译到内存里
     * @param fullClassName 全路径的类名
     * @param javaCode      java代码
     * @return 目标类
     */
    public static Class<?> compileToClass(String fullClassName, String javaCode) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        MyFileManager fileManager = new MyFileManager(compiler.getStandardFileManager(diagnostics, null, null));

        List<JavaFileObject> jfiles = new ArrayList<>();
        jfiles.add(new MySimpleJavaFileObject(fullClassName, javaCode));

        List<String> options = new ArrayList<>();
        options.add("-encoding");
        options.add("UTF-8");


        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jfiles);
        boolean success = task.call();
        if (success) {
            MyJavaClassFileObject javaClassObject = fileManager.getJavaClassObject();
            MyClassLoader dynamicClassLoader = new MyClassLoader();
            //加载至内存
            return dynamicClassLoader.loadClass(fullClassName, javaClassObject);
        } else {
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                String error = compileError(diagnostic);
                throw new RuntimeException(error);
            }
            throw new RuntimeException("compile error");
        }
    }

    
    

    private static String compileError(Diagnostic diagnostic) {
        StringBuilder res = new StringBuilder();
        res.append("LineNumber:[").append(diagnostic.getLineNumber()).append("]\n");
        res.append("ColumnNumber:[").append(diagnostic.getColumnNumber()).append("]\n");
        res.append("Message:[").append(diagnostic.getMessage(null)).append("]\n");
        return res.toString();
    }


    private static class MyClassLoader extends ClassLoader {

        public Class loadClass(String fullName, MyJavaClassFileObject javaClassObject) {
            byte[] classData = javaClassObject.getBytes();
            return this.defineClass(fullName, classData, 0, classData.length);
        }
    }


    private static class MyFileManager extends ForwardingJavaFileManager {

        private MyJavaClassFileObject javaClassObject;

        protected MyFileManager(StandardJavaFileManager standardJavaFileManager) {
            super(standardJavaFileManager);
        }

        public MyJavaClassFileObject getJavaClassObject(){
            return javaClassObject;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            this.javaClassObject = new MyJavaClassFileObject(className, kind);
            return javaClassObject;
        }
    }


    //这里需要重写openOutStream()方法，不输出字节码到文件，而是直接保存在一个输出流中。
    private static class MyJavaClassFileObject extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        public MyJavaClassFileObject(String name, Kind kind) {
            super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        }

        public byte[] getBytes() {
            return outputStream.toByteArray();
        }

        //编译时候会调用openOutputStream获取输出流,并写数据
        @Override
        public OutputStream openOutputStream() throws IOException {
            return outputStream;
        }
    }


    private static class MySimpleJavaFileObject extends SimpleJavaFileObject {

        private String contents;
        private String className;

        public MySimpleJavaFileObject(String className, String contents) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.className = className;
            this.contents = contents;
        }

        @Override
        public CharSequence getCharContent(boolean ignoredEncodingErrors) throws IOException {
            return contents;
        }
    }

}