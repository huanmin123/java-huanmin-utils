package com.utils.common.multithreading.queue.linked;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

//最常用的多线程队列,有界和无界  ,无界就是一直可以添加不会被阻塞,有界就是达到界限就会阻塞,直到不满了才继续执行
public class LinkedBlockingQueueUtils {
    private  static  Map<String , LinkedBlockingQueue> map=new ConcurrentHashMap<>();
    private LinkedBlockingQueue linkedBlockingQueue;
    private String key;



    private LinkedBlockingQueueUtils(String key,int capacity) {
        this.key=key;
        this.linkedBlockingQueue=initialize(key,capacity);
    }

    private  static LinkedBlockingQueue initialize(String key,int capacity) {
        LinkedBlockingQueue linkedBlockingQueue = map.get(key);
        if (linkedBlockingQueue==null) {
            synchronized (LinkedBlockingQueueUtils.class) {
                linkedBlockingQueue = map.get(key);
                if (linkedBlockingQueue == null) {
                    LinkedBlockingQueue linkedBlockingQueue1 = new LinkedBlockingQueue(capacity);
                    map.put(key,linkedBlockingQueue1);
                    linkedBlockingQueue=linkedBlockingQueue1;
                }
            }

        }
        return  linkedBlockingQueue;
    }

    private LinkedBlockingQueueUtils(String key) {
        this.key=key;
        this.linkedBlockingQueue=initialize(key);
    }

    private  static LinkedBlockingQueue initialize(String key) {
        LinkedBlockingQueue linkedBlockingQueue = map.get(key);
        if (linkedBlockingQueue==null) {
            LinkedBlockingQueue linkedBlockingQueue1 = new LinkedBlockingQueue();
            map.put(key,linkedBlockingQueue1);
            linkedBlockingQueue=linkedBlockingQueue1;
        }
        return  linkedBlockingQueue;
    }


    //有界创建
    public static LinkedBlockingQueueUtils build(String key,int capacity) {
        return new LinkedBlockingQueueUtils(key);
    }
    //无界创建
    public static LinkedBlockingQueueUtils build(String key) {
        return new LinkedBlockingQueueUtils(key);
    }

    //如果队列满了，一直阻塞，直到队列不满了或者线程被中断-->阻塞
    public  void add(Object data) {
        try {
            linkedBlockingQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 在队尾插入一个元素,，如果队列已满，则进入等待 ,直到出现以下三种情况：阻塞被唤醒,等待时间超时,当前线程被中断
     *
     * @param data
     * @param time 等待时间
     */
    public  void add(Object data,int time) {
        try {
            linkedBlockingQueue.offer(data,time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    //如果队列为空返回null 否则一直获取到为止

    /**
     *  如果队列不空，出队；如果队列已空且已经超时，返回null；如果队列已空且时间未超时，则进入等待，直到出现以下三种情况：阻塞被唤醒,等待时间超时,当前线程被中断
     * @param time
     * @param clazz
     * @param <T>
     * @return
     */
    public  <T> T getData(int time,Class<T> clazz) {
        T poll=null;
        try {
         poll = (T) linkedBlockingQueue.poll(time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return poll;
    }

    public  <T> T getData(Class<T> clazz) {
        return (T) linkedBlockingQueue.poll();
    }

    public boolean isEmpty() {
        return linkedBlockingQueue.isEmpty();
    }

    /**
     * 获取队列中值,如果获取不到那么则进入等待，直到出现以下三种情况：阻塞被唤醒,等待时间超时,当前线程被中断
     * @param function 处理函数
     * @param rClass //返回值类型
     * @param <R>
     * @return  返回处理结果
     */
    public  <R> R getDataHandle(Function function,Class<R> rClass) {
        Object apply=null;
        try {
            apply = function.apply(linkedBlockingQueue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  (R)apply;
    }

    public <V> void iteration(Function function,Class<V> clazz) {
        while (!linkedBlockingQueue.isEmpty()) {
            if ( (boolean) function.apply((V)linkedBlockingQueue.poll())) {
                return;
            }
        }

    }

}
