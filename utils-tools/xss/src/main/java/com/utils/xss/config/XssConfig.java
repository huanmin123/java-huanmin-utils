package com.utils.xss.config;

import com.utils.xss.XssAop;
import com.utils.xss.XssFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({XssFilter.class,XssAop.class})
public class XssConfig {
}
