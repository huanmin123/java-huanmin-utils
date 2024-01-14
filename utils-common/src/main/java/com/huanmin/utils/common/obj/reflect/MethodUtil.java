package com.huanmin.utils.common.obj.reflect;


import com.huanmin.utils.common.base.UniversalException;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;

/**
 * 反射工具
 *
 * @Author: huanmin
 * @Date: 2022/6/19 21:49
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public final class MethodUtil {

    /**
     * 调用无参数方法
     *
     * @param method 方法对象
     * @param target 调用对象
     * @return 执行结果
     */
    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }

    /**
     * 调用指定对象的方法
     *
     * @param method 方法对象
     * @param target 调用对象
     * @param args   方法参数
     * @return 执行结果
     */
    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            makeAccessible(method);
            return method.invoke(target, args);
        } catch (Exception ex) {
            throw new IllegalStateException(String.format("执行%s.%s()方法错误!"
                    , target.getClass().getName(), method.getName()), ex);
        }
    }

    /**
     * 运行指定的方法
     *
     * @param clazz          需要操作的class类
     * @param methodName     需要运行的方法
     * @param parameterTypes 需要调用的方法的参数类型
     * @param args           给调用的方法赋值
     * @return 方法返回值
     */
    @SneakyThrows
    public static Object runMethod(Class clazz, String methodName, Class[] parameterTypes, Object[] args) {
        Object o = clazz.newInstance();
        //通过方法名和 参数个数+类型 能确认调用具体的方式
        Method method = clazz.getMethod(methodName, parameterTypes);
        makeAccessible(method);
        // 找到具体方法然后给方法传入参数
        Object invoke = method.invoke(o, args);

        return invoke;
    }

    //通过名称调用无惨方法
    @SneakyThrows
    public static Object runMethod(Class clazz, String methodName) {
        Object o = clazz.newInstance();
        //通过方法名和 参数个数+类型 能确认调用具体的方式
        Method method = clazz.getMethod(methodName);
        makeAccessible(method);
        // 找到具体方法然后给方法传入参数
        Object invoke = method.invoke(o, new Object[0]);
        return invoke;
    }

    @SneakyThrows
    public static Object runMethod(Object o, String methodName) {
        //通过方法名和 参数个数+类型 能确认调用具体的方式
        Method method = o.getClass().getMethod(methodName);
        makeAccessible(method);
        // 找到具体方法然后给方法传入参数
        Object invoke = method.invoke(o, new Object[0]);
        return invoke;
    }

    @SneakyThrows
    public static Object runMethod(Object o, String methodName, Class[] parameterTypes, Object[] args) {

        //通过方法名和 参数个数+类型 能确认调用具体的方式
        Method method = o.getClass().getMethod(methodName, parameterTypes);
        makeAccessible(method);
        // 找到具体方法然后给方法传入参数
        Object invoke = method.invoke(o, args);

        return invoke;
    }


    //运行指定类的所有公共无惨方法
    @SneakyThrows
    public static void runMethodAll(Class clazz) {
        Method[] methods = getMethodAll(clazz);
        Object o = clazz.newInstance();
        for (Method method : methods) {
            //判断是否为无参方法
            if (method.getParameterCount() == 0) {
                makeAccessible(method);
                method.invoke(o);
            }
        }
    }

    /**
     * 设置方法可见性
     *
     * @param method 方法
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 是否为equals方法
     *
     * @see Object#equals(Object)
     */
    public static boolean isEqualsMethod(Method method) {
        if (!"equals".equals(method.getName())) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        return (paramTypes.length == 1 && paramTypes[0] == Object.class);
    }

    /**
     * 是否为hashCode方法
     *
     * @see Object#hashCode()
     */
    public static boolean isHashCodeMethod(Method method) {
        return "hashCode".equals(method.getName()) && method.getParameterCount() == 0;
    }

    /**
     * 是否为Object的toString方法
     *
     * @see Object#toString()
     */
    public static boolean isToStringMethod(Method method) {
        return "toString".equals(method.getName()) && method.getParameterCount() == 0;
    }


    //获取指定类的所有公共方法
    public static Method[] getMethodAll(Class clazz) {
        return clazz.getDeclaredMethods();
    }


    //通过方法名获属性名称
    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else {
            if (!name.startsWith("get") && !name.startsWith("set")) {
                return null;
            }

            name = name.substring(3);
        }

        if (name.length() == 1 || name.length() > 1 && !Character.isUpperCase(name.charAt(1))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    //通过属性名称获方法名称 ,get,set,is
    public static String fieldToGetMethod(String name) {
        return "get" + name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }
    public static String fieldToSetMethod(String name) {
        return "set" + name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }
    public static String fieldToIsMethod(String name) {
        return "is" + name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }


}
