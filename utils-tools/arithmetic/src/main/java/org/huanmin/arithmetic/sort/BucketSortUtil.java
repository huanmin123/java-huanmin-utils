package org.huanmin.arithmetic.sort;



import com.utils.common.container.BasicArrayUtil;
import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.executor.ThreadFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class BucketSortUtil {



    /**
     * Bucket sort
     *
     * @param array       array to be sorted
     */
    public static<T extends Comparable<T>> void bucketSort(T[] array,String sortType) {

        int length = array.length;
        // 每1万数据分为一个捅  ,最合适
//        int bucketCount=length/10000;
        int bucketCount=2;
        if (bucketCount <= 0) {
            bucketCount=1;
        }
        if (length <= 1) {
            return;
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
        double interval = ((double) ((int)high - (int)low + 1)) / bucketCount;

        ArrayList<T> []  buckets = new ArrayList[bucketCount];

        //初始化位置
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new ArrayList<T>();
        }
        //对输入数组进行分区
        for (T j : array) {
            buckets[(int) (((int)j - (int)low) / interval)].add(j);
        }

        List<Runnable> list=new ArrayList<>(buckets.length);
        // 多线程排序
        for (ArrayList<T> bucket : buckets) {
            list.add(()->{
                QksortListUtil.quickSortAsc(bucket);  //(快速排序)最快方式
            });
        }
        //执行多线程
        ExecutorUtil.waitComplete( ExecutorUtil.createFutures(list, ThreadFactoryUtil.ThreadConfig.BucketSortUtil));


        //多线程组合数据

        //将第一个给多线程
        List<Runnable> list1=new ArrayList<>(buckets.length);

        list1.add(()->{
            for (int i = 0; i < buckets[0].size(); i++) {
                array[i] = buckets[0].get(i);

            }
        });


        //将剩下的进行多线程
        for (int i = 1; i < buckets.length; i++) {
            int finalI = i;
            list1.add(()->{
                for (int i1 = buckets[finalI -1].size(), j = 0; j < buckets[finalI].size(); i1++,j++) {
                    array[i1]=buckets[finalI].get(j);
                }
            });
        }

        ExecutorUtil.waitComplete( ExecutorUtil.createFutures(list1,ThreadFactoryUtil.ThreadConfig.BucketSortUtil));
        //是否需要倒序
        if ("desc".equals(sortType)) {
            BasicArrayUtil.reverse(array);
        }



    }

}
