package com.huanmin.utils.null_chain.sync;

import com.huanmin.utils.common.enums.DateEnum;
import com.huanmin.utils.null_chain.base.NullConvert;
import com.huanmin.utils.null_chain.NullFun;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public  class NullConvertSyncDefault<T extends Serializable> extends NullFinalitySyncDefault<T > implements NullConvert<T> {


    protected NullConvertSyncDefault(T object, boolean isNull, StringBuffer linkLog) {
        super(object, isNull, linkLog);
    }



    @Override
    public Stream<T> stream() {
        return this.nullChainBase.stream();
    }

    @Override
    public Optional<T> optional() {
        return this.nullChainBase.optional();
    }

    @Override
    public String toStr() {
        return this.nullChainBase.toStr();
    }

    @Override
    public Integer toInt() {
        return this.nullChainBase.toInt();
    }

    @Override
    public String toDateFormat(DateEnum dateEnum) {
        return this.nullChainBase.toDateFormat(dateEnum);
    }

    @Override
    public byte[] toBytes() {
        return this.nullChainBase.toBytes();
    }

    @Override
    public String toJson() {
        return this.nullChainBase.toJson();
    }
}