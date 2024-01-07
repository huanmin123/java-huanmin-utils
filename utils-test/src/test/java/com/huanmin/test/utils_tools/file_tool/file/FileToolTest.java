package com.huanmin.test.utils_tools.file_tool.file;

import com.utils.common.file.FileTool;
import com.utils.common.file.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileToolTest {
    @Test
    //截取文件指定行之后的内容一直到指定行的内容
    public  void cutFileLine() throws IOException {
    
        List<String> files = FileUtil.getFiles("D:\\公司文档\\华付公司\\中债\\SQL语句\\数据\\表结构\\");
        for (String file : files) {
            File file1 = new File(file);
            String parent = file1.getParent();
            File file2= new File(parent+"\\back" );
            if(!file2.exists()){
                file2.mkdirs();
            }
            File file3 = new File(parent+"\\back\\"+file1.getName());
            FileTool.fileCodingTransition(file1, file3, "UTF-8");//转换文件编码
            FileTool.cutFileLineToWrite(file3, file3, "prompt", "spool off");//截取文件内容
        }
    }
    


}
