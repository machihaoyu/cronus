package com.fjs.cronus.service;

import com.fjs.cronus.model.SysConfig;
import com.fjs.cronus.util.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 */
@Service
public class SmsService {

    @Resource
    SmsUtils smsUtils;

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 自动分配发送短信
     *
     * @param sendPhone
     * @param customerName
     * @return
     */
    public String sendSmsForAutoAllocate(String sendPhone, String customerName) {
        String smsResult = "";
        SysConfig sysConfig = sysConfigService.getConfigByName("sysUse");
        if (sysConfig.getConValue().equals("1")) {
            String smsContent = "【房金云】房金所为您分配了客户名：" + customerName + "，请注意跟进。";
            smsResult = smsUtils.sendBatchMessage(sendPhone, smsContent);
        }
        return smsResult;
    }

    public String sendNonCommunicate(String customerName,String telephonenumber)
    {
        String smsResult = "";
        SysConfig sysConfig = sysConfigService.getConfigByName("sysUse");
        if (sysConfig.getConValue().equals("1")) {
            String smsContent = "【房金云】系统将新客户分配给了你,姓名:" + customerName + ",请注意跟进(15分钟内未添加沟通日志，客户自动划走)。";
            smsResult = smsUtils.sendBatchMessage(telephonenumber, smsContent);
        }
        return smsResult;
    }

    public String sendCRMAssistant(String telephoneNumber)
    {
        String smsResult = "";
        SysConfig sysConfig = sysConfigService.getConfigByName("sysUse");
        if (sysConfig.getConValue().equals("1")) {
            String smsContent = "【房金云】自动分配名额已满，请及时增加分配名额。";
            smsResult = smsUtils.sendBatchMessage(telephoneNumber, smsContent);
        }
        return smsResult;
    }
    //发送短信

    public  String sendCommunication(String telephoneNumber,String content)
    {
        String smsResult = "";
        content = "【房金云】" + content;
        smsResult = smsUtils.sendBatchMessage(telephoneNumber,content);
        return smsResult;
    }
}
