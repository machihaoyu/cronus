package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.api.thea.ConfigDTO;
import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.crm.OcdcData;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.entity.AllocateEntity;
import com.fjs.cronus.enums.AllocateSource;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.AgainAllocateCustomer;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerSalePushLog;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.util.DEC3Util;
import com.fjs.cronus.util.EntityToDto;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by feng on 2017/9/15.
 */
@Service
public class OcdcService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //    @Autowired
//    private ConfigRedisService configRedisService;
    @Autowired
    private AgainAllocateCustomerService againAllocateCustomerService;

    @Autowired
    private TheaService theaService;

    @Autowired
    private CustomerSalePushLogService customerSalePushLogService;

    @Autowired
    RestTemplate restTemplate;

//    @Autowired
//    private AllocateLogService allocateLogService;

    @Autowired
    private AutoAllocateService autoAllocateService;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;


    @Autowired
    private CustomerInfoService customerInfoService;

    /**
     * 添加OCDC推送客户信息
     */
//    @Transactional
//    public List<String> addOcdcCustomer(List<Map<String, Object>> listObj) {
//
//        List<String> successlist = new ArrayList<>();
//        //获取推送数据字段信息
////        String[] logArray = CommonConst.CUSTOMER_SALE_PUSH_LOG;
//        //遍历OCDC数据信息
//        List<CustomerSalePushLog> customerSalePushLogList = new ArrayList<CustomerSalePushLog>();
//        for (Map<String, Object> map : listObj) {
//            CustomerSalePushLog customerSalePushLog = null;//this.queryCustomerSalePushLogByOcdcPushData(map);
//            try {
//                AllocateEntity allocateEntity = new AllocateEntity();
//                Map<String, Object> mapc = new HashedMap();
//                mapc.put("telephonenumber", customerSalePushLog.getTelephonenumber());
//                List<CustomerInfo> customerInfoList = customerInfoMapper.selectByOCDCPhone(mapc);
//                if (!CollectionUtils.isEmpty(customerInfoList) && customerInfoList.size() > 0) { //重复客户
//
//                    if (isActiveApplicationChannel(customerSalePushLog)) {//主动申请
//                        //无负责人
//                        if (customerSalePushLog.getOwnerUserId() == null || customerSalePushLog.getOwnerUserId() == 0) {
//                            //自动分配
//                            CustomerDTO customerDTO = new CustomerDTO();
//                            EntityToDto.customerEntityToCustomerDto(customerInfoList.get(0), customerDTO);
//                            allocateEntity = autoAllocateService.autoAllocate(customerDTO, AllocateSource.OCDC);
//                        }
//                        //有负责人分给对应的业务员
//                        else {
//                            queryLoanListByOcdcPushData(customerSalePushLog);
//                        }
//                    }
//                    //是不是三无客户
//                    else {
//                        if (isThreeNonCustomer(customerSalePushLog) || isRepeatPushInTime(customerSalePushLog)) {
//                            ;
//                        } else {
//                            //有无负责人,有负责人跟进，没有自动分配
//                            if (customerSalePushLog.getOwnerUserId() == null || customerSalePushLog.getOwnerUserId() == 0) {
//                                //自动分配
//                                CustomerDTO customerDTO = new CustomerDTO();
//                                BeanUtils.copyProperties(customerInfoList.get(0),customerDTO);
////                                EntityToDto.customerEntityToCustomerDto(customerInfoList.get(0), customerDTO);
//                                allocateEntity = autoAllocateService.autoAllocate(customerDTO, AllocateSource.OCDC);
//                            } else {
//                                //发消息业务员，提醒跟进
//                            }
//                        }
//                    }
//                } else {
//                    CustomerDTO customerDTO = new CustomerDTO();
//                    BeanUtils.copyProperties(customerSalePushLog, customerDTO);
//                    allocateEntity = autoAllocateService.autoAllocate(customerDTO, AllocateSource.OCDC);
//                }
//                if (allocateEntity.getAllocateStatus() != null) {
//                    switch (allocateEntity.getAllocateStatus().getCode()) {
//                        case "0":
//                            break;
//                        case "1":
//                            break;
//                        case "2":
//                            //未分配，添加到待分配池//todo
//                            againAllocateCustomerService.saveStatusByDataId(map);
//                            break;
//                    }
//                }
//                successlist.add(customerSalePushLog.getOcdcId().toString());
//            } catch (RuntimeException E) {
//
//            }
//            customerSalePushLogList.add(customerSalePushLog);
//        }
//
//        //保存OCDC推送日志
//        customerSalePushLogService.insertList(customerSalePushLogList);
//        return successlist;
//    }


    public List<String> addOcdcCustomerNew(OcdcData ocdcData,String token) {

        List<String> successlist = new ArrayList<>();
        //遍历OCDC数据信息
        List<CustomerSalePushLog> customerSalePushLogList = new ArrayList<CustomerSalePushLog>();
/*
<<<<<<< HEAD
        for (Map<String, Object> map : listObj) {
            CustomerSalePushLog customerSalePushLog = this.queryCustomerSalePushLogByOcdcPushData(map);
            try {
                AllocateEntity allocateEntity = new AllocateEntity();
                Map<String, Object> mapc = new HashedMap();
                List paramsList = new ArrayList();
                //TODO加密
                paramsList.add(DEC3Util.des3EncodeCBC(customerSalePushLog.getTelephonenumber()));
                paramsList.add(customerSalePushLog.getTelephonenumber());

                mapc.put("paramsList",paramsList);
                List<CustomerInfo> customerInfoList = customerInfoMapper.selectByOCDCPhone(mapc);
                if (!CollectionUtils.isEmpty(customerInfoList) && customerInfoList.size() > 0) { //重复客户

                    if (isActiveApplicationChannel(customerSalePushLog)) {//主动申请
                        //无负责人
                        if (customerSalePushLog.getOwnerUserId() == null || customerSalePushLog.getOwnerUserId() == 0) {
                            //自动分配
                            CustomerDTO customerDTO = new CustomerDTO();
                            EntityToDto.customerEntityToCustomerDto(customerInfoList.get(0), customerDTO);
                            allocateEntity = autoAllocateService.autoAllocate(customerDTO, AllocateSource.OCDC);
                        }
                        //有负责人分给对应的业务员
                        else {
                            queryLoanListByOcdcPushData(customerSalePushLog);
                        }
                    }
                    //是不是三无客户
                    else {
                        if (isThreeNonCustomer(customerSalePushLog) || isRepeatPushInTime(customerSalePushLog)) {
                            ;
                        } else {
                            //有无负责人,有负责人跟进，没有自动分配
=======
*/
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            for (String map : ocdcData.getData()) {
                JsonNode node = objectMapper.readValue(map, JsonNode.class);
                CustomerSalePushLog customerSalePushLog = this.queryCustomerSalePushLogByOcdcPushData(node);

                try {
                    AllocateEntity allocateEntity = new AllocateEntity();
                    Map<String, Object> mapc = new HashedMap();
                    mapc.put("telephonenumber", customerSalePushLog.getTelephonenumber());
                    List<CustomerInfo> customerInfoList = customerInfoMapper.selectByOCDCPhone(mapc);
                    if (!CollectionUtils.isEmpty(customerInfoList) && customerInfoList.size() > 0) { //重复客户

                        if (isActiveApplicationChannel(customerSalePushLog)) {//主动申请
                            //无负责人
                            if (customerSalePushLog.getOwnerUserId() == null || customerSalePushLog.getOwnerUserId() == 0) {
                                //自动分配
                                CustomerDTO customerDTO = new CustomerDTO();
                                EntityToDto.customerEntityToCustomerDto(customerInfoList.get(0), customerDTO);
                                allocateEntity = autoAllocateService.autoAllocate(customerDTO, AllocateSource.OCDC,token);
                            }
                            //有负责人分给对应的业务员
                            else {
                                queryLoanListByOcdcPushData(customerSalePushLog);
                            }
                        }
                        //是不是三无客户
                        else {
                            if (isThreeNonCustomer(customerSalePushLog) || isRepeatPushInTime(customerSalePushLog)) {
                                ;
                            } else {
                                //有无负责人,有负责人跟进，没有自动分配
                                if (customerSalePushLog.getOwnerUserId() == null || customerSalePushLog.getOwnerUserId() == 0) {
                                    //自动分配
                                    CustomerDTO customerDTO = new CustomerDTO();
                                    BeanUtils.copyProperties(customerInfoList.get(0), customerDTO);
//                                EntityToDto.customerEntityToCustomerDto(customerInfoList.get(0), customerDTO);
                                    allocateEntity = autoAllocateService.autoAllocate(customerDTO, AllocateSource.OCDC,token);
                                } else {
                                    //发消息业务员，提醒跟进
                                }
                            }
                        }
                    } else {
                        CustomerDTO customerDTO = new CustomerDTO();
                        BeanUtils.copyProperties(customerSalePushLog, customerDTO);
                        allocateEntity = autoAllocateService.autoAllocate(customerDTO, AllocateSource.OCDC,token);
                    }
                    if (allocateEntity.getAllocateStatus() != null) {
                        switch (allocateEntity.getAllocateStatus().getCode()) {
                            case "0":
                                break;
                            case "1":
                                break;
                            case "2":
                                //未分配，添加到待分配池//todo
                                AgainAllocateCustomer againAllocateCustomer = new AgainAllocateCustomer();
                                againAllocateCustomer.setDataId(customerSalePushLog.getId());
                                againAllocateCustomer.setJsonData(map);
                                againAllocateCustomer.setCreateTime(new Date());
                                againAllocateCustomer.setUpdataTime(new Date());
                                againAllocateCustomerService.addAgainAllocateCustomer(againAllocateCustomer);
                                break;
                        }
                    }
                    successlist.add(customerSalePushLog.getOcdcId().toString());
                } catch (RuntimeException E) {

                }
                customerSalePushLogList.add(customerSalePushLog);
            }
        }
        catch (Exception e)
        {}

        //保存OCDC推送日志
        customerSalePushLogService.insertList(customerSalePushLogList);
        return successlist;
    }

    /**
     * 客服推送客户
     *
     * @param customerDTO
     */
    public void serviceAllocate(CustomerDTO customerDTO) {

//        AllocateEntity allocateEntity = new AllocateEntity();
//        if (customerDTO.getId() > 0) {
//            allocateEntity = autoAllocateService.autoAllocate(customerDTO, AllocateSource.SERVICES);
//        }
    }

    /**
     * 待分配池定时分配
     */
    //@Scheduled()
    public void waitingPoolAllocate() {

//        AllocateEntity allocateEntity = new AllocateEntity();
//        allocateEntity = autoAllocateService.autoAllocate(null, AllocateSource.WAITING);
    }


    /**
     * 接收从OCDC接收的客户信息
     *
     * @param customerSalePushLogList
     */
    public void addLoanByOcdcPush(List<CustomerSalePushLog> customerSalePushLogList) {
        //先判断输入的数据对象是否已经存在交易
        //根据电话号码查询交易系统数据
//        String telListStr = customerSalePushLogList.get(0).getTelephonenumber();
//        for (int i = 1; i < customerSalePushLogList.size(); i++) {
//            telListStr += "," + customerSalePushLogList.get(i).getTelephonenumber();
//        }
//        List<CustomerInfo> ocdcPushLoanList = this.queryLoanListByOcdcPushData(customerSalePushLogList);
//        Map<String, Object> map = new HashMap<>();
//        map.put("inPhone",telListStr);
//        List<CustomerInfo> customerInfoList = customerInfoMapper.selectByPhone(map);
//        String loanListTelStr = "";
//        //系统已存在的交易电话号码集合
//        if (null != customerInfoList && customerInfoList.size() > 0) {
//            loanListTelStr = customerInfoList.get(0).getTelephonenumber();
//            for (int i = 1; i < customerInfoList.size(); i++) {
//                loanListTelStr += "," + customerInfoList.get(i).getTelephonenumber();
//            }
//        }
//        List<CustomerInfo> newList = new ArrayList<>();//新客户
//        List<CustomerInfo> oldList = new ArrayList<>();//老客户
//
//        for (CustomerInfo customerInfo : ocdcPushLoanList) {
//            if (StringUtils.contains(loanListTelStr, customerInfo.getTelephonenumber())) {
//                oldList.add(customerInfo);
//            } else {
//                newList.add(customerInfo);
//            }
//        }
//
//
//        //批量处理重复客户申请
//        List<Integer> successIds = new ArrayList<>();
//        List<Integer> failIds = new ArrayList<>();
//        if ((null != customerInfoList && customerInfoList.size() > 0) && (null != oldList && oldList.size() > 0)) {
//            //两组集合对比，规划出对应的比例
////            for (Loan ocdcLoan : oldLoanList) {
////                for (Loan loan : loanList) {
////
////                }
////            }
//        }
//        //批量处理新客户
//        if (null != newList && newList.size() > 0) {
////            for (Loan loan : newLoanList) {
////                loan.setOwnUserId(0);
////                loan.setFirstAllocateTime(new Date());
////                Boolean falge =  autoAllocateService.autoAllocate(loan);
////                if (falge) {
////                    successIds.add(loan.getOcdcDataId());
////                } else {
////                    failIds.add(loan.getOcdcDataId());
////                }
////            }
//        }
        //回调OCDC

    }

    /**
     * OCDC推送开关
     */
/*    public void ocdcPushLock() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                *//*String value = configRedisService.getConfigValue(CommonRedisConst.OCDC_PUSH_LOCK);
                if (value.equals(CommonRedisConst.OCDC_PUSH_LOCK_1)) {//向OCDC发送推送延时请求(系统正在处理上一次的数据)
                }*//*
            }
        }

        ).start();

    }*/


    /**
     * 重复的客户是不是需要走自动分配
     */
    /*public Boolean checkRepeatLoanDoAutoAllocate(Loan pushLoan, Loan localLoan) {
        String configValue = configRedisService.getConfigValue(CommonConst.CAN_NOT_ALLOCATE_CUSTOMER_CLASSIFY);
        if (StringUtils.isNotBlank(localLoan.getCustomerClassify())) {
            //重复的客户在特定的时间内之前如果有被自动分配过,就不能再分配,直接让他成功
            //获取配置
            configValue = configRedisService.getConfigValue(CommonConst.R_CUSTOMER_CANNOT_INTO_SALE_TIME);
            //获取该客户的分配日志
            Map<String, Object> map = new HashMap();
            map.put("loanId", localLoan.getId());
            map.put("operation", CommonEnum.LOAN_OPERATION_TYPE_0.getCodeDesc());
            List<AllocateLog> allocateLogList = allocateLogService.selectByParamsMap(map);
            if (null != allocateLogList && allocateLogList.size() > 0) {
                if (null != allocateLogList.get(0).getCreateTime() && StringUtils.isNotBlank(configValue)) {
                    //时间小于配置中的推送时间间隔
                    if ((System.currentTimeMillis() - allocateLogList.get(0).getCreateTime().getTime())
                            <= Integer.valueOf(configValue) * 1000) {
                        //添加日志

                        return true;
                    }
                }
            }
            //对于重复客户的逻辑,如果存在存在负责人,提示,不存在则重新分配
            if (null != localLoan.getOwnUserId() && 0 != localLoan.getOwnUserId()) {
                this.repeatCustomerHasOwnerOperate();
            } else {//否则走自动分配
                pushLoan.setFirstAllocateTime(new Date());
                pushLoan.setOwnUserId(CommonEnum.NO.getCode());
                //调用自动分配逻辑走自动分配
                Boolean flage = autoAllocateService.autoAllocate(pushLoan);
                return flage;
            }
        }
        return false;
    }*/


    /**
     * 根据OCDC推送数据生成交易数据
     *
     * @param customerSalePushLog
     * @return
     */
    public Integer queryLoanListByOcdcPushData(CustomerSalePushLog customerSalePushLog) {
        CustomerInfo customerInfo = new CustomerInfo();
        LoanDTO loan = new LoanDTO();
        if (null != customerSalePushLog.getCustomerId()) {
            loan.setCustomerId(customerSalePushLog.getCustomerId());
        }
        if (null != customerSalePushLog.getLoanAmount()) {
            loan.setLoanAmount(customerSalePushLog.getLoanAmount());
        }
        if (StringUtils.isNotEmpty(customerSalePushLog.getTelephonenumber())) {
            loan.setTelephonenumber(customerSalePushLog.getTelephonenumber());
        }
        if (StringUtils.isNotEmpty(customerSalePushLog.getCustomerName())) {
            loan.setCustomerName(customerSalePushLog.getCustomerName());
        }
        if (null != customerSalePushLog.getCreateTime()) {
            loan.setCreateTime(customerSalePushLog.getCreateTime());
        }
        TheaApiDTO theaApiDTO = theaService.inserLoan(loan);
        if (theaApiDTO != null && theaApiDTO.getResult() == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * OCDC推送对象转换成本地推送日志
     *
     * @param map
     * @return
     */
    public CustomerSalePushLog queryCustomerSalePushLogByOcdcPushData(JsonNode map) {
        CustomerSalePushLog customerSalePushLog = new CustomerSalePushLog();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        try {
            if (null != map.get("id") && StringUtils.isNotBlank(map.get("id").toString())) {
                customerSalePushLog.setOcdcId(map.get("id").asInt());
            } else {
                customerSalePushLog.setOcdcId(CommonEnum.NO.getCode());
            }
            if (null != map.get("customer_id") && StringUtils.isNotBlank(map.get("customer_id").toString())) {
                customerSalePushLog.setCustomerId(map.get("customer_id").asInt());
            } else {
                customerSalePushLog.setCustomerId(CommonEnum.NO.getCode());
            }
            customerSalePushLog.setLoanId(CommonEnum.NO.getCode());
            if (null != map.get("telephonenumber") && StringUtils.isNotBlank(map.get("telephonenumber").toString())) {
                customerSalePushLog.setTelephonenumber(map.get("telephonenumber").asText());
            }
            //customer_name
            if (null != map.get("name") && StringUtils.isNotBlank(map.get("name").toString())) {
                customerSalePushLog.setCustomerName(map.get("name").asText());
            } else {
                customerSalePushLog.setCustomerName(CommonConst.DEFAULT_CUSTOMER_NAME + map.get("data_id").toString());
            }
            if (null != map.get("owner_user_id") && StringUtils.isNotBlank(map.get("owner_user_id").toString())) {
                customerSalePushLog.setOwnerUserId(map.get("owner_user_id").asInt());
            } else {
                customerSalePushLog.setOwnerUserId(CommonEnum.NO.getCode());
            }
            if (null != map.get("owner_user_name") && StringUtils.isNotBlank(map.get("owner_user_name").toString())) {
                customerSalePushLog.setOwnerUserName(map.get("owner_user_name").asText());
            }
            if (null != map.get("creater_user_id") && StringUtils.isNotBlank(map.get("creater_user_id").toString())) {
                customerSalePushLog.setCreaterUserId(map.get("creater_user_id").asInt());
            } else {
                customerSalePushLog.setCreaterUserId(CommonEnum.NO.getCode());
            }
            if (null != map.get("customer_level") && StringUtils.isNotBlank(map.get("customer_level").toString())) {
                customerSalePushLog.setCustomerLevel(map.get("customer_level").asText());
            } else {
                customerSalePushLog.setCustomerLevel(CommonEnum.CUSTOMER_LEVEL_0.getCodeDesc());
            }
            if (null != map.get("loan_amount") && StringUtils.isNotBlank(map.get("loan_amount").toString())) {
                customerSalePushLog.setLoanAmount(map.get("loan_amount").decimalValue());
            }
            if (null != map.get("spare_phone") && StringUtils.isNotBlank(map.get("spare_phone").toString())) {
                customerSalePushLog.setSparePhone(map.get("spare_phone").asText());
            }
            if (null != map.get("age") && StringUtils.isNotBlank(map.get("age").toString())) {
                customerSalePushLog.setAge(map.get("age").asText());
            }
            if (null != map.get("marriage") && StringUtils.isNotBlank(map.get("marriage").toString())) {
                customerSalePushLog.setMarriage(map.get("marriage").asText());
            }
            if (null != map.get("id_card") && StringUtils.isNotBlank(map.get("id_card").toString())) {
                customerSalePushLog.setIdCard(map.get("id_card").asText());
            }
            if (null != map.get("province_huji") && StringUtils.isNotBlank(map.get("province_huji").toString())) {
                customerSalePushLog.setProvinceHuji(map.get("province_huji").asText());
            }
            if (null != map.get("sex") && StringUtils.isNotBlank(map.get("sex").toString())) {
                customerSalePushLog.setSex(map.get("sex").asText());
            } else {
                customerSalePushLog.setSex(CommonEnum.SEX_MALE.getCodeDesc());
            }
            if (null != map.get("customer_address") && StringUtils.isNotBlank(map.get("customer_address").toString())) {
                customerSalePushLog.setCustomerAddress(map.get("customer_address").asText());
            }
            if (null != map.get("per_description") && StringUtils.isNotBlank(map.get("per_description").toString())) {
                customerSalePushLog.setPerDescription(map.get("per_description").asText());
            }
            if (null != map.get("house_amount") && StringUtils.isNotBlank(map.get("house_amount").toString())) {
                customerSalePushLog.setHouseAmount(map.get("house_amount").asText());
            }
            if (null != map.get("house_type") && StringUtils.isNotEmpty(map.get("house_type").toString())) {
                customerSalePushLog.setHouseType(map.get("house_type").asText());
            }
            if (null != map.get("house_value") && StringUtils.isNotBlank(map.get("house_value").toString())) {
                customerSalePushLog.setHouseValue(map.get("house_value").asText());
            }
            if (null != map.get("house_area") && StringUtils.isNotBlank(map.get("house_area").toString())) {
                customerSalePushLog.setHouseArea(map.get("house_area").asText());
            }
            if (null != map.get("house_age") && StringUtils.isNotBlank(map.get("house_age").toString())) {
                customerSalePushLog.setHouseAge(map.get("house_age").toString());
            }
            if (null != map.get("house_loan") && StringUtils.isNotBlank(map.get("house_loan").toString())) {
                customerSalePushLog.setHouseLoan(map.get("house_loan").asText());
            } else {
                customerSalePushLog.setHouseLoan(CommonEnum.HOUSE_LOAN_0.getCodeDesc());
            }
            if (null != map.get("house_alone") && StringUtils.isNotBlank(map.get("house_alone").toString())) {
                customerSalePushLog.setHouseAlone(map.get("house_alone").asText());
            } else {
                customerSalePushLog.setHouseAlone(CommonEnum.HOUSE_ALONE_0.getCodeDesc());
            }
            if (null != map.get("house_location") && StringUtils.isNotBlank(map.get("house_location").toString())) {
                customerSalePushLog.setHouseLoan(map.get("house_location").asText());
            }
            if (null != map.get("city") && StringUtils.isNotBlank(map.get("city").toString())) {
                customerSalePushLog.setCity(map.get("city").asText());
            }
            if (null != map.get("retain") && StringUtils.isNotBlank(map.get("retain").toString())) {
                customerSalePushLog.setRetain( map.get("retain").asInt());
            } else {
                customerSalePushLog.setRetain(CommonEnum.RETAIN_STATUE_0.getCode());
            }
            customerSalePushLog.setCreateTime(new Date());
            customerSalePushLog.setUpdateTime(new Date());
            customerSalePushLog.setReceiveTime(new Date());
            if (null != map.get("is_lock") && StringUtils.isNotBlank(map.get("is_lock").toString())) {
                customerSalePushLog.setIsLock((Integer) map.get("is_lock").asInt());
            } else {
                customerSalePushLog.setIsLock(CommonEnum.NO.getCode());
            }
            if (null != map.get("phone_view_time") && StringUtils.isNotBlank(map.get("phone_view_time").toString())) {

                customerSalePushLog.setPhoneViewTime(sdf.parse(map.get("phone_view_time").asText()));
            }
            if (null != map.get("phone_view_uid") && StringUtils.isNotBlank(map.get("phone_view_uid").toString())) {
                customerSalePushLog.setPhoneViewUid((Integer) map.get("phone_view_uid").asInt());
            } else {
                customerSalePushLog.setPhoneViewUid(CommonEnum.NO.getCode());
            }
            if (null != map.get("phone_view_count") && StringUtils.isNotBlank(map.get("phone_view_count").toString())) {
                customerSalePushLog.setPhoneViewCount((Integer) map.get("phone_view_count").asInt());
            } else {
                customerSalePushLog.setPhoneViewCount(CommonEnum.NO.getCode());
            }
            if (null != map.get("autostatus") && StringUtils.isNotBlank(map.get("autostatus").toString())) {
                customerSalePushLog.setAutostatus((Integer) map.get("autostatus").asInt());
            } else {
                customerSalePushLog.setAutostatus(CommonEnum.YES.getCode());
            }
            if (null != map.get("utm_source") && StringUtils.isNotBlank(map.get("utm_source").toString())) {
                customerSalePushLog.setUtmSource(map.get("utm_source").asText());
            }
            if (null != map.get("customer_source") && StringUtils.isNotBlank(map.get("customer_source").toString())) {
                customerSalePushLog.setCustomerSource(map.get("customer_source").asText());
            }
            if (null != map.get("customer_classify") && StringUtils.isNotBlank(map.get("customer_classify").toString())) {
                customerSalePushLog.setCustomerClassify(map.get("customer_classify").asText());
            } else {
                customerSalePushLog.setCustomerClassify(CommonEnum.CUSTOMER_CLASSIFY_1.getCodeDesc());
            }
            if (null != map.get("laiyuan") && StringUtils.isNotBlank(map.get("laiyuan").toString())) {
                customerSalePushLog.setLaiyuan((Integer) map.get("laiyuan").asInt());
            } else {
                customerSalePushLog.setLaiyuan(CommonEnum.LAIYUAN_0.getCode());
            }
            //ext
            if (null != map.get("exend_text") && StringUtils.isNotBlank(map.get("exend_text").toString())) {
                customerSalePushLog.setExt(map.get("exend_text").toString());
            }
            if (null != map.get("repeat_callback_time") && StringUtils.isNotBlank(map.get("repeat_callback_time").toString())) {
                customerSalePushLog.setRepeatCallbackTime(sdf.parse(map.get("repeat_callback_time").asText()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("--------------------------OCDC转CustomerPush失败！！");
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        }
        return customerSalePushLog;
    }


    /**
     * 判断是否是三无客户
     *
     * @param customerSalePushLog
     * @return
     */
    public Boolean isThreeNonCustomer(CustomerSalePushLog customerSalePushLog) {
        String configValue = theaService.getConfigByName("CanNotAllocateCustomerClassify").getData();
        if (StringUtils.isNotEmpty(configValue) && configValue.contains(customerSalePushLog.getCustomerClassify()))
            return true;
        else
            return false;
    }

    /**
     * 判断是不是客户主动申请渠道
     *
     * @param customerSalePushLog
     * @return
     */
    public Boolean isActiveApplicationChannel(CustomerSalePushLog customerSalePushLog) {
        String activeApplicationChannel = theaService.getConfigByName("activeApplicationChannel").getData();
        if (StringUtils.isNotEmpty(activeApplicationChannel) && activeApplicationChannel.contains(customerSalePushLog.getUtmSource()))
            return true;
        else
            return false;
    }

    /**
     * 判断有无负责人
     *
     * @param customerSalePushLog
     * @return
     */
    public Boolean hasOwnerUser(CustomerSalePushLog customerSalePushLog) {
        if (customerSalePushLog != null && customerSalePushLog.getOwnerUserId() != null && customerSalePushLog.getOwnerUserId() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 渠道是不是属于直接进公盘渠道
     *
     * @return
     */
    public boolean isPublicOffer(CustomerSalePushLog customerSalePushLog) {
        String activeApplicationChannel = theaService.getConfigByName("allocateToNoUserPool").getData();
        if (activeApplicationChannel.contains(customerSalePushLog.getUtmSource()))
            return true;
        else
            return false;
    }

    /**
     * 判断是不是分配城市
     *
     * @return
     */
    public boolean isDistributeCity(CustomerSalePushLog customerSalePushLog) {
        String activeApplicationChannel = theaService.getConfigByName("canAllocateCity").getData();
        if (activeApplicationChannel.contains(customerSalePushLog.getCity()))
            return true;
        else
            return false;
    }

    /**
     * 判断是不是客服推送过来的
     *
     * @return
     */
    public boolean isCustomerServicePush(CustomerSalePushLog customerSalePushLog) {
        if (customerSalePushLog.getLaiyuan() != null && customerSalePushLog.getLaiyuan().equals(1))
            return true;
        else return false;
    }

    /**
     * 判断是不是待分配推送
     *
     * @return
     */
    public boolean isPendingPush() {
        return false;
    }

    /**
     * 是不是设定时间段内不能重复推入客户
     *
     * @return
     */
    public boolean isRepeatPushInTime(CustomerSalePushLog customerSalePushLog) {
        String configValue = theaService.getConfigByName("RCustomerCannotIntoSaleTime").getData();//CanNotAllocateCustomerClassify
        //时间小于配置中的推送时间间隔
        if ((System.currentTimeMillis() - customerSalePushLog.getCreateTime().getTime())
                <= Integer.valueOf(configValue) * 1000) {
            return true;
        } else
            return false;
    }


}
