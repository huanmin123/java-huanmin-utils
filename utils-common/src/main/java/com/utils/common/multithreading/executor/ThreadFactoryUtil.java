package com.utils.common.multithreading.executor;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池
 * @author huanmin
 * @date 2023/11/21
 */
@Slf4j
public class ThreadFactoryUtil {

    private  static Map<ThreadConfig,ThreadPoolExecutor> executorMap=null;
    //获取cpu核心数
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    //现在栈大小都是1M, 那么假设你空闲内存有10G,那么最大线程数就是10G/1M=10000 ,但是一般来说5000就够了,其他程序也需要内存, 到了这个峰值那么就需要考虑
    //是否是逻辑问题,如果是逻辑问题,那么就需要考虑是否需要优化,如果不是逻辑问题,那么就需要考虑是否需要增加机器了, 官方建议jvm最大内存使用30G左右是极限了在多也没用,只会越来越慢
    // 而这30G除非就你一个程序不然你最多可用25G,因为还有其他程序需要内存,所以这个线程池的最大线程数就是25G/1M=25000, 但是考虑到单业务最大并发量,一般来说单台机器5000那么就是极限了
    private final   static int  LIMIT_MAXIMUM_POOL_SIZE=1000;   //如果单业务超出队列后还创建了1000次线程,那么就会报错,代码肯定有问题,要不就是该加服务器了,所以这里限制一下


    //配置线程参数
    // 最大线程:
    // CPU密集型任务配置尽可能小的线程，如配置cpu+1个线程的线程池。
    // IO密集型任务则由于线程并不是一直在执行任务，则配置尽可能多的线程,基本有多少个任务就配置多少个线程
    public   enum   ThreadConfig {
        TEST("TEST",20,100,"TEST"),
        ThreadUniteTransactionManager("ThreadUniteTransactionManager",100,1000,"主子线程事物统一处理"),
        BlockUtil("BlockUtil",20,1000,"BlockUtil"),
        FileNumberSortSection("FileNumberSortSection",20,1000,"FileNumberSortSection"),
        FileUtil("FileUtil",20,1000,"FileUtil"),
        Timer("Timer",20,1000,"Timer"),
        LinkedTransferQueueUtils("LinkedTransferQueueUtils",20,1000,"LinkedTransferQueueUtils"),
        CustomTimerUtils("CustomTimerUtils",20,1000,"CustomTimerUtils"),
        FIleSliceUploadController("FIleSliceUploadController",20,1000,"FIleSliceUploadController"),
        SynchronousQueueUtil("SynchronousQueueUtils",20,1000,"SynchronousQueueUtils"),
        BucketSortUtil("BucketSortUtil",200,10000,"BucketSortUtil"),
        Netty("Netty",20,1000,"Netty"),

        NULL("NULL",500,1000,"Netty");

        private String threadNamePrefix; //线程名称,前缀
        private int maximumPoolSize;//最大线程数
        private int taskNum;//任务数
        private String desc;//描述

        ThreadConfig(String threadNamePrefix,int maximumPoolSize, int taskNum, String desc){

            this.threadNamePrefix = threadNamePrefix;
            this.maximumPoolSize=maximumPoolSize;
            this.taskNum = taskNum;
            this.desc = desc;
        }


    }
    //配置当前项目所需的线程池
    static {
        Map<ThreadConfig,ThreadPoolExecutor> map=new HashMap<>();
        //遍历枚举类
        for (ThreadConfig threadConfig : ThreadConfig.values()) {
            //创建线程池
            ThreadPoolExecutor executor = create(threadConfig);
            //将线程池放入map中
            map.put(threadConfig,executor);
        }
        executorMap=Collections.unmodifiableMap(map);//不可修改的map

    }


    //获取线程池,只读模式不会涉及到线程安全问题
    public static ThreadPoolExecutor getExecutor(ThreadConfig threadConfig){
        return executorMap.get(threadConfig);
    }





    // threadPoolName: 线程池名称
    // 任务数量,建议任务数量要比实际统计的任务数量大一些
    //
    // taskNum任务数统计:  任务数=并发量 *任务执行时间+(并发量 *任务执行时间/2)
    private static ThreadPoolExecutor create(ThreadConfig threadConfig){

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadConfig.threadNamePrefix+"-%d").build();
        // 创建线程池，其中任务队列需要结合实际情况设置合理的容量
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                Math.max(threadConfig.maximumPoolSize, CORE_POOL_SIZE * 2),
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(threadConfig.taskNum),
                namedThreadFactory,
                new MyNewThreadRunsPolicy(threadConfig)); // 拒绝策略
        return executor;
    }


    //拒绝策略 ,直接在调用者线程中创建, 为什要这样做,因为业务场景不好预估,可能出现浮动,或者说是突发的高并发
    // 如果不这样做,那么就会出现线程池任务队列满了,但是线程池也满了,那么就会抛出异常,导致任务失败
    //解决办法就是记录error日志,然后在调用者线程中创建,这样就不会出现任务失败的情况
    //之后可以根据error日志,来调整线程池的大小, 最好加个监控,监控指定日志,如果发现了,就及时修改
    //后期可以做成动态的,替换Map里的线程池,这样就不用重启项目了
    public static class MyNewThreadRunsPolicy implements RejectedExecutionHandler {
        private  volatile int  count=0;
        private final ThreadConfig threadConfig;
        // 创建ThreadMXBean对象

        public MyNewThreadRunsPolicy(ThreadConfig threadConfig) {
            this.threadConfig=threadConfig;
        }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (!e.isShutdown()) {
                ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                        .setNameFormat(threadConfig.threadNamePrefix+"-full"+"-%d").build();
                Thread thread = namedThreadFactory.newThread(r);
                thread.start();
                synchronized (MyNewThreadRunsPolicy.class){
                    count++;
                    if (count>LIMIT_MAXIMUM_POOL_SIZE){
                        throw new RuntimeException("线程池和任务队列满了,并且额外创建了"+count+"个线程,请检查是否有逻辑问题,如果没有,请增加线程池大小或者增加服务器");
                    }
                }
                log.warn("######ThreadFull######{}线程池任务队列已满，当前线程数：{}，任务数：{}，拒绝策略：{}，不通过线程池已新创建线程数(总数)：{}",
                        threadConfig.threadNamePrefix,
                        e.getPoolSize(),
                        e.getQueue().size(),
                        e.getRejectedExecutionHandler().getClass().getSimpleName(),
                        count
                );


            }
        }
    }


}
