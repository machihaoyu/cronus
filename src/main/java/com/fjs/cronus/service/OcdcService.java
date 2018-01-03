package com.fjs.cronus.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.crm.OcdcData;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.entity.AllocateEntity;
import com.fjs.cronus.enums.AllocateSource;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.AgainAllocateCustomer;
import com.fjs.cronus.model.CustomerSalePushLog;
import com.fjs.cronus.service.client.ThorInterfaceService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.util.DEC3Util;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by feng on 2017/9/15.
 */
@Service
public class OcdcService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${phpSystem.customerToService}")
    private String customerToService;

    @Value("${phpSystem.pushCallback}")
    private String pushCallback;

    @Value("${phpSystem.ocdcKey}")
    private String ocdcKey;

    @Autowired
    private AgainAllocateCustomerService againAllocateCustomerService;

    @Autowired
    private TheaClientService theaClientService;

    @Autowired
    private ThorInterfaceService thorUcService;

    @Autowired
    private CustomerSalePushLogService customerSalePushLogService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private AutoAllocateService autoAllocateService;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CustomerInfoService customerInfoService;

    public List<String> addOcdcCustomer(OcdcData ocdcData, AllocateSource allocateSource, String token) {

        CronusDto resultDto = new CronusDto();
        List<String> successList = new ArrayList<>();
        List<String> failList = new ArrayList<>();
        //遍历OCDC数据信息
        List<CustomerSalePushLog> customerSalePushLogList = new ArrayList<CustomerSalePushLog>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            for (String map : ocdcData.getData()) {
                JsonNode node = objectMapper.readValue(map, JsonNode.class);
                CustomerSalePushLog customerSalePushLog = this.queryCustomerSalePushLogByOcdcPushData(node);

                try {
                    AllocateEntity allocateEntity = new AllocateEntity();
                    CustomerDTO customerDTO = getCustomer(customerSalePushLog.getTelephonenumber());
                    if (customerDTO != null && customerDTO.getId() != null && customerDTO.getId() > 0) { //重复客户
                        customerDTO.setTelephonenumber(customerSalePushLog.getTelephonenumber());
                        if (isActiveApplicationChannel(customerSalePushLog)) {//主动申请渠道
                            //无负责人
                            if (customerDTO.getOwnerUserId() == null || customerDTO.getOwnerUserId() == 0) {
                                //自动分配
                                allocateEntity = autoAllocateService.autoAllocate(customerDTO, allocateSource, token);
                            } else {//有负责人分给对应的业务员
                                customerDTO.setLoanAmount(customerSalePushLog.getLoanAmount());
                                sendMail(token, customerDTO);
                                SimpleUserInfoDTO simpleUserInfoDTO = thorUcService.getUserInfoById(token, customerDTO.getOwnerUserId()).getData();
                                if (simpleUserInfoDTO != null && simpleUserInfoDTO.getSub_company_id() != null) {
                                    customerDTO.setSubCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                                }
                                autoAllocateService.addLoan(customerDTO, token);
                                allocateEntity.setSuccess(true);
                            }
                        } else {
                            if (isThreeNonCustomer(customerSalePushLog) || isRepeatPushInTime(customerSalePushLog)) {
                                allocateEntity.setSuccess(true);
                            } else {
                                //有无负责人,有负责人跟进，没有自动分配
                                if (customerDTO.getOwnerUserId() == null || customerDTO.getOwnerUserId() == 0) {
                                    //自动分配
                                    allocateEntity = autoAllocateService.autoAllocate(customerDTO, allocateSource, token);
                                } else {
                                    //发消息业务员，提醒跟进
                                    sendMail(token, customerDTO);
                                    allocateEntity.setSuccess(true);
                                }
                            }
                        }
                    } else {
                        BeanUtils.copyProperties(customerSalePushLog, customerDTO);
                        allocateEntity = autoAllocateService.autoAllocate(customerDTO, allocateSource, token);
                    }
                    if (allocateEntity.getAllocateStatus() != null) {
                        switch (allocateEntity.getAllocateStatus().getCode()) {
                            case "0":
                                break;
                            case "1":
                                break;
                            case "2":
                                //未分配，添加到待分配池
                                AgainAllocateCustomer againAllocateCustomer = new AgainAllocateCustomer();
                                againAllocateCustomer.setDataId(customerSalePushLog.getOcdcId());
                                againAllocateCustomer.setJsonData(map);
                                againAllocateCustomer.setCreateTime(new Date());
                                againAllocateCustomer.setUpdateTime(new Date());
                                againAllocateCustomerService.addAgainAllocateCustomer(againAllocateCustomer);
                                break;
                            case "4":
                                pushServiceSystem(map);
                                break;
                        }
                    }
                    if (allocateEntity.isSuccess()) {
                        successList.add(customerSalePushLog.getOcdcId().toString());
                    } else {
                        failList.add(customerSalePushLog.getOcdcId().toString());
                    }
                } catch (RuntimeException e) {
                    logger.warn(e.getMessage());
                    failList.add(customerSalePushLog.getOcdcId().toString());
                }
                customerSalePushLogList.add(customerSalePushLog);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        //保存OCDC推送日志
        customerSalePushLogService.insertList(customerSalePushLogList);
        autoAllocateFeedback(successList, failList);
        return successList;
    }

    private CustomerDTO getCustomer(String telephone) {
        CustomerDTO customerDTO = null;
        Map<String, Object> mapc = new HashedMap();
        mapc.put("telephonenumber", DEC3Util.des3EncodeCBC(telephone));
        CronusDto<CustomerDTO> cronusDto = customerInfoService.fingByphone(telephone);
        if (cronusDto.getResult() == 0) {
            customerDTO = cronusDto.getData();
        }
        return customerDTO;
    }

    private void sendMail(String token, CustomerDTO customerDTO) {
        theaClientService.sendMail(token,
                "客户姓名:" + customerDTO.getCustomerName() + ",客户电话:" + customerDTO.getTelephonenumber() + "，重复申请，请注意跟进；",
                0, 0, "系统管理员", customerDTO.getOwnerUserId());
    }

    /**
     * 客服推送客户
     *
     * @param
     */
    public String serviceAllocate(String servicedData, String token) {

        List<String> allocateEntities = new ArrayList<>();
        String success = "";
        try {
            OcdcData ocdcData = new OcdcData();
            List<String> listraw = new ArrayList<>();
            listraw.add(servicedData);
            ocdcData.setData(listraw);
            allocateEntities = addOcdcCustomer(ocdcData, AllocateSource.WAITING, token);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        if (allocateEntities.size() > 0)
            success = allocateEntities.get(0);
        return success;

    }

    /**
     * 待分配池定时分配 5min
     */
    public List<String> waitingPoolAllocate(String token) {

        List<String> allocateEntities = new ArrayList<>();
        try {
            OcdcData ocdcData = new OcdcData();
            List<String> listraw = new ArrayList<>();
            List<AgainAllocateCustomer> list = againAllocateCustomerService.getNonAllocateCustomer();
            for (AgainAllocateCustomer againCustomer :
                    list) {
                listraw.add(againCustomer.getJsonData());
            }
            ocdcData.setData(listraw);
            allocateEntities = addOcdcCustomer(ocdcData, AllocateSource.WAITING, token);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return allocateEntities;
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
     * 根据OCDC推送数据生成交易数据
     *
     * @param
     * @return
     */
//    public void createLoan(CustomerDTO customerDTO, String token) {
//        LoanDTO loan = new LoanDTO();
//        if (null != customerDTO.getId()) {
//            loan.setCustomerId(customerDTO.getId());
//        }
//        if (null != customerDTO.getLoanAmount()) {
//            loan.setLoanAmount(customerDTO.getLoanAmount());
//        }
//        if (StringUtils.isNotEmpty(customerDTO.getTelephonenumber())) {
//            loan.setTelephonenumber(customerDTO.getTelephonenumber());
//        }
//        if (StringUtils.isNotEmpty(customerDTO.getCustomerName())) {
//            loan.setCustomerName(customerDTO.getCustomerName());
//        }
//        loan.setOwnUserId(customerDTO.getOwnerUserId());
//        loan.setOwnUserName(customerDTO.getOwnUserName());
//        loan.setUtmSource("自申请");
//        theaClientService.insertLoan(loan, token);
//    }

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
                float value = (float)(Math.round(map.get("loan_amount").asLong()*100/10000))/100;
                customerSalePushLog.setLoanAmount(new BigDecimal(value));
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
                customerSalePushLog.setRetain(map.get("retain").asInt());
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
        String configValue = theaClientService.getConfigByName(CommonConst.CAN_NOT_ALLOCATE_CUSTOMER_CLASSIFY);
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
        String activeApplicationChannel = theaClientService.getConfigByName(CommonConst.ACTIVE_APPLICATION_CHANNEL);
        if (StringUtils.isNotEmpty(activeApplicationChannel) && activeApplicationChannel.contains(customerSalePushLog.getUtmSource()))
            return true;
        else
            return false;
    }

    /**
     * 是不是设定时间段内不能重复推入客户
     *
     * @return
     */
    public boolean isRepeatPushInTime(CustomerSalePushLog customerSalePushLog) {
        String configValue = theaClientService.getConfigByName(CommonConst.R_CUSTOMER_CANNOT_INTO_SALE_TIME);
        //时间小于配置中的推送时间间隔
        if ((System.currentTimeMillis() - customerSalePushLog.getCreateTime().getTime())
                <= Integer.valueOf(configValue) * 1000) {
            return true;
        } else
            return false;
    }

    /**
     * 未分配城市客户到客服系统
     *
     * @param json
     * @return
     */
    public String pushServiceSystem(String json) {
        if (phpSysConnectStatus()) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(90000);
            requestFactory.setReadTimeout(90000);
            restTemplate.setRequestFactory(requestFactory);
            MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
            postParameters.add("key", ocdcKey);
            postParameters.add("data", json);
            String str = restTemplate.postForObject(customerToService, postParameters, String.class);
            return str;
        }else return "";
    }


    /**
     * 销售系统自动分配后给ocdc反馈数据
     *
     * @param
     * @return
     */
    public String autoAllocateFeedback(List<String> successlist, List<String> failList) {
        if (phpSysConnectStatus()) {
            StringBuilder successes = new StringBuilder();
            StringBuilder fails = new StringBuilder();
            for (int i = 0; i < successlist.size(); i++) {
                successes.append(successlist.get(i));
                if (i < successlist.size() - 1)
                    successes.append(",");
            }
            for (int i = 0; i < failList.size(); i++) {
                fails.append(failList.get(i));
                if (i < failList.size() - 1)
                    fails.append(",");
            }

            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(90000);
            requestFactory.setReadTimeout(90000);
            restTemplate.setRequestFactory(requestFactory);
            MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
            postParameters.add("key", ocdcKey);
            postParameters.add("success", successes.toString());
            postParameters.add("fail", fails.toString());
            String str = restTemplate.postForObject(pushCallback, postParameters, String.class);
            return str;
        }
        else return "";
    }

    public boolean phpSysConnectStatus() {
        ValueOperations<String, String> redisConfigOptions = stringRedisTemplate.opsForValue();
        String status = redisConfigOptions.get(CommonConst.PHPSYS_CONNECT_STATUS);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(status) && status.equals("1"))
            return true;
        else
            return false;
    }


}
