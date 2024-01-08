package com.utils.timerwheel;


import com.utils.common.base.UniversalException;
import com.utils.common.obj.reflect.AnnotationUtil;
import com.utils.common.obj.reflect.PackageUtil;
import com.utils.timerwheel.timerannotation.TimerResolver;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/30 8:56
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class TimerContext {
    private static Timer timer=new Timer();//饿汉式单例
    private TimerContext() {
    }

    public static Timer getTimer() {
        return timer;
    }
    public static void init(Class mainClass) throws InstantiationException, IllegalAccessException {
        beanInit(mainClass);//初始化定时器的bean
    }
    //将所有bean初始化到容器中(只支持空参构造)
    private static void beanInit(Class mainClass) throws InstantiationException, IllegalAccessException {
        //扫描包下的所有类
        Package aPackage = mainClass.getPackage();
        //获取包下的所有类
        Set<Class<?>> classes = PackageUtil.getClasses(aPackage.getName());
        //遍历所有类,获取指定Bean注解的类
        for (Class<?> aClass : classes) {
            Map<Class, Annotation> annotationsClass = AnnotationUtil.getAnnotationsClass(aClass, TimerWheel.class);
            if (!annotationsClass.isEmpty()) {
                Object o = aClass.newInstance();
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    try {
                        TimerResolver timerResolver = method.getAnnotation(TimerResolver.class);
                        if (timerResolver==null) {
                            continue;
                        }
                        String key=aClass.getName()+"."+method.getName();
                        String resolver = timerResolver.resolver();
                        if (StringUtils.isEmpty(resolver)) {
                            continue;
                        }
                        timer.add_timer_sask( resolver,key,(data)->{
                            Object invoke=null;
                            try {
                                invoke = method.invoke(o);//执行方法
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                            return invoke;
                        }, null);
                    } catch (Exception e) {
                         UniversalException.logError(e);
                    }
                }
            }
        }

    }

}

