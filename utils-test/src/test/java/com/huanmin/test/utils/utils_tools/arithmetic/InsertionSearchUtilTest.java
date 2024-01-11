package com.huanmin.test.utils.utils_tools.arithmetic;


import com.huanmin.utils.common.base.CodeTimeUtil;
import com.huanmin.utils.common.base.FakerData;
import com.huanmin.utils.arithmetic.query.InsertionSearchUtil;
import com.huanmin.utils.arithmetic.sort.QksortUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertionSearchUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(InsertionSearchUtilTest.class);
    @Test
    public void testTime() throws Exception {

        Integer[] dataInt = FakerData.getDataIntNumber(10001000,5);
        dataInt[9111000]=99;
        dataInt[9111200]=99;
        //使用二分查询必须是有序的列表数组才行
        QksortUtil.quickSortAsc(dataInt);
        CodeTimeUtil.creator(()->{
            int i = InsertionSearchUtil.insertSearch(dataInt, 99);  //1千万  0~1毫秒左右
            System.out.println(i);
            System.out.println(dataInt[i]);
        });


    }



}
