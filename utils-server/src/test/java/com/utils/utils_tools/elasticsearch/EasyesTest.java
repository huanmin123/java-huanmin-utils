package com.utils.utils_tools.elasticsearch;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.easyes.core.conditions.LambdaEsUpdateWrapper;
import cn.easyes.core.toolkit.EsWrappers;

import com.utils.server.WebApplication;
import com.utils.entity.Document;
import com.utils.utils_tools.elasticsearch.mapper.DocumentMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/18 19:00
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebApplication.class})
public class EasyesTest {
    @Resource
    private DocumentMapper documentMapper;

    @Test
    public void testInsert() {
        // 测试插入数据
        Document document = new Document();
        document.setId(1L);
        document.setTitle("huanmin");
        document.setContent("es=========");
        document.setCreator("huanmin");
        document.setGmtCreate(new Date());
        int successCount = documentMapper.insert(document);
        System.out.println(successCount);
    }

    @Test
    public void query() {
        LambdaEsQueryWrapper<Document> eq = EsWrappers.lambdaQuery(Document.class).eq(Document::getId, 1L);
        List<Document> documents = documentMapper.selectList(eq);
        System.out.println(documents);
    }

    @Test
    public void update() {

        Document document = documentMapper.selectById(1L);
        document.setTitle("huanmin12222");
        LambdaEsUpdateWrapper<Document> updateWrapper =
                EsWrappers.lambdaUpdate(Document.class).
                        eq(Document::getId, 1L);

    }
}
