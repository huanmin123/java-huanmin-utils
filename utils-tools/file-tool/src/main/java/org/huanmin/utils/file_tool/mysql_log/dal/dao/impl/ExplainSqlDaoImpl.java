package org.huanmin.utils.file_tool.mysql_log.dal.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.huanmin.utils.dynamic_datasource.aop.DBSwitch;
import org.huanmin.utils.file_tool.mysql_log.dal.dao.ExplainSqlDao;
import org.huanmin.utils.file_tool.mysql_log.dal.mapper.ExplainSqlMapper;
import org.huanmin.utils.file_tool.mysql_log.entity.EplainEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author huanmin
 * @date 2024/1/10
 */
@Repository
@Slf4j
@DBSwitch("master")
public class ExplainSqlDaoImpl  extends ServiceImpl<ExplainSqlMapper, EplainEntity> implements ExplainSqlDao {
    @Autowired
    private  ExplainSqlMapper explainSqlMapper;

    @Override
    public List<EplainEntity> explain(String sql) {
        List<EplainEntity> explain = explainSqlMapper.explain(sql);
        return   Optional.ofNullable(explain).orElseThrow(()->new RuntimeException("不是mysql数据库,无法解析执行计划"));
    }
}
