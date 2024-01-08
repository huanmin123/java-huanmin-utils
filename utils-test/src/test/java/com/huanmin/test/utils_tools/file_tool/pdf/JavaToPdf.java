package com.huanmin.test.utils_tools.file_tool.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.utils.file_tool.pdf.PdfUtils;
import com.utils.file_tool.pdf.PdfWatermark;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JavaToPdf {
    private static final Logger logger = LoggerFactory.getLogger(JavaToPdf.class);
    private static String FILE_DIR = null;

    static {
        try {
            FILE_DIR = Paths.get(ResourceUtils.getURL("classpath:").getPath().substring(1)).toString() + File.separator;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test1() throws IOException, DocumentException {

        Rectangle rect = new Rectangle(PageSize.B4.rotate());
        Document doc = new Document(rect);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter instance = PdfWriter.getInstance(doc, baos);
        doc.open(); //打开PDF  默认为第一页

        doc.close();

    }


    @Test
    public void test2() throws Exception {
        PdfWatermark.setWatermark(FILE_DIR + "createSamplePDF_sui.pdf", FILE_DIR + "createSamplePDF.pdf", "测试打印", 2);

    }

    @Test
    public void test2_1() throws Exception {
        PdfWatermark.addPdfMark(FILE_DIR + "createSamplePDF_sui.pdf", FILE_DIR + "createSamplePDF.pdf", FILE_DIR + "baidu.jpg");

    }


    @Test
    public void test2_2() throws Exception {
        PdfUtils.removeBlankPdfPages(FILE_DIR + "createSamplePDF.pdf", FILE_DIR + "createSamplePDF_NULL.pdf");

    }

    @Test
    public void test2_21() throws Exception {
        PdfUtils.splitPDFFile(FILE_DIR + "createSamplePDF.pdf", FILE_DIR + "createSamplePDF_sp.pdf", 1, 3);

    }

    @Test
    public void de() throws DocumentException, IOException {
        PdfReader reader = new PdfReader(FILE_DIR + "createSamplePDF.pdf");
        // 从原PDF中抽取指定页 生成
        reader.selectPages("1,3,5"); //只选择1和3页和第5页
        //生成新的PDF
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(FILE_DIR
                + "createSamplePDF_de.pdf"));
        stamp.close();
        reader.close();
    }


    @Test
    public void show() {
        // {processId:NDP210427001;[{rm:RM'2021'12345}]}

        // DOMESTIC(国内)       ABROAD(国外)
        String rmId = "RM'2021'12345"; //付款凭证号
        String type = null; // 业务类别
        String time = null; //账务年度
        String processId = "NDP210427001";
        String substring = processId.substring(1, 2);
        System.out.println(substring);
        if ("D".equals(substring)) {
            type = "DOMESTIC";
        } else {
            type = "ABROAD";
        }
        String substring1 = processId.substring(3, 5);
        time = "20" + substring1;


    }

    @Test
    public void zip() throws IOException, DocumentException {
        //创建一个zip
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(FILE_DIR + "zipPDF.zip"));
        //创建PDF文件
        ZipEntry entry = new ZipEntry("hello_" + 1 + ".pdf");
        //将PDF添加到ZIP
        zip.putNextEntry(entry);
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, zip);
        // 后面内容


        writer.setCloseStream(false);
        document.open();
        document.add(new Paragraph("Hello " + 1));
        document.close();
        zip.closeEntry();

        zip.close();
    }

    @Test
    public void he() throws IOException, DocumentException {

        ArrayList<String> arrayList = new ArrayList();
        arrayList.add(FILE_DIR + "splitPDF1.pdf");
        arrayList.add(FILE_DIR + "splitPDF2.pdf");
        mergepdf(arrayList, FILE_DIR + "mergePDF.pdf");

    }

    /**
     * @param fileList 文档地址
     * @param savepath 合并后地址
     */
    public static void mergepdf(ArrayList<String> fileList, String savepath) {
        Document document = null;
        try {
            document = new Document(new PdfReader(fileList.get(0)).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));

            document.open();
            for (int i = 0; i < fileList.size(); i++) {
                PdfReader reader = new PdfReader(fileList.get(i));

                copy.addDocument(reader);
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }


    @Test
    public  void mob() throws DocumentException, IOException {
        editPdfTemplate(FILE_DIR+"moban.pdf",FILE_DIR+"wordPDF.pdf");
    }

    public  void editPdfTemplate(String templateFile, String outFile) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(templateFile); // 模版文件目录
        PdfStamper ps = new PdfStamper(reader, new FileOutputStream(outFile)); // 生成的输出流
        AcroFields s = ps.getAcroFields();

        //编辑文本域表单的内容
        s.setField("fill_1", "558");
        s.setField("fill_2", "99966");

        ps.setFormFlattening(true);  // 模板中的变量赋值之后不能编辑
        ps.close();
        reader.close();

    }

}
