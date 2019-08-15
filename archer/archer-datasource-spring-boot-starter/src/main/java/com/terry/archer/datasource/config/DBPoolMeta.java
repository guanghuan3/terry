package com.terry.archer.datasource.config;


import java.util.Map;

/**
 * 自定义数据源连接池元数据属性实体类，用来初始化自定义设置的数据源连接池
 */
public class DBPoolMeta {

    /**
     * 自定义数据源连接池类全名，配合poolMeta进行连接池的初始化
     */
    private String poolClassName;

    /**
     * 数据库连接池属性配置集合，通过结合DBPoolHandler实现类进行自定义初始化连接池
     */
    private Map<String, Object> poolMeta;

    public Map<String, Object> getPoolMeta() {
        return poolMeta;
    }

    public void setPoolMeta(Map<String, Object> poolMeta) {
        this.poolMeta = poolMeta;
    }

    public String getPoolClassName() {
        return poolClassName;
    }

    public void setPoolClassName(String poolClassName) {
        this.poolClassName = poolClassName;
    }
}
