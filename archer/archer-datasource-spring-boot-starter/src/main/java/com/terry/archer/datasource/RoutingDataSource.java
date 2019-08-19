package com.terry.archer.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * 支持主从多数据库的路由数据源
 */
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 默认的数据源名称
     */
    private String defaultDatasourceName;

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.DATASOURCE_NAMES.get();
    }

    /**
     * 重置数据源为默认数据源
     */
    public void resetDatasource() {
        log.info("Datasource is reset to default.");
        DataSourceHolder.DATASOURCE_NAMES.remove();
    }

    /**
     * 切换数据源到指定的数据源
     * @param datasourceName
     */
    public void switchDatasource(String datasourceName) {
        log.info("Switch datasource to [{}]", new Object[]{datasourceName});
        DataSourceHolder.DATASOURCE_NAMES.set(datasourceName);
    }

    public String getDefaultDatasourceName() {
        return defaultDatasourceName;
    }

    public void setDefaultDatasourceName(String defaultDatasourceName) {
        this.defaultDatasourceName = defaultDatasourceName;
    }

    private static class DataSourceHolder {
        private static ThreadLocal<String> DATASOURCE_NAMES = new ThreadLocal<>();
    }
}
