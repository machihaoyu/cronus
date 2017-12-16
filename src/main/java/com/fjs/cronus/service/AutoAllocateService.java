package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;

import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.thea.WorkDayDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.CrmUserDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.entity.AllocateEntity;
import com.fjs.cronus.enums.AllocateEnum;
import com.fjs.cronus.enums.AllocateSource;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.client.ThorInterfaceService;
import com.fjs.cronus.service.redis.AllocateRedisService;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.util.CommonUtil;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by feng on 2017/9/21.
 */
@Service
public class AutoAllocateService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //    @Value("${publicToken}")
//    private String publicToken;

//    @Autowired
//    private ConfigRedisService configRedisService;

    @Autowired
    private ThorInterfaceService thorUcService;

    @Autowired
    private AllocateRedisService allocateRedisService;

    @Autowired
    private UserMonthInfoService userMonthInfoService;

    @Autowired
    private AllocateLogService allocateLogService;

    @Autowired
    private CustomerInfoService customerInfoService;

    @Autowired
    private TheaClientService theaClientService;

//    @Autowired
//    private LoanLogService loanLogService;

    @Autowired
    ThorInterfaceService thorInterfaceService;

    @Autowired
    private AgainAllocateCustomerService againAllocateCustomerService;


    @Autowired
    private CronusRedisService cronusRedisService;

    @Autowired
    private SmsService smsService;

    /**
     * 判断是不是客户主动申请渠道
     *
     * @param customerDTO
     * @return
     */
    public Boolean isActiveApplicationChannel(CustomerDTO customerDTO) {
        String activeApplicationChannel = theaClientService.getConfigByName(CommonConst.ACTIVE_APPLICATION_CHANNEL);
        if (activeApplicationChannel != null && activeApplicationChannel.contains(customerDTO.getUtmSource()))
            return true;
        else
            return false;
    }

    @Transactional
    public synchronized AllocateEntity autoAllocate(CustomerDTO customerDTO, AllocateSource allocateSource, String token) {
        AllocateEntity allocateEntity = new AllocateEntity();
        allocateEntity.setSuccess(true);
        try {
            //获取自动分配的城市
            String allocateCities = theaClientService.getConfigByName(CommonConst.CAN_ALLOCATE_CITY);

            UserInfoDTO ownerUser = getOwnerUser(customerDTO, token); //获取负责人

            boolean allocateToPublic = isAllocateToPublic(customerDTO.getUtmSource());

            //分配规则
            if (StringUtils.isNotEmpty(ownerUser.getUser_id())) {//存在这个在职负责人
                customerDTO.setOwnerUserId(Integer.valueOf(ownerUser.getUser_id()));
                allocateEntity.setAllocateStatus(AllocateEnum.EXIST_OWNER);
            } else if (allocateToPublic) {
                allocateEntity.setAllocateStatus(AllocateEnum.PUBLIC);
                customerDTO.setOwnerUserId(0);
            } else if (CommonConst.CUSTOMER_SOURCE_FANGSUDAI.equals(customerDTO.getUtmSource())
                    && CommonConst.UTM_SOURCE_FANGXIN.equals(customerDTO.getUtmSource())) {//房速贷，渠道fangxin直接到公盘
                customerDTO.setOwnerUserId(0);
                allocateEntity.setAllocateStatus(AllocateEnum.PUBLIC);
            } else if (StringUtils.contains(allocateCities, customerDTO.getCity())) {
                Integer ownUserId = getAllocateUser(customerDTO.getCity());
                if (ownUserId > 0) {
                    customerDTO.setOwnerUserId(ownUserId);
                    allocateEntity.setAllocateStatus(AllocateEnum.ALLOCATE_TO_OWNER);
                } else { //进入待分配池
                    customerDTO.setOwnerUserId(0);
                    allocateEntity.setAllocateStatus(AllocateEnum.WAITING_POOL);
                }
            } else {
                customerDTO.setOwnerUserId(0);
                switch (allocateSource.getCode()) {
                    case "0":
                    case "1":
                        allocateEntity.setAllocateStatus(AllocateEnum.PUBLIC);
                        break;
                    case "2"://推入客服系统
                        allocateEntity.setAllocateStatus(AllocateEnum.TO_SERVICE_SYSTEM);
                        try {

                        } catch (Exception e) {
                        }
                        break;
                }
            }

            //保存客户
            SimpleUserInfoDTO simpleUserInfoDTO = null;
            Integer customerId = 0;
            if (null != customerDTO.getId() && customerDTO.getId() > 0) {
                customerId = customerDTO.getId();
                if (null != customerDTO.getOwnerUserId() && customerDTO.getOwnerUserId() > 0) {
                    simpleUserInfoDTO = thorUcService.getUserInfoById(token, customerDTO.getOwnerUserId()).getData();
                    if (null != ownerUser.getSub_company_id()) {
                        customerDTO.setSubCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                    } else {
                        customerDTO.setSubCompanyId(0);
                    }
                }
                customerDTO.setReceiveTime(new Date());
                customerDTO.setLastUpdateTime(new Date());
                CustomerInfo customerInfo = new CustomerInfo();
                BeanUtils.copyProperties(customerDTO, customerInfo);
                if (allocateEntity.getAllocateStatus().getCode().equals("1")) {
                    customerInfo.setConfirm(1);
                    customerInfo.setClickCommunicateButton(0);
                    customerInfo.setCommunicateTime(null);
                }
                customerInfoService.editCustomerSys(customerInfo, token);
            } else {
                CronusDto<CustomerDTO> cronusDto = customerInfoService.fingByphone(customerDTO.getTelephonenumber());
                CustomerDTO hasCustomer = cronusDto.getData();
                if (null == hasCustomer || null == hasCustomer.getId()) {
                    switch (allocateEntity.getAllocateStatus().getCode()) {
                        case "0":
                        case "1":
                        case "3":
                            if (customerDTO.getOwnerUserId() != null && customerDTO.getOwnerUserId() > 0) {
                                simpleUserInfoDTO = thorUcService.getUserInfoById(token, customerDTO.getOwnerUserId()).getData();
                                if (null != simpleUserInfoDTO.getSub_company_id()) {
                                    customerDTO.setSubCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                                } else {
                                    customerDTO.setSubCompanyId(0);
                                }
                            }
                            //保存数据
                            customerDTO.setLastUpdateTime(new Date());
                            customerInfoService.addCustomer(customerDTO, token);
                            break;
                    }
                }
            }

            //分配日志&更新分配队列
            switch (allocateEntity.getAllocateStatus().getCode()) {
                case "0":
                    break;
                case "1":
                    String[] cityStrArrayAll = StringUtils.split(allocateCities, ",");
                    if (ArrayUtils.contains(cityStrArrayAll, customerDTO.getCity())) {
                        allocateRedisService.changeAllocateTemplet(customerDTO.getOwnerUserId(), customerDTO.getCity());
                    }
                    //如果是再分配盘的数据则标记再分配成功
                    if (allocateSource.getCode().equals("2")) {
                        Map<String, Object> againAllocateMap = new HashMap<>();
                        againAllocateMap.put("dataId", customerId);
                        againAllocateMap.put("status", CommonEnum.AGAIN_ALLOCATE_STATUS_1.getCodeDesc());
                        againAllocateCustomerService.saveStatusByDataId(againAllocateMap);
                    }
                    //添加分配日志
                    CustomerInfo customerInfo = new CustomerInfo();
                    EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
                    allocateLogService.addAllocatelog(customerInfo, customerDTO.getOwnerUserId(),
                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCode(), null);
                    activeChannelAddTansaction(customerDTO,token);
                    sendMessage(customerDTO.getCustomerName(), customerDTO.getOwnerUserId(), simpleUserInfoDTO, token);
                    break;
                case "2": //WAITING_POOL
                    sendCRMAssistantMessage(customerDTO.getCity(),customerDTO.getCustomerName(), token);
                    break;
                case "3":
                    //添加分配日志
                    CustomerInfo customerInfot = new CustomerInfo();
                    EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfot);
                    allocateLogService.addAllocatelog(customerInfot, customerDTO.getOwnerUserId(),
                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCode(), null);
                    activeChannelAddTansaction(customerDTO,token);
                    sendMessage(customerDTO.getCustomerName(), customerDTO.getOwnerUserId(), simpleUserInfoDTO, token);
                    break;
                case "4":
                    break;
            }

        } catch (Exception e) {
            logger.error("-------------------自动分配失败:ocdcDataId=" + customerDTO.getTelephonenumber() + "-------------------", e);
            allocateEntity.setSuccess(false);
        }
        return allocateEntity;
    }

    private void addCustomer(CustomerDTO customerDTO, String token) {
        SimpleUserInfoDTO simpleUserInfoDTO;
        if (customerDTO.getOwnerUserId() != null && customerDTO.getOwnerUserId() > 0) {
            simpleUserInfoDTO = thorUcService.getUserInfoById(token, customerDTO.getOwnerUserId()).getData();
            if (null != simpleUserInfoDTO.getSub_company_id()) {
                customerDTO.setSubCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
            } else {
                customerDTO.setSubCompanyId(0);
            }
        }
        //保存数据
        customerDTO.setLastUpdateTime(new Date());
        customerInfoService.addCustomer(customerDTO, token);
    }

    /**
     * 主动申请渠道添加交易
     *
     * @param customerDTO
     */
    private void activeChannelAddTansaction(CustomerDTO customerDTO,String token) {
        if (isActiveApplicationChannel(customerDTO)) {
            LoanDTO loanDTO = new LoanDTO();
            loanDTO.setTelephonenumber(customerDTO.getTelephonenumber());
            loanDTO.setLoanAmount(customerDTO.getLoanAmount());
            loanDTO.setCustomerId(customerDTO.getId());
            loanDTO.setCustomerName(customerDTO.getCustomerName());
            theaClientService.inserLoan(loanDTO,token);
        }
    }

    private void sendMessage(String customerName, Integer toId, SimpleUserInfoDTO ownerUser, String token) {
        theaClientService.sendMail(token,
                "房金所为您分配了客户名：" + customerName + "，请注意跟进。",
                0,
                0,
                "系统管理员",
                toId);

        smsService.sendSmsForAutoAllocate(ownerUser.getTelephone(), customerName);
    }

//    private void sendMessage(String customerName,Integer toId, SimpleUserInfoDTO ownerUser,String token) {
//        theaClientService.sendMail(token,
//                "房金所为您分配了客户名：" + customerName + "，请注意跟进。",
//                0,
//                0,
//                "系统管理员",
//                toId);
//    }

    private void sendCRMAssistantMessage(String customerCity,String customerName, String token) {

        BaseUcDTO<List<CrmUserDTO>> crmUser = thorInterfaceService.getCRMUser(token, customerCity);
        List<CrmUserDTO> crmUserDTOList = crmUser.getRetData();
        for (CrmUserDTO crmUserDTO :
                crmUserDTOList) {
            smsService.sendNonCommunicate(customerName, crmUserDTO.getPhone());
        }
    }

    private UserInfoDTO getOwnerUser(CustomerDTO customerDTO, String token) {
        //分析客户扩展信息
        JSONObject extJson = new JSONObject();
        if (StringUtils.isNotBlank(customerDTO.getExt())) {
            extJson = JSON.parseObject(customerDTO.getExt());
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();

        Integer salerId = 0;
        if (null != extJson.get("sale_id") &&
                StringUtils.isNotBlank(extJson.get("sale_id").toString())) {
            //如果sale_id的格式出现异常，强转失败，则继续走下一步的逻辑
            try {
                salerId = Integer.valueOf(extJson.get("sale_id").toString());
                if (0 != salerId) {
                    BaseUcDTO<UserInfoDTO> thorApiDTO = thorUcService.getUserInfoByField(token, null, salerId, null);
                    if (0 == thorApiDTO.getErrNum() && thorApiDTO.getRetData() != null) {
                        userInfoDTO = thorApiDTO.getRetData();
                        salerId = Integer.valueOf(userInfoDTO.getUser_id());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("OCDC推送信息中，自带业务员ID强转/获取信息失败。telephonenumber:" + customerDTO.getTelephonenumber());
                salerId = 0;
            }
        }
        //房速贷推送过来的带业务员手机号的客户

        if (null != extJson.get("owner_user_phone") &&
                StringUtils.isNotBlank(extJson.get("owner_user_phone").toString())) {
            String phone = extJson.get("owner_user_phone").toString();
            //获取业务员信息
            try {
                BaseUcDTO<UserInfoDTO> thorApiDTO = thorUcService.getUserInfoByField(
                        phone, token, null, null);
                if (0 == thorApiDTO.getErrNum() && thorApiDTO.getRetData() != null) {
                    userInfoDTO = thorApiDTO.getRetData();
                    salerId = Integer.valueOf(userInfoDTO.getUser_id());
                } else {
                    salerId = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("请求Thor接口getUserInfoByField失败。phone:" + phone);
            }
        }

        if (userInfoDTO.getStatus() != null && userInfoDTO.getStatus().equals("1")) {
            return userInfoDTO;
        } else return new UserInfoDTO();
    }

    private boolean isAllocateToPublic(String utmSource) {
        boolean allocateToPublic = false;
        //获取配置中不走自动分配的渠道
//            String allocateToNoUserPool = "";
        String allocateToNoUserPool = theaClientService.getConfigByName(CommonConst.ALLOCATE_TO_NO_USER_POOL);

        //判断该推送客户是否在限制渠道中/进公盘
        String[] utmSourceStrArray;
        if (StringUtils.isNotBlank(allocateToNoUserPool)) {
            utmSourceStrArray = allocateToNoUserPool.split(",");
            if (ArrayUtils.contains(utmSourceStrArray, utmSource)) {
                allocateToPublic = true;
            }
        }
        return allocateToPublic;
    }

    /**
     * 根据城市获取分配队列信息
     *
     * @param city
     */
    public Integer getAllocateUser(String city) {
        Integer ownUserId = 0;
        String userIdsStr = allocateRedisService.getAllocateTemplet(city);
        if (StringUtils.isBlank(userIdsStr)) {
            return 0;
        }
//        String[] userIdsArray = userIdsStr.split(",");
        //查询这个队列里面每个人这这个月的分配数（有限）
        List<Integer> ids = CommonUtil.initStrtoIntegerList(userIdsStr);
        Map<String, Object> userMonthMap = new HashMap<>();
        userMonthMap.put("userIds", ids);
//        userMonthMap.put("effectiveDate", TheaDateUtil.getyyyyMMForThisMonth());
        List<UserMonthInfo> userMonthInfoServiceList = userMonthInfoService.selectByParamsMap(userMonthMap);
        List<AllocateLog> allocateLogList = new ArrayList<>();
        if (null != userMonthInfoServiceList && userMonthInfoServiceList.size() > 0) {
            //先将所有的客户已分配数归0，原数据表中的数据清空
            List<Integer> newOwnerIds = new ArrayList<>();
            for (UserMonthInfo userMonthInfo : userMonthInfoServiceList) {
                userMonthInfo.setAssignedCustomerNum(0);
                newOwnerIds.add(userMonthInfo.getUserId());
            }
            //获取该分组业务员的已分配交易数

            Map<String, Object> allocateLogeMap = new HashMap<>();
            allocateLogeMap.put("newOwnerIds", newOwnerIds);
            allocateLogeMap.put("operationsStr", CommonEnum.LOAN_OPERATION_TYPE_0.getCodeDesc() + "," + CommonEnum.LOAN_OPERATION_TYPE_4.getCodeDesc());
            allocateLogeMap.put("createBeginDate", DateUtils.getStartTimeOfThisMonth());
            allocateLogeMap.put("createEndDate", DateUtils.getStartTimeOfNextMonth());
            allocateLogList = allocateLogService.selectByParamsMap(allocateLogeMap);
            //将查询结果重新封装为一个根据分配队列的ID结果的对象(将已分配的队列加+1)
            for (UserMonthInfo userMonthInfo : userMonthInfoServiceList) {
                for (AllocateLog allocateLog : allocateLogList) {
                    if (allocateLog.getNewOwnerId().equals(userMonthInfo.getUserId())) {
                        userMonthInfo.setAssignedCustomerNum(userMonthInfo.getAssignedCustomerNum() + 1);
                    }
                }
                //如果用户的已分配数>= 客户的基础分配数+奖励分配数 的输出用户ID
                if ((userMonthInfo.getBaseCustomerNum() + userMonthInfo.getRewardCustomerNum()) > userMonthInfo.getAssignedCustomerNum()) {
                    ownUserId = userMonthInfo.getUserId();
                    return ownUserId;
                }
            }
        }
        return ownUserId;
    }


    /**
     * 客户未沟通重新分配 定时任务 5min
     */
    public synchronized void nonCommunicateAgainAllocate(String token) {
        try {
            if (currentWorkDayAndTime(token)) {
                List<CustomerInfo> list = customerInfoService.selectNonCommunicateInTime().getData();

                List<Integer> existFailList = cronusRedisService.getRedisFailNonConmunicateAllocateInfo(CommonConst.FAIL_NON_COMMUNICATE_ALLOCATE_INFO);
                if (existFailList == null) {
                    existFailList = new ArrayList<>();
                }
                StringBuilder stringBuilder = new StringBuilder();
                if (existFailList != null) {
                    for (int i = 0; i < existFailList.size(); i++) {
                        stringBuilder.append(existFailList.get(i).toString());
                        if (i < existFailList.size() - 1) {
                            stringBuilder.append(",");
                        }
                    }
                }
                List<Integer> failList = new ArrayList<>();
                for (CustomerInfo customerInfo :
                        list) {
                    if (existFailList.contains(customerInfo.getId())) {
                        continue;
                    }
                    Integer ownUserId = 0;

                    try {
                        ownUserId = getAllocateUser(customerInfo.getCity());
                    } catch (Exception e) {

                    }
                    if (ownUserId > 0) {
                        SimpleUserInfoDTO simpleUserInfoDTO = thorUcService.getUserInfoById(token, ownUserId).getData();
                        if (simpleUserInfoDTO != null && null != simpleUserInfoDTO.getSub_company_id()) {
                            customerInfo.setSubCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                        } else {
                            customerInfo.setSubCompanyId(0);
                        }
                        customerInfo.setReceiveTime(new Date());
                        customerInfo.setLastUpdateTime(new Date());
                        customerInfo.setConfirm(1);
                        customerInfo.setClickCommunicateButton(0);
                        customerInfo.setCommunicateTime(null);
                        customerInfoService.editCustomerSys(customerInfo, token);

                        allocateRedisService.changeAllocateTemplet(customerInfo.getOwnUserId(), customerInfo.getCity());
                        //添加分配日志
                        allocateLogService.addAllocatelog(customerInfo, customerInfo.getOwnUserId(),
                                CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCode(), null);
                        sendMessage(customerInfo.getCustomerName(), ownUserId, simpleUserInfoDTO, token);

                    } else {
                        //分配名额已经满了,向这个城市的crm助理发送短信
                        sendCRMAssistantMessage(customerInfo.getCity(),customerInfo.getCustomerName(), token);
                        if (!failList.contains(customerInfo.getId()))
                            failList.add(customerInfo.getId());
                    }
                }
                //failList 添加到缓存
                existFailList.addAll(failList);
                cronusRedisService.setRedisFailNonConmunicateAllocateInfo(CommonConst.FAIL_NON_COMMUNICATE_ALLOCATE_INFO, failList);

            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    private boolean currentWorkDayAndTime(String token) {
        Date date = new Date();
        Integer hour = DateUtils.getHour(new Date());
        if (10 < hour && hour < 18) {
            return true;
        }
        String month = DateUtils.getYear(date).toString() + "-" + DateUtils.getMonth(date).toString();

        String workDays = "";
        List<WorkDayDTO> workDayDTOList = theaClientService.getWorkDay(token);
        for (WorkDayDTO workday :
                workDayDTOList) {
            if (workday.getMonth().equals(month)) {
                workDays = workday.getWorkdays();
                break;
            }
        }
        if (workDays.contains(DateUtils.getDay(date).toString())) ;
        return true;
    }
}
