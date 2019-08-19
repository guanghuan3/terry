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

    /**
     * 是否需要支持事务
     * 默认为false不支持事务，事务绑定在默认的数据源
     * 为true时，@Transactional将针对value关联的数据源进行事务控制
     * @return
     */
//    boolean supportTransaction() default false;

}
