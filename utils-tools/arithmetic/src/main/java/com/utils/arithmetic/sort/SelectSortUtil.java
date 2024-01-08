package com.utils.arithmetic.sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectSortUtil {
    private static final Logger logger = LoggerFactory.getLogger(InsertSortUtil.class);
    /**
     * 直接选择排序法----选择排序的一种
     * <p>
     * <p>
     * <p>
     * 方法：每一趟从待排序的数据元素中选出最小（或最大）的一个元素， 顺序放在已排好序的数列的最后，直到全部待排序的数据元素排完。
     * <p>
     * <p>
     * <p>
     * 性能：比较次数O(n^2),n^2/2
     * <p>
     * <p>
     * <p>
     * 交换次数O(n),n
     * <p>
     * <p>
     * <p>
     * 交换次数比冒泡排序少多了，由于交换所需CPU时间比比较所需的CUP时间多，所以选择排序比冒泡排序快。
     * <p>
     * <p>
     * <p>
     * 但是N比较大时，比较所需的CPU时间占主要地位，所以这时的性能和冒泡排序差不太多，但毫无疑问肯定要快些。
     *
     * @param data     要排序的数组
     * @param sortType 排序类型
     * @return
     */


    public static   void selectSort(int[] data, String sortType) {
        if ("asc".equals(sortType)) { // 正排序，从小排到大
            int index;
            for (int i = 1; i < data.length; i++) {
                index = 0;
                for (int j = 1; j <= data.length - i; j++) {
                    if (data[j] > data[index]) {
                        index = j;
                    }
                }
                // 交换在位置data.length-i和index(最大值)两个数
                swap(data, data.length - i, index);
            }
        } else if ("desc".equals(sortType)) { // 倒排序，从大排到小
            int index;
            for (int i = 1; i < data.length; i++) {
                index = 0;
                for (int j = 1; j <= data.length - i; j++) {
                    if (data[j] < data[index]) {
                        index = j;
                    }
                }
                // 交换在位置data.length-i和index(最大值)两个数
                swap(data, data.length - i, index);
            }
        } else {
            System.out.println("您输入的排序类型错误！");
        }
    }

    /**
     * 交换数组中指定的两元素的位置
     *
     * @param data
     * @param x
     * @param y
     */


    private  static  void swap(int[] data, int x, int y) {
        int temp = data[x];
        data[x] = data[y];
        data[y] = temp;
    }

}
