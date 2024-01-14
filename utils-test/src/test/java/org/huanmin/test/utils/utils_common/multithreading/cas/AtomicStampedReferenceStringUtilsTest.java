package org.huanmin.test.utils.utils_common.multithreading.cas;

import org.huanmin.utils.common.base.CodeTimeUtil;
import org.huanmin.utils.common.base.UniversalException;
import org.huanmin.utils.common.multithreading.cas.AtomicMarkableReferenceStringUtil;
import org.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import org.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AtomicStampedReferenceStringUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(AtomicStampedReferenceStringUtilsTest.class);
    public   static void main(String[] args) throws Exception {
        CodeTimeUtil.creator(()->{
            AtomicMarkableReferenceStringUtil build = new AtomicMarkableReferenceStringUtil();
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
                     UniversalException.logError(e);
                } catch (ExecutionException e) {
                     UniversalException.logError(e);
                }
            }
            System.out.println(String.valueOf(build.get()));
        });



    }
}
