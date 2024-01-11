package com.huanmin.test.utils.utils_tools.file_tool.csv;

import com.alibaba.fastjson.JSONObject;


import com.huanmin.utils.common.base.FakerData;
import com.huanmin.utils.common.base.UserData;
import lombok.SneakyThrows;
import com.huanmin.utils.file_tool.csv.ObjToCSV;
import org.junit.Test;

import java.util.List;

public class ObjToCSVTest {


    //单个对象
    @Test
    public  void show1(){
        UserData userOne = FakerData.getUserOne();
        String s = ObjToCSV.create(userOne)
                .fieldsAll()
//                .addIncludeFields()
                .addExcludeFields("roleData")
//                .addHead()
                .addContent()
//                .addContents()
                .addEnding((data)->{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("count",data.getNumber());
                    jsonObject.put("time",data.getCreateDate());
                    jsonObject.put("separator",data.getSeparator());
                    return  jsonObject.toJSONString();
                })
                .ToString();
        System.out.println(s);

    }


    //多个对象,可以使用自定义分隔符
    @Test
    public  void show2(){
        UserData userOne = FakerData.getUserOne();
        String s = ObjToCSV.create(userOne,"|")
                .fieldsAll()
                .addExcludeFields("roleData")
                .addContent()
                .addEnding((data)->{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("count",data.getNumber());
                    jsonObject.put("time",data.getCreateDate());
                    jsonObject.put("separator",data.getSeparator());
                    return  jsonObject.toJSONString();
                })
                .ToString();
        System.out.println(s);
    }



    //排除不显示的,其他都显示
    @Test
    public  void show3(){
        List<UserData> userDatas = FakerData.getUserDataList(10);

        String s = ObjToCSV.create(userDatas,"|")
                .fieldsAll()//显示全部字段
                .addExcludeFields("roleData")//排除指定字段
                .addHead()
                .addContents()
                .addEnding((data)->{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("count",data.getNumber());
                    jsonObject.put("time",data.getCreateDate());
                    jsonObject.put("separator",data.getSeparator());

                    return  jsonObject.toJSONString();
                })
                .ToString();
        System.out.println(s);
    }

    //只包含指定字段 ,其他都不显示
    @Test
    public  void show4(){
        List<UserData> userDatas = FakerData.getUserDataList(10);

        String s = ObjToCSV.create(userDatas,"|")
                .addIncludeFields("id","name")
                .addHead()  //添加头部
                .addContents() //添加内容
                .addEnding((data)->{  //添加结尾信息
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("count",data.getNumber());
                    jsonObject.put("time",data.getCreateDate());
                    jsonObject.put("separator",data.getSeparator());
                    return  jsonObject.toJSONString();
                })
                .ToString();
        System.out.println(s);
    }


    //内容
    @Test
    public  void show5(){
        List<UserData> userDatas = FakerData.getUserDataList(10);
        String s = ObjToCSV.create(userDatas,"|")
                .fieldsAll()
                .addExcludeFields("roleData")//排除指定字段
                .addContents() //添加内容
                .ToString();
        System.out.println(s);
    }

    @SneakyThrows
    public static void main(String[] args) {
        List<UserData> userDatas = FakerData.getUserDataList(100);
        String s = ObjToCSV.create(userDatas)
                .fieldsAll()
//                .addIncludeFields()
                .addExcludeFields("roleData")
//                .addHead()
//                .addContent()
                .addContents()
                .addEnding((data)->{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("count",data.getNumber());
                    jsonObject.put("time",data.getCreateDate());
                    return  jsonObject.toJSONString();
                })
                .ToString();
        System.out.println(s);


    }
}
