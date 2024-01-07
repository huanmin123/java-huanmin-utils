package com.utils.common.container;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * 数组基础通用方法
 * 8大数据类型通用方法 ,不能是集合List Set...
 */
public class BasicArrayUtil {


    // 基本数据类型数组反转
    public static void reverse(Object array) {
        int length = Array.getLength(array);
        Object temp = null;// 临时变量
        for (int i = 0; i < length / 2; i++) {
            temp =Array.get(array,i) ;
            Array.set(array,i,Array.get(array,length - 1 - i));
            Array.set(array,length - 1 - i,temp);
        }
    }
    /**
     * 判断数组中是否存在指定元素
     *
     * @param arrays 数组
     * @param val    校验的元素
     * @param <T>    数组原始类型
     * @return 是否存在
     */
    public static <T> boolean contains(T[] arrays, T val) {
        if (arrays == null) {
            return false;
        }
        for (T t : arrays) {
            if (Objects.equals(t, val)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 是否为空
     *
     * @param array 数组类型
     * @param <T>   泛型
     * @return true:为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }
}
