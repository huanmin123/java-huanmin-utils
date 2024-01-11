package com.huanmin.utils.common.multithreading.aqs.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    public  void demo1() throws InterruptedException {
        Semaphore semaphore=new Semaphore(3); //创建一个信号量,信号量的大小为3
//       Semaphore semaphore=new Semaphore(3,true); //创建一个信号量,信号量的大小为3,公平信号量(先入先出授予许可)
        //创建3个线程,每个线程都会去获取信号量,如果信号量的大小为3,那么每个线程都会获取到信号量,如果信号量的大小为1,那么只有一个线程会获取到信号量
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //尝试获取信号量,如果获取到了信号量,那么返回true,如果没有获取到信号量,那么返回false ,不会等待
                    boolean b = semaphore.tryAcquire();
//                    //尝试获取信号量,如果获取到了信号量,那么返回true,如果没有获取到信号量会等待一定时间,如果等待时间内获取到了信号量,那么返回true,如果等待时间内没有获取到信号量,那么返回false
//                    boolean b = semaphore.tryAcquire(10, TimeUnit.SECONDS);
                    if (b) {
                        System.out.println("线程" + Thread.currentThread().getName() + "获取到了信号量");
                      
                        //释放信号量
//                        semaphore.release();
                    } else {
                        System.out.println("线程" + Thread.currentThread().getName() + "没有获取到信号量");
                    }
                }
            });
            thread.start();
        }

    }
    
    public static void main(String[] args) throws InterruptedException {
        SemaphoreDemo semaphoreDemo=new SemaphoreDemo();
        semaphoreDemo.demo1();
    }
}
