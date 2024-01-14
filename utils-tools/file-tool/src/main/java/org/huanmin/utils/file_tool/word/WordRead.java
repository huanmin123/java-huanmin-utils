package org.huanmin.utils.file_tool.word;

import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.usermodel.*;

import java.util.*;

public class WordRead {
    
    /**
     * docx 格式获取表格内容
     * @param table
     */
    public static Map<String, List<String>> getTabelText(XWPFTable table) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
            String key = null;
            List<String> list = new ArrayList<>(16);
            int i = 0;
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                // 简单获取内容（简单方式是不能获取字体对齐方式的）
                StringBuffer sb = new StringBuffer();
                // 一个单元格可以理解为一个word文档，单元格里也可以加段落与表格
                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    sb.append(getParagraphText(paragraph));
                }
                if (i == 0) {
                    key = sb.toString();
                } else {
                    String value = sb.toString();
                    list.add(value == null || Objects.deepEquals(value, "") ? null : value.replace(",", ""));
                }
                i++;
            }
            result.put(key, list);
        }
        return result;
    }
    
    /**
     * docx 获取段落字符串
     * 获取段落内容
     *
     * @param paragraph
     */
    public static String getParagraphText(XWPFParagraph paragraph) {
        StringBuffer runText = new StringBuffer();
        // 获取段落中所有内容
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs.size() == 0) {
            return runText.toString();
        }
        for (XWPFRun run : runs) {
            runText.append(run.text());
        }
        return runText.toString();
    }
    
    /**
     * doc 格式的字段解析表格
     * @param tb
     * @return
     */
    public static Map<String, List<String>> getTabelDocText(Table tb) {
        Map<String, List<String>> result = new HashMap<>(16);
        //迭代行，默认从0开始,可以依据需要设置i的值,改变起始行数，也可设置读取到那行，只需修改循环的判断条件即可
        for (int i = 0; i < tb.numRows(); i++) {
            List<String> list = new ArrayList<>(16);
            int x = 0;
            TableRow tr = tb.getRow(i);
            String key = null;
            //迭代列，默认从0开始
            for (int j = 0; j < tr.numCells(); j++) {
                //取得单元格
                TableCell td = tr.getCell(j);
                StringBuffer sb = new StringBuffer();
                
                //取得单元格的内容
                for (int k = 0; k < td.numParagraphs(); k++) {
                    Paragraph paragraph = td.getParagraph(k);
                    String s = paragraph.text();
                    //去除后面的特殊符号
                    if (null != s && !"".equals(s)) {
                        s = s.substring(0, s.length() - 1);
                    }
                    sb.append(s);
                }
                if (x == 0) {
                    key = sb.toString();
                } else {
                    String value = sb.toString();
                    list.add(value == null || Objects.deepEquals(value, "") ? null : value.replace(",", ""));
                }
                x++;
            }
            result.put(key, list);
        }
        return result;
    }
    
}
