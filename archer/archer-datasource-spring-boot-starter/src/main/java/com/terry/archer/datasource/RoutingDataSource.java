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

    public void resetDatasource() {
        log.info("Datasource is reset to default.");
        DataSourceHolder.DATASOURCE_NAMES.remove();
    }

    public void switchDatasource(String datasourceName) {
        log.info("Switch datasource to [{}]", new Object[]{datasourceName});
        DataSourceHolder.DATASOURCE_NAMES.set(datasourceName);
    }

    /**
     * 切换默认的数据源，以此来控制事务关联的数据源
     * @param targetDataSourceName
     */
    public void switchDefaultDataSource(String targetDataSourceName) {
        // 支持事务，切换回默认的支持事务的数据源
        log.info("Default datasource has been changed to [{}].", new Object[]{targetDataSourceName});
        DataSourceHolder.DATASOURCE_NAMES.set(targetDataSourceName);
        setDefaultTargetDataSource(determineTargetDataSource());
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
