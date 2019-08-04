package com.terry.archer.excel.annotation;

import com.terry.archer.excel.enums.AlignType;
import com.terry.archer.excel.enums.ExcelAction;
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

    /**
     * 对象属性名/字段名
     * @return
     */
    String attr();

    /**
     * 输出到excel的表头字段名称
     * @return
     */
    String label() default "";

    /**
     * 排序字段
     * @return
     */
    short sort() default 99;

    /**
     * 单元格水平模式，默认靠左
     * @return
     */
    AlignType align() default AlignType.CENTER;

    /**
     * 数据输出格式
     * @return
     */
    String pattern() default "";

    /**
     * 数据格式化处理类，FieldFormat接口实现类
     * @return
     */
    Class<? extends FieldFormat> format() default StringFieldFormat.class;

    /**
     * 支持导入导出操作
     * @return
     */
    ExcelAction action() default ExcelAction.ALL;
}
