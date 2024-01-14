package org.huanmin.utils.dynamic_datasource.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 默认数据源切换
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Value("${spring.datasource.default-db-key}")
    private String defaultDbKey;

    @Override
    protected Object determineCurrentLookupKey() {
        String currentDb = DynamicDataSourceService.currentDb();
        if (currentDb == null) {
            return defaultDbKey;
        }
        return currentDb;
    }

}
