package org.huanmin.utils.null_chain.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class NullExecutor {
    //获取cpu核心数
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    //无限制的线程池
    public static ThreadPoolExecutor create(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("NullChain-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                 Integer.MAX_VALUE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                namedThreadFactory);
        return executor;
    }
}
