package com.huanmin.utils.null_chain;

import com.huanmin.utils.null_chain.async.NullChainAsync;
import com.huanmin.utils.null_chain.base.NullChain;
import com.huanmin.utils.null_chain.sync.NullChainSyncDefault;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public class NullBuild {
    public static <T extends Serializable> NullChain<T> asyncRun(boolean async, Queue<Supplier<NullChain<T>>> asyncQueue, Supplier<NullChain<T>> supplier, NullChain<T> current ) throws RuntimeException {
        if (async) {
            //前一个任务是异步的,那么之后的任务需要在前一个任务执行完毕后执行
            asyncQueue.add(supplier);
            return current; //返回当前任务
        } else {
            return supplier.get();
        }
    }

    public static <T extends Serializable> NullChain<T> empty() {
        return new NullChainSyncDefault<>((T) null, true, new StringBuffer());
    }

    public static <T extends Serializable> NullChain<T> empty(StringBuffer linkLog) {
        return new NullChainSyncDefault<T>((T) null, true, linkLog);
    }

    public static <T extends Serializable> NullChain<T> noEmpty(T object) {

        return new NullChainSyncDefault<>(object, false, new StringBuffer());
    }


    public static <T extends Serializable> NullChain<T> noEmpty(T object, StringBuffer linkLog) {
        return new NullChainSyncDefault<>(object, false, linkLog);
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
    public static <T extends Serializable> NullChainAsync<T> asyncRun(boolean async, Queue<Supplier<NullChainAsync<T>>> asyncQueue, Supplier<NullChainAsync<T>> supplier, NullChainAsync<T> current ) throws RuntimeException {
        if (async) {
            //前一个任务是异步的,那么之后的任务需要在前一个任务执行完毕后执行
            asyncQueue.add(supplier);
            return current; //返回当前任务
        } else {
            return supplier.get();
        }
    }



}
