package com.huanmin.test.utils_common.multithreading.cas;

import com.function.impl.CodeStartAndStopTimeUtil;
import com.multithreading.executor.ExecutorUtil;
import com.multithreading.executor.ThreadFactoryUtil;
import com.utils.common.multithreading.cas.AtomicMarkableReferenceStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AtomicStampedReferenceStringUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(AtomicStampedReferenceStringUtilsTest.class);
    public   static void main(String[] args) throws Exception {
        CodeStartAndStopTimeUtil.creator(()->{
            AtomicMarkableReferenceStringUtils build = AtomicMarkableReferenceStringUtils.build("da1");
            Collection<Future<?>> futures = new LinkedList<Future<?>>();
            for (int i1 = 0; i1 < 1000; i1++) {
                Future<?> future = ExecutorUtil.createFuture(ThreadFactoryUtil.ThreadConfig.TEST,()->{
                    for (int i = 0; i < 100000; i++) {
                        build.update( String.valueOf(i));
                    }
                });
                futures.add(future);
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(String.valueOf(build.get()));
        });



    }
}
