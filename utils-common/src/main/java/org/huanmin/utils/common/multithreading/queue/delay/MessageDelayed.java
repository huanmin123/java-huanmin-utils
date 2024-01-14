package org.huanmin.utils.common.multithreading.queue.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 消息体定义 实现Delayed接口就是实现两个方法即compareTo 和 getDelay最重要的就是getDelay方法，这个方法用来判断是否到期……
 *
 * @author whd
 * @date 2017年9月24日 下午8:57:14
 */
public class MessageDelayed<T> implements Delayed {
    private long id;
    private T body; // 消息内容
    private long excuteTime;// 延迟时长，这个是必须的属性因为要按照这个判断延时时长。

    public T getBody() {
        return body;
    }


    public MessageDelayed(long id, T body, long delayTime) {
        this.id = id;
        this.body = body;
        this.excuteTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    }




    // 自定义实现比较方法返回 1 0 -1三个参数
    @Override
    public int compareTo(Delayed delayed) {
        if (delayed==this) {
            return 0;
        }
        if (delayed instanceof MessageDelayed) {

            MessageDelayed msg = (MessageDelayed) delayed;
            long diff = this.excuteTime - msg.excuteTime;
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
                return 1;
            } else {
                //时间相同,那么比较序号
                return Long.compare(this.id, msg.id);
            }
        }

        //一般不会到这里除非不是MessageDelayed对象
        long diff = this.getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS);
        return diff<0?-1:diff>0?1:0;


    }

    // 延迟任务是否到时就是按照这个方法判断如果返回的是负数则说明到期否则还没到期
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.excuteTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public String toString() {
        return "MessageDelayed{" +

                "body=" + body +
                '}';
    }
}