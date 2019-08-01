package com.terry.archer.excel.format.impl;

import com.terry.archer.excel.format.FieldFormat;
import com.terry.archer.utils.CommonUtil;
import com.terry.archer.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理时间格式的输出
 * Created by Administrator
 * on 2019/7/28.
 */
public class DateFieldFormat implements FieldFormat {
    @Override
    public String format(Object target, String pattern) {
        if (CommonUtil.isNotEmpty(target)) {
            // date类型，通过日期格式转换输出
            if (target instanceof Date) {
                if (!CommonUtil.isNotEmpty(pattern)) {
                    return DateUtil.formattedDate((Date) target, DateUtil.DEFAULT_TIME_FORMAT_PATTERN);
                } else {
                    return DateUtil.formattedDate((Date) target, pattern);
                }
            } else {
                // 非日期格式，直接输出字符串
                return target.toString();
            }
        }
        // 如果target为空，则返回空字符串
        return "";
    }
}
