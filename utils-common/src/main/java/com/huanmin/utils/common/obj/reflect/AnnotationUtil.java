package com.huanmin.utils.common.obj.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationUtil {

    //获取类中全部注解和注解对应的方法
    public static   Map<String, Map<Object, Annotation[]>> getAnnotationAll(Class clazz) {
        Map<String, Map<Object, Annotation[]>> map=new HashMap<>();
        //类
        Map<Object, Annotation[]> mapClass=new HashMap<>();
        mapClass.put(clazz,clazz.getAnnotations());
        map.put("class",mapClass);

        //属性
        Map<Object, Annotation[]> mapFields=new HashMap<>();
        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);
            mapFields.put(declaredField,declaredField.getAnnotations());

        }
        map.put("fields",mapFields);
        //方法
        Map<Object, Annotation[]> mapMethods=new HashMap<>();
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            mapMethods.put(declaredMethod,declaredMethod.getAnnotations());
        }
        map.put("methods",mapMethods);
        return   map;
    }

    //获取指定范围(class,fields,methods)内是否包含指定注解,返回范围对象和注解对象
    public static    Map<Object, Annotation> getAnnotations(Class clazz,Class annotationType,String scope) {
        Map<Object, Annotation> result=new HashMap<>();
        Map<String, Map<Object, Annotation[]>> annotationAll = getAnnotationAll(clazz);
        Map<Object, Annotation[]> objectMap = annotationAll.get(scope);
        for (Map.Entry<Object, Annotation[]> objectEntry : objectMap.entrySet()) {
            for (Annotation annotation : objectEntry.getValue()) {
                if (annotation.annotationType()==annotationType) {
                    result.put(objectEntry.getKey(),annotation);
                    break;
                }
            }
        }
        return  result;
    }

    //获取类指定注解
    public static    Map<Class, Annotation> getAnnotationsClass(Class clazz,Class annotationType) {
        Map<Class, Annotation> map=new HashMap<>();
        Map<Object, Annotation> aClass = getAnnotations(clazz, annotationType, "class");
        for (Map.Entry<Object, Annotation> objectAnnotationEntry : aClass.entrySet()) {
            map.put( (Class)objectAnnotationEntry.getKey(),objectAnnotationEntry.getValue());
        }
        return  map;
    }
    //获取类中方法指定注解
    public static Map<Method, Annotation> getAnnotationsMethods(Class clazz, Class annotationType) {
        Map<Method, Annotation> map = new HashMap<>();
        Map<Object, Annotation> aClass = AnnotationUtil.getAnnotations(clazz, annotationType, "methods");
        for (Map.Entry<Object, Annotation> objectAnnotationEntry : aClass.entrySet()) {
            map.put((Method) objectAnnotationEntry.getKey(), objectAnnotationEntry.getValue());
        }
        return map;
    }
    //获取类中属性指定注解
    public static Map<Field, Annotation> getAnnotationsFields(Class clazz, Class annotationType) {
        Map<Field, Annotation> map=new HashMap<>();
        Map<Object, Annotation> aClass = AnnotationUtil.getAnnotations(clazz, annotationType, "fields");
        for (Map.Entry<Object, Annotation> objectAnnotationEntry : aClass.entrySet()) {
            map.put( (Field)objectAnnotationEntry.getKey(),objectAnnotationEntry.getValue());
        }
        return  map;
    }

}
