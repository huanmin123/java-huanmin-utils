package org.huanmin.config;

import org.huanmin.mybaitis.MybaitisTemplateUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MybaitisTemplateUtil.class})
public class JdbcExtendConfig {
}
