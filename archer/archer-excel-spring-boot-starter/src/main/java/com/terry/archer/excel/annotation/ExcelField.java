package com.terry.archer.excel.annotation;

import com.terry.archer.excel.format.FieldFormat;
import com.terry.archer.excel.format.impl.StringFieldFormat;

import java.lang.annotation.*;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelField {

    String attr();

    String label() default "";

    short sort() default 99;

    String pattern() default "";

    Class format() default StringFieldFormat.class;
}
