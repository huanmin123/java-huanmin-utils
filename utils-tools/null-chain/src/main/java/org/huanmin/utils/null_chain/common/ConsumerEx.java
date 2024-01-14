package org.huanmin.utils.null_chain.common;

public interface ConsumerEx<T> {
    void accept(T t) throws Throwable;
}
