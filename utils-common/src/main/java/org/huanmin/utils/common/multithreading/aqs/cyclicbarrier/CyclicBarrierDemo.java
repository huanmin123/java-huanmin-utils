package org.huanmin.utils.common.multithreading.aqs.cyclicbarrier;

import org.huanmin.utils.common.base.UniversalException;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    
    
    //    CyclicBarrier是可循环使用的，计数器的值可以使用reset()方法重置。
    public void demo1(){
        CyclicBarrier cyclicBarrier=new CyclicBarrier(5); //创建一个阻挡器,阻挡5个线程
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                try {
                    System.out.println(Thread.currentThread().getName()+"准备好了");
                    cyclicBarrier.await(); //阻塞当前线程,等待所有线程都准备好了,一起执行
                    //阻塞当前线程,等待所有线程都准备好了,一起执行,如果等待一定时间后部分线程还没准备好那么就不管他了
                    //cyclicBarrier.await(10, TimeUnit.SECONDS);
                    System.out.println(Thread.currentThread().getName()+"开始执行");
                } catch (InterruptedException | BrokenBarrierException e) {
                     UniversalException.logError(e);
                }
            }).start();
        }
    }


}
