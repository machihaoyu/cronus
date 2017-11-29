package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.api.PhpApiDto;
import com.fjs.cronus.api.thea.ConfigDTO;
import com.fjs.cronus.api.thea.Loan;
import com.fjs.cronus.dto.SimpleUserInfoDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.client.ThorInterfaceService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

//    @Autowired
//    private AllocateRedisService allocateRedisService;

//    @Autowired
//    private UserMonthInfoService userMonthInfoService;

//    @Autowired
//    private AllocateLogService allocateLogService;

    @Autowired
    private TheaService theaService;

//    @Autowired
//    private LoanLogService loanLogService;

//    @Autowired
//    private AgainAllocateCustomerService againAllocateCustomerService;

//    @Autowired
//    private SmsService smsService;

    @Transactional
    public Boolean autoAllocate(Loan loan) {
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
            String allocateToNoUserPool = "";
//            String allocateToNoUserPool = configRedisService.getConfigValue(CommonConst.ALLOCATE_TO_NO_USER_POOL);
            //分析交易扩展信息
            JSONObject extJson = new JSONObject();
            if (StringUtils.isNotBlank(loan.getExt())) {
                extJson = JSON.parseObject(loan.getExt());
            }
            //判断该推送客户是否在限制渠道中
            boolean allocateFlage = false;
            String[] utmSourceStrArray;
            if (StringUtils.isNotBlank(allocateToNoUserPool)) {
                utmSourceStrArray = allocateToNoUserPool.split(",");
                if (ArrayUtils.contains(utmSourceStrArray, loan.getUtmSource())) {
                    allocateFlage = true;
                }
            }
            //判断交易是否有默认的业务员(业务员的电话号码)
            Integer salerId = 0;
            SimpleUserInfoDTO simpleUserInfoDTO = new SimpleUserInfoDTO();
            if (!allocateFlage && null != extJson.get("sale_id") &&
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
                    logger.error("OCDC推送信息中，自带业务员ID强转/获取信息失败。telephonenumber:" + loan.getTelephonenumber());
                    salerId = 0;
                }
            }
            //房速贷推送过来的带业务员手机号的客户
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            if (!allocateFlage && null != extJson.get("owner_user_phone") &&
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
            //是否占用自动分配  0:直接到公盘  1:自动分配  2:带业务员
            Integer autoStatus = 0;
            if (allocateFlage) {
                loan.setOwnUserId(0);
            } else if (0 != salerId && "1".equals(userInfoDTO.getStatus())) {//存在这个在职负责人
                loan.setOwnUserId(Integer.valueOf(userInfoDTO.getUser_id()));
                autoStatus = 2;
            } else if (CommonConst.CUSTOMER_SOURCE_FANGSUDAI.equals(loan.getCustomerSource())
                    && CommonConst.UTM_SOURCE_FANGXIN.equals(loan.getUtmSource())) {//房速贷，渠道fangxin直接到公盘
                loan.setOwnUserId(0);
            } else if (StringUtils.contains(mainAndRemoteCityStr, loan.getCity())) {
                loan.setOwnUserId(this.getAllocateUser(loan.getCity()));
                autoStatus = 1;
            } else {
                loan.setOwnUserId(0);
            }
        /*如果数据中存在id说明是己存在表中的记录；过来的数据id一定为0或者null，非0的是走不了分配的*/

            Integer loanId = 0;
            if (null != loan.getId() && loan.getId() > 0) {
                loanId = loan.getId();
                if (null != loan.getOwnUserId() && loan.getOwnUserId() > 0) {
                    simpleUserInfoDTO = thorUcService.getUserInfoById(publicToken, loan.getOwnUserId()).getData();
                    if (null != simpleUserInfoDTO.getSub_company_id()) {
                        loan.setCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                    } else {
                        loan.setCompanyId(0);
                    }
                    loan.setReceiveTime(new Date());
                    loan.setLastUpdateTime(new Date());
                    if (1 == autoStatus) { //是自动分配的
                        //重复申请,无论有效无效,直接变成未沟通
                        loan.setStatus(CommonEnum.LOAN_STATUE_1.getCode());
                        loan.setClickCommunicateButton(CommonEnum.NO.getCode());
                        loan.setCommunicateTime(null);
                    }
//                    theaService.saveOne(loan);
                }
            } else {
                //新生产的数据，如果手机号不存在数据表中，则添加(己包含业务员)
//                Map<String, Object> map = new HashMap<>();
//                map.put("eqPhone", loan.getTelephonenumber());
//                List<Loan> loanList = loanService.selectByParams(map);
//                if (null != loanList && loanList.size() == 0) {
//                    if (autoStatus == 1 && loan.getOwnUserId() == 0) {
//                        //下面进入再分配池子
//
//                        return null;
//                    } else {
//                    /*可自动分配到业务员*/
//                        loan.setLastUpdateTime(new Date());
//                        //先注销之后重新获取
//                        simpleUserInfoDTO = thorUcService.getUserInfoById(publicToken, loan.getOwnUserId()).getData();
//                        if (null != simpleUserInfoDTO.getSub_company_id()) {
//                            loan.setCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
//                        } else {
//                            loan.setCompanyId(0);
//                        }
//                        //保存数据
////                        loanId = loanService.insertOne(loan);
//                    }
//                }
            }
            if (null != loanId && 0 != loanId) {
                //自动分配成功
                AllocateLog allocateLog = new AllocateLog();
                if (autoStatus == 1) {
                    //更新城市队列
//                    String[] cityStrArrayAll = StringUtils.split(",");
//                    if (ArrayUtils.contains(cityStrArrayAll, loan.getCity())) {
//                        allocateRedisService.changeAllocateTemplet(loan.getOwnUserId(), loan.getCity());
//                    }
//                    //如果是再分配盘的数据则标记再分配成功
//                    Map<String, Object> againAllocateMap = new HashMap<>();
//                    againAllocateMap.put("dataId", loanId);
//                    againAllocateMap.put("status", CommonEnum.AGAIN_ALLOCATE_STATUS_1.getCodeDesc());
//                    againAllocateCustomerService.saveStatusByDataId(againAllocateMap);
//                    //添加分配日志
//                    allocateLogService.addAllocatelog(loan, loan.getOwnUserId(),
//                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCode(), null);
                } else if (autoStatus == 2) {//自动分配(带业务员)
                    //添加分配日志
//                    allocateLogService.addAllocatelog(loan, loan.getOwnUserId(),
//                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCode(), null);
                } else {
                    //添加日志
//                    LoanLog loanLog = new LoanLog();
//                    //初始化日志对象
//                    loanLog = CommonInitObject.initLoanLogByLoan(loan);
//                    loanLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_5.getCodeDesc());
//                    loanLog.setLogCreateTime(new Date());
//                    loanLog.setLogUserId(CommonEnum.NO.getCode());
//                    loanLogService.insertOne(loanLog);
                }
                if (null != loan.getOwnUserId() && 0 != loan.getOwnUserId()) {//分配成功，给业务员通知提醒
                    //添加消息记录
//                    String smsResult = smsService.sendSmsForAutoAllocate(simpleUserInfoDTO.getTelephone(), loan.getCustomerName(), loan.getTelephonenumber());
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("-------------------自动分配失败:ocdcDataId="+loan.getOcdcDataId()+"-------------------",e);
            return false;
        }
    }


    /**
     * 根据城市获取分配队列信息
     *
     * @param city
     */
    public Integer getAllocateUser(String city) {
        Integer ownUserId = 0;
//        String userIdsStr = allocateRedisService.getAllocateTemplet(city);
//        if (StringUtils.isBlank(userIdsStr)) {
//            return 0;
//        }
////        String[] userIdsArray = userIdsStr.split(",");
//        //查询这个队列里面每个人这这个月的分配数（有限）
//        List<Integer> ids = CommonUtil.initStrtoIntegerList(userIdsStr);
//        Map<String, Object> userMonthMap = new HashMap<>();
//        userMonthMap.put("userIds", ids);
//        userMonthMap.put("effectiveDate", TheaDateUtil.getyyyyMMForThisMonth());
//        List<UserMonthInfo> userMonthInfoServiceList = userMonthInfoService.selectByParamsMap(userMonthMap);
//        List<AllocateLog> allocateLogList = new ArrayList<>();
//        if (null != userMonthInfoServiceList && userMonthInfoServiceList.size() > 0) {
//            //先将所有的客户已分配数归0，原数据表中的数据清空
//            List<Integer> newOwnerIds = new ArrayList<>();
//            for (UserMonthInfo userMonthInfo : userMonthInfoServiceList) {
//                userMonthInfo.setAssignedCustomerNum(0);
//                newOwnerIds.add(userMonthInfo.getUserId());
//            }
//            //获取该分组业务员的已分配交易数
//
//            Map<String, Object> allocateLogeMap = new HashMap<>();
//            allocateLogeMap.put("newOwnerIds", newOwnerIds);
//            allocateLogeMap.put("operationsStr", CommonEnum.LOAN_OPERATION_TYPE_0.getCodeDesc() +","+ CommonEnum.LOAN_OPERATION_TYPE_4.getCodeDesc());
//            allocateLogeMap.put("createBeginDate", TheaDateUtil.getStartTimeOfThisMonth());
//            allocateLogeMap.put("createEndDate", TheaDateUtil.getStartTimeOfNextMonth());
//            allocateLogList = allocateLogService.selectByParamsMap(allocateLogeMap);
//            //将查询结果重新封装为一个根据分配队列的ID结果的对象(将已分配的队列加+1)
//            for (UserMonthInfo userMonthInfo : userMonthInfoServiceList) {
//                for (AllocateLog allocateLog : allocateLogList) {
//                    if (allocateLog.getNewOwnerId().equals(userMonthInfo.getUserId())) {
//                        userMonthInfo.setAssignedCustomerNum(userMonthInfo.getAssignedCustomerNum() + 1);
//                    }
//                }
//                //如果用户的已分配数>= 客户的基础分配数+奖励分配数 的输出用户ID
//                if ((userMonthInfo.getBaseCustomerNum() + userMonthInfo.getRewardCustomerNum()) > userMonthInfo.getAssignedCustomerNum()) {
//                    ownUserId = userMonthInfo.getUserId();
//                    return ownUserId;
//                }
//            }
//        }
        return ownUserId;
    }
}
