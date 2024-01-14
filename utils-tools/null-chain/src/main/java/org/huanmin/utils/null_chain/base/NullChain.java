package org.huanmin.utils.null_chain.base;

import org.huanmin.utils.null_chain.common.NullChainException;
import org.huanmin.utils.null_chain.common.NullFun;
import org.huanmin.utils.null_chain.common.NullFunEx;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public interface NullChain<T extends Serializable> extends  NullConvert<T>, NullFinality<T> {



    <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U > function);


    <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U > function, Consumer<U> consumer);



    <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function);

    <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function,Consumer<U> consumer);

    <U extends Serializable> NullChain<U> map(NullFun<? super T, ? extends U> mapper);

    NullChain<T> or(Supplier<? extends NullChain<T>> supplier);

    //将一个类型转换为另外一个类型, 万能转换,啥都能做
    <U  extends Serializable> NullChain<U> convert(NullFun<? super T, ? extends U> mapper);


    //将单个NullChain转换为新的NullChain,用于提取一个NullChain<T>中多个值,然后产生一个新的NullChain<T>, 如果是空的那么就不取
    <U> NullChain<T> pick(NullFun<? super T, ? extends U> ...mapper);
    <U> NullChain<T> pick(List<NullFun<? super T, ? extends U>> mapper);
    //复制浅拷贝
    NullChain<T> copy();
    //复制深拷贝
    NullChain<T> deepCopy();

    <U extends Serializable> NullChainAsync<U> async(NullFunEx<NullChain<? super T>,? extends U> consumer)throws NullChainException;




}
