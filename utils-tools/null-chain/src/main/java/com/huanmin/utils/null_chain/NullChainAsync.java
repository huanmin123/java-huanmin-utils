package com.huanmin.utils.null_chain;

import com.huanmin.utils.null_chain.NullChain;
import com.huanmin.utils.null_chain.NullFun;

import java.io.Serializable;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public interface NullChainAsync<T> extends NullChain<T> {
    //异步执行,

    NullChain<T > async(NullFun<? super T, ?> handler);

    //等待上一个异步执行完成拿到结果后,再执行下一个动作
     <U extends  Serializable>NullChain<U> wait(Class<U> uClass );
}
