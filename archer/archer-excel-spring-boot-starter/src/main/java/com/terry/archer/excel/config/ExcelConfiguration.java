package com.terry.archer.excel.config;

import com.terry.archer.excel.format.FieldFormat;
import com.terry.archer.excel.format.impl.DateFieldFormat;
import com.terry.archer.excel.format.impl.StringFieldFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
@Configuration
public class ExcelConfiguration {

    /**
     * 默认的字段格式化输出处理，以字符串String输出
     * @return
     */
    @Primary
    @Bean
    public FieldFormat stringFieldFormat() {
        return new StringFieldFormat();
    }

    /**
     * 默认的日期输出处理
     * @return
     */
    @Primary
    @Bean
    public FieldFormat dateFieldFormat() {
        return new DateFieldFormat();
    }

}
