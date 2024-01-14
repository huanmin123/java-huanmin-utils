package org.huanmin.test.utils.utils_common.multithreading.queue;

import org.huanmin.utils.common.multithreading.queue.linked.LinkedTransferQueueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedTransferQueueUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(LinkedTransferQueueUtilsTest.class);
    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueueUtil<String> build = new  LinkedTransferQueueUtil<>();

        //启动消费者
        build.startConsumer((data)->{
            System.out.println("消费:"+ data);

        });

        //启动生产者
        build.startProducer("hahha");
        build.startProducer("hahha2");
        build.startProducer("hahha3");

        Thread.sleep(1000);
        build.startProducer("hahha4");
        Thread.sleep(1000);
        build.startProducer("hahha5");


    }
}
