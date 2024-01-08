package com.huanmin.test.utils_tools.arithmetic;


import com.utils.common.base.FakerData;
import com.utils.common.container.ArrayUtil;
import org.huanmin.arithmetic.sort.BubbleSortUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class BubbleSortUtilTest {

    @Test
    public void testTime() {
        Integer[] dataInt = FakerData.getDataIntNumber(10000,9);
        long startTime = System.currentTimeMillis();
        //冒泡排序
        BubbleSortUtil.bubbleSortAsc(dataInt);
        long endTime = System.currentTimeMillis();

        System.out.printf("执行时长：%d 毫秒.", (endTime - startTime) );  //1万数据排序 130~200毫秒左右
        boolean order = ArrayUtil.isOrder(dataInt);
        System.out.println(order);

    }


    @Test
    public void test1() {
        Integer[] a = {2, 3, 5, 63, 6, 7, 89, 2, 2, -2, -19};
        //冒泡排序
        BubbleSortUtil.bubbleSortAsc(a);

        System.out.println(Arrays.toString(a));
    }

    @Test
    public void test2() {
        Integer[] a = {2, 3, 5, 63, 6, 7, 89, 2, 2, -2, -19};
        //冒泡排序
        BubbleSortUtil.bubbleSortDesc(a);

        System.out.println(Arrays.toString(a));
    }

}
