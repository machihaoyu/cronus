package com.fjs.cronus.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xdj on 2017/4/19.
 */
public class StringAsciiUtil {

    /**
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        if (isContainChinese(value)) return value;
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    private static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

}
