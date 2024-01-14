package org.huanmin.test.utils.utils_common.multithreading.cas;

import org.huanmin.utils.common.multithreading.cas.AtomicStampedReferenceDoubleUtil;
import org.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import org.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AtomicStampedReferenceLongUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(AtomicStampedReferenceLongUtilsTest.class);


    @Test
    public void  show(){
        AtomicStampedReferenceDoubleUtil build = new AtomicStampedReferenceDoubleUtil();
        for (int i2 = 0; i2 < 1000; i2++) {
            build.update(build.get()+i2);
        }
        System.out.println(String.valueOf(build.get()));
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        AtomicStampedReferenceDoubleUtil build = new AtomicStampedReferenceDoubleUtil();
        Collection<Future<?>> futures = new LinkedList<Future<?>>();
        for (int i = 0; i < 1000; i++) {
            Future<?> future = ExecutorUtil.createFuture(ThreadFactoryUtil.ThreadConfig.TEST,()->{
                for (int i2 = 0; i2 < 100000; i2++) {
                    build.update((double) i2);
                }
            });
            futures.add(future);
        }
        for (Future<?> future : futures) {
            future.get();
        }
        System.out.println("====="+String.valueOf(build.get()));

    }
}
