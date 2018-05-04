package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.api.CommonApiDTO;
import com.fjs.cronus.entity.SMSMessage;
import com.fjs.cronus.mappers.PhoneMapper;
import com.fjs.cronus.service.client.HebeService;
import com.fjs.cronus.util.SmsUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Service
public class SmsService {

    @Resource
    SmsUtils smsUtils;

    @Value("${token.current}")
    private String currentToken;

    @Value("${sms.sysKey}")
    private String sysKey;

    @Value("${sms.switch}")
    private String smsSwitch;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private PhoneMapper phoneMapper;

    @Autowired
    private HebeService hebeService;

    public Integer sendSmsForAutoAllocate(String sendPhone, String customerName) {
        Integer smsResult = 0;
        if (smsChannelOpen()) {
            String smsContent = CommonConst.SMS_SIGN + "房金所为您分配了客户名：" + customerName + "，请注意跟进。";
            smsResult = sendHebeMessage(sendPhone,smsContent);
        }
        return smsResult;
    }

    public Integer sendNonCommunicate(String customerName,String telephoneNumber)
    {
        Integer smsResult = 0;
        if (smsChannelOpen()) {
            String smsContent = CommonConst.SMS_SIGN + "系统将新客户分配给了你,姓名:" + customerName + ",请注意跟进(15分钟内未添加沟通日志，客户自动划走)。";
            smsResult = sendHebeMessage(telephoneNumber,smsContent);
        }
        return smsResult;
    }

    public Integer sendCRMAssistant(String telephoneNumber)
    {
        Integer smsResult = 0;
        if (smsChannelOpen()) {
            Integer count = getPhoneCountToday(telephoneNumber);
            if (count == 0) {
                String smsContent = CommonConst.SMS_SIGN + "自动分配名额已满，请及时增加分配名额。";
                smsResult = sendHebeMessage(telephoneNumber,smsContent);
                if (smsResult.equals(0)) {
                    insertMessage(telephoneNumber, smsContent);
                }
            }
        }
        return smsResult;
    }

    public Integer sendCommunication(String telephoneNumber, String content) {
        Integer smsResult =0;
        if (smsChannelOpen()) {
            content = CommonConst.SMS_SIGN + content;
            smsResult = sendHebeMessage(telephoneNumber, content);
        }
        return smsResult;
    }

    /**
     * 0 发送成功
     * @param telephoneNumber
     * @param content
     * @return
     */
    private Integer sendHebeMessage(String telephoneNumber, String content) {
        SMSMessage smsMessage = new SMSMessage();
        List<String> phones = new ArrayList<>();
        phones.add(telephoneNumber);
        smsMessage.setKey(sysKey);
        smsMessage.setMessage(content);
        smsMessage.setMobile(phones);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(smsMessage));
        CommonApiDTO commonApiDTO = hebeService.sendMessage(currentToken, jsonObject);
        return commonApiDTO.getResult();
    }

    public Integer insertMessage(String phone, String content)
    {
        return phoneMapper.insertPhoneLog(phone,content,0);
    }

    public Integer getPhoneCountToday(String phone)
    {
        return phoneMapper.getPhoneCountToday(phone);
    }

    public boolean smsChannelOpen()
    {
        if (smsSwitch.equals("1")) {
            return true;
        }
        else return false;
    }
}
