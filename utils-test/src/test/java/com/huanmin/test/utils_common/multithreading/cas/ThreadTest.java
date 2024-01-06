package com.huanmin.test.utils_common.multithreading.cas;

import com.function.impl.CodeStartAndStopTimeUtil;
import com.multithreading.executor.ExecutorUtil;
import com.multithreading.executor.ThreadFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ThreadTest {
    private static final Logger logger = LoggerFactory.getLogger(ThreadTest.class);
    public static void main(String[] args) throws Exception {

        CodeStartAndStopTimeUtil.creator(()->{
            StringBuffer build = new StringBuffer();
            Collection<Future<?>> futures = new LinkedList<Future<?>>();
            for (int i1 = 0; i1 < 5; i1++) {
                Future<?> future = ExecutorUtil.createFuture(ThreadFactoryUtil.ThreadConfig.TEST,()->{
                    for (int i = 0; i < 10000; i++) {
                        build.append("a");
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
            System.out.println(String.valueOf(build.length()));
        });


//        CodeStartAndStopTimeUtil.creator(()->{
//
//            Collection<Future<?>> futures = new LinkedList<Future<?>>();
//            for (int i1 = 0; i1 < 5; i1++) {
//                Future<?> future = ExecutorUtils.createFuture(()->{
//                    for (int i = 0; i < 100000; i++) {
//                        add();
//                    }
//                });
//
//                futures.add(future);
//
//            }
//            for (Future<?> future : futures) {
//                try {
//                    future.get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println(num);
//        });





    }
    static  Integer num = 0;

    public  synchronized static void add() {
        num = num +1;
    }



}
