package com.huanmin.utils.null_chain.async;

import com.huanmin.utils.common.enums.DateEnum;
import com.huanmin.utils.null_chain.base.NullConvert;
import com.huanmin.utils.null_chain.NullFun;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public  class NullConvertAsyncDefault<T extends Serializable> extends NullFinalityAsyncDefault<T > implements NullConvertAsync<T> {


    protected NullConvertAsyncDefault(Future<T> future, boolean isNull, StringBuffer linkLog) {
        super(future, isNull, linkLog);
    }

    protected NullConvertAsyncDefault(boolean isNull, T value, StringBuffer linkLog) {
        super(isNull, value, linkLog);
    }



    @Override
    public Stream<T> stream() {
        return null;
    }

    @Override
    public Optional<T> optional() {
        return Optional.empty();
    }

    @Override
    public String toStr() {
        return null;
    }

    @Override
    public Integer toInt() {
        return null;
    }

    @Override
    public String toDateFormat(DateEnum dateEnum) {
        return null;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public String toJson() {
        return null;
    }
}