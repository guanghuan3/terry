package com.terry.archer.excel.enums;

/**
 * excel支持的操作执行类型
 * Created by Administrator
 * on 2019/8/1.
 */
public enum ExcelAction {

    /**
     * 导入导出都支持
     */
    ALL,
    /**
     * 只支持导入操作
     */
    IMPORT,
    /**
     * 值支持导出操作
     */
    EXPORT;
}
