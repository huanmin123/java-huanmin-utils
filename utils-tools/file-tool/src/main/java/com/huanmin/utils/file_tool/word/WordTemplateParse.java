package com.huanmin.utils.file_tool.word;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordTemplateParse {

   public enum WordType {
        doc, docx
    }


    /**
     *
     * @param data  替换的数据
     * @param tempFileStream  模板文件
     * @param targetFileStream 输出的文件
     * @param type 文件;类型
     * @return
     */
    public static Boolean generate(Map<String, String> data, String  tempFileStream, String targetFileStream, WordType type)  {

        try {
            InputStream in=new FileInputStream(tempFileStream);
            OutputStream outputStream=  new FileOutputStream(targetFileStream);
            if (WordType.docx.equals(type) ) {
                return generate_docx(data, in, outputStream);
            } else if (WordType.doc.equals(type)){
                return generate_doc(data, in, outputStream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  false;
    }

    /**
     *
     * @param data  替换的数据
     * @param tempFileStream  模板文件
     * @param response   输出的文件 (游览器下载)
     * @param type 文件;类型
     * @return
     */
    public static Boolean generateDown(Map<String, String> data, String  tempFileStream, String wordName ,HttpServletResponse response, WordType type)  {

        try {
            InputStream in=new FileInputStream(tempFileStream);
            if (WordType.docx.equals(type) ) {

                return generateDown_docx(data, in,wordName, response);
            } else if (WordType.doc.equals(type)){
                return generateDown_doc(data,in,wordName,response);
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  false;
    }



   private static Boolean generate_docx(Map<String, String> data, InputStream tempFileStream, OutputStream targetFileStream) {
        try {
            // 加载磁盘的 temp.docx 文件
            XWPFDocument document = new XWPFDocument(tempFileStream);

            // 解析文档中的【段落】的插值 ${..}
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText();
                // 如果该单元格为空就遍历下一个
                if (text == null || "".equals(text)) {
                    continue;
                }

                // 得到解析 ${...} 后的内容
                String newText = replaceByMap(text, null, data);
                // 判断值是否改变，如果没有改变就不做处理
                if (!text.equals(newText)) {
                    int runSize = paragraph.getRuns().size();
                    // 清空该段落所有值
                    for (int i = 0; i < runSize; i++) {
                        paragraph.removeRun(i);
                    }

                    XWPFRun run = paragraph.createRun();
                    run.setText(newText);
                }

            }

            // 解析文档中【表格】的插值 ${..}
            // 获取文档中的表格
            List<XWPFTable> tables = document.getTables();
            for (XWPFTable table : tables) {
                // 得到所有行的集合并遍历
                List<XWPFTableRow> rows = table.getRows();
                for (XWPFTableRow row : rows) {
                    // 得到所有列的集合并遍历
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        // 获取整个单元格的值
                        String text = cell.getText();
                        // 如果该单元格为空就遍历下一个
                        if (text == null || "".equals(text)) {
                            continue;
                        }

                        // 得到解析 ${...} 后的内容
                        String newText = replaceByMap(text, null, data);
                        // 判断值是否改变，如果没有改变就不做处理
                        if (!text.equals(newText)) {
                            // 清空单元格内容
                            int paragraphsNums = cell.getParagraphs().size();
                            for (int i = 0; i < paragraphsNums; i++) {
                                cell.removeParagraph(i);
                            }
                            cell.setText(newText);
                        }
                    }
                }
            }

            // 将文档写入目标文件中
            document.write(targetFileStream);
            // 关闭流
            tempFileStream.close();
            targetFileStream.close();
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private static Boolean generate_doc(Map<String, String> data, InputStream tempFileStream, OutputStream targetFileStream) {

        try {
            // 加载磁盘的 temp.docx 文件
            HWPFDocument document = new HWPFDocument(tempFileStream);
            Range range = document.getRange();
            //把range范围内的${reportDate}替换为当前的日期
            for (Map.Entry<String, String> stringStringEntry : data.entrySet()) {

                range.replaceText("${" + stringStringEntry.getKey() + "}", stringStringEntry.getValue());

            }

            //把doc输出到输出流中
            document.write(targetFileStream);

            targetFileStream.close();
            tempFileStream.close();
            document.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
//



    private static Boolean generateDown_doc(Map<String, String> data, InputStream tempFileStream,String wordName, HttpServletResponse response ) throws UnsupportedEncodingException {

        // 返回文件相应头设置
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(wordName, "UTF-8"));
        response.setHeader("Content-Type", "application/msword");

        try {
            // 加载磁盘的 temp.docx 文件
            HWPFDocument document = new HWPFDocument(tempFileStream);

            Range range = document.getRange();
            //把range范围内的${reportDate}替换为当前的日期
            for (Map.Entry<String, String> stringStringEntry : data.entrySet()) {

                range.replaceText("${" + stringStringEntry.getKey() + "}", stringStringEntry.getValue());

            }

            //把doc输出到输出流中
            document.write(response.getOutputStream());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            document.write(os);
            byte[] bytes = os.toByteArray();
            // 获取响应报文输出流对象
            ServletOutputStream outputStream = response.getOutputStream();
            // 输出
            outputStream .write(bytes);
            outputStream .flush();
            outputStream .close();

            tempFileStream.close();
            document.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }





    private static Boolean generateDown_docx(Map<String, String> data, InputStream tempFileStream,String wordName, HttpServletResponse response) throws UnsupportedEncodingException {

        // 返回文件相应头设置
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(wordName, "UTF-8"));
        response.setHeader("Content-Type", "application/msword");
        try {
            // 加载磁盘的 temp.docx 文件
            XWPFDocument document = new XWPFDocument(tempFileStream);

            // 解析文档中的【段落】的插值 ${..}
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText();
                // 如果该单元格为空就遍历下一个
                if (text == null || "".equals(text)) {
                    continue;
                }

                // 得到解析 ${...} 后的内容
                String newText = replaceByMap(text, null, data);
                // 判断值是否改变，如果没有改变就不做处理
                if (!text.equals(newText)) {
                    int runSize = paragraph.getRuns().size();
                    // 清空该段落所有值
                    for (int i = 0; i < runSize; i++) {
                        paragraph.removeRun(i);
                    }

                    XWPFRun run = paragraph.createRun();
                    run.setText(newText);
                }

            }

            // 解析文档中【表格】的插值 ${..}
            // 获取文档中的表格
            List<XWPFTable> tables = document.getTables();
            for (XWPFTable table : tables) {
                // 得到所有行的集合并遍历
                List<XWPFTableRow> rows = table.getRows();
                for (XWPFTableRow row : rows) {
                    // 得到所有列的集合并遍历
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        // 获取整个单元格的值
                        String text = cell.getText();
                        // 如果该单元格为空就遍历下一个
                        if (text == null || "".equals(text)) {
                            continue;
                        }

                        // 得到解析 ${...} 后的内容
                        String newText = replaceByMap(text, null, data);
                        // 判断值是否改变，如果没有改变就不做处理
                        if (!text.equals(newText)) {
                            // 清空单元格内容
                            int paragraphsNums = cell.getParagraphs().size();
                            for (int i = 0; i < paragraphsNums; i++) {
                                cell.removeParagraph(i);
                            }
                            cell.setText(newText);
                        }
                    }
                }
            }

            // 将文档写入目标文件中
            document.write(response.getOutputStream());

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            document.write(os);
            byte[] bytes = os.toByteArray();
            // 获取响应报文输出流对象
            ServletOutputStream outputStream = response.getOutputStream();
            // 输出
            outputStream .write(bytes);
            outputStream .flush();
            outputStream .close();

            // 关闭流
            tempFileStream.close();
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }








    // 如果根据正则表达式规则匹配成功则替换，否则返回原值
    private static String replaceByMap(String content, String regex, Map<String, String> map) {
        // 匹配插值 ${...}
        if (regex == null) {
            regex = "\\$\\{[\\w\\W]+\\}";
        }

        // 初始化正则
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(content);

        // 找到匹配的内容的 key
        if (matcher.find()) {
            // 提前key值
            String matchContent = matcher.group(0);
            int start = matchContent.indexOf("{");
            int end = matchContent.indexOf("}");

            // 获取key
            String key = matchContent.substring(start + 1, end);

            // 从map中取出key的值
            String value = map.get(key);
            if (value == null) {
                return content;
            }

            // 得到替换后的值
            String newContent = matcher.replaceAll(value);
            return newContent;
        }

        // 没有匹配到，返回原值
        return content;
    }

}
