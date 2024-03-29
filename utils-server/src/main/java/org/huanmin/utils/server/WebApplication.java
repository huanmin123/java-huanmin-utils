package org.huanmin.utils.server;


import org.huanmin.utils.common.base.UniversalException;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 */
@Slf4j
@EnableSwagger2
// DataSourceAutoConfiguration取消自动装配数据源, 我们自己配置数据源
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan({"org.huanmin.utils.**.mapper"})
@EnableAspectJAutoProxy(proxyTargetClass = true) //开启AOP的动态代理,这样可以不用实现接口就能使用AOP
public class WebApplication {

    public static void main(String[] args) {
        try {
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
        } catch (Exception e) {
             UniversalException.logError(e);
        }

    }


}
