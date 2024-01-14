package org.huanmin.test.core.run;

import org.huanmin.test.core.annotation.SpringTest;
import org.huanmin.test.core.annotation.Test;
import org.huanmin.utils.common.base.UniversalException;
import org.huanmin.utils.common.obj.reflect.AnnotationUtil;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * ContextRefreshedEvent可以用于在Spring容器完全初始化之后执行一些操作。例如，我们可以在Spring容器初始化完毕之后执行一些需要依赖容器中的bean对象才能完成的操作。
 */
@Component
public class SpringTestRun  implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AnnotationConfigServletWebServerApplicationContext annotation = (AnnotationConfigServletWebServerApplicationContext) event.getSource();
        ApplicationContext applicationContext = event.getApplicationContext();
        List<Class> bean = getBean(annotation, SpringTest.class);
        System.out.println("==================================================================================================================================================");
        System.out.println("==================================================================================================================================================");
        System.out.println("==================================================================================================================================================");
        System.out.println("==================================================================================================================================================");
        bean.parallelStream().forEach(aClass -> {
            Object bean1 = applicationContext.getBean(aClass); // 获取bean
            //获取
            AnnotationUtil.getAnnotationsMethods(aClass, Test.class).forEach((method, annotation1)->{
                //判断方法必须是无参数的
                if (method.getParameterCount()!=0) {
                    throw  new UniversalException("@SpringTest注解的[{}]方法必须是无参数的",method.getName());
                }
                //并且方法必须是public
                if (!method.isAccessible()) {
                    throw  new UniversalException("@SpringTest注解的[{}]方法必须是public",method.getName());
                }
                //方法不能是静态的
                if (java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    throw  new UniversalException("@SpringTest注解的[{}]方法不能是静态的",method.getName());
                }
                try {
                    method.invoke(bean1);//执行方法
                } catch (Exception e) {
                     UniversalException.logError(e);
                }
            });

        });
        System.out.println("==================================================================================================================================================");
        System.out.println("==================================================================================================================================================");
        System.out.println("==================================================================================================================================================");
        System.out.println("==================================================================================================================================================");
    }
    //获取spring里所有注解的Bean的Class
    public List<Class> getBean(AnnotationConfigServletWebServerApplicationContext annotation,Class cl) {
        List<Class> classes=new ArrayList<>();
        String[] beanNamesForAnnotation = annotation.getBeanNamesForAnnotation(cl);
        for (String beanName : beanNamesForAnnotation) {
            Class<?> aClass = annotation.getBean(beanName).getClass();
            classes.add(aClass);
        }
        return  classes;
    }
}
