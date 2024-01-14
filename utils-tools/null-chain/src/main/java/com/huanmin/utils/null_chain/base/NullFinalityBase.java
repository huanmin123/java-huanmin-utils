package com.huanmin.utils.null_chain.base;


import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public  class NullFinalityBase<T extends Serializable> implements NullFinality<T> {
    protected boolean isNull=false; //true 为null ,false 不为null
    protected T value;
    protected StringBuffer linkLog = new StringBuffer();

    public void setValue(T value) {
        this.value = value;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    @Override
    public boolean is() {
        return isNull;
    }

    @Override
    public void get(Consumer<? super T> consumer) {
        if (!isNull) {
            consumer.accept(value);
        }
    }

    @Override
    public void get(Consumer<? super T> action, Runnable emptyAction) {
        if (!isNull) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }


    @Override
    public T get() throws NullPointerException {
        if (isNull) {
            throw new NullPointerException(linkLog.toString());
        }
        return value;
    }


    @Override
    public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (!isNull) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public T orElse(T defaultValue) {
        return !isNull ? value : defaultValue;
    }



}
