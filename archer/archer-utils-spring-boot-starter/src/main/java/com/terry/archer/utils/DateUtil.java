package com.terry.archer.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
@Slf4j
public class DateUtil {

    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:dd";

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    public DateUtil(String datePatterns) {
        if (CommonUtil.isNotEmpty(datePatterns)) {
            String[] patterns = datePatterns.split(",");
            // 分割配置的日期格式，并创建格式化类
            for (String p : patterns) {
                try {
                    DateFormatHolder.dateFormatMap.putIfAbsent(p, new SimpleDateFormat(p));
                } catch (Exception e) {
                    log.info("创建日期格式发生异常！错误的格式：[{}]", new Object[]{p});
                }
            }
        }
    }

    /**
     * 将日期对象转换成指定格式的字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String formattedDate(Date date, String pattern) {
        if (CommonUtil.isNull(date) || !CommonUtil.isNotEmpty(pattern)) {
            log.info("日期为：{}, 格式为：{}", new Object[]{date, pattern});
            throw new IllegalArgumentException("参数错误：date和pattern无效！");
        }
        try {
            // 从常用的日期格式中获取，如果不在常用格式中，视为临时创建格式
            if (CommonUtil.isNotEmpty(DateFormatHolder.dateFormatMap.get(pattern))) {
                return DateFormatHolder.dateFormatMap.get(pattern).format(date);
            } else {
                return new SimpleDateFormat(pattern).format(date);
            }
        } catch (Exception e) {
            log.info("日期格式转换错误！日期数据[{}]，目标格式[{}]", new Object[]{date, pattern});
            return null;
        }
    }

    /**
     * 获取一个格式化的日期对象
     * @param dateSource
     * @param pattern
     * @return
     */
    public static Date dateFormat(String dateSource, String pattern) {
        if (!CommonUtil.isNotEmpty(dateSource) || !CommonUtil.isNotEmpty(pattern)) {
            log.info("日期内容为：{}, 格式为：{}", new Object[]{dateSource, pattern});
            throw new IllegalArgumentException("参数错误：date和pattern无效！");
        }
        try {
            // 从常用的日期格式中获取，如果不再常用格式中，视为临时创建格式
            if (CommonUtil.isNotEmpty(DateFormatHolder.dateFormatMap.get(pattern))) {
                return DateFormatHolder.dateFormatMap.get(pattern).parse(dateSource);
            } else {
                return new SimpleDateFormat(pattern).parse(dateSource);
            }
        } catch (ParseException e) {
            log.info("日期格式转换错误！源日期数据[{}]，目标格式[{}]", new Object[]{dateSource, pattern});
            return null;
        }
    }

    public static String getFormattedDate() {
        return getFormattedDate(new Date());
    }

    public static String getFormattedDate(Date date) {
        return formattedDate(date, DEFAULT_DATE_FORMAT_PATTERN);
    }

    public static Date getDateFormat(String dateSource) {
        return dateFormat(dateSource, DEFAULT_DATE_FORMAT_PATTERN);
    }

    public static String getFormattedTime() {
        return getFormattedTime(new Date());
    }

    public static String getFormattedTime(Date date) {
        return formattedDate(date, DEFAULT_TIME_FORMAT_PATTERN);
    }

    public static Date getTimeFormat(String timeSource) {
        return dateFormat(timeSource, DEFAULT_TIME_FORMAT_PATTERN);
    }

    /**
     * 默认格式化持有类
     */
    private static class DateFormatHolder {
        /**
         * 默认内置的两个日期/时间格式化
         */
        private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);
        private static final SimpleDateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat(DEFAULT_TIME_FORMAT_PATTERN);
        /**
         * 时期格式化集合
         */
        private static Map<String, SimpleDateFormat> dateFormatMap = new ConcurrentHashMap<>();

        static {
            dateFormatMap.put(DEFAULT_DATE_FORMAT_PATTERN, DEFAULT_DATE_FORMAT);
            dateFormatMap.put(DEFAULT_TIME_FORMAT_PATTERN, DEFAULT_TIME_FORMAT);
        }
    }
}
