package org.huanmin.test.utils.utils_common.container;

import org.huanmin.utils.common.container.ListSlice;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListSliceTest {

    @Test
    public void   test1() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i< 100;i++) {
            list.add(i);
        }
        ListSlice.sliceAsyncRun(list, 10, t -> {
            System.out.println(Thread.currentThread().getName() + " " + t);
        });
    }
}
