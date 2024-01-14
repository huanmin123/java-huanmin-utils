package org.huanmin.test.utils.utils_tools.file_tool.word;

import org.huanmin.utils.file_tool.word.TextStyle;
import org.huanmin.utils.file_tool.word.WordCreate;
import org.junit.Test;

public class WordCreateTest {
    String path = "C:\\Users\\huanmin\\Desktop\\abc.docx";
    
    @Test
    public void addTitle() {
        WordCreate.builder().
                addTitle(1, "测试word创建1").
                createParagraph().
                addTitle(2, "测试word创建2").
                createParagraph().
                addTitle(3, "测试word创建3").toWordFile(path);
    }
    
    @Test
    public void addParagraph() {
        WordCreate.builder().
                addTitle(1, "测试word创建1").
                createParagraph().
                addText("段落创建1").
                createParagraph().
                addTitle(1, "测试word创建2").
                createParagraph().
                addText("段落创建1").
                toWordFile(path);
    }
    
    @Test
    public void addParagraphDirection() {
        WordCreate.builder().
       
                addTitle(1, "测试word创建1").
                setParagraphCenter(). //标题居中
                createParagraph().
                setParagraphCenter(). //段落居右
                addText("段落创建1, ").
                addText("段落创建2").
                toWordFile(path);
    }
    
    @Test
    public void addTestStyle() {
        WordCreate.builder().
                addTitle(1, "测试word创建1").
                setParagraphCenter(). //标题居中
                setStyle(TextStyle.builder().color("4bccf0").shadow(true).italic(true).build()).
                createParagraph().
                setParagraphCenter(). //段落居中
                addText("段落创建1,").
                setStyle(TextStyle.builder().color("f2a7ae").bold(true).build()).
                addText("段落创建2").
                setStyle(TextStyle.builder().fontSize(20).build()).
                toWordFile(path);
    }
    
    @Test
    public void addLike() {
        WordCreate.builder().
                addTitle(1, "测试word创建").
                createParagraph().
                addLink("http://www.baidu.com", "百度一下").
                toWordFile(path);
    }
    
    @Test
    public void addImage() {
        WordCreate.builder().
                addTitle(1, "测试word创建").
                createParagraph().
                addImage("C:\\Users\\huanmin\\Pictures\\1.jpg", 200, 200).
                toWordFile(path);
        
    }
    
    @Test
    public  void addHeader(){
        WordCreate.builder().
                addHeader("页眉").
                addTitle(1, "测试word创建1").
                createParagraph().
                addText("段落创建1, ").
                addText("段落创建2").
                toWordFile(path);
    }
    @Test
    public  void addTests(){
        WordCreate wordCreate = WordCreate.builder().
                                        addHeader("页眉").
                                        addTitle(1, "测试word创建");
        for (int i = 0; i < 100; i++) {
            wordCreate.createParagraph().
                    addText("段落创建"+i);
               
        }
        wordCreate.addFooter("页脚");
        wordCreate.toWordFile(path);
    }
    
    @Test
    public  void creatTable(){
        WordCreate.builder().addTitle(1,"测试word创建").
                createTable(4,4).
                addCellText(1,1,"1,1").
                addCellText(1,2,"1,2").
                addCellText(1,3,"1,3").
                addCellText(1,4,"1,4").
                addCellText(2,1,"2,1").
                addCellText(2,3,"2,3").
                mergeCellsHorizontal(2,1,2).
                mergeCellsVertically(1,2,3).
                mergeCellsVertically(2,3,4).
                toWordFile(path);
    }
}
