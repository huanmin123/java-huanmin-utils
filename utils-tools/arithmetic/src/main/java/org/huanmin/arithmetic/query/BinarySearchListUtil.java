package org.huanmin.arithmetic.query;

// 二分查询  ,找到后返回下标

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 算法要求 :  查找表是顺序存储的有序表
 *
 *
 * 说明：元素必须是有序的，如果是无序的则要先进行排序操作。
 * <p>
 * 基本思想：也称为是折半查找，属于有序查找算法。用给定值k先与中间结点的关键字比较，
 * 中间结点把线形表分成两个子表，若相等则查找成功；若不相等，再根据k与该中间结点关键字的比较结果确定下一步查找哪个子表，
 * 这样递归进行，直到查找到或查找结束发现表中没有这样的结点。
 * 复杂度分析：最坏情况下，关键词比较次数为log2(n+1)，且期望时间复杂度为O(log2n)；
 * 注：折半查找的前提条件是需要有序表顺序存储，对于静态查找表，一次排序后不再变化，折半查找能得到不错的效率。但对于需要频繁执行插入或删除操作的数据集来说，维护有序的排序会带来不小的工作量，那就不建议使用。
 * 二分法查然效率很高，但我们为什么要和中间的值进行比较，如果我们和数组1/4或者3/4部分的值进行比较可不可以呢，
 * 对于一个要查找的数我们不知道他大概在数组的什么位置的时候我们可以使用二分法进行查找。
 * 但如果我们知道要查找的值大概在数组的最前面或最后面的时候使用二分法查找显然是不明智的,可以使用插值查找
 */

public class BinarySearchListUtil {

    public static <T extends Comparable <T>> int binarySearchFirst(List<T> arr, T value) {
        int low = 0;
        int high = arr.size() - 1;
        if(value.compareTo(arr.get(low)) <0  || value.compareTo(arr.get(high)) >0 ){
            return  -1;
        }
        while(low <= high) {
            int mid = (low + high) >> 1; //每次折半比较
            if (value .compareTo(arr.get(mid))==0 ) {
                return mid;
            }else  if (value.compareTo(arr.get(mid)) > 0) {
                low=mid + 1;
            } else if (value.compareTo(arr.get(mid)) < 0 ) {
                high=mid - 1;
            }
        }
        return -1;
    }



    public static <T extends Comparable <T>> int binarySearchLast(List<T> arr, T value) {
        int low = 0;
        int high = arr.size() - 1;
        if(value.compareTo(arr.get(low)) <0  || value.compareTo(arr.get(high)) >0 ){
            return -1;
        }
        Map<String, Integer> m = new HashMap<String, Integer>(2);
        m.put("index", -1);
        while (low <= high) {
            int mid = (low + high) >> 1; //每次折半比较
            if (value.compareTo(arr.get(mid)) >0 ) {
                low=mid + 1;
            } else if (value.compareTo(arr.get(mid)) <0 ) {
                high=mid - 1;
            } else {
                m.put("index", mid);
                low=mid + 1;
            }
        }
        return m.get("index");
    }

    public static   <T extends Comparable <T>>  List<Integer>  binarySearchFirstAndLast(List<T> arr, T value) {
        int low = 0;
        int high = arr.size() - 1;
        List<Integer> list = new ArrayList<>();
        if(value.compareTo(arr.get(low)) < 0 || value.compareTo(arr.get(high)) > 0){
            list.add(-1);
            return list;
        }

        while (low <= high) {
            int mid = (low + high) >> 1; //每次折半比较
            if (value.compareTo(arr.get(mid)) > 0) {
                low=mid + 1;
            } else if (value.compareTo(arr.get(mid)) < 0 ) {
                high=mid - 1;
            } else {
                list.add(mid);
                low=mid + 1;
            }
        }
        if (list.isEmpty()) {
            list.add(-1);
        }
        return list;
    }




    //(查询全部的值)
    public static <T extends Comparable <T>>  List<Integer> binarySearchAll(List<T> arr, T value) {
        int low = 0;
        int high = arr.size() - 1;
        List<Integer> list = new ArrayList<>(10);
        if(value.compareTo(arr.get(low)) < 0  || value.compareTo(arr.get(high)) > 0 ){
            list.add(-1);
            return list;
        }
        while (low <= high) {
            int mid = (low + high) >> 1; //每次折半比较
            if (value.compareTo(arr.get(mid)) > 0 ) {
                low=mid + 1;
            } else if (value.compareTo(arr.get(mid)) < 0) {
                high=mid - 1;
            } else {
                int temp=mid-1;
                //向左查询
                while (temp >= 0 && arr.get(temp).compareTo(value)==0) {
                    list.add(temp);
                    temp--;
                }
                //加入序列
                list.add(mid);
                temp=mid+1;
                //向右查询
                while (temp <= arr.size() - 1 && arr.get(temp).compareTo(value)==0 ) {
                    list.add(temp);
                    temp++;
                }
                return list;
            }
        }
        list.add(-1);
        return list;
    }





}
