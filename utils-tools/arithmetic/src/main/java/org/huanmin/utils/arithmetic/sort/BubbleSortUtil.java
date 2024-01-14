package org.huanmin.utils.arithmetic.sort;
/**
 * @author huanmin
 */
/**
 *  （1）基于交换思想的排序算法
 *  （2）从一端开始，逐个比较相邻的两个元素的大小，发现大于或者小于即交换。
 *  （3）一次遍历，一定能将其中最大(小)的元素交换到其最终位置上
 *   (4)  重复 1~3 直到全部排序完成
 */
public class BubbleSortUtil {

    //大到小排序
    public  static <T extends Comparable<T>>  void  bubbleSortDesc(T[]  array){
        int length = array.length;
        for(int i=0;i<length;i++)
        {
            for(int j=0;j<length-i-1;j++)
            {
                if(array[j].compareTo(array[j+1])< 0)
                {
                    T temp=array[j];
                    array[j]=array[j+1];
                    array[j+1]=temp;
                }
            }
        }


    }
    // 从小到大排序
    public  static <T extends Comparable<T>> void  bubbleSortAsc(T[]  array){
        int length = array.length;
        for(int i=0;i<length;i++)
        {
            for(int j=0;j<length-i-1;j++)
            {
                if(array[j].compareTo(array[j+1])>0)
                {
                    swap(array,j);
                }
            }
        }

    }




    private static <T extends Comparable<T>>  void swap(T[] array, int j) {
        T temp=array[j];
        array[j]=array[j+1];
        array[j+1]=temp;
    }

}
