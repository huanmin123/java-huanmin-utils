package org.huanmin.utils.file_tool.mysql_log.service;

import org.huanmin.utils.file_tool.mysql_log.entity.EplainEntity;

import java.util.List;

/**
 * @author huanmin
 * @date 2024/1/10
 */
public interface ExplainSqlService {
    List<EplainEntity> explain(String sql);
}
