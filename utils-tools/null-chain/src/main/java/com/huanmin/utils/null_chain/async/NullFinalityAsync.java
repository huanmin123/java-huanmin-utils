package com.huanmin.utils.null_chain.async;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface NullFinalityAsync<T> extends Serializable {


    void is(Consumer<Boolean> consumer);

    void isOr(Consumer<? super T> action, Runnable emptyAction);

    T get(Consumer<? super T> consumer) throws NullPointerException;

    <X extends Throwable> void orThrow(Consumer<T> consumer,Supplier<? extends X> exceptionSupplier) throws X;

    void orElse(Consumer<T> consumer,T defaultValue);

}
