package org.huanmin.file_tool.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

//网页下载版
public class ExcelDownLoadUtils {

    /**
     * @Author: HuAnmin
     * @email:  3426154361@qq.com
     * @Date:   2021/3/26 7:52
     * @param:  obj   数据    (同时创建此数据类型的实体类的表)
     * @param:  suffix   创建表的后缀    xlsx  xls
     * @param:  response
     * @return: void
     * @Description:  反射的方式创建一个excel同时插入录入数据并下载
     */

    public static void downloadExcel(List<Object> obj, String suffix, HttpServletResponse response) throws IOException {



        Object excel = ExcelUtils.createExcel(obj.get(0).getClass(),suffix, response);
        String name = obj.get(0).getClass().getName();  //获取类名
        name = name.substring(name.lastIndexOf(".")+1);
        if (Objects.nonNull(excel)){
            if ( excel instanceof XSSFWorkbook) {
                XSSFWorkbook workbook = (XSSFWorkbook) excel;

                for (Object o : obj) {
                    ExcelUtils.add(o ,name,workbook );
                }

                workbook.write(response.getOutputStream());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                workbook .write(os);
                byte[] bytes = os.toByteArray();
                // 获取响应报文输出流对象
                ServletOutputStream outputStream = response.getOutputStream();
                // 输出
                outputStream .write(bytes);
                outputStream .flush();
                outputStream .close();
            }
            if ( excel instanceof  HSSFWorkbook ) {
                HSSFWorkbook workbook = (HSSFWorkbook) excel;
                for (Object o : obj) {
                    ExcelUtils.add(o ,name,workbook );
                }
                workbook.write(response.getOutputStream());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                workbook .write(os);
                byte[] bytes = os.toByteArray();
                // 获取响应报文输出流对象
                ServletOutputStream outputStream = response.getOutputStream();
                // 输出
                outputStream .write(bytes);
                outputStream .flush();
                outputStream .close();
            }


        }


    }


    /**
     * 反射的方式创建一个excel同时插入录入数据并下载   (合并单元格版 )
     * @param obj    数据    (同时创建此数据类型的实体类的表)
     * @param suffix  创建表的后缀    xlsx  xls
     * @param firstRow 行的开始位置   (从0开始)
     * @param lastRow   行的结束位置  (从0开始)
     * @param firstCol  列的开始位置  (从0开始)
     * @param lastCol   列的结束位置  (从0开始)
     *  从无论是行还是列都是从0开始
     * @param response
     *
     */
    public static void downloadExcel(List<Object> obj,String suffix, int firstRow, int lastRow, int firstCol, int lastCol,HttpServletResponse response) throws IOException {

        Object excel = ExcelUtils.createExcel(obj.get(0).getClass(),suffix,firstRow,lastRow,firstCol,lastCol, response);
        String name = obj.get(0).getClass().getName();  //获取类名
        name = name.substring(name.lastIndexOf(".")+1);
        if (Objects.nonNull(excel)){
            if ( excel instanceof  XSSFWorkbook ) {
                XSSFWorkbook workbook = (XSSFWorkbook) excel;

                for (Object o : obj) {
                    ExcelUtils.add(o ,name,workbook );
                }

                workbook.write(response.getOutputStream());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                workbook .write(os);
                byte[] bytes = os.toByteArray();
                // 获取响应报文输出流对象
                ServletOutputStream outputStream = response.getOutputStream();
                // 输出
                outputStream .write(bytes);
                outputStream .flush();
                outputStream .close();
            }
            if ( excel instanceof HSSFWorkbook) {
                HSSFWorkbook workbook = (HSSFWorkbook) excel;
                for (Object o : obj) {
                    ExcelUtils.add(o ,name,workbook );
                }
                workbook.write(response.getOutputStream());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                workbook .write(os);
                byte[] bytes = os.toByteArray();
                // 获取响应报文输出流对象
                ServletOutputStream outputStream = response.getOutputStream();
                // 输出
                outputStream .write(bytes);
                outputStream .flush();
                outputStream .close();
            }


        }


    }

}
