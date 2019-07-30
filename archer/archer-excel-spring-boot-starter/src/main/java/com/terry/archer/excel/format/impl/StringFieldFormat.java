package com.terry.archer.excel.format.impl;

import com.terry.archer.excel.format.FieldFormat;
import com.terry.archer.utils.CommonUtil;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
public class StringFieldFormat implements FieldFormat {
    @Override
    public String format(Object target, String pattern) {
        return CommonUtil.isNotEmpty(target) ? target.toString() : "";
    }
}
