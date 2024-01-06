package com.huanmin.test.utils_common.multithreading.queue;


import com.utils.common.multithreading.queue.delay.DelayQueueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayQueueTest {
    private static final Logger logger = LoggerFactory.getLogger(DelayQueueTest.class);
    public static void main(String[] args) {
        // 创建延时队列
        DelayQueueUtils build = DelayQueueUtils.buildAndCStartConsumer("da1", (messagedelayed) -> {
            String body = (String) messagedelayed.getBody();
            System.out.println(" 消息体：" + body);
        });
        build.add("hello",3000); //3秒后到期,被消费
        build.add("world",10000); //10秒后到期,被消费
        build.add("huhuuh",15000); //15秒后到期,被消费

        DelayQueueUtils.build("da1").add("hahhah",18000);//18秒后到期,被消费



    }
}
