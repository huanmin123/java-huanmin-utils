package com.huanmin.utils.server.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

//文件上传配置
@Configuration
public class MultipartConfig {
    //文件上传大小
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxRequestSize(DataSize.ofGigabytes(2));//设置总上传数据总大小,2G
        factory.setMaxFileSize(DataSize.ofGigabytes(2));//设置单个文件上传大小,2G
        return factory.createMultipartConfig();
    }
}
