package com.utils.common.multithreading.queue.priority;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Function;

public class PriorityBlockingQueueUtils {
    private static Map<String, PriorityBlockingQueue<MessagePriority>> map = new ConcurrentHashMap<>();
    private PriorityBlockingQueue<MessagePriority> priorityBlockingQueue;
    private String key;

    private PriorityBlockingQueueUtils(String key) {
        this.key = key;
        this.priorityBlockingQueue = initialize(key);
    }

    private static PriorityBlockingQueue<MessagePriority> initialize(String key) {
        PriorityBlockingQueue<MessagePriority> priorityBlockingQueue = map.get(key);
        if (priorityBlockingQueue == null) {
            PriorityBlockingQueue<MessagePriority> priorityBlockingQueue1 = new PriorityBlockingQueue<MessagePriority>();
            map.put(key, priorityBlockingQueue1);
            priorityBlockingQueue = priorityBlockingQueue1;
        }
        return priorityBlockingQueue;
    }

    public static PriorityBlockingQueueUtils build(String key) {
        return new PriorityBlockingQueueUtils(key);
    }


    public void add(Object data, PriorityEnum type) {
        priorityBlockingQueue.offer(new MessagePriority(data, type));
    }

    //如果队列空了，一直阻塞，直到队列不为空或者线程被中断-->阻塞
    public <T> T getDataTake(Class<T> cl) {
        MessagePriority take = null;
        try {
            take = priorityBlockingQueue.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (T) take.getBody();
    }
    //如果没有元素，直接返回null；如果有元素，出队
    public <T> T getDataPoll(Class<T> cl) {
        MessagePriority take = priorityBlockingQueue.poll();;

        return (T) take.getBody();
    }


    public boolean isEmpty() {
        return priorityBlockingQueue.isEmpty();
    }

    public <V> void   iteration(Function function,Class<V> cl) {
        while (!priorityBlockingQueue.isEmpty()) {
            if ( (boolean) function.apply((V)priorityBlockingQueue.poll().getBody())) {
                return;
            }
        }

    }

}
