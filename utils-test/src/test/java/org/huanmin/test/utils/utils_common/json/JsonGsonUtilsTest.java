package org.huanmin.test.utils.utils_common.json;


import org.huanmin.test.entity.UserEntity;
import org.huanmin.utils.common.json.JsonGsonUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonGsonUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonGsonUtilsTest.class);
    //实体类转 json字符串
    @Test
    public void show1(){
        UserEntity ben=new UserEntity();
        ben.setName("abc");
        ben.setAge(22);
        String s = JsonGsonUtils.objTurnJson(ben);
        System.out.println(s);

    }

    // list  转 Json 字符串

    @Test
    public void show2(){
        UserEntity  ben=new UserEntity();
        ben.setName("abc");
        ben.setAge(22);
        UserEntity  ben1=new UserEntity();
        ben.setName("abc1");
        ben.setAge(221);


        List<UserEntity> list=new ArrayList();
        list.add(ben);
        list.add(ben1);
        String s = JsonGsonUtils.objTurnJson(list);
        System.out.println(s);

    }

    // map 转 Json 字符串
    @Test
    public void show3(){

//        Map<String,Object> map=new HashMap<>();
//        map.put("name","abc");
//        map.put("age",123);
//        String s = JsonGsonUtils.objTurnJson(map);
//        System.out.println(s);
        Map<String,String> json=new HashMap<String,String>(){{
            put("sysId","OASYS");
            put("busiId", "OAFile");
            put("roleId", "OASYS0001");
            put("creator", "gsws1"); //创建人
            put("busiNo", "3293"); //业务实例编号
            put("fileSeq","0"); //一直给0就行
            put("fileName","AC通过MQ推人员、机构功能的相关信息.docx"); //文件名称
            put("nodeName", "OA附件");
        }};
        System.out.println(json.toString());
        String s11 = JsonGsonUtils.objTurnJson(json);
        System.out.println(s11);
    }

    //json 字符串转 Map
    @Test
    public void show(){
        String str="{\"name\":\"abc2\",\"age\":\"1222231\"}";
        Map<String,Object> stringObjectMap = JsonGsonUtils.jsonTurnMap(str);
        System.out.println("show"+stringObjectMap.get("name"));
    }


    // 把Json 字符串 转 实体类
    @Test
    public void show23(){
        String str="{\"name\":\"abc2\",\"age\":\"1222231\"}";
        UserEntity o = JsonGsonUtils.jsonTurnJavaBena(str, UserEntity.class);
        System.out.println(o.getName());
    }



    // 把Json 字符串 转 List
    @Test
    public void sh221ow(){
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

        List<UserEntity> list = JsonGsonUtils.jsonTurnList(str,UserEntity.class);

        for (UserEntity UserEntity : list) {
            System.out.println(UserEntity.getName());
        }

    }


    // Map 和   UserEntity  组合
    @Test
    public void sh1o2w23(){
        String str="{ \"name\":\"abc\",obj:\"{name:abc,age:123}\" }";
        Map<String, Object> stringObjectMap = JsonGsonUtils.jsonTurnMap(str);
        String str1 = (String) stringObjectMap.get("obj");    //obj  的值必须是 字符串 "{name:abc,age:123}"
        UserEntity UserEntity = JsonGsonUtils.jsonTurnJavaBena(str1,UserEntity.class);
        System.out.println("sh1o2w23"+UserEntity);



    }

    // Map 和   List 组合
    @Test
    public void sh1ow23(){

        String str="{ \"name\":\"abc\",\"list\":[{\"name\":\"abc\",\"age\":\"123\"}, {\"name\":\"abc1\",\"age\":\"1231\"}] }";

        Map<String, Object> stringObjectMap = JsonGsonUtils.jsonTurnMap(str);

        String list = (String) JsonGsonUtils.objTurnJson(stringObjectMap.get("list")) ;

        List<UserEntity> list1 = JsonGsonUtils.jsonTurnList(list,UserEntity.class);
        for (Object person1 : list1) {
            System.out.println( "sh1ow23"+person1 );
        }


    }



}
