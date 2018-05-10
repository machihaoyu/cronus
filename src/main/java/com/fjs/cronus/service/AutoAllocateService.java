package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;

import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.avatar.AvatarApiDTO;
import com.fjs.cronus.dto.avatar.FirstBarDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.BaseChannelDTO;
import com.fjs.cronus.dto.thea.WorkDayDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.CrmUserDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.entity.AllocateEntity;
import com.fjs.cronus.enums.AllocateEnum;
import com.fjs.cronus.enums.AllocateSource;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.client.AvatarClientService;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.redis.AllocateRedisService;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.thea.ThorClientService;
import com.fjs.cronus.util.CommonUtil;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import static java.util.stream.Collectors.*;

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
    private ThorService thorUcService;

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
    ThorService thorService;

    @Autowired
    private AgainAllocateCustomerService againAllocateCustomerService;


    @Autowired
    private CronusRedisService cronusRedisService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private ThorClientService thorClientService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TheaService theaService;

    @Autowired
    private AvatarClientService avatarClientService;

    /**
     * 判断是不是客户主动申请渠道
     *
     * @param customerDTO
     * @return
     */
    private Boolean isActiveApplicationChannel(CustomerDTO customerDTO) {
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
            // 获取自动分配的城市
            String allocateCities = theaClientService.getConfigByName(CommonConst.CAN_ALLOCATE_CITY);

            UserInfoDTO ownerUser = this.getOwnerUser(customerDTO, token); // 获取负责人

            boolean allocateToPublic = this.isAllocateToPublic(customerDTO.getUtmSource()); // 根据渠道，判断是否需要自动分配

            Integer subCompanyIdBox = null; // 一级吧id
            Integer salesmanIdBox = null;   // 业务员id
            BaseChannelDTO baseChannelDTO = null; // 来源、媒体、渠道

            // 分配规则
            if ( ( customerDTO.getId() == null || customerDTO.getId().equals(0) ) && StringUtils.contains(allocateCities, customerDTO.getCity())) {
                // 商机系统分支
                // 规则：1、新用户；2、在有效城市范围内

                baseChannelDTO = this.getChannelInfoByChannelName(customerDTO.getUtmSource()); // 根据渠道获取来源、媒体
                if (this.allocateForAvatar(token, allocateSource, customerDTO, baseChannelDTO, subCompanyIdBox, salesmanIdBox)) {
                    allocateEntity.setAllocateStatus(AllocateEnum.ALLOCATE_TO_OWNER);
                    customerDTO.setOwnerUserId(salesmanIdBox);
                } else {
                    allocateEntity.setAllocateStatus(AllocateEnum.WAITING_POOL);
                }
            }if (StringUtils.isNotEmpty(ownerUser.getUser_id())) { // 存在这个在职负责人
                customerDTO.setOwnerUserId(Integer.valueOf(ownerUser.getUser_id()));
                allocateEntity.setAllocateStatus(AllocateEnum.EXIST_OWNER);
            } else if (allocateToPublic) { // 公盘;根据渠道，判断是否需要自动分配
                allocateEntity.setAllocateStatus(AllocateEnum.PUBLIC);
                customerDTO.setOwnerUserId(0);
            } else if (CommonConst.CUSTOMER_SOURCE_FANGSUDAI.equals(customerDTO.getUtmSource())
                    && CommonConst.UTM_SOURCE_FANGXIN.equals(customerDTO.getUtmSource())) { // 公盘;房速贷、渠道fangxin 直接到公盘
                customerDTO.setOwnerUserId(0);
                allocateEntity.setAllocateStatus(AllocateEnum.PUBLIC);
            } else if (StringUtils.isNotEmpty(customerDTO.getCity()) && StringUtils.contains(allocateCities, customerDTO.getCity())) { // 在有效分配城市内
                Integer ownUserId = this.getAllocateUser(customerDTO.getCity());
                if (ownUserId > 0) { // 自动分配队列
                    customerDTO.setOwnerUserId(ownUserId);
                    allocateEntity.setAllocateStatus(AllocateEnum.ALLOCATE_TO_OWNER);
                } else { // 进入待分配池
                    customerDTO.setOwnerUserId(0);
                    allocateEntity.setAllocateStatus(AllocateEnum.WAITING_POOL);
                }
            } else {
                customerDTO.setOwnerUserId(0);
                switch (allocateSource.getCode()) {
                    case "1": // 客服
                        allocateEntity.setAllocateStatus(AllocateEnum.PUBLIC);
                        break;
                    case "0": // OCDC
                    case "2": // 待分配池，推入客服系统
                        allocateEntity.setAllocateStatus(AllocateEnum.TO_SERVICE_SYSTEM);
                        break;
                }
            }

            // 保存客户
            SimpleUserInfoDTO simpleUserInfoDTO = null;
            Integer customerId = 0;
            if (null != customerDTO.getId() && customerDTO.getId() > 0) { // 老客户
                // 更新客户信息
                customerId = customerDTO.getId();
                if (null != customerDTO.getOwnerUserId() && customerDTO.getOwnerUserId() > 0) {
                    simpleUserInfoDTO = thorClientService.getUserInfoById(token, customerDTO.getOwnerUserId());
                    if (null != simpleUserInfoDTO.getSub_company_id()) {
                        customerDTO.setSubCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                    } else {
                        customerDTO.setSubCompanyId(0);
                    }
                    customerDTO.setOwnUserName(simpleUserInfoDTO.getName());
                    customerDTO.setReceiveTime(new Date());
                    customerDTO.setLastUpdateTime(new Date());
                    CustomerInfo customerInfo = new CustomerInfo();
                    EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
                    if (allocateEntity.getAllocateStatus().getCode().equals("1")) {
                        customerInfo.setConfirm(1);
                        customerInfo.setClickCommunicateButton(0);
                        customerInfo.setCommunicateTime(null);
                    }
                    customerInfoService.editCustomerSys(customerInfo, token);
                }
            } else { // 新客户
                CronusDto<CustomerDTO> cronusDto = customerInfoService.fingByphone(customerDTO.getTelephonenumber());
                CustomerDTO hasCustomer = cronusDto.getData();
                if (null == hasCustomer || null == hasCustomer.getId()) {
                    switch (allocateEntity.getAllocateStatus().getCode()) {
                        case "0": // 公盘
                        case "1": // 自动分配队列
                        case "3": // 已存在负责人
                            if (customerDTO.getOwnerUserId() != null && customerDTO.getOwnerUserId() > 0) { // 已存在负责人
                                simpleUserInfoDTO = thorClientService.getUserInfoById(token, customerDTO.getOwnerUserId());
                                if (null != simpleUserInfoDTO.getSub_company_id()) {
                                    customerDTO.setSubCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                                } else {
                                    customerDTO.setSubCompanyId(0);
                                }
                                if (null != simpleUserInfoDTO.getName())
                                    customerDTO.setOwnUserName(simpleUserInfoDTO.getName());
                            }

                            // 处理是否保留
                            customerDTO.setLastUpdateTime(new Date());
                            customerDTO.setRemain(CommonConst.REMAIN_STATUS_NO);
                            if (allocateEntity.getAllocateStatus().getCode().equals("3")||allocateEntity.getAllocateStatus().getCode().equals("1")){
                                if (this.isActiveApplicationChannel(customerDTO)) {
                                    customerDTO.setRemain(CommonConst.REMAIN_STATUS_YES);
                                }
                            }

                            // 新建用户信息
                            CronusDto cronusDto1 = customerInfoService.addOcdcCustomer(customerDTO, token);
                            if (cronusDto1.getResult() == 0) {
                                customerDTO.setId(Integer.parseInt(cronusDto1.getData().toString()));
                            }
                            break;
                        case "2": // 再分配池
                            break;
                    }
                }
            }

            // 分配日志&更新分配队列
            switch (allocateEntity.getAllocateStatus().getCode()) {
                case "0": // 公盘
                    break;
                case "1": // 自动分配队列
//                    String[] cityStrArrayAll = StringUtils.split(allocateCities, ",");
//                    if (ArrayUtils.contains(cityStrArrayAll, customerDTO.getCity())) {
                    if (allocateCities.contains(customerDTO.getCity())) {
                        allocateRedisService.changeAllocateTemplet(customerDTO.getOwnerUserId(), customerDTO.getCity());
                    }
                    // 添加分配日志
                    CustomerInfo customerInfo = new CustomerInfo();
                    EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
                    allocateLogService.autoAllocateAddAllocatelog(customerInfo.getId(), customerDTO.getOwnerUserId(),
                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCode(), subCompanyIdBox, baseChannelDTO.getMedia_id());

                    if (this.isActiveApplicationChannel(customerDTO)) {
                        String loan = this.addLoan(customerDTO, token);
                        allocateEntity.setDescription(loan);
                    }
                    this.sendMessage(customerDTO.getCustomerName(), customerDTO.getOwnerUserId(), simpleUserInfoDTO, token);
                    break;
                case "2": // 进入待分配池
                    this.sendCRMAssistantMessage(customerDTO.getCity(), customerDTO.getCustomerName(), token);
                    break;
                case "3": // 已存在负责人
                    // 添加分配日志
                    CustomerInfo customerInfot = new CustomerInfo();
                    customerInfot.setCreateUser(0);
                    customerInfot.setLastUpdateUser(0);
                    EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfot);
                    allocateLogService.addAllocatelog(customerInfot, customerDTO.getOwnerUserId(),
                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCode(), null);
                    if (this.isActiveApplicationChannel(customerDTO)) {
                        // 在有效渠道内、有负责人，直接创建交易
                        String loan = this.addLoan(customerDTO, token);
                        allocateEntity.setDescription(loan);
                    }
                    this.sendMessage(customerDTO.getCustomerName(), customerDTO.getOwnerUserId(), simpleUserInfoDTO, token);
                    break;
                case "4": // 推入客服系统
                    break;
            }

        } catch (Exception e) {
            logger.error("-------------------自动分配失败:ocdcDataId=" + customerDTO.getTelephonenumber() + "-------------------", e);
            allocateEntity.setDescription("自动分配失败");
            allocateEntity.setSuccess(false);
        }
        return allocateEntity;
    }

    /**
     * 商机系统分配规则.
     */
    private Boolean allocateForAvatar(String token, AllocateSource allocateSource, CustomerDTO customerDTO, BaseChannelDTO baseChannelDTO, Integer subCompanyIdBox, Integer salesmanIdBox) {

        // 找一级吧（队列获取）
        Integer subCompanyId = this.allocateRedisService.getSubCompanyIdFromQueue(token, customerDTO.getCity());
        if (subCompanyId == null) return false; // 进入待分配池
        subCompanyId = this.getSubCompanyIdFromQueue(token, customerDTO.getCity(), true, subCompanyId, null);
        if (subCompanyId == null) return false; // 进入待分配池（缓存被意外动过导致为null情况）

        // 根据媒体找业务员（队列获取）
        Integer source_id = baseChannelDTO.getSource_id();
        Integer media_id = baseChannelDTO.getMedia_id();
        String currentMonthStr = this.allocateRedisService.getCurrentMonthStr();
        Integer salesmanId = this.allocateRedisService.getAndPush2End(subCompanyId, media_id, currentMonthStr);
        if (salesmanId == null){
            // 具体媒体queue无时，去总分配queue找
            salesmanId = this.allocateRedisService.getAndPush2End(subCompanyId, CommonConst.COMPANY_MEDIA_QUEUE_COUNT, currentMonthStr);
            media_id = CommonConst.COMPANY_MEDIA_QUEUE_COUNT;
            baseChannelDTO.setMedia_id(CommonConst.COMPANY_MEDIA_QUEUE_COUNT);
            if (salesmanId == null) return false; // 进入待分配池
        }
        salesmanId = this.getSalesmanId(subCompanyId, media_id, currentMonthStr, true, salesmanId, null);
        if (salesmanId == null) return false; // 进入待分配池（缓存被意外动过导致为null情况）

        subCompanyIdBox = subCompanyId; // 记录分给的一级吧
        salesmanIdBox = salesmanId;     // 记录分给的业务员
        return true; // 符合商机系统
    }

    /**
     * 商机分配规则:获取一级吧id.
     *
     * @param token
     * @param cityName
     * @param isFirst               是否是第一次进入方法
     * @param startSubCompanyId     最开始的一级吧id
     * @param recursionSubCompanyId 由于此方法会轮询一级吧queue去找一级吧id.所以该变量是递归调用时当前的一级吧id
     * @return
     */
    private Integer getSubCompanyIdFromQueue(String token, String cityName, boolean isFirst, Integer startSubCompanyId, Integer recursionSubCompanyId){

        Boolean isOk = false;

        // 查看该一级吧是否满足商机系统分配规则,满足就分配到业务员
        // 月分配数 < 订购数
        Integer id = isFirst ? startSubCompanyId : recursionSubCompanyId;

        // TODO lihong 处理一级吧业务

        if (isOk) {
            // 符合商机系统分配规则
            return id;
        } else {
            // 循环queue获取一级吧，但不能包含最初进入的(包含说明已经全部循环一遍了)
            Integer temp = this.allocateRedisService.getSubCompanyIdFromQueue(token, cityName);
            if (temp != null && !startSubCompanyId.equals(temp)){
                return this.getSubCompanyIdFromQueue(token, cityName, false, startSubCompanyId, temp);
            }
            // 正常情况下，是不会是null,除非缓存被意外动过
            return null;
        }
    }

    /**
     * 商机分配规则:获取一级吧下，符合商机系统规则的业务员.
     */
    private Integer getSalesmanId (Integer subCompanyId, Integer mediaId, String currentMonthStr, boolean isFirst, Integer startSalesmanId, Integer recursionSaleSmanId) {

        Boolean isOK = false;
        // 查看该一级吧是否满足商机系统分配规则,满足就分配到业务员
        // 规则：
        // 1、优先从特殊队列（媒体对应的队列）找
        // 2、再从总分配队列找
        Integer id = isFirst ? startSalesmanId : recursionSaleSmanId;

        // TODO lihong 处理一级吧业务员业务

        if (isOK) {
            // 符合商机系统分配规则
            return id;
        } else {
            // 循环queue找，但不能包含最初进入的(包含说明已经全部循环一遍了)
            Integer temp = this.allocateRedisService.getAndPush2End(subCompanyId, mediaId, currentMonthStr);
            if (temp != null && !startSalesmanId.equals(temp)){
                return this.getSalesmanId(subCompanyId, mediaId, currentMonthStr, false, startSalesmanId, temp);
            }
        }
        // 正常情况下，是不会是null,除非缓存被意外动过
        return null;
    }

    /**
     * 主动申请渠道添加交易
     * @param customerDTO
     * @param token
     */
    public String addLoan(CustomerDTO customerDTO, String token) {
//        if (isActiveApplicationChannel(customerDTO)) {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setTelephonenumber(customerDTO.getTelephonenumber());
        loanDTO.setLoanAmount(customerDTO.getLoanAmount());
        loanDTO.setCustomerId(customerDTO.getId());
        loanDTO.setCustomerName(customerDTO.getCustomerName());
        loanDTO.setOwnUserId(customerDTO.getOwnerUserId());
        loanDTO.setOwnUserName(customerDTO.getOwnUserName());
        loanDTO.setCompanyId(customerDTO.getSubCompanyId());
        loanDTO.setUtmSource("自申请");
        return theaClientService.insertLoan(loanDTO, token);
//        }
    }

    /**
     * 根据渠道获取渠道基本信息（目的获取来源id、媒体id）.
     */
    private BaseChannelDTO getChannelInfoByChannelName(String UtmSource) {
        JSONObject params = new JSONObject();
        params.put("channelName", UtmSource);
        TheaApiDTO<BaseChannelDTO> infoByChannelName = theaService.getInfoByChannelName(params);

        BaseChannelDTO result = new BaseChannelDTO();
        if (infoByChannelName.getResult() == 0 && infoByChannelName.getData() != null){
            result = infoByChannelName.getData();
        }
        if (result == null || result.getSource_id() == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "Source_id 不能为null");
        }
        if (result.getMedia_id() == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "Media_id 不能为null");
        }
        return result;
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

    private void sendCRMAssistantMessage(String customerCity, String customerName, String token) {

        try {
            BaseUcDTO<List<CrmUserDTO>> crmUser = thorService.getCRMUser(token, customerCity);
            List<CrmUserDTO> crmUserDTOList = crmUser.getRetData();
            for (CrmUserDTO crmUserDTO :
                    crmUserDTOList) {
                smsService.sendCRMAssistant(crmUserDTO.getPhone());
            }
        }catch (Exception e)
        {
            logger.error("--sendCRMAssistantMessage:",e);
        }
    }

    private UserInfoDTO getOwnerUser(CustomerDTO customerDTO, String token) {
        // 分析客户扩展信息
        JSONObject extJson = new JSONObject();
        if (StringUtils.isNotBlank(customerDTO.getExt())) {
            extJson = JSON.parseObject(customerDTO.getExt());
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();

        Integer salerId;
        if (null != extJson.get("sale_id") &&
                StringUtils.isNotBlank(extJson.get("sale_id").toString())) {
            // 如果sale_id的格式出现异常，强转失败，则继续走下一步的逻辑
            try {
                salerId = Integer.valueOf(extJson.get("sale_id").toString());
                if (0 != salerId) {
                    BaseUcDTO<UserInfoDTO> thorApiDTO = thorUcService.getUserInfoByField(token, null, salerId, null);
                    if (0 == thorApiDTO.getErrNum() && thorApiDTO.getRetData() != null) {
                        userInfoDTO = thorApiDTO.getRetData();
//                        salerId = Integer.valueOf(userInfoDTO.getUser_id());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("OCDC推送信息中，自带业务员ID强转/获取信息失败。telephonenumber:" + customerDTO.getTelephonenumber());
            }
        }

        // 房速贷推送过来的带业务员手机号的客户
        if (null != extJson.get("owner_user_phone") &&
                StringUtils.isNotBlank(extJson.get("owner_user_phone").toString())) {
            String phone = extJson.get("owner_user_phone").toString();
            // 获取业务员信息
            try {
                BaseUcDTO<UserInfoDTO> thorApiDTO = thorUcService.getUserInfoByField(token,phone, null, null);
                if (0 == thorApiDTO.getErrNum() && thorApiDTO.getRetData() != null) {
                    userInfoDTO = thorApiDTO.getRetData();
//                    salerId = Integer.valueOf(userInfoDTO.getUser_id());
                }
//                else {
//                    salerId = 0;
//                }
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
        // 获取配置中不走自动分配的渠道
//            String allocateToNoUserPool = "";
        String allocateToNoUserPool = theaClientService.getConfigByName(CommonConst.ALLOCATE_TO_NO_USER_POOL);

        // 判断该推送客户是否在限制渠道中/进公盘
        String[] utmSourceStrArray;
        if (StringUtils.isNotBlank(allocateToNoUserPool)) {
            utmSourceStrArray = allocateToNoUserPool.replace("[","").replace("]","").replace("\"","").split(",");
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

        // 获取业务员id
        Integer ownUserId = 0;
        String userIdsStr = allocateRedisService.getAllocateTemplet(city);
        if (StringUtils.isBlank(userIdsStr)) {
            return 0;
        }
        String[] userIdsArray = userIdsStr.split(",");

        // 查询这个队列里面每个人这这个月的分配数（有限）
        List<Integer> ids = CommonUtil.initStrtoIntegerList(userIdsStr);
        Map<String, Object> userMonthMap = new HashMap<>();
        userMonthMap.put("userIds", ids);
        userMonthMap.put("effectiveDate", DateUtils.getyyyyMMForThisMonth());
        List<UserMonthInfo> userMonthInfoServiceList = userMonthInfoService.selectByParamsMap(userMonthMap);
        List<AllocateLog> allocateLogList = new ArrayList<>();
        if (null != userMonthInfoServiceList && userMonthInfoServiceList.size() > 0) {
            //先将所有的客户已分配数归0，原数据表中的数据清空
            List<Integer> newOwnerIds = new ArrayList<>();
            for (UserMonthInfo userMonthInfo : userMonthInfoServiceList) {
                userMonthInfo.setAssignedCustomerNum(0);
                newOwnerIds.add(userMonthInfo.getUserId());
            }

            // 获取该分组业务员的已分配交易数
            Map<String, Object> allocateLogeMap = new HashMap<>();
            allocateLogeMap.put("newOwnerIds", newOwnerIds);
//            allocateLogeMap.put("operationsStr", CommonEnum.LOAN_OPERATION_TYPE_0.getCodeDesc() + "," + CommonEnum.LOAN_OPERATION_TYPE_4.getCodeDesc());
            allocateLogeMap.put("createBeginDate", DateUtils.getStartTimeOfThisMonth());
            allocateLogeMap.put("createEndDate", DateUtils.getStartTimeOfNextMonth());
            allocateLogList = allocateLogService.selectByParamsMap(allocateLogeMap);

            // 将查询结果重新封装为一个根据分配队列的ID结果的对象(将已分配的队列加+1)
            for (int i = 0; i < userIdsArray.length; i++) {
                String user = userIdsArray[i];
                for (UserMonthInfo userMonthInfo : userMonthInfoServiceList) {
                    if (user.equals(userMonthInfo.getUserId().toString())) {
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
            }

        }
        return ownUserId;
    }

    /**
     * 从业务员分配队列中找出未满的业务员.
     */
    private Integer getAllocateUser(Integer companyId, Integer mediaId){

        // 获取当月字符串
        String currentMonthStr = allocateRedisService.getCurrentMonthStr();

        // 将新客户分给有效的业务员
        List<Integer> salesmanList = allocateRedisService.finaAllFromQueue(companyId, mediaId, currentMonthStr);
        salesmanList = CollectionUtils.isEmpty(salesmanList) ? new ArrayList<>() : salesmanList;

        // 获取业务员月分配数
        Map<String, Object> userMonthMap = new HashMap<>();
        userMonthMap.put("userIds", salesmanList);
        userMonthMap.put("effectiveDate", currentMonthStr);
        userMonthMap.put("companyid", companyId);
        userMonthMap.put("mediaid", mediaId);
        userMonthMap.put("status", CommonEnum.entity_status1.getCode());
        List<UserMonthInfo> userMonthInfoServiceList = CollectionUtils.isEmpty(salesmanList) ? new ArrayList<>() : userMonthInfoService.selectByParamsMap(userMonthMap);
        userMonthInfoServiceList.stream().forEach(item -> {
            Integer baseCustomerNum = item.getBaseCustomerNum() == null ? 0 : item.getBaseCustomerNum();
            Integer rewardCustomerNum = item.getRewardCustomerNum() == null ? 0 : item.getRewardCustomerNum();
            item.setAssignedCustomerNum(baseCustomerNum + rewardCustomerNum);
        });
        Map<Integer, Integer> userIdMappingAssignedCustomerNum = userMonthInfoServiceList.stream().collect(groupingBy(UserMonthInfo::getUserId, summingInt(UserMonthInfo::getAssignedCustomerNum)));

        // 获取业务员已分配数
        Map<String, Object> allocateLogeMap = new HashMap<>();
        allocateLogeMap.put("newOwnerIds", salesmanList);
        allocateLogeMap.put("operationsStr", CommonEnum.LOAN_OPERATION_TYPE_0.getCodeDesc() + "," + CommonEnum.LOAN_OPERATION_TYPE_4.getCodeDesc());
        allocateLogeMap.put("createBeginDate", DateUtils.getStartTimeOfThisMonth());
        allocateLogeMap.put("createEndDate", DateUtils.getStartTimeOfNextMonth());
        List<AllocateLog> allocateLogList = CollectionUtils.isEmpty(salesmanList) ? new ArrayList<>() : allocateLogService.selectByParamsMap(allocateLogeMap);
        Map<Integer, Long> userIdMappingAssignedCustomerNum2 = allocateLogList.stream().collect(groupingBy(AllocateLog::getNewOwnerId, counting()));

        // 找出未满的业务员
        for (Integer salesmanId : salesmanList) {
            Integer x = userIdMappingAssignedCustomerNum.get(salesmanId); // 月分配数
            Long y = userIdMappingAssignedCustomerNum2.get(salesmanId);  // 已分配数
            if ( x != null && y != null && x > y){
                return  salesmanId;
            }
        }
        return null;
    }

    /**
     * 客户未沟通重新分配 定时任务 5min
     */
    @Transactional
    public synchronized void nonCommunicateAgainAllocate(String token) {
        new Thread(() -> {
            ValueOperations<String, String> redisConfigOptions = stringRedisTemplate.opsForValue();
            StringBuilder sb = new StringBuilder();
            sb.append("nonCommunicateAgainAllocate");
            try {
                if (currentWorkDayAndTime(token)) {
                    sb.append("-start");
                    String status = redisConfigOptions.get(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE);
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(status) && status.equals("1")) {
                        sb.append("-status 1");
                        return;
                    }
                    sb.append("-status 0");
                    redisConfigOptions.set(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE, CommonEnum.YES.getCode().toString());
                    List<CustomerInfo> list = customerInfoService.selectNonCommunicateInTime().getData();

                    List<Integer> existFailList = cronusRedisService.getRedisFailNonConmunicateAllocateInfo(CommonConst.FAIL_NON_COMMUNICATE_ALLOCATE_INFO);
                    if (existFailList == null) {
                        existFailList = new ArrayList<>();
                    }
                    List<Integer> failList = new ArrayList<>();
                    List<Integer> successList = new ArrayList<>();
                    for (CustomerInfo customerInfo :
                            list) {
                        if (existFailList.contains(customerInfo.getId())) {
                            if (!existFailList.contains(customerInfo.getId()))
                                failList.add(customerInfo.getId());
                            continue;
                        }
                        if (!allocateLogService.newestAllocateLog(customerInfo.getId())) {
                            if (!existFailList.contains(customerInfo.getId()))
                                failList.add(customerInfo.getId());
                            continue;
                        }
                        Integer ownUserId = 0;
                        try {
                            ownUserId = this.getAllocateUser(customerInfo.getCity());
                        } catch (Exception e) {

                        }
                        if (ownUserId > 0) {
                            allocateRedisService.changeAllocateTemplet(customerInfo.getOwnUserId(), customerInfo.getCity());

                            allocateLogService.addAllocatelog(customerInfo, ownUserId,
                                    CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCode(), null);
                            SimpleUserInfoDTO simpleUserInfoDTO = thorUcService.getUserInfoById(token, ownUserId).getData();
                            if (simpleUserInfoDTO != null && null != simpleUserInfoDTO.getSub_company_id()) {
                                customerInfo.setSubCompanyId(Integer.valueOf(simpleUserInfoDTO.getSub_company_id()));
                            } else {
                                customerInfo.setSubCompanyId(0);
                            }
                            customerInfo.setRemain(0);
                            customerInfo.setOwnUserId(ownUserId);
                            customerInfo.setOwnUserName(simpleUserInfoDTO.getName());
                            customerInfo.setReceiveTime(new Date());
                            customerInfo.setLastUpdateTime(new Date());
                            customerInfo.setConfirm(1);
                            customerInfo.setClickCommunicateButton(0);
                            customerInfo.setCommunicateTime(null);
                            customerInfoService.updateCustomerNonCommunicate(customerInfo);


                            //添加分配日志

                            sendMessage(customerInfo.getCustomerName(), ownUserId, simpleUserInfoDTO, token);
                            successList.add(customerInfo.getId());

                        } else {
                            //分配名额已经满了,向这个城市的crm助理发送短信
                            sendCRMAssistantMessage(customerInfo.getCity(), customerInfo.getCustomerName(), token);
                            if (!failList.contains(customerInfo.getId()))
                                failList.add(customerInfo.getId());
                        }
                    }
                    redisConfigOptions.set(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE, CommonEnum.NO.getCode().toString());
                    //failList 添加到缓存
                    existFailList.addAll(failList);
                    cronusRedisService.setRedisFailNonConmunicateAllocateInfo(CommonConst.FAIL_NON_COMMUNICATE_ALLOCATE_INFO, failList);
                    if (successList.size()>0) {
                        theaService.invalidLoans(token, convertListToString(successList));
                    }
                    sb.append("--");
                    sb.append("failList:" + failList.toString());
                    sb.append("--");
                    sb.append("successList:" + successList.toString());
                }
                else {
                    sb.append("--non work time");
                }
            } catch (Exception e) {
                redisConfigOptions.set(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE, CommonEnum.NO.getCode().toString());
                logger.error("nonCommunicateAgainAllocate--", e);
            }
            logger.warn(sb.toString());
        }).run();
    }

    private String convertListToString(List<Integer> list)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            if (i < list.size() - 1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }


    /**
     * 设置未沟通重新分配状态
     * @param status
     */
    public boolean nonCommunicateAllocateStatus(String status)
    {
        boolean setStatus = false;
        ValueOperations<String, String> redisConfigOptions = stringRedisTemplate.opsForValue();
        if (status.equals("1")) {
            redisConfigOptions.set(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE, CommonEnum.YES.getCode().toString());
            setStatus = true;
        }
        else if (status.equals("0")){
            redisConfigOptions.set(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE, CommonEnum.NO.getCode().toString());
            setStatus = true;
        }
        return setStatus;
    }

    /**
     * 获取未沟通状态
     * @return
     */
    public String getNonCommunicateAllocateStatus()
    {
        ValueOperations<String, String> redisConfigOptions = stringRedisTemplate.opsForValue();
        return redisConfigOptions.get(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE);
    }

    public boolean currentWorkDayAndTime(String token) {
        boolean value = false;
        Date date = new Date();
        Integer hour = DateUtils.getHour(new Date());
        if (10 <= hour && hour < 18) {
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
            if (workDays.contains(DateUtils.getDay(date).toString())) {
                value = true;
            }
        }
        return value;
    }
}
