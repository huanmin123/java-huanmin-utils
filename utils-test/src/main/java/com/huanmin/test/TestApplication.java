package com.huanmin.test;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
// DataSourceAutoConfiguration取消自动装配数据源, 我们自己配置数据源
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan({"com.utils.**.mapper"})
public class TestApplication {
    public static void main(String[] args) {
        log.info("TestApplication start...");
        try {
            SpringApplication.run(TestApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
