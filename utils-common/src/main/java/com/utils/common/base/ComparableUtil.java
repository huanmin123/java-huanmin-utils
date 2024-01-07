package com.utils.common.base;

public class ComparableUtil {

    //判断两个对象是否相等
    public static  <T extends Comparable <T>> boolean equals(T t1, T t2) {
        if (t1 == null && t2 == null) {
            return true;
        }
        if (t1 != null) {
            return t1.equals(t2);
        }
        return false;
    }
    //判断a是否大于b
    public static  <T extends Comparable <T>> boolean greater(T t1, T t2) {
        if (t1 == null && t2 == null) {
            return false;
        }
        if (t1 != null) {
            return t1.compareTo(t2) > 0;
        }
        return false;
    }
    //判断a是否小于b
    public static  <T extends Comparable <T>> boolean less(T t1, T t2) {
        if (t1 == null && t2 == null) {
            return false;
        }
        if (t1 != null) {
            return t1.compareTo(t2) < 0;
        }
        return false;
    }

    //判断a是否大于等于b
    public static  <T extends Comparable <T>> boolean greaterOrEquals(T t1, T t2) {
        if (t1 == null && t2 == null) {
            return true;
        }
        if (t1 != null) {
            return t1.compareTo(t2) >= 0;
        }
        return false;
    }
    //判断a是否小于等于b
    public static  <T extends Comparable <T>> boolean lessOrEquals(T t1, T t2) {
        if (t1 == null && t2 == null) {
            return true;
        }
        if (t1 != null) {
            return t1.compareTo(t2) <= 0;
        }
        return false;
    }

}
