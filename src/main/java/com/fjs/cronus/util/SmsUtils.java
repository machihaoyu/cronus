package com.fjs.cronus.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsUtils {
    private static final String apikey = "9919fd3ed2e63c1fcfe2ac056f97e870";
    public static String url = "http://yunpian.com/v1/sms/send.json";//云片短信

    @Value("${sms.url}")
    public String url2;//建州短信

    @Value("${sms.key}")
    public String key;//建州短信

    public static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);

    /**
     * 提交普通短信
     *
     * @param phone
     * @return
     */
    public String sendBatchMessage(String phone, String content) {

        String status = null;
        SimpleDateFormat sdfv = new SimpleDateFormat("yyyyMMdd");
        try {
            status = sendSms2(phone, content);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return status;

    }

    public String sendSms(String msgTemplateFlag, String mobile, String ip, String source) throws Exception {
        System.out.println("------>发送一条短信!!");
        String smsContent = "【房金云】注册验证码XXXX，5分钟有效，请妥善保管，切勿泄露。";
        String sendStatus = null;
        Map<String, String> msgParamDto = new HashMap<String, String>();
        msgParamDto.put("apikey", apikey);
        msgParamDto.put("text", smsContent);
        msgParamDto.put("mobile", mobile);
	/*	HttpRequestVO httpRequestVO=new HttpRequestVO(url);
		httpRequestVO.setJsonEntityData(msgParamDto);*/
        String outStr = HttpUtil.post(url, msgParamDto);
        System.out.println(outStr);
        LinkedTreeMap resultMap = JSON.parseObject(outStr, LinkedTreeMap.class); //JSONAlibabaUtil.toBean(JSONAlibabaUtil.parseString(string), Map.class);
        if (Integer.parseInt(resultMap.get("code").toString()) == 0) {
            sendStatus = "success";
        }
        //sendStatus =0;
        return sendStatus;
    }

    public String sendSms2(String mobile, String smsContent) throws Exception {
        String sendStatus = null;
        System.out.println("------>发送一条短信!!");
        String requestUrl = url2 + "/api/v1/sendSms?msg=" + smsContent + "&mobile=" + mobile + "&access_token="+key;
        //HttpRequestVO httpRequestVO=new HttpRequestVO(url2);
        Map map = new HashMap();//拼接地址所以不用map穿传参数
        String httpResponseVO = HttpUtil.post(requestUrl, map);
        if (StringUtils.isEmpty(httpResponseVO)) {
            sendStatus = "success";
        }
        return sendStatus;
    }
}
