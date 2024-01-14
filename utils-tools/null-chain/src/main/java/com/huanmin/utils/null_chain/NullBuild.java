package com.huanmin.utils.null_chain;


import cn.hutool.log.Log;
import com.google.common.collect.Maps;
import com.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import com.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import com.huanmin.utils.null_chain.base.NullChain;
import com.huanmin.utils.null_chain.base.NullChainBase;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @author huanmin
 * @date 2024/1/11
 */
@Slf4j
public class NullBuild {
    public static  Map<String, Object> resultMap= Maps.newConcurrentMap();
    public static  Map<String, Queue<Thread>> threadMap= Maps.newConcurrentMap();
    public static  Map<String, Boolean> stopMap= Maps.newConcurrentMap();
    public static  Map<String, AtomicInteger> stopVerifyMap= Maps.newConcurrentMap();


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



    public static void stop(String key) {
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.NULL, () -> {
            NullBuild.stopMap.put(key, true);
            Queue<Thread> threads = NullBuild.threadMap.get(key);
            if (threads != null) {//唤醒所有线程
                for (Thread thread : threads) {
                    LockSupport.unpark(thread);
                }
            }

            AtomicInteger atomicInteger = NullBuild.stopVerifyMap.get(key);
            while (atomicInteger.get()!=threads.size()){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
             log.info("stop-清理资源");
            NullBuild.resultMap.remove(key);
            NullBuild.threadMap.remove(key);
            NullBuild.stopMap.remove(key);
            NullBuild.stopVerifyMap.remove(key);
        });
    }
    public static void clear(String key) {
        log.info("clear-清理资源");
        NullBuild.threadMap.remove(key);
        NullBuild.stopMap.remove(key);
        NullBuild.resultMap.remove(key);
        NullBuild.stopVerifyMap.remove(key);
    }

}
