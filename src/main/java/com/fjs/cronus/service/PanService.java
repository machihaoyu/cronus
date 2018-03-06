package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.controller.PublicOfferController;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.api.uc.AppUserDto;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.dto.cronus.PanParamDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.enums.CustListTimeOrderEnum;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.AllocateLogMapper;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerInfoLog;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DEC3Util;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.*;

/**
 * Created by msi on 2017/11/30.
 */
@Service
public class PanService {

    private static final Logger logger = LoggerFactory.getLogger(PanService.class);
    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    AllocateLogMapper allocateLogMapper;
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    UcService ucService;
    @Autowired
    CustomerInfoLogMapper customerInfoLogMapper;
    @Autowired
    TheaService theaService;

    @Autowired
    private AutoCleanService autoCleanService;

    public QueryResult<CustomerListDTO> listByOffer(PanParamDTO pan, Integer userId, Integer companyId , String token, String system,
                                                Integer page, Integer size, List<String> mainCitys, List<Integer> subCompanyIds, Integer type,Integer mountLevle,List<String> utmList,List<String>paramsList) {

        QueryResult<CustomerListDTO> result = new QueryResult<>();
        Map<String,Object> map=new HashedMap();
        List<CustomerListDTO> resultDto = new ArrayList<>();
        if (pan != null){
            //客户姓名
            if (StringUtils.isNotEmpty(pan.getCustomerName())) {
                map.put("customerName",pan.getCustomerName());
            }
            //电话
            if (StringUtils.isNotEmpty(pan.getTelephonenumber())) {
                //手机号加密
                map.put("telephonenumber", DEC3Util.des3EncodeCBC(pan.getTelephonenumber()));
            }
            //合作状态
            if (StringUtils.isNotEmpty(pan.getCustomerClassify())) {
                map.put("cooperation_status", pan.getCustomerClassify());
            }
            if (StringUtils.isNotEmpty(pan.getHouseStatus())) {
                map.put("houseStatus", pan.getHouseStatus());
            }
            //公司
            if (companyId != null) {
                map.put("companyId", companyId);
            }
            if (StringUtils.isNotEmpty(pan.getUtmSource())) {
                map.put("utmSource",pan.getUtmSource());
            }
            if (StringUtils.isNotEmpty(pan.getCity())) {
                map.put("city",pan.getCity());
            }
            if (StringUtils.isNotEmpty(pan.getCustomerSource())){
                map.put("customerSource",pan.getCustomerSource());
            }
            if (mountLevle != null){
                map.put("mountLevle",mountLevle);
            }
            map.put("mainCitys",mainCitys);
            map.put("subCompanyIds",subCompanyIds);
            map.put("start",(page-1)*size);
            map.put("size",size);
            if (utmList != null && utmList.size() > 0){
                map.put("utmList",utmList);
            }
            if (paramsList != null && paramsList.size() > 0){
                map.put("paramsList",paramsList);
            }
            logger.warn("----------------------->从Uc中获取用户信息getAllUserInfo()");
            PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token,CommonConst.SYSTEM_NAME_ENGLISH);
            logger.warn("----------------------->从Uc中获取用户信息getAllUserInfo()结束");
            if (userInfoDTO == null){
                throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
            }
            Integer lookphone =Integer.parseInt(userInfoDTO.getUser_info().getLook_phone());
            Integer user_Id = Integer.parseInt(userInfoDTO.getUser_info().getUser_id());
            logger.warn("----------------------->查询数据库获取信息开始");
            List<CustomerInfo> customerInfoList = customerInfoMapper.publicOfferList(map);
            logger.warn("----------------------->查询数据库获取信息结束");
            Integer total = customerInfoMapper.publicOfferCount(map);
            logger.warn("----------------------->查询数据库获取总数量信息结束");
            if (customerInfoList != null && customerInfoList.size() > 0) {
                for (CustomerInfo customerInfo : customerInfoList){
                    CustomerListDTO customerDto = new CustomerListDTO();
                    EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto,lookphone,userId);
                    resultDto.add(customerDto);
                }
            }
            result.setRows(resultDto);
            result.setTotal(total.toString());
        }
        return  result;
    }
    public QueryResult<CustomerListDTO> listByOfferNew(PanParamDTO pan, Integer userId, Integer companyId , String token, String system,
                                                    Integer page, Integer size, List<String> mainCitys, List<Integer> subCompanyIds, Integer type,Integer mountLevle,
                                                       List<String> utmList,List<String>paramsList,String orderField,String sort) {

        QueryResult<CustomerListDTO> result = new QueryResult<>();
        //屏蔽到渠道增加媒体
        List<String> channleList = new ArrayList<>();
        Map<String,Object> map=new HashMap();
        List<CustomerListDTO> resultDto = new ArrayList<>();
        if (pan != null){
            //客户姓名
            if (StringUtils.isNotEmpty(pan.getCustomerName())) {
                map.put("customerName",pan.getCustomerName());
            }
            //电话
            if (StringUtils.isNotEmpty(pan.getTelephonenumber())) {
                //手机号加密
                map.put("telephonenumber", DEC3Util.des3EncodeCBC(pan.getTelephonenumber()));
            }
            //合作状态
            if (StringUtils.isNotEmpty(pan.getCustomerClassify())) {
                map.put("cooperation_status", pan.getCustomerClassify());
            }
            if (StringUtils.isNotEmpty(pan.getHouseStatus())) {
                map.put("houseStatus", pan.getHouseStatus());
            }
            //公司
            if (companyId != null) {
                map.put("companyId", companyId);
            }
            if (StringUtils.isNotEmpty(pan.getUtmSource())) {
                //TODO 由媒体换成渠道
                List<String> list = theaClientService.getChannelNameListByMediaName(token,pan.getUtmSource());
                map.put("utmSources",list);

            }
            if (StringUtils.isNotEmpty(pan.getCity())) {
                map.put("city",pan.getCity());
            }
            if (StringUtils.isNotEmpty(pan.getCustomerSource())){
                map.put("customerSource",pan.getCustomerSource());
            }
            if (mountLevle != null){
                map.put("mountLevle",mountLevle);
            }
            //排序---zl-----
            if (!org.springframework.util.StringUtils.isEmpty(orderField) && CustListTimeOrderEnum.getEnumByCode(orderField) != null) {
                if (org.springframework.util.StringUtils.isEmpty(sort)){
                    sort = "desc";
                }
                map.put("order", orderField + " " + sort);
            }
            map.put("mainCitys",mainCitys);
            map.put("subCompanyIds",subCompanyIds);
            map.put("start",(page-1)*size);
            map.put("size",size);
            if (utmList != null && utmList.size() > 0){
                map.put("utmList",utmList);
            }
            if (paramsList != null && paramsList.size() > 0){
                map.put("paramsList",paramsList);
            }
            logger.warn("----------------------->从Uc中获取用户信息getAllUserInfo()");
            PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token,CommonConst.SYSTEM_NAME_ENGLISH);
            logger.warn("----------------------->从Uc中获取用户信息getAllUserInfo()结束");
            if (userInfoDTO == null){
                throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
            }
            Integer lookphone =Integer.parseInt(userInfoDTO.getUser_info().getLook_phone());
            Integer user_Id = Integer.parseInt(userInfoDTO.getUser_info().getUser_id());
            logger.warn("----------------------->查询数据库获取信息开始");
            List<CustomerInfo> customerInfoList = customerInfoMapper.publicOfferList(map);
            logger.warn("----------------------->查询数据库获取信息结束");
            Integer total = customerInfoMapper.publicOfferCount(map);
            logger.warn("----------------------->查询数据库获取总数量信息结束");
            if (customerInfoList != null && customerInfoList.size() > 0) {
                for (CustomerInfo customerInfo : customerInfoList){
                    CustomerListDTO customerDto = new CustomerListDTO();
                    if (!channleList.contains(customerInfo.getUtmSource())){
                        channleList.add(customerInfo.getUtmSource());
                    }
                    EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto,lookphone,userId);
                    resultDto.add(customerDto);
                }
            }
            //屏蔽媒体
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("channelNames",channleList);
            Map<String,String> mediaMap = theaClientService.getMediaName(token,jsonObject);
            for (CustomerListDTO customerListDTO : resultDto ){
                System.out.println(mediaMap.get(customerListDTO.getUtmSource()));
                customerListDTO.setUtmSource(mediaMap.get(customerListDTO.getUtmSource()));
            }
            result.setRows(resultDto);
            result.setTotal(total.toString());
        }
        return  result;
    }
    @Transactional
    public boolean pullPan(Integer customerId,Integer userId,String token,String userName){
       //判断清洗中不能领取客户每周日的八点开始进行自动清洗
        Map<String,Object> paramMap = new HashMap<>();
        boolean flag = false;
        Date date = new Date();
        if (DateUtils.dayForWeek(date) == 7 && DateUtils.getHour(date) == 20 && DateUtils.getMinute(date) < 10){
            throw new CronusException(CronusException.Type.MESSAGE_CUSTOMERCLEAN_ERROR);
        }
        //判断是否是自动清洗的状态
//        String status = theaClientService.findValueByName(token, CommonConst.AUTO_CLEAN_STATUS);
//        if (Integer.parseInt(status) == 1){
        if (autoCleanService.autoCleanStatus()){
            throw new CronusException(CronusException.Type.MESSAGE_CUSTOMERCLEAN_ERROR);
        }
        //找到客户信息
        CustomerInfo customerInfo = customerInfoService.findCustomerById(customerId);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        //开始改变
        receiveCustomerByType(customerInfo,userId,token);
        //增加分配日志
        AllocateLog allocateLog = new AllocateLog();
        allocateLog.setCreateTime(new Date());
        allocateLog.setCustomerId(customerInfo.getId());
        allocateLog.setOldOwnerId(customerInfo.getOwnUserId());
        allocateLog.setNewOwnerId(userId);
        allocateLog.setCreateUserId(userId);
        allocateLog.setCreateUserName(userName);
        allocateLog.setOperation(CommonEnum.LOAN_OPERATION_TYPE_2.getCodeDesc());
        //开始插入
        allocateLog.setResult(CommonConst.OPERATIONSUCESS);
        Integer insertAllocateLog = allocateLogMapper.insert(allocateLog);
        if (null == insertAllocateLog) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERLOG_ERROR);
        }
        flag = true;
        return flag;

    }

    /**
     * 领取客户
     * @param customerInfo
     * @param userId
     * @param token
     */
    @Transactional
    public void receiveCustomerByType(CustomerInfo customerInfo,Integer userId,String token){
        AppUserDto ucUserDTO = ucService.getUserInfoByID(token,userId);
        Date date = new Date();
        if (customerInfo.getOwnUserId() != 0){
            throw new CronusException(CronusException.Type.MESSAGE_PULLCUSTOMEROWNER_ERROR);
        }
        customerInfo.setLastUpdateUser(userId);
        customerInfo.setLastUpdateTime(date);
        customerInfo.setOwnUserId(userId);
        customerInfo.setOwnUserName(ucUserDTO.getName());
        customerInfo.setReceiveTime(date);
        customerInfo.setSubCompanyId(Integer.valueOf(ucUserDTO.getSub_company_id()));
        customerInfo.setCompanyId(Integer.valueOf(ucUserDTO.getCompany_id()));
        //更改领取人
        customerInfo.setReceiveId(userId);
        customerInfoMapper.updateCustomer(customerInfo);

        //开始插入日志
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription(CommonConst.OPERATION);
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
    }
    public Integer keepCount(Integer userId){
        Date date = new Date();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("createUserId",userId);
        paramMap.put("operation",CommonConst.OPERATION);
        String  today = DateUtils.format(date,DateUtils.FORMAT_SHORT);
        // paramMap.put("operation",CommonConst.OPERATION);
        paramMap.put("createTime",today);
        Integer count = allocateLogMapper.receiveCountByWhere(paramMap);
        return count;
    }
    public QueryResult<CustomerListDTO> specialListByOffer(PanParamDTO pan, Integer userId, Integer companyId , String token, String system,
                                                    Integer page, Integer size, List<String> mainCitys, List<Integer> subCompanyIds, Integer type,Integer mountLevle,List<String> utmList,List<String>paramsList) {

        QueryResult<CustomerListDTO> result = new QueryResult<>();
        Map<String,Object> map=new HashedMap();
        List<CustomerListDTO> resultDto = new ArrayList<>();
        if (pan != null){
            //客户姓名
            if (StringUtils.isNotEmpty(pan.getCustomerName())) {
                map.put("customerName",pan.getCustomerName());
            }
            //电话
            if (StringUtils.isNotEmpty(pan.getTelephonenumber())) {
                //手机号加密
                map.put("telephonenumber", DEC3Util.des3EncodeCBC(pan.getTelephonenumber()));
            }
            //合作状态
            if (StringUtils.isNotEmpty(pan.getCustomerClassify())) {
                map.put("cooperation_status", pan.getCustomerClassify());
            }
            if (StringUtils.isNotEmpty(pan.getHouseStatus())) {
                map.put("houseStatus", pan.getHouseStatus());
            }
            //公司
            if (companyId != null) {
                map.put("companyId", companyId);
            }
            if (StringUtils.isNotEmpty(pan.getUtmSource())) {
                map.put("utmSource",pan.getUtmSource());
            }
            if (StringUtils.isNotEmpty(pan.getCity())) {
                map.put("city",pan.getCity());
            }
            if (StringUtils.isNotEmpty(pan.getCustomerSource())){
                map.put("customer_source",pan.getCustomerSource());
            }
            if (mountLevle != null){
                map.put("mountLevle",mountLevle);
            }
            map.put("mainCitys",mainCitys);
            map.put("subCompanyIds",subCompanyIds);
            map.put("type",type);
            map.put("start",(page-1)*size);
            map.put("size",size);
            if (utmList != null && utmList.size() > 0){
                map.put("utmList",utmList);
            }
            if (paramsList != null && paramsList.size() > 0){
                map.put("paramsList",paramsList);
            }
            PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token,CommonConst.SYSTEM_NAME_ENGLISH);
            if (userInfoDTO == null){
                throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
            }
            Integer lookphone =Integer.parseInt(userInfoDTO.getUser_info().getLook_phone());
            Integer user_Id = Integer.parseInt(userInfoDTO.getUser_info().getUser_id());
            List<CustomerInfo> customerInfoList = customerInfoMapper.specialListByOffer(map);
            Integer total = customerInfoMapper.specialListByOfferCount(map);
            if (customerInfoList != null && customerInfoList.size() > 0) {
                for (CustomerInfo customerInfo : customerInfoList){
                    CustomerListDTO customerDto = new CustomerListDTO();
                    EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto,lookphone,userId);
                    resultDto.add(customerDto);
                }
            }
            result.setRows(resultDto);
            result.setTotal(total.toString());
        }
        return  result;
    }
    public QueryResult<CustomerListDTO> specialListByOfferNew(PanParamDTO pan, Integer userId, Integer companyId , String token, String system,
                                                              Integer page, Integer size, List<String> mainCitys, List<Integer> subCompanyIds, Integer type,Integer mountLevle,
                                                              List<String> utmList,List<String>paramsList,Integer utmFlag,String orderField,String sort) {

        QueryResult<CustomerListDTO> result = new QueryResult<>();
        Map<String,Object> map=new HashedMap();
        List<CustomerListDTO> resultDto = new ArrayList<>();
        List<String> channleList = new ArrayList<>();
        if (pan != null){
            //客户姓名
            if (StringUtils.isNotEmpty(pan.getCustomerName())) {
                map.put("customerName",pan.getCustomerName());
            }
            //电话
            if (StringUtils.isNotEmpty(pan.getTelephonenumber())) {
                //手机号加密
                map.put("telephonenumber", DEC3Util.des3EncodeCBC(pan.getTelephonenumber()));
            }
            //合作状态
            if (StringUtils.isNotEmpty(pan.getCustomerClassify())) {
                map.put("cooperation_status", pan.getCustomerClassify());
            }
            if (StringUtils.isNotEmpty(pan.getHouseStatus())) {
                map.put("houseStatus", pan.getHouseStatus());
            }
            //公司
            if (companyId != null) {
                map.put("companyId", companyId);
            }
            if (StringUtils.isNotEmpty(pan.getUtmSource())) {
                map.put("utmSource",pan.getUtmSource());
            }
            if (StringUtils.isNotEmpty(pan.getCity())) {
                map.put("city",pan.getCity());
            }
            if (StringUtils.isNotEmpty(pan.getCustomerSource())){
                map.put("customer_source",pan.getCustomerSource());
            }
            if (mountLevle != null){
                map.put("mountLevle",mountLevle);
            }
            //排序---zl-----
            if (!org.springframework.util.StringUtils.isEmpty(orderField) && CustListTimeOrderEnum.getEnumByCode(orderField) != null) {
                if (org.springframework.util.StringUtils.isEmpty(sort)){
                    sort = "desc";
                }
                map.put("order", orderField + " " + sort);
            }
            map.put("mainCitys",mainCitys);
            map.put("subCompanyIds",subCompanyIds);
            map.put("type",type);
            map.put("utmFlag",utmFlag);
            map.put("start",(page-1)*size);
            map.put("size",size);
            if (utmList != null && utmList.size() > 0){
                map.put("utmList",utmList);
            }
            if (paramsList != null && paramsList.size() > 0){
                map.put("paramsList",paramsList);
            }
            logger.warn("------------->Uc获取用户信息开始");
            PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token,CommonConst.SYSTEM_NAME_ENGLISH);
            logger.warn("------------->Uc获取用户信息结束");
            if (userInfoDTO == null){
                throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
            }
            Integer lookphone =Integer.parseInt(userInfoDTO.getUser_info().getLook_phone());
            Integer user_Id = Integer.parseInt(userInfoDTO.getUser_info().getUser_id());
            logger.warn("------------->数据库查询开始");
            List<CustomerInfo> customerInfoList = customerInfoMapper.specialListByOffer(map);
            logger.warn("------------->数据库查询结束");
            logger.warn("------------->数据库查询总数量开始");
            Integer total = customerInfoMapper.specialListByOfferCount(map);
            logger.warn("------------->数据库查询总数量结束");
            if (customerInfoList != null && customerInfoList.size() > 0) {
                for (CustomerInfo customerInfo : customerInfoList){
                    if (!channleList.contains(customerInfo.getUtmSource())){
                        channleList.add(customerInfo.getUtmSource());
                    }
                    CustomerListDTO customerDto = new CustomerListDTO();
                    EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto,lookphone,userId);
                    resultDto.add(customerDto);
                }
                //屏蔽媒体
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("channelNames",channleList);
                Map<String,String> mediaMap = theaClientService.getMediaName(token,jsonObject);
                for (CustomerListDTO customerListDTO : resultDto ){
                    System.out.println(mediaMap.get(customerListDTO.getUtmSource()));
                    customerListDTO.setUtmSource(mediaMap.get(customerListDTO.getUtmSource()));
                }
            }
            result.setRows(resultDto);
            result.setTotal(total.toString());
        }
        return  result;
    }
}
