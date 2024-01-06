package com.huanmin.test.utils_common.multithreading.cas;

import com.multithreading.executor.ExecutorUtil;
import com.multithreading.executor.ThreadFactoryUtil;
import com.utils.common.multithreading.cas.LongAdderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LongAdderUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(LongAdderUtilsTest.class);


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        LongAdderUtils build = LongAdderUtils.build("da1");
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
