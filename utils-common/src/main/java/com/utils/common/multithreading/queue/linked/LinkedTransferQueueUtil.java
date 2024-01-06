package com.utils.common.multithreading.queue.linked;


import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.executor.ThreadFactoryUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * LinkedBlockingQueue和SynchronousQueue 相结合的队列
 */
public class LinkedTransferQueueUtil<T> {
    private final LinkedTransferQueue<T>  linkedTransferQueue;
    private LinkedTransferQueueUtil() {
        this.linkedTransferQueue=new LinkedTransferQueue();
    }


    public static LinkedTransferQueueUtil build() {
        return new LinkedTransferQueueUtil();
    }

    //启动一个生产者
    public  void  startProducer(T data) {
       handle(data);
    }
    //启动一个消费者
    public synchronized void  startConsumer(Consumer<T> consumer) {
        new ConsumerQueue().handle(consumer);
    }

    //立即将元素推送给消费者,如果没有消费者,那么返回false,将内容扔掉不会放入队列中
    //我这里进行了二次加工,开启多线程一直推送消息直到成功为止
    public void handle(T data) {
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.LinkedTransferQueueUtils,()->{
            while (!this.linkedTransferQueue.tryTransfer(data)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private class ConsumerQueue {
        //从队列中取出一个值,如果没有值那么一直阻塞到有为止,然后进行处理
       public void handle(Consumer consumer) {
           ExecutorUtil.newThread(()->{
               while (true) {
                   try {
                       consumer.accept(linkedTransferQueue.take());
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           });
       }
    }



    //向队列尾部添加一个元素,因为是无界的,所以永远不会被阻塞
    public void put(T data) {
        linkedTransferQueue.put(data);
    }

    public boolean isEmpty() {
        return linkedTransferQueue.isEmpty();
    }

    //立即将元素推送给消费者,如果没有消费者,那么将元素插入到队列尾部等待被消费
    public  void  transfer(T data) {
        try {
            linkedTransferQueue.transfer(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    //从队列中取出一个值,如果队列为空那么返回null ,如果队列存在值那么一直获取到值为止
    public <T> T  poll(Class<T> cl) {
        Object poll=linkedTransferQueue.poll();;
        return (T) poll;
    }
    //从队列中取出一个值,如果队列为空那么返回null ,如果队列存在值那么一直获取到值为止,或者超时
    public <T> T  poll(Class<T> cl,int time) {
        Object poll= null;
        try {
            poll = linkedTransferQueue.poll(time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ;
        return (T) poll;
    }
}
