package com.utils.null_chain;

import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public interface NullThread<T> {
    //异步执行,

    NullChain<T > asyn(NullFun<? super T, ?> handler);

    //等待上一个异步执行完成拿到结果后,再执行下一个动作
     <U extends  Serializable>NullChain<U> wait(Class<U> uClass );
}