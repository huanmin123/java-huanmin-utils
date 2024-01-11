package com.huanmin.utils.arithmetic.sort;

/**
 * 希尔排序是把记录按照下标的增量进行分组，对每组使用直接插入排序算法排序；随着增量逐渐减少，
 * 每组包含的关键词越来越多，当增量减至1时，整个文件恰被分成一组，算法便终止
 */
public class ShellSortUtil {

    /**
     * 希尔排序(移位法)
     * @param arr 要排序的数组
     */
    public static void shellAscQuick (int[] arr){
        //将数组每次进行分组
        for (int gap=arr.length/2;gap>0;gap/=2) {
            //分组的数组之间进行比较交换
            for (int i=gap;i<arr.length;i++) {
                int j=i;
                int temp=arr[j];
                while (j-gap>=0 && temp<arr[j-gap]) {
                    //移动
                    arr[j]=arr[j-gap];
                    j-=gap;
                }
                arr[j]=temp;
            }
        }

    }

    public static void shellDescQuick (int[] arr){
        //将数组每次进行分组
        for (int gap=arr.length/2;gap>0;gap/=2) {
            //分组的数组之间进行比较交换
            for (int i=gap;i<arr.length;i++) {
                int j=i;
                int temp=arr[j];
                while (j-gap>=0 && temp>arr[j-gap]) {
                    //移动
                    arr[j]=arr[j-gap];
                    j-=gap;
                }
                arr[j]=temp;
            }
        }

    }


    /**
     * 希尔排序(交换法 效率低)
     * @param arr 要排序的数组
     */

    public static void shellAsc(int[] arr){

        //希尔排序实际上是直接插入排序的升级版本，在直接插入排序的算法中，如果越到后面突然出现某个比较小的值
        //这个时候排序的步骤就越长，希尔排序就是为了解决这个问题，先大致的排一下，然后拍的过程中用的是直接插入排序算法
        //首先计算步长
        for(int d = arr.length/2;d>0;d = d/2){
            //开始直接排序算法
            //先来一轮直接排序
            for(int i = d;i < arr.length;i++){
                //然后开始交换
                for(int j = i - d;j >=0; j = j-d){
                    if(arr[j] < arr[j+d]){
                        int temp = arr[j];
                        arr[j] = arr[j+d];
                        arr[j+d] = temp;
                    }
                }
            }
        }
    }

    public static void shellDesc(int[] arr){

        //希尔排序实际上是直接插入排序的升级版本，在直接插入排序的算法中，如果越到后面突然出现某个比较小的值
        //这个时候排序的步骤就越长，希尔排序就是为了解决这个问题，先大致的排一下，然后拍的过程中用的是直接插入排序算法
        //首先计算步长
        for(int d = arr.length/2;d>0;d = d/2){
            //开始直接排序算法
            //先来一轮直接排序
            for(int i = d;i < arr.length;i++){
                //然后开始交换
                for(int j = i - d;j >=0; j = j-d){
                    if(arr[j] < arr[j+d]){
                        int temp = arr[j];
                        arr[j] = arr[j+d];
                        arr[j+d] = temp;
                    }
                }
            }
        }
    }




}