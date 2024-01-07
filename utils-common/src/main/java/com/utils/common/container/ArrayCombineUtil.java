package com.utils.common.container;

import java.util.ArrayList;
import java.util.List;

public class ArrayCombineUtil {


    //旧比新多出的部分(差集)
    public static List<Integer> arraySurplus(List<Integer> newList, List<Integer> oldList) {
        List<Integer> list = new ArrayList<>();//创建一个新的集合
        for (Integer integer : newList) {//遍历新集合
            if (!oldList.contains(integer)) {//如果新集合中的元素不在旧集合中
                list.add(integer);//将新集合中的元素添加到新集合中
            }
        }
        return list;

    }

    //重合的部分 (并集)
    public static List<Integer> arrayDoublication(List<Integer> newList, List<Integer> oldList) {
        List<Integer> list = new ArrayList<>();//创建一个新的集合
        for (Integer integer : newList) {//遍历新集合
            if (oldList.contains(integer)) {//如果新集合中的元素在旧集合中
                list.add(integer);//将新集合中的元素添加到新集合中
            }
        }
        return list;
    }

    //新比旧多出的部分(差集)
    public static List<Integer> arrayMore(List<Integer> newList, List<Integer> oldList) {
        List<Integer> list = new ArrayList<>();//创建一个新的集合
        for (Integer integer : oldList) {//遍历旧集合
            if (!newList.contains(integer)) {//如果旧集合中的元素不在新集合中
                list.add(integer);//将旧集合中的元素添加到新集合中
            }
        }
        return list;
    }


}