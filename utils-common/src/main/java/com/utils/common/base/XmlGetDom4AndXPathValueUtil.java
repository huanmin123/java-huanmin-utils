package com.utils.common.base;


import com.utils.common.obj.reflect.FieldUtil;
import org.dom4j.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://www.w3school.com.cn/xpath/xpath_axes.asp
// 获取xml节点和属性的value
public class XmlGetDom4AndXPathValueUtil {
    // 将xml字符串转换为Document对象
    public static Document parseXmlString(String xmlString) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlString);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return document;
    }
    
    //将Document对象转换为xml字符串
    public static String parseXmlString(Document document) {
        return document.asXML();
    }
    //将xml字符串转换为Map对象 ,只支持一层
    public static Map<String, String> getXmlToMap(String xml,String rootName ,String tabName,String attrName) {
        Map<String, String> map = new HashMap<>();
        Document document = parseXmlString(xml);
        Node root = document.selectSingleNode("//" + rootName);
        List<Node> nodes = root.selectNodes("//"+tabName);
        for (Node node : nodes) {
            Node arrtN = node.selectSingleNode("@" + attrName);
            String arrtV = arrtN.getText();
            map.put(arrtV,node.getText());
        }
        return map;
    }
    //将xml字符串转换为List<Map>对象 ,平级支持多层
    public static List<Map<String,String>> getXmlToMaps(String xml,String rootNames ,String tabName,String attrName) {
        List<Map<String,String>> list = new ArrayList<>();
        Document document = parseXmlString(xml);
        List<Node> roots = document.selectNodes("//" + rootNames);
        for (Node root : roots) {
            Map<String,String> map = new HashMap<>();
            List<Node> nodes = root.selectNodes("child::"+tabName);
            for (Node node : nodes) {
                Node arrtN = node.selectSingleNode("@" + attrName);
                String arrtV = arrtN.getText();
                map.put(arrtV,node.getText());
            }
            list.add(map);
        }
        return list;
    }
    
    
    //将Map对象转换为xml字符串
    public static String getMapToXml(Map<String, String> map, String rootName, String tabName, String attrName) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement(rootName);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Element tab = root.addElement(tabName);
            tab.addAttribute(attrName, entry.getKey());
            tab.setText(entry.getValue());
        }
        return parseXmlString(document);
    }

   
    //将bean对象转换为xml字符串
    public static String getBeanToXml(Object bean, String rootName, String tabName, String attrName)  {
        Document document = DocumentHelper.createDocument();
        try {
            Class<?> aClass = bean.getClass();
            Element root = document.addElement(rootName);
            Field[] fields = FieldUtil.getFields(aClass);//获取所有属性
            for (Field field : fields) {
                Element tab = root.addElement(tabName);
                tab.addAttribute(attrName, field.getName());
                Object v = field.get(bean);
                tab.setText(v == null ? "" : v.toString());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return parseXmlString(document);
    }
    
  
    //将xml字符串转换为bean对象 ,只支持一层,并且属性名和xml属性值一致,Bean类型必须都是String 否则报错 java.lang.IllegalArgumentException: Can not set int field
    public static <T> T getXmlToBean(String xml, String rootName, String tabName, String attrName, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            Document document = parseXmlString(xml);
            Node root = document.selectSingleNode("//" + rootName);
            List<Node> nodes = root.selectNodes("//"+tabName);
            for (Node node : nodes) {
                Node arrtN = node.selectSingleNode("@" + attrName);
                String arrtV = arrtN.getText();
                Field field = FieldUtil.getField(clazz, arrtV);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(t, node.getText());
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return t;
    }
    
    // 获取xml指定tabName 的第一个值
    public static String getXmlByTabNameOne(String xmlString, String tabName) {
        Document document = parseXmlString(xmlString);
        Node node = document.selectSingleNode("//" + tabName);
        return node == null ? null : node.getText();
    }
    // 获取xml指定tabName 的所有值
    public static List<String> getXmlByTabName(String xmlString, String tabName) {
        Document document = parseXmlString(xmlString);
        List<Node> nodes = document.selectNodes("//" + tabName);
        List<String> list = new ArrayList<>();
        for (Node node : nodes) {
            list.add(node.getText());
        }
        return list;
    }
    // 获取xml指定attrName的第一个值
    public static String getXmlByAttrNameOne(String xmlString, String attrName) {
        Document document = parseXmlString(xmlString);
        Node node = document.selectSingleNode("//@" + attrName);
        return node == null ? null : node.getText();
    }
    // 获取xml指定attrName的所有值
    public static List<String> getXmlByAttrName(String xmlString, String attrName) {
        Document document = parseXmlString(xmlString);
        List<Node> nodes = document.selectNodes("//@" + attrName);
        List<String> list = new ArrayList<>();
        for (Node node : nodes) {
            list.add(node.getText());
        }
        return list;
    }

    // 获取xml指定tabName , attrName=attrValue
    public static String getXmlByTabNameAndAttrNameEqAttrValueOne(String xmlString, String tabName, String attrName, String attrValue) {
        Document document = parseXmlString(xmlString);
        Node node = document.selectSingleNode("//" + tabName + "[@" + attrName + "='" + attrValue + "']");
        return node == null ? null : node.getText();
    }
    // 获取xml指定tabName , attrName=attrValue
    public static List<String> getXmlByTabNameAndAttrNameEqAttrValueAll(String xmlString, String tabName, String attrName, String attrValue) {
        Document document = parseXmlString(xmlString);
        List<Node> nodes = document.selectNodes("//" + tabName + "[@" + attrName + "='" + attrValue + "']");
        List<String> list = new ArrayList<>();
        for (Node node : nodes) {
            list.add(node.getText());
        }
        return list;
    }
   //获取指定tabName包含attrName的第一个tabName值
   public static List<String> getXmlByTabNameAndAttrNameRetTabNameOne(String xmlString, String tabName, String attrName) {
        Document document = parseXmlString(xmlString);
        List<Node> nodes = document.selectNodes("//" + tabName + "[@" + attrName + "]");
        List<String> list = new ArrayList<>();
        for (Node node : nodes) {
            list.add(node.getText());
        }
        return list;
    }
    //获取指定tabName包含attrName的所有tabName值
    public static List<String> getXmlByTabNameAndAttrNameRetTabNameAll(String xmlString, String tabName, String attrName) {
        Document document = parseXmlString(xmlString);
        List<Node> nodes = document.selectNodes("//" + tabName + "[@" + attrName + "]");
        List<String> list = new ArrayList<>();
        for (Node node : nodes) {
            list.add(node.getText());
        }
        return list;
    }
    //获取指定tabName包含attrName的第一个attrName值
    public static List<String> getXmlByTabNameAndAttrNameRetAttrValueOne(String xmlString, String tabName, String attrName) {
        Document document = parseXmlString(xmlString);
        List<Node> nodes = document.selectNodes("//" + tabName + "[@" + attrName + "]");
        List<String> list = new ArrayList<>();
        for (Node node : nodes) {
            Node arrtN = node.selectSingleNode("@" + attrName);
            list.add(arrtN.getText());
        }
        return list;
    }
    //获取指定tabName包含attrName的所有attrName值
    public static List<String> getXmlByTabNameAndAttrNameRetAttrValueAll(String xmlString, String tabName, String attrName) {
        Document document = parseXmlString(xmlString);
        List<Node> nodes = document.selectNodes("//" + tabName + "[@" + attrName + "]");
        List<String> list = new ArrayList<>();
        for (Node node : nodes) {
            Node arrtN = node.selectSingleNode("@" + attrName);
            list.add(arrtN.getText());
        }
        return list;
    }
 
 
}