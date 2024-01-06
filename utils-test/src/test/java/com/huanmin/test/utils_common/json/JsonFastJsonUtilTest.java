package com.huanmin.test.utils_common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entity.UserData;
import com.path.ResourceFileUtil;
import com.utils.common.json.JsonFastJsonUtil;
import com.utils.common.json.JsonJacksonUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JsonFastJsonUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonFastJsonUtilTest.class);
    @Test
    public void test1(){
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
        List<UserData> UserDatas = JsonFastJsonUtil.toList(str, UserData.class);

        System.out.println("test1"+UserDatas);



    }


    @Test
    public void test2(){


        String str="{ \"name\":\"abc\",\"list\":[{\"name\":\"abc\",\"age\":\"123\"}, {\"name\":\"abc1\",\"age\":\"1231\"}] }";
        Map<String, Object> map = JsonFastJsonUtil.toMap(str);
        System.out.println("test2"+map.get("list"));

    }
    @Test
    public void test3(){
        String str="[" +
                "        {" +
                "            \"name\": \"abc\"," +
                "            \"age\": \"123\"" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"abc1\"," +
                "            \"age\": \"1231\"" +
                "        }" +
                "    ]";

        String str1="{ \"name\":\"abc\",\"list\":[{\"name\":\"abc\",\"age\":\"123\"}, {\"name\":\"abc1\",\"age\":\"1231\"}] }";
        JSONArray jsonArray = JSON.parseArray(str);
        String pretty = JsonFastJsonUtil.pretty(jsonArray);
        System.out.println(pretty);

        JSONObject jsonObject = JSON.parseObject(str1);
        String pretty1 = JsonFastJsonUtil.pretty(jsonObject);
        System.out.println(pretty1);
    }
    @Test
    public void test4(){

        String absoluteFilePathAndCreate = ResourceFileUtil.getAbsoluteFileOrDirPathAndCreate("JSON.txt");
        System.out.println(absoluteFilePathAndCreate);


        String str="[" +
                "        {" +
                "            \"name\": \"abc\"," +
                "            \"age\": \"123\"" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"abc1\"," +
                "            \"age\": \"1231\"" +
                "        }" +
                "    ]";

        JsonJacksonUtil.writeFileJson(new File(absoluteFilePathAndCreate),str);

    }


    @Test
    public void test5(){

        String absoluteFilePathAndCreate = ResourceFileUtil.getAbsoluteFileOrDirPathAndCreate("JSON.txt");
        String s = JsonJacksonUtil.readFileJson(new File(absoluteFilePathAndCreate));

        System.out.println(s );

    }
}
