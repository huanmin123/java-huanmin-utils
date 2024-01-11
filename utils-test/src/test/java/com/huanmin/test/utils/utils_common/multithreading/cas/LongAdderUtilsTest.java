package com.huanmin.test.utils.utils_common.multithreading.cas;

import com.huanmin.utils.common.multithreading.cas.LongAdderUtil;
import com.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import com.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LongAdderUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(LongAdderUtilsTest.class);


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        LongAdderUtil build = new LongAdderUtil();
        Collection<Future<?>> futures = new LinkedList<Future<?>>();
        for (int i = 0; i < 5; i++) {
            Future<?> future = ExecutorUtil.createFuture(ThreadFactoryUtil.ThreadConfig.TEST,()->{
                for (int i1 = 0; i1 < 100000; i1++) {
                    build.increment();
                }
            });
            futures.add(future);
        }


        for (Future<?> future : futures) {
            future.get();
        }
        System.out.println(String.valueOf(build.get()));
    }
}
