package com.utils.common.obj.reflect;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtil {

    //获取类名称
    public static  String getClassName(Class c){
        String name = c.getName();
        int i = name.lastIndexOf('.');
        if(i==-1){
            return  null;
        }
        return name.substring(i+1,name.length());
    }


    @SneakyThrows
    public static   Object newObj(Class clazz) {
        Object o = clazz.getDeclaredConstructor().newInstance();
        // 生成对象
        return o;
    }

    //通过构造函数创建对象
    @SneakyThrows
    public static   Object getConstructionObj(Class clazz,Class[] parameterTypes,Object[] args) {
        //通过类形列表确认要创建的构造器对象
        Constructor con = clazz.getDeclaredConstructor(parameterTypes);
        //给构指定的造函数赋值
        Object newObj = con.newInstance(args);
        // 生成对象
        return  newObj;
    }


    //拿到类内部的泛型,的类型
    public static <T> Class<T> deSerializable(Class<T> clazz, int index) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[index];
        }
        throw new RuntimeException();
    }


}
