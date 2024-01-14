package org.huanmin.utils.common.obj.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyJdkInstance {

    public static <T> T getObject(Class<T> interfaceType,InvocationHandler invocationHandler) {
        return(T) Proxy.newProxyInstance(interfaceType.getClassLoader(),  new Class[] {interfaceType} ,invocationHandler);
    }
    public static <T> T getObject(Object o,InvocationHandler invocationHandler) {
        ClassLoader classLoader = o.getClass().getClassLoader();//类加载器。
        Class<?>[] interfaces = o.getClass().getInterfaces();//对象所有接口
        return (T) Proxy.newProxyInstance(classLoader, interfaces ,invocationHandler);
    }

}
