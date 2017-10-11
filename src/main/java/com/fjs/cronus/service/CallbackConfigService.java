package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONArray;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.CronusApplication;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CallbackConfigDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CallbackConfigMapper;
import com.fjs.cronus.model.CallbackConfig;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.util.FastJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msi on 2017/10/11.
 */
@Service
public class CallbackConfigService {

    @Autowired
    CallbackConfigMapper callbackConfigMapper;

    @Autowired
    CronusRedisService cronusRedisService;
    public CronusDto editCallbackConfig(){
        CronusDto resultDto = new CronusDto();
        //从缓存中查询配置信息
        List<CallbackConfigDTO> resultList = new ArrayList<>();
        List<CallbackConfigDTO> redisList = cronusRedisService.getRedisCronusInfo(ResultResource.CALLBACKCONFIG_KEY);
        if (resultList != null && resultList.size() >0 ){
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(redisList);
            return  resultDto;
        }
        //从库中查询
        List<CallbackConfig> callbackConfigs = callbackConfigMapper.selectAll();
        if (callbackConfigs  == null || callbackConfigs.size() == 0){
            throw new CronusException(CronusException.Type.CRM_CALLBACK_CONFIG_ERROR);
        }
        for (CallbackConfig callbackConfig : callbackConfigs) {

            CallbackConfigDTO callbackConfigDto = new CallbackConfigDTO();
            callbackConfigDto.setConfId(callbackConfig.getConfId());
            callbackConfigDto.setCycle(callbackConfig.getCycle());
            callbackConfigDto.setQuestion(FastJsonUtils.stringToJsonArray(callbackConfig.getQuestion()));
            resultList.add(callbackConfigDto);
        }
        //存入缓存
        cronusRedisService.setRedisCronusInfo(ResultResource.CALLBACKCONFIG_KEY,resultList);


        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData(resultList);
        return  resultDto;


    }

    public CronusDto editCallbackConfigOk(){
        CronusDto resultDto = new CronusDto();
        return  resultDto;
    }
}
