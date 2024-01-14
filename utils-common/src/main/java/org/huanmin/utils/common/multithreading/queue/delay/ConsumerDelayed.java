package org.huanmin.utils.common.multithreading.queue.delay;

import org.huanmin.utils.common.base.UniversalException;

import java.util.concurrent.DelayQueue;
import java.util.function.Consumer;

public class ConsumerDelayed<T> implements Runnable {
    // 延时队列 ,消费者从其中获取消息进行消费
    private DelayQueue<MessageDelayed> queue;
    private Consumer<MessageDelayed<T>> consumer;

    public ConsumerDelayed(DelayQueue<MessageDelayed> queue,Consumer<MessageDelayed<T>> consumer) {
        this.queue = queue;
        this.consumer=consumer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (queue.size()>0) {
                    //取出到期的元素,并且从队列中删除
                    MessageDelayed take = queue.take();
                    consumer.accept(take);
                }
            } catch (InterruptedException e) {
                 UniversalException.logError(e);
            }
        }
    }
}
