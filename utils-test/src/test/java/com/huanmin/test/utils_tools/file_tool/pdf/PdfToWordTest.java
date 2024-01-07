package com.huanmin.test.utils_tools.file_tool.pdf;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfToWordTest {
  @Test
  public  void test1(){
      String s = readPdfByPage("C:\\Users\\Administrator\\Desktop\\abc.pdf");
      System.out.println(s);
  }
    /**
     * 用来读取pdf文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readPdfByPage(String fileName) {
        String result = "";
        File file = new File(fileName);
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            // 新建一个PDF解析器对象
            PdfReader reader = new PdfReader(fileName);
            reader.setAppendable(true);
            // 对PDF文件进行解析，获取PDF文档页码
            int size = reader.getNumberOfPages();
            for(int i = 1 ; i < size + 1; ){
                //一页页读取PDF文本
                String pageStr = PdfTextExtractor.getTextFromPage(reader,i);
                result = result + pageStr + "\n" + "PDF解析第"+ (i)+ "页\n";
                i= i+1;
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return result;
    }

}
