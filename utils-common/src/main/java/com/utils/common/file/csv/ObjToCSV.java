package com.utils.common.file.csv;

import com.date.LocalDateUtils;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * 对象转csv格式
 *
 * @describe
 * 1百万数据测试 2~3秒
 * 调用顺序如下(根据情况自行选择)
 *         String s = ObjToCSV.create(userDatas)
 *                 .fieldsAll()   //转换全部列
 *                 .addIncludeFields() //只包含某些列
 *                 .addExcludeFields("roleData") //排除某些列
 *                 .addHead() //添加头部字段
 *                 .addContent() //一个对象(一行)
 *                 .addContents() //多个对象(多行)
 *                 .addEnding((data)->{ //结尾说明
 *                     JSONObject jsonObject = new JSONObject();
 *                     jsonObject.put("count",data.getNumber());
 *                     jsonObject.put("time",data.getCreateDate());
 *                     return  jsonObject.toJSONString();
 *                 })
 *                 .ToString(); //结果
 */
@Data
public class ObjToCSV<T> {

    private String separator = "\u0001|\u0001";  //默认分隔符
    private StringBuilder str = new StringBuilder();
    private T object;
    private List<T> objects;
    private List<String> excludeFields = new ArrayList<String>(100) {{
        add("serialVersionUID");
    }};
    private List<String> includeFields = new ArrayList<String>(100);
    private boolean fieldsAll = false;
    private  int number;//多少行一共
    //日期串 20220617204504
    private  String createDate=LocalDateUtils.getLocalDateTimeStr1() ;

    private ObjToCSV(T object, String separator) {
        this.object = object;
        this.separator = separator;
        //行数
        this.number=1;
    }
    private ObjToCSV(T object) {
        this.object = object;
        //行数
        this.number=1;

    }
    private ObjToCSV(List<T> objects) {
        if (objects.isEmpty()) {
            throw new NullPointerException("不能是空");
        }
        this.object = objects.get(0);
        this.objects = objects;
        //行数
        this.number=objects.size();
    }
    private ObjToCSV(List<T> objects, String separator) {
        if (objects.isEmpty()) {
            throw new NullPointerException("不能是空");
        }
        this.object = objects.get(0);
        this.objects = objects;
        this.separator = separator;
        //行数
        this.number=objects.size();

    }


    // 排除和包含都有,那么以包含的为主
    private boolean decideFields(String fieldName) {

        //包含
        if (includeFields.contains(fieldName)) {
            return false;
        }
        //排除
        if (excludeFields.contains(fieldName)) {
            return true;
        }

        // 开启全部放行
        if (fieldsAll) {
            return false;
        }

        //默认拦截全部
        return true;
    }

    public static <T> ObjToCSV<T> create(T object, String separator) {
        return new ObjToCSV<T>(object, separator);
    }
    public static <T> ObjToCSV<T> create(T object) {
        return new ObjToCSV<T>(object);
    }
    public static <T> ObjToCSV<T> create(List<T> object, String separator) {
        return new ObjToCSV<T>(object, separator);
    }
    public static <T> ObjToCSV<T> create(List<T> object) {
        return new ObjToCSV<T>(object);
    }
    //全部放行
    public ObjToCSV<T> fieldsAll() {
        fieldsAll = true;
        return this;
    }

    //包含
    public ObjToCSV<T> addIncludeFields(String... fieldName) {
        includeFields.addAll(Arrays.asList(fieldName));
        return this;
    }

    //排除
    public ObjToCSV<T> addExcludeFields(String... fieldName) {
        excludeFields.addAll(Arrays.asList(fieldName));
        return this;
    }

    //添加头部
    public ObjToCSV<T> addHead() {
        StringBuilder str1 = new StringBuilder();
        Class<?> aClass = this.object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        int length = fields.length;
        for (int i = 0; i < length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (decideFields(field.getName())) {
                continue;
            }
            str1.append(field.getName()).append(separator);
        }
        int length1 = separator.length();
        String substring = str1.substring(0, str1.length() - length1);
        this.str.append(substring + "\n");
        return this;
    }
    //在结尾补充自定义的描述
    public ObjToCSV<T> addEnding(Function<ObjToCSV,String> functor){
        String apply = functor.apply( this);
        this.str.append(apply);
        return this;
    }


    //将对象转换为CSV格式字符串
    @SneakyThrows
    public ObjToCSV<T> addContent() {
        StringBuilder str1 = new StringBuilder();
        Class<?> aClass = this.object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        int length = fields.length;
        for (int i = 0; i < length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (decideFields(field.getName())) {
                continue;
            }
            Object o = field.get(object);
            String value = "";
            if (o != null) {
                value = String.valueOf(o);
            }
            str1.append(value).append(separator);
        }
        int length1 = separator.length();
        String substring = str1.substring(0, str1.length() - length1);
        this.str.append(substring + "\n");

        return this;
    }
    //将多个对象转换为csv格式
    public ObjToCSV<T> addContents() {
        for (T t : this.objects) {
            this.object=t;
            addContent();
        }
        return this;
    }
    //将结果输出为字符串
    public String ToString() {
        return str.toString();
    }



}
