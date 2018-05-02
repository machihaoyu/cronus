package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.*;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.CustomerPartDTO;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPUserDto;
import com.fjs.cronus.dto.api.uc.AppUserDto;
import com.fjs.cronus.dto.api.uc.SubCompanyDto;
import com.fjs.cronus.dto.cronus.*;
import com.fjs.cronus.dto.customer.CustomerComDTO;
import com.fjs.cronus.dto.customer.CustomerCountDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.LoanDTO6;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.enums.CustListTimeOrderEnum;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.AllocateLogMapper;
import com.fjs.cronus.mappers.CommunicationLogMapper;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerInfoLog;

import com.fjs.cronus.service.api.OutPutService;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DEC3Util;
import com.fjs.cronus.util.EntityToDto;
import com.fjs.cronus.util.PhoneFormatCheckUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Created by msi on 2017/9/13.
 */
@Service
public class CustomerInfoService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerInfoService.class);
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
    @Autowired
    TheaService theaService;
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    OutPutService outPutService;
    @Autowired
    CommunicationLogMapper communicationLogMapper;
    @Resource
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    SmsService smsService;

    public static final String REDIS_CRONUS_GETHISTORYCOUNT = "cronus_cronus_getHistoryCount_";
    public static final long REDIS_CRONUS_GETHISTORYCOUNT_TIME = 600;
    public static final long NEW_CUSTOMER_MESSAGE_TIME = 86400;

    public List<CustomerInfo> findList() {
        List<CustomerInfo> resultList = new ArrayList();
        resultList = customerInfoMapper.selectAll();
        return resultList;
    }

    public QueryResult customerList(Integer userId, String customerName, String telephonenumber, String utmSource, String ownUserName,
                                    String customerSource, Integer circle, Integer companyId, Integer page, Integer size, Integer remain, String level, String token) {
        QueryResult result = new QueryResult();
        Map<String, Object> paramsMap = new HashMap<>();
        List<CustomerInfo> resultList = new ArrayList<>();
        List<CustomerListDTO> dtoList = new ArrayList<>();
        PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        if (!StringUtils.isEmpty(customerName)) {
            paramsMap.put("customerName", customerName);
        }
        if (!StringUtils.isEmpty(utmSource)) {
            paramsMap.put("utmSource", utmSource);
        }
        if (!StringUtils.isEmpty(ownUserName)) {
            paramsMap.put("ownUserName", ownUserName);
        }
        if (!StringUtils.isEmpty(customerSource)) {
            paramsMap.put("customerSource", customerSource);
        }
        if (circle != null) {
            paramsMap.put("circle", circle);
        }
        if (companyId != null) {
            paramsMap.put("companyId", companyId);
        }
        if (remain != null) {
            paramsMap.put("remain", remain);
        }
        if (!StringUtils.isEmpty(level)) {
            paramsMap.put("level", level);
        }
        //手机需要解密加密
        if (!StringUtils.isEmpty(telephonenumber)) {
            paramsMap.put("telephonenumber", DEC3Util.des3EncodeCBC(telephonenumber));
        }
        //获取下属员工
        List<Integer> ids = ucService.getSubUserByUserId(token, userId);
        paramsMap.put("owerId", ids);
        paramsMap.put("start", (page - 1) * size);
        paramsMap.put("size", size);
        Integer lookphone = Integer.parseInt(userInfoDTO.getUser_info().getLook_phone());
        resultList = customerInfoMapper.customerList(paramsMap);
        Integer count = customerInfoMapper.customerListCount(paramsMap);
        if (resultList != null && resultList.size() > 0) {
            for (CustomerInfo customerInfo : resultList) {
                CustomerListDTO customerDto = new CustomerListDTO();
                EntityToDto.customerEntityToCustomerListDto(customerInfo, customerDto, lookphone, userId);
                //判断自己的lookphone
                dtoList.add(customerDto);
            }
            result.setRows(dtoList);
        }
        result.setTotal(count.toString());
        return result;
    }


    public QueryResult customerListNew(Integer userId, String customerName, String telephonenumber, String utmSource, String ownUserName,
                                       String customerSource, Integer circle, Integer companyId, Integer page, Integer size, Integer remain,
                                       String level, String token, String orderField,String sort,String cooperationStatus,Integer communication_order,
                                       String createTimeStart,String createTimeEnd) {
        QueryResult result = new QueryResult();
        Map<String, Object> paramsMap = new HashMap<>();
        List<CustomerInfo> resultList = new ArrayList<>();
        List<CustomerListDTO> dtoList = new ArrayList<>();
        List<String> channleList = new ArrayList<>();
        PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        if (!StringUtils.isEmpty(customerName)) {
            paramsMap.put("customerName", customerName);
        }
        if (!StringUtils.isEmpty(utmSource)) {
            List<String> utmList = theaClientService.getChannelNameListByMediaName(token,utmSource);
            if (utmList == null || utmList.size() == 0){
                result.setRows(resultList);
                result.setTotal("0");
                return result;
            }
            paramsMap.put("utmSources",utmList);
        }
        if (!StringUtils.isEmpty(ownUserName)) {
            paramsMap.put("ownUserName", ownUserName);
        }
        if (!StringUtils.isEmpty(customerSource)) {
            paramsMap.put("customerSource", customerSource);
        }
        if (circle != null) {
            paramsMap.put("circle", circle);
        }
        if (companyId != null) {
            paramsMap.put("companyId", companyId);
        }
        if (remain != null) {
            paramsMap.put("remain", remain);
        }
        if (!StringUtils.isEmpty(level)) {
            paramsMap.put("level", level);
        }
        if (!StringUtils.isEmpty(cooperationStatus)){
            paramsMap.put("cooperationStatus", cooperationStatus);
        }
        if (communication_order != null){
            paramsMap.put("communication_order", communication_order);
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(createTimeStart)){
            paramsMap.put("createTimeStart",createTimeStart + " 00:00:00");
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(createTimeEnd)){
            paramsMap.put("createTimeEnd",createTimeEnd + " 23:59:59");
        }
        //手机需要解密加密
        if (!StringUtils.isEmpty(telephonenumber)) {
            paramsMap.put("telephonenumber", DEC3Util.des3EncodeCBC(telephonenumber));
        }
        //排序---xdj-----
        if (!StringUtils.isEmpty(orderField)) {
            if (StringUtils.isEmpty(sort)){
                sort = "desc";
            }
            paramsMap.put("order", orderField + " " + sort);
        }
        //排序---xdj-----
        //获取下属员工
        List<Integer> ids = ucService.getSubUserByUserId(token, userId);
        paramsMap.put("owerId", ids);
        paramsMap.put("start", (page - 1) * size);
        paramsMap.put("size", size);
        Integer lookphone = Integer.parseInt(userInfoDTO.getUser_info().getLook_phone());
        resultList = customerInfoMapper.customerList(paramsMap);
        Integer count = customerInfoMapper.customerListCount(paramsMap);
        if (resultList != null && resultList.size() > 0) {
            for (CustomerInfo customerInfo : resultList) {
                if (!channleList.contains(customerInfo.getUtmSource())){
                    channleList.add(customerInfo.getUtmSource());
                }
                CustomerListDTO customerDto = new CustomerListDTO();
                EntityToDto.customerEntityToCustomerListDto(customerInfo, customerDto, lookphone, userId);
                //判断自己的lookphone
                dtoList.add(customerDto);
            }
            //屏蔽媒体
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("channelNames",channleList);
            Map<String,String> mediaMap = theaClientService.getMediaName(token,jsonObject);
            for (CustomerListDTO customerListDTO : dtoList ){
                System.out.println(mediaMap.get(customerListDTO.getUtmSource()));
                customerListDTO.setUtmSource(mediaMap.get(customerListDTO.getUtmSource()));
            }
            result.setRows(dtoList);
        }
        result.setTotal(count.toString());
        return result;
    }


    @Transactional
    public CronusDto addCustomer(CustomerDTO customerDTO, String token) {
        CronusDto cronusDto = new CronusDto();
        //判断必传字段*/
        //json转map 参数，教研参数
        UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO.getUser_id() == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户信息出错!");
        }
        validAddData(customerDTO);
        //实体与DTO相互转换
        CustomerInfo customerInfo = new CustomerInfo();
        EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
        //新加字段
        List<EmplouInfo> emplouInfos = customerDTO.getEmployedInfo();
        //转Json在转String
        if (emplouInfos != null && emplouInfos.size() > 0) {
            String jsonString = JSONArray.toJSONString(emplouInfos);
            customerInfo.setEmployedInfo(jsonString);
        }
        customerInfo.setRetirementWages(customerDTO.getRetirementWages());
        Date date = new Date();
        //刚申请的客户
        customerInfo.setCompanyId(Integer.valueOf(userInfoDTO.getCompany_id()));
        customerInfo.setSubCompanyId(Integer.valueOf(userInfoDTO.getSub_company_id()));
        customerInfo.setCustomerType(CommonConst.CUSTOMER_TYPE_MIND);
        customerInfo.setRemain(CommonConst.REMAIN_STATUS_NO);
        customerInfo.setConfirm(CommonConst.CONFIRM__STATUS_NO);
        customerInfo.setLastUpdateUser(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfo.setCreateTime(date);
        customerInfo.setCreateUser(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfo.setLastUpdateTime(date);
        customerInfo.setIsDeleted(0);
        customerInfo.setReceiveId(0);
        customerInfo.setAutostatus(1);//自动分配
        customerInfo.setCommunicateId(0);
        customerInfoMapper.insertCustomer(customerInfo);
        if (customerInfo.getId() == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR);
        }
        //开始插入log表
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("增加一条客户记录");
        customerInfoLog.setLogUserId(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        cronusDto.setResult(ResultResource.CODE_SUCCESS);
        cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        cronusDto.setData(customerInfo.getId());
        return cronusDto;
    }

    @Transactional
    public CronusDto addOcdcCustomer(CustomerDTO customerDTO, String token) {
        CronusDto cronusDto = new CronusDto();
        //实体与DTO相互转换
        CustomerInfo customerInfo = new CustomerInfo();
        EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
        //新加字段
        List<EmplouInfo> emplouInfos = customerDTO.getEmployedInfo();
        //转Json在转String
        if (emplouInfos != null && emplouInfos.size() > 0) {
            String jsonString = JSONArray.toJSONString(emplouInfos);
            customerInfo.setEmployedInfo(jsonString);
        }
        customerInfo.setRetirementWages(customerDTO.getRetirementWages());
        Date date = new Date();
        //刚申请的客户
        customerInfo.setCustomerType(CommonConst.CUSTOMER_TYPE_MIND);
//        customerInfo.setRemain(CommonConst.REMAIN_STATUS_NO);
        customerInfo.setConfirm(CommonConst.CONFIRM__STATUS_NO);
        customerInfo.setReceiveTime(date);
        customerInfo.setFirstCommunicateTime(date);
        customerInfo.setLastUpdateUser(0);
        customerInfo.setCreateTime(date);
        customerInfo.setCreateUser(0);
        customerInfo.setLastUpdateTime(date);
        customerInfo.setIsDeleted(0);
        customerInfo.setReceiveId(0);
        customerInfo.setAutostatus(1);//自动分配
        customerInfo.setCommunicateId(0);
        customerInfo.setFirstAllocateTime(date);
        customerInfoMapper.insertCustomer(customerInfo);
        if (customerInfo.getId() == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR);
        }
        //开始插入log表
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("增加一条客户记录");
        customerInfoLog.setLogUserId(0);//系统
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        cronusDto.setResult(ResultResource.CODE_SUCCESS);
        cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        cronusDto.setData(customerInfo.getId());
        return cronusDto;
    }

    @Transactional
    public CronusDto addCRMCustomer(AddCustomerDTO customerDTO, UserInfoDTO userInfoDTO, String token) {
        CronusDto cronusDto = new CronusDto();
        //判断必传字段*/
        //json转map 参数，教研参数
        if (userInfoDTO.getUser_id() == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户信息出错!");
        }
        String customerName = customerDTO.getCustomerName();
        String telephonenumber = customerDTO.getTelephonenumber();
        if (customerName == null || "".equals(customerName)) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERNAME_ERROR);
        }
        if (telephonenumber == null || "".equals(telephonenumber)) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONE_ERROR);
        }
        if (PhoneFormatCheckUtils.isChinaPhoneLegal(telephonenumber) == false) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONE_ERROR);
        }
        //判断手机号是否被注册
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("telephonenumber", DEC3Util.des3EncodeCBC(telephonenumber));
        paramsMap.put("start", 0);
        paramsMap.put("size", 10);
        List<CustomerInfo> customerInfos = customerInfoMapper.customerList(paramsMap);
        if (customerInfos.size() > 0) {
            cronusDto.setResult(ResultResource.CODE_OTHER_ERROR);
            cronusDto.setMessage(ResultResource.PHNOEERROR);
            return cronusDto;
        }

        //实体与DTO相互转换
        //对手机号加密
        CustomerInfo customerInfo = new CustomerInfo();
        Date date = new Date();
        customerInfo.setCustomerName(customerDTO.getCustomerName());
        String telephone = DEC3Util.des3EncodeCBC(customerDTO.getTelephonenumber());
        customerInfo.setTelephonenumber(telephone);
        customerInfo.setCustomerSource(customerDTO.getCustomerSource());
        customerInfo.setUtmSource(customerDTO.getUtmSource());
        customerInfo.setLoanAmount(customerDTO.getLoanAmount());
        customerInfo.setCreateTime(date);
        customerInfo.setCreateUser(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfo.setLastUpdateTime(date);
        customerInfo.setLastUpdateUser(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfo.setCustomerType(ResultResource.CUSTOMERTYPE);
        customerInfo.setIsDeleted(0);
        customerInfo.setCompanyId(Integer.valueOf(userInfoDTO.getCompany_id()));
        customerInfo.setSubCompanyId(Integer.valueOf(userInfoDTO.getSub_company_id()));
        customerInfo.setRemain(CommonConst.REMAIN_STATUS_NO);
        customerInfo.setConfirm(CommonConst.CONFIRM__STATUS_NO);
        customerInfo.setReceiveId(0);
        customerInfo.setCommunicateId(0);
        customerInfo.setOwnUserId(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfo.setOwnUserName(userInfoDTO.getName());
        //设置城市 应设置为当前人所在分公司的城市
        String city = ucService.getCityByUserid(token,Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfo.setCity(city);
        customerInfo.setAutostatus(0);
        customerInfoMapper.insertCustomer(customerInfo);
        if (customerInfo.getId() == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR);
        }
        //开始插入log表
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("增加一条客户记录");
        customerInfoLog.setLogUserId(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        //需要像ocdc推送客户
        outPutService.synchronToOcdc(customerInfo);
        cronusDto.setResult(ResultResource.CODE_SUCCESS);
        cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        cronusDto.setData(customerInfo.getId());
        return cronusDto;
    }

    public CronusDto<Integer> fingBytelephone(String telephonenumber) {
        CronusDto resultDto = new CronusDto();
        //手机需要加密
        Map<String, Object> paramsMap = new HashMap<>();
        CustomerDTO dto = new CustomerDTO();
        String encryptTelephone = DEC3Util.des3EncodeCBC(telephonenumber);
        paramsMap.put("telephonenumber", encryptTelephone);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfo.getId());
        return resultDto;
    }

    public CronusDto findCustomerListByIds(String customerids, String customerName) {
        CronusDto resultDto = new CronusDto();
        Map<String, Object> paramsMap = new HashMap<>();
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
        if (!StringUtils.isEmpty(customerName)) {
            paramsMap.put("customerName", customerName);
        }
        List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerListByFeild(paramsMap);
        //遍历
        List<CustomerDTO> customerDtos = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfoList) {
            CustomerDTO customerDto = new CustomerDTO();
            EntityToDto.customerEntityToCustomerDto(customerInfo, customerDto);
            String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
            customerDto.setTelephonenumber(telephone);
            customerDto.setUtmSource(customerInfo.getUtmSource());
            customerDtos.add(customerDto);
        }
        if (customerDtos != null && customerDtos.size() > 0) {
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setData(customerDtos);
        }
        return resultDto;
    }

    public CronusDto<CustomerDTO> editCustomer(Integer customerId, String token) {
        CronusDto<CustomerDTO> resultDto = new CronusDto();
        Map<String, Object> paramsMap = new HashMap<>();
        //获取业务员信息
        PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
        }
        Integer lookphone = Integer.valueOf(userInfoDTO.getUser_info().getLook_phone());
        paramsMap.put("id", customerId);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        CustomerDTO customerDto = new CustomerDTO();
        EntityToDto.customerEntityToCustomerDto(customerInfo, customerDto);
        //对手机号进行加
        String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
        customerDto.setTelephonenumber(telephone);
        customerDto.setRetirementWages(customerInfo.getRetirementWages());
        String employedInfo = customerInfo.getEmployedInfo();
        List<EmplouInfo> emplouInfos = new ArrayList<>();
        if (!StringUtils.isEmpty(employedInfo)) {
            JSONArray jsonArray = JSONArray.parseArray(employedInfo);
            emplouInfos = jsonArray.toJavaList(EmplouInfo.class);
            customerDto.setEmployedInfo(emplouInfos);
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerDto);
        return resultDto;
    }

    /**
     * 提交编辑用户
     *
     * @return
     */
    @Transactional
    public CronusDto editCustomerOk(CustomerDTO customerDTO, UserInfoDTO userInfoDTO, String token) {
        CronusDto resultDto = new CronusDto();
        //校验权限
        //校验参数手机号不更新
        Integer user_id = Integer.valueOf(userInfoDTO.getUser_id());
        if (user_id == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "信息出错!");
        }
        Map<String, Object> paramsMap = new HashMap<>();
        if (customerDTO.getId() == null || "".equals(customerDTO.getId())) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        if (customerDTO.getCustomerName() == null || "".equals(customerDTO.getCustomerName())) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERNAME_ERROR);
        }
        if (customerDTO.getHouseStatus() == null || "".equals(customerDTO.getHouseStatus())) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEHOUSE_ERROR);
        }
        Integer id = customerDTO.getId();
        paramsMap.put("id", id);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Date date = new Date();
        EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
        customerInfo.setRetirementWages(customerDTO.getRetirementWages());
        List<EmplouInfo> emplouInfos = customerDTO.getEmployedInfo();
        if (emplouInfos != null && emplouInfos.size() > 0) {
            String jsonString = JSONArray.toJSONString(emplouInfos);
            customerInfo.setEmployedInfo(jsonString);
        }
        customerInfo.setLastUpdateTime(date);
        customerInfo.setLastUpdateUser(user_id);
        customerInfoMapper.updateCustomer(customerInfo);
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("编辑客户信息");
        customerInfoLog.setLogUserId(user_id);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfo.getId());
        return resultDto;
    }

    public CronusDto editCustomerSys(CustomerDTO customerDTO, String token) {
        CronusDto resultDto = new CronusDto();
        //校验参数手机号不更新
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "信息出错!");
        }
        Map<String, Object> paramsMap = new HashMap<>();
        Integer id = customerDTO.getId();
        paramsMap.put("id", id);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Date date = new Date();
        BeanUtils.copyProperties(customerDTO, customerInfo);

        customerInfo.setLastUpdateTime(date);
        customerInfo.setConfirm(0);
        customerInfo.setLastUpdateUser(user_id);
        customerInfo.setClickCommunicateButton(0);
        customerInfoMapper.updateCustomerSys(customerInfo);
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("自动分配更新客户");
        customerInfoLog.setLogUserId(user_id);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfo.getId());
        return resultDto;
    }

    public List findCustomerByType(String customerType) {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Integer> customerInfoList = new ArrayList<>();
        if (!StringUtils.isEmpty(customerType)) {
            paramsMap.put("customerType", customerType);
        } else {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        customerInfoList = customerInfoMapper.findCustomerByType(paramsMap);
        //遍历
        if (customerInfoList != null && customerInfoList.size() > 0) {
            return customerInfoList;
        }
        return customerInfoList;

    }

    public void validAddData(CustomerDTO customerInfo) {
        String customerName = customerInfo.getCustomerName();
        String telephonenumber = customerInfo.getTelephonenumber();
        Integer customerId = customerInfo.getId();

        if (customerName == null || "".equals(customerName)) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERNAME_ERROR);
        }
        if (telephonenumber == null || "".equals(telephonenumber)) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONE_ERROR);
        }
        if (PhoneFormatCheckUtils.isChinaPhoneLegal(telephonenumber) == false) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONE_ERROR);
        }
        //判断手机号是否被注册
        if (customerId == null) {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("telephonenumber", DEC3Util.des3EncodeCBC(telephonenumber));
            paramsMap.put("start", 0);
            paramsMap.put("size", 10);
            List<CustomerInfo> customerInfos = customerInfoMapper.customerList(paramsMap);
            if (customerInfos.size() > 0) {
                throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONERE_ERROR);
            }
        }
    }

    public CronusDto<CustomerDTO> findCustomerByFeild(Integer customerId, String telephoneNumber) {
        CronusDto resultDto = new CronusDto();
        Map<String, Object> paramsMap = new HashMap<>();
        if (!StringUtils.isEmpty(customerId)) {
            paramsMap.put("id", customerId);
        }
        if (!StringUtils.isEmpty(telephoneNumber)) {
            //手机号加密

            paramsMap.put("telephonenumber", DEC3Util.des3EncodeCBC(telephoneNumber));
        }
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        CustomerDTO customerDto = new CustomerDTO();
        EntityToDto.customerEntityToCustomerDto(customerInfo, customerDto);
        //d对手机进行解密
        String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
        customerDto.setTelephonenumber(telephone);
        customerDto.setRetirementWages(customerInfo.getRetirementWages());
        String employedInfo = customerInfo.getEmployedInfo();
        List<EmplouInfo> emplouInfos = new ArrayList<>();
        if (!StringUtils.isEmpty(employedInfo)) {
            JSONArray jsonArray = JSONArray.parseArray(employedInfo);
            emplouInfos = jsonArray.toJavaList(EmplouInfo.class);
            customerDto.setEmployedInfo(emplouInfos);
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerDto);
        return resultDto;
    }

    public CronusDto findCustomerByCity(String city) {
        CronusDto resultDto = new CronusDto();
        Map<String, Object> paramsMap = new HashMap<>();
        if (!StringUtils.isEmpty(city)) {
            paramsMap.put("city", city);
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
        return resultDto;
    }

    public CronusDto findCustomerByOtherCity(String citys) {
        CronusDto resultDto = new CronusDto();
        //处理参数
        String[] strArray = null;
        strArray = citys.split(",");
        List<String> list = new ArrayList();
        for (int i = 0; i < strArray.length; i++) {
            list.add("'" + strArray[i] + "'");
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("cityList", list);
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
        return resultDto;
    }

    @Transactional
    public CronusDto<Boolean> editCustomerType(Integer customer_id, Integer user_id) {
        CronusDto resultDto = new CronusDto();
        //根据uid查询到客户相关信息
        boolean flag = false;
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("id", customer_id);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERIDENTITYINFO_ERROR);
        }
        //开始更改信息由意向客户改为协议客户
        String customerType = customerInfo.getCustomerType();
        if (customerType.equals(CustomerEnum.intentional_customer.getName())) {
            //改成协议客户
            customerInfo.setCustomerType(CustomerEnum.agreement_customer.getName());
            customerInfo.setCustomerLevel(CustomerEnum.agreement_customer.getName());
            customerInfoMapper.updateCustomer(customerInfo);
            CustomerInfoLog customerInfoLog = new CustomerInfoLog();
            Date date = new Date();
            EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
            customerInfoLog.setLogCreateTime(date);
            customerInfoLog.setLogDescription("签章协议");
            customerInfoLog.setLogUserId(user_id);
            customerInfoLog.setIsDeleted(0);
            customerInfoLogMapper.addCustomerLog(customerInfoLog);
        }
        //开始更新
        //生成日志记录
        flag = true;
        resultDto.setData(flag);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return resultDto;
    }

    @Transactional
    public CronusDto<Boolean> editCustomerTypeTOConversion(Integer customer_id, Integer user_id) {
        CronusDto resultDto = new CronusDto();
        //根据uid查询到客户相关信息
        boolean flag = false;
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("id", customer_id);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERIDENTITYINFO_ERROR);
        }
        //协议客户改为成交用户
        String customerType = customerInfo.getCustomerType();
        if (customerType.equals(CustomerEnum.agreement_customer.getName())) {
            //成交用户
            customerInfo.setCustomerType(CustomerEnum.conversion_customer.getName());
            customerInfo.setCustomerLevel(CustomerEnum.conversion_customer.getName());
            //开始更新
            customerInfoMapper.updateCustomer(customerInfo);
            //生成日志记录
            CustomerInfoLog customerInfoLog = new CustomerInfoLog();
            Date date = new Date();
            EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
            customerInfoLog.setLogCreateTime(date);
            customerInfoLog.setLogDescription("成交用户");
            customerInfoLog.setLogUserId(user_id);
            customerInfoLog.setIsDeleted(0);
            customerInfoLogMapper.addCustomerLog(customerInfoLog);
        }
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
        //对手机号进行解密
        // customerInfo.setTelephonenumber(DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber()));
        return customerInfo;
    }

    public QueryResult<CustomerListDTO> allocationCustomerList(String customerName, String utmSource, String customerSource, Integer autostatus, Integer page, Integer size, Integer type, String telephonenumber, String token) {
        List<CustomerInfo> resultList = new ArrayList<>();
        Map<String, Object> paramsMap = new HashMap<>();
        List<CustomerListDTO> doList = new ArrayList<>();
        QueryResult<CustomerListDTO> result = new QueryResult<>();
        PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
        }
        Integer lookphone = Integer.parseInt(userInfoDTO.getUser_info().getLook_phone());
        Integer userId = Integer.parseInt(userInfoDTO.getUser_info().getUser_id());
        //获取下属id
        List<Integer> ownerIds = ucService.getSubUserByUserId(token, userId);
        Integer count = null;
        if (!StringUtils.isEmpty(customerName)) {
            paramsMap.put("customerName", customerName);
        }
        if (!StringUtils.isEmpty(utmSource)) {
            if ("自申请".equals(utmSource)){
                utmSource = "c-app";
            }
            paramsMap.put("utmSource", utmSource);
        }
        if (!StringUtils.isEmpty(telephonenumber)) {
            paramsMap.put("telephonenumber", telephonenumber);
        }
        if (!StringUtils.isEmpty(customerSource)) {
            paramsMap.put("customerSource", customerSource);
        }
        if (autostatus != null) {
            paramsMap.put("autostatus", autostatus);
        }
        if (ownerIds != null && ownerIds.size() > 0) {
            paramsMap.put("ownerIds", ownerIds);
        }
        paramsMap.put("start", (page - 1) * size);
        paramsMap.put("size", size);
        if (type == 1) {//已沟通客户 判断沟通时间不为null;
            resultList = customerInfoMapper.communicatedList(paramsMap);
            count = customerInfoMapper.communicatedListCount(paramsMap);
        } else {
            resultList = customerInfoMapper.allocationCustomerList(paramsMap);
            count = customerInfoMapper.allocationCustomerListCount(paramsMap);
        }
        if (resultList != null && resultList.size() > 0) {
            for (CustomerInfo customerInfo : resultList) {
                CustomerListDTO customerDto = new CustomerListDTO();
                EntityToDto.customerEntityToCustomerListDto(customerInfo, customerDto, lookphone, userId);
                doList.add(customerDto);
            }
            result.setRows(doList);
            result.setTotal(count.toString());
        }
        result.setRows(doList);
        result.setTotal(count.toString());
        return result;
    }

    /**
     * 增加排序
     * @param customerName
     * @param utmSource
     * @param customerSource
     * @param autostatus
     * @param page
     * @param size
     * @param type
     * @param telephonenumber
     * @param token
     * @return
     */
    public QueryResult<CustomerListDTO> allocationCustomerListNew(String customerName, String utmSource, String customerSource, Integer autostatus, Integer page, Integer size, Integer type, String telephonenumber,
                                                                  String orderField,String sort,String token,String createTimeStart,String createTimeEnd) {
        List<CustomerInfo> resultList = new ArrayList<>();
        Map<String, Object> paramsMap = new HashMap<>();
        List<CustomerListDTO> doList = new ArrayList<>();
        List<String> channleList = new ArrayList<>();
        QueryResult<CustomerListDTO> result = new QueryResult<>();
        PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
        }
        Integer lookphone = Integer.parseInt(userInfoDTO.getUser_info().getLook_phone());
        Integer userId = Integer.parseInt(userInfoDTO.getUser_info().getUser_id());
        //获取下属id
        List<Integer> ownerIds = ucService.getSubUserByUserId(token, userId);
        Integer count = null;
        if (!StringUtils.isEmpty(customerName)) {
            paramsMap.put("customerName", customerName);
        }
        if (!StringUtils.isEmpty(utmSource)) {
            if ("自申请".equals(utmSource)){
                utmSource = "c-app";
                paramsMap.put("utmSource", utmSource);
            }else {
                List<String> utmList = theaClientService.getChannelNameListByMediaName(token,utmSource);
                if (utmList == null || utmList.size() == 0){
                    result.setRows(doList);
                    result.setTotal("0");
                    return result;
                }
                paramsMap.put("utmSources",utmList);
            }
        }

        if (!StringUtils.isEmpty(telephonenumber)) {
            paramsMap.put("telephonenumber", telephonenumber);
        }
        if (!StringUtils.isEmpty(customerSource)) {
            paramsMap.put("customerSource", customerSource);
        }
        if (autostatus != null) {
            paramsMap.put("autostatus", autostatus);
        }
        if (ownerIds != null && ownerIds.size() > 0) {
            paramsMap.put("ownerIds", ownerIds);
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(createTimeStart)){
            paramsMap.put("createTimeStart",createTimeStart + " 00:00:00");
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(createTimeEnd)){
            paramsMap.put("createTimeEnd",createTimeEnd + " 23:59:59");
        }
        //排序---zl-----
        if (!StringUtils.isEmpty(orderField) && CustListTimeOrderEnum.getEnumByCode(orderField) != null) {
            if (StringUtils.isEmpty(sort)){
                sort = "desc";
            }
            paramsMap.put("order", orderField + " " + sort);
        }
        paramsMap.put("start", (page - 1) * size);
        paramsMap.put("size", size);
        if (type == 1) {//已沟通客户 判断沟通时间不为null;
            resultList = customerInfoMapper.communicatedList(paramsMap);
            count = customerInfoMapper.communicatedListCount(paramsMap);
        } else {
            resultList = customerInfoMapper.allocationCustomerList(paramsMap);
            count = customerInfoMapper.allocationCustomerListCount(paramsMap);
        }
        if (resultList != null && resultList.size() > 0) {
            for (CustomerInfo customerInfo : resultList) {
                if (!channleList.contains(customerInfo.getUtmSource())){
                    channleList.add(customerInfo.getUtmSource());
                }
                CustomerListDTO customerDto = new CustomerListDTO();
                EntityToDto.customerEntityToCustomerListDto(customerInfo, customerDto, lookphone, userId);
                doList.add(customerDto);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("channelNames",channleList);
            Map<String,String> mediaMap = theaClientService.getMediaName(token,jsonObject);
            for (CustomerListDTO customerListDTO : doList ){
                System.out.println(mediaMap.get(customerListDTO.getUtmSource()));
                customerListDTO.setUtmSource(mediaMap.get(customerListDTO.getUtmSource()));
            }
            result.setRows(doList);
            result.setTotal(count.toString());
        }
        result.setRows(doList);
        result.setTotal(count.toString());
        return result;
    }

    //不分页查询客户
    public List<CustomerInfo> listByCondition(CustomerInfo customerInfo, UserInfoDTO userInfoDTO, String token, String systemName) {

        List<CustomerInfo> resultList = new ArrayList<>();
        List ids = new ArrayList();
        Map<String, Object> paramsMap = new HashMap<>();
        //判断当前登录用户所属公司
        Integer companyId = null;
        if (!StringUtils.isEmpty(userInfoDTO.getCompany_id())) {
            companyId = Integer.parseInt(userInfoDTO.getCompany_id());
            customerInfo.setCompanyId(companyId);
        }
        //得到下属员工
        ids.add(Integer.valueOf(userInfoDTO.getUser_id()));
        paramsMap.put("owerId", ids);
        if (customerInfo != null) {
            if (customerInfo.getRemain() != null) {
                paramsMap.put("remain", customerInfo.getRemain());
            }
            if (customerInfo.getCompanyId() != null) {
                paramsMap.put("companyId", companyId);
            }
            if (customerInfo.getCustomerType() != null) {
                paramsMap.put("customerType", customerInfo.getCustomerType());
            }
        }
        resultList = customerInfoMapper.findCustomerListByFeild(paramsMap);

        return resultList;
    }

    @Transactional
    public CronusDto keepCustomer(Integer customerId, UserInfoDTO userInfoDTO, String token) {
        logger.warn("开始保留客户Service-------》");
        CronusDto resultDto = new CronusDto();
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
        logger.warn("查询客户结束-------》");
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
        logger.warn("更新客户结束-------》");
        //插入日志
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_11.getCodeDesc());
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);

        resultDto.setData(true);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        return resultDto;
    }

    public CustomerSourceDTO quitCustomerSource(Integer userId, String token) {
        //查询数据库中所有的组
        CustomerSourceDTO customerSourceDTO = new CustomerSourceDTO();
        List<String> customerSourceByGroup = customerInfoMapper.customerSourceByGroup();
        //获取当前登录用户能管理的总公司

        List<SubCompanyDto> companys = ucService.getAllCompanyByUserId(token, userId, CommonConst.SYSTEM_NAME_ENGLISH);

        customerSourceDTO.setCompanyDtos(companys);
        customerSourceDTO.setSource(customerSourceByGroup);
        return customerSourceDTO;
    }

    public List<String> getAllCustomerSource() {
        List<String> customerSourceByGroup = new ArrayList<>();
        customerSourceByGroup = customerInfoMapper.customerSourceByGroup();
        return customerSourceByGroup;
    }

    public QueryResult<CustomerListDTO> resignCustomerList(String token, String customerName, String telephonenumber, String utmSource,String media, String ownUserName, String customerSource,
                                                           String level, Integer companyId, Integer page, Integer size,String createTimeStart,String createTimeEnd) {
        QueryResult<CustomerListDTO> queryResult = new QueryResult();
        List<CustomerListDTO> resultList = new ArrayList<>();
        List<String> channleList = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        //获取离职员工的ids
        List<PHPUserDto> userDtos = ucService.getUserByIds(token, null, null, null, "eq", null, null, null, 3);
        if (userDtos != null && userDtos.size() > 0) {
            for (PHPUserDto userDto : userDtos) {
                ids.add(Integer.valueOf(userDto.getUser_id()));
            }
            paramMap.put("owerId", ids);
            if (!StringUtils.isEmpty(customerName)) {
                paramMap.put("customerName", customerName);
            }

            if (!StringUtils.isEmpty(telephonenumber)) {
                paramMap.put("telephonenumber", DEC3Util.des3EncodeCBC(telephonenumber));
            }
            if (!StringUtils.isEmpty(media)){
                //TODO 通过媒体获取渠道
                List<String> utmList = theaClientService.getChannelNameListByMediaName(token,media);
                if (utmList == null || utmList.size() == 0){
                    queryResult.setRows(resultList);
                    queryResult.setTotal("0");
                    return queryResult;
                }
                paramMap.put("utmSources",utmList);
            }
            if (!StringUtils.isEmpty(utmSource)){
                paramMap.put("utmSource",utmSource);
            }
            if (!StringUtils.isEmpty(ownUserName)) {
                paramMap.put("ownUserName", ownUserName);
            }
            if (!StringUtils.isEmpty(customerSource)) {
                paramMap.put("customerSource", customerSource);
            }
            if (!StringUtils.isEmpty(level)) {
                paramMap.put("level", level);
            }
            if (!StringUtils.isEmpty(companyId)) {
                paramMap.put("companyId", companyId);
            }
            if (!StringUtils.isEmpty(createTimeStart)){
                paramMap.put("createTimeStart",createTimeStart + " 00:00:00");
            }
            if (!StringUtils.isEmpty(createTimeEnd)){
                paramMap.put("createTimeEnd",createTimeEnd + " 23:59:59");
            }
            paramMap.put("start", (page - 1) * size);
            paramMap.put("size", size);
            PHPLoginDto phpLoginDto = ucService.getAllUserInfo(token, CommonConst.SYSTEM_NAME_ENGLISH);
            if (phpLoginDto == null) {
                throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
            }
            List<CustomerInfo> customerInfoList = customerInfoMapper.customerList(paramMap);
            Integer lookphone = Integer.parseInt(phpLoginDto.getUser_info().getLook_phone());
            Integer userId = Integer.parseInt(phpLoginDto.getUser_info().getLook_phone());
            if (customerInfoList != null && customerInfoList.size() > 0) {

                for (CustomerInfo customerInfo : customerInfoList) {
                    CustomerListDTO customerDto = new CustomerListDTO();
                    if (!channleList.contains(customerInfo.getUtmSource())){
                        channleList.add(customerInfo.getUtmSource());
                    }
                    EntityToDto.customerEntityToCustomerListDto(customerInfo, customerDto, lookphone, userId);
                    String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
                    String phoneNumber = telephone.substring(0, 7) + "****";
                    customerDto.setTelephonenumber(phoneNumber);
                    resultList.add(customerDto);
                }
                queryResult.setRows(resultList);
                Integer count = customerInfoMapper.customerListCount(paramMap);
                queryResult.setTotal(count.toString());
            }

        }

        return queryResult;
    }

    @Transactional
    public boolean cancelkeepCustomer(Integer customerId, UserInfoDTO userInfoDTO, String token) {
//        boolean flag = false;
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
        customerInfo.setRemain(0);
        customerInfo.setRemain(CommonConst.REMAIN_STATUS_NO);
        customerInfo.setLastUpdateUser(userId);
        Date date = new Date();
        customerInfo.setLastUpdateTime(date);
        customerInfoMapper.updateCustomer(customerInfo);

        //插入日志
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_12.getCodeDesc());
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
//        TheaApiDTO resultDto = theaService.cancelLoanByCustomerId(token, customerId.toString());
//        if (resultDto != null && resultDto.getResult() == 0) {
//            flag = true;
//        }
        return true;
    }

    public List<CustomerInfo> getByIds(String ids) {
        List<CustomerInfo> resultList = new ArrayList<>();
        Map<String, Object> paramsMap = new HashMap<>();
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
        if (resultList != null && resultList.size() > 0) {
            for (CustomerInfo customerInfo : resultList) {
                String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
                customerInfo.setTelephonenumber(telephone);
            }
        }
        return resultList;
    }

    @Transactional
    public CronusDto removeCustomer(String ids, String token) {
        boolean flag = false;
        CronusDto cronusDto = new CronusDto();
        if (StringUtils.isEmpty(ids)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        //根据编号查询到客户
        List<CustomerInfo> customerInfoList = getByIds(ids);
        if (customerInfoList == null || customerInfoList.size() == 0) {
            throw new CronusException(CronusException.Type.MESSAGE_NOT_EXIST_LOAN);
        }
        for (CustomerInfo customerInfo : customerInfoList) {
            if (customerInfo.getRemain() == CommonConst.CONFIRM__STATUS_NO) {
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage("" + customerInfo.getCustomerName() + "--已被保留,不能丢回!");
                return cronusDto;
            }
            //判断是否是意向客户
            if (!customerInfo.getCustomerType().equals(CommonConst.CUSTOMER_TYPE_MIND)) {
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage("" + customerInfo.getCustomerName() + "该客户不是意向客户,不能丢回");
                return cronusDto;
            }
            //负责人是不是自己
            if (!StringUtils.isEmpty(userInfoDTO.getUser_id()) && customerInfo.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id())) {
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage("" + customerInfo.getCustomerName() + "--必须负责人自己才能操作!");
                return cronusDto;
            }
            //是否沟通过
            List<CommunicationLog> communicationLogList = new ArrayList<CommunicationLog>();
            if (!StringUtils.isEmpty(userInfoDTO.getUser_id())) {
                communicationLogList = communicationLogService.listByCustomerIdAndUserId(customerInfo.getId(), Integer.parseInt(userInfoDTO.getUser_id()), token);
            }
            if (communicationLogList.size() == 0) {
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage("" + customerInfo.getCustomerName() + "--领取后, 必须添加过沟通才能扔回公盘!");
                return cronusDto;
            }

        }
        //开始批量移除客户到公盘
        batchRemove(ids, Integer.valueOf(userInfoDTO.getUser_id()));
        //开始废弃交易
        TheaApiDTO resultDto = theaService.cancelLoanByCustomerId(token, ids);
        if (resultDto == null || resultDto.getResult() != 0) {
            throw new CronusException(CronusException.Type.CRM_CONNECT_ERROR);
        }
        flag = true;
        cronusDto.setData(flag);
        cronusDto.setResult(CommonMessage.REMOVE_SUCCESS.getCode());
        cronusDto.setMessage(CommonMessage.REMOVE_SUCCESS.getCodeDesc());
        return cronusDto;
    }

    @Transactional
    public void batchRemove(String ids, Integer userId) {
        Date date = new Date();
        String[] arr = ids.split(",");
        Map<String, Object> paramsMap = new HashMap<>();
        List<Integer> paramsList = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            String[] strArray = null;
            strArray = ids.split(",");
            for (int i = 0; i < strArray.length; i++) {
                paramsList.add(Integer.parseInt(strArray[i]));
            }
        }
        if (paramsList != null && paramsList.size() > 0) {
            //开始插入日志
            for (Integer id : paramsList) {
                //查询到此客户
                CustomerInfo customerInfo = findCustomerById(id);
                if (customerInfo != null) {
                    //开始插入日志
                    CustomerInfoLog customerInfoLog = new CustomerInfoLog();
                    EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
                    customerInfoLog.setLogCreateTime(date);
                    customerInfoLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_9.getCodeDesc());
                    customerInfoLog.setLogUserId(userId);
                    customerInfoLog.setIsDeleted(0);
                    customerInfoLogMapper.addCustomerLog(customerInfoLog);
                }
            }

            //开始移除公盘
            paramsMap.put("paramsList", paramsList);
            paramsMap.put("lastUpdateUser", userId);
            paramsMap.put("lastUpdateTime", date);
            customerInfoMapper.batchRemove(paramsMap);
        }
    }

    public CronusDto<Boolean> removeCustomerAll(RemoveDTO removeDTO, UserInfoDTO userInfoDTO, String token) {
        Date date = new Date();
        CronusDto<Boolean> resultDto = new CronusDto<>();
        boolean flag = false;
        //判断有没有选择负责人
        Map<String, Object> paramMap = new HashMap<>();
        List<Integer> ownIds = new ArrayList<>();//负责人
        if (StringUtils.isEmpty(removeDTO.getEmpId())) {
            resultDto.setData(flag);
            resultDto.setMessage(ResultResource.MESSAGE_REMOVECUSTOERAll_ERROR);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            return resultDto;
        }
        //判断这个负责人是不是在职的
        if (userInfoDTO == null || !"1".equals(userInfoDTO.getStatus())) {
            resultDto.setData(flag);
            resultDto.setMessage(ResultResource.MESSAGE_REMOVECUSTOERSTATUS_ERROR);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            return resultDto;
        }
        if (StringUtils.isEmpty(removeDTO.getIds())) {
            resultDto.setData(flag);
            resultDto.setMessage(ResultResource.MESSAGE_REMOVECUSTNOTNULL_ERROR);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            return resultDto;
        }
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
        if (result == false) {
            resultDto.setData(flag);
            resultDto.setMessage(ResultResource.CRM_CUSOMERALLACATE_ERROR);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            return resultDto;
        }
        //查询这些客户的信息
        paramMap.put("paramsList", uniqueList);
        List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerListByFeild(paramMap);
        //得到这些业务员的负责人
        if (customerInfoList != null && customerInfoList.size() > 0) {
            for (CustomerInfo customerInfo : customerInfoList) {
                ownIds.add(customerInfo.getOwnUserId());
            }
            //对负责人去除操作
            ownIds = new ArrayList<Integer>(new HashSet<>(ownIds));
            //得到用户的信息逗号隔开
            String strIds = listToString(uniqueList);
            // List<PHPUserDto> userList = ucService.getUserByIds(token,strIds,null,null,null,null,null,null,null);
            // TODO 下面开始更改这些客户的信息
          /*  flag = removeToUser(uniqueList,removeDTO.getEmpId(),userInfoDTO.getName(),token,userInfoDTO.getUser_id(),userInfoDTO.getName());
            if (flag == false){
                throw new CronusException(CronusException.Type.MESSAGE_REMOVENOTINJOB_ERROR);
            }*/
            //调用交易系统修改

            for (CustomerInfo customerInfo : customerInfoList) {
                Integer remain = customerInfo.getRemain();
                customerInfo.setSubCompanyId(Integer.valueOf(userInfoDTO.getSub_company_id()));
                customerInfo.setOwnUserId(removeDTO.getEmpId());
                AppUserDto userInfoByID = ucService.getUserInfoByID(token,removeDTO.getEmpId());
                customerInfo.setOwnUserName(userInfoByID.getName());
                customerInfo.setReceiveTime(date);
                customerInfo.setRemain(remain);
                customerInfo.setLastUpdateTime(date);
                customerInfo.setLastUpdateUser(Integer.valueOf(userInfoDTO.getUser_id()));
                customerInfoMapper.updateCustomer(customerInfo);
                try {
                    //发送短信
                    String message = "尊敬的客户您好，因公司人员调整，房金所新的融资经理" + userInfoDTO.getName()
                            + userInfoDTO.getTelephone() + "将继续为您服务，感谢您对房金所的支持与信赖。";
                    smsService.sendCommunication(customerInfo.getTelephonenumber(), message);
                } catch (Exception e) {
                    logger.error("removeCustomerAll >>>>>> 员工离职时给客户发送短信失败" + e.getMessage(),e);
                }

            }
            try {
                Integer thearesult = theaClientService.serviceContractToUser(token, strIds, removeDTO.getEmpId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Integer thearesult1 = theaClientService.cancelAll(token, strIds, removeDTO.getEmpId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            removeCustomerAddLog(customerInfoList, removeDTO.getEmpId(), Integer.valueOf(userInfoDTO.getUser_id()), userInfoDTO.getName());
            flag = true;
        }

        resultDto.setData(flag);
        resultDto.setMessage(ResultResource.CRM_MOVE_SUCESSS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        return resultDto;
    }

    /**
     * @return
     */
    @Transactional
    public boolean saveRemoveInfo(List<Integer> mustUpCustomerIds, Integer toUser, String token) {
        boolean flag = false;
        Date date = new Date();
        //如果客户为要修改的状态,修改客户信
        if (mustUpCustomerIds != null && mustUpCustomerIds.size() > 0) {
            //处理
            // String customerIds = listToString(mustUpCustomerIds);
            //查询到这些用户信息
            AppUserDto userDTO = ucService.getUserInfoByID(token, toUser);
            //开始更新
            Map<String, Object> paramsMap = new HashMap<>();
            //获取当前登录用户信息
            Integer userId = ucService.getUserIdByToken(token);
            paramsMap.put("paramsList", mustUpCustomerIds);
            paramsMap.put("subcompanyId", userDTO.getSub_company_id());
            paramsMap.put("ownerUserId", toUser);
            paramsMap.put("receiveTime", date);
            paramsMap.put("lastUpdateTime", date);
            paramsMap.put("lastUpdateUser", userId);
            customerInfoMapper.batchUpdate(paramsMap);
        }
        flag = true;
        return flag;
    }

    public void removeCustomerAddLog(List<CustomerInfo> customerInfos, Integer toUser, Integer userId, String userName) {
        //遍历添加日志
        Date date = new Date();
        if (customerInfos != null && customerInfos.size() > 0) {
            for (CustomerInfo customerInfo : customerInfos) {
                //添加分配日志
                AllocateLog allocateLog = new AllocateLog();
                allocateLog.setCreateTime(new Date());
                allocateLog.setCustomerId(customerInfo.getId());
                allocateLog.setOldOwnerId(customerInfo.getOwnUserId());
                allocateLog.setNewOwnerId(toUser);
                allocateLog.setCreateUserId(userId);
                allocateLog.setCreateUserName(userName);
                //json化字符串
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(customerInfo);
                allocateLog.setResult(jsonObject.toJSONString());
                allocateLog.setCreateTime(date);
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_9.getCodeDesc());
                Integer result = allocateLogMapper.insert(allocateLog);
                if (result == null) {
                    throw new CronusException(CronusException.Type.CRM_CUSTOMERLOG_ERROR);
                }
                //开始添加客户操作日志
                CustomerInfoLog customerInfoLog = new CustomerInfoLog();
                EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
                customerInfoLog.setLogCreateTime(date);
                customerInfoLog.setLogDescription("增加一条客户记录");
                customerInfoLog.setLogUserId(userId);
                customerInfoLog.setIsDeleted(0);
                customerInfoLogMapper.addCustomerLog(customerInfoLog);
            }
        }
    }

    public List<SubCompanyDto> getAllCompany(String token, Integer userId) {
        List<SubCompanyDto> companys = ucService.getAllCompanyByUserId(token, userId, CommonConst.SYSTEM_NAME_ENGLISH);
        return companys;
    }

    public String listToString(List list) {

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
        return str.toString();
    }

    @Transactional
    public void insertLog(CustomerInfo customerInfo, Integer userId) {
        //生成日志记录
        Date date = new Date();
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("编辑客户信息");
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
    }

    @Transactional
    public void insertAddCustomerLog(CustomerInfo customerInfo, Integer userId) {
        //生成日志记录
        Date date = new Date();
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("新增客户信息");
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
    }

    public CronusDto<CustomerDTO> fingByphone(String telephonenumber) {
        CronusDto resultDto = new CronusDto();
        //手机需要加密
        CustomerDTO dto = new CustomerDTO();
        Map<String, Object> mapc = new HashedMap();
        mapc.put("telephonenumber", DEC3Util.des3EncodeCBC(telephonenumber));
        List<CustomerInfo> customerInfos = customerInfoMapper.selectByOCDCPhone(mapc);
        if (customerInfos != null && customerInfos.size() > 0) {
            EntityToDto.customerEntityToCustomerDto(customerInfos.get(0), dto);
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(dto);
        return resultDto;
    }

    public List<Integer> customerListToCheck(String customerName, String utmSource, String city, String token) {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Integer> customerInfoList = new ArrayList<>();
        if (!StringUtils.isEmpty(customerName)) {
            paramsMap.put("customerName", customerName);
        }
        if (!StringUtils.isEmpty(utmSource)) {
            paramsMap.put("utmSource", utmSource);
        }
        if (!StringUtils.isEmpty(city)) {
            paramsMap.put("city", city);
        }
        customerInfoList = customerInfoMapper.findCustomerByType(paramsMap);
        return customerInfoList;
    }


    @Transactional
    public CronusDto addUploadCustomer(List<CustomerDTO> customerDTOs, String token) {
        CronusDto cronusDto = new CronusDto();
        List<CustomerInfo> customerInfos = new ArrayList<>();
        List<CustomerInfoLog> customerInfoLogs = new ArrayList<>();
        Date date = new Date();
        //判断必传字段*/
        //json转map 参数，教研参数
        UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO.getUser_id() == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户信息出错!");
        }
        for (CustomerDTO customerDTO : customerDTOs) {
            validAddData(customerDTO);
            //实体与DTO相互转换
            CustomerInfo customerInfo = new CustomerInfo();
            EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
            //新加字段
            List<EmplouInfo> emplouInfos = customerDTO.getEmployedInfo();
            //转Json在转String
            if (emplouInfos != null && emplouInfos.size() > 0) {
                String jsonString = JSONArray.toJSONString(emplouInfos);
                customerInfo.setEmployedInfo(jsonString);
            }
            customerInfo.setRetirementWages(customerDTO.getRetirementWages());
            //刚申请的客户
            customerInfo.setCompanyId(0);//导入客户不属于任何分公司
            customerInfo.setSubCompanyId(0);//导入客户不属于任何分公司
            customerInfo.setCustomerType(CommonConst.CUSTOMER_TYPE_MIND);
            customerInfo.setRemain(CommonConst.REMAIN_STATUS_NO);
            customerInfo.setConfirm(CommonConst.CONFIRM__STATUS_NO);
            customerInfo.setLastUpdateUser(Integer.valueOf(userInfoDTO.getUser_id()));
            customerInfo.setCreateTime(date);
            customerInfo.setCreateUser(Integer.valueOf(userInfoDTO.getUser_id()));
            customerInfo.setLastUpdateTime(date);
            customerInfo.setIsDeleted(0);
            customerInfo.setReceiveId(0);
            customerInfo.setCommunicateId(0);
            customerInfo.setOwnUserId(0);
            customerInfo.setAutostatus(0);
            //customerInfoMapper.insertCustomer(customerInfo);
            customerInfos.add(customerInfo);
            //开始插入log表
            //生成日志记录
        }
        //开始插入数据库
        customerInfoMapper.insertBatch(customerInfos);
        for (CustomerInfo customerInfo : customerInfos) {
            CustomerInfoLog customerInfoLog = new CustomerInfoLog();
            EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
            customerInfoLog.setLogCreateTime(date);
            customerInfoLog.setLogDescription("增加一条客户记录");
            customerInfoLog.setLogUserId(Integer.valueOf(userInfoDTO.getUser_id()));
            customerInfoLog.setIsDeleted(0);
            //customerInfoLogMapper.addCustomerLog(customerInfoLog);
            customerInfoLogs.add(customerInfoLog);
        }
        customerInfoLogMapper.insertBatch(customerInfoLogs);
        //推送到ocdc
        try {
            for(CustomerInfo customerInfo : customerInfos ) {
                outPutService.synchronToOcdc(customerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cronusDto.setResult(ResultResource.CODE_SUCCESS);
        cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return cronusDto;
    }

    public CronusDto editCustomerSys(CustomerInfo customerInfo, String token) {
        CronusDto resultDto = new CronusDto();
        //校验参数手机号不更新
//        Integer user_id = ucService.getUserIdByToken(token);
//        if (user_id == null) {
//            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "信息出错!");
//        }
        customerInfo.setLastUpdateTime(new Date());
//        customerInfo.setConfirm(0);
        customerInfo.setLastUpdateUser(0);
//        customerInfo.setClickCommunicateButton(0);
//        customerInfo.setTelephonenumber(DEC3Util.des3EncodeCBC(customerInfo.getTelephonenumber()));
        customerInfoMapper.updateCustomerSys(customerInfo);
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(new Date());
        customerInfoLog.setLogDescription("自动分配更新客户");
        customerInfoLog.setLogUserId(0);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfo.getId());
        return resultDto;
    }

    public CronusDto updateCustomerNonCommunicate(CustomerInfo customerInfo) {
        CronusDto resultDto = new CronusDto();
        //校验参数手机号不更新
//        customerInfo.setLastUpdateTime(date);
//        customerInfo.setConfirm(0);
        customerInfo.setLastUpdateUser(0);
//        customerInfo.setClickCommunicateButton(0);
        customerInfoMapper.updateCustomerNonCommunicate(customerInfo);
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(new Date());
        customerInfoLog.setLogDescription("未沟通分配");
        customerInfoLog.setLogUserId(0);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfo.getId());
        return resultDto;
    }

    public CronusDto<List<CustomerInfo>> selectNonCommunicateInTime() {
        CronusDto resultDto = new CronusDto();
        //手机需要加密
        List<CustomerInfo> customerInfos = customerInfoMapper.selectNonCommunicateInTime();
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfos);
        return resultDto;
    }

    public Map<String, Integer> countForAutoClean() {
        return customerInfoMapper.countForAutoClean();
    }

    /**
     * 根据属性Map查询
     *
     * @param telStr
     * @return
     */
    public List<CustomerInfo> selectByParams(Map<String, Object> telStr) {
        List<CustomerInfo> customerInfoList = customerInfoMapper.selectByParams(telStr);
        return customerInfoList;
    }

    public List<Integer> selectForAutoClean(Map<String, Object> paramsMap) {
        return customerInfoMapper.selectForAutoClean(paramsMap);
    }

    public void batchUpdate(Map<String, Object> paramsMap) {
        customerInfoMapper.batchUpdate(paramsMap);
    }


    /**
     * 提交编辑用户
     *
     * @return
     */
    @Transactional
    public CronusDto editClientCustomerOk(CustomerDTO customerDTO, String token) {
        CronusDto resultDto = new CronusDto();
        //校验权限
        //校验参数手机号不更新
        // UserSortInfoDTO userSortInfoDTO = ucService.getSortUserInfo(token);
      /*  if (userSortInfoDTO == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "信息出错!");
        }*/
        SortUserInfoByPhoneDTO userSortInfoDTO = ucService.getSortUserInfoByPhone(token, customerDTO.getTelephonenumber());
        Map<String, Object> paramsMap = new HashMap<>();
        if (customerDTO.getId() == null || "".equals(customerDTO.getId())) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        if (customerDTO.getCustomerName() == null || "".equals(customerDTO.getCustomerName())) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERNAME_ERROR);
        }
        Integer id = customerDTO.getId();
        paramsMap.put("id", id);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Date date = new Date();
        EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
        customerInfo.setRetirementWages(customerDTO.getRetirementWages());
        List<EmplouInfo> emplouInfos = customerDTO.getEmployedInfo();
        if (emplouInfos != null && emplouInfos.size() > 0) {
            String jsonString = JSONArray.toJSONString(emplouInfos);
            customerInfo.setEmployedInfo(jsonString);
        }
        customerInfo.setLastUpdateTime(date);
        customerInfo.setLastUpdateUser(Integer.valueOf(userSortInfoDTO.getUser_id()));
        customerInfoMapper.updateCustomer(customerInfo);
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("编辑客户信息");
        customerInfoLog.setLogUserId(Integer.valueOf(userSortInfoDTO.getUser_id()));
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfo.getId());
        return resultDto;
    }

    public String getTelePhone(Integer customerId, String token) {

        CustomerInfo customerInfo = findCustomerById(customerId);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //解密
        String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
        return telephone;
    }

    public ScrmbDTO getCommunByCustomerId(Integer customerId) {

        ScrmbDTO scrmbDTO = new ScrmbDTO();
        CustomerInfo customerInfo = findCustomerById(customerId);
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        scrmbDTO.setCustomerId(customerInfo.getId());
        scrmbDTO.setCommunicateTime(customerInfo.getCommunicateTime());
        scrmbDTO.setConfirm(customerInfo.getConfirm());
        scrmbDTO.setCustomerName(customerInfo.getCustomerName());
        return scrmbDTO;
    }
    public List<ScrmbDTO> getCommunByCustomerIds(String customerIds) {

        Map<String,Object> paramsMap = new HashMap<>();
        List paramsList = new ArrayList();
        List<ScrmbDTO> scrmbDTOs = new ArrayList<>();
        if (!StringUtils.isEmpty(customerIds)){
            String[] strArray = null;
            strArray = customerIds.split(",");
            for (int i = 0; i < strArray.length; i++) {
                paramsList.add(Integer.parseInt(strArray[i]));
            }
            paramsMap.put("paramsList", paramsList);
        }else {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        List<CustomerInfo> customerInfos = customerInfoMapper.findCustomerListByFeild(paramsMap);
        for (CustomerInfo customerInfo : customerInfos) {
            ScrmbDTO scrmbDTO = new ScrmbDTO();
            scrmbDTO.setCustomerId(customerInfo.getId());
            scrmbDTO.setCommunicateTime(customerInfo.getCommunicateTime());
            scrmbDTO.setConfirm(customerInfo.getConfirm());
            scrmbDTO.setCustomerName(customerInfo.getCustomerName());
            scrmbDTOs.add(scrmbDTO);
          }
        return scrmbDTOs;
    }

    public Integer getKeepCount(UserInfoDTO userInfoDTO){

        List<CustomerInfo> resultList = new ArrayList<>();
        List ids = new ArrayList();
        Map<String, Object> paramsMap = new HashMap<>();
        //判断当前登录用户所属公司
        ids.add(Integer.valueOf(userInfoDTO.getUser_id()));
        paramsMap.put("owerId", ids);
        paramsMap.put("remain",1);//保留状态
        paramsMap.put("customerType","意向客户");
        resultList = customerInfoMapper.findCustomerListByFeild(paramsMap);
        return resultList.size();
    }

    @Transactional
    public CronusDto addLoan(LoanDTO6 loanDTO, UserInfoDTO userInfoDTO, String token) {
        CronusDto resultDto = new CronusDto();
        Integer userId = null;
        if (!StringUtils.isEmpty(userInfoDTO.getUser_id())) {
            userId = Integer.parseInt(userInfoDTO.getUser_id());
        }
        //插入日志
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        customerInfoLog.setLogCreateTime(new Date());
        customerInfoLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_13.getCodeDesc());
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);

        customerInfoLog.setCustomerId(loanDTO.getCustomerId());
        customerInfoLog.setTelephonenumber(loanDTO.getTelephonenumber());
        customerInfoLog.setCreateTime(new Date());
        customerInfoLog.setLastUpdateTime(new Date());
        customerInfoLog.setCreateUser(userId);
        customerInfoLog.setLastUpdateUser(userId);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);

        logger.warn("调用交易接口产生交易-------》");
        TheaApiDTO theaApiDTO = theaService.addLoan(loanDTO, token);
        logger.warn("调用交易接口结束-------》");
        if (theaApiDTO != null && theaApiDTO.getResult() == 0) {
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
        } else {
            resultDto.setData(theaApiDTO.getData());
            resultDto.setMessage(theaApiDTO.getMessage());
            resultDto.setResult(theaApiDTO.getResult());
        }
        return resultDto;
    }

//    public CronusDto<CustomerCountDTO> getTodayCount(String token, Integer userId) {
//
//        CronusDto<CustomerCountDTO> resultDto = new CronusDto<>();
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String today = sdf.format(date);
//        String startTime = today + " 00:00:00";
//        String endTime = today + " 23:59:59";
//        Map<String,Object> paramMap = new HashMap<>();
//        paramMap.put("userId",userId);
//        paramMap.put("startTime",startTime);
//        paramMap.put("endTime",endTime);
//        //历史分配
//        List<CustomerComDTO> historyCountList = allocateLogMapper.getHistoryCount(paramMap);
//        CustomerCountDTO customerCountDTO = new CustomerCountDTO();
//        Integer noCommunicationHistory = 0;//未沟通分配历史数
//        Integer automaticAllocationHistory = 0;//自动分配历史数
//        Integer collectCustomersHistory = 0;//领取客户历史数
//        if (null != historyCountList && historyCountList.size() > 0){
//
//            for (CustomerComDTO customerComDTO : historyCountList){
//                if (customerComDTO != null && customerComDTO.getOperation().equals("未沟通分配")){
//                    noCommunicationHistory = customerComDTO.getCount();
//                }
//                if (customerComDTO != null && customerComDTO.getOperation().equals("自动分配")){
//                    automaticAllocationHistory = customerComDTO.getCount();
//                }
//                if (customerComDTO != null && customerComDTO.getOperation().equals("领取客户")){
//                    collectCustomersHistory = customerComDTO.getCount();
//                }
//            }
//        }
//        customerCountDTO.setHistoryAllocation(noCommunicationHistory + automaticAllocationHistory);
//        customerCountDTO.setHistoryReceive(collectCustomersHistory);
//        //历史沟通次数
//        Integer historyCommunication = communicationLogMapper.gethistoryCount(paramMap);
//        customerCountDTO.setHistoryCommunicate(historyCommunication);
//        //历史沟通客户数
//        Integer historyCommunicationCustomer = communicationLogMapper.getHistoryCustomer(paramMap);
//        customerCountDTO.setHistoryCommunicateCustomer(historyCommunicationCustomer);
//
//        //今日分配
//        List<CustomerComDTO> todayCountList = allocateLogMapper.getTodayCount(paramMap);
//        Integer noCommunicationToday = 0;//未沟通分配今日数
//        Integer automaticAllocationToday = 0;//自动分配历今日数
//        Integer collectCustomersToday = 0;//领取客户今日数
//        if (null != todayCountList && todayCountList.size() > 0){
//            for (CustomerComDTO customerComDTO : todayCountList){
//                if (customerComDTO != null && customerComDTO.getOperation().equals("未沟通分配")){
//                    noCommunicationToday = customerComDTO.getCount();
//                }
//                if (customerComDTO != null && customerComDTO.getOperation().equals("自动分配")){
//                    automaticAllocationToday = customerComDTO.getCount();
//                }
//                if (customerComDTO != null && customerComDTO.getOperation().equals("领取客户")){
//                    collectCustomersToday = customerComDTO.getCount();
//                }
//
//            }
////            customerCountDTO.setTodayAllocation(noCommunicationToday + automaticAllocationToday);
////            customerCountDTO.setTodayReceive(collectCustomersToday);
//        }
//        customerCountDTO.setTodayAllocation(noCommunicationToday + automaticAllocationToday);
//        customerCountDTO.setTodayReceive(collectCustomersToday);
//        //今日沟通次数
//        Integer todayCommunication = communicationLogMapper.getTodayCount(paramMap);
//        customerCountDTO.setTodayCommunicate(todayCommunication);
//        //今日沟通客户数
//        Integer todayCommunicationCustomer = communicationLogMapper.getTodayCommunicateCustomer(paramMap);
//        customerCountDTO.setTodayCommunicateCustomer(todayCommunicationCustomer);
//
//        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
//        resultDto.setResult(ResultResource.CODE_SUCCESS);
//        resultDto.setData(customerCountDTO);
//        return resultDto;
//    }


    public CronusDto<CustomerCountDTO> getTodayCount(String token, Integer userId) {

        CronusDto<CustomerCountDTO> resultDto = new CronusDto<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        String startTime = today + " 00:00:00";
        String endTime = today + " 23:59:59";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId",userId);
        paramMap.put("startTime",startTime);
        paramMap.put("endTime",endTime);
        CustomerCountDTO customerCountDTO = new CustomerCountDTO();


        //今日分配
        List<CustomerComDTO> todayCountList = allocateLogMapper.getTodayCount(paramMap);
        Integer noCommunicationToday = 0;//未沟通分配今日数
        Integer automaticAllocationToday = 0;//自动分配历今日数
        Integer collectCustomersToday = 0;//领取客户今日数
        if (null != todayCountList && todayCountList.size() > 0){
            for (CustomerComDTO customerComDTO : todayCountList){
                if (customerComDTO != null && customerComDTO.getOperation().equals("未沟通分配")){
                    noCommunicationToday = customerComDTO.getCount();
                }
                if (customerComDTO != null && customerComDTO.getOperation().equals("自动分配")){
                    automaticAllocationToday = customerComDTO.getCount();
                }
                if (customerComDTO != null && customerComDTO.getOperation().equals("领取客户")){
                    collectCustomersToday = customerComDTO.getCount();
                }
            }
        }
        customerCountDTO.setTodayAllocation(noCommunicationToday + automaticAllocationToday);
        customerCountDTO.setTodayReceive(collectCustomersToday);
        //今日沟通次数
        Integer todayCommunication = communicationLogMapper.getTodayCount(paramMap);
        customerCountDTO.setTodayCommunicate(todayCommunication);
        //今日沟通客户数
        Integer todayCommunicationCustomer = communicationLogMapper.getTodayCommunicateCustomer(paramMap);
        customerCountDTO.setTodayCommunicateCustomer(todayCommunicationCustomer);

        List<CustomerComDTO> historyCountList = new ArrayList<>();
        //从redis中获取历史分配
        ValueOperations<String, String> redisOptions = null;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisOptions = redisTemplate.opsForValue();
        String redisData = redisOptions.get(CustomerInfoService.REDIS_CRONUS_GETHISTORYCOUNT + userId);
        if (null != redisData && !StringUtils.isEmpty(redisData)){
            CustomerCountDTO customerCountDTOTem = JSONObject.parseObject(redisData, CustomerCountDTO.class);
//            CronusDto<CustomerCountDTO> cronusDto = JSONObject.parseObject(redisData, CronusDto.class);
//            customerCountDTO = cronusDto.getData();
            //customerCountDTO = JSONObject.parseObject(cronusDto.getData(),customerCountDTO.getClass());
            if (null != customerCountDTO){
                customerCountDTOTem.setTodayAllocation(noCommunicationToday + automaticAllocationToday);
                customerCountDTOTem.setTodayReceive(collectCustomersToday);
                customerCountDTOTem.setTodayCommunicate(todayCommunication);
                customerCountDTOTem.setTodayCommunicateCustomer(todayCommunicationCustomer);
                customerCountDTOTem.setHistoryAllocation(customerCountDTOTem.getHistoryAllocation() + customerCountDTO.getTodayAllocation());
                customerCountDTOTem.setHistoryReceive(customerCountDTOTem.getHistoryReceive() + customerCountDTO.getTodayReceive());
                customerCountDTOTem.setHistoryCommunicate(customerCountDTOTem.getHistoryCommunicate() + customerCountDTO.getTodayCommunicate());
                customerCountDTOTem.setHistoryCommunicateCustomer(customerCountDTOTem.getHistoryCommunicateCustomer() + customerCountDTO.getTodayCommunicateCustomer());
                resultDto.setData(customerCountDTOTem);
                return  resultDto;
            }
        }
        //如果redis中没有, 就查数据库
        //历史分配
        Calendar calendar = Calendar.getInstance();
        try {
//            Date parse = sdf.parse(date.toString());
            calendar.setTime(sdf.parse(today));
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            date = calendar.getTime();
            endTime = sdf.format(date);
            paramMap.put("endTime",endTime + " 23:59:59");
        } catch (ParseException e) {
            logger.error("getTodayCount >>>>>>日期转换失败" + e.getMessage(),e);
        }

        historyCountList = allocateLogMapper.getHistoryCount(paramMap);
        Integer noCommunicationHistory = 0;//未沟通分配历史数
        Integer automaticAllocationHistory = 0;//自动分配历史数
        Integer collectCustomersHistory = 0;//领取客户历史数
        if (null != historyCountList && historyCountList.size() > 0){

            for (CustomerComDTO customerComDTO : historyCountList){
                if (customerComDTO != null && customerComDTO.getOperation().equals("未沟通分配")){
                    noCommunicationHistory = customerComDTO.getCount();
                }
                if (customerComDTO != null && customerComDTO.getOperation().equals("自动分配")){
                    automaticAllocationHistory = customerComDTO.getCount();
                }
                if (customerComDTO != null && customerComDTO.getOperation().equals("领取客户")){
                    collectCustomersHistory = customerComDTO.getCount();
                }
            }
        }
        customerCountDTO.setHistoryAllocation(noCommunicationHistory + automaticAllocationHistory);
        customerCountDTO.setHistoryReceive(collectCustomersHistory);
//        //历史沟通次数
        Integer historyCommunication = communicationLogMapper.gethistoryCount(paramMap);
        customerCountDTO.setHistoryCommunicate(historyCommunication);
        //历史沟通客户数
        Integer historyCommunicationCustomer = communicationLogMapper.getHistoryCustomer(paramMap);
        customerCountDTO.setHistoryCommunicateCustomer(historyCommunicationCustomer);

        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerCountDTO);

        try {
            //把昨天之前的数据存入redis
            redisOptions = redisTemplate.opsForValue();
            redisOptions.set(CustomerInfoService.REDIS_CRONUS_GETHISTORYCOUNT + userId,JSONObject.toJSONString(customerCountDTO),CustomerInfoService.REDIS_CRONUS_GETHISTORYCOUNT_TIME, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("getTodayCount >>>>>> 存入redis失败" + e.getMessage(),e);
        }
        //昨天之前的数据加上今天的数据
        customerCountDTO.setHistoryAllocation(noCommunicationHistory + automaticAllocationHistory + customerCountDTO.getTodayAllocation());
        customerCountDTO.setHistoryReceive(collectCustomersHistory + customerCountDTO.getTodayReceive());
        customerCountDTO.setHistoryCommunicate(historyCommunication + customerCountDTO.getTodayCommunicate());
        customerCountDTO.setHistoryCommunicateCustomer(historyCommunicationCustomer + customerCountDTO.getTodayCommunicateCustomer());

        resultDto.setData(customerCountDTO);
        return resultDto;
    }


    /**
     * 新用户注册15天之后发送短信
     */
    public void sandMessage() {

        String utmSource = "wangluoyingxiao,androidyysc";
        //16天之前的日期
        String time = getDate(-16);
        String timeStart = time + " 00:00:00";
        String timeEnd = time + " 23:59:59";
        HashMap<String, Object> map = new HashMap<>();
        map.put("utmSource",utmSource);
        map.put("timeStart",timeStart);
        map.put("timeEnd",timeEnd);

        List<CustomerInfo> customerInfoList = customerInfoMapper.getNewCustomer(map);
//        String customerphone = "18701780932";
//        smsService.sendCommunication(customerphone, CommonConst.NEW_CUSTOMER_MESSAGE);
        if (null != customerInfoList && customerInfoList.size() > 0){
            String key = "new_customer_message";
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            ValueOperations<String, String> redis = redisTemplate.opsForValue();
            String flag = redis.get(key);
            if (flag == null || StringUtils.isEmpty(flag)){
                redis.set(key,"0",CustomerInfoService.NEW_CUSTOMER_MESSAGE_TIME,TimeUnit.SECONDS);
                flag = "0";
            }
            // 从缓存中取数据, 0-今天未执行, 1-今天执行了
            if ("0".equals(flag)){
                for (CustomerInfo customerInfo : customerInfoList){
                    //解密手机号
                    String customerphone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
                    try {
                        //发送短信
                        smsService.sendCommunication(customerphone, CommonConst.NEW_CUSTOMER_MESSAGE);

                        //定时任务,将时间存入redis
                        redis.set(key,"1",CustomerInfoService.NEW_CUSTOMER_MESSAGE_TIME,TimeUnit.SECONDS);
                    } catch (Exception e) {
                        logger.error("sandMessage >>>>>>定时任务 : 新客户15天发送短信失败" + e.getMessage(),e);
                    }
                }
            }
        }

    }

    /**
     * 获取日期
     * @param before
     * @return
     */
    public static String getDate(Integer before){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(today));
            calendar.add(Calendar.DAY_OF_MONTH,before);
            date = calendar.getTime();
            String time = sdf.format(date);
            return time;
        } catch (ParseException e) {
            logger.error("日期转化失败>>>" + e.getMessage(),e);
            return null;
        }
    }

    public CustomerPartDTO selectCustomerDTOByPhone(String phone){
        //对手机号码进行加密
        String telephone = DEC3Util.des3EncodeCBC(phone);
        return customerInfoMapper.selectCustomerDTOByPhone(telephone);
    }

}
