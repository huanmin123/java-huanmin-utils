package com.huanmin.test.server.common.obj.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestServiceImpl implements TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    @Override
    public String getList(String code, String name) {
        System.out.println("code :"+code+"name: "+name);
        return "111";
    }
}
