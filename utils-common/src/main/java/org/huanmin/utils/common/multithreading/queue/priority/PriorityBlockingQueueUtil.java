package org.huanmin.utils.common.multithreading.queue.priority;

import org.huanmin.utils.common.base.UniversalException;

import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueUtil<T> {
    private PriorityBlockingQueue<MessagePriority<T>> priorityBlockingQueue;

    public PriorityBlockingQueueUtil() {
        this.priorityBlockingQueue = new PriorityBlockingQueue<>();
    }

    public void add(T data, PriorityEnum type) {
        priorityBlockingQueue.offer(new MessagePriority<T>(data, type));
    }

    //如果队列空了，一直阻塞，直到队列不为空或者线程被中断-->阻塞
    public  T getDataTake() {
        MessagePriority<T> take = null;
        try {
            take = priorityBlockingQueue.take();

        } catch (InterruptedException e) {
             UniversalException.logError(e);
        }
        if (take != null) {
            return take.getBody();
        }
        return null;
    }
    //如果没有元素，直接返回null；如果有元素，出队
    public T getDataPoll(Class<T> cl) {
        MessagePriority<T> take = priorityBlockingQueue.poll();;

        if (take != null) {
            return (T) take.getBody();
        }
        return null;
    }


    public boolean isEmpty() {
        return priorityBlockingQueue.isEmpty();
    }


}
