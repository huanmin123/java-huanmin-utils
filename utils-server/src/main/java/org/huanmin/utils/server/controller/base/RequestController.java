package org.huanmin.utils.server.controller.base;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@RequestMapping("/request")
public class RequestController {

    //     用于 HttpUtilsTest 类里的 代码进行测试
    @GetMapping
    public String  Get(HttpServletRequest request){
        Enumeration<String> parameterNames = request.getParameterNames();
        String json="{";
        while (parameterNames.hasMoreElements()){
            String s = parameterNames.nextElement();
            String parameter = request.getParameter(s);
            System.out.println("key"+s+"____"+"value"+parameter);
            json+=s+":"+parameter+",";
        }
        json= json.substring(0,json.length()-2)+"}";
        return  json;
    }

    @PostMapping
    public String Post(@RequestBody String body){
        System.out.println("请求体"+body);
        return body;
    }
    @PutMapping
    public String Put(@RequestBody String body){
        System.out.println("请求体"+body);
        return body;
    }
    @DeleteMapping
    public String Delete(@RequestBody String body){
        System.out.println("请求体"+body);
        return body;
    }

}
