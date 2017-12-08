package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;

import com.fjs.cronus.api.thea.ConfigDTO;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
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
import com.fjs.cronus.util.CommonUtil;
import com.fjs.cronus.util.DateUtils;
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
    private String publicToken;

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
    private TheaService theaService;

//    @Autowired
//    private LoanLogService loanLogService;

    @Autowired
    private AgainAllocateCustomerService againAllocateCustomerService;

//    @Autowired
//    private SmsService smsService;

    /**
     * 判断是不是客户主动申请渠道
     *
     * @param customerInfo
     * @return
     */
    public Boolean isActiveApplicationChannel(CustomerInfo customerInfo) {
        TheaApiDTO<ConfigDTO> theaApiDTO = theaService.getConfigByName("activeApplicationChannel");
        ConfigDTO configDTO = theaApiDTO.getData();
        String activeApplicationChannel = configDTO.getValue();
        if (activeApplicationChannel.contains(customerInfo.getUtmSource()))
            return true;
        else
            return false;
    }

    @Transactional
    public AllocateEntity autoAllocate(CustomerDTO customerDTO,AllocateSource allocateSource) {
        AllocateEntity allocateEntity = new AllocateEntity();
        try {
            //获取自动分配的城市（主要城市+异地城市）
            TheaApiDTO<ConfigDTO> theaApiDTO = theaService.getConfigByName(CommonConst.MAIN_CITY);
            ConfigDTO configDTO = theaApiDTO.getData();
            String mainCityStr = configDTO.getValue();

            theaApiDTO = theaService.getConfigByName(CommonConst.REMOTE_CITY);
            configDTO = theaApiDTO.getData();
            String remoteCityStr = configDTO.getValue();
            String mainAndRemoteCityStr = "";
            if (StringUtils.isNotBlank(mainCityStr) && StringUtils.isNotBlank(remoteCityStr)) {
                mainAndRemoteCityStr = mainCityStr + "," + remoteCityStr;
            } else if (StringUtils.isNotBlank(mainCityStr) && StringUtils.isBlank(remoteCityStr)) {
                mainAndRemoteCityStr = mainCityStr;
            } else if (StringUtils.isBlank(mainCityStr) && StringUtils.isNotBlank(remoteCityStr)) {
                mainAndRemoteCityStr = remoteCityStr;
            } else {
                mainAndRemoteCityStr = "";
            }
            //获取配置中不走自动分配的渠道
//            String allocateToNoUserPool = "";
            String allocateToNoUserPool = theaService.getConfigByName(CommonConst.ALLOCATE_TO_NO_USER_POOL).getData().getValue();
            //分析交易扩展信息
            JSONObject extJson = new JSONObject();
            if (StringUtils.isNotBlank(customerDTO.getExt())) {
                extJson = JSON.parseObject(customerDTO.getExt());
            }

            //判断是否有默认的业务员(业务员的电话号码)
            Integer salerId = 0;
            SimpleUserInfoDTO simpleUserInfoDTO = new SimpleUserInfoDTO();
            if ( null != extJson.get("sale_id") &&
                    StringUtils.isNotBlank(extJson.get("sale_id").toString())) {
                //如果sale_id的格式出现异常，强转失败，则继续走下一步的逻辑
                try {
                    salerId = Integer.valueOf(extJson.get("sale_id").toString());
                    if (0 != salerId) {
                        simpleUserInfoDTO = thorUcService.getUserInfoById(publicToken, salerId).getData();
                        if (null != simpleUserInfoDTO) {
                            salerId = Integer.valueOf(simpleUserInfoDTO.getUser_id());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("OCDC推送信息中，自带业务员ID强转/获取信息失败。telephonenumber:" + customerDTO.getTelephonenumber());
                    salerId = 0;
                }
            }
            //房速贷推送过来的带业务员手机号的客户
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            if ( null != extJson.get("owner_user_phone") &&
                    StringUtils.isNotBlank(extJson.get("owner_user_phone").toString())) {
                String phone = extJson.get("owner_user_phone").toString();
                //获取业务员信息
                try {
                    BaseUcDTO<UserInfoDTO> thorApiDTO = thorUcService.getUserInfoByField(
                            phone, publicToken, null, null);
                    if (0 == thorApiDTO.getErrNum()) {
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

            //判断该推送客户是否在限制渠道中/进公盘
            boolean allocateToPublic = false;
            String[] utmSourceStrArray;
            if (StringUtils.isNotBlank(allocateToNoUserPool)) {
                utmSourceStrArray = allocateToNoUserPool.split(",");
                if (ArrayUtils.contains(utmSourceStrArray, customerDTO.getUtmSource())) {
                    allocateToPublic = true;
                }
            }

            //是否占用自动分配  0:直接到公盘  1:自动分配  2:带业务员
            Integer autoStatus = 0;
            if (allocateToPublic) {
                customerDTO.setOwnerUserId(0);

            } else if (0 != salerId && "1".equals(userInfoDTO.getStatus())) {//存在这个在职负责人
                customerDTO.setOwnerUserId(Integer.valueOf(userInfoDTO.getUser_id()));
                autoStatus = 2;
            } else if (CommonConst.CUSTOMER_SOURCE_FANGSUDAI.equals(customerDTO.getUtmSource())
                    && CommonConst.UTM_SOURCE_FANGXIN.equals(customerDTO.getUtmSource())) {//房速贷，渠道fangxin直接到公盘
                customerDTO.setOwnerUserId(0);
            } else if (StringUtils.contains(mainAndRemoteCityStr, customerDTO.getCity())) {
                customerDTO.setOwnerUserId(this.getAllocateUser(customerDTO.getCity()));
                autoStatus = 1;
            } else {
                customerDTO.setOwnerUserId(0);
                switch (allocateSource.getCode())
                {
                    case "0":
                    case "2":
                        //推入客服系统

                }
            }
            /*如果数据中存在id说明是己存在表中的记录；过来的数据id一定为0或者null，非0的是走不了分配的*/

            Integer loanId = 0;
            if (null != customerDTO.getId() && customerDTO.getId() > 0) {
                loanId = customerDTO.getId();
                if (null != customerDTO.getOwnerUserId() && customerDTO.getOwnerUserId() > 0) {
                    simpleUserInfoDTO = thorUcService.getUserInfoById(publicToken, customerDTO.getOwnerUserId()).getData();
                    if (null != simpleUserInfoDTO.getSub_company_id()) {
                        customerDTO.setOwnerUserId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                    } else {
                        customerDTO.setOwnerUserId(0);
                    }
                    //customerDTO.se(new Date()); //todo customerDTO 无领取时间 `receive_time`
                    customerDTO.setLastUpdateTime(new Date());
                    if (1 == autoStatus) { //是自动分配的
                        //重复申请,无论有效无效,直接变成未沟通
//                        customerDTO.setst(CommonEnum.LOAN_STATUE_1.getCode());
//                        customerDTO.setClickCommunicateButton(CommonEnum.NO.getCode());
//                        customerDTO.setCommunicateTime(null);
                    }
//                    theaService.saveOne(loan);
                    customerInfoService.editCustomerOk(customerDTO,publicToken);
                }
            } else {
                //新生产的数据，如果手机号不存在数据表中，则添加(己包含业务员)
//                Map<String, Object> map = new HashMap<>();
//                map.put("eqPhone", customerDTO.getTelephonenumber());
                CronusDto<CustomerDTO> cronusDto = customerInfoService.fingByphone(customerDTO.getTelephonenumber());
                CustomerDTO customerDTO1 = cronusDto.getData();
                if (null != customerDTO1) {
                    if (autoStatus == 1 && customerDTO.getOwnerUserId() == 0) {
                        //下面进入再分配池子
                        //againAllocateCustomerService.addAgainAllocateCustomer(c);
                        allocateEntity.setSuccess(true);
                        allocateEntity.setAllocateStatus(AllocateEnum.CRM_FILE_DATA_SUCCESS);
                    } else {
                    /*可自动分配到业务员*/
                        customerDTO.setLastUpdateTime(new Date());
                        //先注销之后重新获取
                        simpleUserInfoDTO = thorUcService.getUserInfoById(publicToken, customerDTO.getOwnerUserId()).getData();
                        if (null != simpleUserInfoDTO.getSub_company_id()) {
                            customerDTO.setSubCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                        } else {
                            customerDTO.setSubCompanyId(0);
                        }
                        //保存数据
                        customerInfoService.addCustomer(customerDTO, publicToken);
                    }
                }
            }
            if (null != loanId && 0 != loanId) {
                //自动分配成功
                AllocateLog allocateLog = new AllocateLog();
                if (autoStatus == 1) {
                    //更新城市队列
                    String[] cityStrArrayAll = StringUtils.split(",");
                    if (ArrayUtils.contains(cityStrArrayAll, customerDTO.getCity())) {
                        allocateRedisService.changeAllocateTemplet(customerDTO.getOwnerUserId(), customerDTO.getCity());
                    }
                    //如果是再分配盘的数据则标记再分配成功
                    Map<String, Object> againAllocateMap = new HashMap<>();
                    againAllocateMap.put("dataId", loanId);
                    againAllocateMap.put("status", CommonEnum.AGAIN_ALLOCATE_STATUS_1.getCodeDesc());
                    againAllocateCustomerService.saveStatusByDataId(againAllocateMap);
                    //添加分配日志
//                    allocateLogService.addAllocatelog(customerDTO, customerDTO.getOwnerUserId(),//todo
//                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCode(), null);
                } else if (autoStatus == 2) {//自动分配(带业务员)
                    //添加分配日志
//                    allocateLogService.addAllocatelog(customerDTO, customerDTO.getOwnerUserId(),//todo
//                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCode(), null);
                } else {
//                    //添加日志
//                    customeri loanLog = new LoanLog();
//                    //初始化日志对象
//                    loanLog = CommonInitObject.initLoanLogByLoan(loan);
//                    loanLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_5.getCodeDesc());
//                    loanLog.setLogCreateTime(new Date());
//                    loanLog.setLogUserId(CommonEnum.NO.getCode());
//                    loanLogService.insertOne(loanLog);
                }
                if (null != customerDTO.getOwnerUserId() && 0 != customerDTO.getOwnerUserId()) {//分配成功，给业务员通知提醒
                    //添加消息记录
//                    String smsResult = smsService.sendSmsForAutoAllocate(simpleUserInfoDTO.getTelephone(), loan.getCustomerName(), loan.getTelephonenumber());
                }
                allocateEntity.setSuccess(true);
            } else {
                allocateEntity.setSuccess(false);
            }
        } catch (Exception e) {
//            logger.error("-------------------自动分配失败:ocdcDataId=" + customerDTO.getOcdcDataId() + "-------------------", e);
            allocateEntity.setSuccess(false);
        }
        return allocateEntity;
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
