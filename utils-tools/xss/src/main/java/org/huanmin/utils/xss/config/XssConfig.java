package org.huanmin.utils.xss.config;

import org.huanmin.utils.xss.XssAop;
import org.huanmin.utils.xss.XssFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({XssFilter.class,XssAop.class})
public class XssConfig {
}
