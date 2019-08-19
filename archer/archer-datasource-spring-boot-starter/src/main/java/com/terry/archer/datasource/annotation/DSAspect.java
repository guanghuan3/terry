package com.terry.archer.datasource.annotation;

import com.terry.archer.datasource.RoutingDataSource;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

/**
 * 多数据源切换切面
 * 支持事务注解@Transactional，添加@Order，保证切换数据源的执行顺序在DataSourceTransactionManager之前执行
 */
@Order(value=10)
@Aspect
public class DSAspect {

    @Autowired
    private DataSource dataSource;

    /**
     * 执行业务之前切换到指定的数据源
     * @param ds
     */
    @Before("@annotation(ds)")
    public void getDataSourceBefore(DS ds) {
        ((RoutingDataSource)dataSource).switchDatasource(ds.value());
    }

    /**
     * 切换数据源执行完业务之后切换回默认数据源
     * @param ds
     */
    @After("@annotation(ds)")
    public void getDataSourceAfter(DS ds) {
        ((RoutingDataSource)dataSource).resetDatasource();
    }
}
