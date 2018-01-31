package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CustomerEnum;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.dto.*;
import com.fjs.cronus.dto.api.uc.AppUserDto;
import com.fjs.cronus.dto.cronus.*;
import com.fjs.cronus.dto.cronus.CallbackLogDTO;
import com.fjs.cronus.dto.cronus.RepeatCustomerSaleDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.*;
import com.fjs.cronus.model.*;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.service.uc.LoaService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DEC3Util;
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
    @Autowired
    CustomerInfoLogMapper customerInfoLogMapper;
    @Autowired
    CustomerSalePushLogMapper customerSalePushLogMapper;
    @Autowired
    CustomerInfoService customerInfoService;
    public QueryResult callbackCustomerList(String callback_start_time, String callback_end_time, String search_name,
                                            Integer type, String search_city, String search_telephone, String search_callback_status, Integer page, Integer size, Integer communication_order,Integer cycle,
                                            Integer ownUserId,Integer isHaveOwn,Integer subCompanyId,String token){
        QueryResult resultDto = new QueryResult();
        //筛选回访人
        //List  customerIdList = new ArrayList();
        Map<String,Object> paramsMap = new HashMap<>();
        //根据token查询到当前登录用户信息
        if (type == null || "".equals(type)){
            throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
        }

        if (!StringUtils.isEmpty(callback_start_time)){
            Date startDate = DateUtils.parse(callback_start_time,DateUtils.FORMAT_SHORT);
            paramsMap.put("createTimeStart",startDate);
        }
        if (!StringUtils.isEmpty(callback_end_time)){
            Date endDate = DateUtils.parse(callback_end_time,DateUtils.FORMAT_SHORT);
            paramsMap.put("createTimeEnd",endDate);
        }
       /*  if (paramsMap != null && paramsMap.size() > 0 ){
             //从phonelog中查询到customerId
             customerIdList = phoneLogMapper.getCustomerId(paramsMap);
             paramsMap.clear();
         }
         if (customerIdList != null  && customerIdList.size() > 0){
             paramsMap.put("customerIdList",customerIdList);
         }*/
         paramsMap.put("customer_type", CustomerEnum.getByValue(type).getName());
         if (!StringUtils.isEmpty(search_city)){
             paramsMap.put("city",search_city);
         }
         if (!StringUtils.isEmpty(search_name)){
            paramsMap.put("search_name",search_name);
          }
        if (!StringUtils.isEmpty(search_callback_status)){
            paramsMap.put("search_callback_status",search_callback_status);
        }
        if (!StringUtils.isEmpty(ownUserId)){
            paramsMap.put("ownUserId",ownUserId);
        }
        if (!StringUtils.isEmpty(isHaveOwn)){
            paramsMap.put("isHaveOwn",isHaveOwn);
        }
        if (!StringUtils.isEmpty(subCompanyId)){
            paramsMap.put("subCompanyId",subCompanyId);
        }
        if (!StringUtils.isEmpty(search_telephone)){
            String encryptTelephone = DEC3Util.des3EncodeCBC(search_telephone);
            paramsMap.put("telephonenumber",encryptTelephone);
        }
       //计算分页
        if (communication_order == 1){
           //从未回访的
            paramsMap.put("communication_order",communication_order);
        }else if (communication_order == 2){
            //需要重新回访的,后去当前系统时间
            //从缓存中获取配置时间
           // Integer configTime = getConfigTime(type);
            Integer configTime = cycle;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR,-configTime * 30);
            Date searchTime = cal.getTime();
            //不为null
            paramsMap.put("searchTime",searchTime);
            paramsMap.put("communication_order",communication_order);
        }else if (communication_order == 3){
           // Integer configTime = getConfigTime(type);
            Integer configTime = cycle;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR,-configTime * 30);
            Date searchTime = cal.getTime();
            paramsMap.put("searchTime",searchTime);//大于
            paramsMap.put("callback_status","正常");
            paramsMap.put("communication_order",communication_order);
        }else {
            //默认所有需要回访的
           // Integer configTime = getConfigTime(type);
            Integer configTime = cycle;
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
            callbackCustomerDTO.setId(customerInfo.getId());
            callbackCustomerDTO.setCallbackStatus(customerInfo.getCallbackStatus());
            if (customerInfo.getCallbackTime() != null) {
                callbackCustomerDTO.setCallbackTime(customerInfo.getCallbackTime());
            }
            callbackCustomerDTO.setCity(customerInfo.getCity());
            if (customerInfo.getCreateTime() != null){
                callbackCustomerDTO.setCreateTime(customerInfo.getCreateTime());
            }
            Integer communicationOrder = createOrderWhere(customerInfo);
            callbackCustomerDTO.setCommunicationOrder(communicationOrder);
            //解密
            String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
            callbackCustomerDTO.setTelephonenumber(telephone);
            callbackCustomerDTO.setCustomerType(customerInfo.getCustomerType());
            callbackCustomerDTO.setCustomerName(customerInfo.getCustomerName());
            callbackCustomerDTO.setLoanAmount(customerInfo.getLoanAmount());
            if (customerInfo.getOwnUserId() != null && customerInfo.getOwnUserId() != 0){
                 AppUserDto ucUserDTO = ucService.getUserInfoByID(token,customerInfo.getOwnUserId());
                 callbackCustomerDTO.setOwnUserName(ucUserDTO.getName());
                 callbackCustomerDTO.setOwnUserId(customerInfo.getOwnUserId());
                 callbackCustomerDTO.setSub_company(ucUserDTO.getSub_company_name() + "(" + ucUserDTO.getCompany_name() + ")");
            }else {
                callbackCustomerDTO.setOwnUserName("无");
                callbackCustomerDTO.setSub_company("未知");
                callbackCustomerDTO.setOwnUserId(customerInfo.getOwnUserId());
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
                     List<QuestionsDTO> questionsDTOS = callbackConfigDTO.getQuestion();
                     if (questionsDTOS != null && questionsDTOS.size() > 0) {
                         for (QuestionsDTO questionsDTO : questionsDTOS ) {
                             qusetions.add(questionsDTO);
                         }
                     }
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
   public CronusDto  editCallbackOk(Integer customerId,String callback_status,Integer userId){
        boolean flag = false;
        CronusDto resultDto = new CronusDto();
        Date date = new Date();
        Map<String,Object> paramsMap = new HashMap<>();
  /*      //获取当前登录用户的信息
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        com.fjs.cronus.dto.uc.UserInfoDTO userDTO = ucService.getUserInfoByID(token,user_id);
        if (userDTO == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //*/
       /* //正常状态需回答完问题并生成callbacklog数据
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
        //更新客户的回访时间
        //根据id找到客户*/
        paramsMap.put("id",customerId);
        CustomerInfo updatecustomerInfo = customerInfoMapper.findByFeild(paramsMap);
        //更改状态和时间、
        updatecustomerInfo.setCallbackStatus(callback_status);
        updatecustomerInfo.setCallbackTime(date);
        updatecustomerInfo.setLastUpdateUser(userId);
        updatecustomerInfo.setLastUpdateTime(date);
        customerInfoMapper.updateCustomer(updatecustomerInfo);
        //插入日志
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(updatecustomerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("增加一条客户记录");
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        flag = true;
        resultDto.setData(flag);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        return resultDto;

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


  /*  public Integer  getConfigTime(Integer type){
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
    }*/

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
//        dto.setCooperationStatus(loanDTO.getCooperationStatus());
        dto.setMindLamount(loanDTO.getLoanAmount());
//        dto.setOwnUserId(loanDTO.getOwnUserId());
        dto.setOwnUserName(loanDTO.getOwnUserName());
//        dto.setCustomerSource(loanDTO.getCustomerSource());
//        dto.setUtmSource(loanDTO.getUtmSource());
        return  dto;
    }

    public QueryResult<RepeatCustomerDTO> repeatcustomerList(String telephonenumber,String repeat_start_time,String repeat_end_time,String customer_name,
                                                       Integer repeat_callback_status,Integer page,Integer size){
        QueryResult<RepeatCustomerDTO> resultDto = new QueryResult<>();
        List<RepeatCustomerDTO> callbackDTOS = new ArrayList<>();
        Map<String,Object> paramsMap = new HashMap<>();
        List<String> telephoneList = new ArrayList<>();
        if (!StringUtils.isEmpty(telephonenumber)){
            paramsMap.put("telephonenumber",telephonenumber);
        }
        if (!StringUtils.isEmpty(repeat_start_time)){
            Date startDate = DateUtils.parse(repeat_start_time,DateUtils.FORMAT_SHORT);
            paramsMap.put("repeat_start_time",startDate);
        }
        if (!StringUtils.isEmpty(repeat_end_time)){
            Date endtDate = DateUtils.parse(repeat_end_time,DateUtils.FORMAT_SHORT);
            paramsMap.put("repeat_end_time",endtDate);
        }
        if (!StringUtils.isEmpty(customer_name)){
            paramsMap.put("customer_name",customer_name);
        }
        if (!StringUtils.isEmpty(repeat_callback_status)){
            paramsMap.put("repeat_callback_status",repeat_callback_status);
        }
        paramsMap.put("start",(page-1) * size);
        paramsMap.put("size",size);
        //开始拼装语句
        List<RepeatCustomerSaleDTO> customerList = customerSalePushLogMapper.repeatcustomerList(paramsMap);
        Integer count = customerSalePushLogMapper.repeatcustomerListConut(paramsMap);
        Map<String,RepeatParamDTO> repeatParamDTOMap = new HashMap<>();
        if (customerList != null && customerList.size() > 0){
            //循环
            for (RepeatCustomerSaleDTO  repeatCustomerSaleDTO : customerList) {
                if (repeatCustomerSaleDTO.getTelephonenumber() != null) {
                    //加密
                    telephoneList.add(DEC3Util.des3EncodeCBC(repeatCustomerSaleDTO.getTelephonenumber()));
                    //存储一个key value的参数
                    RepeatParamDTO repeatParamDTO = new RepeatParamDTO();
                    repeatParamDTO.setReat_num(repeatCustomerSaleDTO.getReat_num());
                    repeatParamDTO.setRepeat_callback_time(repeatCustomerSaleDTO.getRepeat_callback_time());
                    repeatParamDTOMap.put(repeatCustomerSaleDTO.getTelephonenumber(),repeatParamDTO);
                }
            }
        }
        if (telephoneList != null && telephoneList.size() > 0){
            //开始查询
            Map<String,Object> params = new HashMap<>();
            params.put("telephoneList",telephoneList);
            List<CustomerInfo>  customerInfoList = customerInfoMapper.findCustomerListByFeild(params);
            if (customerInfoList != null && customerInfoList.size() > 0){
                for (CustomerInfo customerInfo : customerInfoList) {
                    RepeatCustomerDTO repeatCustomerDTO = new RepeatCustomerDTO();
                    repeatCustomerDTO.setCity(customerInfo.getCity());
                    repeatCustomerDTO.setUtmSource(customerInfo.getUtmSource());
                    repeatCustomerDTO.setCreateTime(customerInfo.getCreateTime());
                    repeatCustomerDTO.setCustomerName(customerInfo.getCustomerName());
                    repeatCustomerDTO.setCustomerSource(customerInfo.getCustomerSource());
                    repeatCustomerDTO.setHouseStatus(customerInfo.getHouseStatus());
                    repeatCustomerDTO.setId(customerInfo.getId());
                    repeatCustomerDTO.setLoanAmount(customerInfo.getLoanAmount());
                    repeatCustomerDTO.setOwnUserName(customerInfo.getOwnUserName());
                    String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
                    repeatCustomerDTO.setTelephonenumber(telephone);
                    RepeatParamDTO repeatParamDTO= repeatParamDTOMap.get(telephone);
                    repeatCustomerDTO.setReat_num(repeatParamDTO.getReat_num());
                    repeatCustomerDTO.setRepeat_callback_time(repeatParamDTO.getRepeat_callback_time());
                    callbackDTOS.add(repeatCustomerDTO);

                }
            }
            params.clear();
        }
        resultDto.setTotal(count.toString());
        resultDto.setRows(callbackDTOS);
        return  resultDto;
    }
    public List<RepeatChildDTO> getchildInfo(Integer customerId){
        List<RepeatChildDTO> repeatChildDTOS = new ArrayList<>();
        Map<String,Object> parmasMap = new HashMap<>();
        CustomerInfo customerInfo = customerInfoService.findCustomerById(customerId);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //查询客户推送日志记录
        String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
        //查询信息
        parmasMap.put("telephone",telephone);
        List<CustomerSalePushLog> customerSalePushLogs = customerSalePushLogMapper.findByFeild(parmasMap);
        if (customerSalePushLogs != null && customerSalePushLogs.size() > 0){
            for (CustomerSalePushLog customerSalePushLog : customerSalePushLogs){
                RepeatChildDTO repeatChildDTO = new RepeatChildDTO();
                repeatChildDTO.setCity(customerSalePushLog.getCity());
                repeatChildDTO.setCreateTime(customerSalePushLog.getCreateTime());
                repeatChildDTO.setCustomerName(customerSalePushLog.getCustomerName());
                repeatChildDTO.setCustomerSource(customerSalePushLog.getCustomerSource());
                repeatChildDTO.setCity(customerSalePushLog.getCity());
                repeatChildDTO.setId(customerSalePushLog.getId());
                repeatChildDTO.setLoanAmount(customerSalePushLog.getLoanAmount());
                repeatChildDTO.setUtmSource(customerSalePushLog.getUtmSource());
                //兼容老的数据库中housestatus字段
                if (StringUtils.isEmpty(customerSalePushLog.getHouseStatus())){
                    String str = customerSalePushLog.getExt();
                    //转json
                    JSONObject jsonObject = JSONObject.parseObject(str);
                    if (jsonObject.getString("house_status") != null) {
                        String hosestatus = jsonObject.getString("house_status");
                        repeatChildDTO.setHouseStatus(hosestatus);
                    }
                }else {

                    repeatChildDTO.setHouseStatus(customerSalePushLog.getHouseStatus());
                }
                repeatChildDTOS.add(repeatChildDTO);
            }

        }
        return  repeatChildDTOS;
    }



}
