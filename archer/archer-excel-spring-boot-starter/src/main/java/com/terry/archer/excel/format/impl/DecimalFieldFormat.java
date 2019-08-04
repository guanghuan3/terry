package com.terry.archer.excel.format.impl;

import com.terry.archer.excel.format.FieldFormat;
import com.terry.archer.utils.CommonUtil;

import java.math.BigDecimal;

/**
 * 处理数字格式的输出
 * 格式为0.小数位数，其中小数位数以#字符代替
 * Created by Administrator
 * on 2019/8/1.
 */
public class DecimalFieldFormat implements FieldFormat {

    public static final String SCALE_PATTERN = "(0)|(0\\.#+)";

    /**
     * 格式处理方式以#为小数点位占位符。小数点左边固定为0，小数点右边为小数位，小数位数以#字符个数为准
     * 例如：0.##，表示支持2位小数的数字
     * @param target
     * @param pattern
     * @return
     */
    @Override
    public String format(Object target, String pattern) {
        // 如果target为空则返回空字符
        if (CommonUtil.isNotEmpty(target)) {
            try {
                // 如果pattern不匹配0.##的格式，则返回字符串0
                if (CommonUtil.isNotEmpty(pattern) && pattern.matches(SCALE_PATTERN)) {
                    BigDecimal decimal = new BigDecimal(String.valueOf(target));
                    // 将target按照0.##格式进行处理
                    String[] patterns = pattern.split("\\.");
                    int scale = 0;
                    // 有小数点
                    if (patterns.length == 2) {
                        scale = patterns[1].length();
                    }
                    return decimal.setScale(scale, BigDecimal.ROUND_DOWN).toString();
                } else {
                    return target.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return target.toString();
            }

        } else {
            return "";
        }
    }
}
