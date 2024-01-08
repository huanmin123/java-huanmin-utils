package com.huanmin.test.utils_tools.arithmetic;

import com.utils.common.base.FakerData;
import org.huanmin.arithmetic.sort.InsertSortUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class InsertSortUtilTest {
    private  final Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void testTime() {
        Integer[] arr = FakerData.getDataIntNumber(10000000,9);

        long startTime = System.currentTimeMillis();
        //插入排序
        InsertSortUtil.insertSort(arr,"asc");
        long endTime = System.currentTimeMillis();

        System.out.printf("执行时长：%d 毫秒.", (endTime - startTime) );  //1万数据  10~20毫秒

    }


    @Test
    public void test1() {
        //给出无序数组
        Integer arr[] = {72, 6, 57, 88, 60, 42,-1,-55,-25,-25, 83, 73, 48, 85};

        //输出无序数组
        System.out.println(Arrays.toString(arr));
        //快速排序
        InsertSortUtil.insertSort(arr,"asc");
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
        InsertSortUtil.insertSort(arr,"desc");
        //partition(arr,0,arr.length-1);
        //输出有序数组
        System.out.println(Arrays.toString(arr));
    }


}
