package com.utils.file_tool.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import com.utils.common.file.ResourceFileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Map;

public class HtmlPdf{
    private static final Logger logger = LoggerFactory.getLogger(HtmlPdf.class);

    // 必须在resoures下面
    private  static String font= "templates/font/simsun.ttf";  //字体
    private  static String templ= "templates/pdf_export_employee_kpi.html"; //模板


    private HtmlPdf(){}

    private volatile static Configuration configuration;

    static {
        if (configuration == null) {
            synchronized (HtmlPdf.class) {
                if (configuration == null) {
                    configuration = new Configuration(Configuration.VERSION_2_3_28);
                }
            }
        }
    }

    /**
     * freemarker 引擎渲染 html
     *
     * @param dataMap 传入 html 模板的 Map 数据
     * @param ftlFilePath html 模板文件相对路径(相对于 resources路径,路径 + 文件名)
     *                    eg: "templates/pdf_export_demo.ftl"
     * @return
     */
    public static String freemarkerRender(Map<String, Object> dataMap, String ftlFilePath) {
        Writer out = new StringWriter();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try {
            configuration.setDirectoryForTemplateLoading(new File(ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile(ftlFilePath).getParent()));
            configuration.setLogTemplateExceptions(false);
            configuration.setWrapUncheckedExceptions(true);
            Template template = configuration.getTemplate(ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile(ftlFilePath).getName());
            template.process(dataMap, out);
            out.flush();
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 使用 iText 生成 PDF 文档
     *
     * @param htmlTmpStr html 模板文件字符串
     * @param fontFile 所需字体文件(相对路径+文件名)
     * */
    public static byte[] createPDF(String htmlTmpStr, String fontFile) {
        ByteArrayOutputStream outputStream = null;
        byte[] result = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlTmpStr);
            ITextFontResolver fontResolver = renderer.getFontResolver();
            // 解决中文支持问题,需要所需字体(ttc)文件
            fontResolver.addFont(ResourceFileUtil.getCurrentProjectResourcesAbsolutePath(fontFile),BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.layout();
            renderer.createPDF(outputStream);
            result=outputStream.toByteArray();
            if(outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }








    /**
     * PDF 文件导出
     *
     * @return
     */

    public static ResponseEntity<?> export(Map<String, Object> dataMap, String pdfName) {
        HttpHeaders headers = new HttpHeaders();
        /**
         * 数据导出(PDF 格式)
         */

        String htmlStr = HtmlPdf.freemarkerRender(dataMap, templ);

        String s = formatHtml(htmlStr);
        System.out.println(s);
        byte[] pdfBytes = HtmlPdf.createPDF(s, font);
        if (pdfBytes != null && pdfBytes.length > 0) {
            String fileName=null;
            if (pdfName!=null){
                fileName= pdfName;
            }else {
                fileName = System.currentTimeMillis() + (int) (Math.random() * 90000 + 10000) + ".pdf";
            }
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);

        }

        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<String>("{ \"code\" : \"404\", \"message\" : \"not found\" }",
                headers, HttpStatus.NOT_FOUND);
    }


    public static ResponseEntity<?> export(Map<String, Object> dataMap) {
        return  export(   dataMap ,null);
    }

    /**
     * 使用jsoup规范化html
     *
     * @param html html内容
     * @return 规范化后的html
     */
    private static String formatHtml(String html) {
        Document doc = Jsoup.parse(html);
        // 去除过大的宽度
        String style = doc.attr("style");
        if (StringUtils.isNotEmpty(style) && style.contains("width")) {
            doc.attr("style", "");
        }
        Elements divs = doc.select("div");
        for (Element div : divs) {
            String divStyle = div.attr("style");
            if (StringUtils.isNotEmpty(divStyle) && divStyle.contains("width")) {
                div.attr("style", "");
            }
        }
        // jsoup生成闭合标签
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return doc.html();
    }
}
