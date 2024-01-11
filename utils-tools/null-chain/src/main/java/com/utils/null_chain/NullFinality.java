package com.utils.null_chain;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public interface NullFinality<T>  {
    boolean is();

    void is(Consumer<? super T> consumer);
    void isOr(Consumer<? super T> action, Runnable emptyAction);

    T get() throws NullPointerException;


    <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X;

    T orElse(T defaultValue);





}