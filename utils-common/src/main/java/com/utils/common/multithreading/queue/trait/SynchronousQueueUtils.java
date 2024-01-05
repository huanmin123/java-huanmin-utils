package com.utils.common.multithreading.queue.trait;

import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.executor.ThreadFactoryUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.function.Consumer;


//专门为生成者和消费者提供的队列,  生产一个必须被消费了才能继续生产,  也就是始终只能一个值在队列中,
// 如果对速度没太大要求的话我们可以用来在线程间安全的交换单一元素,这样好处就是降低吞吐量,对系统cpu等性能影响较低,
// 如果需要快速处理的话选择可以使用LinkedTransferQueueUtils,来加快处理
public class SynchronousQueueUtils {

    private  static Map<String , SynchronousQueue> map=new ConcurrentHashMap<>();
    private SynchronousQueue  synchronousQueue;
    private String key;
    private SynchronousQueueUtils(String key) {
        this.key=key;
        this.synchronousQueue=initialize(key);
    }


    private  static SynchronousQueue initialize(String key) {
        SynchronousQueue synchronousQueue = map.get(key);
        if (synchronousQueue==null) {
            SynchronousQueue synchronousQueue1 = new SynchronousQueue();
            map.put(key,synchronousQueue1);
            synchronousQueue=synchronousQueue1;
        }
        return  synchronousQueue;
    }


    public static SynchronousQueueUtils build(String key) {
        return new SynchronousQueueUtils(key);
    }


    //启动一个生产者
    public synchronized void  startProducer(Object data) {
        new ProducerQueue().handle(data);
    }

    public synchronized void  startConsumer(Consumer consumer) {
        new ConsumerQueue().handle(consumer);
    }

    private class ProducerQueue {
        //立即将元素推送给消费者,如果没有消费者,那么返回false,将内容扔掉不会放入队列中
        //我这里进行了二次加工,开启多线程一直推送消息直到成功为止
        public void handle(Object data) {
            ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.SynchronousQueueUtils,()->{
                try {
                    synchronousQueue.put(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private class ConsumerQueue {
        //从队列中取出一个值,如果没有值那么一直阻塞到有为止,然后进行处理
        public void handle(Consumer consumer) {
            ExecutorUtil.newThread(()->{
                ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.SynchronousQueueUtils,()->{
                    while (true) {
                        try {
                            consumer.accept(synchronousQueue.take());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            });
        }
    }



}
