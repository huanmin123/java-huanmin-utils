package org.huanmin.utils.jdbc_extend.config;

import org.huanmin.utils.jdbc_extend.mybaitis.MybaitisTemplateUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MybaitisTemplateUtil.class})
public class JdbcExtendConfig {
}
