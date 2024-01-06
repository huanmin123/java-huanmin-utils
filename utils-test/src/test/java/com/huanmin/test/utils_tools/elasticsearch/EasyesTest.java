package com.huanmin.test.utils_tools.elasticsearch;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.easyes.core.conditions.LambdaEsUpdateWrapper;
import cn.easyes.core.toolkit.EsWrappers;
import com.huanmin.test.TestApplication;
import com.huanmin.test.dao.DocumentMapper;
import com.huanmin.test.entity.DocumentEntity;
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
@SpringBootTest(classes = {TestApplication.class})
public class EasyesTest {
    @Resource
    private DocumentMapper documentMapper;

    @Test
    public void testInsert() {
        // 测试插入数据
        DocumentEntity document = new DocumentEntity();
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
        LambdaEsQueryWrapper<DocumentEntity> eq = EsWrappers.lambdaQuery(DocumentEntity.class).eq(DocumentEntity::getId, 1L);
        List<DocumentEntity> documents = documentMapper.selectList(eq);
        System.out.println(documents);
    }

    @Test
    public void update() {

        DocumentEntity document = documentMapper.selectById(1L);
        document.setTitle("huanmin12222");
        LambdaEsUpdateWrapper<DocumentEntity> updateWrapper =
                EsWrappers.lambdaUpdate(DocumentEntity.class).
                        eq(DocumentEntity::getId, 1L);

    }
}
