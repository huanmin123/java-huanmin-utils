package com.huanmin.utils.xss.config;

import com.huanmin.utils.xss.XssAop;
import com.huanmin.utils.xss.XssFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({XssFilter.class,XssAop.class})
public class XssConfig {
}
