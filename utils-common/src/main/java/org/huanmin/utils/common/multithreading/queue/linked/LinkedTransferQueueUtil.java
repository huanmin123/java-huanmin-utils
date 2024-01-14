package org.huanmin.utils.common.multithreading.queue.linked;


import org.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.huanmin.utils.common.base.UniversalException;
import org.huanmin.utils.common.multithreading.executor.ExecutorUtil;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * LinkedBlockingQueue和SynchronousQueue 相结合的队列
 */
public class LinkedTransferQueueUtil<T> {
    private final LinkedTransferQueue<T>  linkedTransferQueue;
    public LinkedTransferQueueUtil() {
        this.linkedTransferQueue=new LinkedTransferQueue<T>();
    }

    //启动一个生产者
    public  void  startProducer(T data) {
       handle(data);
    }
    //启动一个消费者
    public synchronized void  startConsumer(Consumer<T> consumer) {
        handle(consumer);
    }

    //立即将元素推送给消费者,如果没有消费者,那么返回false,将内容扔掉不会放入队列中
    //我这里进行了二次加工,开启多线程一直推送消息直到成功为止
    public void handle(T data) {
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.LinkedTransferQueueUtils,()->{
            while (!this.linkedTransferQueue.tryTransfer(data)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    UniversalException.logError(e);
                }
            }
        });


    }

    //从队列中取出一个值,如果没有值那么一直阻塞到有为止,然后进行处理
    public void handle(Consumer<T> consumer) {
        ExecutorUtil.newThread(()->{
            while (true) {
                try {
                    consumer.accept(linkedTransferQueue.take());
                } catch (InterruptedException e) {
                    UniversalException.logError(e);
                }
            }
        });
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
            UniversalException.logError(e);
        }
    }



    //从队列中取出一个值,如果队列为空那么返回null ,如果队列存在值那么一直获取到值为止
    public  T  poll(Class<T> cl) {
        return  linkedTransferQueue.poll();
    }
    //从队列中取出一个值,如果队列为空那么返回null ,如果队列存在值那么一直获取到值为止,或者超时
    public  T  poll(Class<T> cl,int time) {
        try {
            return  linkedTransferQueue.poll(time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            UniversalException.logError(e);
        }
        return null;
    }
}
