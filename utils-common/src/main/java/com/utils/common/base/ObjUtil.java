package com.utils.common.base;

/**
 * Object简单的基础工具类
 *
 * @Author: huanmin
 * @Date: 2022/6/19 21:58
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */



import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class ObjUtil {

    /**
     * 如果obj为null，则返回默认值，不为null，则返回obj
     *
     * @param obj          obj
     * @param defaultValue 默认值
     * @param <T>          值泛型
     * @return obj不为null 返回obj，否则返回默认值
     */
    public static <T> T defaultIfNull(T obj, T defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    //---------------------------------------------------------------------
    // 对象类型判断
    //---------------------------------------------------------------------

    public static boolean isCollection(Object obj) {
        return obj instanceof Collection;
    }

    public static boolean isMap(Object obj) {
        return obj instanceof Map;
    }

    public static boolean isNumber(Object obj) {
        return obj instanceof Number;
    }

    public static boolean isBoolean(Object obj) {
        return obj instanceof Boolean;
    }

    public static boolean isEnum(Object obj) {
        return obj instanceof Enum;
    }

    public static boolean isDate(Object obj) {
        return obj instanceof Date || obj instanceof TemporalAccessor;
    }

    public static boolean isCharSequence(Object obj) {
        return obj instanceof CharSequence;
    }

    /**
     * 判断对象是否为八大基本类型包装类除外即(boolean, byte, char, short, int, long, float, and double)<br/>
     *
     * @param obj
     * @return
     */
    public static boolean isPrimitive(Object obj) {
        return obj != null && obj.getClass().isPrimitive();
    }

    /**
     * 判断对象是否为包装类或者非包装类的基本类型
     *
     * @param obj
     * @return
     */
    public static boolean isWrapperOrPrimitive(Object obj) {
        return isPrimitive(obj) || isNumber(obj) || isCharSequence(obj) || isBoolean(obj);
    }

    /**
     * 判断一个对象是否为数组
     *
     * @param obj
     * @return
     */
    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    /**
     * 判断一个对象是否为基本类型数组即(int[], long[], boolean[], double[]....)
     *
     * @param obj
     * @return
     */
    public static boolean isPrimitiveArray(Object obj) {
        return isArray(obj) && obj.getClass().getComponentType().isPrimitive();
    }
}

