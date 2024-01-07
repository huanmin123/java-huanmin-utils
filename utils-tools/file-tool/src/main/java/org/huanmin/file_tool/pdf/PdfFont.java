package org.huanmin.file_tool.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

public class PdfFont {
    // color= BaseColor.BLUE(蓝色)     BaseColor.BLACK(黑色)  new BaseColor(105, 105, 105)(文本色)
    // style= Font.ITALIC细,Font.NORMA正常L,Font.BOLD粗
    // size=字体大小

    public static Font getChineseFont( int size,int style,BaseColor color) {

        BaseFont bfChinese;
        Font fontChinese = null;
        try {
            bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bfChinese, size, style, color);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }

    public static Font getChineseFont(int size, int style) {
        BaseFont bfChinese;
        Font fontChinese = null;
        try {
            bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bfChinese, size, style, new BaseColor(105, 105, 105));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }
    public static Font getChineseFont( int size) {
        BaseFont bfChinese;
        Font fontChinese = null;
        try {
            bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bfChinese, size, Font.NORMAL, new BaseColor(105, 105, 105));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }
    public static Font getChineseFont(int style,BaseColor color) {
        BaseFont bfChinese;
        Font fontChinese = null;
        try {
            bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bfChinese,style, Font.NORMAL, color);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }

    public static Font getChineseFont( long size, BaseColor color) {
        BaseFont bfChinese;
        Font fontChinese = null;
        try {
            bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bfChinese, size, Font.NORMAL, color);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }

    public static Font getChineseFont( BaseColor color) {
        BaseFont bfChinese;
        Font fontChinese = null;
        try {
            bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bfChinese, 15, Font.NORMAL, color);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;
    }



    // 默认配置  黑色字体 大小15  不加粗
    public static Font getChineseFont( ) {
        BaseFont bfChinese;
        Font fontChinese = null;
        try {
            bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bfChinese, 15, Font.NORMAL, new BaseColor(105, 105, 105));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fontChinese;

    }



}
