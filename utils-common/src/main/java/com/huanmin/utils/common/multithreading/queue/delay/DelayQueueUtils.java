package com.huanmin.utils.common.multithreading.queue.delay;



import com.huanmin.utils.common.multithreading.executor.ExecutorUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
//延时队列
public class DelayQueueUtils {

    private  static Map<String, DelayQueue<MessageDelayed>> map=new ConcurrentHashMap<>();
    private  static AtomicLong  atomicLong=new AtomicLong();//自增
    private  static int start=0;
    private  String key;
    private  DelayQueue<MessageDelayed> delayeds;
    private   DelayQueueUtils(String key) {
        this.key=key;
        this.delayeds=initialize(key);
    }
    //创建对象的同时启动消费者 (整个声明周期只允许启动一次消费者)
    public static  synchronized DelayQueueUtils   buildAndCStartConsumer(String key, Consumer<MessageDelayed> consumer) {
        //启动消费者 ,只允许启动一次
        // 启动消费线程 消费添加到延时队列中的消息，前提是任务到了延期时间
        if (start==0) {
            ExecutorUtil.newThread(()->{
                new ConsumerDelayed(initialize(key),consumer);
            });
            start=1;
        }
        return  new DelayQueueUtils (key);
    }
    public static   DelayQueueUtils   build(String key) {
        return  new DelayQueueUtils (key);
    }

    private  static DelayQueue<MessageDelayed> initialize(String key) {
        DelayQueue<MessageDelayed> messageDelayeds = map.get(key);
        if (messageDelayeds==null) {
            synchronized (DelayQueueUtils.class) {
                messageDelayeds = map.get(key);
                if (messageDelayeds == null) {
                    DelayQueue<MessageDelayed> messageDelayeds1 = new DelayQueue<MessageDelayed>();
                    map.put(key,messageDelayeds1);
                    messageDelayeds=messageDelayeds1;
                }
            }
        }
        return  messageDelayeds;
    }


    /**
     *
     * @param data 数据
     * @param date 到期时间  (毫秒)  以当前时间向后算
     */
    public void add(Object data, long date) {
        delayeds.offer(new MessageDelayed(atomicLong.getAndIncrement(),data,date));
    }

}
