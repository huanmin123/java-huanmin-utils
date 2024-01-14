package com.huanmin.utils.null_chain;

import com.huanmin.utils.common.base.UniversalException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * @author huanmin
 * @date 2023/11/21
 */
@Slf4j
public class NullChainException extends UniversalException {

    public NullChainException(String message, Object... args) {
        super(message, args);
    }

    public NullChainException(Throwable e, String message, Object... args) {
        super(e, message, args);
    }

    public NullChainException(Throwable e) {
        super(e);
    }
}
