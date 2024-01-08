package com.utils.arithmetic.query;

import com.utils.common.base.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MaxMinBinarySearchUtil {
    public static <T extends Number> T getMax(T[] arr, int L, int R) {
        if (L == R) {
            return arr[L];
        }
        int mid = (L + R) / 2;
        T maxLeft = getMax(arr, L, mid);
        T maxRight = getMax(arr, mid + 1, R);
        return (T)NumberUtil.max(maxLeft, maxRight); // 返回两个数较大的一个
    }

    public static  <T extends Number>   T getMin(T[] arr,int L,int R) {
        if (L==R) {
            return arr[L];
        }
        int mid=(L+R)>>1;
        T minLeft=getMin(arr, L, mid);
        T minRight = getMin(arr, mid + 1, R);
        return (T)NumberUtil.min(minLeft, minRight);
    }
//=================================================下面是集合处理,和上面的逻辑一样==================================================================


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
