package com.utils.common.multithreading.queue.priority;

public class MessagePriority<T>  implements Comparable<MessagePriority>{

    private   int type;  //1. 普通用户 ,2.vip用户(可以插队到前面)

    private   T body;

    public T getBody() {
        return body;
    }

    public int getType() {
        return type;
    }
    public MessagePriority( T body, PriorityEnum  type) {
        this.body = body;
        this.type=type.getType();
    }

    @Override
    public int compareTo(MessagePriority o) {
        // 1(大于)  0 (等于) -1 (小于,队列开头位置)
        return Integer.compare(o.getType(), type);
    }

    @Override
    public String toString() {
        return "MessagePriority{" +
                "type=" + type +
                ", body=" + body +
                '}';
    }
}
