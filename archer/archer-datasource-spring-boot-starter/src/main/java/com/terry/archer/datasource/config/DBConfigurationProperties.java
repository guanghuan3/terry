package com.terry.archer.datasource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 数据源配置属性实体类
 */
@ConfigurationProperties(prefix = "archer.datasource")
@Configuration
public class DBConfigurationProperties {

    /**
     * Default datasource name specified
     */
    private String defaultName;

    /**
     * Multi DataSource configuration, format is 'datasourceName=DBPoolConfig'
     */
    private Map<String, DBPoolConfig> dynamic;

    /**
     * Whether use mutil datasource
     */
    private Boolean enable = false;

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Map<String, DBPoolConfig> getDynamic() {
        return dynamic;
    }

    public void setDynamic(Map<String, DBPoolConfig> dynamic) {
        this.dynamic = dynamic;
    }
}
