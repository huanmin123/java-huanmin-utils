package com.utils.common.base;

import org.springframework.lang.NonNull;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * java内置定时器
 */
public class ScheduledThreadPoolExecutorUtils {

    private  ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private ScheduledThreadPoolExecutorUtils(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
    }

    private static class InitializeThreadFactory implements ThreadFactory {

       @Override
       public Thread newThread(@NonNull  Runnable r) {
           //可以在创建线程时候进行其他的处理,比如线程名称
           return new Thread(r);
       }
   }
   public static ScheduledThreadPoolExecutorUtils build() {
       return  new ScheduledThreadPoolExecutorUtils(new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2,new InitializeThreadFactory()));
   }

    /**
     * 延迟并以固定周期时间循环执行命令
     * @param command 任务     scheduleAtFixedRate不会计算任务本身的时间(推荐)。 scheduleWithFixedDelay的实际执行周期为延迟时间delay+任务执行时间。
     * @param initialDelay  延迟时间
     * @param period  周期
     * @param unit  时间单位
     */
   public   void scheduleAtFixedRate(Runnable command,
                              long initialDelay,
                              long period,
                              TimeUnit unit) {
       scheduledThreadPoolExecutor.scheduleAtFixedRate(command,initialDelay,period,unit);

   }

    /**
     *  周期时间循环执行命令
     * @param command 任务
     * @param delay 周期
     * @param unit 时间单位
     */
    public   void  schedule(Runnable command,
                       long delay, TimeUnit unit) {
        scheduledThreadPoolExecutor.schedule(command,delay,unit);
    }


}
