package org.huanmin.utils.file_tool.mysql_log.service;

import org.huanmin.utils.file_tool.mysql_log.dal.dao.ExplainSqlDao;
import org.huanmin.utils.file_tool.mysql_log.entity.EplainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huanmin
 * @date 2024/1/10
 */
@Service
public class ExplainSqlServiceImpl  implements ExplainSqlService{
    @Autowired
    private ExplainSqlDao explainSqlDao;
    @Override
    public List<EplainEntity> explain(String sql) {
        return explainSqlDao.explain(sql);
    }
}
