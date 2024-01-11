package com.huanmin.test.utils.utils_tools.arithmetic;

import com.huanmin.utils.common.base.CodeTimeUtil;
import com.huanmin.utils.common.base.FakerData;
import com.huanmin.utils.common.container.ArrayUtil;
import com.huanmin.utils.arithmetic.sort.QksortUtil;
import org.junit.Test;

/**
 * @author huanmin
 * @date 2024/1/8
 */
public class QksortUtilTest {
    @Test
    public  void quickSortAscTest() throws Exception {
        Integer[] dataInt = FakerData.getDataIntNumber(10000000,9);
        CodeTimeUtil.creator(()->{
            QksortUtil.quickSortDesc(dataInt);
        });
        boolean order = ArrayUtil.isOrder(dataInt);
        System.out.println(order);

    }
}
