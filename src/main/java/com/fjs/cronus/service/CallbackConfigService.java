package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.CronusApplication;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CallbackConfigDTO;
import com.fjs.cronus.dto.cronus.CallbackConfigList;
import com.fjs.cronus.dto.cronus.QuestionsDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CallbackConfigMapper;
import com.fjs.cronus.model.CallbackConfig;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.FastJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
import java.util.*;

/**
 * Created by msi on 2017/10/11.
 */
@Service
public class CallbackConfigService {

    @Autowired
    CallbackConfigMapper callbackConfigMapper;

    @Autowired
    CronusRedisService cronusRedisService;
    @Autowired
    UcService ucService;

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
            //去除question
            JSONArray jsonArray = FastJsonUtils.stringToJsonArray(callbackConfig.getQuestion());
            //遍历
            List<QuestionsDTO>  questionsDTOS = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++){
                QuestionsDTO questionsDTO = new QuestionsDTO();
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                questionsDTO.setName(jsonObject.getString("name"));
                questionsDTO.setAnswer(jsonObject.getString("answer"));
                questionsDTOS.add(questionsDTO);
            }
            callbackConfigDto.setQuestion(questionsDTOS);
            resultList.add(callbackConfigDto);
        }
        //存入缓存
        cronusRedisService.setRedisCronusInfo(ResultResource.CALLBACKCONFIG_KEY,resultList);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData(resultList);
        return  resultDto;


    }

    @Transactional
    public CronusDto editCallbackConfigOk(CallbackConfigList callbackConfigList,String token){
        CronusDto resultDto = new CronusDto();
        //取出实体
        List<CallbackConfigDTO> callbackConfigDTOS = callbackConfigList.getCallbackConfigDTOS();
        Date date = new Date();
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
           throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        if (callbackConfigDTOS != null && callbackConfigDTOS.size() > 0){
            for (CallbackConfigDTO callbackConfigDTO : callbackConfigDTOS) {
                //根据id查询到相关配置
                 Map<String,Object> paramsMap = new HashMap<>();
                 paramsMap.put("confId",callbackConfigDTO.getConfId());
                 CallbackConfig callbackConfig  = callbackConfigMapper.findByFeild(paramsMap);
                 if (callbackConfig == null){
                     throw new CronusException(CronusException.Type.CRM_CALLBAXKCONFIG_ERROR);
                 }
                 //开始更新
                callbackConfig.setCycle(callbackConfigDTO.getCycle());
                List<QuestionsDTO> questionsDTOS = callbackConfigDTO.getQuestion();
                //转json
                JSONArray jsonArray = new JSONArray();
                for (QuestionsDTO questionsDTO : questionsDTOS){
                    jsonArray.add(questionsDTO);
                }
                callbackConfig.setQuestion(jsonArray.toString());
                callbackConfig.setLastUpdateUser(user_id);
                callbackConfig.setLastUpdateTime(date);
                callbackConfigMapper.updateCallbackConfig(callbackConfig);
            }
        }
        //更新redis缓存
        List<CallbackConfig> callbackConfigs = callbackConfigMapper.findAll();
        saveRedis(callbackConfigs);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return  resultDto;
    }

    public void saveRedis (List<CallbackConfig> callbackConfigs){
        List<CallbackConfigDTO> resultList = new ArrayList<>();

        if (callbackConfigs  == null || callbackConfigs.size() == 0){
            throw new CronusException(CronusException.Type.CRM_CALLBACK_CONFIG_ERROR);
        }
        for (CallbackConfig callbackConfig : callbackConfigs) {

            CallbackConfigDTO callbackConfigDto = new CallbackConfigDTO();
            callbackConfigDto.setConfId(callbackConfig.getConfId());
            callbackConfigDto.setCycle(callbackConfig.getCycle());
            //去除question
            JSONArray jsonArray = FastJsonUtils.stringToJsonArray(callbackConfig.getQuestion());
            //遍历
            List<QuestionsDTO>  questionsDTOS = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++){
                QuestionsDTO questionsDTO = new QuestionsDTO();
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                questionsDTO.setName(jsonObject.getString("name"));
                questionsDTO.setAnswer(jsonObject.getString("answer"));
                questionsDTOS.add(questionsDTO);
            }
            callbackConfigDto.setQuestion(questionsDTOS);
            resultList.add(callbackConfigDto);
        }
        //存入缓存
        cronusRedisService.setRedisCronusInfo(ResultResource.CALLBACKCONFIG_KEY,resultList);

    }
}
