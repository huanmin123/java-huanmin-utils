package org.huanmin.test.utils.utils_common.multithreading.utils;


import org.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import org.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.huanmin.utils.common.multithreading.utils.LockSupportUtil;
import org.huanmin.utils.common.multithreading.utils.SleepTools;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class LockSupportUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(LockSupportUtilsTest.class);

    //这个是没有Thread对象我们在内部进行手动处理
    @Test
    public void show() {
        AtomicInteger num= new AtomicInteger();

        //生成消费者
        new Thread(()->{
            LockSupportUtil.setThread("key2");
            while (true) {
                if (num.intValue()>=10) {
                    LockSupportUtil.notifyLock("key1");
                    return;
                }
                if (num.intValue() == 0) {
                    //阻塞等待生产者生产
                    LockSupportUtil.waitLock();
                } else {
                    System.out.println("消费者-消费"+num.intValue());
                    //唤醒生产者,然后自己阻塞
                    LockSupportUtil.notifyWaitLock("key1");
                }

            }
        }).start();

        //生成生产者
        new Thread(()->{
            LockSupportUtil.setThread("key1");
            while (true) {
                if (num.intValue()>=10) {
                    LockSupportUtil.notifyLock("key2");
                    return;
                }
                num.incrementAndGet();
                System.out.println("生产者-生产"+num.intValue());
                LockSupportUtil.notifyWaitLock("key2");
            }
        }).start();
        SleepTools.second(10);
    }


    //能拿到Thread我们可以直接在外部处理
    @Test
    public void show1() {
        AtomicInteger num= new AtomicInteger();

        //生成者消费者
        Thread thread1 = new Thread(() -> {
            while (true) {
                if (num.intValue() >= 10) {
                    LockSupportUtil.notifyLock("key1");
                    return;
                }

                if (num.intValue() == 0) {
                    //阻塞等待生产者生产
                    LockSupportUtil.waitLock();
                } else {
                    System.out.println("消费者-消费"+num.intValue());
                    //唤醒生产者,然后自己阻塞
                    LockSupportUtil.notifyWaitLock("key1");
                }
            }
        });

        //生成生产者
        Thread thread = new Thread(() -> {

            while (true) {
                if (num.intValue() >= 10) {
                    LockSupportUtil.notifyLock("key2");
                    return;
                }
                num.incrementAndGet();
                System.out.println("生产者-生产"+num.intValue());
                LockSupportUtil.notifyWaitLock("key2");
            }
        });

        //生产者
        LockSupportUtil.setThread("key1",thread);
        //消费者
        LockSupportUtil.setThread("key2",thread1);

        thread1.start();
        thread.start();

        SleepTools.second(10);
    }


    //线程池方式
    @Test
    public void show2() {
        AtomicInteger num= new AtomicInteger();
        //生成者消费者
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.TEST,LockSupportUtil.run("key2",()->{
            while (true) {
                if (num.intValue() >= 10) {
                    LockSupportUtil.notifyLock("key1");
                    return;
                }

                if (num.intValue() == 0) {
                    //阻塞等待生产者生产
                    LockSupportUtil.waitLock();
                } else {
                    System.out.println("消费者-消费"+num.intValue());
                    //唤醒生产者,然后自己阻塞
                    LockSupportUtil.notifyWaitLock("key1");
                }
            }
        }));

        //生成生产者
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.TEST, LockSupportUtil.run("key1",()->{
            while (true) {
                if (num.intValue() >= 10) {

                    return;
                }
                num.incrementAndGet();
                System.out.println("生产者-生产"+num.intValue());
                //唤醒指定消费者进行消费,然后阻塞自己
                LockSupportUtil.notifyWaitLock("key2");
            }
        }));

        SleepTools.second(10);

    }


}
