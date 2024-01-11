package com.huanmin.utils.common.json;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.huanmin.utils.common.base.UniversalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;




public class JsonGsonUtils<T> {

   private  static Gson gson ;

   static {
       GsonBuilder gsonBuilder = new GsonBuilder() ;
       // 设置成漂亮的输出
//       gsonBuilder.setPrettyPrinting();
       //设定日期解析格式
       gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
       gson=gsonBuilder.create();
   }

    //把json 字符串 转换为 Map
    public static Map<String, Object> jsonTurnMap(String json){
        Map<String, Object> res = null;
        try {
            res = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
            }.getType());

        } catch (JsonSyntaxException e) {
             UniversalException.logError(e);
        }
        return res;
    }



    // 把JSOn 字符串 转实体类 泛型版
    public static <T>T jsonTurnJavaBena(String json, Class clazz){
       T res = null;
        try {
            res =  (T)gson.fromJson(json,clazz);
        } catch (JsonSyntaxException e) {
             UniversalException.logError(e);
        }
        return res;
    }


//    //把json 字符串 转换为 LIst
    public static  <T>  List<T>  jsonTurnList(String json,Class<T> clazz){
        List<T> newres = new ArrayList<>();
        List<T> res = null;
        try {
            res = gson.fromJson(json, new TypeToken<ArrayList<T>>() {}.getType());

        } catch (JsonSyntaxException e) {
             UniversalException.logError(e);
        }
        for (T re : res) {
            newres.add(JsonGsonUtils.jsonTurnJavaBena(JsonGsonUtils.objTurnJson(re),clazz));
        }

        return newres;
    }



    //把  实体类对象 , List , Map  转 json 字符串
    // 注意不支持集合{{  }} 这种快捷方式初始值创建,因为不支持泛型推断,导致内容识别不了
    public static String objTurnJson(Object object){
        return  gson.toJson(object);
    }

}
