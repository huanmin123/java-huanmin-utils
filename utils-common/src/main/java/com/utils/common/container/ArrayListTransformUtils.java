package com.utils.common.container;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 各种数组,List相互转换 等
 */
public class ArrayListTransformUtils {

    /**
     * int[] 转 Integer[]
     * @param arrays
     * @return
     */
    public static Integer[] intToInteger(int[] arrays){
        return    Arrays.stream(arrays).boxed().toArray(Integer[]::new);
    }

    /**
     * double[] 转 Double[]
     * @param doubles
     * @return
     */
    public static Double[] doubleToDouble(double[] doubles){
        return    Arrays.stream(doubles).boxed().toArray(Double[]::new);
    }


    //包装类型转基本数据类型
    public static int[] intToInteger(Integer[] arrays){
        return    Arrays.stream(arrays).mapToInt(Integer::valueOf).toArray();
    }
    /**
     * int[] 转  List<Integer>
     * @param array
     * @return
     */
    public static List<Integer> intToList(int[] array){
        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }

    /**
     * Integer[] 转 List<Integer>
     * @param array
     * @return
     */
    public  static List<Integer>  integerToList(Integer[] array){
        return Arrays.stream(array).mapToInt(Integer::valueOf).boxed().collect(Collectors.toList());
    }



    /**
     * List<Integer> 转  Integer[]
     * @param list
     * @return
     */
    public static Integer[] listToInteger(List<Integer> list){
        return  list.stream().mapToInt(Integer::valueOf).boxed().toArray(Integer[]::new);
    }

    /**
     *  List<Integer> 转 int[]
     * @param list
     * @return
     */
    public static int[] listToint(List<Integer> list){
        return   list.stream().mapToInt(Integer::valueOf).toArray();
    }
}
