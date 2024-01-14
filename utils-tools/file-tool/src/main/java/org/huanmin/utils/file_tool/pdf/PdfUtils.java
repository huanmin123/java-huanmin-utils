package org.huanmin.utils.file_tool.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PdfUtils {



    /**
     *  删除PDF内空页
     *
     * @param pdfSourceFile    原文件
     * @param pdfDestinationFile  处理后新生成的文件
     */

    public static void removeBlankPdfPages(String pdfSourceFile, String pdfDestinationFile)
    {
        try
        {
            // step 1: create new reader
            PdfReader r = new PdfReader(pdfSourceFile);
            RandomAccessFileOrArray raf = new RandomAccessFileOrArray(pdfSourceFile);
            Document document = new Document(r.getPageSizeWithRotation(1));
            // step 2: create a writer that listens to the document
            PdfCopy writer = new PdfCopy(document, new FileOutputStream(pdfDestinationFile));
            // step 3: we open the document
            document.open();
            // step 4: we add content
            PdfImportedPage page = null;


            //loop through each page and if the bs is larger than 20 than we know it is not blank.
            //if it is less than 20 than we don't include that blank page.
            for (int i=1;i<=r.getNumberOfPages();i++)
            {
                //get the page content
                byte bContent [] = r.getPageContent(i,raf);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                //write the content to an output stream
                bs.write(bContent);
                //add the page to the new pdf
                if (bs.size() > 20)
                {
                    page = writer.getImportedPage(r, i);
                    writer.addPage(page);
                }
                bs.close();
            }
            //close everything
            document.close();
            writer.close();
            raf.close();
            r.close();
        }
        catch(Exception e)
        {
            //do what you need here
        }
    }



    /**
     * 截取pdfFile的第from页至第end页，组成一个新的文件名
     * @param respdfFile  需要分割的PDF
     * @param savepath  新PDF
     * @param from  起始页
     * @param end  结束页
     */
    public static void splitPDFFile(String respdfFile, String savepath, int from, int end) {
        Document document = null;
        PdfCopy copy = null;
        try {
            PdfReader reader = new PdfReader(respdfFile);
            int n = reader.getNumberOfPages();
            if(end==0){
                end = n;
            }
            ArrayList<String> savepaths = new ArrayList<String>();
            savepaths.add(savepath);
            document = new Document(reader.getPageSize(1));
            copy = new PdfCopy(document, new FileOutputStream(savepaths.get(0)));
            document.open();
            for(int j=from; j<=end; j++) {
                document.newPage();
                PdfImportedPage page = copy.getImportedPage(reader, j);
                copy.addPage(page);
            }
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch(DocumentException e) {
            e.printStackTrace();
        }
    }

}
