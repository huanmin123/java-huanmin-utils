package org.huanmin.file_tool.word;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WordCreate {
    private final String fontFamily = "宋体";
    private final int fontSize = 12;
    private XWPFDocument document = null;//文档
    private XWPFParagraph paragraph = null;//行
    private XWPFRun run;//块
    private XWPFTable table;//表格
    private XWPFTableRow row;//行
    private XWPFTableCell cell;//单元格
    
    //统一字体
    public static WordCreate builder() {
        return new WordCreate();
    }
    
    private WordCreate() {
        document = new XWPFDocument();
        initTitleConfig();
        
    }
    
    //初始化标题样式
    private void initTitleConfig() {
        for (int i = 1; i <= 10; i++) {
            WordTool.addCustomHeadingStyle(document, i + "级标题", i);
        }
    }
    
    //创建段落,也就是一行
    public WordCreate createParagraph() {
        paragraph = document.createParagraph();
        return this;
    }
    
    /**
     * @param level 标题级别 1-10, 每级标题字体大小-2 ,1级标题字体大小: 22
     * @return
     */
    public WordCreate addTitle(int level, String text) {
        createParagraph();
        paragraph.setStyle(level + "级标题");
        run = paragraph.createRun();
        run.setFontFamily(fontFamily);
        level = Math.max(level, 1);//最小为1
        level = Math.min(level, 10);//最大为10
        run.setFontSize(20 - ((level - 1) * 2));
        run.setText(text);
        return this;
    }
    
    public WordCreate addText(String text) {
        run = paragraph.createRun();
        run.setFontFamily(fontFamily);
        run.setText(text);
        run.setFontSize(fontSize);
        return this;
    }
    
    //字体基础样式
    public WordCreate setStyle(TextStyle textStyle) {
        run.setFontFamily(textStyle.getFontFamily() != null ? textStyle.getFontFamily() : fontFamily);
        run.setFontSize(textStyle.getFontSize() != null ? textStyle.getFontSize() : fontSize);
        run.setColor(textStyle.getColor());
        run.setBold(textStyle.isBold());
        run.setItalic(textStyle.isItalic());
        run.setStrikeThrough(textStyle.isStrike());
        run.setShadow(textStyle.isShadow());
        run.setDoubleStrikethrough(textStyle.isDoubleStrike());
        return this;
    }
    
    //添加超链接
    public WordCreate addLink(String url, String text) {
        // 将链接添加为外部关系
        String id = paragraph
                            .getDocument()
                            .getPackagePart()
                            .addExternalRelationship(url,
                                    XWPFRelation.HYPERLINK.getRelation()).getId();
        // 添加链接并将其绑定到关系
        CTHyperlink cLink = paragraph.getCTP().addNewHyperlink();
        cLink.setId(id);
        // 创建链接文本
        CTText ctText = CTText.Factory.newInstance();
        ctText.setStringValue(text);
        CTR ctr = CTR.Factory.newInstance();
        CTRPr rpr = ctr.addNewRPr();
        
        //设置超链接样式
        CTColor color = CTColor.Factory.newInstance();
        color.setVal("0000FF");
        rpr.setColor(color);
        rpr.addNewU().setVal(STUnderline.SINGLE);
        
        //设置字体
        CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
        fonts.setAscii(fontFamily);
        fonts.setEastAsia(fontFamily);
        fonts.setHAnsi(fontFamily);
        
        //设置字体大小
        CTHpsMeasure sz = rpr.isSetSz() ? rpr.getSz() : rpr.addNewSz();
        sz.setVal(new BigInteger(fontSize * 2 + ""));
        ctr.setTArray(new CTText[]{ctText});
        // 将链接文本插入到链接中
        cLink.setRArray(new CTR[]{ctr});
        return this;
    }
    
    //居中
    public WordCreate setParagraphCenter() {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setVerticalAlignment(TextAlignment.CENTER);//垂直居中
        return this;
    }
    
    //居左
    public WordCreate setParagraphLeft() {
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        paragraph.setVerticalAlignment(TextAlignment.CENTER);//垂直居中
        return this;
    }
    
    //居右
    public WordCreate setParagraphRight() {
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        paragraph.setVerticalAlignment(TextAlignment.CENTER);//垂直居中
        return this;
    }
    
    
    //添加图片
    public WordCreate addImage(String filePath, int width, int height) {
        try {
            XWPFRun run = paragraph.createRun();                   //获取文件流
            InputStream stream = Files.newInputStream(Paths.get(filePath));
            //获取图片名称
            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            run.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, fileName, Units.toEMU(width), Units.toEMU(height));
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    //添加页眉
    public WordCreate addHeader(String string) {
        createParagraph();
        paragraph.setBorderBottom(Borders.THICK);
        setParagraphCenter();
        addText(string);
        return this;
    }
    //添加页脚
    public WordCreate addFooter(String string) {
        createParagraph();
        paragraph.setBorderTop(Borders.THICK);
        setParagraphCenter();
        addText(string);
        return this;
    }
    
    //换行
    public WordCreate addNewLine() {
        run.addBreak();
        return this;
    }
    
    //添加表格
    public WordCreate createTable(int row, int col) {
        table = document.createTable(row, col);
        table.setTableAlignment(TableRowAlign.CENTER);
        return this;
    }
    //添加单元格值
    public WordCreate addCellText(int row, int col, String text) {
        this.row = table.getRow(row - 1);
        cell = this.row.getCell(col - 1);
        paragraph = cell.addParagraph();
        addText(text);
        return this;
    }
    
    //设置一行的单元文字样式
    public WordCreate setRowStyle(int row, TextStyle textStyle) {
        this.row = table.getRow(row - 1);
        for (int i = 0; i < this.row.getTableCells().size(); i++) {
            cell = this.row.getCell(i);
            List<XWPFParagraph> paragraphs = cell.getParagraphs();
            for (XWPFParagraph xwpfParagraph : paragraphs) {
                List<XWPFRun> runs = xwpfParagraph.getRuns();
                for (XWPFRun xwpfRun : runs) {
                    run = xwpfRun;
                    setStyle(textStyle);
                }
            }
        }
        return this;
    }
    //设置一列的单元样式
    public WordCreate setColStyle(int col, TextStyle textStyle) {
        for (int i = 0; i < table.getNumberOfRows(); i++) {
            row = table.getRow(i);
            cell = row.getCell(col - 1);
            List<XWPFParagraph> paragraphs = cell.getParagraphs();
            for (XWPFParagraph xwpfParagraph : paragraphs) {
                List<XWPFRun> runs = xwpfParagraph.getRuns();
                for (XWPFRun xwpfRun : runs) {
                    run = xwpfRun;
                    setStyle(textStyle);
                }
            }
        }
        return this;
    }
   
    //合并单元格
    public WordCreate mergeTableCell(int row, int col, int rowSpan, int colSpan) {
        table.getRow(row).getCell(col).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
        table.getRow(row).getCell(col).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
        for (int i = row; i < row + rowSpan; i++) {
            for (int j = col; j < col + colSpan; j++) {
                if (i == row && j == col) {
                    continue;
                }
                table.getRow(i).getCell(j).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                table.getRow(i).getCell(j).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
        return this;
    }
    
    /**
     * 单元格列合并
     * @param row 合并列所在行
     * @param startCell 开始列
     * @param endCell 结束列
     */
    public  WordCreate mergeCellsHorizontal( int row, int startCell, int endCell) {
        row = row - 1;
        startCell = startCell - 1;
        endCell = endCell - 1;
        for (int i = startCell; i <= endCell; i++) {
            XWPFTableCell cell = table.getRow(row).getCell(i);
            if (i == startCell) {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
        return this;
    }
    //单元格行合并
    public  WordCreate mergeCellsVertically(int col, int startRow, int endRow) {
        col = col - 1;
        startRow = startRow - 1;
        endRow = endRow - 1;
        for (int i = startRow; i <= endRow; i++) {
            XWPFTableCell cell = table.getRow(i).getCell(col);
            if (i == startRow) {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
        return this;
    }

  
  
    
    public void toWordFile(String outFilePath) {
        try {
            FileOutputStream out = new FileOutputStream(outFilePath);
            document.write(out);
            document.close();//关闭文档
            out.close();//关闭输出流
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
