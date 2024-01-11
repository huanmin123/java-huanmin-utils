package com.huanmin.test.utils.utils_common.multithreading.cas;

import com.huanmin.utils.common.base.CodeTimeUtil;
import com.huanmin.utils.common.base.UniversalException;
import com.huanmin.utils.common.multithreading.cas.AtomicLongUtil;
import com.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import com.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AtomicLongUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(AtomicLongUtilsTest.class);
    public static void main(String[] args) throws Exception {
        CodeTimeUtil.creator(()->{
            AtomicLongUtil build = new AtomicLongUtil();
            Collection<Future<?>> futures = new LinkedList<Future<?>>();
            for (int i = 0; i < 5; i++) {
                Future<?> future = ExecutorUtil.createFuture(ThreadFactoryUtil.ThreadConfig.TEST,()->{
                    for (int i2 = 0; i2 < 100000; i2++) {
                        build.incrementAdd();
                    }
                });
                futures.add(future);

            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                     UniversalException.logError(e);
                } catch (ExecutionException e) {
                     UniversalException.logError(e);
                }
            }
            System.out.println(String.valueOf(build.get()));
        });


    }
}
