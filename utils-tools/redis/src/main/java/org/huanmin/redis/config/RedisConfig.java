package org.huanmin.redis.config;

import org.huanmin.redis.jedis.utils.JedisUtil;
import org.huanmin.redis.redisTemplate.config.RedisTemplateConfig;
import org.huanmin.redis.redisTemplate.utils.RedisTemplateUtil;
import org.huanmin.redis.redisson.aspect.RedissonAspect;
import org.huanmin.redis.redisson.utils.RedissonLockUtil;
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
