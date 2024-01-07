package com.utils.common.spring;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dn
 * @since 2023/12/15
 */
@Data
@NoArgsConstructor
public class HttpJsonResponse<T> implements Serializable {

    protected static SerializeConfig config = new SerializeConfig();
    private static final long serialVersionUID = -1133943227545232191L;
    private static final Object EMPTY = null;
    public static final Integer SUCCESS_DEFAULT_CODE = 0;
    public static final Integer FAILED_DEFAULT_CODE = -1;
    private T data;
    private Integer code;
    private String message;
    private String traceId;

    static {
        //序列化的时候,将返回值转换为a_b_c的形式
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

    public static <T> HttpJsonResponse<T> success() {
        HttpJsonResponse<T> httpJsonResult = new HttpJsonResponse<>();
        httpJsonResult.setCode(SUCCESS_DEFAULT_CODE);
        httpJsonResult.setMessage("成功");
        httpJsonResult.setData(null);
        return httpJsonResult;
    }

    public static <T> HttpJsonResponse<T> success(T t) {
        HttpJsonResponse<T> httpJsonResult = new HttpJsonResponse<>();
        httpJsonResult.setCode(SUCCESS_DEFAULT_CODE);
        httpJsonResult.setMessage("成功");
        httpJsonResult.setData(t);
        return httpJsonResult;
    }

    public static <T> HttpJsonResponse<T> failed(Integer code, String message) {
        HttpJsonResponse<T> httpJsonResult = new HttpJsonResponse<>();
        httpJsonResult.setCode(code);
        httpJsonResult.setMessage(message);
        return httpJsonResult;
    }

    public static <T> HttpJsonResponse<T> failed(String message) {
        return failed(FAILED_DEFAULT_CODE, message);
    }
    public static <T> HttpJsonResponse<T> failed() {
        return failed(FAILED_DEFAULT_CODE, "失败");
    }

    @Deprecated
    public <R> HttpJsonResponse<R> add(R bean) {
       HttpJsonResponse<R> response = new HttpJsonResponse<>();
       response.setCode(this.getCode());
       response.setMessage(this.getMessage());
       response.setData(bean);
       return response;
    }

}
