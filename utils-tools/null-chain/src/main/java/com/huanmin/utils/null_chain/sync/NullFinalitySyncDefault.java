package com.huanmin.utils.null_chain.sync;


import com.huanmin.utils.null_chain.base.NullFinality;
import com.huanmin.utils.null_chain.base.NullChainBase;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public  class NullFinalitySyncDefault<T extends Serializable> implements NullFinality<T> {
    protected NullChainBase<T> nullChainBase;

    private NullFinalitySyncDefault() {
    }

    protected NullFinalitySyncDefault(T object, boolean isNull, StringBuffer linkLog) {
        this.nullChainBase = new NullChainBase<>(object, isNull, linkLog);
    }

    @Override
    public boolean is() {
        return  this.nullChainBase.is() ;
    }

    @Override
    public void is(Consumer<? super T> consumer) {
        this.nullChainBase.is(consumer);
    }

    @Override
    public void isOr(Consumer<? super T> action, Runnable emptyAction) {
        this.nullChainBase.isOr(action, emptyAction);
    }

    @Override
    public T get() throws NullPointerException {
        return this.nullChainBase.get();
    }

    @Override
    public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return this.nullChainBase.orThrow(exceptionSupplier);
    }

    @Override
    public T orElse(T defaultValue) {
        return this.nullChainBase.orElse(defaultValue);
    }
}
