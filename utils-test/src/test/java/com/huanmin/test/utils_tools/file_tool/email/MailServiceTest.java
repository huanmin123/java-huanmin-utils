package com.huanmin.test.utils_tools.file_tool.email;


import com.utils.common.file.ResourceFileUtil;
import com.utils.file_tool.email.MailServiceUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailServiceUtil mailService;

    @Resource
    private TemplateEngine templateEngine;

//    纯文本方式
    @Test
    public void sendTextMailText(){
        for (int i = 0; i < 100; i++) {
           mailService.sendTextMailText("菜逼","杨泽你个大坑比","1813609599@qq.com");
            System.out.println(i);
        }

    }

    //  文本附件的方式
    @Test
    public void sendTextMailText1() throws FileNotFoundException {
        String filePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "test.doc";
        List<String> list= new ArrayList<>();
        list.add(filePath);
        list.add(filePath);
        mailService.sendTextMailText("测试springbootimail-主题","测试spring boot imail - 内容",list,"huanmin123xx@163.com");
    }

    //  HTML发送邮件  可以带附件 + 图片

    @Test
    public void sendHtmlMail() throws FileNotFoundException {

        //添加图片
        Map<String,String>  map=new HashMap();
        map.put("img1",ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "timg.jpg");
        map.put("img2",ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "timg.jpg");
        //附件方式
        List<String> list= new ArrayList<>();
        String filePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "test.doc";
        list.add(filePath);
        list.add(filePath);

        String content = "<html>" +
                "<body>" +
                "<h3>hello world</h3>" +
                "<h1>html</h1>" +
                "<h1>图片-附件-邮件</h1>" +
                "<img src='cid:img1'></img>" +    //此img1 对应上面 map的img1   否则图片找不到
                "<img src='cid:img2'></img>" +
                "<body>" +
                "</html>";


        mailService.sendHtmlMail("这是一封图片邮件",content,list, map,"huanmin123xx@163.com");
    }

    //  HTML发送邮件  只带附件

    @Test
    public void sendHtmlMail1() throws FileNotFoundException {

        //附件方式
        List<String> list= new ArrayList<>();
        String filePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "test.doc";
        list.add(filePath);
        list.add(filePath);
        String content = "<html>" +
                "<body>" +
                "<h3>hello world</h3>" +
                "<h1>html</h1>" +
                "<h1>附件邮件</h1>" +
                "<body>" +
                "</html>";
        mailService.sendHtmlMail("这是一封图片邮件",content,list,"huanmin123xx@163.com");
    }
    //  HTML发送邮件  只带图片
    @Test
    public void sendHtmlMail2() throws FileNotFoundException {
        //添加图片
        Map<String,String>  map=new HashMap();
        map.put("img1",ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "timg.jpg");
        map.put("img2",ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "timg.jpg");
        String content = "<html>" +
                "<body>" +
                "<h3>hello world</h3>" +
                "<h1>html</h1>" +
                "<h1>图片邮件</h1>" +
                "<img src='cid:img1'></img>" +    //此img1 对应上面 map的img1   否则图片找不到
                "<img src='cid:img2'></img>" +
                "<body>" +
                "</html>";
        mailService.sendHtmlMail("这是一封图片邮件",content,map,"huanmin123xx@163.com");
    }



    //    使用Template模板方式   带附件 带图片
    @Test
    public void testTemplateMailTest1() throws FileNotFoundException {
        //添加图片
        Map<String,String>  map=new HashMap();
        map.put("img1",ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "timg.jpg");
        map.put("img2",ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "timg.jpg");
        //附件方式
        List<String> list= new ArrayList<>();
        String filePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "test.doc";
        list.add(filePath);
        list.add(filePath);

        Context context = new Context();
        // 模本内容填充
        // 模本内容填充
        context.setVariable("name","huanmin123xx");
        context.setVariable("idImage1","img1"); //此img1 对应上面 map的img1   否则图片找不到
        context.setVariable("idImage2","img2");
            // 模板名称
        String emailContent = templateEngine.process("emailTeplate", context);
        mailService. sendHtmlMailTemplate("这是一封HTML模板邮件",emailContent,list,map,"huanmin123xx@163.com");

    }

    //    使用Template模板方式   带图片
    @Test
    public void testTemplateMailTest2() throws FileNotFoundException {
        //添加图片
        Map<String,String>  map=new HashMap();
        map.put("img1",ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "timg.jpg");
        map.put("img2",ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "timg.jpg");

        Context context = new Context();
        // 模本内容填充
        context.setVariable("name","huanmin123xx");
        context.setVariable("idImage1","img1"); //此img1 对应上面 map的img1   否则图片找不到
        context.setVariable("idImage2","img2");
        // 模板名称
        String emailContent = templateEngine.process("emailTeplate", context);
        mailService. sendHtmlMailTemplate("这是一封HTML模板邮件",emailContent,map,"huanmin123xx@163.com");

    }


    //    使用Template模板方式   带附件
    @Test
    public void testTemplateMailTest3() throws FileNotFoundException {

        //附件方式
        List<String> list= new ArrayList<>();
        String filePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("templates") + File.separator + "test.doc";
        list.add(filePath);
        list.add(filePath);

        Context context = new Context();
        // 模本内容填充
        context.setVariable("name","huanmin123xx");
        // 模板名称
        String emailContent = templateEngine.process("emailTeplate", context);
        mailService. sendHtmlMailTemplate("这是一封HTML模板邮件",emailContent,list,"huanmin123xx@163.com");

    }




}
