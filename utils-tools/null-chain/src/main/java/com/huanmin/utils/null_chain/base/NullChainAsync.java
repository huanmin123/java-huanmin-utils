package com.huanmin.utils.null_chain.base;

import com.huanmin.utils.null_chain.ConsumerEx;
import com.huanmin.utils.null_chain.NullFun;
import com.huanmin.utils.null_chain.NullFunEx;
import org.apache.tools.ant.taskdefs.Get;

import java.io.Serializable;

public interface NullChainAsync<T extends Serializable> {


    <U extends Serializable> NullChainAsync<U> async(NullFunEx<NullChain<? super T>,? extends U> consumer) ;



}
