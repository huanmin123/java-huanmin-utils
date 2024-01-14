package org.huanmin.utils.null_chain.base;

import org.huanmin.utils.null_chain.common.NullChainException;
import org.huanmin.utils.null_chain.common.NullFunEx;

import java.io.Serializable;

public interface NullChainAsync<T extends Serializable> {


    <U extends Serializable> NullChainAsync<U> async(NullFunEx<NullChain<? super T>, ? extends U> consumer)throws NullChainException;


}
