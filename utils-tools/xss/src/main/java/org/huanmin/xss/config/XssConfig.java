package org.huanmin.xss.config;

import org.huanmin.xss.XssAop;
import org.huanmin.xss.XssFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({XssFilter.class,XssAop.class})
public class XssConfig {
}
