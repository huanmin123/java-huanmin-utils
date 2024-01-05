package com.utils.common.base;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 判断各种类型是否为空
 * @author 胡安民
 * @Description:  判断是否为空的工具栏，如果不使用StringUtils的jdk的工具类，可以自行封装
 *
 */
public class NullUtils {


    /**
     * 判断字符串不为空  不为空 返回true
     * @param str
     * @return
     */
    public static boolean notEmpty(String str){
        return str != null && !"".equals(str.trim())&&!"[]".equals(str)&&!"{}".equals(str);
    }
    //判断多个字符串是否为空,都不为空返回true
    public static boolean notEmptys(String... str){
        for (String s : str) {
           if(!notEmpty(s)){
               return false;
           }
        }
        return true;
    }

   
    

    /**
     * 判断字符串为空  为空返回 true
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || str.length() == 0||"".equals(str.trim());
    }
    //判断多个字符串是否为空,都是空返回true
    public static boolean isEmptys(String... str){
        for (String s : str) {
            if(!isEmpty(s)){
                return false;
            }
        }
        return true;
    }




    /**
     * 单例集合  判断是否为空  不为空 返回true
     * @param collection 使用泛型
     * @return
     */
    public static <T> boolean notEmpty(Collection<T> collection){
        if(collection != null){
            Iterator<T> iterator = collection.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next != null) {
                    return true;
                }
            }
        }
        return false;
    }
    //判断多个集合是否为空, 都不为空返回true
    public static <T> boolean notEmptys(Collection<T>... collection){
        for (Collection s : collection) {
            if(!notEmpty(s)){
                return false;
            }
        }
        return true;
    }
    /**
     *   Object类型 判断是否为空 不为空 返回true
     * @param o
     * @return
     */
    public static boolean notEmpty(Object o){
        return o != null && !"".equals(o) && !"null".equals(o);
    }
    //判断对象是否为空,如果为空返回默认值
    public static  <T> T notEmptyElse(T obj, T defaultObj) {
        return (obj != null) ? obj : defaultObj;
    }

    /**
     * map集合判断是否为空  不为空 返回true
     * @param map 使用泛型，可以传递不同的类型参数
     * @return
     */
    public static <T> boolean notEmpty(Map<T, T> map){
        return map != null && !map.isEmpty();
    }


    /**
     * byte类型数组判断是否为空  不为空 返回true
     * @param t
     * @return
     */
    public static boolean notEmpty(byte[] t){
        return t != null && t.length > 0;
    }

    /**
     * short类型数组 判断是否为空  不为空 返回true
     * @param t
     * @return
     */
    public static boolean notEmpty(short[] t){
        return t != null && t.length > 0;
    }

    /**
     * int类型数组  判断是否为空  不为空 返回true
     * @param t 可以是int,
     * @return
     */
    public static boolean notEmpty(int[] t){
        return t != null && t.length > 0;
    }

    /**
     * long类型数组  判断是否为空 不为空 返回true
     * @param t
     * @return
     */
    public static boolean notEmpty(long[] t){
        return t != null && t.length > 0;
    }

    /**
     * String类型的数组 判断是否为空 不为空 返回true
     * @param t
     * @return
     */
    public static boolean notEmpty(String[] t){
        return t != null && t.length > 0;
    }

    /**
     * Object类型数组 判断是否为空 不为空 返回true
     * @param t
     * @return
     */
    public static boolean notEmpty(Object[] t){
        return t != null && t.length > 0;
    }



    /**
     *   Object类型 判断是否为空   (绝对判断)

     */
    public static boolean isBlank(Object obj) {
        boolean blank = true;
        if (obj != null) {
            if (obj instanceof String) {
                if (!obj.equals("")) {
                    blank = false;
                }
            } else if (obj instanceof List) {
                if (!((List<?>) obj).isEmpty()) {
                    blank = false;
                }
            } else if (obj instanceof Map) {
                if (!((Map<?, ?>) obj).isEmpty()) {
                    blank = false;
                }
            } else if (obj instanceof Collection) {
                if (!((Collection<?>) obj).isEmpty()) {
                    blank = false;
                }
            } else if (obj instanceof Long || obj instanceof Double || obj instanceof Float || obj instanceof Integer
                    || obj instanceof Float || obj instanceof Integer) {
                if (!obj.toString().equals("0")) {
                    blank = false;
                }
            } else {
                blank = false;
            }
        }
        return blank;
    }
    


}