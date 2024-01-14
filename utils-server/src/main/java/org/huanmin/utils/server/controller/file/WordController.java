package org.huanmin.utils.server.controller.file;

import org.huanmin.utils.common.file.ResourceFileUtil;
import org.huanmin.utils.file_tool.word.WordTemplateParse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;

@RestController
@RequestMapping("/word")
public class WordController {

  // http://172.29.204.221:7001/word/test

  /** PDF 文件导出 */
  @GetMapping(value = "/test")
  public void word(HttpServletResponse response) {

    // 数据
    TreeMap<String, String> data = new TreeMap<String, String>();
    data.put("yue1", "33");
    data.put("yue2", "22");
    data.put("yue3", "55");
    data.put("yue4", "12");
    data.put("yue5", "2");
    data.put("yue6", "151");
    data.put("yue7", "33");
    data.put("yue8", "34");
    data.put("yue9", "123");
    data.put("yue10", "15");
    data.put("yue11", "2");
    data.put("yue12", "10");
    // 加载模型【模型】
    String absolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("temp.docx"); // 模板位置

    WordTemplateParse.generateDown(
        data, absolutePath, "newtemp.docx", response, WordTemplateParse.WordType.docx);

  }
}
