package com.huanmin.utils.null_chain.async;

import com.huanmin.utils.null_chain.NullFun;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public interface NullChainAsync<T> extends NullConvertAsync<T>, NullFinalityAsync<T> {


    NullChainAsync<T > async(NullFun<? super T, ?> handler);

    //等待上一个异步执行完成拿到结果后,再执行下一个动作
     <U extends  Serializable>NullConvertAsync<U> wait(Class<U> uClass );


    <U extends Serializable> NullConvertAsync<U> of(NullFun<? super T, ? extends U > function);


    <U extends Serializable> NullConvertAsync<U> of(NullFun<? super T, ? extends U > function, Consumer<U> consumer);


    <U extends Serializable> NullConvertAsync<U> no(NullFun<? super T, ? extends U> function);

    <U extends Serializable> NullConvertAsync<U> no(NullFun<? super T, ? extends U> function,Consumer<U> consumer);

    <U extends Serializable> NullConvertAsync<U> map(NullFun<? super T, ? extends U> mapper);

    NullConvertAsync<T> or(Supplier<? extends NullConvertAsync<T>> supplier);

    //将一个类型转换为另外一个类型, 万能转换,啥都能做
    <U  extends Serializable> NullConvertAsync<U> convert(NullFun<? super T, ? extends U> mapper);


    //将单个NullChain转换为新的NullChain,用于提取一个NullChain<T>中多个值,然后产生一个新的NullChain<T>, 如果是空的那么就不取
    <U> NullConvertAsync<T> pick(NullFun<? super T, ? extends U> ...mapper);
    <U> NullConvertAsync<T> pick(List<NullFun<? super T, ? extends U>> mapper);
    //复制浅拷贝
    NullConvertAsync<T> copy();
    //复制深拷贝
    NullConvertAsync<T> deepCopy();
}
