package com.huanmin.utils.arithmetic.query;

/**
 *
 * 插值查找
 *
 * 算法要求 :  查找表是顺序存储的有序表
 *
 *
 * 　 在介绍插值查找之前，首先考虑一个新问题，为什么二分算法一定要是折半，而不是折四分之一或者折更多呢？
 *    二分法查然效率很高，但我们为什么要和中间的值进行比较，如果我们和数组1/4或者3/4部分的值进行比较可不可以呢，
 *    对于一个要查找的数我们不知道他大概在数组的什么位置的时候我们可以使用二分法进行查找。
 *    但如果我们知道要查找的值大概在数组的最前面或最后面的时候使用二分法查找显然是不明智的
 * 　　打个比方，在英文字典里面查“apple”，你下意识翻开字典是翻前面的书页还是后面的书页呢？如果再让你查“zoo”，你又怎么查？很显然，这里你绝对不会是从中间开始查起，而是有一定目的的往前或往后翻。
 * 　　同样的，比如要在取值范围1 ~ 10000 之间 100 个元素从小到大均匀分布的数组中查找5， 我们自然会考虑从数组下标较小的开始查找。
 * 　　经过以上分析，折半查找这种查找方式，不是自适应的（也就是说是傻瓜式的）。二分查找中查找点计算如下：
 * 　　mid=(low+high)/2, 即mid=low+1/2*(high-low);
 * 　　通过类比，我们可以将查找的点改进为如下：
 * 　　mid=low+(key-a[low])/(a[high]-a[low])*(high-low)，
 * 　　也就是将上述的比例参数1/2改进为自适应的，根据关键字在整个有序表中所处的位置，让mid值的变化更靠近关键字key，这样也就间接地减少了比较次数。
 * 　　基本思想：基于二分查找算法，将查找点的选择改进为自适应选择，可以提高查找效率。当然，插值查找也属于有序查找。
 * 　　注：对于表长较大，而关键字分布又比较均匀的查找表来说，插值查找算法的平均性能比折半查找要好的多。反之，
 *    数组中如果分布非常不均匀，那么插值查找未必是很合适的选择。
 */
public class InsertionSearchUtil {


    public static <T extends Comparable<T>>  int insertSearch(T[] array, T key) {
        return search(array, key, 0, array.length - 1);
    }
    private static <T extends Comparable<T> > int search(T[] array, T key, int left, int right) {
        while (left <= right) {
            if (array[right] == array[left]) {
                if (array[right].compareTo(key) == 0 ) {
                    return right;
                } else {
                    return -1;
                }
            }
            int middle = left + (((Integer) key - (Integer)array[left]) / ((Integer)array[right] - (Integer)array[left])) * (right - left);
            if (array[middle] == key) {
                return middle;
            }
            if (key.compareTo(array[middle]) < 0 ) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return -1;
    }
}