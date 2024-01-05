package com.utils.common.file.csv;

import com.utils.common.base.ObjConverter;
import com.utils.common.obj.reflect.ClassUtil;
import com.utils.common.string.PatternCommon;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * csv转对象
 * @Author: huanmin
 * @Date: 2022/6/18 15:59
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class CSVToObj<T> extends ParameterizedTypeReference<T> {
    private String separator  ;  //默认分隔符
    private List<String> objects ;//数据
    private List<T> list=new ArrayList<>();//数据
    private Map<String,String> fieldsTypes = new LinkedHashMap<>();
    public CSVToObj(List<String> objects,String separator) {
        //特殊符号的处理
        this.separator = PatternCommon.division(separator);
        this.objects=objects;
    }
    //列,映射,转换的时候会和映射的顺序保持一致 ,默认使用当前文件的分隔符作为切割
    public CSVToObj<T>  mapping(String fieldNames){
        String[] split = fieldNames.split(separator);
        Class<? extends CSVToObj> aClass = ClassUtil.deSerializable(getClass(),0);
        Field[] declaredFields = aClass.getDeclaredFields();
        for (String s : split) {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if(s.equals(declaredField.getName())){
                    fieldsTypes.put(declaredField.getName(), declaredField.getType().getTypeName());
                }
            }
        }
        return this;
    }

    @SneakyThrows
    public CSVToObj<T>  transform(){
        Class<? extends CSVToObj> aClass = ClassUtil.deSerializable(getClass(),0);
        for (String object : objects) {
            String[] split = object.split(separator);
            T o = (T)aClass.newInstance();
            Field[] declaredFields = o.getClass().getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                declaredField.setAccessible(true);
                if (fieldsTypes.containsKey(declaredField.getName())) {
                    declaredField.set(o, ObjConverter.cast(split[i],fieldsTypes.get(declaredField.getName())));
                }
            }
            list.add(o);
        }
        return this;
    }
    public List<T>  result(){
        return list;
    }


}
