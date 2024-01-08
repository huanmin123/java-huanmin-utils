package com.utils.arithmetic.sort;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Slf4j
public class InsertSortUtil {
    /**
     * 插入排序
     * <p>
     * <p>
     * <p>
     * 方法：将一个记录插入到已排好序的有序表（有可能是空表）中,从而得到一个新的记录数增1的有序表。
     * <p>
     * <p>
     * <p>
     * 性能：比较次数O(n^2),n^2/4
     * <p>
     * <p>
     * <p>
     * 复制次数O(n),n^2/4
     * <p>
     * <p>
     * <p>
     * 比较次数是前两者的一般，而复制所需的CPU时间较交换少，所以性能上比冒泡排序提高一倍多，而比选择排序也要快。
     *
     * @param data     要排序的数组
     * @param sortType 排序类型
     */
    public static void insertSort(Integer[] data, String sortType) {
        if (sortType.equals("asc")) { // 正排序，从小排到大
            // 比较的轮数
            for (int i = 1; i < data.length; i++) {
                // 保证前i+1个数排好序
                int temp = data[i];
                int j;
                for (j = i; j > 0 && data[j - 1] > temp; j--) {
                    data[j] = data[j - 1];
                }
                data[j] = temp;
            }

        } else if (sortType.equals("desc")) { // 倒排序，从大排到小
            // 比较的轮数
            for (int i = 1; i < data.length; i++) {
                // 保证前i+1个数排好序
                int temp = data[i];
                int j;
                for (j = i; j > 0 && data[j - 1] < temp; j--) {
                    data[j] = data[j - 1];
                }
                data[j] = temp;

            }

        } else {
            log.error("您输入的排序类型错误！");
        }
    }
    public static void insertSort(Double[] data, String sortType) {
        if (sortType.equals("asc")) { // 正排序，从小排到大
            // 比较的轮数
            for (int i = 1; i < data.length; i++) {
                // 保证前i+1个数排好序
                double temp = data[i];
                int j;
                for (j = i; j > 0 && data[j - 1] > temp; j--) {
                    data[j] = data[j - 1];
                }
                data[j] = temp;
            }

        } else if (sortType.equals("desc")) { // 倒排序，从大排到小
            // 比较的轮数
            for (int i = 1; i < data.length; i++) {
                // 保证前i+1个数排好序
                double temp = data[i];
                int j;
                for (j = i; j > 0 && data[j - 1] < temp; j--) {
                    data[j] = data[j - 1];
                }
                data[j] = temp;

            }

        } else {
            System.out.println("您输入的排序类型错误！");
        }
    }

}
