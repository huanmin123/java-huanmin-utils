package org.huanmin.redis.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
//设置session的默认在redis中的存活时间
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7200)   //Session过期时间,2小时,默认1800秒(半小时)   -1 永不过期
public class SessionConfig {}