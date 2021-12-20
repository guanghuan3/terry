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
        Random rand = new Random();
        String number = String.valueOf(rand.nextInt(randRadio));

        if (number.length() < len) {
            StringBuffer sb = new StringBuffer(number);
            int scale = len - number.length();
            // 补零
            for (int i = 0; i < scale; i ++) {
                sb.insert(0, 0);
            }
            return sb.toString();
        } else {
            return number.substring(0, len);
        }
    }

}
