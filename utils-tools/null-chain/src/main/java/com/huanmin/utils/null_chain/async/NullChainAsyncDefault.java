package com.huanmin.utils.null_chain.async;

import com.huanmin.utils.null_chain.NullFun;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public class NullChainAsyncDefault<T extends Serializable> extends NullConvertAsyncDefault<T> implements NullChainAsync<T> {


    protected NullChainAsyncDefault(Future<T> future, boolean isNull, StringBuffer linkLog) {
        super(future, isNull, linkLog);
    }

    public NullChainAsyncDefault(boolean isNull, T value, StringBuffer linkLog) {
        super(isNull, value, linkLog);
    }

    @Override
    public NullChainAsync<T> async(NullFun<? super T, ?> handler) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public<U extends  Serializable>NullConvertAsync<U> wait(Class<U> uClass ){
        return null;
    }

    @Override
    public <U extends Serializable> NullConvertAsync<U> of(NullFun<? super T, ? extends U> function) {
        return null;
    }

    @Override
    public <U extends Serializable> NullConvertAsync<U> of(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        return null;
    }

    @Override
    public <U extends Serializable> NullConvertAsync<U> no(NullFun<? super T, ? extends U> function) {
        return null;
    }

    @Override
    public <U extends Serializable> NullConvertAsync<U> no(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        return null;
    }

    @Override
    public <U extends Serializable> NullConvertAsync<U> map(NullFun<? super T, ? extends U> mapper) {
        return null;
    }

    @Override
    public NullConvertAsync<T> or(Supplier<? extends NullConvertAsync<T>> supplier) {
        return null;
    }

    @Override
    public <U extends Serializable> NullConvertAsync<U> convert(NullFun<? super T, ? extends U> mapper) {
        return null;
    }

    @Override
    public <U> NullConvertAsync<T> pick(NullFun<? super T, ? extends U>... mapper) {
        return null;
    }

    @Override
    public <U> NullConvertAsync<T> pick(List<NullFun<? super T, ? extends U>> mapper) {
        return null;
    }

    @Override
    public NullConvertAsync<T> copy() {
        return null;
    }

    @Override
    public NullConvertAsync<T> deepCopy() {
        return null;
    }


}
