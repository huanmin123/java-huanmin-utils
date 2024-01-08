package com.utils.file_tool.csv;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SimpCsvToObj<T> {
    private String separator=","  ;  //默认分隔符
    private List<T> result  ;        //返回结果
    private  File csvPath; //csv文件路径
    private   String[]  csvHead ;//csv文件头
    private  Class aClass; //泛型类型
    public SimpCsvToObj(File csvPath) {
        this.csvPath=csvPath;
        init();
    }
    public SimpCsvToObj(String separator,File csvPath) {
        this.separator = separator;
        this.csvPath=csvPath;
        init();
    }
    private  void  init(){
        try {
            aClass = deSerializable(getClass(), 0);//获取泛型类型
            readCsvHead();//读取csv文件头
            readCsv();//读取csv文件数据
        } catch (IOException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    //获取文件第一行数据
    private   void  readCsvHead() throws IOException {
        //读取csv文件开头一行数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(csvPath.toPath())));
        String line = bufferedReader.readLine();
        bufferedReader.close();
        csvHead=  line.split(separator);//返回csv文件头
    }



    private  void readCsv() throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(csvPath.toPath())));
        bufferedReader.readLine();//跳过表头
        result=new ArrayList<>();
        String line;
        while ((line=bufferedReader.readLine())!=null){
            String[] split = line.split(separator);
            Object o = aClass.newInstance();//创建一行数据
            for (int i = 0; i < split.length; i++) {
                Field declaredField = aClass.getDeclaredField(csvHead[i]);//获取字段
                declaredField.setAccessible(true);
                declaredField.set(o,getFieldValue(declaredField,split[i]));
            }
            result.add((T) o);
        }
    }

    //拿到类内部的泛型,的类型
    private static <T> Class<T> deSerializable(Class<T> clazz, int index) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[index];
        }
        throw new RuntimeException();
    }

    public  List<T> getResult(){
        return result;
    }

    //判断Field是什么类型,然后将字符串转换成对应的类型
    private static Object getFieldValue(Field field, String value) {
        Class<?> type = field.getType();
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Float.class || type == float.class) {
            return Float.parseFloat(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == Byte.class || type == byte.class) {
            return Byte.parseByte(value);
        } else if (type == Short.class || type == short.class) {
            return Short.parseShort(value);
        } else if (type == Character.class || type == char.class) {
            return value.charAt(0);
        } else {
            throw new RuntimeException("不支持的类型");
        }
    }



}

