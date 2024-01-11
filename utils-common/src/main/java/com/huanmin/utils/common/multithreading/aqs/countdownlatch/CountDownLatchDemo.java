package com.huanmin.utils.common.multithreading.aqs.countdownlatch;

// CountDownLatch是一次性的，计算器的值只能在构造方法中初始化一次，之后没有任何机制再次对其设置值，
// 当CountDownLatch使用完毕后，它不能再次被使用。

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {


    //构建一个阻挡器  ,count就是格挡的次数
    public void demo() throws InterruptedException {
        CountDownLatch countDownLatch=new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"准备好了");
                countDownLatch.countDown(); //减少锁存器的计数，如果计数达到零，则释放所有等待线程。
  
                System.out.println(Thread.currentThread().getName()+"开始执行");
            }).start();
        }
    
        countDownLatch.await(); //使当前线程等待，直到锁存器倒数为零
        //使当前线程等待，直到锁存器倒数为零，如果等待超时那么就不等待了。
        //countDownLatch.await(10, TimeUnit.SECONDS);
 
    }
    

  

}
