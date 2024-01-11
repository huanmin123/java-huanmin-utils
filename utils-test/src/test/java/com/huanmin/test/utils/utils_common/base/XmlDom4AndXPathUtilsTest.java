package com.huanmin.test.utils.utils_common.base;

import com.huanmin.utils.common.base.UserData;
import com.huanmin.utils.common.base.XmlGetDom4AndXPathValueUtil;
import org.dom4j.Document;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class XmlDom4AndXPathUtilsTest {
    

    @Test
    public void parseXmlString() {
        String xml="<attrs>\n" +
                           "  <attr name=\"rolecode.deptCountersign\">WE_DEPARTMENT_DOCUMENT</attr>\n" +
                           "  <attr name=\"expandable\">false</attr>\n" +
                           "  <attrtest test=\"123\">abc</attrtest>\n" +
                           "</attrs>";
        Document document = XmlGetDom4AndXPathValueUtil.parseXmlString(xml);
        System.out.println(document);
    }
 
    @Test
    public void getXmlToMap(){
        String xml="<attrs>\n" +
                           "  <attr name=\"rolecode.deptCountersign\">WE_DEPARTMENT_DOCUMENT</attr>\n" +
                           "  <attr name=\"expandable\">false</attr>\n" +
                           "  <attrtest test=\"123\">abc</attrtest>\n" +
                           "</attrs>";
        Map<String, String> xmlToMap = XmlGetDom4AndXPathValueUtil.getXmlToMap(xml, "attrs", "attr", "name");
        System.out.println(xmlToMap); //{name=rolecode.deptCountersign, value=WE_DEPARTMENT_DOCUMENT}, attr={name=expandable, value=false}
    }
    @Test
    public void getMapToXml(){
        String xml="<attrs>\n" +
                           "  <attr name=\"rolecode.deptCountersign\">WE_DEPARTMENT_DOCUMENT</attr>\n" +
                           "  <attr name=\"expandable\">false</attr>\n" +
                           "  <attrtest test=\"123\">abc</attrtest>\n" +
                   "</attrs>";
        Map<String, String> xmlToMap = XmlGetDom4AndXPathValueUtil.getXmlToMap(xml, "attrs", "attr", "name");
        String mapToXml = XmlGetDom4AndXPathValueUtil.getMapToXml(xmlToMap, "attrs", "attr", "name");
        System.out.println(mapToXml);
    }
    @Test
    public void getXmlToMaps(){
        String xml="        <root>\n" +
                               "\t<attrs>\n" +
                               "\t  <attr name=\"rolecode.deptCountersign\">WE_DEPARTMENT_DOCUMENT</attr>\n" +
                               "\t  <attr name=\"expandable\">false</attr>\n" +
                               "\t</attrs>\n" +
                               "\t<attrs>\n" +
                               "\t  <attr name=\"rolecode.deptCountersign\">WE_DEPARTMENT_DOCUMENT</attr>\n" +
                               "\t  <attr name=\"expandable\">false</attr>\n" +
                               "\t</attrs>\n" +
                           "</root>";
        List<Map<String, String>> xmlToMaps = XmlGetDom4AndXPathValueUtil.getXmlToMaps(xml, "attrs", "attr", "name");

        System.out.println(xmlToMaps);
    }
    
    @Test
    public  void getBeanToXml() throws IllegalAccessException {
        UserData build = UserData.builder().name("哈哈").age(22).sex("男").build();
        String beanToXml = XmlGetDom4AndXPathValueUtil.getBeanToXml(build, "user","attr","name");
        System.out.println(beanToXml);
    }
    
    //
    @Test
    public  void getXmlToBean() throws IllegalAccessException, ParseException {
//        String xml="<user>\n" +
//                           "  <attr name=\"name\">哈哈</attr>\n" +
//                           "  <attr name=\"age\">22</attr>\n" +
//                           "  <attr name=\"sex\">男</attr>\n" +
//                           "</user>";
//        UserData xmlToBean = XmlGetDom4AndXPathValueUtil.getXmlToBean(xml, "user", "attr", "name", UserData.class);
//        System.out.println(xmlToBean);
    
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-3-22 09:08:32.393000");
        long diff =new Date().getTime() -date.getTime();
        String now = new SimpleDateFormat("dd天HH时mm分").format(new Date(diff));
        System.out.println(now);
    }
}
