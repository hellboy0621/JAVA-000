package com.transformers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("current datasourcce: " + DataSourceContextHolder.getDataBaseType());
        return DataSourceContextHolder.getDataBaseType();
    }
}
