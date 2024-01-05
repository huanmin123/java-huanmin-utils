package com.utils.common.string;

import com.file.TransitionFileStreamUtil;
import info.monitorenter.cpdetector.io.*;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class StringUtil {

    //截取域名   http://www.baidu.com/s/t  截取后  www.baidu.com
    public static String subDomain(String url) {
        String str = url.replace("http://", "").replace("https://", "");//去除http和https前缀
        String str1 = str.split("/")[0];//按‘/’分隔，取第一个
        return str1;
    }


    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }


    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 判断 原字符串中包含某字符的个数
     *
     * @param longStr 原字符串
     * @param mixStr  需要匹配的字符串
     * @return 包含个数
     */
    public static int containStrCount(String longStr, String mixStr) {
        //如果确定传入的字符串不为空，可以把下面这个判断去掉，提高执行效率
        int count = 0;
        int index = 0;
        while ((index = longStr.indexOf(mixStr, index)) != -1) {
            index = index + mixStr.length();
            count++;
        }
        return count;
    }


    //字符串查找指定长度 从指定位置开始到什么位置结束 ,start从0开始 ,end从1开始
   public static int str_find_n(String str, String search, int start, int end) {
        int i = start;
        int j = 0;
        int k = 0;
        int len = str.length();
        int search_len = search.length();
        while (i < len && i < end) {
            if (str.charAt(i) == search.charAt(j)) {
                k = i;
                while (j < search_len && str.charAt(k) == search.charAt(j) ) {
                    k++;
                    j++;
                }
                if (j == search_len) {
                    return i;
                }
                j = 0;
            }
            i++;
        }
        return -1;
    }

    //替换指定范围内所有的字符串, str1:源字符串  str2:要替换的字符串, str3:替换为什么  start:开始位置  end:结束位置  start从0开始 ,end从1开始
    public static String str_replace_all_n(String str1, String str2, String str3, int start, int end) {
        int i = str_find_n(str1, str2, start, end);
        if (i == -1) {
            return str1;
        }
        int len1 = str1.length();
        int len2 = str2.length();
        int len3 = str3.length();
        char[] str = new char[len1 - len2 + len3 ];
        int j = 0;
        int k = 0;
        while (j < i) {
            str[j] = str1.charAt(j);
            j++;
        }
        while (k < len3) {
            str[j] = str3.charAt(k);
            j++;
            k++;
        }
        k = i + len2;
        while (k < len1) {
            str[j] = str1.charAt(k);
            j++;
            k++;
        }
//        str[j] = '\0';  在java中不需要
        return str_replace_all_n(new String(str), str2, str3, i, end);
    }
    //替换指定范围内所有的字符串, str1:源字符串  str2:要替换的字符串, str3:替换为什么  start:开始位置  end:结束位置  start从0开始 ,end从1开始



    //判断是否是数字包括负数  是返回true 不是返回false
    public static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        //截取掉-号
        if (str.startsWith("-")) {
            str = str.substring(1);
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //判断是否都是字符  是返回true 不是返回false
    public static boolean isLetter(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //判断是否是小数包括负数  是返回true 不是返回false
    public static boolean isDecimal(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        if (str.startsWith("-")) {
            str = str.substring(1);
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    //判断是否是特殊字符(除了字母和数字之外的符号都是特殊字符)  是返回true 不是返回false
    public static boolean isSpecial(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //判断是否是字母和数字组成的字符串  是返回true 不是返回false
    public static boolean isLetterOrDigit(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //判断字符串是否是布尔值  是返回true 不是返回false
    public static boolean isBoolean(String str) {
        if ("true".equals(str) || "false".equals(str)) {
            return true;
        }
        return false;
    }

    // 判断字符串是,数字,字母,小数,数字返回1,字母返回2,小数返回3,其他返回0
    public static int isNumberOrLetterOrDecimal(String str) {
        if (isNumeric(str)) {
            return 1;
        } else if (isLetter(str)) {
            return 2;
        } else if (isDecimal(str)) {
            return 3;
        } else {
            return 0;
        }
    }
    
    //判断多个字符串是否相等,只要有一个相等就返回true,否则返回false
    public static  boolean equalsAny(String str1,String ...str2){
        for (String s : str2) {
            if (str1.equals(s)){
                return true;
            }
        }
        return false;
    }
    //字符串比较,同时比较多个字符串,只要一个等就返回false,如果都不等就返回true
    public static boolean equalsNotAny(String str, String... strs) {
        for (String s : strs) {
            if (str.equals(s)) {
                return false;
            }
        }
        return true;
    }
    //判断多个字符串是否相等,全部相等才返回true,否则返回false
    public static  boolean equalsAll(String str1,String ...str2){
        for (String s : str2) {
            if (!str1.equals(s)){
                return false;
            }
        }
        return true;
    }


    //计算两个字符串的相似度,返回百分比 0-100
    public static int getSimilarityRatio(String str, String target) {
        int[][] d; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) { // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
                        + temp);
            }
        }
        return 100 - d[n][m] * 100 / Math.max(str.length(), target.length());
    }

    private static int min(int one, int two, int three) {
        return (one = Math.min(one, two)) < three ? one : three;
    }

    public static void main(String[] args) {
        System.out.println(getSimilarityRatio("123", "123"));
    }


    //获取n~n之间的所有连续数字的字符串
    public static List<String> get_range(String start, String end) {
        if (start == null || end == null) {
            return null;
        }
        if (!isNumeric(start) || !isNumeric(end)) {
            return null;
        }
        int start_int = Integer.parseInt(start);
        int end_int = Integer.parseInt(end);
        List<String> pList = new ArrayList<>(end_int - start_int + 1);
        for (int i = start_int; i <= end_int; i++) {
            String str = String.valueOf(i);
            pList.add(str);
        }
        return pList;
    }




//单个字符串的操作(自增(++),自减(--),取反(~),布尔取反(!),取绝对值(+),取负(-),向上取整(ceil),向下取整(floor),四舍五入(round)
public static String str_calculate_one(String a, String symbol) {
        //判断是否是数值
        if (isNumeric(a)) {
            if ("++".equals(symbol)) { //自增
                return String.valueOf(Integer.parseInt(a) + 1);
            }
            if ( "--".equals(symbol)) { //自减
                return String.valueOf(Integer.parseInt(a) - 1);
            }
            if ( "~".equals(symbol)) { //按位取反
                return String.valueOf(~Integer.parseInt(a));
            }
            if ( "-".equals(symbol)) { //取负

                if (a.charAt(0)!='-' ) { //如果不是负数
                    return String.valueOf(-Integer.parseInt(a));
                } else {
                    return a;
                }
            }
            if ( "+".equals(symbol)) { //取绝对值
                return String.valueOf(Math.abs(Integer.parseInt(a)));
            }
            if (isDecimal(a)) { //如果是小数
                if ("ceil".equals(symbol)) { //向上取整
                    return String.valueOf(Math.ceil(Double.parseDouble(a)));
                }
                if ("floor".equals(symbol)) { //向下取整
                    return String.valueOf(Math.floor(Double.parseDouble(a)));
                }
                if ( "round".equals(symbol)) { //四舍五入
                    return String.valueOf(Math.round(Double.parseDouble(a)));
                }
            } else {//如果是整数
                return a;
            }
        }
        //判断是否是布尔值
        if (isBoolean(a)) {
            if ( "!".equals(symbol)) { //取反
                if ("true".equals(a)) {
                    return String.valueOf(false);
                }
                return  String.valueOf(true);
            }
        }
        return null;
    }

    //计算两个字符串的各种操作
    // 数值运算: 加(+) 减(-) 乘(*) 除(/) 取余(%) 自增(++) 自减(--)
    // 比较运算: 大于(>)、小于(<)、等于(==)、 大于等于(>=)、小于等于(<=)和不等于(!=)
    // 逻辑运算: 包括与(&&)、或(||)、非(!)
    // 位操作: 按位与(&)、按位或(|)、按位异或(^)、按位取反(~)
    public String str_calculate_two(String a, String b, String symbol) {
        //判断是否是整数
        if (isNumeric(a) && isNumeric(b)) {
            if ("+".equals(symbol)) { //加
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) + Double.parseDouble(b));

            }
            if ("-".equals(symbol)) { //减
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) - Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) - Double.parseDouble(b));

            }
            if ("*".equals(symbol)) { //乘
                if (isNumeric(a) && isNumeric(b)) {

                    return String.valueOf(Integer.parseInt(a) * Integer.parseInt(b));
                }

                return String.valueOf(Double.parseDouble(a) * Double.parseDouble(b));

            }
            if ("/".equals(symbol)) { //除
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) / Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) / Double.parseDouble(b));
            }
            if ("%".equals(symbol)) { //取余
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) % Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) % Double.parseDouble(b));

            }
            if (">".equals(symbol)) { //大于
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) > Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) > Double.parseDouble(b));
            }

            if ("<".equals(symbol)) { //小于
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) < Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) < Double.parseDouble(b));
            }
            if ("==".equals(symbol)) { //等于
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) == Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) == Double.parseDouble(b));
            }
            if (">=".equals(symbol)) { //大于等于
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) >= Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) >= Double.parseDouble(b));
            }
            if ("<=".equals(symbol)) { //小于等于
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) <= Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) <= Double.parseDouble(b));
            }
            if ("!=".equals(symbol)) { //不等于
                if (isNumeric(a) && isNumeric(b)) {
                    return String.valueOf(Integer.parseInt(a) != Integer.parseInt(b));
                }
                return String.valueOf(Double.parseDouble(a) != Double.parseDouble(b));
            }

            if ("&".equals(symbol)) { //按位与
                return String.valueOf(Integer.parseInt(a) & Integer.parseInt(b));
            }
            if ("|".equals(symbol)) { //按位或
                return String.valueOf(Integer.parseInt(a) | Integer.parseInt(b));
            }
            if ("^".equals(symbol)) { //按位异或
                return String.valueOf(Integer.parseInt(a) ^ Integer.parseInt(b));
            }
            if ("<<".equals(symbol)) { //左移
                return String.valueOf(Integer.parseInt(a) << Integer.parseInt(b));
            }
            if (">>".equals(symbol)) { //右移
                return String.valueOf(Integer.parseInt(a) >> Integer.parseInt(b));
            }
        }

        if (isBoolean(a) && isBoolean(b)) {
            if ( "&&".equals(symbol)) { //与
                return String.valueOf(Boolean.parseBoolean(a) && Boolean.parseBoolean(b));
            }
            if ("||".equals(symbol)) { //或
                return String.valueOf(Boolean.parseBoolean(a) || Boolean.parseBoolean(b));
            }
        }
        return null;
    }

    //判断字符串是否是json对象{ key:value }
    // 没有对双引号进行判断和内容进行强制判断,
    // 只是简单的判断{}符号
   public  boolean str_is_map(String str) {
        if (str == null) {
            return false;
        }
        if (str.length() < 2) {
            return false;
        }
        if (str.startsWith("{")&&str.endsWith("}")) {
            return true;
        }
        return false;
    }

    //字符串转int转换失败返回0
    public static   int str_to_int(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }
    
    //最好的hash算法,冲突较少,效率较高
    public static int getHashCode(String str){
        int hash = 0;
        int seed = 131;
        for(int i = 0;i< str.length();++i)
        {
            hash =  hash*seed + str.charAt(i);
        }
        return (hash & 0x7FFFFFFF);
    }
    
    //字符串编码获取
    public static String getStrCharsetName(String str) throws IOException {
        // 获取 CodepageDetectorProxy 实例
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        // 添加解析器，会使用到添加的后 2 个 ext 里的 jar 包
        detector.add(new ParsingDetector(false));
        detector.add(JChardetFacade.getInstance());
        detector.add(ASCIIDetector.getInstance());
        detector.add(UnicodeDetector.getInstance());
        //字符串转InputStream
        InputStream inputStream = TransitionFileStreamUtil.strToIo(str);
        Charset charset = detector.detectCodepage(inputStream,str.length());
        if (charset != null){
            return charset.name();
        } else{
            return null;
        }
    }
    //字符串编码转换
    @SneakyThrows
    public  String strTransitionCoding(String str, String oldCoding, String newCoding){
        return new String(str.getBytes(oldCoding),newCoding);
    
    }

    //提取字符串中的中文
    public static String extractChinese(String str) {
        char[] charArray = str.toCharArray();
        StringBuilder result = new StringBuilder();

        for (char c : charArray) {
            if (c >= '\u4e00' && c <= '\u9fa5') {
                result.append(c);
            }
        }

        return result.toString();
    }
    //判断字符是否是中文
    public static boolean isChineseUsingUnicodeScript(char c) {
        Character.UnicodeScript script = Character.UnicodeScript.of(c);
        return script == Character.UnicodeScript.HAN;
    }
    //判断字符串是否是中文
    public static boolean isChinese(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (isChineseUsingUnicodeScript(c)) {
                return true;
            }
        }
        return false;
    }

    //取出一个字符串所有可能的连续子串,正反顺序都取
    //限制最小长度
    public static   List<String>  getSubString(String str, int length) {
        List<String> list = new ArrayList<>();
        if (str == null || str.length() < length) {
            return list;
        }
        for (int i = 0; i < str.length(); i++) {
            for (int j = str.length(); j > i; j--) {
                //去除空格
                String substring = str.substring(i, j).trim();
                if (substring.length() < length) {
                    continue;
                }
                list.add(substring);
            }
        }
        //反向
        for (int i = str.length(); i > 0; i--) {
            for (int j = 0; j < i; j++) {
                String substring = str.substring(j, i).trim();
                if (substring.length() < length) {
                    continue;
                }
                list.add(substring);
            }
        }
        //去重
        HashSet<String> hashSet = new HashSet<>(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }

    //字符串按行读取
    public static void readStrLine(String str, Consumer<String> consumer){

        try (BufferedReader br = new BufferedReader(new StringReader(str))) {
            String line;
            while ((line = br.readLine()) != null) {
                consumer.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //字符串*n次拼接
    public static String strSplicing(String str, int n){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }


    
}
