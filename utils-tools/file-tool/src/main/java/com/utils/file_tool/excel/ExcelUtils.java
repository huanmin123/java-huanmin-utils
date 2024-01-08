package com.utils.file_tool.excel;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  本地 Excel表格  增删改查操作
 *  一定要注意不能有空值的出现 不然后期我们统一管理表格不好管理  如果非要使用空值 可以用null代替
 * 一个Excel表格如果有多张表(Excel)   没关系直接表名就行    注意的是如果你在Excel表格里手动添加的新的表或者修改了什么东西
 * 一定要关闭外部的Excel 否则java就有可能找不到表格 或者数据还是原来的数据
 *  我们自动识别xlss , xls 这两种格式的文件
 *  而sheet 可以理解为数据库内的 表
 * @author 胡安民
 */

public class ExcelUtils {




    // PATH 表示  Excel文件的位置  (可以理解为数据库)
    // 增删改 都需要此路径  而创建文件是单独自己指定路径
    //    public static String PATH = "C:\\Users\\12841\\Desktop\\tb_spec.xlsx";
    //    public static String PATH = "C:\\Users\\12841\\Desktop\\tb_spec.xls";
    public static String PATH = "C:\\Users\\12841\\Desktop\\2020100913421411.xlsx";


    /**
     * 查询一个数据
     *
     * @param id    定位行
     * @param cos   定位类列   注意下标从1开始  因为0是id列
     * @param sheet 表名
     * @return 通过行和列 获取到 指定单元格内的值
     */
    public static String getValue(String id, int cos, String sheet) {
        List<PageData> res = ExcelPOI.vttInit(PATH, sheet);
        for (int i = 0; i < res.size(); i++) {
            if (res.get(i).getString(0).equals(id)) {
                return res.get(i).getString(cos);
            }
        }
        return null;
    }




    /**
     * 获取一条数据
     *
     * @param id        表的id 列值的
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static Map<String, Object> selectById(String id, String sheetName) throws ParseException, IOException {
        List<PageData> list = ExcelPOI.select(PATH, sheetName);
        Map<String, Object> params = new HashMap<>();
        FileInputStream inp = new FileInputStream(PATH);
        if (ExcelPOI.type.equals("xlsx")) {
            XSSFWorkbook wb = new XSSFWorkbook(inp);
            XSSFSheet sheet = wb.getSheet(sheetName);
            //获得总列数
            int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
            if (list != null && list.size() > 0) {
                for (int i = 1; i < list.size(); i++) {
                    String idCol = list.get(i).getString(0);
                    if (id.equals(idCol)) {
                        for (int j = 0; j < coloumNum; j++) {
                            params.put(list.get(0).getString(j), list.get(i).getString(j));
                        }
                    }
                }
            }
            return params;
        }
        if (ExcelPOI.type.equals("xls")) {
            HSSFWorkbook wb = new HSSFWorkbook(inp);
            HSSFSheet sheet = wb.getSheet(sheetName);
            //获得总列数
            int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
            if (list != null && list.size() > 0) {
                for (int i = 1; i < list.size(); i++) {
                    String idCol = list.get(i).getString(0);
                    if (id.equals(idCol)) {
                        for (int j = 0; j < coloumNum; j++) {
                            params.put(list.get(0).getString(j), list.get(i).getString(j));
                        }
                    }
                }
            }

        }

        return params;
    }




    /**
     * 查询整个表数据
     *
     * @param sheetName
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static List<Map<String, Object>> selectList(String sheetName) throws ParseException, IOException {
        List<PageData> list = ExcelPOI.select(PATH, sheetName);
        FileInputStream inp = new FileInputStream(PATH);
        List<Map<String, Object>> recordList = new ArrayList<>();

        if (ExcelPOI.type.equals("xlsx")) {
            XSSFWorkbook wb = new XSSFWorkbook(inp);
            XSSFSheet sheet = wb.getSheet(sheetName);
            //获得总列数
            int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();

            Map<String, Object> params = new HashMap<>();
            if (list != null && list.size() > 0) {
                for (int i = 1; i < list.size(); i++) {
                    params = new HashMap<>();//一定要先置空 不然就没有索引
                    for (int j = 0; j < coloumNum; j++) {
                        params.put(list.get(0).getString(j), list.get(i).getString(j));
                    }
                    recordList.add(params);
                }
            }

            return recordList;
        }
        if (ExcelPOI.type.equals("xls")) {
            HSSFWorkbook wb = new HSSFWorkbook(inp);
            HSSFSheet sheet = wb.getSheet(sheetName);
            //获得总列数
            int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
            Map<String, Object> params = new HashMap<>();
            if (list != null && list.size() > 0) {
                for (int i = 1; i < list.size(); i++) {
                    params = new HashMap<>();//一定要先置空 不然就没有索引
                    for (int j = 0; j < coloumNum; j++) {
                        params.put(list.get(0).getString(j), list.get(i).getString(j));
                    }
                    recordList.add(params);
                }
            }
        }

        return recordList;
    }




    /**
     * 修改一条数据
     *
     * @param params    key 是 id 列    对应的 值 value  是
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @throws IOException
     * @throws ParseException
     */
    public static void update(Map<String, Object> params, String sheetName) throws IOException, ParseException {
        int colNum = 0;

        if (ExcelPOI.type.equals("xlsx")) {
            colNum = getColNum_xslx(sheetName);
        }

        if (ExcelPOI.type.equals("xls")) {
            colNum = getColNum_xsl(sheetName);
        }

        List<PageData> list = ExcelPOI.select(PATH, sheetName);
        if (list != null && list.size() > 0) {
            for (int i = 1; i < list.size(); i++) {
                String idCol = list.get(i).getString(0);
                String id = params.get("id") == null ? "" : params.get("id").toString();
                if (idCol.equals(id)) {
                    for (int j = 0; j < colNum; j++) {
                        String name = list.get(0).getString(j);
                        String record = params.get(name) == null ? "" : params.get(name).toString();
                        ExcelPOI.update(i - 1, j, record, PATH, sheetName);
                    }
                }
            }
        }
    }




    /**
     * 增加一条数据  (再次提醒不要有空值的出现  每一列都要添加数据  实在不想添加 可以为null  而不是空着  )
     *
     * @param params
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @throws IOException
     * @throws ParseException
     */
    public static void add(Map<String, Object> params, String sheetName) throws IOException, ParseException {
        PageData pd = new PageData();
        List<PageData> list = ExcelPOI.select(PATH, sheetName);
        pd = new PageData();
        int colNum = 0;
        if (ExcelPOI.type.equals("xlsx")) {
            colNum = getColNum_xslx(sheetName);
        }
        if (ExcelPOI.type.equals("xls")) {
            colNum = getColNum_xsl(sheetName);
        }


        for (int i = 0; i < colNum; i++) {
            String name = list.get(0).getString(i);
            String record = params.get(name) == null ? "" : params.get(name).toString();
            pd.put(i, record);
        }
        ExcelPOI.insert(pd, PATH, sheetName);
    }








    /**
     * 将实体类转换为Map<String, Object>的方式 增加一条数据  (再次提醒不要有空值的出现  每一列都要添加数据 实在不想添加可以为null  而不是空着  )
     *
     * @param obj
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @throws IOException
     * @throws ParseException
     */
    public static void add(Object obj, String sheetName) {
        Map<String,Object> map=convertToMap( obj);
        try {
            add(map,sheetName);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }






    /**
     * web
     * 将实体类转换为Map<String, Object>的方式 增加一条数据  (再次提醒不要有空值的出现  每一列都要添加数据 实在不想添加可以为null  而不是空着  )
     *
     * @param obj
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @throws IOException
     * @throws ParseException
     */
    public static void add(Object obj, String sheetName ,Object excel) {
        Map<String,Object> map=convertToMap( obj);
        try {
            add(map,sheetName, excel);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * WEB
     * 增加一条数据  (再次提醒不要有空值的出现  每一列都要添加数据  实在不想添加 可以为null  而不是空着  )
     *
     * @param params
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @throws IOException
     * @throws ParseException
     */
    public static void add(Map<String, Object> params, String sheetName,Object excel) throws IOException, ParseException {
        PageData pd = new PageData();
        List<PageData> list = ExcelPOI.select( excel, sheetName);
        pd = new PageData();
        int colNum = 0;
        if (excel instanceof XSSFWorkbook ) {
            XSSFWorkbook workbook = (XSSFWorkbook) excel;
            colNum = getColNum_xslx(sheetName,workbook);
        }
        if (excel instanceof  HSSFWorkbook) {
            HSSFWorkbook workbook = (HSSFWorkbook) excel;
            colNum = getColNum_xsl(sheetName,workbook);
        }


        for (int i = 0; i < colNum; i++) {
            String name = list.get(0).getString(i);
            String record = params.get(name) == null ? "" : params.get(name).toString();
            pd.put(i, record);
        }
        ExcelPOI.insert(pd, excel, sheetName);
    }


    /**
     * 删除一行数据
     *
     * @param id
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @throws IOException
     * @throws ParseException
     */
    public static void delete(String id, String sheetName) throws IOException, ParseException {
        PageData pd = new PageData();
        pd.put("content", "");
        List<PageData> list = ExcelPOI.select(PATH, sheetName);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String idCol = list.get(i).getString(0);
                if (idCol.equals(id)) {
                    ExcelPOI.delete(i, PATH, sheetName);
                }
            }
        }
    }





    /**
     * 获取列数
     *
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @return
     * @throws IOException
     */
    public static int getColNum_xslx(String sheetName) throws IOException {
        FileInputStream inp = new FileInputStream(PATH);
        XSSFWorkbook wb = new XSSFWorkbook(inp);
        XSSFSheet sheet = wb.getSheet(sheetName);
        //获得总列数
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
        return coloumNum;
    }

    /**
     * 获取列数(WEB)
     *
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @return
     * @throws IOException
     */
    public static int getColNum_xslx(String sheetName, XSSFWorkbook wb) throws IOException {
        XSSFSheet sheet = wb.getSheet(sheetName);
        //获得总列数
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
        return coloumNum;
    }


    /**
     * 获取列数
     *
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @return
     * @throws IOException
     */
    public static int getColNum_xsl(String sheetName) throws IOException {
        FileInputStream inp = new FileInputStream(PATH);
        HSSFWorkbook wb = new HSSFWorkbook(inp);
        HSSFSheet sheet = wb.getSheet(sheetName);
        //获得总列数
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
        return coloumNum;
    }

    /**
     * 获取列数(WEB)
     *
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @return
     * @throws IOException
     */
    public static int getColNum_xsl(String sheetName, HSSFWorkbook wb) throws IOException {
        HSSFSheet sheet = wb.getSheet(sheetName);
        //获得总列数
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
        return coloumNum;
    }




    /**
     * 获取行数
     *
     * @param sheetName 表名   ( 进入表格里 下面可以看到)
     * @throws IOException
     */
    public static int getRow(String sheetName) {

        int id = 0;
        if (ExcelPOI.type.equals("xlsx")) {
            XSSFWorkbook workbook = ExcelPOI.getExcelByPath_xslx(PATH);
            XSSFSheet sheet = workbook.getSheet(sheetName);

            id = ExcelPOI.getExcelRealRow(sheet) + 1;
        }
        if (ExcelPOI.type.equals("xls")) {
            HSSFWorkbook workbook = ExcelPOI.getExcelByPath_xsl(PATH);
            HSSFSheet sheet = workbook.getSheet(sheetName);

            id = ExcelPOI.getExcelRealRow(sheet) + 1;
        }
        return id;
    }





    //获取最大的 id值 (注意如果你id不是有序的 那么就不要使用)
    // 原理是 获取你内容的总条数 然后取值  也就是说取的是最后一条的id
    public static int getMaxId(String path, String sheets) {
        List<PageData> test = ExcelPOI.vttInit(path, sheets);
        PageData last = null;
        if (test.size() > 1) {
            last = test.get(test.size() - 1);
        } else if (test.size() == 1) {
            return 0;
        }
        return Integer.parseInt(last.getString(0));
    }



    //获取excel  表内 指定id 是否存在 如果存在 那么 返回 当前id 所在的条数  否则 -1
    public static int getXH(String id, String sheets) {
        List<PageData> res = ExcelPOI.vttInit(PATH, sheets);
        for (int i = 0; i < res.size(); i++) {
            if (res.get(i).getString(0).equals(id)) {
                return i;
            }
        }
        return -1;
    }



    /**
     * @param excelFile 你要创建的Excel  的存储路径和文件名  比如: C:\Users\12841\Desktop\20201009134214.xlsx
     * @param sheetName Excel 内表名 记得不能和其他表重复
     * @param cellArray 你要创建表的列名的数组  比如: ["id","name","age","sex"]
     *                  插入的时候也是按照这个顺序的  id列必须有 而且必须是第一个 否则此工具类的 增删改查方法 都将失败
     */
    public static void createExcel(String excelFile, String sheetName, String[] cellArray) {
        ExcelPOI.createExcel(excelFile, sheetName, cellArray);
    }



    

    /**
     * 反射创建Excel 通过实体类直接生成表和字段
     * @param excelFile 你要创建的Excel  的存储路径和文件名  比如: C:\Users\12841\Desktop\20201009134214.xlsx
     * @param obj   实体类
     */
    public static void createExcel(String excelFile,Class obj) {


        String name = obj.getName();  //获取类名
        name = name.substring(name.lastIndexOf(".")+1);
        Field[] declaredFields = obj.getDeclaredFields();
        String[] cellArray=new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            cellArray[i]=declaredFields[i].getName();
        }
        createExcel(excelFile, name, cellArray);

    }



    /**
     * 反射创建Excel 通过实体类直接生成表和字段   (Web)
    * @Author: HuAnmin
    * @email:  3426154361@qq.com
    * @Date:   2021/3/25 18:22
    * @param:  obj    实体类
    * @param:  suffix  文件后缀  xlsx  xls
    * @param:  response
    * @return: java.lang.Object
    * @Description: 方法功能描述....
    */

    public static  Object createExcel(Class obj,String suffix,HttpServletResponse response) {
        response.setContentType("application/ynd.ms-excel;charset=UTF-8");
        String name = obj.getName();  //获取类名
        name = name.substring(name.lastIndexOf(".")+1);
        Field[] declaredFields = obj.getDeclaredFields();
        String[] cellArray=new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            cellArray[i]=declaredFields[i].getName();
        }

        name+="."+suffix;
        if(name.contains("xlsx")|| name.contains("xls")){
            response.setHeader("Content-Disposition", "inline; filename="+name);
           return ExcelPOI.createExcel(name, cellArray);
        }
         return  null;
    }


        // 反射创建Excel 通过实体类直接生成表和字段  (Web)     (可以合并单元格)
    public static  Object createExcel(Class obj,String suffix,int firstRow, int lastRow, int firstCol, int lastCol,HttpServletResponse response) {
        response.setContentType("application/ynd.ms-excel;charset=UTF-8");
        String name = obj.getName();  //获取类名
        name = name.substring(name.lastIndexOf(".")+1);
        Field[] declaredFields = obj.getDeclaredFields();
        String[] cellArray=new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            cellArray[i]=declaredFields[i].getName();
        }

        name+="."+suffix;
        if(name.contains("xlsx")|| name.contains("xls")){
            response.setHeader("Content-Disposition", "inline; filename="+name);
            return ExcelPOI.createExcel(name, cellArray,firstRow,lastRow,firstCol,lastCol);
        }
        return  null;
    }




    //实体类的方式创建表同时插入数据
    /**
    * @Author: HuAnmin
    * @email:  3426154361@qq.com     
    * @Date:   2021/3/25 14:42
    * @param:  createExcelPath   创建的文件路径
    * @param:  obj   需要添加的值
    * @return: void
    * @Description: 方法功能描述....
    */
    
    public static void excelCreateAndInsert(String createExcelPath,List<Object> obj){
        createExcel(createExcelPath, obj.get(0).getClass()); //根据实体类创建表
        String name = obj.get(0).getClass().getName();  //获取类名
        name = name.substring(name.lastIndexOf(".")+1);
        for (Object o : obj) {
            add(o,name );
        }
    }



    public static Map convertToMap(Object obj) {
        try {
            if (obj instanceof Map) {
                return (Map)obj;
            }
            Map<String, String> returnMap = BeanUtils.describe(obj);
            returnMap.remove("class");
            return returnMap;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
            e1.printStackTrace();
        }
        return new HashMap();
    }








    //------------------------------------------ 以下代码都是 上面工具代码的底层代码  不要轻易修改 除非你能看得懂代码


    /**
     * poi实现excel文档操作  功能类
     *
     * @author Ivan
     */
    static public class ExcelPOI {

        public static String type;

        static {
            type = suffix(ExcelUtils.PATH);  //获取文件后缀  不带点
        }

        public static String suffix(String file) {
            File str1 = new File(file);
            //提取扩展名
            String str2 = str1.getPath().substring(str1.getPath().lastIndexOf(".") + 1);
            return str2;
        }


        // 当前只完成内容的模糊查询
        public static List<PageData> select(String path, String sheet) throws ParseException {
            List<PageData> res = vttInit(path, sheet);
            List<PageData> rs = new ArrayList<>();
            rs.add(res.get(0));
            for (int i = 1; i < res.size(); i++) {
                rs.add(res.get(i));
            }
            return rs;
        }
        // 当前只完成内容的模糊查询  WEB
        public static List<PageData> select(Object excel, String sheet) throws ParseException {
            List<PageData> res = vttInit(excel, sheet);
            List<PageData> rs = new ArrayList<>();
            rs.add(res.get(0));
            for (int i = 1; i < res.size(); i++) {
                rs.add(res.get(i));
            }
            return rs;
        }



        public static List<PageData> selectMore(String path, String sheet) throws ParseException {
            List<PageData> res = vttInit(path, sheet);
            List<PageData> rs = new ArrayList<>();
            rs.add(res.get(0));
            for (int i = 1; i < res.size(); i++) {
                rs.add(res.get(i));
            }
            return rs;
        }


        public static void insert(PageData pd, String path, String sheets) throws IOException {
            if (type.equals("xlsx")) {
                XSSFWorkbook workbook = getExcelByPath_xslx(path);
                XSSFSheet sheet = workbook.getSheet(sheets);
                XSSFRow row = sheet.getRow(0);
                int cell_end = row.getLastCellNum();// 这个就是列数
                int row_end = getExcelRealRow(sheet);// 这是行数
                row = sheet.createRow(row_end + 1);
                for (int i = 0; i < cell_end; i++) {
                    row.createCell(i).setCellValue(pd.getString(i));
                }
                FileOutputStream out = new FileOutputStream(path);
                workbook.write(out);
                out.close();
                return;
            }


            if (type.equals("xls")) {
                HSSFWorkbook workbook = getExcelByPath_xsl(path);
                HSSFSheet sheet = workbook.getSheet(sheets);
                HSSFRow row = sheet.getRow(0);
                int cell_end = row.getLastCellNum();// 这个就是列数
                int row_end = getExcelRealRow(sheet);// 这是行数
                row = sheet.createRow(row_end + 1);
                for (int i = 0; i < cell_end; i++) {
                    row.createCell(i).setCellValue(pd.getString(i));
                }
                FileOutputStream out = new FileOutputStream(path);
                workbook.write(out);
                out.close();
            }

        }

   //WEB
        public static void insert(PageData pd, Object excel, String sheets) throws IOException {
            if (excel instanceof XSSFWorkbook ) {
                XSSFWorkbook workbook = (XSSFWorkbook)excel;
                XSSFSheet sheet = workbook.getSheet(sheets);
                XSSFRow row = sheet.getRow(0);
                int cell_end = row.getLastCellNum();// 这个就是列数
                int row_end = getExcelRealRow(sheet);// 这是行数
                row = sheet.createRow(row_end + 1);
                for (int i = 0; i < cell_end; i++) {
                    row.createCell(i).setCellValue(pd.getString(i));
                }

                return;
            }

            if (excel instanceof  HSSFWorkbook ) {
                HSSFWorkbook workbook =( HSSFWorkbook) excel;
                HSSFSheet sheet = workbook.getSheet(sheets);
                HSSFRow row = sheet.getRow(0);
                int cell_end = row.getLastCellNum();// 这个就是列数
                int row_end = getExcelRealRow(sheet);// 这是行数
                row = sheet.createRow(row_end + 1);
                for (int i = 0; i < cell_end; i++) {
                    row.createCell(i).setCellValue(pd.getString(i));
                }
            }


        }


        public static void delete(int rowIndex, String path, String sheets) throws IOException {
            if (type.equals("xlsx")) {

                XSSFWorkbook workbook = getExcelByPath_xslx(path);
                XSSFSheet sheet = workbook.getSheet(sheets);
                int lastRowNum = sheet.getLastRowNum();
                if (rowIndex >= 0 && rowIndex < lastRowNum){
                    sheet.shiftRows(rowIndex + 1, lastRowNum, -1);// 将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行

                }
                if (rowIndex == lastRowNum) {
                    XSSFRow removingRow = sheet.getRow(rowIndex);
                    if (removingRow != null){
                        sheet.removeRow(removingRow);
                    }

                }
                FileOutputStream out = new FileOutputStream(path);
                workbook.write(out);
                out.close();
                return;
            }

            if (type.equals("xls")) {

                HSSFWorkbook workbook = getExcelByPath_xsl(path);
                HSSFSheet sheet = workbook.getSheet(sheets);
                int lastRowNum = sheet.getLastRowNum();
                if (rowIndex >= 0 && rowIndex < lastRowNum){
                    sheet.shiftRows(rowIndex + 1, lastRowNum, -1);// 将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行

                }
                if (rowIndex == lastRowNum) {
                    HSSFRow removingRow = sheet.getRow(rowIndex);
                    if (removingRow != null){
                        sheet.removeRow(removingRow);

                    }
                }
                FileOutputStream out = new FileOutputStream(path);
                workbook.write(out);
                out.close();
            }


        }

        /**
         * @param rowNum 行数
         * @param colNum 列数
         * @param value
         * @param path
         * @param sheets
         * @throws IOException
         */
        public static void update(int rowNum, int colNum, String value, String path, String sheets) throws IOException {

            if (type.equals("xlsx")) {
                XSSFWorkbook workbook = getExcelByPath_xslx(path);
                XSSFSheet sheet = workbook.getSheet(sheets);
                XSSFRow row = sheet.getRow(rowNum + 1);
                XSSFCell cell = row.getCell(colNum);
                if (cell == null) {
                    row.createCell(colNum).setCellValue(value);
                } else {
                    row.getCell(colNum).setCellValue(value);
                }
                FileOutputStream out = new FileOutputStream(path);
                workbook.write(out);
                out.close();
                return;
            }

            if (type.equals("xls")) {
                HSSFWorkbook workbook = getExcelByPath_xsl(path);
                HSSFSheet sheet = workbook.getSheet(sheets);
                HSSFRow row = sheet.getRow(rowNum + 1);
                HSSFCell cell = row.getCell(colNum);
                if (cell == null) {
                    row.createCell(colNum).setCellValue(value);
                } else {
                    row.getCell(colNum).setCellValue(value);
                }
                FileOutputStream out = new FileOutputStream(path);
                workbook.write(out);
                out.close();
            }
        }

        public static List<PageData> vttInit(String path, String sheets) {
            List<PageData> rs = new ArrayList<>();

            if (type.equals("xls")) {
                HSSFWorkbook workbook = getExcelByPath_xsl(path);
                HSSFSheet sheet = workbook.getSheet(sheets);
                // 得到Excel表格
                HSSFRow row = sheet.getRow(0);
                // 得到Excel工作表指定行的单元格
                HSSFCell cell = row.getCell(0);
                int cell_end = row.getLastCellNum();// 这个就是列数(标题)
                int row_end = getExcelRealRow(sheet);// 这是行数
                for (int i = 0; i <= row_end; i++) {
                    row = sheet.getRow(i);
                    PageData exd = new PageData();
                    for (int j = 0; j <= cell_end + 1; j++) {
                        if (row != null && i == 0 && j == cell_end) {
                            exd.put(j, "");
                            continue;
                        } else if (row != null && i == 0 && j == cell_end + 1) {
                            exd.put(j, "");
                            continue;
                        } else if (row != null && j < cell_end){
                            cell = row.getCell(j);
                        } else if (i != 0 && j == cell_end){
                            continue;
                        }
                        if (cell != null){
                            exd.put(j, formatCell(cell));
                        } else {
                            exd.put(j, "");
                        }
                    }
                    exd.put("xh", i + "");
                    rs.add(exd);

                }
                return rs;
            }


            if (type.equals("xlsx")) {
                XSSFWorkbook workbook = getExcelByPath_xslx(path);
                XSSFSheet sheet = workbook.getSheet(sheets);
                // 得到Excel表格
                XSSFRow row = sheet.getRow(0);
                // 得到Excel工作表指定行的单元格
                XSSFCell cell = row.getCell(0);
                int cell_end = row.getLastCellNum();// 这个就是列数(标题)
                int row_end = getExcelRealRow(sheet);// 这是行数
                for (int i = 0; i <= row_end; i++) {
                    row = sheet.getRow(i);
                    PageData exd = new PageData();
                    for (int j = 0; j <= cell_end + 1; j++) {
                        if (row != null && i == 0 && j == cell_end) {
                            exd.put(j, "");
                            continue;
                        } else if (row != null && i == 0 && j == cell_end + 1) {
                            exd.put(j, "");
                            continue;
                        } else if (row != null && j < cell_end){
                            cell = row.getCell(j);
                        } else if (i != 0 && j == cell_end){
                            continue;
                        }if (cell != null){
                            exd.put(j, formatCell(cell));
                        } else {
                            exd.put(j, "");
                        }
                    }
                    exd.put("xh", i + "");
                    rs.add(exd);
                }
            }
            return rs;
        }







        //WEB
        public static List<PageData> vttInit(Object excxl, String sheets) {
            List<PageData> rs = new ArrayList<>();

            if ( excxl instanceof   HSSFWorkbook) {
                HSSFWorkbook workbook = (HSSFWorkbook) excxl;
                HSSFSheet sheet = workbook.getSheet(sheets);
                // 得到Excel表格
                HSSFRow row = sheet.getRow(0);
                // 得到Excel工作表指定行的单元格
                HSSFCell cell = row.getCell(0);
                int cell_end = row.getLastCellNum();// 这个就是列数(标题)
                int row_end = getExcelRealRow(sheet);// 这是行数
                for (int i = 0; i <= row_end; i++) {
                    row = sheet.getRow(i);
                    PageData exd = new PageData();
                    for (int j = 0; j <= cell_end + 1; j++) {
                        if (row != null && i == 0 && j == cell_end) {
                            exd.put(j, "");
                            continue;
                        } else if (row != null && i == 0 && j == cell_end + 1) {
                            exd.put(j, "");
                            continue;
                        } else if (row != null && j < cell_end){
                            cell = row.getCell(j);
                        } else if (i != 0 && j == cell_end){
                            continue;
                        }
                        if (cell != null){
                            exd.put(j, formatCell(cell));
                        } else {
                            exd.put(j, "");
                        }
                    }
                    exd.put("xh", i + "");
                    rs.add(exd);

                }
                return rs;
            }


            if ( excxl instanceof   XSSFWorkbook) {
                XSSFWorkbook workbook = ( XSSFWorkbook) excxl;
                XSSFSheet sheet = workbook.getSheet(sheets);
                // 得到Excel表格
                XSSFRow row = sheet.getRow(0);
                // 得到Excel工作表指定行的单元格
                XSSFCell cell = row.getCell(0);
                int cell_end = row.getLastCellNum();// 这个就是列数(标题)
                int row_end = getExcelRealRow(sheet);// 这是行数
                for (int i = 0; i <= row_end; i++) {
                    row = sheet.getRow(i);
                    PageData exd = new PageData();
                    for (int j = 0; j <= cell_end + 1; j++) {
                        if (row != null && i == 0 && j == cell_end) {
                            exd.put(j, "");
                            continue;
                        } else if (row != null && i == 0 && j == cell_end + 1) {
                            exd.put(j, "");
                            continue;
                        } else if (row != null && j < cell_end){
                            cell = row.getCell(j);
                        } else if (i != 0 && j == cell_end){
                            continue;
                        }if (cell != null){
                            exd.put(j, formatCell(cell));
                        } else {
                            exd.put(j, "");
                        }
                    }
                    exd.put("xh", i + "");
                    rs.add(exd);
                }
            }
            return rs;
        }













        // 获取Excel表的真实行数
        public static int getExcelRealRow(Sheet sheet) {
            boolean flag = false;
            for (int i = 1; i <= sheet.getLastRowNum(); ) {
                Row r = sheet.getRow(i);
                if (r == null) {
                    // 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
                    sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                    continue;
                }
                flag = false;
                for (Cell c : r) {
                    if (c.getCellType() != CellType.BLANK) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    i++;
                    continue;
                } else {
                    // 如果是空白行（即可能没有数据，但是有一定格式）
                    if (i == sheet.getLastRowNum()){// 如果到了最后一行，直接将那一行remove掉
                        sheet.removeRow(r);
                    } else {// 如果还没到最后一行，则数据往上移一行
                        sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                    }

                }
            }
            return sheet.getLastRowNum();
        }

        /**
         * 通过文件路劲获取excel文件  (xsls格式)
         *
         * @param path
         * @return XSSFWorkbook
         */
        public static XSSFWorkbook getExcelByPath_xslx(String path) {
            try {
                byte[] buf = IOUtils.toByteArray(new FileInputStream(path));//execelIS为InputStream流
                //在需要用到InputStream的地方再封装成InputStream
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                XSSFWorkbook workbook = new XSSFWorkbook(byteArrayInputStream);
                return workbook;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 通过文件路劲获取excel文件   (xslx格式)
         *
         * @param path
         * @return HSSFWorkbook
         */
        public static HSSFWorkbook getExcelByPath_xsl(String path) {
            try {
                byte[] buf = IOUtils.toByteArray(new FileInputStream(path));//execelIS为InputStream流
                //在需要用到InputStream的地方再封装成InputStream
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                HSSFWorkbook workbook = new HSSFWorkbook(byteArrayInputStream);
                return workbook;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         * 通过文件获取excel对象
         *
         * @param file
         * @return
         */
        public HSSFWorkbook getExcelByFile(File file) {
            try {
                POIFSFileSystem fspoi = new POIFSFileSystem(new FileInputStream(file.getPath()));
                HSSFWorkbook workbook = new HSSFWorkbook(fspoi);
                return workbook;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         * @param excelFile 你要创建的Excel  的存储路径和文件名  比如: C:\Users\12841\Desktop\20201009134214.xlsx
         * @param sheetName Excel 内表名 记得不能和其他表重复
         * @param cellArray 你要创建表的列名的数组  比如: ["id","name","age","sex"]
         *                  插入的时候也是按照这个顺序的  id列必须有 而且必须是第一个 否则此工具类的 增删改查方法 都将失败
         */

        public static void createExcel(String excelFile, String sheetName, String[] cellArray) {


            if (type.equals("xlsx")) {
                // 创建工作薄对象
                XSSFWorkbook workbook = new XSSFWorkbook();
                // 创建工作表对象
                XSSFSheet sheet = workbook.createSheet(sheetName); // 这里也可以设置sheet的Name
                // 创建工作表的行
                XSSFRow row = sheet.createRow(0);// 设置第一行，从零开始
                for (int i = 0; i < cellArray.length; i++) {
                    row.createCell(i).setCellValue(cellArray[i]);
                }
                try {
                    // 文档输出
                    FileOutputStream out = new FileOutputStream(excelFile);
                    workbook.write(out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            if (type.equals("xls")) {
                // 创建工作薄对象
                HSSFWorkbook workbook = new HSSFWorkbook();// 这里也可以设置sheet的Name
                // 创建工作表对象
                HSSFSheet sheet = workbook.createSheet(sheetName);
                // 创建工作表的行
                HSSFRow row = sheet.createRow(0);// 设置第一行，从零开始
                for (int i = 0; i < cellArray.length; i++) {
                    row.createCell(i).setCellValue(cellArray[i]);
                }
                try {
                    // 文档输出
                    FileOutputStream out = new FileOutputStream(excelFile);
                    workbook.write(out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public static  Object  createExcel(String sheetName, String[] cellArray) {

            type=suffix(sheetName);
            String name=sheetName;
            name=name.substring(0,name.lastIndexOf("."));
            sheetName=name;
            if ("xlsx".equals(type)) {
                // 创建工作薄对象
                XSSFWorkbook workbook = new XSSFWorkbook();
                // 创建工作表对象
                XSSFSheet sheet = workbook.createSheet(sheetName); // 这里也可以设置sheet的Name
                // 创建工作表的行
                XSSFRow row = sheet.createRow(0);// 设置第一行，从零开始
                for (int i = 0; i < cellArray.length; i++) {
                    row.createCell(i).setCellValue(cellArray[i]);
                }
                return  workbook;
            }
            if ("xlsx".equals(type)) {
                // 创建工作薄对象
                HSSFWorkbook workbook = new HSSFWorkbook();// 这里也可以设置sheet的Name
                // 创建工作表对象
                HSSFSheet sheet = workbook.createSheet(sheetName);
                // 创建工作表的行
                HSSFRow row = sheet.createRow(0);// 设置第一行，从零开始
                for (int i = 0; i < cellArray.length; i++) {
                    row.createCell(i).setCellValue(cellArray[i]);
                }
                return  workbook;
            }
            return  null;

        }


        public static  Object  createExcel(String sheetName, String[] cellArray,int firstRow, int lastRow, int firstCol, int lastCol) {

            type=suffix(sheetName);
            String name=sheetName;
            name=name.substring(0,name.lastIndexOf("."));
            sheetName=name;
            if ("xlsx".equals(type)) {
                // 创建工作薄对象
                XSSFWorkbook workbook = new XSSFWorkbook();
                // 创建工作表对象
                XSSFSheet sheet = workbook.createSheet(sheetName); // 这里也可以设置sheet的Name
                //合并单元格
                sheet.addMergedRegion(new CellRangeAddress(firstRow,lastRow,firstCol,lastCol));
                // 创建工作表的行
                XSSFRow row = sheet.createRow(0);// 设置第一行，从零开始
                for (int i = 0; i < cellArray.length; i++) {
                    row.createCell(i).setCellValue(cellArray[i]);
                }
                return  workbook;
            }
            if ("xlsx".equals(type)) {
                // 创建工作薄对象
                HSSFWorkbook workbook = new HSSFWorkbook();// 这里也可以设置sheet的Name
                // 创建工作表对象
                HSSFSheet sheet = workbook.createSheet(sheetName);
                // 创建工作表的行
                HSSFRow row = sheet.createRow(0);// 设置第一行，从零开始
                for (int i = 0; i < cellArray.length; i++) {
                    row.createCell(i).setCellValue(cellArray[i]);
                }
                return  workbook;
            }
            return  null;

        }



        public static String formatCell(Cell cell) {
            String ret;
            switch (cell.getCellType()) {
                case STRING:
                    ret = cell.getStringCellValue();
                    break;
                case FORMULA:
                    Workbook wb = cell.getSheet().getWorkbook();
                    CreationHelper crateHelper = wb.getCreationHelper();
                    FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                    ret = formatCell(evaluator.evaluateInCell(cell));
                    break;
                case NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                        SimpleDateFormat sdf = null;
                        if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                            sdf = new SimpleDateFormat("HH:mm");
                        } else {// 日期
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                        }
                        Date date = cell.getDateCellValue();
                        ret = sdf.format(date);
                    } else if (cell.getCellStyle().getDataFormat() == 58) {
                        // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        double value = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(value);
                        ret = sdf.format(date);
                    } else {
                        ret = NumberToTextConverter.toText(cell.getNumericCellValue());
                    }
                    break;
                case BLANK:
                    ret = "";
                    break;
                case BOOLEAN:
                    ret = String.valueOf(cell.getBooleanCellValue());
                    break;
                case ERROR:
                    ret = null;
                    break;
                default:
                    ret = null;
            }
            return ret; // 有必要自行trim
        }
    }

    //实体类

    static public class PageData extends HashMap implements Map, Serializable {
        private static final long serialVersionUID = 1L;
        Map map = null;
        String request;

        public PageData(String request) {
            this.request = request;
            Map properties = stringToMap(request);
            Map returnMap = new HashMap();
            Iterator entries = properties.entrySet().iterator();
            Entry entry;
            String name = "";
            String value = "";
            while (entries.hasNext()) {
                entry = (Entry) entries.next();
                name = (String) entry.getKey();
                Object valueObj = entry.getValue();
                if (null == valueObj) {
                    value = "";
                } else if (valueObj instanceof String[]) {
                    String[] values = (String[]) valueObj;
                    for (int i = 0; i < values.length; i++) {
                        value = values[i] + ",";
                    }
                    value = value.substring(0, value.length() - 1);
                } else {
                    value = valueObj.toString();
                }
                returnMap.put(name, value);
            }

            map = returnMap;
        }

        public PageData() {
            map = new HashMap();
        }

        public PageData(ResultSet res) {
            Map returnMap = new HashMap();
            try {
                ResultSetMetaData rsmd = res.getMetaData();
                int count = rsmd.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    String key = rsmd.getColumnLabel(i);
                    String value = res.getString(i);
                    returnMap.put(key, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            map = returnMap;
        }


        public Map stringToMap(String request) {
            String res[] = request.split("&");
            Map resMap = new HashMap<>();
            for (int i = 0; i < res.length; i++) {
                String obj[] = res[i].split("=");
                resMap.put(obj[0], obj[1]);
            }
            return resMap;

        }

        public String getString(Object key) {
            return (String) map.get(key);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object put(Object key, Object value) {
            return map.put(key, value);
        }

        @Override
        public Object remove(Object key) {
            return map.remove(key);
        }
        @Override
        public void clear() {
            map.clear();
        }
        @Override
        public boolean containsKey(Object key) {
            return map.containsKey(key);
        }
        @Override
        public boolean containsValue(Object value) {
            return map.containsValue(value);
        }
        @Override
        public Set entrySet() {
            return map.entrySet();
        }
        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }
        @Override
        public Set keySet() {
            return map.keySet();
        }
        @Override
        @SuppressWarnings("unchecked")
        public void putAll(Map t) {
            map.putAll(t);
        }
        @Override
        public int size() {
            return map.size();
        }
        @Override
        public Collection values() {
            return map.values();
        }
    }
}

