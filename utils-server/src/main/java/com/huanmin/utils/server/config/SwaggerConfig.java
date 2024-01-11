package com.huanmin.utils.server.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author HuAnmin
 * @version 1.0
 * @email 3426154361@qq.com
 * @date 2021/3/23-15:39
 * @description 类描述....
 */
@Configuration
@EnableSwagger2     //开启Swagger2 -ui
public class SwaggerConfig {
    @Bean  //Swagger的使用主要是要将docket对象传入IOC容器
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) //关于文档的各种信息
                .enable(true) //使Swagger生效 项目上线的时候这个需要设置为false
                .groupName("java-huanmin-utils") //设置分组
                .select()
                //选择扫描的接口  controller
//                .apis(RequestHandlerSelectors.basePackage("com.booksadmin.controller"))//指定扫描的接口
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) //包含注解的方式来确定要显示的接口(推荐)
                .build();
    }
    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("SpringBoot 使用 Swagger2")
                //创建人     个人信息地址    邮箱
                .contact(new Contact("HuAnmin", "xxxxxxxxxxxx", "3426154361@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("这是一个 工具项目 主要提供各种服务和工具的.......")
                .build();
    }

}