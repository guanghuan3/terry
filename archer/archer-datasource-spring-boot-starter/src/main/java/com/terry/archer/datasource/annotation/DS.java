package com.terry.archer.datasource.annotation;

import java.lang.annotation.*;

@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DS {

    /**
     * 数据源名称
     * @return
     */
    String value();

}
