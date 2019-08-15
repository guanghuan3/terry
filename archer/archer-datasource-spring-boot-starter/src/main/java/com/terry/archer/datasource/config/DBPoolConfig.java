package com.terry.archer.datasource.config;


import java.util.Map;

/**
 * 自定义数据源连接池元数据属性实体类，用来初始化自定义设置的数据源连接池
 * 样例如下：
  archer:
    datasource:
      # 表示使用多数据源
      enable: true
      # 表示多数据源中使用名称为master的数据源作为默认数据源
      default-name: master
      dynamic:
        # 数据源名称为master，以及该数据源的相关属性
        master:
          # 使用HikariDataSource数据源连接池
          poolClassType: com.zaxxer.hikari.HikariDataSource
          properties:
            jdbcUrl: jdbc:mysql://ip1:port/dataBaseName?useSSL=false&useUnicode=true&characterEncoding=utf-8
            username: root
            password: 123456
            driverClassName: com.mysql.cj.jdbc.Driver
            ......
        # 另一个数据源，名称为slave
        slave:
          # 使用DruidDataSource作为数据源，属性与HikariDataSource有所不同
          poolClassType: com.alibaba.druid.pool.DruidDataSource
          properties:
            jdbcUrl: jdbc:mysql://ip2:port/dataBaseName?useSSL=false&useUnicode=true&characterEncoding=utf-8
            driverClass: com.mysql.cj.jdbc.Driver
            ......
        # 快速配置数据源，使用默认基础配置（极简），名称为default
        default:
          # 没有配置使用指定数据源类型，默认属性为如下四项
          properties:
            url: {数据库连接串}
            driverClassName:{数据库驱动类型}
            username:{数据库用户名}
            password:{数据库密码}
 */
public class DBPoolConfig {

    private String poolClassType;

    /**
     * 一个数据源/连接池的所有属性集合
     * 1、当设置了poolClassType时，表示为数据源连接池的配置属性，属性名字与指定的数据源连接池的配置属性需要一一对应，否则有可能不能正常运行
     * 2、当没有设置poolClassType时，表示为数据源直连（默认会使用HikariDataSource作为数据源连接池），只需要设置数据源连接的常用四项即可。四项为：url,username,password,driverClassName
     */
    private Map<String, Object> properties;

    public String getPoolClassType() {
        return poolClassType;
    }

    public void setPoolClassType(String poolClassType) {
        this.poolClassType = poolClassType;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
