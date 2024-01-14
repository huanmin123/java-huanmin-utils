package org.huanmin.test.utils.utils_common.base;

import org.huanmin.test.entity.UserEntity;
import org.huanmin.utils.common.string.PatternCommon;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class TestPattern {
    private static final Logger logger = LoggerFactory.getLogger(TestPattern.class);
    @Test
    public  void patternUrlEndAndParameter() {

        String path="https://editor.csdn.net/front-end/front";
        String str="/front-end/front/";
        boolean b = PatternCommon.patternUrlEndAndParameter(path, str);
        System.out.println(b);

    }

    @Test
    public void PatternCommon() {
        boolean b = PatternCommon.patternEmailAll("22214151w@qq.com");
        System.out.println(b );

    }
    @Test
    public void patternUrlAll() {
        boolean b = PatternCommon.patternUrlAll("http://c.runoob.com:81111/code21232fddemo/6230/");
        System.out.println(b );

    }

    @Test
    public  void guopstr() {
        String sqsl="# Query_time: 0.000816  Lock_time: 0.000086 Rows_sent: 15  Rows_examined: 288";
        sqsl=PatternCommon.trimAll(sqsl);
        List<String> list = PatternCommon.cutPatternStr(sqsl, "#Query_time:(.*)Lock_time:(.*)Rows_sent:(.*)Rows_examined:(.*)", 1);
        System.out.println(list);
    }
    
    @Test
    public void  renderString(){
        String str="hello ${name},id${id}";
        String s = PatternCommon.renderString(str, new HashMap<String, String>() {{
            put("name", "123");
            put("id", "456");
        }});
        System.out.println(s);
    }
    @Test
    public void  renderStringClass(){
        String str="hello ${name},id${id}";
        UserEntity userData=new UserEntity();
        userData.setId(123);
        userData.setName("456");
        String s = PatternCommon.renderString(str, userData);
        System.out.println(s);
    }

}
