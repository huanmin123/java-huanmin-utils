package com.utils.server;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 */
@Slf4j
@EnableSwagger2
@SpringBootApplication
@MapperScan({"com.utils.**.mapper"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WebApplication.class);
        Environment env = app.run(args).getEnvironment();
        app.addListeners();
        log.info("\n\t----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access Urls:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "Swagger API:http://localhost:{}/swagger-ui.html\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                env.getProperty("server.port"));

    }


}
