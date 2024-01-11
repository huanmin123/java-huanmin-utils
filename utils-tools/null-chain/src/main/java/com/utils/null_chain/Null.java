package com.utils.null_chain;

import com.utils.common.obj.copy.BeanCopyUtil;

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
    public static  <T> NullChain<T>[] toArray(T... array){
        return toList(Arrays.asList(array)).toArray(new NullChain[array.length]);
    }

    //将Map<K,V> 转换为 Map<K,NullChain<V>>
    public static  <K,V> Map<K,NullChain<V>> toMap(Map<K,V> map){
        Map<K, V> map1 = Objects.requireNonNull(map);
        return map1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Null.of(e.getValue())));
    }

    /**
     * 将多个NullChain<B> 合并为一个NullChain<R> ,这种适合于多个不同的对象, 合并为一个新的对象(bo->[vo|dto])
     * 对象必须实现get/set方法 ,并且属性名相同
     * 排除null值和空字符串的属性
     * 后面的对象如果有重复的属性那么会覆盖前面的对象的属性值
     * 空NullChain会跳过合并,非空NullChain会执行合并
     * @param tClass 合并后的对象类型
     * @param nullChains 多个NullChain<B>
     * @return NullChain<R>
     * @param <R> 合并后的对象类型
     */
    public static <R> NullChain<R> merge(Class<R> tClass, List<NullChain<?>> nullChains) {
        try {
            R r = tClass.newInstance();
            for (NullChain<?> nullChain : nullChains) {
                if (!nullChain.is()){
                    BeanCopyUtil.copyPropertiesIgnoreNull(nullChain.get(), r);
                }
            }
            return Null.of(r);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static <B, R> NullChain<R> merge(Class<R> tClass, NullChain<B>... nullChains) {
        return merge(tClass, Arrays.asList(nullChains));
    }

}
