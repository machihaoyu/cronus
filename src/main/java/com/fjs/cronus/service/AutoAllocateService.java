package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;

import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.api.thea.MailDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.CrmUserDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.entity.AllocateEntity;
import com.fjs.cronus.enums.AllocateEnum;
import com.fjs.cronus.enums.AllocateSource;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.client.ThorInterfaceService;
import com.fjs.cronus.service.redis.AllocateRedisService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.util.CommonUtil;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//    @Autowired
//    private SmsService smsService;

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
    public AllocateEntity autoAllocate(CustomerDTO customerDTO, AllocateSource allocateSource, String token) {
        AllocateEntity allocateEntity = new AllocateEntity();
        allocateEntity.setSuccess(true);
        try {
            //获取自动分配的城市
            String allocateCities = theaClientService.getConfigByName(CommonConst.CAN_ALLOCATE_CITY);

            UserInfoDTO ownerUser = getOwnerUser(customerDTO, token); //获取负责人

            boolean allocateToPublic = isAllocateToPublic(customerDTO.getUtmSource());

            //是否占用自动分配  0:直接到公盘  1:自动分配  2:带业务员
//            Integer autoStatus = 0;
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
            /*如果数据中存在id说明是己存在表中的记录；过来的数据id一定为0或者null，非0的是走不了分配的*/
            SimpleUserInfoDTO simpleUserInfoDTO;
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
                customerInfoService.editCustomerSys(customerDTO, token);
            } else {
                CronusDto<CustomerDTO> cronusDto = customerInfoService.fingByphone(customerDTO.getTelephonenumber());
                CustomerDTO hasCustomer = cronusDto.getData();
                if (null == hasCustomer || null == hasCustomer.getId()) {
                    switch (allocateEntity.getAllocateStatus().getCode()) {
                        case "0":
                        case "1":
                        case "3":
                            //先注销之后重新获取
                            addCustomer(customerDTO, token);
                            break;
                    }
                }
            }


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
                    activeChannelAddTansaction(customerDTO);
                    sendMessage(customerDTO, token);
                    break;
                case "2": //WAITING_POOL
                    sendCRMAssistantMessage(customerDTO,token);
                    break;
                case "3":
                    //添加分配日志
                    CustomerInfo customerInfot = new CustomerInfo();
                    EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfot);
                    allocateLogService.addAllocatelog(customerInfot, customerDTO.getOwnerUserId(),
                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCode(), null);
                    activeChannelAddTansaction(customerDTO);
                    sendMessage(customerDTO, token);
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
    private void activeChannelAddTansaction(CustomerDTO customerDTO) {
        if (isActiveApplicationChannel(customerDTO)) {
            LoanDTO loanDTO = new LoanDTO();
            loanDTO.setTelephonenumber(customerDTO.getTelephonenumber());
            loanDTO.setLoanAmount(customerDTO.getLoanAmount());
            loanDTO.setCustomerId(customerDTO.getId());
            loanDTO.setCustomerName(customerDTO.getCustomerName());
            theaClientService.inserLoan(loanDTO);
        }
    }

    private void sendMessage(CustomerDTO customerDTO, String token) {
        theaClientService.sendMail(token,
                "房金所为您分配了客户名：" + customerDTO.getCustomerName() + "，请注意跟进。",
                0,
                0,
                "系统管理员",
                customerDTO.getOwnerUserId());
    }

    private void sendCRMAssistantMessage(CustomerDTO customerDTO, String token) {

        BaseUcDTO<List<CrmUserDTO>> crmUser = thorInterfaceService.getCRMUser(token, customerDTO.getCity());
        List<CrmUserDTO> crmUserDTOList = crmUser.getRetData();
        for (CrmUserDTO crmUserDTO:
             crmUserDTOList) {
            //todo 发送短信
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
}
