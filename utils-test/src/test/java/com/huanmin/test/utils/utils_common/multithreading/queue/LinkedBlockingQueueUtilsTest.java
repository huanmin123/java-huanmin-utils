package com.huanmin.test.utils.utils_common.multithreading.queue;

import com.huanmin.utils.common.multithreading.queue.linked.LinkedBlockingQueueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedBlockingQueueUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(LinkedBlockingQueueUtilsTest.class);
    public static void main(String[] args) {
        LinkedBlockingQueueUtil<Integer> build = new LinkedBlockingQueueUtil<>();

        build.add(1);
        build.add(2);
        build.add(3);
        Integer take1 = build.take();
        System.out.println(take1);

    }
}
