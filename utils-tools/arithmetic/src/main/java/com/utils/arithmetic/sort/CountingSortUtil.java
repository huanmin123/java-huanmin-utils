package com.utils.arithmetic.sort;


import com.utils.common.container.ArrayUtil;

public class CountingSortUtil {
    /**
     *  计数排序是一个非基于比较的线性时间排序算法，是一种稳定排序的算法。
     *  它的基本思想是对于给定的输入序列中的每一个元素 x，确定该序列中值小于 x 的元素的个数。
     *  一旦确定了这一信息，就可以将 x 直接存放到最终的输出序列的正确位置上
     * @param arr
     */

    public static void countingSortAsc(Integer[] arr) {
        int length = arr.length;
        int[] temp = new int[length];
        int[] count = new int[length];
        for(int i = 0;i < length - 1;i++) {
            for(int j = i + 1;j < length;j++) {
                if(arr[i] < arr[j]) {
                    count[j]++;
                } else {
                    count[i]++;
                }
            }
        }
        for(int i = 0;i < length;i++) {
            temp[count[i]] = arr[i];
        }

        //使用java自带的数组copy
        System.arraycopy(temp, 0, arr, 0, length);

    }

    public static void countingSortDesc(Integer[] arr) {
        int length = arr.length;
        int[] temp = new int[length];
        int[] count = new int[length];
        for(int i = 0;i < length - 1;i++) {
            for(int j = i + 1;j < length;j++) {
                if(arr[i] > arr[j]) {
                    count[j]++;
                } else {
                    count[i]++;
                }
            }
        }
        for(int i = 0;i < length;i++) {
            temp[count[i]] = arr[i];
        }
        //使用java自带的数组copy
        System.arraycopy(temp, 0, arr, 0, length);


    }



    //分布计数排序
    //该方法的时间效率要优于快速排序和合并排序，
    // 但是此方法对于给定数据有一定的要求，内部的数据不能太大,否则会导致内存溢出的,一般数据大小在万级别的就行(5位数,每增加一位耗时翻倍)
    // 即数组中元素满足min <= A[i] <= max，且在min ~ max之间的所有元素在数组A中均有出现。
    // ,该方法比快速排序都快 ,经过测试仅限于基本数据类型
    public static void distributionCountingSortAscOrDesc(Integer[] array, String sortType) {

        // 取数组中最大和最小值
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int length = array.length;
        for (int item : array) {
            max = Math.max(max, item);
            min = Math.min(min, item);
        }

        int[] temp = new int[length];
        int i1 = max - min;
        int[] d = new int[ i1+ 1];
        for (int value : array) {
            d[value - min]++;
        }
        for(int i = 0;i < i1;i++) {
            d[i + 1] = d[i + 1] + d[i];
        }
        for (int k : array) {
            int j = k - min;
            temp[d[j] - 1] = k;
            d[j]--;
        }
        //使用java自带的数组copy
        if ("desc".equals(sortType)) {
            //反转
           ArrayUtil.reverse(array);
        }
        //将temp复制到array
        for (int i = 0; i < temp.length; i++) {
            array[i]=temp[i];
        }


    }







}
