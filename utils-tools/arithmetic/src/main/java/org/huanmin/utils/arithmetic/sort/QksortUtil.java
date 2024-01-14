package org.huanmin.utils.arithmetic.sort;

/**
 * @author huanmin
 */

import java.util.List;

/**
 * 快速排序(单台机器中最快的排序方式)
 * 　选定一个元素作为中间元素，然后将表中所有元素与该中间元素相比较，将表中比中间元素小的元素调到表的前面，将比中间元素大的元素 调到后面，再将中间元素放在
 * 　这两部分之间以作为分界点，这样便得到一个划分。 然后再对左右两部分分别进行快速排序，直到每个子表仅有一个元素或为空表为止。
 */
public class QksortUtil {

    private static <T extends Comparable<T>> int partitionAsc(T[] arr, int low, int high) {
        //指定左指针i和右指针j
        int i = low;
        int j = high;

        //将第一个数作为基准值。挖坑
        T x = arr[low];

        //使用循环实现分区操作
        while (i < j) {//5  8
            //1.从右向左移动j，找到第一个小于基准值的值 arr[j]
            while (arr[j].compareTo(x) >= 0 && i < j) {
                j--;
            }
            //2.将右侧找到小于基准数的值加入到左边的（坑）位置， 左指针想中间移动一个位置i++
            if (i < j) {
                arr[i] = arr[j];
                i++;
            }


            //3.从左向右移动i，找到第一个大于等于基准值的值 arr[i]
            while (arr[i].compareTo(x) < 0 && i < j) {
                i++;
            }
            //4.将左侧找到的打印等于基准值的值加入到右边的坑中，右指针向中间移动一个位置 j--
            if (i < j) {
                arr[j] = arr[i];
                j--;
            }
        }

        //使用基准值填坑，这就是基准值的最终位置
        arr[i] = x;//arr[j] = y;
        //返回基准值的位置索引
        return i; //return j;
    }


    private static <T extends Comparable<T>> int partitionDesc(T[] arr, int low, int high) {
        //指定左指针i和右指针j
        int i = low;
        int j = high;

        //将第一个数作为基准值。挖坑
        T x = arr[low];

        //使用循环实现分区操作
        while (i < j) {//5  8
            //1.从右向左移动j，找到第一个小于基准值的值 arr[j]
            while (arr[j].compareTo(x) <= 0 && i < j) {
                j--;
            }
            //2.将右侧找到小于基准数的值加入到左边的（坑）位置， 左指针想中间移动一个位置i++
            if (i < j) {
                arr[i] = arr[j];
                i++;
            }


            //3.从左向右移动i，找到第一个大于等于基准值的值 arr[i]
            while (arr[i].compareTo(x) > 0  && i < j) {
                i++;
            }
            //4.将左侧找到的打印等于基准值的值加入到右边的坑中，右指针向中间移动一个位置 j--
            if (i < j) {
                arr[j] = arr[i];
                j--;
            }
        }

        //使用基准值填坑，这就是基准值的最终位置
        arr[i] = x;//arr[j] = y;
        //返回基准值的位置索引
        return i; //return j;
    }

    //递归何时结束
    private static <T extends Comparable<T>> void quickSort(T[] arr, int low, int high, String sort) {
        if (low < high) {

            if ("asc".equals(sort)) {
                //分区操作，将一个数组分成两个分区，返回分区界限索引
                int index = partitionAsc(arr, low, high);
                //对左分区进行快排
                quickSort(arr, low, index - 1, sort);
                //对右分区进行快排
                quickSort(arr, index + 1, high, sort);
            } else if ("desc".equals(sort)) {
                //分区操作，将一个数组分成两个分区，返回分区界限索引
                int index = partitionDesc(arr, low, high);
                //对左分区进行快排
                quickSort(arr, low, index - 1, sort);
                //对右分区进行快排
                quickSort(arr, index + 1, high, sort);
            }


        }

    }

    // 从小到大排序
    public static  <T extends Comparable<T>>  void quickSortAsc(T[] arr) {
        int low = 0;
        int high = arr.length - 1;
        quickSort(arr, low, high, "asc");
    }

    // 从大到小排序
    public static <T extends Comparable<T>>  void quickSortDesc(T[] arr) {
        int low = 0;
        int high = arr.length - 1;
        quickSort(arr, low, high, "desc");
    }









//=================================================下面是集合处理,和上面的逻辑一样==================================================================






    private static <T extends Comparable<T>> int partitionAsc(List<T> arr, int low, int high) {
        //指定左指针i和右指针j
        int i = low;
        int j = high;

        //将第一个数作为基准值。挖坑
        T x = arr.get(low);

        //使用循环实现分区操作
        while (i < j) {//5  8
            //1.从右向左移动j，找到第一个小于基准值的值 arr[j]
            while (arr.get(j).compareTo(x) >= 0 && i < j) {
                j--;
            }
            //2.将右侧找到小于基准数的值加入到左边的（坑）位置， 左指针想中间移动一个位置i++
            if (i < j) {
                arr.set(i, arr.get(j));
                i++;
            }


            //3.从左向右移动i，找到第一个大于等于基准值的值 arr[i]
            while (arr.get(i).compareTo(x) < 0 && i < j) {
                i++;
            }
            //4.将左侧找到的打印等于基准值的值加入到右边的坑中，右指针向中间移动一个位置 j--
            if (i < j) {
                arr.set(j, arr.get(i));
                j--;
            }
        }

        //使用基准值填坑，这就是基准值的最终位置
        arr.set(i, x);//arr[j] = y;
        //返回基准值的位置索引
        return i; //return j;
    }


    private static <T extends Comparable<T>> int partitionDesc(List<T> arr, int low, int high) {
        //指定左指针i和右指针j
        int i = low;
        int j = high;

        //将第一个数作为基准值。挖坑
        T x = arr.get(low);

        //使用循环实现分区操作
        while (i < j) {//5  8
            //1.从右向左移动j，找到第一个小于基准值的值 arr[j]
            while (arr.get(j).compareTo(x) <= 0 && i < j) {
                j--;
            }
            //2.将右侧找到小于基准数的值加入到左边的（坑）位置， 左指针想中间移动一个位置i++
            if (i < j) {
                arr.set(i, arr.get(j));
                i++;
            }


            //3.从左向右移动i，找到第一个大于等于基准值的值 arr[i]
            while (arr.get(i).compareTo(x) > 0  && i < j) {
                i++;
            }
            //4.将左侧找到的打印等于基准值的值加入到右边的坑中，右指针向中间移动一个位置 j--
            if (i < j) {
                arr.set(j, arr.get(i));
                j--;
            }
        }

        //使用基准值填坑，这就是基准值的最终位置
        arr.set(i, x);//arr[j] = y;
        //返回基准值的位置索引
        return i; //return j;
    }

    //递归何时结束
    private static <T extends Comparable<T>> void quickSort(List<T> arr, int low, int high, String sort) {
        if (low < high) {

            if ("asc".equals(sort)) {
                //分区操作，将一个数组分成两个分区，返回分区界限索引
                int index = partitionAsc(arr, low, high);
                //对左分区进行快排
                quickSort(arr, low, index - 1, sort);
                //对右分区进行快排
                quickSort(arr, index + 1, high, sort);
            } else if ("desc".equals(sort)) {
                //分区操作，将一个数组分成两个分区，返回分区界限索引
                int index = partitionDesc(arr, low, high);
                //对左分区进行快排
                quickSort(arr, low, index - 1, sort);
                //对右分区进行快排
                quickSort(arr, index + 1, high, sort);
            }


        }

    }

    // 从小到大排序
    public static  <T extends Comparable<T>>  void quickSortAsc(List<T>arr) {
        int low = 0;
        int high = arr.size() - 1;
        quickSort(arr, low, high, "asc");
    }

    // 从大到小排序
    public static <T extends Comparable<T>>  void quickSortDesc(List<T> arr) {
        int low = 0;
        int high = arr.size() - 1;
        quickSort(arr, low, high, "desc");
    }

}
