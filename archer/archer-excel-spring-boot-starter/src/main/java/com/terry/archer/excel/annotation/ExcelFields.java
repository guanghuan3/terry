package com.terry.archer.excel.annotation;

import java.lang.annotation.*;
import java.util.List;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelFields {

    ExcelField[] fields();

    String fileName() default "";

    String sheetName() default "";

}
