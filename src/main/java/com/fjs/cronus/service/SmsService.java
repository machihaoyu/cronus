package com.fjs.cronus.service;

import com.fjs.cronus.util.SmsUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 */
@Service
public class SmsService {

    @Resource
    SmsUtils smsUtils;

    /**
     * 自动分配发送短信
     *
     * @param sendPhone
     * @param customerName
     * @return
     */
    public String sendSmsForAutoAllocate(String sendPhone, String customerName) {
        String smsContent = "房金所为您分配了客户名："+customerName+"，请注意跟进。";
        String smsResult = smsUtils.sendBatchMessage(sendPhone, smsContent);
        return smsResult;
    }

    public String sendNonCommunicate(String customerName,String telephonenumber)
    {
        String smsContent = "系统将新客户分配给了你,姓名:"+customerName+",请注意跟进(15分钟内未添加沟通日志，客户自动划走)";
        String smsResult = smsUtils.sendBatchMessage(telephonenumber, smsContent);
        return smsResult;
    }

    public String sendCRMAssistant(String telephonenumber)
    {
        String smsContent = "自动分配名额已满，请及时增加分配名额。";
        String smsResult = smsUtils.sendBatchMessage(telephonenumber, smsContent);
        return smsResult;
    }

}
