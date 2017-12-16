package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.*;
import com.fjs.cronus.dto.ContractDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPUserDto;
import com.fjs.cronus.dto.api.crius.Contract;
import com.fjs.cronus.dto.api.crius.ServiceContract;
import com.fjs.cronus.dto.api.uc.SubCompanyDto;
import com.fjs.cronus.dto.cronus.*;
import com.fjs.cronus.dto.thea.ServiceContractDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.AllocateLogMapper;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CommunicationLog;
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
    @Autowired
    CommunicationLogService communicationLogService;
    @Autowired
    AllocateService allocateService;
    @Autowired
    AllocateLogMapper allocateLogMapper;

    public  List<CustomerInfo> findList(){
        List<CustomerInfo> resultList = new ArrayList();
        resultList = customerInfoMapper.selectAll();
        return  resultList;
    }

    public QueryResult customerList(Integer  userId,String customerName,String telephonenumber,String utmSource, String ownUserName,
                                    String customerSource, Integer circle, Integer companyId,Integer page,Integer size,Integer remain,String level,String token){
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
        if (remain != null){
            paramsMap.put("remain",remain);
        }
        if (level != null){
            paramsMap.put("level",level);
        }
        if (telephonenumber != null){
            paramsMap.put("telephonenumber",telephonenumber);
        }
        //获取下属员工
        List<Integer> ids = ucService.getSubUserByUserId(token,userId);
        paramsMap.put("owerId",ids);
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

    @Transactional
    public CronusDto addCRMCustomer(AddCustomerDTO customerDTO, String token){
        CronusDto cronusDto = new CronusDto();
        //判断必传字段*/
        //json转map 参数，教研参数
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户信息出错!");
        }
        String customerName = customerDTO.getCustomerName();
        String telephonenumber = customerDTO.getTelephonenumber();
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
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("telephonenumber",telephonenumber);
            paramsMap.put("start",0);
            paramsMap.put("size",10);
            List<CustomerInfo> customerInfos = customerInfoMapper.customerList(paramsMap);
            if (customerInfos.size() > 0){
                throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONERE_ERROR);
            }

        //实体与DTO相互转换
        CustomerInfo customerInfo = new CustomerInfo();

        Date date = new Date();
        customerInfo.setCustomerName(customerDTO.getCustomerName());
        customerInfo.setTelephonenumber(customerDTO.getTelephonenumber());
        customerInfo.setCustomerSource(customerDTO.getCustomerSource());
        customerInfo.setUtmSource(customerDTO.getUtmSource());
        customerInfo.setLoanAmount(customerDTO.getLoanAmount());
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
    public CronusDto<CustomerDTO> editCustomer(Integer customerId){
        CronusDto<CustomerDTO> resultDto = new CronusDto();
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
        //校验权限

        PHPLoginDto userInfo = ucService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        String[] authority=userInfo.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.UPDATE_LOAN_URL)){
                resultDto.setResult(CommonMessage.UPDATE_FAIL.getCode());
                resultDto.setMessage(CommonConst.NO_AUTHORIZE);
                return resultDto;
            }
        }
        //校验参数手机号不更新
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "信息出错!");
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
        customerInfo.setRetirementWages(customerInfo.getRetirementWages());
        List<EmplouInfo> emplouInfos= customerDTO.getEmployedInfo();
        if (emplouInfos != null && emplouInfos.size() > 0) {
            String jsonString = JSONArray.toJSONString(emplouInfos);
            customerInfo.setEmployedInfo(jsonString);
        }
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
        if (customerId == null){
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("telephonenumber",telephonenumber);
            paramsMap.put("start",0);
            paramsMap.put("size",10);
            List<CustomerInfo> customerInfos = customerInfoMapper.customerList(paramsMap);
            if (customerInfos.size() > 0){
                throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONERE_ERROR);
            }
        }
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

    @Transactional
    public CronusDto editCustomerTypeTOConversion(Integer customer_id ,Integer user_id){
        CronusDto resultDto = new CronusDto();
        //根据uid查询到客户相关信息
        boolean flag = false;
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",customer_id);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERIDENTITYINFO_ERROR);
        }
        //协议客户改为成交用户
        String customerType = customerInfo.getCustomerType();
        if (customerType.equals(CustomerEnum.agreement_customer.getName())){
            //成交用户
            customerInfo.setCustomerType(CustomerEnum.conversion_customer.getName());
        }
        //开始更新
        customerInfoMapper.updateCustomer(customerInfo);
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        Date date = new Date();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("成交用户");
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

    public QueryResult<CustomerListDTO> allocationCustomerList(String customerName,String utmSource,String customerSource,Integer autostatus,Integer page,Integer size,Integer type){
        List<CustomerInfo> resultList = new ArrayList<>();
        Map<String,Object> paramsMap = new HashMap<>();
        List<CustomerListDTO> doList = new ArrayList<>();
        QueryResult<CustomerListDTO> result = new QueryResult<>();
        Integer count = null;
        if (!StringUtils.isEmpty(customerName)){
            paramsMap.put("customerName",customerName);
        }
        if (!StringUtils.isEmpty(utmSource)){
            paramsMap.put("utmSource",utmSource);
        }
        if (!StringUtils.isEmpty(customerSource)){
            paramsMap.put("customerSource",customerSource);
        }
        if (autostatus != null){
            paramsMap.put("autostatus",autostatus);
        }
        paramsMap.put("start",(page-1) * size);
        paramsMap.put("size",size);
        if (type == 1){//已沟通客户 判断沟通时间不为null;
            resultList = customerInfoMapper.communicatedList(paramsMap);
            count = customerInfoMapper.communicatedListCount(paramsMap);
        }else {
            resultList = customerInfoMapper.allocationCustomerList(paramsMap);
            count = customerInfoMapper.allocationCustomerListCount(paramsMap);
        }
        if (resultList != null && resultList.size() > 0){
            for (CustomerInfo customerInfo : resultList) {
                CustomerListDTO customerDto = new CustomerListDTO();
                EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto);
                doList.add(customerDto);
            }
            result.setRows(doList);
            result.setTotal(count.toString());
        }
        result.setRows(doList);
        result.setTotal(count.toString());
        return  result;
    }
    //不分页查询客户
    public List<CustomerInfo> listByCondition(CustomerInfo customerInfo,UserInfoDTO userInfoDTO,String token,String systemName){

        List<CustomerInfo> resultList = new ArrayList<>();
        Map<String,Object> paramsMap = new HashMap<>();
        //判断当前登录用户所属公司
        Integer companyId = null;
        if (!StringUtils.isEmpty(userInfoDTO.getCompany_id())) {
            companyId = Integer.parseInt(userInfoDTO.getCompany_id());
            customerInfo.setCompanyId(companyId);
        }
        //得到下属员工
        Integer userId = Integer.parseInt(userInfoDTO.getUser_id());
        List<Integer> ids = ucService.getSubUserByUserId(token,userId);
        paramsMap.put("owerId",ids);
        if (customerInfo != null) {
            if (customerInfo.getRemain() != null) {
                paramsMap.put("remain", customerInfo.getRemain());
            }
            if (customerInfo.getCompanyId() != null) {
                paramsMap.put("companyId", companyId);
            }
        }
        resultList = customerInfoMapper.findCustomerListByFeild(paramsMap);
        return  resultList;
    }

    @Transactional
    public boolean keepCustomer(Integer customerId,UserInfoDTO userInfoDTO){
        boolean flag = false;
        Integer userId = null;
        if (!StringUtils.isEmpty(userInfoDTO.getUser_id())) {
            userId = Integer.parseInt(userInfoDTO.getUser_id());
        }
        //根据id查询到
        Map<String, Object> paramsMap = new HashMap<>();
        if (!StringUtils.isEmpty(customerId)) {
            paramsMap.put("id", customerId);
        }
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        customerInfo.setOwnUserId(userId);
        customerInfo.setOwnUserName(userInfoDTO.getName());
        customerInfo.setRemain(CommonConst.REMAIN_STATUS_YES);
        customerInfo.setLastUpdateUser(userId);
        Date date = new Date();
        customerInfo.setLastUpdateTime(date);
        //开始更新
        customerInfoMapper.updateCustomer(customerInfo);
        //插入日志
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_11.getCodeDesc());
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        flag = true;
        return flag;
    }

    public CustomerSourceDTO quitCustomerSource(Integer userId,String token){
        //查询数据库中所有的组
        CustomerSourceDTO customerSourceDTO = new CustomerSourceDTO();
        List<String> customerSourceByGroup = customerInfoMapper.customerSourceByGroup();
        //获取当前登录用户能管理的总公司

        List<SubCompanyDto> companys = ucService.getAllCompanyByUserId(token,userId,CommonConst.SYSTEM_NAME_ENGLISH);

        customerSourceDTO.setCompanyDtos(companys);
        customerSourceDTO.setSource(customerSourceByGroup);
        return  customerSourceDTO;
    }

    public QueryResult<CustomerListDTO> resignCustomerList(String token,String customerName,String telephonenumber,String utmSource,String ownUserName,String customerSource,
                                                           String level,Integer companyId,Integer page,Integer size){
        QueryResult<CustomerListDTO> queryResult = new  QueryResult();
        List<CustomerListDTO> resultList = new ArrayList<>();
        Map<String,Object> paramMap = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        //获取离职员工的ids
        List<PHPUserDto> userDtos = ucService.getUserByIds(token,null,null,null,"eq",null,null,null,3);
        if (userDtos != null && userDtos.size() > 0){
            for (PHPUserDto userDto : userDtos) {
                ids.add(Integer.valueOf(userDto.getUser_id()));
            }
            paramMap.put("owerId",ids);
            if (!StringUtils.isEmpty(customerName)){
                paramMap.put("customerName",customerName);
            }
            if (!StringUtils.isEmpty(telephonenumber)){
                paramMap.put("telephonenumber",telephonenumber);
            }
            if (!StringUtils.isEmpty(utmSource)){
                paramMap.put("utmSource",utmSource);
            }
            if (!StringUtils.isEmpty(ownUserName)){
                paramMap.put("ownUserName",ownUserName);
            }
            if (!StringUtils.isEmpty(customerSource)){
                paramMap.put("customerSource",customerSource);
            }
            if (!StringUtils.isEmpty(level)){
                paramMap.put("level",level);
            }
            if (!StringUtils.isEmpty(companyId)){
                paramMap.put("companyId",companyId);
            }
            paramMap.put("start",(page-1) * size);
            paramMap.put("size",size);
            List<CustomerInfo> customerInfoList = customerInfoMapper.customerList(paramMap);
            if (customerInfoList != null && customerInfoList.size() > 0){

                for (CustomerInfo customerInfo : customerInfoList) {
                    CustomerListDTO customerDto = new CustomerListDTO();
                    EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto);
                    resultList.add(customerDto);
                }
                queryResult.setRows(resultList);
                Integer count = customerInfoMapper.customerListCount(paramMap);
                queryResult.setTotal(count.toString());
            }

        }

        return queryResult;
    }

    public boolean cancelkeepCustomer(Integer customerId,UserInfoDTO userInfoDTO){
           boolean flag = false;
        Integer userId = null;
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
            userId = Integer.parseInt(userInfoDTO.getUser_id());
        }
        Map<String, Object> paramsMap = new HashMap<>();
        if (!StringUtils.isEmpty(customerId)) {
            paramsMap.put("id", customerId);
        }
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        customerInfo.setOwnUserId(null);
        customerInfo.setOwnUserName(null);
        customerInfo.setRemain(CommonConst.REMAIN_STATUS_NO);
        customerInfo.setLastUpdateUser(userId);
        Date date = new Date();
        customerInfo.setLastUpdateTime(date);
        customerInfoMapper.updateCustomer(customerInfo);

        //插入日志
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_12.getCodeDesc());
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        flag = true;
        return flag;
    }

    public List<CustomerInfo> getByIds(String ids){
        List<CustomerInfo> resultList = new ArrayList<>();
        Map<String,Object> paramsMap = new HashMap<>();
        List<Integer> paramsList = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            String[] strArray = null;
            strArray = ids.split(",");
            for (int i = 0; i < strArray.length; i++) {
                paramsList.add(Integer.parseInt(strArray[i]));
            }
            paramsMap.put("paramsList", paramsList);
        }
        resultList = customerInfoMapper.findCustomerListByFeild(paramsMap);

        return  resultList;
    }

    @Transactional
    public CronusDto removeCustomer(String ids,String token){
        boolean flag = false;
        CronusDto cronusDto = new CronusDto();
        if (StringUtils.isEmpty(ids)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        UserInfoDTO userInfoDTO=ucService.getUserIdByToken(token,CommonConst.SYSTEMNAME);
        //根据编号查询到客户
        List<CustomerInfo> customerInfoList = getByIds(ids);
        if (customerInfoList == null || customerInfoList.size() == 0){
            throw new CronusException(CronusException.Type.MESSAGE_NOT_EXIST_LOAN);
        }
        for (CustomerInfo customerInfo : customerInfoList) {
            if (customerInfo.getRemain() == CommonConst.CONFIRM__STATUS_NO){
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage("" + customerInfo.getId() + "请先确认客户");
                return cronusDto;
            }
            //判断是否是意向客户
            if (!customerInfo.getCustomerType().equals(CommonConst.CUSTOMER_TYPE_MIND) ){
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage("" + customerInfo.getId() + "该客户不是意向客户");
                return cronusDto;
            }
            //负责人是不是自己
            if (!StringUtils.isEmpty(userInfoDTO.getUser_id()) && customerInfo.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id())){
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage("" + customerInfo.getId() + "该客户负责人不是本人");
                return cronusDto;
            }
            //是否沟通过
            List<CommunicationLog> communicationLogList= new ArrayList<CommunicationLog>();
            if (!StringUtils.isEmpty(userInfoDTO.getUser_id())){
                communicationLogList=communicationLogService.listByCustomerIdAndUserId(customerInfo.getId(),Integer.parseInt(userInfoDTO.getUser_id()),token);
            }
            if (communicationLogList.size() == 0){
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage("" + customerInfo.getId() + "请先沟通该客户");
                return cronusDto;
            }

        }
        //开始批量移除客户到公盘
        batchRemove(ids,Integer.valueOf(userInfoDTO.getUser_id()));
        flag = true;
        cronusDto.setResult(CommonMessage.REMOVE_SUCCESS.getCode());
        cronusDto.setMessage(CommonMessage.REMOVE_SUCCESS.getCodeDesc());
        cronusDto.setData(flag);
        return  cronusDto;
    }

    @Transactional
    public void batchRemove(String ids,Integer userId){
        Date date = new Date();
        String[] arr = ids.split(",");
        Map<String,Object> paramsMap = new HashMap<>();
        List<Integer> paramsList = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            String[] strArray = null;
            strArray = ids.split(",");
            for (int i = 0; i < strArray.length; i++) {
                paramsList.add(Integer.parseInt(strArray[i]));
            }
        }
        if (paramsList != null && paramsList.size() > 0){
            //开始插入日志
            for (Integer id : paramsList) {
                //查询到此客户
                CustomerInfo customerInfo = findCustomerById(id);
                if (customerInfo != null){
                    //开始插入日志
                    CustomerInfoLog customerInfoLog = new CustomerInfoLog();
                    EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
                    customerInfoLog.setLogCreateTime(date);
                    customerInfoLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_9.getCodeDesc());
                    customerInfoLog.setLogUserId(userId);
                    customerInfoLog.setIsDeleted(0);
                    customerInfoLogMapper.addCustomerLog(customerInfoLog);
                }
            }

            //开始移除公盘
            paramsMap.put("paramsList",paramsList);
            paramsMap.put("lastUpdateUser",userId);
            paramsMap.put("lastUpdateTime",date);
            customerInfoMapper.batchRemove(paramsMap);
        }
    }

    public boolean removeCustomerAll(RemoveDTO removeDTO,String token){
            Date date = new Date();
            boolean flag = false;
            //判断有没有选择负责人
            Map<String,Object> paramMap = new HashMap<>();
            List<Integer> ownIds = null;//负责人
            if (StringUtils.isEmpty(removeDTO.getEmpId())){
                throw new CronusException(CronusException.Type.MESSAGE_REMOVECUSTOERAll_ERROR);
            }
            //判断这个负责人是不是在职的
            UcUserDTO userInfoDTO = ucService.getUserInfoByID(token,removeDTO.getEmpId());
            Integer status = Integer.parseInt(userInfoDTO.getStatus());
            if (status != 1){
                throw new CronusException(CronusException.Type.MESSAGE_REMOVECUSTOERSTATUS_ERROR);
            }
            if (StringUtils.isEmpty(removeDTO.getIds())){
                throw new CronusException(CronusException.Type.MESSAGE_REMOVECUSTNOTNULL_ERROR);
            }
            //
        List<Integer> paramsList = new ArrayList<>();
        if (removeDTO.getIds() != null && !"".equals(removeDTO.getIds())) {
            String[] strArray = null;
            strArray = removeDTO.getIds().split(",");
            for (int i = 0; i < strArray.length; i++) {
                paramsList.add(Integer.parseInt(strArray[i]));
            }
        }
        //去重复操作
        List<Integer> uniqueList = new ArrayList<Integer>(new HashSet<>(paramsList));
        //判断客户存在不存在首次分配未处理的的
        boolean result = allocateService.validCustomerAllIsOperate(removeDTO.getIds());
        if (result == false){
            throw new CronusException(CronusException.Type.CRM_CUSOMERALLACATE_ERROR);
        }
        //查询这些客户的信息
        paramMap.put("paramsList",uniqueList);
        List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerListByFeild(paramMap);
        //得到这些业务员的负责人
        if (customerInfoList != null && customerInfoList.size() > 0) {
            for (CustomerInfo customerInfo : customerInfoList) {
                ownIds.add(customerInfo.getOwnUserId());
            }
           //对负责人去除操作
            ownIds =  new ArrayList<Integer>(new HashSet<>(ownIds));
            //得到用户的信息逗号隔开
            String strIds  = listToString(ownIds);
            List<PHPUserDto> userList = ucService.getUserByIds(token,strIds,null,null,null,null,null,null,null);
            // TODO 下面开始更改这些客户的信息
            flag = removeToUser(uniqueList,removeDTO.getEmpId(),userInfoDTO.getName(),token,userInfoDTO.getUser_id(),userInfoDTO.getName());
            if (flag == false){
                throw new CronusException(CronusException.Type.MESSAGE_REMOVENOTINJOB_ERROR);
            }
            /*负责人变更时，保留状态归零*/
            for (CustomerInfo customerInfo : customerInfoList) {
                Integer remain = customerInfo.getRemain();
                if (remain != 2){
                    remain =0;
                }
                customerInfo.setRemain(remain);
                customerInfo.setLastUpdateTime(date);
                customerInfo.setLastUpdateUser(Integer.valueOf(userInfoDTO.getUser_id()));
                customerInfoMapper.updateCustomer(customerInfo);
            }
            flag =true;
        }
            return  flag;
    }

    public boolean removeToUser(List<Integer> customerids,Integer touser,String touserName,String token,String userId,String userName){
        boolean flag = false;
        //循环每一个客户,对每一个客户进行处理


        List mustUpAgreementIds= new ArrayList();
        List mustUpCustomerIds=new ArrayList();

        List mustUpContractIds1=new ArrayList();//进行中的合同 主键
        List mustUpContractIds2=new ArrayList();//正在结案中的合同 主键

        List<LogArrDTO> logarr = new ArrayList();

        //开始遍历
        for (Integer customerId : customerids) {
            LogArrDTO logArrDTO = new LogArrDTO();
            List<ServiceLogDTO> serviceLogDTOS = new ArrayList<>();
            mustUpCustomerIds.add(customerId);
            //TODO 通过客户id获取他的服务合同 调用交易系统
            List<Integer> serviceContractDTOS = null;//每一个客户对应的居间协议/服务合同的id
            //判断如果没有服务合同
            if (serviceContractDTOS == null || serviceContractDTOS.size() == 0){
                logArrDTO.setCustomerId(customerId);
            }else {//服务合同不为空的话
                for (Integer agreementIds : serviceContractDTOS) {
                    //取得当前服务合同对应的合同信息
                    //TODO  根据服务合同id 获取所有的合同
                    ServiceLogDTO serviceLogDTO = new ServiceLogDTO();
                    List<ContracrLogDTO> contractInfo = null;
                    if (contractInfo == null || contractInfo.size() == 0){
                        logArrDTO.setCustomerId(customerId);
                        serviceLogDTO.setServiceContracrId(agreementIds);
                        serviceLogDTOS.add(serviceLogDTO);
                        mustUpAgreementIds.add(agreementIds);
                    }else {//此条信息有合同的时候
                         /*
                         * 3种类型：已结案：不做任何处理
                         * 结案中：合同的负责人设为被转移人；合同状态变为进行中；审批流程重置；协议的负责人设为被转移人；
                         * 进行中（未申请结案）：合同的负责人设为被转移人；协议的负责人设为被转移人；
                         */
                        //遍历合同的信息
                        logArrDTO.setCustomerId(customerId);
                        serviceLogDTO.setServiceContracrId(agreementIds);
                        List<ContracrLogDTO> contracrLogDTOS1 = new ArrayList<>();//第一种合同
                        List<ContracrLogDTO> contracrLogDTOS2 = new ArrayList<>();//第二种种合同
                        serviceLogDTOS.add(serviceLogDTO);
                        for (ContracrLogDTO contracrLogDTO : contractInfo) {
                            if (contracrLogDTO.getStatus() == 0) {//合同的状态为进行中 还没有提交申请
                                contracrLogDTOS1.add(contracrLogDTO);
                                mustUpAgreementIds.add(agreementIds);
                                mustUpContractIds1.add(contracrLogDTO.getContractId());
                            } else if (contracrLogDTO.getStatus() == 1) {////正在结案中
                                mustUpAgreementIds.add(agreementIds);
                                contracrLogDTOS2.add(contracrLogDTO);
                                mustUpContractIds2.add(contracrLogDTO.getContractId());
                                //修改合同的负责人,将合同的状态变成进行中,审核流程变成还未审核,审核状态变成默认
                            }
                        }
                    }
                }
            }
            logarr.add(logArrDTO);

        }
        //
        List<ReturnLogArrDTO> returnLogArrDTOS = selectUseToLog(logarr);
        if (saveRemoveInfo(mustUpAgreementIds,mustUpCustomerIds,mustUpContractIds1,mustUpContractIds2,touser,touserName,token) == true){
            //下面是保存 转移记录到分配表
            removeCustomerAddLog(returnLogArrDTOS,touser,Integer.valueOf(userId),userName);
            flag = true;
        }


        return  flag;

    }


    public List<ReturnLogArrDTO> selectUseToLog(List<LogArrDTO> logarr){
        List<ReturnLogArrDTO> resultList = new ArrayList<>();
        //遍历
        if (logarr != null && logarr.size() > 0){
            for (LogArrDTO logArrDTO : logarr) {
                //找出客户的信息
               ReturnLogArrDTO returnLogArrDTO = new ReturnLogArrDTO();
               CustomerInfo customerInfo = findCustomerById(logArrDTO.getCustomerId());
               returnLogArrDTO.setCustomerInfo(customerInfo);

                List<Integer> serviceIds = new ArrayList<>();
                List<Integer> contract1 = new ArrayList<>();
                List<Integer> contract2 = new ArrayList<>();
                if (logArrDTO.getServiceLogDTOS()!= null && logArrDTO.getServiceLogDTOS().size() > 0){
                   List<ServiceLogDTO> serviceContract = logArrDTO.getServiceLogDTOS();
                   for (ServiceLogDTO serviceLogDTO : serviceContract) {
                       serviceIds.add(serviceLogDTO.getServiceContracrId());
                       List<ContracrLogDTO> contracrLogDTOS1 = serviceLogDTO.getContracrLogDTOS();
                       List<ContracrLogDTO> contractLogDTOS2 = serviceLogDTO.getContracrLogDTOS2();
                       for (ContracrLogDTO contracrLogDTO :contracrLogDTOS1 ) {
                           contract1.add(contracrLogDTO.getContractId());
                       }
                       for (ContracrLogDTO contracrLogDTO1 :contractLogDTOS2 ) {
                           contract2.add(contracrLogDTO1.getContractId());
                       }
                   }
               }

               //TODO 调用交易系统开始填写信息
                List<ServiceContract> serviceContracts = null;
                List<Contract> contracts1 = null;
                List<Contract> contracts2 = null;
                returnLogArrDTO.setAgreementInfos(serviceContracts);
                returnLogArrDTO.setContractInfos1(contracts1);
                returnLogArrDTO.setContractInfos2(contracts2);

                resultList.add(returnLogArrDTO);
            }

        }
        return  resultList;

    }

    /**
     *
     * @param mustUpAgreementIds
     * @param mustUpCustomerIds
     * @param mustUpContractIds1
     * @param mustUpContractIds2
     * @param toUser
     * @param toUserName
     * @return
     */
    @Transactional
    public boolean saveRemoveInfo(List<Integer> mustUpAgreementIds,List<Integer> mustUpCustomerIds,
                                  List<Integer> mustUpContractIds1,List<Integer> mustUpContractIds2,
                                  Integer toUser,String toUserName,String token){

        boolean flag = false;
        Date date = new Date();
        //如果客户为要修改的状态,修改客户信
        if (mustUpCustomerIds != null && mustUpCustomerIds.size() > 0){
           //处理
           // String customerIds = listToString(mustUpCustomerIds);
            //查询到这些用户信息
            UcUserDTO userDTO = ucService.getUserInfoByID(token,toUser);
            //开始更新
            Map<String,Object> paramsMap = new HashMap<>();
            //获取当前登录用户信息
            Integer userId = ucService.getUserIdByToken(token);
            paramsMap.put("paramsList",mustUpCustomerIds);
            paramsMap.put("subcompanyId",userDTO.getSub_company_id());
            paramsMap.put("ownerUserId",toUser);
            paramsMap.put("receiveTime",date);
            paramsMap.put("lastUpdateTime",date);
            paramsMap.put("lastUpdateUser",userId);
            customerInfoMapper.batchUpdate(paramsMap);
        }
        // TODO 调用接口
         String serviceContractIds = listToString(mustUpAgreementIds);
         //(serviceContractIds,toUser)
        // TODO 修改进行中的合同
        String contractIds = listToString(mustUpContractIds1);
        //(contractIds,toUser)
        //修改正在结案中的合同
        String contractIdInConclusion = listToString(mustUpContractIds2);
        //(contractIdInConclusion,toUser)
        //TODO 未生效的业绩改成失效

        //TODO 审核全部改为失效
        flag =true;
        return  flag;
    }

    public void  removeCustomerAddLog(List<ReturnLogArrDTO> returnLogArrDTOS,Integer toUser,Integer userId,String userName){
        //遍历添加日志
        Date date = new Date();
        if (returnLogArrDTOS != null && returnLogArrDTOS.size() > 0) {
            for (ReturnLogArrDTO returnLogArrDTO : returnLogArrDTOS){
                 //添加分配日志
                AllocateLog allocateLog = new AllocateLog();
                allocateLog.setCreateTime(new Date());
                allocateLog.setCustomerId(returnLogArrDTO.getCustomerInfo().getId());
                allocateLog.setOldOwnerId(returnLogArrDTO.getCustomerInfo().getOwnUserId());
                allocateLog.setNewOwnerId(toUser);
                allocateLog.setCreateUserId(userId);
                allocateLog.setCreateUserName(userName);
                //json化字符串
                JSONObject jsonObject = (JSONObject)JSONObject.toJSON(returnLogArrDTO);
                allocateLog.setResult(jsonObject.toJSONString());
                allocateLog.setCreateTime(date);
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_9.getCodeDesc());
                Integer result = allocateLogMapper.insert(allocateLog);
                if (result == null){
                    throw  new CronusException(CronusException.Type.CRM_CUSTOMERLOG_ERROR);
                }
                //开始添加客户操作日志
                CustomerInfoLog customerInfoLog = new CustomerInfoLog();
                EntityToDto.customerEntityToCustomerLog(returnLogArrDTO.getCustomerInfo(),customerInfoLog);
                customerInfoLog.setLogCreateTime(date);
                customerInfoLog.setLogDescription("增加一条客户记录");
                customerInfoLog.setLogUserId(userId);
                customerInfoLog.setIsDeleted(0);
                customerInfoLogMapper.addCustomerLog(customerInfoLog);
            }
        }
    }

    public String listToString(List list){

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1)//当循环到最后一个的时候 就不添加逗号,
            {
                str.append(list.get(i));
            } else {
                str.append(list.get(i));
                str.append(",");
            }

        }
        return  str.toString();
    }
}
