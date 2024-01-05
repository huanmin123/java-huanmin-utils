package com.utils.common.obj.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class ProxyCglibInstance {


    public static <T> T getObject(Class<T> target, MethodInterceptor invocationHandler) {
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target);
        //3.设置回调函数
        en.setCallback(invocationHandler);
        //4.创建子类(代理对象)
        return (T)en.create();
    }

    public static <T> T getObject(Object target, MethodInterceptor invocationHandler) {
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target.getClass());
        //3.设置回调函数
        en.setCallback(invocationHandler);
        //4.创建子类(代理对象)
        return (T)en.create();
    }

}
