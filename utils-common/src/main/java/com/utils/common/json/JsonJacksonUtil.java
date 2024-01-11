package com.utils.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.utils.common.base.DateUtil;
import com.utils.common.enums.DateEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// 注意如果转换对象有内部类 那么这个对象的内部类必须设置static
// 不然报错can only instantiate non-static inner class by using default, no-argument constructor
// localDate,localDateTime,localTime 在格式化的时候必须时间格式一致,不然报错,而Date会自适应
@Slf4j
public class JsonJacksonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //configure方法 配置一些需要的参数
        // 转换为格式化的json 显示出来的格式美化
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        //序列化的时候序列对象的那些属性
        //JsonInclude.Include.NON_DEFAULT 属性为默认值不序列化
        //JsonInclude.Include.ALWAYS      所有属性
        //JsonInclude.Include.NON_EMPTY   属性为 空（“”） 或者为 NULL 都不序列化
        //JsonInclude.Include.NON_NULL    属性为NULL 不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //设置Date类型的序列化及反序列化格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat(DateEnum.DATETIME_PATTERN.getValue()));
        // 允许出现单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //处理不同的时区偏移格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //添加各种时间的序列化和反序列化的格式,localDate,localDateTime,localTime....
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateEnum.DATETIME_PATTERN.getValue())));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateEnum.DATETIME_PATTERN.getValue())));

        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateEnum.DATE_PATTERN.getValue())));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateEnum.DATE_PATTERN.getValue())));

        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateEnum.TIME_PATTERN.getValue())));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateEnum.TIME_PATTERN.getValue())));

        objectMapper.registerModule(javaTimeModule);
        // 忽略 transient 修饰的属性
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);

    }

    /**
     * javaBean、列表(list,map),数组  转换为json字符串
     */
    @SneakyThrows
    public static String toJson(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * javaBean、列表(list,map),数组,转换为json字符串,忽略空值
     */
    @SneakyThrows
    public static String toJsonNotNull(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(obj);
    }


    /**
     * json 转JavaBean
     */
    @SneakyThrows
    public static <T> T jsonToBean(String jsonString, Class<T> clazz) {
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return objectMapper.readValue(jsonString, clazz);
    }


    /**
     * json字符串转换为列表
     */
    @SneakyThrows
    public static <T> List<T> jsonToList(String jsonArrayStr, Class<T> clazz) {

        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        List<T> lst = (List<T>) objectMapper.readValue(jsonArrayStr, javaType);
        return lst;
    }

    /**
     * json字符串转换为列表
     */
    @SneakyThrows
    public static <T> T[] jsonToArray(String jsonArrayStr, Class<T> clazz) {
        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        List<T> lst = (List<T>) objectMapper.readValue(jsonArrayStr, javaType);
        return (T[]) lst.toArray();
    }


    /**
     * json字符串  转换为map
     */
    @SneakyThrows
    public static Map<String, Object> jsonToMap(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue(jsonString, Map.class);
    }


    /**
     * json字符串 转换为map  指定转换类型
     */
    @SneakyThrows
    public static <T> Map<String, T> jsonToMap(String jsonString, Class<T> clazz) {
        Map<String, Map<String, Object>> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Map<String, Object>>>() {
        });
        Map<String, T> result = new HashMap<String, T>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * 深度转换json成map
     *
     * @param json
     * @return
     */
    @SneakyThrows
    public static Map<String, Object> jsonToMapDepth(String json) {
        return json2MapRecursion(json, objectMapper);
    }

    /**
     * map 转 json字符串
     *
     * @param map
     * @return
     */
    @SneakyThrows
    public static String mapToJson(Map map) {
        return objectMapper.writeValueAsString(map);
    }

    /**
     * map类型  转 指定JavaBean     原理就是key对应属性名称   value对应属性值
     */
    @SneakyThrows
    public static <T> T mapToBean(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

    // 将Bean转成Map     原理就是属性名称对应key   属性值对应value
    @SneakyThrows
    public static Map beanToMap(Object obj) {
        return objectMapper.readValue(toJson(obj), Map.class);
    }


    //将JSON写入文件中
    @SneakyThrows
    public static void writeFileJson(File file, String data) {
        OutputStream outputStream = new FileOutputStream(file);
        objectMapper.writeValue(outputStream, data);
    }

    //读取文件的JSON
    @SneakyThrows
    public static String readFileJson(File file) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readValue(file, JsonNode.class);
        return toJson(rootNode);
    }


    // ----------------------------------------------------底层方法----------------------------------------------------------


    /**
     * 把json解析成list，如果list内部的元素存在jsonString，继续解析
     *
     * @param json
     * @param mapper 解析工具
     * @return
     * @throws Exception
     */
    private static List<Object> json2ListRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        }

        List<Object> list = mapper.readValue(json, List.class);

        for (Object obj : list) {
            if (obj != null && obj instanceof String) {
                String str = (String) obj;
                if (str.startsWith("[")) {
                    obj = json2ListRecursion(str, mapper);
                } else if (obj.toString().startsWith("{")) {
                    obj = json2MapRecursion(str, mapper);
                }
            }
        }

        return list;
    }


    /**
     * 把json解析成map，如果map内部的value存在jsonString，继续解析
     *
     * @param json
     * @param mapper
     * @return
     * @throws Exception
     */
    private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        }

        Map<String, Object> map = mapper.readValue(json, Map.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj != null && obj instanceof String) {
                String str = ((String) obj);

                if (str.startsWith("[")) {
                    List<?> list = json2ListRecursion(str, mapper);
                    map.put(entry.getKey(), list);
                } else if (str.startsWith("{")) {
                    Map<String, Object> mapRecursion = json2MapRecursion(str, mapper);
                    map.put(entry.getKey(), mapRecursion);
                }
            }
        }

        return map;
    }


    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


    /**
     * map  转JavaBean
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

}
