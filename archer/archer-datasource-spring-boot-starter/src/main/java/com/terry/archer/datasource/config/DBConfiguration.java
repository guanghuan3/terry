package com.terry.archer.datasource.config;

import com.terry.archer.datasource.RoutingDataSource;
import com.terry.archer.datasource.annotation.DSAspect;
import com.terry.archer.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 多数据源配置类
 * 在配置项archer.datasource.enable=true时生效
 */
@Slf4j
@ConditionalOnProperty(prefix = "archer.datasource", name = "enable", havingValue = "true")
@Configuration
public class DBConfiguration {

    @Primary
    @Bean
    public DataSource dataSource(DBConfigurationProperties dbConfigurationProperties) {
        // 必须设置默认数据源名称
        if (CommonUtil.isNotEmpty(dbConfigurationProperties)
                && CommonUtil.isNotEmpty(dbConfigurationProperties.getDefaultName())) {

            // 创建动态路由数据源
            DataSource dataSource = createDataSource(dbConfigurationProperties);

            log.info("初始化动态数据源连接池完成.");
            return dataSource;
        }
        // 没有设置默认数据源  抛出异常
        else {
            throw new IllegalArgumentException("默认数据源不能为空。请指定目标数据源的默认数据源名称");
        }
    }

    private DataSource createDataSource(DBConfigurationProperties dbConfigurationProperties) {
        RoutingDataSource rdb = new RoutingDataSource();

        // 配置的默认数据源名称
        String defaultDataSourceName = dbConfigurationProperties.getDefaultName();
        // 目标多数据源集合
        Map<Object, Object> targetDataSource = new HashMap<>();

        // 多数据源的配置属性集合
        Map<String, DBPoolConfig> dynamic = dbConfigurationProperties.getDynamic();
        if (CommonUtil.isNotEmpty(dynamic)) {
            // 根据集合中的数据源配置属性逐个初始化数据源
            Set<String> dsNames = dynamic.keySet();
            // 数据库连接/连接池配置实例引用
            DBPoolConfig dbPoolConfig = null;
            // 数据库连接/连接池属性集合
            Map<String, Object> properties = null;
            String poolClassType = null;

            for (String dsName : dsNames) {
                // 动态数据源信息
                dbPoolConfig = dynamic.get(dsName);
                // 连接池类型
                poolClassType = dbPoolConfig.getPoolClassType();
                // 连接池属性
                properties = dbPoolConfig.getProperties();

                // 根据指定的数据源类型进行初始化
                if (CommonUtil.isNotEmpty(poolClassType)) {
                    // 根据数据源类型创建数据源连接
                    createSpecifiedDataSource(properties, dsName, targetDataSource, poolClassType);
                } else {
                    // 没有设置数据源连接池类型，则创建默认基础数据源连接
                    createDefaultDataSource(properties, dsName, targetDataSource);
                }

                // 数据源名称为默认的数据源名称时，设置为默认数据源
                if (dsName.equalsIgnoreCase(defaultDataSourceName)) {
                    rdb.setDefaultTargetDataSource(targetDataSource.get(dsName));
                    rdb.setDefaultDatasourceName(defaultDataSourceName);
                }
            }

            // 装配所有的目标数据源
            rdb.setTargetDataSources(targetDataSource);
        }
        // 没有设置数据源连接属性
        else {
            throw new IllegalArgumentException("请设置数据源连接信息，至少提供url,password,username,driverClassName四项属性");
        }

        return rdb;
    }

    private void createSpecifiedDataSource(Map<String, Object> properties, String dsName, Map<Object, Object> targetDataSource, String poolClassType) {
        try {
            // 根据指定的数据库连接池类型初始化数据库连接
            Class<DataSource> clazz = (Class<DataSource>) Class.forName(poolClassType);
            DataSource dataSource = clazz.newInstance();

            if (CommonUtil.isNotEmpty(properties)) {
                // 将properties中的key-value一一绑定到dataSource实例对应的属性上
                MapConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
                Binder binder = new Binder(source);
                binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(dataSource));
            }
            targetDataSource.put(dsName, dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建基础的数据源
     * @param properties
     * @param dsName
     * @param targetDataSource
     */
    private void createDefaultDataSource(Map<String, Object> properties, String dsName, Map<Object, Object> targetDataSource) {
        String username = String.valueOf(properties.get("username"));
        String password = String.valueOf(properties.get("password"));
        String url = String.valueOf(properties.get("url"));
        String driverClassName = String.valueOf(properties.get("driverClassName"));
        if (CommonUtil.isEmpty(username)
                || CommonUtil.isEmpty(password)
                || CommonUtil.isEmpty(url)
                || CommonUtil.isEmpty(driverClassName)) {
            throw new IllegalArgumentException("动态数据源[" + dsName + "]连接属性不能为空。请设置数据源url, password, username, driverClassName等基础属性");
        }

        // 没有指定连接池，则创建默认的数据库连接
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .url(url)
                .build();

        targetDataSource.put(dsName, dataSource);
    }

    /**
     * 对@DS注解操作的切面
     * @return
     */
    @Bean
    public DSAspect dSAspect() {
        return new DSAspect();
    }
}
