package org.huanmin.test.utils.utils_tools.arithmetic;


import org.huanmin.utils.common.base.FakerData;
import org.huanmin.utils.arithmetic.sort.ShellSortUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ShellSortUtilTest {
    private  final Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void testTime() {
        int[] arr = FakerData.getDataInt(100000);

        long startTime = System.currentTimeMillis();
        //希尔排序
        ShellSortUtil.shellAscQuick(arr);
        long endTime = System.currentTimeMillis();

        System.out.printf("执行时长：%d 毫秒.", (endTime - startTime) );  //10万数据  15~20毫秒

    }


    @Test
    public void test1() {
        //给出无序数组
        int arr[] = {72, 6, 57, 88, 60, 42,-1,-55,-25,-25, 83, 73, 48, 85};

        //输出无序数组
        System.out.println(Arrays.toString(arr));
        //希尔排序
        ShellSortUtil.shellAsc(arr);
        //partition(arr,0,arr.length-1);
        //输出有序数组
        System.out.println(Arrays.toString(arr));
    }


    @Test
    public void test2() {
        //给出无序数组
        int arr[] = {72, 6, 57, 88, 60, 42,-1,-55,-25,-25, 83, 73, 48, 85};

        //输出无序数组
        System.out.println(Arrays.toString(arr));
        //希尔排序
        ShellSortUtil.shellDescQuick(arr);
        //partition(arr,0,arr.length-1);
        //输出有序数组
        System.out.println(Arrays.toString(arr));
    }


}
