package com.huanmin.utils.null_chain.async;

import com.huanmin.utils.null_chain.base.NullFinality;
import com.huanmin.utils.null_chain.base.NullChainBase;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NullFinalityAsyncDefault  <T extends Serializable> implements NullFinalityAsync<T>{
    protected NullChainBase<T> nullChainBase;
    protected StringBuffer linkLog = new StringBuffer();
    protected Future<T> future;
    protected boolean  async=false;//true 异步，false 同步
    protected Queue<Runnable> asyncQueue;

    protected NullFinalityAsyncDefault(Future<T> future, boolean isNull, StringBuffer linkLog) {
        this.future = future;
        this.async=true;
        this.asyncQueue = new LinkedList<>();
        this.nullChainBase = new NullChainBase<>(null, isNull, linkLog);
    }

    protected NullFinalityAsyncDefault( boolean isNull,T value, StringBuffer linkLog) {
        this.future = null;
        this.async=false;
        this.asyncQueue = new LinkedList<>();
        this.nullChainBase = new NullChainBase<>(value, isNull, linkLog);
    }




    @Override
    public void isOr(Consumer<? super T> action, Runnable emptyAction) {

    }


    @Override
    public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return null;
    }

    @Override
    public T orElse(T defaultValue) {
        return null;
    }

    //将NullChainBase转换为NullChainAsyncDefault
    protected NullFinalityAsyncDefault<T> convert(NullChainBase<T> nullChainBase){
        return  new NullFinalityAsyncDefault<>( nullChainBase.is(),nullChainBase.get(), nullChainBase.getLinkLog());
    }
}
