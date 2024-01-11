package com.utils.null_chain;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public class Null {
    public static <T> NullChain<T> of(T o) {
        if (o == null) {
            return NullChainDefault.empty();
        }
        return NullChainDefault.noEmpty(o);
    }

    //遇到空直接抛异常
    public static <T> NullChain<T> no(T object) {
        return NullChainDefault.noEmpty(Objects.requireNonNull(object));
    }

    //将List<T> 转换为 List<NullChain<T>>
    public static  <T> List<NullChain<T>> toList(List<T> list){
        List<T> list1 = Objects.requireNonNull(list);
        return list1.stream().map(Null::of).collect(Collectors.toList());
    }
    //将数组T[] 转换为 NullChain<T>[]
    public static  <T> NullChain<T>[] toArray(T[] array){
        T[] ts = Objects.requireNonNull(array);
        return Arrays.stream(ts).map(Null::of).toArray(NullChain[]::new);
    }

    //将Map<K,V> 转换为 Map<K,NullChain<V>>
    public static  <K,V> Map<K,NullChain<V>> toMap(Map<K,V> map){
        Map<K, V> map1 = Objects.requireNonNull(map);
        return map1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Null.of(e.getValue())));
    }

}
