package org.huanmin.utils.common.container;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;

/**
 * 数组基础通用方法
 * 8大数据类型通用方法 ,不能是集合List Set...
 */
public class ArrayUtil {


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




    //判断List是否是有序的, 采用的策略是尽可能的少比较
    public static <T extends Comparable<T>> boolean isOrder(T[] data) {
        //判断开头中间结尾是否是无序的
        if (data[0].compareTo(data[1]) > 0 || data[data.length / 2 - 1].compareTo(data[data.length / 2]) > 0 || data[data.length - 2].compareTo(data[data.length - 1]) > 0) {
            return false;
        }

        //100数据以内挨个比较
        if (data.length <= 100) {
            return isOrder(data, 0, data.length- 1);
        } else if (data.length <= 1000) {//1000数据取开头中间结尾各50数据比较
            return isOrder(data, 0, 50) && isOrder(data, data.length / 2 - 25, data.length / 2 + 25) && isOrder(data, data.length - 50, data.length- 1);
        } else if (data.length<= 10000) {//10000数据取,随机取10个50个范围数据比较
            for (int i = 0; i < 10; i++) {
                int start = (int) (Math.random() * (data.length - 50));
                if (!isOrder(data, start, start + 50)) {
                    return false;
                }
            }
            return true;
        }else {//10000以上数据取,每隔1000个数据取50个范围数据比较
            for (int i = 0; i < data.length; i+=1000) {
                if (!isOrder(data, i, i + 50)) {
                    return false;
                }
            }
            return true;
        }

    }

    //随机判断几个范围内的数是否是有序的
    private static <T extends Comparable<T>> boolean isOrder(T[] data, int start, int end) {
        int length = data.length;
        if (start < 0 || end > length - 1) {
            throw new RuntimeException("start or end is error");
        }
        for (int i = start; i < end; i++) {
            if (data[i].compareTo(data[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }








}
