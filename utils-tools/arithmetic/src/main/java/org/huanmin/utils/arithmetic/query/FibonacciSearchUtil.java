package org.huanmin.utils.arithmetic.query;

import java.util.Arrays;
import java.util.List;

/**
 * 斐波那契查找 (斐波那契查找效率并没有那么高)
 * 算法要求 :  查找表是顺序存储的有序表
 * 斐波那契数列，又称黄金分割数列，指的是这样一个数列：一、一、二、三、五、八、····，在数学上，
 * 斐波那契被递归方法以下定义：F(1)=1，F(2)=1，F(n)=f(n-1)+F(n-2) （n>=2）。
 * 该数列越日后相邻的两个数的比值越趋向于黄金比例值（0.618）。
 * java斐波那契查找就是在二分查找的基础上根据斐波那契数列进行分割的。在斐波那契数列找一个等于略大于查找表中元素个数的数F[n]，
 * 将原查找表扩展为长度为F[n](若是要补充元素，则补充重复最后一个元素，直到知足F[n]个元素)，完成后进行斐波那契分割，
 * 即F[n]个元素分割为前半部分F[n-1]个元素，后半部分F[n-2]个元素，找出要查找的元素在那一部分并递归，直到找到。
 * 斐波那契查找的时间复杂度仍是O(log  2  n  )，可是  与折半查找相比，斐波那契查找的优势是它只涉及加法和减法运算，
 * 而不用除法，而除法比加减法要占用更多的时间，所以，斐波那契查找的运行时间理论上比折半查找小，可是仍是得视具体状况而定。
 * 对于斐波那契数列：一、一、二、三、五、八、....（也能够从0开始），先后两个数字的比值随着数列的增长
 * 愈来愈接近黄金比值0.618。好比这里的89，把它想象成整个有序表的元素个数，而89是由前面的两个斐波那契数34和55相加以后的和，
 * 也就是说把元素个数为89的有序表分别由前55个数据元素组成的前半段和由后34个数据元素组成的后半段，
 * 那么前半段元素个数和整个有序表长度的比值就接近黄金比值0.618，假如要查找的元素在前半段，
 * 那么继续按照斐波那契数列来看，55 = 34 + 21，因此继续把前半段分红前34个数据元素的前半段和后21个元素的后半段，
 * 继续查找，如此反复，直到查找成功或失败，这样就把斐波那契数列应用到查找算法中了。算法
 */
public class FibonacciSearchUtil {


    /**
     * @return 斐波那契数列
     */
    private static Integer[] fibonacciSequence(Integer maxSize) {
        Integer[] f = new Integer[maxSize];
        f[0] = 1;
        f[1] = 1;
        for (int i = 2; i < maxSize; i++) {
            f[i] = f[i - 1] + f[i - 2];
        }
        return f;
    }

    /**
     * 斐波那契查找
     *
     * @param arrays 传入数组
     * @param value  待搜索的值
     * @return 下标
     */
    public static <T extends Comparable<T>> Integer fibonacciSearch(T[] arrays, T value) {
        int left = 0;
        int right = arrays.length - 1;
        int mid = 0;
        //存放斐波那契数列
        Integer[] fibArray = fibonacciSequence(arrays.length);
        //表示斐波那契分割数值的下标
        int fibIndex = 0;
        //获取到斐波那契分割数值的下标
        while (right > fibArray[fibIndex] - 1) {
            fibIndex++;
        }
        //fibArray[fibIndex]的值可能大于a的长度，因此需要构建一个新数组，不足的部分会使用0填充
        T[] temp = Arrays.copyOf(arrays, fibArray[fibIndex]);
        //将新填充的内容替换为最后的数
        //例：temp = {1,3,4,6,9,11,0,0} => {1,3,4,6,9,11,11,11}
        for (int i = right + 1; i < temp.length; i++) {
            temp[i] = arrays[right];
        }
        //使用while来循环处理，找到value，前提是左指针在右指针前边
        while (left <= right) {
            mid = left + fibArray[fibIndex - 1] - 1;
            //当查找的值小于当前值时应该向数组的前边遍历
            if (value.compareTo(temp[mid]) < 0) {
                right = mid - 1;
                //斐波那契数向前移一位
                fibIndex--;
            }
            //当查找的值小于当前值时应该向数组的后边遍历
            else if (value.compareTo(temp[mid]) > 0) {
                left = mid + 1;
                fibIndex -= 2;
            } else {
                if (mid <= right) {
                    return mid;
                } else {
                    return right;
                }
            }
        }
        return -1;
    }



}
