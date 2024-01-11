package com.huanmin.utils.server.interfaces.controller;


import com.huanmin.utils.common.spring.HttpJsonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * TestController
 * </p>
 *
 * @author dn
 * @date 2023/12/19 15:22
 */
@RestController
@Api(tags = "test-controller")
@RequestMapping("test")
@CrossOrigin
public class TestController {
    @GetMapping("")
    @ApiOperation("测试")
    public HttpJsonResponse<Void> test() {
        return HttpJsonResponse.success();
    }
}
