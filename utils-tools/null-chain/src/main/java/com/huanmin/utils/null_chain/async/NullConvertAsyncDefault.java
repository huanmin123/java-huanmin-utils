package com.huanmin.utils.null_chain.async;

import com.huanmin.utils.common.enums.DateEnum;
import com.huanmin.utils.common.json.JsonJacksonUtil;
import com.huanmin.utils.common.obj.serializable.SerializeUtil;
import com.huanmin.utils.common.string.StringUtil;
import com.huanmin.utils.null_chain.NullConvert;
import com.huanmin.utils.null_chain.NullFun;
import com.huanmin.utils.null_chain.sync.NullFinalityDefault;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public  class NullConvertAsyncDefault<T extends Serializable> extends NullFinalityAsyncDefault<T > implements NullConvert<T> {


    protected NullConvertAsyncDefault(Future<?> future, T object, boolean async, boolean isNull, StringBuffer linkLog) {
        super(future, object, async, isNull, linkLog);
    }

    @Override
    public <U> U convert(NullFun<? super T, ? extends U> mapper) {
        return null;
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