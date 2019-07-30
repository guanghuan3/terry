package com.terry.archer.config;

import com.terry.archer.utils.ApplicationContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator
 * on 2019/7/29.
 */
@Configuration
public class UtilConfiguration {

    @Bean
    public ApplicationContextUtil applicationContextUtil(){
        return new ApplicationContextUtil();
    }
}
