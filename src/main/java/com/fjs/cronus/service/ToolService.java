package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.PhoneAreaDTO;
import com.fjs.cronus.dto.cronus.PhoneResultDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.DEC3Util;
import com.fjs.cronus.util.HttpsClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by msi on 2017/10/12.
 */
@Service
public class ToolService {



    @Autowired
    CustomerInfoMapper customerInfoMapper;

    @Value("${phone.phoneArea.url}")
    private String phoneAreaUrl;

    public CronusDto getPhineArea(Integer customerId){
        CronusDto cronusDto = new CronusDto();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",customerId);
        CustomerInfo customerInfo= customerInfoMapper.findByFeild(paramMap);
        if(customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERUNFIND_ERROR);
        }
        String telephoneNumber = customerInfo.getTelephonenumber();
        //TODO 手机号加密解密
        String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
        String url = phoneAreaUrl + telephone;
        String resultStr = HttpsClientUtil.sendHttps(url);
        PhoneAreaDTO phoneAreaDTO = new PhoneAreaDTO();
        JSONObject jsonObject = JSONObject.parseObject(resultStr);
        PhoneResultDTO resultDTO = jsonObject.toJavaObject(PhoneResultDTO.class);

        JSONObject json = (JSONObject) resultDTO.getRetData();
        phoneAreaDTO = json.toJavaObject(PhoneAreaDTO.class);
        cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        cronusDto.setResult(ResultResource.CODE_SUCCESS);
        cronusDto.setData(phoneAreaDTO);
        return  cronusDto;
    }
}
