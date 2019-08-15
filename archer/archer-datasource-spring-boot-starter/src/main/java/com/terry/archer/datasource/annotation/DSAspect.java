package com.terry.archer.datasource.annotation;

import com.terry.archer.datasource.RoutingDataSource;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class DSAspect {

    /**
     * 执行业务之前切换到指定的数据源
     * @param ds
     */
    @Before("@annotation(ds)")
    public void getDataSourceBefore(DS ds) {
        RoutingDataSource.switchDatasource(ds.value());
    }

    /**
     * 切换数据源执行完业务之后切换回默认数据源
     * @param ds
     */
    @After("@annotation(ds)")
    public void getDataSourceAfter(DS ds) {
        RoutingDataSource.resetDatasource();
    }
}
