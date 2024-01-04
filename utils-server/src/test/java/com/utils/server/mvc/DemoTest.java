package com.utils.server.mvc;

import com.utils.server.WebApplication;
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
@SpringBootTest(classes = {WebApplication.class})
@Slf4j
public class DemoTest {

    @Test
    public void test() {
        log.info("test");
    }
}
