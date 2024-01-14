package org.huanmin.utils.common.container;

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
            data.set(i, data.get(length - 1 - i));
            data.set(length - 1 - i, temp);
        }
    }

    //判断List是否是有序的, 采用的策略是尽可能的少比较
    public static <T extends Comparable<T>> boolean isOrder(List<T> data) {
        //判断开头中间结尾是否是无序的
        if (data.get(0).compareTo(data.get(1)) > 0 || data.get(data.size() / 2 - 1).compareTo(data.get(data.size() / 2)) > 0 || data.get(data.size() - 2).compareTo(data.get(data.size() - 1)) > 0) {
            return false;
        }

        //100数据以内挨个比较
        if (data.size() <= 100) {
            return isOrder(data, 0, data.size() - 1);
        } else if (data.size() <= 1000) {//1000数据取开头中间结尾各50数据比较
            return isOrder(data, 0, 50) && isOrder(data, data.size() / 2 - 25, data.size() / 2 + 25) && isOrder(data, data.size() - 50, data.size() - 1);
        } else if (data.size() <= 10000) {//10000数据取,随机取10个50个范围数据比较
            for (int i = 0; i < 10; i++) {
                int start = (int) (Math.random() * (data.size() - 50));
                if (!isOrder(data, start, start + 50)) {
                    return false;
                }
            }
            return true;
        }else {//10000以上数据取,每隔1000个数据取50个范围数据比较
            for (int i = 0; i < data.size(); i+=1000) {
                if (!isOrder(data, i, i + 50)) {
                    return false;
                }
            }
            return true;
        }

    }

    //复制List
    public static <T> List<T> copy(List<T> data) {
        List<T> list = new ArrayList<>(data.size());
        for (T t : data) {
            list.add(t);
        }
        return list;
    }



    //随机判断几个范围内的数是否是有序的
    private static <T extends Comparable<T>> boolean isOrder(List<T> data, int start, int end) {
        int length = data.size();
        if (start < 0 || end > length - 1) {
            throw new RuntimeException("start or end is error");
        }
        for (int i = start; i < end; i++) {
            if (data.get(i).compareTo(data.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }






}
