package com.huanmin.test.utils_common.multithreading.cas;

import com.function.impl.CodeStartAndStopTimeUtil;
import com.multithreading.executor.ExecutorUtil;
import com.multithreading.executor.ThreadFactoryUtil;
import com.utils.common.multithreading.cas.AtomicStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AtomicStringUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(AtomicStringUtilsTest.class);
    public static void main(String[] args) throws Exception {

        CodeStartAndStopTimeUtil.creator(()->{
            AtomicStringUtils build = AtomicStringUtils.build("da1");
            Collection<Future<?>> futures = new LinkedList<Future<?>>();
            for (int i1 = 0; i1 < 5000; i1++) {
                Future<?> future = ExecutorUtil.createFuture(ThreadFactoryUtil.ThreadConfig.TEST,()->{
                    for (int i2 = 0; i2 < 10; i2++) {
                        build.updateAndGet("a");
                    }
                });
                futures.add(future);
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(build.get());
        });



    }
}
