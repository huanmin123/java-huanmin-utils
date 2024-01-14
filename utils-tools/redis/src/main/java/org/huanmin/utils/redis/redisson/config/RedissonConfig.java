package org.huanmin.utils.redis.redisson.config;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/8/1 17:37
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.redisson.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.redisson.pool.min-idle}")
    private int minIdle;
    @Bean
    public RedissonClient getRedisson() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        if(password == null || password.equals("")){
            singleServerConfig.setAddress("redis://" + host + ":" + port);
        }else{
            singleServerConfig.setAddress("redis://" + host + ":" + port).setPassword(password);
        }
        singleServerConfig.setKeepAlive(true);
        singleServerConfig.setPingConnectionInterval(500);//定义每个连接到Redis的PING命令发送间隔。
        singleServerConfig.setTimeout(10000);//Redis服务器响应超时。Redis命令成功发送后开始倒计时。
        singleServerConfig.setConnectTimeout(10000);
        singleServerConfig.setConnectionMinimumIdleSize(minIdle); //最小空闲Redis连接量
        singleServerConfig.setConnectionPoolSize(maxActive); //Redis连接池大小

        return Redisson.create(config);
    }

}
