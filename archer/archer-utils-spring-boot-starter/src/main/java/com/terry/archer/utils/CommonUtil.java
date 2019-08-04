package com.terry.archer.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
public class CommonUtil {

    public static Boolean isNull(Object obj) {
        return obj == null ? true : false;
    }

    /**
     * 判断对象是否不为空，不为空字符串，不为空集合，不为空数组，不为空映射
     * @param obj
     * @return true是非空，fasle是空
     */
    public static Boolean isNotEmpty(Object obj) {
        if (isNull(obj)) {
            return false;
        } else {
            if (obj instanceof String) {
                return ((String) obj).length() > 0;
            } else if (obj instanceof Object[]) {
                return ((Object[]) obj).length > 0;
            } else if (obj instanceof Collection) {
                return ((Collection) obj).size() > 0;
            } else if (obj instanceof Map) {
                return ((Map) obj).size() > 0;
            } else {
                return true;
            }
        }
    }

    public static boolean isEmpty(Object obj) {
        return !isNotEmpty(obj);
    }
}
