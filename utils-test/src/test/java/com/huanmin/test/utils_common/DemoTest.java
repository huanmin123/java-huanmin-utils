package com.huanmin.test.utils_common;

import org.junit.Test;

/**
 * @author huanmin
 * @date 2024/1/3
 */
public class DemoTest {
    @Test
    public void demo1() {
        String replace = "%sss%".replace("%", "%%");
        System.out.println(replace);
    }


}
