package org.huanmin.redis.jedis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/8/4 11:54
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */

@Configuration
public class JedisConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(JedisConfig.class);
    
    @Value("${spring.redis.host}")
    private String host;
    
    @Value("${spring.redis.port}")
    private int port;
    
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;
    
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;
    
    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        JedisPool jedisPool = null;
        if (password == null || password.equals("")) {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        }
        System.out.println("JedisPool连接成功:" + host + "\t" + port);
        
        return jedisPool;
    }
}

