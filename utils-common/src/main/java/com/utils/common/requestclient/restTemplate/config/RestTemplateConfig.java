package com.utils.common.requestclient.restTemplate.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.*;

import java.io.IOException;

@Configuration
public class RestTemplateConfig {
//    @LoadBalanced  // 开启负载均衡   如果不需要那么就注释掉
//    @Bean
//    public  RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
    @LoadBalanced  // 开启负载均衡   如果不需要那么就注释掉  ,开启负载后必须使用服务名
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        MyResponseErrorHandler errorHandler = new MyResponseErrorHandler();
        restTemplate.setErrorHandler(errorHandler);
//        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        // 通过下面代码可以添加新的 HttpMessageConverter
        //messageConverters.add(new );
        return restTemplate;
    }
//    创建 RestTemplate 时需要一个 ClientHttpRequestFactory，
//    通过这个请求工厂，我们可以统一设置请求的超时时间，设置代理以及一些其他细节。
//    通过下面代码配置后，我们直接在代码中注入 RestTemplate 就可以使用了。
//    有时候我们还需要通过 ClientHttpRequestFactory 配置最大链接数，忽略SSL证书等，大家需要的时候可以自己查看代码设置。
    @Bean //   我们可以统一设置请求的超时时间
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000); //读取超时时间
        factory.setConnectTimeout(15000); //连接超时时间
        // 设置代理
//        factory.setProxy(null);
        return factory;
    }




    class MyResponseErrorHandler extends DefaultResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return super.hasError(response);
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
            if (statusCode == null) {
                throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(),
                        response.getHeaders(), getResponseBody(response), getCharset(response));
            }
            handleError(response, statusCode);
        }
        @Override
        protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
            switch (statusCode.series()) {
                case CLIENT_ERROR:
                    HttpClientErrorException exp1 = new HttpClientErrorException(statusCode, response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
                    System.err.println("客户端调用异常"+exp1);
                    throw  exp1;
                case SERVER_ERROR:
                    HttpServerErrorException exp2 = new HttpServerErrorException(statusCode, response.getStatusText(),
                            response.getHeaders(), getResponseBody(response), getCharset(response));
                    System.err.println("服务端调用异常"+exp2);
                    throw exp2;
                default:
                    UnknownHttpStatusCodeException exp3 = new UnknownHttpStatusCodeException(statusCode.value(), response.getStatusText(),
                            response.getHeaders(), getResponseBody(response), getCharset(response));
                    System.err.println("网络调用未知异常");
                    throw exp3;
            }
        }

    }

}
