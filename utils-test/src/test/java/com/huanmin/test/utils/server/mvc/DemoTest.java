package com.huanmin.test.utils.server.mvc;

import com.huanmin.test.TestApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huanmin
 * @date 2024/1/3
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class})
@Slf4j
public class DemoTest {

    @Test
    public void test() {
        log.info("test");
    }
}
