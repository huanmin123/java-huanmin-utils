package com.utils.file_tool.pdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfRead {
    /**
     * 用来读取pdf文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public static  List<String> readPdfByPage(String fileName) {
        List<String> list=new ArrayList<>();
        File file = new File(fileName);
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            // 新建一个PDF解析器对象
            PdfReader reader = new PdfReader(fileName);
            reader.setAppendable(true);
            // 对PDF文件进行解析，获取PDF文档页码
            int size = reader.getNumberOfPages();
            for(int i = 1 ; i < size + 1;i++ ){
                //一页页读取PDF文本
                String pageStr = PdfTextExtractor.getTextFromPage(reader,i);
                list.add(pageStr);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
        return list;
    }
    //读取某几页PDF文本
    public static  List<String> readPdfByPage(String fileName, int from, int end) {
        List<String> list=new ArrayList<>();
        String result = "";
        File file = new File(fileName);
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            // 新建一个PDF解析器对象
            PdfReader reader = new PdfReader(fileName);
            reader.setAppendable(false);
            // 对PDF文件进行解析，获取PDF文档页码
            int size = reader.getNumberOfPages();
            for(int i = from ; i <= end && i< size;i++ ){
                //一页页读取PDF文本
                String pageStr = PdfTextExtractor.getTextFromPage(reader,i);
                list.add(pageStr);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
        return list;
    }

}
