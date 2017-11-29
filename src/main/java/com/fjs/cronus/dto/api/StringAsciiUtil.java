package com.fjs.cronus.dto.api;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
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

    public static Map<String, Object> fegionException(String exceptionMsg) {
        String destSrc = "content";
        String result = exceptionMsg.substring(exceptionMsg.indexOf(destSrc) + destSrc.length() + 1);
        String res = result.replaceAll("\\\n", "");
        FegionExceptionDTO fegionExceptionDTO = null;
        Map<String, Object> map = new HashMap<>();
        if (res.contains("error") && res.contains("error_description")) {
            fegionExceptionDTO = JSONObject.parseObject(res, FegionExceptionDTO.class);
            map.put("fegionExceptionDTO", fegionExceptionDTO);
        } else if (res.contains("timestamp") && res.contains("status")) {
            FegionResponseErrorDTO responseError = JSONObject.parseObject(res, FegionResponseErrorDTO.class);
            map.put("responseError", responseError);
        }
        return map;
    }

}
