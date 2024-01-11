package com.huanmin.test.utils.utils_tools.arithmetic;


import com.huanmin.utils.arithmetic.query.MaxMinBinarySearchUtil;
import org.junit.Test;

public class MaxMinBinarySearchUtilTest  {

    @Test
    public  void getMax(){
        Integer[] arr = { 1, 8, 9, 20, 5, -6, 2, 5, 8, 0 };
        int i = MaxMinBinarySearchUtil.getMax(arr, 0, arr.length - 1);
        System.out.println("最大值"+i);
    }

    @Test
    public  void getMin(){
        Integer[] arr = { 1, 8, 9, 20, 5, -6, 2, 5, 8, 0 };
        int j=MaxMinBinarySearchUtil.getMin(arr, 0, arr.length - 1);
        System.out.println("最小值"+j);
    }
}
