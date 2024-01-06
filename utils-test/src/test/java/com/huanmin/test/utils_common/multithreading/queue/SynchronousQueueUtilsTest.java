package com.huanmin.test.utils_common.multithreading.queue;

import com.utils.common.multithreading.queue.trait.SynchronousQueueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronousQueueUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(SynchronousQueueUtilsTest.class);
    public static void main(String[] args) {

        SynchronousQueueUtil<String> build = new SynchronousQueueUtil<>();

        build.startConsumer((data)->{
            System.out.println("1消费者"+data);
        });

        build.startProducer("hell");
        build.startProducer("hell1");
        build.startProducer("hell2");
        build.startProducer("hell3");


        SynchronousQueueUtil<String> build1 = new SynchronousQueueUtil<>();

        build1.startConsumer((data)->{
            System.out.println("2消费者"+data);
        });

        build1.startProducer("1hell");
        build1.startProducer("1hell1");
        build1.startProducer("1hell2");
        build1.startProducer("1hell3");


    }
}
