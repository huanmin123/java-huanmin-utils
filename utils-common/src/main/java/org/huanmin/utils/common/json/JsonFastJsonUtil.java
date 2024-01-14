package org.huanmin.utils.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonFastJsonUtil {
    private final static Logger logger = LoggerFactory.getLogger(JsonFastJsonUtil.class);
    private static SerializeConfig config;

    static {
        config = new SerializeConfig();
        config.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }

    private static final SerializerFeature[] features = {
            // 输出空置字段
            SerializerFeature.WriteMapNullValue,
            // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullListAsEmpty,
            // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullNumberAsZero,
            // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullBooleanAsFalse,
            // 字符类型字段如果为null，输出为""，而不是null
            SerializerFeature.WriteNullStringAsEmpty
    };


    /**
     * 将对象转为json字符串 ,可以是单个对象，也可以是list map等
     *
     * @param object
     * @return
     */
    public static String toFeaturesJson(Object object) {
        return JSON.toJSONString(object, config, features);
    }

    /**
     * 将对象转为json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return JSON.toJSONString(object, config);
    }

    /**
     * 将json字符串转为Object实例
     *
     * @param json
     * @return
     */
    public static Object parse(String json) {
        return JSON.parse(json);
    }

    /**
     * 将json字符串转为指定类型的实例
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * 将json转为Map
     *
     * @param json
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> toMap(String json) {
        return (Map<String, T>) JSONObject.parseObject(json);
    }

    /**
     * 将json转为指定类型的List
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String json, Class<T> cls) {
        return JSON.parseArray(json, cls);
    }


    /**
     *
     * @param json  必须是 JSONArray 或者JSONObject 才会进行样式转换
     * @return
     */
    public static String   pretty(JSON json){
        String pretty = JSON.toJSONString(json, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        return  pretty;
    }

    //判断是否是json格式
    public static boolean isJson(String json) {
        try {
            JSON.parse(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
