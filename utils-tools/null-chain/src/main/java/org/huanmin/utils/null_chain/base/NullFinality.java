package org.huanmin.utils.null_chain.base;

import org.huanmin.utils.null_chain.common.NullChainException;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public interface NullFinality<T extends Serializable>  extends Serializable {
    boolean is();
    void get(Consumer<? super T> action, Runnable emptyAction);

    void get(Consumer<? super T> consumer);
    T get() throws NullChainException;

    <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X;

    T orElse(T defaultValue);


}
