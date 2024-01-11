package com.huanmin.utils.server.controller.base;

import com.huanmin.utils.common.media.img.ImageVerificationCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/verificationCode")
public class VerificationCodeController {


    //http://localhost:7001/verificationCode/img
    /**
     *  获取用户ip
     */
    @GetMapping(value = "/img")
    public void verificationCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
        ImageVerificationCode.ini(request,response);  //生成验证码图片

    }



}
