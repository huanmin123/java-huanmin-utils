package com.utils.common.multithreading.queue.linked;

import com.utils.common.base.UniversalException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

//最常用的多线程队列,有界和无界  ,无界就是一直可以添加不会被阻塞,有界就是达到界限就会阻塞,直到不满了才继续执行
public class LinkedBlockingQueueUtil<T> {
    private LinkedBlockingQueue<T> linkedBlockingQueue;

    private LinkedBlockingQueueUtil(int capacity) {
        this.linkedBlockingQueue = new LinkedBlockingQueue(capacity);
    }

    private LinkedBlockingQueueUtil() {
        this.linkedBlockingQueue = new LinkedBlockingQueue();
    }

    //有界创建
    public static <T> LinkedBlockingQueueUtil build(int capacity) {
        return new LinkedBlockingQueueUtil<T>(capacity);
    }

    //无界创建
    public static <T> LinkedBlockingQueueUtil build(String key) {
        return new LinkedBlockingQueueUtil<T>();
    }

    //如果队列满了，一直阻塞，直到队列不满了或者线程被中断-->阻塞
    public void add(T data) {
        try {
            linkedBlockingQueue.put(data);
        } catch (InterruptedException e) {
            UniversalException.logError(e);
        }

    }

    /**
     * 在队尾插入一个元素,，如果队列已满，则进入等待 ,直到出现以下三种情况：阻塞被唤醒,等待时间超时,当前线程被中断
     *
     * @param data
     * @param time 等待时间
     */
    public void add(T data, int time) {
        try {
            linkedBlockingQueue.offer(data, time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            UniversalException.logError(e);
        }
    }
    //如果队列为空返回null 否则一直获取到为止

    /**
     * 如果队列不空，出队；如果队列已空且已经超时，返回null；如果队列已空且时间未超时，则进入等待，直到出现以下三种情况：阻塞被唤醒,等待时间超时,当前线程被中断
     *
     * @param time
     * @param clazz
     * @param <T>
     * @return
     */
    public T getData(int time) {
        try {
            return linkedBlockingQueue.poll(time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            UniversalException.logError(e);
        }
        return null;
    }

    //检索并删除此队列的头，如果此队列为空，则返回null。
    public T getData() {
        try {
            return linkedBlockingQueue.poll();
        } catch (Exception e) {
            UniversalException.logError(e);
        }
        return null;
    }

    public boolean isEmpty() {
        return linkedBlockingQueue.isEmpty();
    }

    //一直等待获取到为止
    public T take() throws InterruptedException {
        return linkedBlockingQueue.take();
    }


}
