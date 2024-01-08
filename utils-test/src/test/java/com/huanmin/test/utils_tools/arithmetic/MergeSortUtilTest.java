package com.huanmin.test.utils_tools.arithmetic;


import com.utils.common.base.FakerData;
import com.utils.common.container.ArrayUtil;
import com.utils.arithmetic.sort.MergeSortUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MergeSortUtilTest {
    private  final Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void testTime() {
        Integer[] arr = FakerData.getDataIntNumber(10000,9);

        long startTime = System.currentTimeMillis();
        //归并排序
        MergeSortUtil.mergeSort(arr,"asc");
        long endTime = System.currentTimeMillis();

        System.out.printf("执行时长：%d 毫秒.", (endTime - startTime) );  //1万数据  80~90毫秒
        boolean order = ArrayUtil.isOrder(arr);
        System.out.println(order);
    }


    @Test
    public void test1() {
        //给出无序数组
        Integer arr[] = {72, 6, 57, 88, 60, 42,-1,-55,-25,-25, 83, 73, 48, 85};

        //输出无序数组
        System.out.println(Arrays.toString(arr));
        //快速排序
        MergeSortUtil.mergeSort(arr,"asc");
        //partition(arr,0,arr.length-1);
        //输出有序数组
        System.out.println(Arrays.toString(arr));
    }


    @Test
    public void test2() {
        //给出无序数组
        Integer arr[] = {72, 6, 57, 88, 60, 42,-1,-55,-25,-25, 83, 73, 48, 85};

        //输出无序数组
        System.out.println(Arrays.toString(arr));
        //快速排序
        MergeSortUtil.mergeSort(arr,"desc");
        //partition(arr,0,arr.length-1);
        //输出有序数组
        System.out.println(Arrays.toString(arr));
    }
}
