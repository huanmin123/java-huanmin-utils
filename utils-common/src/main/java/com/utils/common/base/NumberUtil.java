package com.utils.common.base;

import org.apache.logging.log4j.util.Strings;

public class NumberUtil {

    //将字符串转换为Number类型
    public static Number parseNumber(String str) {
        if (Strings.isBlank(str)) {
            return null;
        }
        if (str.contains(".")) {
            return Double.parseDouble(str);
        } else {
            return Long.parseLong(str);
        }
    }
    //将泛型转换为Number类型
    public static Number parseNumber(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return (Number) obj;
        } else {
            return parseNumber(obj.toString());
        }
    }


        // 返回两个数较大的一个
    public static Number max(Number a, Number b) {
        if (a == null||b==null) {
            return null;
        }
        if (a.doubleValue() > b.doubleValue()) {
            return a;
        } else {
            return b;
        }
    }
    // 返回两个数较小的一个
    public static Number min(Number a, Number b) {
        if (a == null||b==null) {
            return null;
        }
        if (a.doubleValue() < b.doubleValue()) {
            return a;
        } else {
            return b;
        }
    }



}
