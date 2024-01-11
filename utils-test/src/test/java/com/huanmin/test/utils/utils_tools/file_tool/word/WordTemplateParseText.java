package com.huanmin.test.utils.utils_tools.file_tool.word;


import com.huanmin.utils.common.file.ResourceFileUtil;
import com.huanmin.utils.file_tool.word.WordTemplateParse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;

public class WordTemplateParseText {
    private static final Logger logger = LoggerFactory.getLogger(WordTemplateParseText.class);

    @Test
    public void test01() throws FileNotFoundException {
        // 数据
        TreeMap<String, String> data = new TreeMap<String, String>();
        data.put("yue1", "33");
        data.put("yue2", "22");
        data.put("yue3", "55");
        data.put("yue4", "12");
        data.put("yue5", "2");
        data.put("yue6", "151");
        data.put("yue7", "33");
        data.put("yue8", "34");
        data.put("yue9", "123");
        data.put("yue10", "15");
        data.put("yue11", "2");
        data.put("yue12", "10");

        // 加载模型【模型】
        String absolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("temp.docx"); //模板位置

        // 输出位置【输出】  生成新的word位置和模板在一个目录下
        String newabsolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("temp.docx")+ File.separator+"new_temp.docx";
        System.out.println(newabsolutePath);


        Boolean generate = WordTemplateParse.generate(data, absolutePath, newabsolutePath, WordTemplateParse.WordType.docx);
        System.out.println(String.valueOf(generate));



    }

    @Test
    public void test011() throws IOException {
        // 数据
        TreeMap<String, String> data = new TreeMap<String, String>();
        data.put("yue1", "33");
        data.put("yue2", "22");
        data.put("yue3", "55");
        data.put("yue4", "12");
        data.put("yue5", "2");
        data.put("yue6", "151");
        data.put("yue7", "33");
        data.put("yue8", "34");
        data.put("yue9", "123");
        data.put("yue10", "15");
        data.put("yue11", "2");
        data.put("yue12", "10");


        // 加载模型【模型】
        String absolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("temp1.doc"); //模板位置

        // 输出位置【输出】   生成新的word位置和模板在一个目录下
        String newabsolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("temp1.doc")+ File.separator+"new_temp1.doc";
        System.out.println(newabsolutePath);



        Boolean generate = WordTemplateParse.generate(data, absolutePath, newabsolutePath, WordTemplateParse.WordType.doc);
        System.out.println(String.valueOf(generate));



    }

}
