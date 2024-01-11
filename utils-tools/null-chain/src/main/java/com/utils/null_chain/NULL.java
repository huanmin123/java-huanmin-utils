package com.utils.null_chain;

import com.utils.common.json.JsonJacksonUtil;
import com.utils.common.obj.copy.BeanCopyUtil;
import com.utils.common.obj.serializable.SerializeUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 使用Null工具类的要求
 * 1.类必须实现get/set方法 (强制必须实现,否则会找不到属性方法)
 * 2.类必须有空构造方法
 * 3.类的字段类型必须都是包装类型,不能是基本类型
 * 4.类必须继承Serializable序列化接口,否则无法使用此工具,这个是强制的(便于网络传输后还能转换回来)
 * 以上1,2点要求,可以使用lombok的@Data注解来实现, 如果主动创建了带参构造方法,那么需要指定@NoArgsConstruct注解
 * 上4点都是各大公司代码规范中的基本要求,不满2,3也不影响基本功能的使用,只是在一些高级功能中会有影响
 *
 * @author huanmin
 * @date 2024/1/11
 */
public class NULL {
    public static <T extends Serializable> NullChain<T> of(T o) {
        if (o == null) {
            return NullBuild.empty();
        }
        return NullBuild.noEmpty(o);
    }

    //遇到空直接抛异常
    public static <T extends Serializable> NullChain<T> no(T object) {
        return NullBuild.noEmpty(Objects.requireNonNull(object));
    }


    //将json转换为NullChain<T>
    public static <T extends Serializable> NullChain<T> toNULL(String json, Class<T> tClass) {
        T t = JsonJacksonUtil.jsonToBean(json, tClass);
//        T t = JsonFastJsonUtil.parse(json, tClass);
        if (t == null) {
            return NullBuild.empty();
        }
        return NULL.of(t);
    }
    //反序列化,前提对象在序列话的时候是使用的NullChain<T>对象转换的否则会报错
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> NullChain<T> toNULL(byte[] bytes, Class<T> tClass) {
        if (bytes == null) {
            return NullBuild.empty();
        }
        NullChain<T> unserialize = SerializeUtil.unserialize(bytes, NullChainDefault.class);
        if (unserialize == null) {
            return NullBuild.empty();
        }
        return unserialize;
    }




    //将数组T[] 转换为 NullChain<T>[]
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> NullChain<T>[] toArray(T... array) {
        if (array == null) {
            return NullBuild.emptyArray();
        }
        return toList(Arrays.asList(array)).toArray(new NullChain[array.length]);
    }




    //将List<T> 转换为 List<NullChain<T>>
    public static <T extends Serializable> List<NullChain<T>> toList(List<T> list) {
        if (list == null||list.isEmpty()) {
            return NullBuild.emptyList();
        }
        return list.stream().map(NULL::of).collect(Collectors.toList());
    }



    //将json转List<NullChain<T>>
    public static <T extends Serializable> List<NullChain<T>> toList(String json, Class<T> tClass) {
        List<T> ts = JsonJacksonUtil.jsonToList(json, tClass);
        if (ts.isEmpty()) {
            return NullBuild.emptyList();
        }
        return toList(ts);
    }



    public static <T extends Serializable> Map<String,NullChain<T>> toMap(String json, Class<T> tClass) {
        Map<String, T> stringTMap = JsonJacksonUtil.jsonToMap(json, tClass);
        if (stringTMap.isEmpty()) {
            return NullBuild.emptyMap();
        }
        return toMap(stringTMap);
    }

    //将Map<K,V> 转换为 Map<K,NullChain<V>>
    public static <K, V extends Serializable> Map<K, NullChain<V>> toMap(Map<K, V> map) {
        if (map == null||map.isEmpty()) {
             return NullBuild.emptyMap() ;
        }
        return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> NULL.of(e.getValue())));
    }

    /**
     * 将多个NullChain<B> 合并为一个NullChain<R> ,这种适合于多个不同的对象, 合并为一个新的对象(bo->[vo|dto])
     * 对象必须实现get/set方法 ,并且属性名相同
     * 排除null值和空字符串的属性
     * 后面的对象如果有重复的属性那么会覆盖前面的对象的属性值
     * 空NullChain会跳过合并,非空NullChain会执行合并
     *
     * @param tClass     合并后的对象类型
     * @param nullChains 多个NullChain<B>
     * @param <R>        合并后的对象类型
     * @return NullChain<R>
     */
    public static <R extends Serializable> NullChain<R> merge(Class<R> tClass, List<NullChain<?>> nullChains) {
        if (nullChains == null||nullChains.isEmpty()) {
            return NullBuild.empty();
        }
        try {
            R r = tClass.newInstance();
            for (NullChain<?> nullChain : nullChains) {
                if (!nullChain.is()) {
                    BeanCopyUtil.copyPropertiesIgnoreNull(nullChain.get(), r);
                }
            }
            return NULL.of(r);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <B, R extends Serializable> NullChain<R> merge(Class<R> tClass, NullChain<B>... nullChains) {
        if (nullChains == null||nullChains.length==0) {
            return NullBuild.empty();
        }
        return merge(tClass, Arrays.asList(nullChains));
    }






}
