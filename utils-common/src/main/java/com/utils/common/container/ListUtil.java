package com.utils.common.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtil {

    //反转List
    public static <T> void reverse(List<T> data) {
        int length = data.size();
        T temp = null;// 临时变量
        for (int i = 0; i < length / 2; i++) {
            temp = data.get(i);
            data.set(i,data.get(length - 1 - i));
            data.set(length - 1 - i,temp);
        }
    }



}
