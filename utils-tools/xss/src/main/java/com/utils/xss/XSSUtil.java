package com.utils.xss;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class XSSUtil {

 private static Map<String,Integer> map=new HashMap(){{
        put("<script>(.*?)</script>",Pattern.CASE_INSENSITIVE);
        put("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        put("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        put("<script>|</script>",Pattern.CASE_INSENSITIVE);
        put("<script(.*?)>",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        put("eval\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        put("e­xpression\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        put("javascript:",Pattern.CASE_INSENSITIVE);
        put("vbscript:", Pattern.CASE_INSENSITIVE);
        put("onload(.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        put("<|>",Pattern.MULTILINE | Pattern.DOTALL);


    }};

    //    防止防止站点脚本注入工具,替换非法字符
    public static <T>T cleanXSS(T value,Class<T> cl) {
        if (value != null) {
            String s = JSON.toJSONString(value);
            for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()) {
                Pattern scriptPattern = Pattern.compile(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                s = scriptPattern.matcher(s).replaceAll("");
                value = JSON.parseObject(s, cl);
            }
        }
        return value;
    }


    //    防止防止站点脚本注入工具, 如果有特殊字符或者脚本那么报错
    public static void cleanXSSError(Object value) throws Exception {
        if (value != null) {
            String s = JSON.toJSONString(value);
            for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()) {
                Pattern scriptPattern = Pattern.compile(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                boolean matches = scriptPattern.matcher(s).find();
                if (matches) {
                    throw new Exception("有非法字符"+stringIntegerEntry.getKey()+"存在...禁止接下来操作---请检查添加的数据,将非法数据清除后在操作");
                }
            }

        }

    }

    //    防止防止站点脚本注入工具,如果有含特殊字符或者脚本那么返回true,
    public static boolean cleanXSSErrorBool(Object value) {
        if (value != null) {
            String s = JSON.toJSONString(value);
            for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()) {
                Pattern scriptPattern = Pattern.compile(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                boolean matches  = scriptPattern.matcher(s).find();
                if (matches) {
                   return true;
                }
            }
        }
        return  false;
    }
}
