package com.terry.archer.utils;

/**
 * Created by Administrator
 * on 2019/7/28.
 */
public class StringUtil {

    public static final String GET = "get";

    /**
     * 将字符串首字母转大写
     * @param str
     * @return
     */
    public static String toUpcaseFirstChar(String str) {
        char[] chars = str.toCharArray();
        if (97 <= chars[0] && chars[0] <= 122) {
            chars[0] -= 32;
        }
        return new String(chars);
    }

    /**
     * 将字符串首字母转小写
     * @param str
     * @return
     */
    public static String toLowcaseFirstChar(String str) {
        char[] chars = str.toCharArray();
        if (65 <= chars[0] && chars[0] <= 90) {
            chars[0] += 32;
        }
        return new String(chars);
    }

    /**
     * 通过属性名字获取该属性的get方法
     * @param attrName
     * @return
     */
    public static String getMethodByAttr(String attrName) {
        return GET + toUpcaseFirstChar(attrName);
    }
}
