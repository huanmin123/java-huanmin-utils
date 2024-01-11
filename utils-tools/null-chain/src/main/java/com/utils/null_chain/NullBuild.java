package com.utils.null_chain;

import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public class NullBuild {
    protected static  void  asyncRun(boolean async,, Runnable runnable){
     if (async){

     }


    }
    protected static <T extends Serializable> NullChain<T> empty() {
        return new NullChainDefault<>((T) null, true, new StringBuffer());
    }

    protected static <T extends Serializable> NullChain<T> empty(StringBuffer linkLog) {
        return new NullChainDefault<T>((T) null, true, linkLog);
    }

    protected static <T extends Serializable> NullChain<T> noEmpty(T object) {

        return new NullChainDefault<>(object, false, new StringBuffer());
    }


    protected static <T extends Serializable> NullChain<T> noEmpty(T object, StringBuffer linkLog) {
        return new NullChainDefault<>(object, false, linkLog);
    }
    protected static <K, V extends Serializable> Map<K, NullChain<V>> emptyMap() {
        Map<K, V> map = new HashMap<>();
        return (Map<K, NullChain<V>>) map;
    }

    //空list
    protected static <T extends Serializable> List<NullChain<T>> emptyList() {
        List<T> list = new ArrayList<>();
        return (List<NullChain<T>>) list;
    }

    //空array
    protected static <T extends Serializable> NullChain<T>[] emptyArray() {
        return (NullChain<T>[]) new NullChain[0];
    }
    protected static <T extends Serializable> NullChain<T> async(Future<?> future, T object, StringBuffer linkLog) {
        return new NullChainDefault<>( future,object,true, false, linkLog);
    }




}
