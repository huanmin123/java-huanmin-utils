package com.huanmin.utils.null_chain.async;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface NullFinalityAsync<T> extends Serializable {

    boolean is();

    void is(Consumer<? super T> consumer);

    void isOr(Consumer<? super T> action, Runnable emptyAction);


    T get() throws NullPointerException;


    <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X;

    T orElse(T defaultValue);

}
