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
public class LinkedTransferQueueUtils {
    private  static Map<String , LinkedTransferQueue> map=new ConcurrentHashMap<>();
    private LinkedTransferQueue  linkedTransferQueue;
    private String key;
    private LinkedTransferQueueUtils(String key) {
        this.key=key;
        this.linkedTransferQueue=initialize(key);
    }

    private  static LinkedTransferQueue initialize(String key) {
        LinkedTransferQueue linkedTransferQueue = map.get(key);
        if (linkedTransferQueue==null) {
            synchronized (LinkedTransferQueueUtils.class) {
                linkedTransferQueue = map.get(key);
                if (linkedTransferQueue == null) {
                    LinkedTransferQueue linkedTransferQueue1 = new LinkedTransferQueue();
                    map.put(key,linkedTransferQueue1);
                    linkedTransferQueue=linkedTransferQueue1;
                }
            }
        }
        return  linkedTransferQueue;
    }


    public static LinkedTransferQueueUtils build(String key) {
        return new LinkedTransferQueueUtils(key);
    }

    //启动一个生产者
    public synchronized void  startProducer(Object data) {
        new ProducerQueue().handle(data);
    }
    //启动一个消费者
    public synchronized void  startConsumer(Consumer consumer) {
        new ConsumerQueue().handle(consumer);
    }

   private class ProducerQueue {
        //立即将元素推送给消费者,如果没有消费者,那么返回false,将内容扔掉不会放入队列中
       //我这里进行了二次加工,开启多线程一直推送消息直到成功为止
        public void handle(Object data) {
            ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.LinkedTransferQueueUtils,()->{
                while (!linkedTransferQueue.tryTransfer(data)) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

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
    public void put(Object data) {
        linkedTransferQueue.put(data);
    }

    public boolean isEmpty() {
        return linkedTransferQueue.isEmpty();
    }

    //立即将元素推送给消费者,如果没有消费者,那么将元素插入到队列尾部等待被消费
    public  void  transfer(Object data) {
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
