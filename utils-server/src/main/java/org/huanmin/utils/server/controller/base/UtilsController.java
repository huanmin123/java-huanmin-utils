package org.huanmin.utils.server.controller.base;

import org.huanmin.utils.common.spring.IPUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/utils")
public class UtilsController {
    //http://172.29.204.221:7001/ip/test
    /**
     *  获取用户ip
     */
    @GetMapping(value = "/ip")
    public String ip(HttpServletRequest request) throws Exception {
        return  IPUtil.getIpAddr(request);
    }

}
