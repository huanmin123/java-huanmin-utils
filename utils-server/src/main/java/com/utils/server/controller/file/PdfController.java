package com.utils.server.controller.file;

import com.utils.file_tool.pdf.HtmlPdf;
import com.utils.file_tool.pdf.PdfDownLoadUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/pdf")
public class PdfController {


    /**
     * PDF 文件导出
     */
    @GetMapping(value = "/export")
    public ResponseEntity<?> export(){
        try {
            Map<String, Object> dataMap = new HashMap<>(16);
            dataMap.put("statisticalTime",new Date().toString());
            dataMap.put("max",5);
            ResponseEntity<?> responseEntity = HtmlPdf.export(dataMap);
            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<String>("{ \"code\" : \"404\", \"message\" : \"not found\" }",
                headers, HttpStatus.NOT_FOUND);
    }












    /**
     * PDF 文件导出
     */
    @GetMapping(value = "/export1")
    public ResponseEntity<?> export1(HttpServletRequest request, HttpServletResponse response)  {
        HttpHeaders headers = new HttpHeaders();

        String pdName="student.pdf"; //文件名称

        //设置页面编码格式
        try {
            ByteArrayOutputStream ini = PdfDownLoadUtils.ini();
            // 下载PDF
            headers.setContentDispositionFormData("attachment", pdName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(ini.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //   返回错误信息
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<String>("{ \"code\" : \"404\", \"message\" : \"not found\" }",
                headers, HttpStatus.NOT_FOUND);
    }

    /**
     * PDF 文件导出
     */
    @GetMapping(value = "/export2")
    public ResponseEntity<?> export2(HttpServletRequest request, HttpServletResponse response)  {
        HttpHeaders headers = new HttpHeaders();

        String pdName="student.pdf"; //文件名称

        //设置页面编码格式
        try {
            ByteArrayOutputStream ini = PdfDownLoadUtils.ini();
             //页面打开PDF 不下载
            response.setContentLength(ini.size());
            OutputStream out = response.getOutputStream();
            ini.writeTo(out);
            return new ResponseEntity<byte[]>( headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //   返回错误信息
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<String>("{ \"code\" : \"404\", \"message\" : \"not found\" }",
                headers, HttpStatus.NOT_FOUND);
    }

}
