package com.huanmin.utils.null_chain.async;

import com.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import com.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import com.huanmin.utils.null_chain.*;
import com.huanmin.utils.null_chain.NullChain;
import com.huanmin.utils.null_chain.NullBuild;
import com.huanmin.utils.null_chain.NullFun;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public class NullChainAsyncDefault<T extends Serializable> extends NullConvertAsyncDefault<T> implements NullChainAsync<T> {


    public NullChainAsyncDefault(Future<?> future, T object, boolean async, boolean isNull, StringBuffer linkLog) {
        super(future, object, async, isNull, linkLog);
    }

    @Override
    public NullChain<T> async(NullFun<? super T, ?> handler) {
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

    @Override
    public <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U> function) {
        return null;
    }

    @Override
    public <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        return null;
    }

    @Override
    public <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function) {
        return null;
    }

    @Override
    public <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        return null;
    }

    @Override
    public <U extends Serializable> NullChain<U> map(NullFun<? super T, ? extends U> mapper) {
        return null;
    }

    @Override
    public NullChain<T> or(Supplier<? extends NullChain<T>> supplier) {
        return null;
    }

    @Override
    public <U> NullChain<T> pick(NullFun<? super T, ? extends U>... mapper) {
        return null;
    }

    @Override
    public <U> NullChain<T> pick(List<NullFun<? super T, ? extends U>> mapper) {
        return null;
    }

    @Override
    public NullChain<T> copy() {
        return null;
    }

    @Override
    public NullChain<T> deepCopy() {
        return null;
    }
}
