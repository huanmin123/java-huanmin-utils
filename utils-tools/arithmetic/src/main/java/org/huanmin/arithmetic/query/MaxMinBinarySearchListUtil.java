package org.huanmin.arithmetic.query;

import com.utils.common.base.NumberUtil;

import java.util.List;

public class MaxMinBinarySearchListUtil {

    public static <T extends Number> T getMax(List<T> arr, int L, int R) {
        if (L == R) {
            return arr.get(L);
        }
        int mid = (L + R) / 2;
        T maxLeft = getMax(arr, L, mid);
        T maxRight = getMax(arr, mid + 1, R);
        return (T)NumberUtil.max(maxLeft, maxRight); // 返回两个数较大的一个
    }

    public static  <T extends Number>   T getMin(List<T> arr,int L,int R) {
        if (L==R) {
            return arr.get(L);
        }
        int mid=(L+R)>>1;
        T minLeft=getMin(arr, L, mid);
        T minRight = getMin(arr, mid + 1, R);
        return (T)NumberUtil.min(minLeft, minRight);
    }



}
