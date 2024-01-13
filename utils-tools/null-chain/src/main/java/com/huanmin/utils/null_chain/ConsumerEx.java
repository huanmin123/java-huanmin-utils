package com.huanmin.utils.null_chain;

public interface ConsumerEx<T> {
    void accept(T t) throws Throwable;
}
