package org.huanmin.utils.null_chain.common;

import org.huanmin.utils.common.base.UniversalException;
import lombok.extern.slf4j.Slf4j;

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
