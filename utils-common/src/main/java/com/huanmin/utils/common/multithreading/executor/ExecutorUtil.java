package com.huanmin.utils.common.multithreading.executor;

import com.huanmin.utils.common.base.UniversalException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class ExecutorUtil {

    //守护线程
    public static void newThreadDaemon(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {System.out.println(t.getName() + ": " + e.getMessage());});
        thread.setDaemon(true);
        thread.start();
    }
    //创建普通线程
    public static void newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {System.out.println(t.getName() + ": " + e.getMessage());});
        thread.start();
    }
    public static <T> Future<T> newThread(Callable<T> callable) {
        FutureTask<T> futureTask = new FutureTask<T>(callable);
        Thread thread = new Thread(futureTask);
        thread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {System.out.println(t.getName() + ": " + e.getMessage());});
        thread.start();
        return futureTask;
    }

    public static void create(ThreadFactoryUtil.ThreadConfig config, Runnable runnable) {
        ThreadPoolExecutor executor = ThreadFactoryUtil.getExecutor(config);
        executor.submit(runnable);
    }

    public static Future<?> createFutureR(ThreadFactoryUtil.ThreadConfig config, Callable callable) {
        ThreadPoolExecutor executor = ThreadFactoryUtil.getExecutor(config);
        return executor.submit(callable);

    }

    public static Future<?> createFuture(ThreadFactoryUtil.ThreadConfig config, Runnable runnable) {
        ThreadPoolExecutor executor = ThreadFactoryUtil.getExecutor(config);
        return executor.submit(runnable);

    }


    public static Collection<Future<?>> createFutures(  List<Runnable> runnables,ThreadFactoryUtil.ThreadConfig config) {
        Collection<Future<?>> futures = new LinkedList<Future<?>>();
        ThreadPoolExecutor executor = ThreadFactoryUtil.getExecutor(config);
        for (Runnable function : runnables) {
            futures.add(executor.submit(function));
        }
        return futures;
    }



    public static  Collection<Future<?>> createFutures(ThreadFactoryUtil.ThreadConfig config, List<Callable> callables) {
        Collection<Future<?>> futures = new LinkedList<Future<?>>();
        ThreadPoolExecutor executor = ThreadFactoryUtil.getExecutor(config);
        for (Callable callable : callables) {
            futures.add(executor.submit(callable));
        }
        return futures;
    }




    //不用等待所有线程执行完毕,而是谁先执行完毕,就返回谁的结果 ,以此类推,等待全部线程执行完毕
    public  static void createCompletionServicesAll(ThreadFactoryUtil.ThreadConfig config, List<Callable> callables, Consumer< Future> consumer)  {
        CompletionService<?> completionService = new ExecutorCompletionService<>(ThreadFactoryUtil.getExecutor(config));
        for (Callable callable : callables) {
            completionService.submit(callable);
        }
        try {
            for (int i = 0; i < callables.size(); i++) {
                consumer.accept(completionService.take());
            }
        } catch (InterruptedException e) {
             UniversalException.logError(e);
        }
    }
    //一堆线程同时执行,谁先执行完毕那么就采用谁的结果,其余线程结果不管  ,注意如果执行了,
    // 但是没有任何线程结果返回,那么一直等待,如果是线程内部向外部报错了,那么也算返回了,并且打印错误
    public  static Object createCompletionServicesOne(ThreadFactoryUtil.ThreadConfig config, List<Callable> callables) throws ExecutionException {
        CompletionService<?> completionService = new ExecutorCompletionService<>(ThreadFactoryUtil.getExecutor(config));
        for (Callable callable : callables) {
            completionService.submit(callable);
        }
        Object result=null;
        try {
            result=completionService.take().get();
        } catch (InterruptedException e) {
             UniversalException.logError(e);
        }

        return result;
    }







    //阻塞全部线程完成后
    public static void waitComplete(Collection<Future<?>> collection) {
        for (Future<?> future : collection) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                 UniversalException.logError(e);
            }
        }
    }
    //阻塞单个线程完成后
    public static void waitComplete(Future<?> future) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
             UniversalException.logError(e);
        }
    }



}
