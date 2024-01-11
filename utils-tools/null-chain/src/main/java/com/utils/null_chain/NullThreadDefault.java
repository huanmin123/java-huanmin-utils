package com.utils.null_chain;

import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.executor.ThreadFactoryUtil;

import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public class NullThreadDefault<T extends Serializable> extends NullConvertDefault<T> implements NullThread<T> {


    @Override
    public NullChain<T> asyn(NullFun<? super T, ?> handler) {
        if (isNull) {
            return NullBuild.empty(linkLog);
        }
        Future<?> futureR = ExecutorUtil.createFutureR(ThreadFactoryUtil.ThreadConfig.NULL, () -> handler.apply(value));
        return NullBuild.async(futureR, value, linkLog);
    }

    @Override
    @SuppressWarnings("unchecked")
    public<U extends  Serializable>NullChain<U> wait(Class<U> uClass ){
        if (!async) {
            return NullBuild.empty(linkLog);
        }
        try {
            U u = (U) future.get();
            return  NULL.of(u);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
