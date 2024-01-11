package com.huanmin.utils.common.container;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 遍历和处理二维矩阵
 * @Description:
 * 遍历和处理二维矩阵   (只适用int,Integer,和double,Double,等这些类型)(一般矩阵都是长和宽都是一样的,如果不够那么自行补充0,或者null,保证长宽一致)
 */
@Slf4j
public class ArrayMatrixUtil {


    //二维数组,基本类型(int)转包装类型(Integer)
    public static Integer[][] intToIntegerByMatrix(int[][] arrays) {
        int length = arrays.length;
        Integer[][] integers = new Integer[length][length];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = ContainerTransformUtil.intToInteger(arrays[i]);
        }
        return integers;
    }
    //二维数组,基本类型(double)转包装类型(Double)
    public static Double[][] doubleToDoubleByMatrix(double[][] doubles) {
        int length = doubles.length;
        Double[][] doubles1 = new Double[length][length];
        for (int i = 0; i < doubles1.length; i++) {
            doubles1[i] = ContainerTransformUtil.doubleToDouble(doubles[i]);
        }
        return doubles1;
    }

    // 行 ,列 遍历
    /**
     * @param objects
     * @param consumer 返回行,列  ,比如: 1,1  ,2,2, 3,3
     * @param <T>
     */
    public static <T> void arrayRowAndColByIteration(T[][] objects, Consumer<ArrayContext<T>> consumer) {

        //获取矩阵长度
        int length = objects.length;
        for (int i = 0; i < length; i++) {
            //获取行
            T[] row = objects[i];
            //获取列
            T[] col = (T[]) new Object[length];
            for (int i1 = 0; i1 < length; i1++) {
                col[i1] = objects[i1][i];
            }
            ArrayContext<T> arrayContext = new ArrayContext<T>(row, col, i + 1, i + 1, row.length, col.length);
            consumer.accept(arrayContext);
        }
    }

    /**
     * 三角遍历  (比直接行列遍历要节约内存,并且后期进行处理的数据也会变少)
     *
     * @param objects
     * @param consumer
     * @param type(必须) 是否包含相交值 ,如果包含那么列和行的相交值都会读取 , 1是(行和列都包含) , 2否(行包含,列取消),3否(列包含,行取消)
     */

    public static <T> void arrayRowAndColByTriangleByIteration(T[][] objects, Consumer<ArrayContext<T>> consumer, int type) {
        //获取矩阵长度
        int length = objects.length;

        for (int i = 0; i < length; i++) {

            int rowLen = 0; //行的数量
            int rowindex = 0;//行的起始位置
            int colLen = length - i; //列的数量
            int colindex = 0;//列的起始位置

            //调整相交值的获取方式
            switch (type) {
                case 1:
                    rowLen = length - i;
                    rowindex = i;
                    colLen = rowLen;
                    colindex = i;
                    break;
                case 2:
                    rowLen = length - i;
                    rowindex = i;
                    colLen = rowLen - 1;
                    colindex = i + 1;
                    break;
                case 3:
                    colLen = length - i;
                    colindex = i;
                    rowLen = colLen - 1;
                    rowindex = i + 1;
                    break;
                default:
                    return;
            }

            T[] row = objects[i];
            //改变行的内容
            T[] newRow = (T[]) new Object[rowLen];
            ArrayCopyUtils.copyStart(row, newRow, rowindex);
            //获取列
            T[] col = (T[]) new Object[length];
            for (int i1 = 0; i1 < length; i1++) {
                col[i1] = objects[i1][i];
            }
            //改变列的内容
            T[] newCol = (T[]) new Object[colLen];
            ArrayCopyUtils.copyStart(col, newCol, colindex);
            ArrayContext<T> arrayContext = new ArrayContext<T>(newRow, newCol, i + 1, i + 1, newRow.length, newCol.length);
            consumer.accept(arrayContext);
        }
    }

    /**
     * 在大的矩阵中,搜索小的矩阵,返回小的矩阵(原样还原)    ,(都是从0开始)
     *
     * @param objects  数据源[][]
     * @param rowStart 起始行  x
     * @param rowEnd   结束行
     * @param colStart 起始列  y
     * @param colEnd   结束列
     * @param collect  矩阵汇总(转一维数组)  ,可以为null      (通过外部传进来的数组,我们将查询出的矩阵,添加到数组中,按照行的顺序)
     * @param consumer 矩阵行列处理 ,可以为null    (将获取到的矩阵,进行处理,每次获取一行和一列)
     * @return 返回查询的矩阵
     */
    public static <T> T[][] arrayInsideMatrixOne(T[][] objects, int rowStart, int rowEnd, int colStart, int colEnd, Object[] collect, Consumer<ArrayContext<T>> consumer) {
        int row =  Math.abs(rowEnd - rowStart); //行
        int col = Math.abs(colEnd - colStart) ; //列
        //使用Array创建二维数组,不会导致转换异常
        //获取小矩阵
        T[][] newMatrix = (T[][]) Array.newInstance(objects[0].getClass().getComponentType(), row, col);
        //读取前进行效验,行溢出调整
        int length = objects.length;
        if (rowEnd > length) {
            //重新定位行结尾,复制剩下的
            rowEnd = length ;
        }

        int newMatrixIndex = 0; //小矩阵下标
        for (int i = rowStart, j = 0; i < rowEnd; i++, j++) {
            T[] object = objects[i];
            ArrayCopyUtils.copy(object, newMatrix[newMatrixIndex++], colStart, colEnd);
        }

        if (collect != null) {
            T[] newArrays = createCollectMatrix(newMatrix, row * col);
            ArrayCopyUtils.copyAll(newArrays, collect);
        }
        //回调处理
        if (consumer != null) {
            arrayRowAndColByIteration(newMatrix, consumer);
        }
        return newMatrix;
    }

    //矩阵转数组
    public static <T> T[] createCollectMatrix(T[][] newMatrix, int size) {
        AtomicInteger i = new AtomicInteger();
        T[] newArrays = (T[]) Array.newInstance(newMatrix[0].getClass().getComponentType(), size);
        //小矩阵数据汇总
        arrayRowAndColByIteration(newMatrix, (data) -> {
            T[] rows = data.getRows();
            ArrayCopyUtils.copyTargetStart(rows, newArrays, i.get());
            i.addAndGet(rows.length);

        });
        return newArrays;
    }

    /**
     * 依据指定的矩阵大小进行按照从左到右从上到下遍历,如果不够那么就自行换行补充
     * @param objects
     * @param consumer
     * @param algorithm   3*3 , 4,4, 4*3  ,3*4
     * @param <T>
     */
    public static  <T> void arrayInsideMatrixByIteration(T[][] objects, String algorithm, Consumer<ArrayContext<T>> consumer) {
        int length = objects.length;
        String[] split = algorithm.split("\\*");
        int row = Integer.parseInt(split[0]);//行
        int col = Integer.parseInt(split[1]);//列
        //算出一个矩阵的数量
        int matrixSize = row * col;
        //算出一行有几个小矩阵 (边界)
        int rowMatrix = (int) Math.ceil((double) length / row);
        //算出列有几个小矩阵 (边界)
        int colMatrix = (int) Math.ceil((double) length / col);
        // 算出总数中有几个小矩阵
        int matrixtotal = rowMatrix*colMatrix;
        //当前小矩形遍历的指针
        int lineFeed = 0;
        //换行的次数
        int changeLineCount = 0;
        //读取的row1指针位置
        int row1 = 0;
        //读取的row2指针位置
        int row2 = row;
        //读取的col1指针位置
        int col1 = 0;
        //读取的col2指针位置
        int col2 = col;
        // 当前指针(总指针)
        int pointer=0;
        while (changeLineCount < colMatrix) {
            lineFeed++;
            // 每次读取一个矩阵
            T[][] ts = arrayInsideMatrixOne(objects, row1, row2, col1, col2, null, null);
            //移动小矩形向右读取指针
            col1 += col;
            col2 += col;
            //矩阵遍历处理
            if(consumer!=null){
                ArrayContext<T> tArrayContext = new ArrayContext<>();
                //初始化矩阵数据
                tArrayContext.createArrayMatrix(ts,pointer++,matrixSize,matrixtotal);
                consumer.accept(tArrayContext);
            }
            //换行
            if (lineFeed == rowMatrix) {
                //换行
                row1 += row;
                row2 += row;
                //列从头读取
                col1 = 0;
                col2 = col;
                //重置指针
                lineFeed = 0;
                //增加换行记录
                changeLineCount++;
            }

        }
        if(matrixtotal==pointer){
            System.out.println("-----矩阵遍历读取完毕--------");
        }else {
            log.error("-----矩阵读取有误---读取小矩阵的数量不对--------");
        }
    }




    //获取指定x,y和目标xy 相交一周的所有点位,(去掉重复的)
    public  static  List<int[]> aroundCoordinate(int x,int y,int x1,int y1){
       // 根据开始地点和结束地点计算最小边界圈
       Set<String> set = new HashSet<>();
        //判断y谁是小的谁是大的
        int miny= Math.min(y, y1);
        int maxy=Math.max(y,  y1);
        //判断X谁是小的谁是大的
        int minx= Math.min(x, x1);
        int maxx=Math.max(x, x1);
        //算出所有的宽,的点位
        for (int i = miny,j=maxy; i <= maxy; i++,j--) {
            set.add(minx+","+i );
            set.add(maxx+","+j );
        }
        //算出所有高的点位
        for (int i = minx,j=maxx; i <=maxx; i++,j--) {
            set.add(i+","+miny);
            set.add(j+","+maxy);
        }

        List<int[]> list = new ArrayList<>();
        set.forEach(data->{
            String[] split = data.split(",");
            list.add(new int[]{Integer.parseInt(split[0]),Integer.parseInt(split[1])});
        });

        return list;
    }



    //查询二维数组
    public static <T> void show(T[][] integers2) {
        for (T[] ts : integers2) {
            System.out.println(Arrays.toString(ts));
        }
    }























    @Data
    public static class ArrayContext<T> {
        private  T[] rows;
        private T[] cols;
        private int row; //下标  (从0开始)
        private int col; //下标  (从0开始)
        private  int rowLength; //行长度
        private  int colLength; //列长度

        private ArrayMatrix<T> matrix;//小矩阵

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


        public ArrayMatrix<T> getArrayMatrix(){
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

            this.matrix = new ArrayMatrix<T>(matrix, matrixIndex, matrixSize, matrixtotal);
        }

    }
    @Data
    public static class  ArrayMatrix<T>{
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
