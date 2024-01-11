package com.huanmin.utils.null_chain.async;

import com.huanmin.utils.null_chain.NullFinality;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NullFinalityAsyncDefault  <T extends Serializable> implements NullFinality<T>{
    protected boolean isNull=false; //true 为null ,false 不为null
    protected T value;
    protected StringBuffer linkLog = new StringBuffer();
    protected Future<?> future;
    protected Queue<Runnable> asyncQueue;
    protected boolean async=false;

    protected NullFinalityAsyncDefault(Future<?> future,T object, boolean async, boolean isNull, StringBuffer linkLog) {
        this.isNull = isNull;
        this.value = object;
        this.future = future;
        this.async = async;
        this.asyncQueue = new LinkedList<>();
        if (object != null) {
            this.linkLog.append(object.getClass().getName()).append("->");
        }
        if (linkLog.length() > 0) {
            this.linkLog.append(linkLog);
        }
    }
    @Override
    public boolean is() {
        return false;
    }

    @Override
    public void is(Consumer<? super T> consumer) {

    }

    @Override
    public void isOr(Consumer<? super T> action, Runnable emptyAction) {

    }

    @Override
    public T get() throws NullPointerException {
        return null;
    }

    @Override
    public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return null;
    }

    @Override
    public T orElse(T defaultValue) {
        return null;
    }
}
