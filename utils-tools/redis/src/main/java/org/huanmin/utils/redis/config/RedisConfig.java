package org.huanmin.utils.redis.config;

import org.huanmin.utils.redis.redisTemplate.utils.RedisTemplateUtil;
import org.huanmin.utils.redis.jedis.utils.JedisUtil;
import org.huanmin.utils.redis.redisson.aspect.RedissonAspect;
import org.huanmin.utils.redis.redisson.utils.RedissonLockUtil;
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
