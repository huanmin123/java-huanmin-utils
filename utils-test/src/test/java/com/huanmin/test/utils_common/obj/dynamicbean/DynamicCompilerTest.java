package com.huanmin.test.utils_common.obj.dynamicbean;

import com.utils.common.obj.dynamicbean.DynamicBeanFile;
import com.utils.common.obj.dynamicbean.DynamicBeanFileJavaPath;
import com.utils.common.obj.dynamicbean.DynamicClassLoader;
import com.utils.common.obj.dynamicbean.DynamicCompiler;
import com.utils.common.obj.reflect.MethodUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicCompilerTest {
    private static final Logger logger = LoggerFactory.getLogger(DynamicCompilerTest.class);
    //动态生成类,内存版(推荐)
    @Test
    public void test() throws Exception {
        String method = "show";
        String codes =
                "package com.loginsimpl;" +
                "import java.io.File;" +
                "public class TestB1 {" +
                "public  void  show() {\n" +
                "       System.out.println(\"好\");\n" +
                "    }" +
                "}";

        Class clazz = DynamicCompiler.compileToClass("com.loginsimpl.TestB1",codes );
        MethodUtil.runMethod(clazz,"show");
        

    }
    //字符串class版本
    @Test
    public void test11() throws Exception {
        String method = "show";
        String codes =
                "package com.loginsimpl;" +
                "import java.io.File;" +
                "public class TestB2 {" +
                "public  void  show() {\n" +
                "       System.out.println(\"好\");\n" +
                "    }" +
                "}";

        Class clazz = DynamicClassLoader.compile("com.loginsimpl.TestB2",codes );
        MethodUtil.runMethod(clazz,"show");


    }
    //字符文件版
    @Test
    public void test1() throws Exception {
        String method = "show";
            //包会自动创建的在classes目录里
        String codes =
                "package com.obj1;"+
                "import java.io.File;" +
                "public class TestB1 {" +
                "   public  void  show() {\n" +
                "       System.out.println(\"好\");\n" +
                "    }" +
                "}";

        Class<?> clazz = DynamicBeanFile.loadBean(codes,"com.obj1.TestB1");
        MethodUtil.runMethod(clazz,"show");


    }



    //文件版
    @Test
    public void show() {
        //注意文件内 package要和fullBeanName一致
        Class aClass = DynamicBeanFileJavaPath.loadBean("com.obj.dynamicbean.BeanAA", "D:\\project\\utils\\common-utils\\src\\test\\java\\com\\obj\\dynamicbean\\BeanAA.java");
        Integer show1 = (Integer) MethodUtil.runMethod(aClass, "show1", new Class[]{int.class}, new Object[]{1111});
       System.out.println("show"+show1);
    }



}
