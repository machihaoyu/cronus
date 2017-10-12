package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONArray;
import com.fjs.cronus.Common.CustomerEnum;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.*;
import com.fjs.cronus.dto.cronus.*;
import com.fjs.cronus.dto.cronus.CallbackLogDTO;
import com.fjs.cronus.dto.loan.LoanDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CallbackConfigMapper;
import com.fjs.cronus.mappers.CallbackLogMapper;
import com.fjs.cronus.mappers.CallbackPhoneLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.CallbackConfig;
import com.fjs.cronus.model.CallbackLog;
import com.fjs.cronus.model.CallbackPhoneLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.service.uc.LoaService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import com.fjs.cronus.util.FastJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.*;

/**
 * Created by msi on 2017/10/10.
 */
@Service
public class CallbackService {



    @Autowired
    CallbackPhoneLogMapper phoneLogMapper;
    @Autowired
    CronusRedisService cronusRedisService;
    @Autowired
    CallbackConfigMapper callbackConfigMapper;
    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    LoaService loaService;
    @Autowired
    UcService ucService;
    @Autowired
    CallbackPhoneLogMapper callbackPhoneLogMapper;
    @Autowired
    CallbackLogMapper callbackLogMapper;

    public QueryResult callbackCustomerList(String callback_user, String callback_start_time, String callback_end_time, String search_name,
                                            Integer type, String search_city, String search_telephone, String search_callback_status, Integer page, Integer size, Integer communication_order, String token){
        QueryResult resultDto = new QueryResult();
        //筛选回访人
        List  customerIdList = new ArrayList();
        Map<String,Object> paramsMap = new HashMap<>();
        //根据token查询到当前登录用户信息
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR);
        }
        if (type == null || "".equals(type)){
            throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
        }

        if (!StringUtils.isEmpty(callback_start_time)){
            Date startDate = DateUtils.parse(callback_start_time,DateUtils.FORMAT_LONG);
            paramsMap.put("createTimeStart",startDate);
        }
        if (!StringUtils.isEmpty(callback_end_time)){
            Date endDate = DateUtils.parse(callback_end_time,DateUtils.FORMAT_LONG);
            paramsMap.put("createTimeEnd",endDate);
        }
        if (!StringUtils.isEmpty(callback_user)){
            paramsMap.put("callback_user",callback_user);
        }
         if (paramsMap != null && paramsMap.size() > 0 ){
             //从phonelog中查询到customerId
             customerIdList = phoneLogMapper.getCustomerId(paramsMap);
             paramsMap.clear();
         }
         if (customerIdList != null  && customerIdList.size() > 0){
             paramsMap.put("customerIdList",customerIdList);
         }
         paramsMap.put("customer_type", CustomerEnum.getByValue(type).getName());

        List<String> cityList = new ArrayList<>();
         if (!StringUtils.isEmpty(search_city)){
             search_city="'" + search_city + "'";
             cityList.add(search_city);
             paramsMap.put("cityList",cityList);
         }else {
             cityList = Arrays.<String> asList(ResultResource.CITYS);
             paramsMap.put("cityList",cityList);
         }
         if (!StringUtils.isEmpty(search_name)){
            paramsMap.put("search_name",search_name);
          }
        if (!StringUtils.isEmpty(search_callback_status)){
            paramsMap.put("search_callback_status",search_callback_status);
        }
        if (!StringUtils.isEmpty(search_telephone)){
            List phonelist = new ArrayList();
            //TODO 手机号加密操作
            String encodePhone = "";
            phonelist.add(encodePhone);
            phonelist.add(search_telephone);
            paramsMap.put("phonelist",phonelist);
        }
       //计算分页
        if (communication_order == 1){
           //从未回访的
            paramsMap.put("communication_order",communication_order);
        }else if (communication_order == 2){
            //需要重新回访的,后去当前系统时间
            //从缓存中获取配置时间
            Integer configTime = getConfigTime(type);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR,-configTime * 30);
            Date searchTime = cal.getTime();
            //不为null
            paramsMap.put("searchTime",searchTime);
            paramsMap.put("communication_order",communication_order);
        }else if (communication_order == 3){
            Integer configTime = getConfigTime(type);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR,-configTime * 30);
            Date searchTime = cal.getTime();
            paramsMap.put("searchTime",searchTime);//大于
            paramsMap.put("callback_status","正常");
            paramsMap.put("communication_order",communication_order);
        }else {
            //默认所有需要回访的
            Integer configTime = getConfigTime(type);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR,-configTime * 30);
            Date searchTime = cal.getTime();
            paramsMap.put("searchTime",searchTime);//小与包括时间为null的

        }
        paramsMap.put("start",(page-1) * size);
        paramsMap.put("size",size);
        List<CustomerInfo> customerInfoList = customerInfoMapper.getListByWhere(paramsMap);
        Integer count = customerInfoMapper.getListByWhereCount(paramsMap);
        List<CallbackCustomerDTO> resultList = new ArrayList<>();
        //遍历
        for (CustomerInfo customerInfo : customerInfoList) {
            CallbackCustomerDTO callbackCustomerDTO = new CallbackCustomerDTO();
           LoanDTO dto  = loaService.selectByCustomerId(token,customerInfo.getId());
            if (dto == null){
                throw new CronusException(CronusException.Type.CRM_CUSTOMERLOAN_ERROR);
            }
            callbackCustomerDTO.setId(customerInfo.getId());
            callbackCustomerDTO.setCallbackStatus(customerInfo.getCallbackStatus());
            if (customerInfo.getCallbackTime() != null) {
                callbackCustomerDTO.setCallbackTime(customerInfo.getCallbackTime());
            }
            callbackCustomerDTO.setCity(customerInfo.getCity());
            if (customerInfo.getCreateTime() != null){
                callbackCustomerDTO.setCreateTime(customerInfo.getCreateTime());
            }
            String phoneNumber = customerInfo.getTelephonenumber().substring(0, 3) + "****" +customerInfo.getTelephonenumber().substring(7, customerInfo.getTelephonenumber().length());
            Integer communicationOrder = createOrderWhere(customerInfo);
            callbackCustomerDTO.setCommunicationOrder(communicationOrder);
            callbackCustomerDTO.setTelephonenumber(phoneNumber);
            callbackCustomerDTO.setCustomerLevel(customerInfo.getCustomerLevel());
            callbackCustomerDTO.setCustomerName(customerInfo.getCustomerName());
            callbackCustomerDTO.setLoanAmount(dto.getMindAmount());
            if (dto.getOwnUserId() != null) {
                callbackCustomerDTO.setOwnUserId(dto.getOwnUserId());
            }
            callbackCustomerDTO.setOwnUserName("无");
            callbackCustomerDTO.setSub_company("未知");
            if (dto.getOwnUserId() != null){

                 UcUserDTO ucUserDTO = ucService.getUserInfoByID(token,dto.getOwnUserId());
                callbackCustomerDTO.setOwnUserName(ucUserDTO.getName());
                callbackCustomerDTO.setSub_company(ucUserDTO.getSub_company_name());
            }
            resultList.add(callbackCustomerDTO);
        }
        resultDto.setRows(resultList);
        resultDto.setTotal(count.toString());
        return  resultDto;

    }

    public CronusDto editCallback(Integer customerId,String token){
        //根据id查询到所有相关信息
        CronusDto cronusDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",customerId);
        CallbackCusLoanDTO dto = findCustomerByWhere(paramsMap,token);
        dto.setRel_telephonenumber(dto.getTelephonenumber());
        dto.setTelephonenumber(dto.getTelephonenumber().substring(0, 3) + "****" + dto.getTelephonenumber().substring(7,dto.getTelephonenumber().length()));
        cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        cronusDto.setResult(ResultResource.CODE_SUCCESS);
        cronusDto.setData(dto);
        return  cronusDto;
    }

    public CronusDto getCalledRecordInclude(Integer customerId,String token){
        CronusDto cronusDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("customerId",customerId);

        List<String> statusList =  Arrays.<String> asList(ResultResource.CALLBACKSTATUS);
        paramsMap.put("statusList",statusList);
        List<CallbackPhoneLog> callbackPhoneLogList = callbackPhoneLogMapper.findByFeild(paramsMap);
        List<PhoneLogDTO> resultList = new ArrayList<>();
        if (callbackPhoneLogList != null && callbackPhoneLogList.size() > 0){
            for (CallbackPhoneLog callbackPhoneLog : callbackPhoneLogList) {
                PhoneLogDTO dto = new PhoneLogDTO();
                dto.setCustomerId(callbackPhoneLog.getCustomerId());
                dto.setCreateTime(callbackPhoneLog.getCreateTime());
                dto.setDescription(callbackPhoneLog.getDescription());
                dto.setId(callbackPhoneLog.getId());
                dto.setOperatUserId(callbackPhoneLog.getOperatUserId());
                dto.setOperatUserName(callbackPhoneLog.getOperatUserName());
                dto.setStatus(callbackPhoneLog.getStatus());
                resultList.add(dto);
            }
            cronusDto.setData(resultList);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            return cronusDto;
        }
        cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        cronusDto.setResult(ResultResource.CODE_SUCCESS);
        return cronusDto;

    }

    public CronusDto getOneQuestion(Integer id,String token){
        //根据id找到当前日志
        CronusDto resultDto =new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        List<CallbackPhoneLog> callbackPhoneLogList = callbackPhoneLogMapper.findByFeild(paramsMap);
        if (callbackPhoneLogList == null || callbackPhoneLogList.size()  == 0){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERBAXKLOG_ERROR);
        }
        CallbackPhoneLog callbackPhoneLog = callbackPhoneLogList.get(0);
        Date createTime = callbackPhoneLog.getCreateTime();//获取创建时间
        paramsMap.clear();
        paramsMap.put("createTime",createTime);
        List<CallbackLog>  callbackLogList= callbackLogMapper.findByFeild(paramsMap);
        List<CallbackLogDTO> resultList = new ArrayList<>();
        if (callbackLogList != null && callbackLogList.size() > 0){
            for (CallbackLog callbackLog : callbackLogList) {
                CallbackLogDTO callbackLogDTO = new CallbackLogDTO();
                callbackLogDTO.setId(callbackLog.getId());
                callbackLogDTO.setCustomerId(callbackLog.getCustomerId());
                callbackLogDTO.setQuestion(callbackLog.getQuestion());
                callbackLogDTO.setAnswer(callbackLog.getAnswer());
                callbackLogDTO.setCreateTime(callbackLog.getCreateTime());
                callbackLogDTO.setCreateUserName(callbackLog.getCreateUserName());
                callbackLogDTO.setCreateUserId(callbackLog.getCreateUserId());
                resultList.add(callbackLogDTO);
            }
            resultDto.setData(resultList);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            return resultDto;
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        return resultDto;

    }

    public CronusDto getQuestion(Integer customerId,String token){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",customerId);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if(customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERUNFIND_ERROR);
        }
        String customerType = customerInfo.getCustomerType();
        //获取所有的问题信息
        JSONArray qusetions = null;
        List<CallbackConfigDTO> callbackConfigDTOS = getAllCallbackConfig();
        if (callbackConfigDTOS != null && callbackConfigDTOS.size() >0 ){
            for (CallbackConfigDTO callbackConfigDTO: callbackConfigDTOS) {
                if (callbackConfigDTO.getConfId() == CustomerEnum.getByIndex(customerType).getValue()){
                    qusetions = callbackConfigDTO.getQuestion();
                }
            }
            resultDto.setData(qusetions);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            return resultDto;
        }
        resultDto.setData(qusetions);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return resultDto;
    }

    @Transactional
   public CronusDto  editCallbackOk(CallbackDTO callbackDTO,String token){
        CronusDto resultDto = new CronusDto();
        Date date = new Date();
        Map<String,Object> paramsMap = new HashMap<>();
        if (StringUtils.isEmpty(callbackDTO.getCallbackStatus())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMCALLSTATUS_ERROR);
        }
        //获取当前登录用户的信息
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        UcUserDTO userDTO = ucService.getUserInfoByID(token,user_id);
        if (userDTO == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //
        //正常状态需回答完问题并生成callbacklog数据
        if (ResultResource.CUSTOMERSTATUS.equals(callbackDTO.getCallbackStatus())){
            boolean flag = true;
            //获取所有的问题
            List<QuestionsDTO> questionsDTOS = callbackDTO.getQuestion();
            if (questionsDTOS == null || questionsDTOS.size() == 0 ){
                throw new CronusException(CronusException.Type.CRM_CUSTOMQUESTION_ERROR);
            }
            List<CallbackLog> callbackLogList = null;
            for (QuestionsDTO questionsDTO : questionsDTOS) {
                 if (StringUtils.isEmpty(questionsDTO.getAnswer())){
                     callbackLogList.clear();
                     throw new CronusException(CronusException.Type.CRM_CUSTOANSWER_ERROR);
                 }
                CallbackLog callbackLog = new CallbackLog();
                callbackLog.setCustomerId(callbackDTO.getCustomerId());
                callbackLog.setCreateUserId(user_id);
                callbackLog.setCreateUserName(userDTO.getName());
                callbackLog.setQuestion(questionsDTO.getName());
                callbackLog.setAnswer(questionsDTO.getName());
                callbackLog.setCreateTime(date);
                callbackLogList.add(callbackLog);
            }
            //批量插入数据
            paramsMap.put("list",callbackLogList);
            try {
                callbackLogMapper.adCallbackLog(paramsMap);
            }catch (Exception e){
                throw new CronusException(CronusException.Type.CRM_CUSTOMERLOG_ERROR);
            }

        }
        //开始写入手机号日志
        CallbackPhoneLog callbackPhoneLog = new CallbackPhoneLog();
        callbackPhoneLog.setCustomerId(callbackDTO.getCustomerId());
        callbackPhoneLog.setOperatUserId(user_id);
        callbackPhoneLog.setCreateUser(user_id);
        callbackPhoneLog.setCreateTime(date);
        callbackPhoneLog.setOperatUserName(userDTO.getName());
        callbackPhoneLog.setDescription(callbackDTO.getCallbackDescription());
        callbackPhoneLog.setStatus(callbackDTO.getCallbackStatus());
        callbackPhoneLog.setRemark(callbackDTO.getCallbackRemark());
        callbackPhoneLog.setLastUpdateTime(date);
        callbackPhoneLog.setLastUpdateUser(user_id);
        callbackPhoneLog.setIsDeleted(0);
        callbackPhoneLogMapper.addCallbackPhoneLog(callbackPhoneLog);
        //更新客户的回访时间和回访状态,注意异常回访的情况下不更新回访时间

        return  resultDto;

   }

    public Integer createOrderWhere(CustomerInfo customerInfo){

        Integer communicationOrder = null;
        if (customerInfo.getCallbackTime() == null){
            communicationOrder = 1;
        }else {
            List<CallbackConfigDTO> resultList = getAllCallbackConfig();
            //遍历
            Integer cycle  = null;
            for (CallbackConfigDTO callbackConfigDTO  : resultList) {
                Integer type = (CustomerEnum.getByIndex(customerInfo.getCustomerType())).getValue();
                if (type == callbackConfigDTO.getConfId()){
                    cycle = Integer.parseInt(callbackConfigDTO.getCycle());
                }

            }
            Date date = new Date();
            Date callbakTime = customerInfo.getCallbackTime();
            Long time1=Long.parseLong(DateUtils.format(date,DateUtils.FORMAT_FULL_Long));
            Long time2=Long.parseLong(DateUtils.format(callbakTime,DateUtils.FORMAT_FULL_Long));
            if (time1 - time2 < (long)cycle*30*24*60*60 && ResultResource.CUSTOMERSTATUS.equals(customerInfo.getCallbackStatus())){
                communicationOrder = 3;
            }else {
                communicationOrder = 2;
            }
        }
        return  communicationOrder;
    }


    public Integer  getConfigTime(Integer type){
        //从缓存中获取到配置信息
        Integer cycle = null;
        List<CallbackConfigDTO> callbackConfigDtos = getAllCallbackConfig();
        if (callbackConfigDtos  == null || callbackConfigDtos.size() == 0){
            throw new CronusException(CronusException.Type.CRM_CALLBACK_CONFIG_ERROR);
        }
        for ( CallbackConfigDTO callbackConfigDto : callbackConfigDtos) {
           if (type == callbackConfigDto.getConfId()) {
               cycle = Integer.parseInt(callbackConfigDto.getCycle());
           }
        }

    return cycle;
    }

    public List<CallbackConfigDTO>  getAllCallbackConfig(){
          //从缓存中获取配置
        List<CallbackConfigDTO> resultList = new ArrayList<>();
        List<CallbackConfigDTO> redisList = cronusRedisService.getRedisCronusInfo(ResultResource.CALLBACKCONFIG_KEY);
    if (resultList != null && resultList.size() >0 ){
        return  redisList;
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
        return resultList;
    }
    public CallbackCusLoanDTO findCustomerByWhere(Map<String,Object> paramsMap,String token){
        CallbackCusLoanDTO dto = new CallbackCusLoanDTO();

        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        EntityToDto.customerEntityToCallbackCustomerDto(customerInfo,dto);
        if (dto.getCustomerId() == 279717){
            dto.setTelephonenumber("1861689****");
        }
        LoanDTO loanDTO = loaService.selectByCustomerId(token,customerInfo.getId());
        // 拼装交易参数
        dto.setCooperationStatus(loanDTO.getCooperationStatus());
        dto.setMindLamount(loanDTO.getMindAmount());
        dto.setOwnUserId(loanDTO.getOwnUserId());
        dto.setOwnUserName(loanDTO.getOwnUserName());
        dto.setCustomerSource(loanDTO.getCustomerSource());
        dto.setUtmSource(loanDTO.getUtmSource());
        return  dto;

    }
}
