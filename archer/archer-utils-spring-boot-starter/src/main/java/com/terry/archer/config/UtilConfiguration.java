package com.terry.archer.config;

import com.terry.archer.utils.ApplicationContextUtil;
import com.terry.archer.utils.DateUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator
 * on 2019/7/29.
 */
@ConfigurationProperties(prefix = "archer.utils")
@Configuration
public class UtilConfiguration {

    private String datePatterns;

    @Bean
    public DateUtil dateUtil() {
        return new DateUtil(datePatterns);
    }

    @Bean
    public ApplicationContextUtil applicationContextUtil(){
        return new ApplicationContextUtil();
    }

    public String getDatePatterns() {
        return datePatterns;
    }

    public void setDatePatterns(String datePatterns) {
        this.datePatterns = datePatterns;
    }
}
