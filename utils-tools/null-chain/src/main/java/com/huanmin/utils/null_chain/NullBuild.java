package com.huanmin.utils.null_chain;


import com.google.common.collect.Maps;
import com.huanmin.utils.null_chain.base.NullChain;
import com.huanmin.utils.null_chain.base.NullChainBase;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public class NullBuild {
    public static Map<String, Object> resultMap= Maps.newConcurrentMap();
    public static Map<String, Queue<Thread>> threadMap= Maps.newConcurrentMap();
    public static Map<String, Boolean> stopMap= Maps.newConcurrentMap();


    public static <T extends Serializable> NullChain<T> empty() {
        return new NullChainBase<>((T) null, true, new StringBuffer());
    }

    public static <T extends Serializable> NullChain<T> empty(StringBuffer linkLog) {
        return new NullChainBase<T>((T) null, true, linkLog);
    }

    public static <T extends Serializable> NullChain<T> noEmpty(T object) {

        return new NullChainBase<>(object, false, new StringBuffer());
    }


    public static <T extends Serializable> NullChain<T> noEmpty(T object, StringBuffer linkLog) {
        return new NullChainBase<>(object, false, linkLog);
    }

    public static <K, V extends Serializable> Map<K, NullChain<V>> emptyMap() {
        Map<K, V> map = new HashMap<>();
        return (Map<K, NullChain<V>>) map;
    }

    //空list
    public static <T extends Serializable> List<NullChain<T>> emptyList() {
        List<T> list = new ArrayList<>();
        return (List<NullChain<T>>) list;
    }

    //空array
    public static <T extends Serializable> NullChain<T>[] emptyArray() {
        return (NullChain<T>[]) new NullChain[0];
    }



}
