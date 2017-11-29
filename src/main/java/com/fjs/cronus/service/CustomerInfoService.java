package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONArray;
import com.fjs.cronus.Common.CustomerEnum;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.dto.cronus.EmplouInfo;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerInfoLog;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.EntityToDto;
import com.fjs.cronus.util.FastJsonUtils;
import com.fjs.cronus.util.PhoneFormatCheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by msi on 2017/9/13.
 */
@Service
public class CustomerInfoService {

    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    UcService ucService;
    @Autowired
    CustomerInfoLogMapper customerInfoLogMapper;

    public  List<CustomerInfo> findList(){
        List<CustomerInfo> resultList = new ArrayList();
        resultList = customerInfoMapper.selectAll();
        return  resultList;
    }

    public QueryResult customerList(String customerName,String telephonenumber,String utmSource, String ownUserName,
                                    String customerSource, Integer circle, Integer companyId,Integer page,Integer size){
        QueryResult result = new QueryResult();
        Map<String,Object> paramsMap = new HashMap<>();
        List<CustomerInfo> resultList = new ArrayList<>();
        List<CustomerListDTO> dtoList = new ArrayList<>();
        if (!StringUtils.isEmpty(customerName)){
            paramsMap.put("customerName",customerName);
        }
        if (!StringUtils.isEmpty(utmSource)){
            paramsMap.put("utmSource",utmSource);
        }
        if (!StringUtils.isEmpty(ownUserName)){
            paramsMap.put("ownUserName",ownUserName);
        }
        if (!StringUtils.isEmpty(customerSource)){
            paramsMap.put("customerSource",customerSource);
        }
        if (circle != null){
            paramsMap.put("circle",circle);
        }
        if (companyId != null){
            paramsMap.put("companyId",companyId);
        }
        paramsMap.put("start",(page-1) * size);
        paramsMap.put("size",size);
        resultList = customerInfoMapper.customerList(paramsMap);
        if (resultList != null && resultList.size() > 0){
            for (CustomerInfo customerInfo : resultList) {
                CustomerListDTO customerDto = new CustomerListDTO();
                EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto);
                dtoList.add(customerDto);
            }
            Integer count = customerInfoMapper.customerListCount(paramsMap);
            result.setRows(dtoList);
            result.setTotal(count.toString());
        }
        Integer count = customerInfoMapper.customerListCount(paramsMap);
        result.setRows(dtoList);
        result.setTotal(count.toString());
        return  result;
    }

    @Transactional
    public CronusDto addCustomer(CustomerDTO customerDTO, String token){
        CronusDto cronusDto = new CronusDto();
         //判断必传字段*/
         //json转map 参数，教研参数
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户面谈信息出错!");
        }
         validAddData(customerDTO);
         //实体与DTO相互转换
         CustomerInfo customerInfo = new CustomerInfo();
         EntityToDto.customerCustomerDtoToEntity(customerDTO,customerInfo);
         //新加字段
         List<EmplouInfo> emplouInfos = customerDTO.getEmployedInfo();
        //转Json在转String
        if (emplouInfos != null && emplouInfos.size() > 0) {
            String jsonString = JSONArray.toJSONString(emplouInfos);
            customerInfo.setEmployedInfo(jsonString);
        }
         customerInfo.setRetirementWages(customerDTO.getRetirementWages());
         Date date = new Date();
         customerInfo.setCreateTime(date);
         customerInfo.setCreateUser(user_id);
         customerInfo.setLastUpdateTime(date);
         customerInfo.setLastUpdateUser(user_id);
         customerInfo.setCustomerType(ResultResource.CUSTOMERTYPE);
         customerInfo.setIsDeleted(0);
         customerInfoMapper.insertCustomer(customerInfo);
         if (customerInfo.getId() == null){
             throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR);
         }
         //开始插入log表
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("增加一条客户记录");
        customerInfoLog.setLogUserId(user_id);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        cronusDto.setResult(ResultResource.CODE_SUCCESS);
        cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        cronusDto.setData(customerInfo.getId());
        return  cronusDto;
    }

    public CronusDto<Integer> fingBytelephone(String telephonenumber){
        CronusDto resultDto =  new CronusDto();
        //手机需要加密
        Map<String,Object> paramsMap = new HashMap<>();
        CustomerDTO dto= new CustomerDTO();
        String encryptTelephone = "";//加密后的
        List paramsList = new ArrayList();
        paramsList.add(encryptTelephone);
        paramsList.add(telephonenumber);
        paramsMap.put("paramsList",paramsList);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        EntityToDto.customerEntityToCustomerDto(customerInfo,dto);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfo.getId());
        return  resultDto;
    }
    public CronusDto findCustomerListByIds (String customerids,String customerName){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        List paramsList = new ArrayList();
        //截取逗号
        if (customerids != null && !"".equals(customerids)) {
            String[] strArray = null;
            strArray = customerids.split(",");
            for (int i = 0; i < strArray.length; i++) {
                paramsList.add(Integer.parseInt(strArray[i]));
            }
            paramsMap.put("paramsList", paramsList);
        }
        if (!StringUtils.isEmpty(customerName)){
            paramsMap.put("customerName",customerName);
        }
        List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerListByFeild(paramsMap);
        //遍历
        List<CustomerDTO> customerDtos = new ArrayList<>();
       for (CustomerInfo customerInfo: customerInfoList) {
            CustomerDTO customerDto = new CustomerDTO();
            EntityToDto.customerEntityToCustomerDto(customerInfo,customerDto);
            //TODO  增加source 调用接口 来源渠道
            customerDtos.add(customerDto);
        }
        if (customerDtos != null && customerDtos.size() > 0) {
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setData(customerDtos);
        }
        return  resultDto;
    }
    public CronusDto editCustomer(Integer customerId){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",customerId);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        CustomerDTO customerDto = new CustomerDTO();
        EntityToDto.customerEntityToCustomerDto(customerInfo,customerDto);
        customerDto.setRetirementWages(customerInfo.getRetirementWages());
        String employedInfo = customerInfo.getEmployedInfo();
        List<EmplouInfo> emplouInfos = new ArrayList<>();
        if (!StringUtils.isEmpty(employedInfo)){
            JSONArray jsonArray = JSONArray.parseArray(employedInfo);
            emplouInfos = jsonArray.toJavaList(EmplouInfo.class);
            customerDto.setEmployedInfo(emplouInfos);
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerDto);
        return  resultDto;
    }

    /**
     * 提交编辑用户
     * @return
     */
    @Transactional
    public CronusDto editCustomerOk(CustomerDTO customerDTO, String token){
        CronusDto resultDto = new CronusDto();
        //校验参数手机号不更新
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "编辑客户面谈信息出错!");
        }
        Map<String,Object> paramsMap = new HashMap<>();
        if (customerDTO.getId() == null || "".equals(customerDTO.getId()) ){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        if (customerDTO.getCustomerName() == null || "".equals(customerDTO.getCustomerName()) ){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERNAME_ERROR);
        }
        if (customerDTO.getHouseStatus() == null || "".equals(customerDTO.getHouseStatus())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEHOUSE_ERROR);
        }
        Integer id = customerDTO.getId();
        paramsMap.put("id",id);
        CustomerInfo customerInfo  = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Date date = new Date();
        EntityToDto.customerCustomerDtoToEntity(customerDTO,customerInfo);
        customerInfo.setLastUpdateTime(date);
        customerInfo.setLastUpdateUser(user_id);
        customerInfoMapper.updateCustomer(customerInfo);
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("编辑交易信息");
        customerInfoLog.setLogUserId(user_id);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfo.getId());
        return  resultDto;
    }
    public List findCustomerByType(String customerType){
        Map<String,Object> paramsMap = new HashMap<>();
        List<Integer> customerInfoList = new ArrayList<>();
        if (!StringUtils.isEmpty(customerType)){
        paramsMap.put("customerType",customerType);
        }else {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        customerInfoList = customerInfoMapper.findCustomerByType(paramsMap);
        //遍历
        if (customerInfoList != null && customerInfoList.size() > 0) {
           return  customerInfoList;
        }
        return  customerInfoList;

    }

    public void validAddData(CustomerDTO customerInfo){
        String customerName = customerInfo.getCustomerName();
        String telephonenumber = customerInfo.getTelephonenumber();
        Integer customerId = customerInfo.getId();

        if (customerName == null || "".equals(customerName)){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERNAME_ERROR);
        }
        if (telephonenumber == null || "".equals(telephonenumber)) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONE_ERROR);
        }
        if (PhoneFormatCheckUtils.isChinaPhoneLegal(telephonenumber) == false){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONE_ERROR);
        }
        //判断手机号是否被注册
     /*   if (customerId == null){
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("telephonenumber",telephonenumber);
            paramsMap.put("start",0);
            paramsMap.put("size",10);
            List<CustomerInfo> customerInfos = customerInfoMapper.customerList(paramsMap);
            if (customerInfos.size() > 0){
                throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONERE_ERROR);
            }
        }*/
    }
    public CronusDto<CustomerDTO> findCustomerByFeild(Integer customerId){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        if (!StringUtils.isEmpty(customerId)) {
            paramsMap.put("id", customerId);
        }
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        CustomerDTO customerDto = new CustomerDTO();
        EntityToDto.customerEntityToCustomerDto(customerInfo,customerDto);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerDto);
        return  resultDto;
    }
    public  CronusDto findCustomerByCity(String city){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        if (!StringUtils.isEmpty(city)){
            paramsMap.put("city",city);
        }
        List<Integer> customerIds = new ArrayList<>();
        List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerListByFeild(paramsMap);
        if (customerInfoList != null && customerInfoList.size() > 0) {
            for (CustomerInfo customerInfo : customerInfoList) {
                customerIds.add(customerInfo.getId());
            }
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerIds);
        return  resultDto;
    }
    public  CronusDto findCustomerByOtherCity(String citys){
        CronusDto resultDto = new CronusDto();
        //处理参数
        String[] strArray = null;
        strArray = citys.split(",");
        List<String> list = new ArrayList();
        for (int i= 0;i<strArray.length;i++){
            list.add("'"+ strArray[i] + "'");
        }
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("cityList",list);
        List<Integer> customerIds = new ArrayList<>();
        List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerByOtherCity(paramsMap);
        if (customerInfoList != null && customerInfoList.size() > 0) {
            for (CustomerInfo customerInfo : customerInfoList) {
                customerIds.add(customerInfo.getId());
            }
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerIds);
        return  resultDto;
    }
    @Transactional
    public CronusDto editCustomerType(Integer customer_id ,Integer user_id,String customerTypeSta,String customerTypeEnd){
        CronusDto resultDto = new CronusDto();
        //根据uid查询到客户相关信息
        boolean flag = false;
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",customer_id);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERIDENTITYINFO_ERROR);
        }
        //开始更改信息由意向客户改为协议客户
        String customerType = customerInfo.getCustomerType();
        if (customerType.equals(CustomerEnum.intentional_customer.getName())){
            //改成协议客户
            customerInfo.setCustomerType(CustomerEnum.agreement_customer.getName());
        }
        //开始更新
        customerInfoMapper.updateCustomer(customerInfo);
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        Date date = new Date();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("签章协议");
        customerInfoLog.setLogUserId(user_id);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        flag = true;
        resultDto.setData(flag);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return resultDto;
    }

    public CustomerInfo findCustomerById(Integer customerId) {

        Map<String, Object> paramsMap = new HashMap<>();
        if (!StringUtils.isEmpty(customerId)) {
            paramsMap.put("id", customerId);
        }
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        return customerInfo;
    }
}
