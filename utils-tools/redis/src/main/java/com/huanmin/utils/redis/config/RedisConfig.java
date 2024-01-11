package com.huanmin.utils.redis.config;

import com.huanmin.utils.redis.redisTemplate.utils.RedisTemplateUtil;
import com.huanmin.utils.redis.jedis.utils.JedisUtil;
import com.huanmin.utils.redis.redisson.aspect.RedissonAspect;
import com.huanmin.utils.redis.redisson.utils.RedissonLockUtil;
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
