package com.huanmin.test.utils_common.json;


import com.huanmin.test.entity.UserEntity;
import com.utils.common.json.JsonJacksonUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class JacksonUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtilTest.class);
    @Test
    public void show(){
        String str="[\n" +
                "        {\n" +
                "            \"name\": \"abc\",\n" +
                "            \"age\": \"123\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"abc1\",\n" +
                "            \"age\": \"1231\"\n" +
                "        }\n" +
                "    ]";
        System.out.println(str);
        List<UserEntity> objects1 = JsonJacksonUtil.jsonToList(str, UserEntity.class);


        System.out.println("show"+objects1);

    }

    @Test
    public void test2(){


        String str="{ \"name\":\"abc\",\"list\":[{\"name\":\"abc\",\"age\":\"123\"}, {\"name\":\"abc1\",\"age\":\"1231\"}] }";
        Map<String, Object> map = JsonJacksonUtil.jsonToMap(str);
        System.out.println("test2"+map.get("list"));

    }



}
