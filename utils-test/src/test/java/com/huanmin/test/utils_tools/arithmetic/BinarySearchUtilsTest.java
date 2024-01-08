package com.huanmin.test.utils_tools.arithmetic;


import com.utils.common.base.CodeTimeUtil;
import com.utils.common.base.FakerData;
import com.utils.arithmetic.query.BinarySearchUtil;
import com.utils.arithmetic.sort.QksortUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

//二分查询
public class BinarySearchUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(BinarySearchUtilsTest.class);

    @Test
    public void testTime() throws Exception {

        Integer[] dataInteger = FakerData.getDataInteger(10000);
        dataInteger[9111]=99900900;

        //使用二分查询必须是有序的列表数组才行
        CodeTimeUtil.creator(()->{
            QksortUtil.quickSortAsc(dataInteger);
                int i = BinarySearchUtil.binarySearchFirst(dataInteger, 99900900);  //1千万  0~1毫秒左右
                System.out.println(i );
        });


    }


    @Test
    public void test1() {
        Integer[] dataInt = {-3, 2,2, 0,5, -1, -2, 99, 0, -6,5, 5};
        QksortUtil.quickSortAsc(dataInt);
        System.out.println(Arrays.toString(dataInt));
        int i = BinarySearchUtil.binarySearchLast(dataInt, 5);
        System.out.println("test2"+i );

    }



    @Test
    public void test2() {
        Integer[] dataInt = {-3,5, 2,2, 0, -1, 5,-2, 99, 0, -6,5, 5};
        QksortUtil.quickSortAsc(dataInt);
        System.out.println(Arrays.toString(dataInt));
        List<Integer> list = BinarySearchUtil.binarySearchFirstAndLast(dataInt, 5);

        System.out.println("test2"+list);

    }

    @Test
    public void test3() {
        Integer[] dataInt = {-3,5, 2,2, 0, -1, 5,-2, 99, 0, -6,5, 5};
        QksortUtil.quickSortAsc(dataInt);
        System.out.println(Arrays.toString(dataInt));
        List<Integer> list = BinarySearchUtil.binarySearchAll(dataInt, 5);
        System.out.println("test2"+list);
    }

}
