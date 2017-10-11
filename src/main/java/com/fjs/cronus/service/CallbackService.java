package com.fjs.cronus.service;

import com.fjs.cronus.Common.CustomerEnum;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;

import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CallbackConfigDTO;
import com.fjs.cronus.dto.cronus.CallbackCustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.loan.LoanDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CallbackConfigMapper;
import com.fjs.cronus.mappers.CallbackPhoneLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.CallbackConfig;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.service.uc.LoaService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public QueryResult callbackCustomerList(String callback_user, String callback_start_time, String callback_end_time, String search_name,
                                            Integer type, String search_city, String search_telephone, String search_callback_status, Integer page, Integer size, Integer communication_order, String token){
        QueryResult resultDto = new QueryResult();
        //筛选回访人
        List  customerIdList = new ArrayList();
        Map<String,Object> paramsMap = new HashMap<>();
        //根据token查询到当前登录用户信息
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户面谈信息出错!");
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
    public Integer createOrderWhere(CustomerInfo customerInfo){

        Integer communicationOrder = null;
        if (customerInfo.getCallbackTime() == null){
            communicationOrder = 1;
        }else {
            List<CallbackConfigDTO> resultList = getAllCallbackConfig();
            //遍历
            Integer cycle  = null;
            for (CallbackConfigDTO callbackConfigDTO  : resultList) {

                Integer type = CustomerEnum.getByIndex(customerInfo.getCustomerType()).getValue();
                if (type.toString().equals(callbackConfigDTO.getCycle())){
                    cycle = Integer.parseInt(callbackConfigDTO.getCycle());
                }

            }
            //判断是否过回访时间  Long time1=Long.parseLong(DateUtils.format(create_time,DateUtils.FORMAT_FULL_Long));
           // Long time2=Long.parseLong(DateUtils.format(date,DateUtils.FORMAT_FULL_Long));
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

        //

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
           if (type == callbackConfigDto.getConfId());
            cycle = Integer.parseInt(callbackConfigDto.getCycle());
        }

    return cycle;
    }

    public List<CallbackConfigDTO>  getAllCallbackConfig(){
          //从缓存中获取配置
        List<CallbackConfigDTO> resultList = new ArrayList<>();
        resultList = cronusRedisService.getRedisCronusInfo(ResultResource.CALLBACKCONFIG_KEY);
    if (resultList != null && resultList.size() >0 ){
        return  resultList;
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
            callbackConfigDto.setQuestion(callbackConfig.getQuestion());
            resultList.add(callbackConfigDto);
        }
        //存入缓存
        cronusRedisService.setRedisCronusInfo(ResultResource.CALLBACKCONFIG_KEY,resultList);
        return resultList;
    }
}
