package com.huanmin.test.utils.utils_tools.arithmetic;


import com.huanmin.utils.common.base.CodeTimeUtil;
import com.huanmin.utils.common.base.FakerData;
import com.huanmin.utils.common.container.ArrayUtil;
import com.huanmin.utils.arithmetic.sort.BucketSortUtil;
import org.junit.Test;

import java.util.Arrays;

public class BucketSortUtilTest {
    @Test
    public void testTime() throws Exception {
        Integer[] dataInt = FakerData.getDataIntNumber(10000000,9);
      
        CodeTimeUtil.creator(()->{
            BucketSortUtil.bucketSort(dataInt,"asc");
        });
        boolean order = ArrayUtil.isOrder(dataInt);
        System.out.println(order);
    }


    @Test
    public void test1() {
        Integer[] a = {2, 3, 5, 63, 6, 7, 89, 2, 2, -2, -19};

        BucketSortUtil.bucketSort(a,"asc");

        System.out.println(Arrays.toString(a));
    }

    @Test
    public void test2() {
        Integer[] a = {2, 3, 5, 63, 6, 7, 89, 2, 2, -2, -19};

        BucketSortUtil.bucketSort(a,"desc");

        System.out.println(Arrays.toString(a));
    }
}
