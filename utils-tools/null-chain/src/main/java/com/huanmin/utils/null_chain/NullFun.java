package com.huanmin.utils.null_chain;

import java.io.Serializable;

/**
 * @author huanmin
 * @date 2024/1/11
 */
@FunctionalInterface
public interface NullFun<T, R> extends java.util.function.Function<T, R>, Serializable {

}