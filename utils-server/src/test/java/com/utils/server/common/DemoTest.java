package com.utils.server.common;

import org.junit.Test;

/**
 * @author huanmin
 * @date 2024/1/3
 */
public class DemoTest {
    @Test
    public void demo1() {
        int a = 1;
        Object obj = a;
        if (obj instanceof Number) {
            System.out.println("true");

        }
    }


}
