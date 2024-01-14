package org.huanmin.utils.file_tool.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfWatermark {

    /**
     * 文字水印
     * <p>
     * 中间或者两边水印
     *
     * @param bos   添加完水印的输出
     * @param input 原PDF文件输入
     * @param word  水印内容
     * @param model 水印添加位置1中间，2两边
     */
    public static void setWatermark(String bos, String input, String word, int model)
            throws DocumentException, IOException {

        PdfReader reader = new PdfReader(new FileInputStream(input));
        PdfStamper stamper = new PdfStamper(reader,new BufferedOutputStream(new FileOutputStream(bos) ));
        PdfContentByte content;
        // 创建字体,第一个参数是字体路径,itext有一些默认的字体比如说：
        BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
//        BaseFont base = BaseFont.createFont(FILE_DIR+"msyh.ttf", BaseFont.IDENTITY_H,
//                BaseFont.EMBEDDED);
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.3f);//水印透明度
        // 获取PDF页数
        int total = reader.getNumberOfPages();
        // 遍历每一页
        for (int i = 0; i < total; i++) {
            float width = reader.getPageSize(i + 1).getWidth(); // 页宽度
            float height = reader.getPageSize(i + 1).getHeight(); // 页高度
            content = stamper.getOverContent(i + 1);// 内容
            content.beginText();//开始写入文本
            content.setGState(gs);
            content.setColorFill(BaseColor.LIGHT_GRAY);
            content.setTextMatrix(70, 200);//设置字体的输出位置

            if (model == 1) { //平行居中的3条水印
                content.setFontAndSize(base, 40); //字体大小
                //showTextAligned 方法的参数分别是（文字对齐方式，位置内容，输出水印X轴位置，Y轴位置，旋转角度）
                content.showTextAligned(Element.ALIGN_CENTER, word, width / 2, height/3, 30);
                content.showTextAligned(Element.ALIGN_CENTER, word, width / 2, height/2, 30);
                content.showTextAligned(Element.ALIGN_CENTER, word, width / 2, height/4-50, 30);
            } else { // 左右两边个从上到下4条水印
                float rotation = 30;// 水印旋转度数

                content.setFontAndSize(base, 20);
                content.showTextAligned(Element.ALIGN_LEFT, word, 20, height - 50, rotation);
                content.showTextAligned(Element.ALIGN_LEFT, word, 20, height / 4 * 3 - 50, rotation);
                content.showTextAligned(Element.ALIGN_LEFT, word, 20, height / 2 - 50, rotation);
                content.showTextAligned(Element.ALIGN_LEFT, word, 20, height / 4 - 50, rotation);

                content.setFontAndSize(base, 22);
                content.showTextAligned(Element.ALIGN_RIGHT, word, width - 20, height - 50, rotation);
                content.showTextAligned(Element.ALIGN_RIGHT, word, width - 20, height / 4 * 3 - 50, rotation);
                content.showTextAligned(Element.ALIGN_RIGHT, word, width - 20, height / 2 - 50, rotation);
                content.showTextAligned(Element.ALIGN_RIGHT, word, width - 20, height / 4 - 50, rotation);
            }
            content.endText();//结束写入文本
        }
        stamper.close();
        reader.close();
    }


    /**
     * 给pdf文件添加水印  (注意如果有添加背景颜色的话那么 图片水印将失效被盖住了)
     *
     * @param outPdfFile    加了水印后要输出的路径
     * @param InPdfFile     要加水印的原pdf文件路径
     * @param markImagePath 水印图片路径
     * @throws Exception
     */
    public static void addPdfMark( String outPdfFile,String InPdfFile, String markImagePath) throws Exception {

        PdfReader reader = new PdfReader(InPdfFile, "PDF".getBytes());

        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outPdfFile));
        PdfGState gs = new PdfGState();

        Image img = Image.getInstance(markImagePath);// 插入水印
        img.setRotationDegrees(30);//旋转 角度
//        img.scaleAbsolute(200,100);//自定义图片大小
        img.scalePercent(50);//图片依照比例缩放 百分之50
        gs.setFillOpacity(0.3f);//水印透明度
        int total = reader.getNumberOfPages(); //全部页
        PdfContentByte content;
        for (int i = 0; i < total; i++) {
            content = stamp.getUnderContent(+ 1); // 内容
            content.setGState(gs);
            float width = reader.getPageSize(i + 1).getWidth(); // 页宽度
            float height = reader.getPageSize(i + 1).getHeight(); // 页高度
            img.setAbsolutePosition(width / 2, height/3); //图片位置   第1张图
            content.addImage(img);
            img.setAbsolutePosition(width / 2, height/2); //图片位置   第2张图
            content.addImage(img);
            img.setAbsolutePosition(width / 2, height/4-50); //图片位置  第3张图
            content.addImage(img);
        }

        stamp.close();// 关闭
        reader.close();
    }




}
