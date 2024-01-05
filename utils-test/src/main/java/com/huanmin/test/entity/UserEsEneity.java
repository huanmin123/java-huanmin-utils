package com.huanmin.test.entity;


import lombok.Data;
import org.huanmin.es8.annotation.DocId;
import org.huanmin.es8.annotation.EsClass;
import org.huanmin.es8.annotation.EsField;
import org.huanmin.es8.enums.EsDataType;


// 只能使用包装类型 ,不能使用基础类型 ,否则会导致一些高级特性有问题
@EsClass
@Data
public class UserEsEneity {

    @DocId
    private  Long id;
    @EsField(type = EsDataType.KEYWORD   )
    private  String name;
    @EsField(type =EsDataType.INTEGER)
    private  Integer age;
    @EsField(type = EsDataType.TEXT,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private  String dec;
    @EsField(type =EsDataType.KEYWORD)
    private  String sku;
    @EsField(type =EsDataType.DOUBLE)
    private  Double price;

    public  String toAction(String str) {
        System.out.println(str);
        return  str;
    }
}
