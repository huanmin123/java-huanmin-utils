package com.huanmin.utils.null_chain.sync;

import com.huanmin.utils.null_chain.NullChain;
import com.huanmin.utils.null_chain.NullFun;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public class NullChainDefault<T extends Serializable> extends NullConvertDefault<T> implements NullChain<T> {


    public NullChainDefault(T object, boolean isNull, StringBuffer linkLog) {
        super(object, isNull, linkLog);
    }

    @Override
    public <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U> function) {
        return this.nullChainBase.of(function);
    }

    @Override
    public <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        return this.nullChainBase.of(function, consumer);
    }

    @Override
    public <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function) {
        return this.nullChainBase.no(function);
    }

    @Override
    public <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        return this.nullChainBase.no(function, consumer);
    }

    @Override
    public <U extends Serializable> NullChain<U> map(NullFun<? super T, ? extends U> mapper) {
        return this.nullChainBase.map(mapper);
    }

    @Override
    public NullChain<T> or(Supplier<? extends NullChain<T>> supplier) {
        return this.nullChainBase.or(supplier);
    }

    @Override
    public <U> NullChain<T> pick(NullFun<? super T, ? extends U>... mapper) {
        return this.nullChainBase.pick(mapper);
    }

    @Override
    public <U> NullChain<T> pick(List<NullFun<? super T, ? extends U>> mapper) {
        return this.nullChainBase.pick(mapper);
    }

    @Override
    public NullChain<T> copy() {
        return this.nullChainBase.copy();
    }

    @Override
    public NullChain<T> deepCopy() {
        return this.nullChainBase.deepCopy();
    }
}
