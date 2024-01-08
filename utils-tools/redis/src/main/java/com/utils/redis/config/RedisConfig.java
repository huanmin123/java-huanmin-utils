package com.utils.redis.config;

import com.utils.redis.jedis.utils.JedisUtil;
import com.utils.redis.redisTemplate.utils.RedisTemplateUtil;
import com.utils.redis.redisson.aspect.RedissonAspect;
import com.utils.redis.redisson.utils.RedissonLockUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        JedisUtil.class,
        RedissonAspect.class,
        RedisTemplateUtil.class,
        RedissonLockUtil.class,
})
public class RedisConfig {
}
