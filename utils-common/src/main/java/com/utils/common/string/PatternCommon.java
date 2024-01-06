package com.utils.common.string;

import com.utils.common.base.UniversalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 测试正则表达式地址: https://c.runoob.com/codedemo/6230/
// 正则表达式教程:  https://www.runoob.com/regexp/regexp-tutorial.html
/**
 *
 * 正则表达式工具
 * @Description:
 * 一、校验数字的表达式
 * 数字：^[0-9]*$
 * n位的数字：^\d{n}$
 * 至少n位的数字：^\d{n,}$
 * m-n位的数字：^\d{m,n}$
 * 零和非零开头的数字：^(0|[1-9][0-9]*)$
 * 非零开头的最多带两位小数的数字：^([1-9][0-9]*)+(\.[0-9]{1,2})?$
 * 带1-2位小数的正数或负数：^(\-)?\d+(\.\d{1,2})$
 * 正数、负数、和小数：^(\-|\+)?\d+(\.\d+)?$
 * 有两位小数的正实数：^[0-9]+(\.[0-9]{2})?$
 * 有1~3位小数的正实数：^[0-9]+(\.[0-9]{1,3})?$
 * 非零的正整数：^[1-9]\d*$ 或 ^([1-9][0-9]*){1,3}$ 或 ^\+?[1-9][0-9]*$
 * 非零的负整数：^\-[1-9][]0-9"*$ 或 ^-[1-9]\d*$
 * 非负整数：^\d+$ 或 ^[1-9]\d*|0$
 * 非正整数：^-[1-9]\d*|0$ 或 ^((-\d+)|(0+))$
 * 非负浮点数：^\d+(\.\d+)?$ 或 ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$
 * 非正浮点数：^((-\d+(\.\d+)?)|(0+(\.0+)?))$ 或 ^(-([1-9]\d*\.\d*|0\.\d*[1-9]\d*))|0?\.0+|0$
 * 正浮点数：^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$ 或 ^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$
 * 负浮点数：^-([1-9]\d*\.\d*|0\.\d*[1-9]\d*)$ 或 ^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$
 * 浮点数：^(-?\d+)(\.\d+)?$ 或 ^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$
 *
 *二、校验字符的表达式
 * 汉字：^[\u4e00-\u9fa5]*$
 * 英文和数字：^[A-Za-z0-9]+$ 或 ^[A-Za-z0-9]{4,40}$
 * 长度为3-20的所有字符：^.{3,20}$
 * 由26个英文字母组成的字符串：^[A-Za-z]+$
 * 由26个大写英文字母组成的字符串：^[A-Z]+$
 * 由26个小写英文字母组成的字符串：^[a-z]+$
 * 由数字和26个英文字母组成的字符串：^[A-Za-z0-9]+$
 * 由数字、26个英文字母或者下划线组成的字符串：^\w+$ 或 ^\w{3,20}$
 * 中文、英文、数字包括下划线：^[\u4E00-\u9FA5A-Za-z0-9_]+$
 * 中文、英文、数字但不包括下划线等符号：^[\u4E00-\u9FA5A-Za-z0-9]+$ 或 ^[\u4E00-\u9FA5A-Za-z0-9]{2,20}$
 * 可以输入含有^%&',;=?$\"等字符：[^%&',;=?$\x22]+
 * 禁止输入含有~的字符：[^~\x22]+
 *
 *三、特殊需求表达式
 * 匹配url: (http|https)://([\w-.]+:[1-9]{0,5})(/[\w-./?%&=]*)?$
 * 任意邮箱匹配:  ^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$
 * 手机号码:  ^1(2|3|4|5|6|7|8|9)\d{9}$
 * 身份证号码: ^(\d{15}$)|(^\d{18}$)|(^\d{17})(\d|X|x)$
 * 密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)：^[a-zA-Z]\w{5,17}$
 * 强密码(必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-10之间)：^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$
 * 日期格式：^\d{4}-\d{1,2}-\d{1,2}
 * 钱的输入格式:  ^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$
 * xml格式: ^([a-zA-Z]+-?)+[a-zA-Z0-9]+\\.[x|X][m|M][l|L]$
 * 空白行的正则表达式：\n\s*\r (可以用来删除空白行)
 * HTML标记的正则表达式：<(\S*?)[^>]*>.*?|<.*? />
 * 首尾空白字符的正则表达式：^\s*|\s*$ (可以用来删除行首行尾的空白字符(包括空格、制表符、换页符等等)，非常有用的表达式)
 * IPv4地址：((2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2})(\.((2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2})){3}
 */

//各种字符串的处理
public class PatternCommon {
    private static final Logger logger = LoggerFactory.getLogger(PatternCommon.class);
//    在正则表达式中，需要非常注意find与matches的区别。一旦不注意，可能出错。
//    因为find只是部分匹配，只要能找到就返回true；
//    而mathes是要整个输入字符串与模式串都匹配，丝毫不差才行,否则返回false


    //特殊符号转义
    public static String division(String str){
        switch (str){
            case ".":
                return "[.]";
            case "|":
                return "\\|";
            case "*":
                return "\\*";
            case "\\":
                return "\\\\";
            case "[]":
                return "\\[\\]";
        }
        return str;
    }


    //完全匹配
    public  static boolean isPatternAll(String str,String regex) {
        return   str.matches(regex);
    }
    // 0 搜索行 ,1多行搜索
    public static Boolean isPatternSearch(String content, String regex,int type) {
        int pattern=0;
        if (type == 1) {
            //多行匹配是可以跨过(\r\n,\n)这些符号,从而匹配其他语句
            pattern = Pattern.MULTILINE;  //多行匹配
        } else {
            //包含匹配 (Pattern.DOTALL 只匹配一行 (\r\n,\n)这些符号后的语句就不会匹配了)
            pattern = Pattern.DOTALL;  //单行匹配
        }
        Pattern pa = Pattern.compile(regex, pattern);
        Matcher ma = pa.matcher(content);
        if (ma.find()) {
            return true;
        }
        return false;
    }


    //正则表达式截取想要的内容 ,可以有多个匹配
    //分组匹配的前提是表达式必须先匹配所有内容,然后子表达式才能提取内存   ,子表达式需要使用  () 包裹起来
    //    String sqsl="# User@Host: root[root] @  [192.168.42.1]  Id:    92";
    //    List<String> list = cutPatternStr(sqsl, "User@Host: (.*)\\s*Id:\\s*(\\d*)", 1);
    //结果:
    //User@Host: root[root] @  [192.168.42.1]  Id:    92    (这个是全匹配的)
    //root[root] @  [192.168.42.1]        (从全匹配中提取子匹配(.*))
    // 92    (从全匹配中提取子匹配(\\d*))
    // 0 搜索行 ,1多行搜索
    public static List<String> cutPatternStr(String content,String regex,int type) {
        List<String> list=new ArrayList<>();
        int pattern=0;
        if (type == 1) {
            //多行匹配是可以跨过(\r\n,\n)这些符号,从而匹配其他语句
            pattern = Pattern.MULTILINE;  //多行匹配
        } else {
            //包含匹配 (Pattern.DOTALL 只匹配一行 (\r\n,\n)这些符号后的语句就不会匹配了)
            pattern = Pattern.DOTALL;  //单行匹配
        }
        Pattern pa = Pattern.compile(regex, pattern);
        Matcher ma = pa.matcher(content);

        if (ma.find()) {
            for (int i = 0; i <= ma.groupCount(); i++) {
                list.add(ma.group(i));
            }
           return list;
        }
        return null;
    }

    //替换文件中的分割符,适配当前系统的分割符
    public static String replaceSeparatorAdaptation(String path) {
        path= path.replaceAll("/|\\\\", Matcher.quoteReplacement(File.separator));
        return path;
    }

    //去掉全部的空格换行和tab
    public  static String trimAll(String str) {
       return str.replaceAll("(\\s|\\n|\\t|\\r)*","");
    }
    //全部的空格
    public  static String trimEmptyAll(String str) {
       return str.replaceAll("(\\s|\\t)*","");
    }

    //开头的空格
    public static String trimEmptyStartEnd(String str) {
        return str.replaceAll("^(\\s|\\t)*|(\\s|\\t)*$","");
    }



    // -------------------------积累的案例--------------------

    //匹配url是否是以xx结尾的并且包含?xx的参数
    //数据类型:
    // https://c.runoob.com/front-end 或者是  https://c.runoob.com/front-end?xxx  包括  以上url是否结尾加 /

   public static boolean  patternUrlEndAndParameter(String mainUrl,String sonUrl) {
        String regex="^.+("+sonUrl+"{0,1}/?|"+sonUrl+"{0,1}/?\\?.*)$";
        System.out.println(regex);
        return isPatternAll(mainUrl,regex);
   }


    //任意邮箱匹配
    public static boolean  patternEmailAll(String email) {
        String regex="^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        System.out.println(regex);
        return isPatternAll(email,regex);
    }


    //匹配是否是url
    public static boolean  patternUrlAll(String url) {
        String regex="^(http|https)://([\\w-.]+:[1-9]{0,5})(/[\\w-./?%&=]*)?$";
        System.out.println(regex);
        return isPatternAll(url,regex);
    }

    /**
     * 根据键值对填充字符串，
     *    System.out.println(renderString("hello ${name},id${id}", new HashMap() {{
     *             put("name","123");
     *             put("id","333");
     *         }}) );
     * 输出： hello 123,id333
     * @param content
     * @param map
     * @return
     */
    public static String renderString(String content, Map<String, String> map){
        Set<Map.Entry<String, String>> sets = map.entrySet();
        for(Map.Entry<String, String> entry : sets) {
            String regex = "\\$\\{" + entry.getKey() + "\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll(String.valueOf(entry.getValue()));
        }
        return content;
    }
    //实体类字段名称填充字符串
    public static String renderString(String content, Object obj)  {
        Map<String, String> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();//获取所有属性
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), String.valueOf(field.get(obj)));//获取属性值填充到map
            } catch (IllegalAccessException e) {
                 UniversalException.logError(e);
            }
        }
        return renderString(content, map);
    }

}
