package com.terry.archer.utils;

import java.util.Random;

public class NumberUtil {

    /**
     * 获取补零后的随机数的字符串
     * @param len
     * @param randRadio
     * @return
     */
    public static String getRandNumStr(int len, int randRadio) {
        return getRandNumStr(len, randRadio, true);
    }

    /**
     * 获取补零后的随机数的字符串
     * @param len       最大长度
     * @param randRadio 随机数字取值范围
     * @param fillZero  是否补零，true是，false否
     * @return
     */
    public static String getRandNumStr(int len, int randRadio, boolean fillZero) {
        Random rand = new Random();
        String number = String.valueOf(rand.nextInt(randRadio));

        if (number.length() < len && fillZero) {
            StringBuffer sb = new StringBuffer(number);
            int scale = len - number.length();
            // 补零
            for (int i = 0; i < scale; i ++) {
                sb.insert(0, 0);
            }
            // 补零后的数字
            return sb.toString();
        } else if (number.length() > len) {
            // 超过长度后截取的数字
            return number.substring(0, len);
        } else {
            // 直接返回内容
            return number;
        }
    }

}
