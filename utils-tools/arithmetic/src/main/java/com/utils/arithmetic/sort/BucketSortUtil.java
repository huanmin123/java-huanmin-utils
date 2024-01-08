package com.utils.arithmetic.sort;


import com.utils.common.container.ArrayUtil;
import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.executor.ThreadFactoryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 在多态机器上理论是可以没有数据量的限制, 排序速度和快速排序一样,但是需要更多的内存
 * 100万数据是1秒左右  ,只要有足够多的机器,理论上数据量在万亿级别也是可以的1秒左右
 * 需要改造: 数据分区, 数据排序, 合并数据 这三个步骤 ,都可以通过网络的策略来实现在不同的机器上执行
 */
public class BucketSortUtil {



    /**
     * Bucket sort
     * @param  array
     * @param  sortType 排序方式
     */
    public static<T extends Comparable<T>> void bucketSort(T[] array,String sortType) {

        int length = array.length;
        if (length <= 1) {
            return;
        }
        // 每1万数据分为一个捅  ,最合适
        int bucketCount=bucketCount(length);
        if (bucketCount <= 0) {
            bucketCount=1;
        }


        T high = array[0];
        T low = array[0];
        //找到输入元素的范围
        for (int i = 1; i < length; i++) {
            if (array[i].compareTo(high) > 0 ) {
                high = array[i];
            }
            if (array[i].compareTo(low) < 0 ) {
                low = array[i];
            }
        }
        // 一桶范围
        double interval = ((double) ((Integer)high - (Integer)low + 1)) / bucketCount;
        //如果是负数那么就表示数据溢出了
        if (interval<=0){
            throw new ArithmeticException("数据溢出,数据大小不能超过21亿和负的21亿");
        }


        ArrayList<T> []  buckets = new ArrayList[bucketCount];

        //初始化位置
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new ArrayList<T>();
        }
        //对输入数组进行分区
        for (T j : array) {
            buckets[(int) (((Integer)j - (Integer)low) / interval)].add(j);
        }

        List<Runnable> list=new ArrayList<>(buckets.length);
        // 多线程排序
        for (ArrayList<T> bucket : buckets) {
            list.add(()->{
                QksortUtil.quickSortAsc(bucket);  //(快速排序)最快方式
            });
        }
        //执行多线程排序
        ExecutorUtil.waitComplete( ExecutorUtil.createFutures(list, ThreadFactoryUtil.ThreadConfig.BucketSortUtil));

        //数据合并
        List<Runnable> list1=new ArrayList<>(buckets.length);
        int start=0;
        for (int i = 0; i < buckets.length; i++) {
            int finalStart = start;
            int finalI = i;
            list1.add(()->{
                for (int iz = 0, i1 = finalStart; iz < buckets[finalI].size(); iz++, i1++) {
                    array[i1]=buckets[finalI].get(iz);
                }
            });
            start+=buckets[i].size();
        }
        //执行多线程插入
        ExecutorUtil.waitComplete( ExecutorUtil.createFutures(list1, ThreadFactoryUtil.ThreadConfig.BucketSortUtil));
        //是否需要倒序
        if ("desc".equals(sortType)) {
            ArrayUtil.reverse(array);
        }

    }


    /**
     * Bucket sort
     * @param  array
     * @param  sortType 排序方式
     */
    public static<T extends Comparable<T>> void bucketSort(List<T> array,String sortType) {

        int length = array.size();
        if (length <= 1) {
            return;
        }
        // 每1万数据分为一个捅  ,最合适
        int bucketCount=bucketCount(length);
        if (bucketCount <= 0) {
            bucketCount=1;
        }


        T high = array.get(0);
        T low = array.get(0);
        //找到输入元素的范围
        for (int i = 1; i < length; i++) {
            if (array.get(i).compareTo(high) > 0 ) {
                high = array.get(i);
            }
            if (array.get(i).compareTo(low) < 0 ) {
                low = array.get(i);
            }
        }
        // 一桶范围
        double interval = ((double) ((Integer)high - (Integer)low + 1)) / bucketCount;
        //如果是负数那么就表示数据溢出了
        if (interval<=0){
            throw new ArithmeticException("数据溢出,数据大小不能超过21亿和负的21亿");
        }


        ArrayList<T> []  buckets = new ArrayList[bucketCount];

        //初始化位置
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new ArrayList<T>();
        }
        //对输入数组进行分区
        for (T j : array) {
            buckets[(int) (((Integer)j - (Integer)low) / interval)].add(j);
        }

        List<Runnable> list=new ArrayList<>(buckets.length);
        // 多线程排序
        for (ArrayList<T> bucket : buckets) {
            list.add(()->{
                QksortUtil.quickSortAsc(bucket);  //(快速排序)最快方式
            });
        }
        //执行多线程排序
        ExecutorUtil.waitComplete( ExecutorUtil.createFutures(list, ThreadFactoryUtil.ThreadConfig.BucketSortUtil));

        List<Runnable> list1=new ArrayList<>(buckets.length);
        int start=0;
        for (int i = 0; i < buckets.length; i++) {
            int finalStart = start;
            int finalI = i;
            list1.add(()->{
                for (int iz = 0, i1 = finalStart; iz < buckets[finalI].size(); iz++, i1++) {
                    array.set(i1, buckets[finalI].get(iz));
                }
            });
            start+=buckets[i].size();
        }
        //执行多线程插入
        ExecutorUtil.waitComplete( ExecutorUtil.createFutures(list1, ThreadFactoryUtil.ThreadConfig.BucketSortUtil));
        //是否需要倒序
        if ("desc".equals(sortType)) {
            ArrayUtil.reverse(array);
        }

    }






    //计算桶的大小,1000数据每100一个桶, 1000每1000一个桶, 10万每1万一个桶 ,最高1万数据一个桶
    private static int  bucketCount(int size){
        if(size<=1000){
            return 100;
        } else if(size<=10000){
            return 1000;
        }else if(size<=100000){
            return 10000;
        }
        return 10000;
    }


}
