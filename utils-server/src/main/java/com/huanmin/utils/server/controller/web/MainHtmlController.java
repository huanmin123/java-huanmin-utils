package com.huanmin.utils.server.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainHtmlController {

    //http://localhost:12345
    @RequestMapping("")
    public String index() {
        return "index";
    }


}
