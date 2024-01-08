package com.utils.file_tool.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

public class PdfDownLoadUtils {


    public static ByteArrayOutputStream ini() throws DocumentException {
        Rectangle rect = new Rectangle(PageSize.B4.rotate());
        Document doc = new Document(rect);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter instance = PdfWriter.getInstance(doc, baos);
        doc(doc);
        return baos;
    }

    private static void doc(Document doc) throws DocumentException {
        doc.open(); //打开PDF  默认为第一页

        PdfPTable table = new PdfPTable(3);//设置3列
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Cell with colspan 3"));//设置列的内容
        cell.setColspan(3);//合并3列
        table.addCell(cell);//将内容添加到表格里第一行

        cell = new PdfPCell(new Phrase("Cell with rowspan 2")); //设置列的内容
        cell.setRowspan(2); //合并2行
        table.addCell(cell); //将内容添加到表格里第二行
        // 然后开始填空行
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");

        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        doc.add(table);
        doc.close();
    }


}
