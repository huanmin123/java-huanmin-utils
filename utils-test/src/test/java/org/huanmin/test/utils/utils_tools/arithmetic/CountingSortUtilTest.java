package org.huanmin.test.utils.utils_tools.arithmetic;


import org.huanmin.utils.common.base.CodeTimeUtil;
import org.huanmin.utils.common.base.FakerData;
import org.huanmin.utils.common.container.ArrayUtil;
import org.huanmin.utils.arithmetic.sort.CountingSortUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CountingSortUtilTest {
    @Test
    public void testTime() throws Exception {
        Integer[] arr = FakerData.getDataIntNumber(10000,6);

        CodeTimeUtil.creator(()->{
            //计数排序
            CountingSortUtil.distributionCountingSortAscOrDesc(arr,"asc");
        });
        boolean order = ArrayUtil.isOrder(arr);
        System.out.println(order);
    }

    @Test
    public void testTime1() throws Exception {
        Integer[] arr = FakerData.getDataIntNumber(10000,9);
        ArrayList<Integer> arrayUtil = new ArrayList(Arrays.asList(arr));
        CodeTimeUtil.creator(()->{
            //计数排序
            CountingSortUtil.distributionCountingSortAscOrDesc(arrayUtil.toArray(new Integer[arrayUtil.size()]),"asc");
        });
    }



    @Test
    public void test1() {
        //给出无序数组
        Integer arr[] = {72, 6, 57, 88, 60, 42,-1,-55,-25,-25, 83, 73, 48, 85};

        //计数排序
        CountingSortUtil.countingSortAsc(arr);
        //输出有序数组
        System.out.println(Arrays.toString(arr));
    }




}
