package com.huanmin.test.utils_common.container;

import com.utils.common.container.ArrayMatrixUtil;

import org.junit.Test;

public class ArrayMatrixUtilTest {
    private static int[][] ints = {
            {0, 9, 2, 4, 8, 1, 7, 6, 3},
            {4, 1, 3, 7, 6, 2, 9, 8, 5},
            {8, 6, 7, 3, 5, 9, 4, 1, 2},
            {6, 2, 4, 1, 9, 5, 3, 7, 8},
            {7, 5, 9, 8, 4, 3, 1, 2, 6},
            {1, 3, 8, 6, 2, 7, 5, 9, 4},
            {2, 7, 1, 5, 3, 8, 6, 4, 9},
            {3, 8, 6, 9, 1, 4, 2, 5, 7},
            {0, 4, 5, 2, 7, 6, 8, 3, 1}
    };
    private static double[][] ints1 = {
            {0.1, 9.1, 2, 4, 8, 1.1, 7.1, 6.1, 3},
            {4.1, 1, 3.1, 7, 6, 2.1, 9.1, 8.1, 5},
            {8, 6, 7.1, 3, 5, 9, 4.1, 1.1, 2},
            {6, 2, 4.1, 1, 9, 5.1, 3, 7.1, 8},
            {7, 5.1, 9.1, 8.1, 4.1, 3.1, 1, 2, 6},
            {1, 3.1, 8.1, 6.1, 2.1, 7, 5, 9.1, 4.1},
            {2, 7.1, 1.1, 5.1, 3.1, 8.1, 6, 4.1, 9.1},
            {3.1, 8.1, 6.1, 9.1, 1.1, 4.1, 2, 5, 7},
            {0, 4.1, 5.1, 2.1, 7.1, 6, 8.1, 3, .1}
    };

    @Test
    public void intToIntegerByMatrix() {
        Integer[][] integers = ArrayMatrixUtil.intToIntegerByMatrix(ints);
        ArrayMatrixUtil.show(integers);
    }

    @Test
    public void doubleToDoubleByMatrix() {
        Double[][] doubles = ArrayMatrixUtil.doubleToDoubleByMatrix(ints1);
        ArrayMatrixUtil.show(doubles);
    }


    //依据指定的矩阵大小进行按照从左到右从上到下遍历,如果不够那么就自行换行补充
    @Test
    public void arrayInsideMatrixByIteration() {
        Integer[][] integers = ArrayMatrixUtil.intToIntegerByMatrix(ints);
        ArrayMatrixUtil.arrayInsideMatrixByIteration(integers, "4*4", (data) -> {
            ArrayMatrixUtil.ArrayMatrix<Integer> arrayMatrix = data.getArrayMatrix();
            ArrayMatrixUtil.show(arrayMatrix.getMatrix());
        });

        //--------------------------------------------
        Double[][] doubles = ArrayMatrixUtil.doubleToDoubleByMatrix(ints1);
        ArrayMatrixUtil.arrayInsideMatrixByIteration(doubles, "4*4", (data) -> {
            ArrayMatrixUtil.ArrayMatrix<Double> arrayMatrix1 = data.getArrayMatrix();
            ArrayMatrixUtil.show(arrayMatrix1.getMatrix());
        });
    }

    @Test
    public void arrayRowAndColByIteration() {

    }
}
