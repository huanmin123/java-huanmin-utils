package com.utils.server.config;


import com.utils.common.base.UniversalException;
import com.utils.common.spring.HttpJsonResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 对全局 Controlle 异常进行处理
@ControllerAdvice
public class BaseExceptionHandlerConfig  {

    /**
     * 读取http请求消息失败
     * @param e
     * @return
     */
    @ExceptionHandler({UniversalException.class})
    public HttpJsonResponse<Void> httpMessageNotReadable(UniversalException e) {
        return HttpJsonResponse.failed(e.getMessage());
    }


    /**
     * 只要 controller层报错都会被捕捉到 并执行下面代码
     * Exception 异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public HttpJsonResponse<Void> exception(Exception e) {
        return HttpJsonResponse.failed(e.getMessage());
    }


    /**
     * 只要 controller层报错都会被捕捉到 并执行下面代码
     * RuntimeException 异常处理
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public HttpJsonResponse<Void> runtimeException(Exception e) {
        return HttpJsonResponse.failed(e.getMessage());
    }
}
