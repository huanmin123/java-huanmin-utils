package com.huanmin.utils.common.multithreading.queue.trait;

import com.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import com.huanmin.utils.common.base.UniversalException;
import com.huanmin.utils.common.multithreading.executor.ExecutorUtil;

import java.util.concurrent.SynchronousQueue;
import java.util.function.Consumer;


//专门为生成者和消费者提供的队列,  生产一个必须被消费了才能继续生产,  也就是始终只能一个值在队列中,
// 如果对速度没太大要求的话我们可以用来在线程间安全的交换单一元素,这样好处就是降低吞吐量,对系统cpu等性能影响较低,
// 如果需要快速处理的话选择可以使用LinkedTransferQueueUtils,来加快处理
public class SynchronousQueueUtil<T> {
    private SynchronousQueue<T>  synchronousQueue;
    public SynchronousQueueUtil() {
        this.synchronousQueue= new SynchronousQueue<T>();
    }

    //启动一个生产者
    public synchronized void  startProducer(T data) {
       handle(data);
    }

    public synchronized void  startConsumer(Consumer<T> consumer) {
        handle(consumer);
    }

    //立即将元素推送给消费者,如果没有消费者,那么返回false,将内容扔掉不会放入队列中
    //我这里进行了二次加工,开启多线程一直推送消息直到成功为止
    public void handle(T data) {
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.SynchronousQueueUtil,()->{
            try {
                synchronousQueue.put(data);
            } catch (InterruptedException e) {
                UniversalException.logError(e);
            }
        });
    }

    //从队列中取出一个值,如果没有值那么一直阻塞到有为止,然后进行处理
    public void handle(Consumer<T> consumer) {
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.SynchronousQueueUtil,()->{
            while (true) {
                try {
                    consumer.accept(synchronousQueue.take());
                } catch (InterruptedException e) {
                    UniversalException.logError(e);
                }
            }
        });
    }



}
