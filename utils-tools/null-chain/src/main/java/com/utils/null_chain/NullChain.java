package com.utils.null_chain;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public interface NullChain<T> extends NullConvert<T>, NullFinality<T> {
    <U> NullChain<U> of(NullFun<? super T, ? extends U > function);


    <U> NullChain<U> of(NullFun<? super T, ? extends U > function, Consumer<U> consumer);


    <U> NullChain<U> no(NullFun<? super T, ? extends U> function);

    <U> NullChain<U> no(NullFun<? super T, ? extends U> function,Consumer<U> consumer);

    <U> NullChain<U> map(NullFun<? super T, ? extends U> mapper);

    NullChain<T> or(Supplier<? extends NullChain<T>> supplier);




    //将单个NullChain转换为新的NullChain,用于提取一个NullChain<T>中多个值,然后产生一个新的NullChain<T>
    <U> NullChain<U> pick(NullFun<? super T, ? extends U> ...mapper);
    //复制浅拷贝
    NullChain<T> copy();
    //复制深拷贝
    NullChain<T> deepCopy();


}
