package com.utils.common.container;

import lombok.Data;

@Data
public class ArrayContext<T> {
    private  T[] rows;
    private T[] cols;
    private int row; //下标  (从0开始)
    private int col; //下标  (从0开始)
    private  int rowLength; //行长度
    private  int colLength; //列长度

    private ArrayMatrix matrix;//小矩阵

    public ArrayContext() {
    }

    public ArrayContext(T[] rows, T[] cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public ArrayContext(T[] rows, T[] cols, int row, int col) {
        this.rows = rows;
        this.cols = cols;
        this.row = row;
        this.col = col;
    }

    public ArrayContext(T[] rows, T[] cols, int row, int col, int rowLength, int colLength) {
        this.rows = rows;
        this.cols = cols;
        this.row = row;
        this.col = col;
        this.rowLength = rowLength;
        this.colLength = colLength;
    }


    public  ArrayMatrix getArrayMatrix(){
        return this.matrix;
    }

    /**
     *
     * @param matrix  小矩阵
     * @param matrixIndex  当前矩阵的下标
     * @param matrixSize  小矩阵的大小
     * @param matrixtotal 一共多少矩阵
     */
    public  void createArrayMatrix(T[][] matrix, int matrixIndex, int matrixSize, int matrixtotal){

       this.matrix =   new ArrayMatrix(matrix,matrixIndex,matrixSize,matrixtotal);
    }
    @Data
        class  ArrayMatrix{
        private  T[][] matrix;//矩阵
        private  int matrixIndex;//当前矩阵下标  (从0开始)
        private int matrixSize; //单个矩阵数量
        private int matrixtotal; //一共多少矩阵


        public ArrayMatrix(T[][] matrix, int matrixIndex, int matrixSize, int matrixtotal) {
            this.matrix = matrix;
            this.matrixIndex = matrixIndex;
            this.matrixSize = matrixSize;
            this.matrixtotal = matrixtotal;
        }
    }
}
