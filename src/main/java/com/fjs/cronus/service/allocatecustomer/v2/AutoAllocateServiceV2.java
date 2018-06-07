package com.fjs.cronus.service.allocatecustomer.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.dto.AllocateForAvatarDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.avatar.AvatarApiDTO;
import com.fjs.cronus.dto.avatar.OrderNumberDTO;
import com.fjs.cronus.dto.avatar.OrderNumberDetailDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.thea.WorkDayDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.CrmUserDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.entity.AllocateEntity;
import com.fjs.cronus.enums.AllocateEnum;
import com.fjs.cronus.enums.AllocateSource;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.UserMonthInfoMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.*;
import com.fjs.cronus.service.allocatecustomer.v2.UserMonthInfoServiceV2;
import com.fjs.cronus.service.client.AvatarClientService;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.redis.AllocateRedisServiceV2;
import com.fjs.cronus.service.redis.CRMRedisLockHelp;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.thea.ThorClientService;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import com.fjs.cronus.util.SingleCutomerAllocateDevInfoUtil;
import com.fjs.framework.exception.BaseException;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by feng on 2017/9/21.
 */
@Service
public class AutoAllocateServiceV2 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String companyIdKey = "companyKey";
    private static final String salesmanIdKey = "salesmanId";

    //    @Value("${publicToken}")
//    private String publicToken;

//    @Autowired
//    private ConfigRedisService configRedisService;

    @Autowired
    private ThorService thorUcService;

    @Autowired
    private AllocateRedisServiceV2 allocateRedisService;

    @Autowired
    private UserMonthInfoServiceV2 userMonthInfoService;

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

    @Autowired
    private CompanyMediaQueueService companyMediaQueueService;

    @Autowired
    private UserMonthInfoMapper userMonthInfoMapper;

    @Autowired
    private CRMRedisLockHelp cRMRedisLockHelp;

    @Autowired
    private DelayAllocateService delayAllocateService;

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


    /**
     * 处理单个客户的分配.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public synchronized AllocateEntity autoAllocate(CustomerDTO customerDTO, AllocateSource allocateSource, String token) {
        AllocateEntity allocateEntity = new AllocateEntity();
        allocateEntity.setSuccess(true);
        Long lockToken = null;

        try {
            // 锁1分钟，如20分钟内未计算完，就超时抛错回滚;
            // 其他并行线程重试6次，每次等待5秒，共30秒
            lockToken = this.cRMRedisLockHelp.lockBySetNX2(CommonRedisConst.ALLOCATE_LOCK, 60, TimeUnit.SECONDS, 6, 5, TimeUnit.SECONDS);

            // 获取自动分配的城市
            String allocateCities = theaClientService.getConfigByName(CommonConst.CAN_ALLOCATE_CITY);

            // 需要去不同方法取数据(初始化为null，知道里面key是什么)
            AllocateForAvatarDTO signCustomAllocate = new AllocateForAvatarDTO();

            Integer mediaId = userMonthInfoService.getChannelInfoByChannelName(token, customerDTO.getUtmSource()); // 根据渠道获取来源、媒体、渠道
            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k12, ImmutableMap.of("UtmSource", customerDTO.getUtmSource()), ImmutableMap.of("mediaId", mediaId));

            String currentMonthStr = this.allocateRedisService.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_CURRENT); // 当月字符串

            // 分配规则
            if (customerDTO.getId() == null || customerDTO.getId().equals(0)) {
                // 新客户：走商机系统规则
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k13);

                if (StringUtils.contains(allocateCities, customerDTO.getCity())) {
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k15);

                    signCustomAllocate = this.allocateForAvatar(token, customerDTO, mediaId, currentMonthStr);
                    if (signCustomAllocate.getSuccessOfAvatar()) {
                        SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k14, ImmutableMap.of("SalesmanId", signCustomAllocate.getSalesmanId()));
                        // 找到被分配的业务员
                        allocateEntity.setAllocateStatus(AllocateEnum.ALLOCATE_TO_OWNER);
                        customerDTO.setOwnerUserId(signCustomAllocate.getSalesmanId());
                    } else {
                        // 未找到，进商机池
                        SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k17);
                        allocateEntity.setAllocateStatus(AllocateEnum.AVATAR_POOL);
                        customerDTO.setOwnerUserId(-1); // 标记是商机池，-1
                    }
                } else {
                    // 进客服系统
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Req(SingleCutomerAllocateDevInfoUtil.k16, ImmutableMap.of("city", customerDTO.getCity()));
                    allocateEntity.setAllocateStatus(AllocateEnum.TO_SERVICE_SYSTEM);
                }
            } else {
                // 老客户
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k1);

                UserInfoDTO ownerUser = this.getOwnerUser(customerDTO, token); // 获取负责人

                if (StringUtils.isNotEmpty(ownerUser.getUser_id())) { // 存在这个在职负责人
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k18, ImmutableMap.of("salemanid", ownerUser.getUser_id()));

                    customerDTO.setOwnerUserId(Integer.valueOf(ownerUser.getUser_id()));
                    allocateEntity.setAllocateStatus(AllocateEnum.EXIST_OWNER);
                } else if (StringUtils.isNotEmpty(customerDTO.getCity()) && StringUtils.contains(allocateCities, customerDTO.getCity())) { // 在有效分配城市内
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k15);
                    // 根据城市，去找一级吧下业务员
                    signCustomAllocate = this.getAllocateUserV2(token, customerDTO.getCity(), currentMonthStr, mediaId);
                    if (signCustomAllocate.getSuccessOfOldcustomer()) { //找到业务员
                        SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k14, ImmutableMap.of("SalesmanId", signCustomAllocate.getSalesmanId()));
                        allocateEntity.setAllocateStatus(AllocateEnum.ALLOCATE_TO_OWNER);
                        customerDTO.setOwnerUserId(signCustomAllocate.getSalesmanId());
                    } else { // 未找到，抛错记录日志
                        SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k19);
                        throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "老客户去队列没找到业务员");
                    }
                } else {
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k16);
                    customerDTO.setOwnerUserId(0);
                    allocateEntity.setAllocateStatus(AllocateEnum.TO_SERVICE_SYSTEM);
                }
            }

            // 保存客户
            SimpleUserInfoDTO simpleUserInfoDTO = null;
            if (null != customerDTO.getId() && customerDTO.getId() > 0) { // 老客户

                // 更新客户信息
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
                        case "-1": // 商机池
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
                            if (allocateEntity.getAllocateStatus().getCode().equals("3") || allocateEntity.getAllocateStatus().getCode().equals("1")) {
                                if (this.isActiveApplicationChannel(customerDTO)) {
                                    customerDTO.setRemain(CommonConst.REMAIN_STATUS_YES);
                                }
                            }

                            // 新建用户信息
                            CronusDto cronusDto1 = customerInfoService.addOcdcCustomer(customerDTO, token);
                            if (cronusDto1.getResult() == 0) {
                                customerDTO.setId(Integer.parseInt(cronusDto1.getData().toString()));
                                if (signCustomAllocate.getSuccessOfAvatar()) {
                                    // 新客户已找到业务员，记录分配数
                                    this.userMonthInfoService.incrNum2DBForOCDCPush(signCustomAllocate, mediaId, currentMonthStr, customerDTO);
                                }
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
                    // 添加分配日志
                    CustomerInfo customerInfo = new CustomerInfo();
                    EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
                    allocateLogService.autoAllocateAddAllocatelog(customerInfo.getId(), customerDTO.getOwnerUserId(),
                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCode());

                    if (this.isActiveApplicationChannel(customerDTO)) {
                        String loan = this.addLoan(customerDTO, token);
                        allocateEntity.setDescription(loan);
                    }

                    Integer r = this.sendMessage(customerDTO.getCustomerName(), customerDTO.getOwnerUserId(), simpleUserInfoDTO, token);
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k51
                            , ImmutableMap.of("CustomerName", customerDTO.getCustomerName(), "OwnerUserId", customerDTO.getOwnerUserId(), "simpleUserInfoDTO", simpleUserInfoDTO, "token", token)
                            , ImmutableMap.of("短信响应", r)
                    );
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
                    Integer rr = this.sendMessage(customerDTO.getCustomerName(), customerDTO.getOwnerUserId(), simpleUserInfoDTO, token);
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k52
                            , ImmutableMap.of("CustomerName", customerDTO.getCustomerName(), "OwnerUserId", customerDTO.getOwnerUserId(), "simpleUserInfoDTO", simpleUserInfoDTO, "token", token)
                            , ImmutableMap.of("短信响应", rr)
                    );
                    break;
                case "4": // 推入客服系统
                    break;
            }

            // 在20秒内未完成运算，视为失败；事务回滚；redis解锁;让给其他线程资源
            long l = this.cRMRedisLockHelp.getCurrentTimeFromRedisServicer() - lockToken;
            if (l > (20 * 1000)) {
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k20, ImmutableMap.of("请求耗时", l));
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "服务超时（redis锁超时）");
            }

        } catch (Exception e) {
            logger.error("-------------------自动分配失败:ocdcDataId=" + customerDTO.getTelephonenumber() + "-------------------", e);
            StringBuffer sb = new StringBuffer();
            sb.append("自动分配失败: errorMessage=" + e.getMessage() + " telephonenumber=" + customerDTO.getTelephonenumber() + " ocdcId=" + customerDTO.getOcdcId());
            // 以单个客户为维度，记录每个客户分配异常的信息
            if (e instanceof BaseException) {
                // 已知异常
                BaseException be = (BaseException) e;
                sb.append(" 已知异常:" + be.getResponseError().getMessage());
            } else {
                // 未知异常
                sb.append(" 未知异常:" + e.getMessage());
            }
            SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k10, ImmutableMap.of("errorMessage", sb.toString()));
            SingleCutomerAllocateDevInfoUtil.local.get().setSuccess(false);
            allocateEntity.setSuccess(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            this.cRMRedisLockHelp.unlockForSetNx2(CommonRedisConst.ALLOCATE_LOCK, lockToken);
        }
        return allocateEntity;
    }

    /**
     * 商机系统分配规则.
     */
    private AllocateForAvatarDTO allocateForAvatar(String token, CustomerDTO customerDTO, Integer media_id, String currentMonthStr) {

        AllocateForAvatarDTO result = new AllocateForAvatarDTO();

        long size = allocateRedisService.getSubCompanyIdQueueSize(token, customerDTO.getCity(), media_id);
        SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k21
                , ImmutableMap.of("media_id", media_id, "city", customerDTO.getCity())
                , ImmutableMap.of("队列大小", size));
        for (int j = 0; j < size; j++) {
            // 循环城市下一级吧queue

            // 从queue获取一级吧
            Integer subCompanyId = this.allocateRedisService.getSubCompanyIdFromQueue(customerDTO.getCity(), media_id);
            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k22 + j
                    , ImmutableMap.of("media_id", media_id, "city", customerDTO.getCity())
                    , ImmutableMap.of("subCompanyId", subCompanyId));
            if (subCompanyId == null) {
                continue;
            }

            // 获取当月已分配数
            Integer orderNumOfCompany = userMonthInfoMapper.getOrderNum(subCompanyId, currentMonthStr, CommonEnum.entity_status1.getCode(), media_id);
            orderNumOfCompany = orderNumOfCompany == null ? 0 : orderNumOfCompany;
            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k23 + j
                    , ImmutableMap.of("subCompanyId", subCompanyId, "currentMonthStr", currentMonthStr, "media_id", media_id)
                    , ImmutableMap.of("当月已分配数", orderNumOfCompany));

            // 从商机系统获取
            JSONObject json = new JSONObject();
            json.put("firstBarId", subCompanyId);
            json.put("month", allocateRedisService.getMonthStr4avatar(currentMonthStr));
            AvatarApiDTO<OrderNumberDTO> orderNumberDTOAvatarApiDTO = this.avatarClientService.queryOrderNumber(token, json);
            if (orderNumberDTOAvatarApiDTO == null || orderNumberDTOAvatarApiDTO.getResult() != 0) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "请求商机系统异常：响应为null");
            }
            if (orderNumberDTOAvatarApiDTO.getResult() != 0) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "请求商机系统异常:" + " result:" + orderNumberDTOAvatarApiDTO.getResult() + " message:" + orderNumberDTOAvatarApiDTO.getMessage());
            }
            OrderNumberDTO data = orderNumberDTOAvatarApiDTO.getData();
            if (data == null || CollectionUtils.isEmpty(data.getOrderNumberList())) {
                // 响应正常，但无数据视为未订购
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k24 + j
                        , ImmutableMap.of("响应正常，但无数据视为未订购", 0));
                continue;
            }

            List<OrderNumberDetailDTO> orderNumberList = data.getOrderNumberList();
            Integer orderNumber = orderNumberList.stream().filter(i -> i != null && media_id.equals(i.getMeidaId())).map(OrderNumberDetailDTO::getOrderNumber).findAny().orElse(0);
            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k24 + j
                    , ImmutableMap.of("firstBarId", subCompanyId, "currentMonthStr", currentMonthStr)
                    , ImmutableMap.of("订购数", orderNumber));

            // 业务校验：一级吧已购数、订购数
            if (orderNumOfCompany >= orderNumber) {
                // 已购数 >= 订购数
                continue;
            }

            // 找业务员
            //
            // 先去特殊队列找，找不到然后去总分配队列找
            Integer salesmanId = null;

            boolean idFromCountQueue = false; // 记录业务员是从特殊媒体queue取出，还是总queue中取出.

            long queueSizeMedia = allocateRedisService.getQueueSize(subCompanyId, media_id, currentMonthStr);
            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k26 + j
                    , ImmutableMap.of("subCompanyId", subCompanyId, "media_id", media_id, "currentMonthStr", currentMonthStr)
                    , ImmutableMap.of("队列大小", queueSizeMedia));

            for (int i = 0; i < queueSizeMedia; i++) {
                // 去特殊分配队列找
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k46 + j);

                salesmanId = allocateRedisService.getAndPush2End(subCompanyId, media_id, currentMonthStr);
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k27 + j + "$" + i
                        , ImmutableMap.of("subCompanyId", subCompanyId, "media_id", media_id, "currentMonthStr", currentMonthStr)
                        , ImmutableMap.of("salesmanId", salesmanId));
                if (salesmanId == null) {
                    continue;
                }

                // 比较该业务员的 已购数据、分配数
                // 获取月分配数
                UserMonthInfo e = new UserMonthInfo();
                e.setCompanyid(subCompanyId);
                e.setUserId(salesmanId);
                e.setEffectiveDate(currentMonthStr);
                e.setStatus(CommonEnum.entity_status1.getCode());
                e.setMediaid(media_id);
                List<UserMonthInfo> select = userMonthInfoMapper.select(e);

                if (CollectionUtils.isEmpty(select)
                        ) {
                    // 数据错误，直接忽略，给下一个业务员处理
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k28 + j + "$" + i
                            , ImmutableMap.of("subCompanyId", subCompanyId, "media_id", media_id, "currentMonthStr", currentMonthStr, "salesmanId", salesmanId)
                            , ImmutableMap.of("list", "列表为空 or 0", "数据错误，直接忽略", "给下一个业务员处理"));
                    salesmanId = null;
                    continue;
                }
                UserMonthInfo userMonthInfo = select.stream()
                        .filter(item -> item != null
                                && item.getBaseCustomerNum() != null
                                && item.getRewardCustomerNum() != null
                                && item.getAssignedCustomerNum() != null
                        ).findAny()
                        .orElse(null);

                if (userMonthInfo == null) {
                    // 数据错误，直接忽略，给下一个业务员处理
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k28 + j + "$" + i
                            , ImmutableMap.of("subCompanyId", subCompanyId, "media_id", media_id, "currentMonthStr", currentMonthStr, "salesmanId", salesmanId)
                            , ImmutableMap.of("userMonthInfo", "数据为null", "list大小", select.size()));
                    salesmanId = null;
                    continue;
                }
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k28 + j + "$" + i
                        , ImmutableMap.of("subCompanyId", subCompanyId, "media_id", media_id, "currentMonthStr", currentMonthStr, "salesmanId", salesmanId)
                        , ImmutableMap.of("userMonthInfoid", userMonthInfo.getId(),
                                "AssignedCustomerNum", userMonthInfo.getAssignedCustomerNum(),
                                "RewardCustomerNum", userMonthInfo.getRewardCustomerNum(),
                                "BaseCustomerNum", userMonthInfo.getBaseCustomerNum()));

                // 比较特殊媒体
                if (userMonthInfo.getAssignedCustomerNum() >= userMonthInfo.getBaseCustomerNum() + userMonthInfo.getRewardCustomerNum()) {
                    // 该业务员 已购数 >= 分配数
                    salesmanId = null;
                    continue;
                }

                // 比较总队列数量
                UserMonthInfo e2 = new UserMonthInfo();
                e2.setCompanyid(subCompanyId);
                e2.setUserId(salesmanId);
                e2.setEffectiveDate(currentMonthStr);
                e2.setStatus(CommonEnum.entity_status1.getCode());
                e2.setMediaid(CommonConst.COMPANY_MEDIA_QUEUE_COUNT);
                List<UserMonthInfo> select2 = userMonthInfoMapper.select(e2);

                if (CollectionUtils.isEmpty(select2)
                        ) {
                    // 数据错误，直接忽略，给下一个业务员处理
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k29 + j + "$" + i
                            , ImmutableMap.of("subCompanyId", subCompanyId, "media_id", CommonConst.COMPANY_MEDIA_QUEUE_COUNT, "currentMonthStr", currentMonthStr, "salesmanId", salesmanId)
                            , ImmutableMap.of("list", "队列为空 or 0", "数据错误，直接忽略", "给下一个业务员处理"));
                    salesmanId = null;
                    continue;
                }
                UserMonthInfo userMonthInfo2 = select2.stream()
                        .filter(item -> item != null
                                && item.getBaseCustomerNum() != null
                                && item.getRewardCustomerNum() != null
                                && item.getAssignedCustomerNum() != null
                        ).findAny()
                        .orElse(null);
                if (userMonthInfo2 == null) {
                    // 数据错误，直接忽略，给下一个业务员处理
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k29 + j + "$" + i
                            , ImmutableMap.of("subCompanyId", subCompanyId, "media_id", CommonConst.COMPANY_MEDIA_QUEUE_COUNT, "currentMonthStr", currentMonthStr, "salesmanId", salesmanId)
                            , ImmutableMap.of("userMonthInfo2", "数据为null", "list大小", select2.size(), "数据错误，直接忽略", "给下一个业务员处理"));
                    salesmanId = null;
                    continue;
                }
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k29 + j + "$" + i
                        , ImmutableMap.of("subCompanyId", subCompanyId, "media_id", CommonConst.COMPANY_MEDIA_QUEUE_COUNT, "currentMonthStr", currentMonthStr, "salesmanId", salesmanId)
                        , ImmutableMap.of("userMonthInfo id", userMonthInfo2.getId()
                                , "BaseCustomerNum", userMonthInfo2.getBaseCustomerNum()
                                , "RewardCustomerNum", userMonthInfo2.getRewardCustomerNum()
                                , "AssignedCustomerNum", userMonthInfo2.getAssignedCustomerNum()));

                // 比较总队列
                if (userMonthInfo2.getAssignedCustomerNum() >= userMonthInfo2.getBaseCustomerNum() + userMonthInfo2.getRewardCustomerNum()) {
                    // 该业务员 已购数 >= 分配数
                    salesmanId = null;
                    continue;
                }

                // 找到业务员接待
                if (salesmanId != null) {
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k30 + j + "$" + i
                            , ImmutableMap.of("salesmanId", salesmanId)
                    );
                    break;
                }
            }
            if (salesmanId == null) {
                // 去总分配队列找
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k31 + j);

                // 是否需要判断特殊队列(特殊队列是否被关注)
                Boolean flag = false;
                if (queueSizeMedia > 0) {
                    flag = true;
                } else {
                    Set<Integer> followMediaidFromDB = this.companyMediaQueueService.findFollowMediaidFromDB(subCompanyId);
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k25 + j + "$" + j
                            , ImmutableMap.of("subCompanyId", subCompanyId)
                            , ImmutableMap.of("关注的媒体", followMediaidFromDB));
                    if (followMediaidFromDB.contains(media_id)) {
                        flag = true;
                    }
                }

                queueSizeMedia = allocateRedisService.getQueueSize(subCompanyId, CommonConst.COMPANY_MEDIA_QUEUE_COUNT, currentMonthStr);
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k32 + j
                        , ImmutableMap.of("subCompanyId", subCompanyId, "currentMonthStr", currentMonthStr, "mediaid", CommonConst.COMPANY_MEDIA_QUEUE_COUNT)
                        , ImmutableMap.of("队列大小", queueSizeMedia));


                for (int k = 0; k < queueSizeMedia; k++) {
                    idFromCountQueue = true;
                    // 需要业务员queue找业务员

                    salesmanId = allocateRedisService.getAndPush2End(subCompanyId, CommonConst.COMPANY_MEDIA_QUEUE_COUNT, currentMonthStr);
                    if (salesmanId == null) {
                        SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k33 + j + "$" + k
                                , ImmutableMap.of("subCompanyId", subCompanyId, "currentMonthStr", currentMonthStr, "mediaid", CommonConst.COMPANY_MEDIA_QUEUE_COUNT)
                                , ImmutableMap.of("salesmanId", "数据为null", "数据错误，直接忽略", "给下一个业务员处理"));
                        continue;
                    }

                    // 比较该业务员的 已购数据、分配数
                    UserMonthInfo e = new UserMonthInfo();
                    e.setCompanyid(subCompanyId);
                    e.setUserId(salesmanId);
                    e.setMediaid(CommonConst.COMPANY_MEDIA_QUEUE_COUNT);
                    e.setEffectiveDate(currentMonthStr);
                    e.setStatus(CommonEnum.entity_status1.getCode());
                    List<UserMonthInfo> select = userMonthInfoMapper.select(e);

                    if (CollectionUtils.isEmpty(select)
                            ) {
                        // 数据错误，直接忽略，给下一个业务员处理
                        SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k34 + j + "$" + k
                                , ImmutableMap.of("subCompanyId", subCompanyId, "salesmanId", salesmanId, "currentMonthStr", currentMonthStr, "mediaid", CommonConst.COMPANY_MEDIA_QUEUE_COUNT)
                                , ImmutableMap.of("list", "数据为null or 0", "数据错误，直接忽略", "给下一个业务员处理"));
                        salesmanId = null;
                        continue;
                    }

                    UserMonthInfo userMonthInfo = select.get(0);
                    if (userMonthInfo == null
                            || userMonthInfo.getBaseCustomerNum() == null
                            || userMonthInfo.getRewardCustomerNum() == null
                            || userMonthInfo.getAssignedCustomerNum() == null
                            ) {
                        // 数据错误，直接忽略，给下一个业务员处理
                        SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k34 + j + "$" + k
                                , ImmutableMap.of("subCompanyId", subCompanyId, "salesmanId", salesmanId, "currentMonthStr", currentMonthStr, "mediaid", CommonConst.COMPANY_MEDIA_QUEUE_COUNT)
                                , ImmutableMap.of("userMonthInfo", "数据为null", "list", select.size(), "数据错误，直接忽略", "给下一个业务员处理"));
                        salesmanId = null;
                        continue;
                    }
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k34 + j + "$" + k
                            , ImmutableMap.of("subCompanyId", subCompanyId, "salesmanId", salesmanId, "currentMonthStr", currentMonthStr, "mediaid", CommonConst.COMPANY_MEDIA_QUEUE_COUNT)
                            , ImmutableMap.of("BaseCustomerNum", userMonthInfo.getBaseCustomerNum()
                                    , "AssignedCustomerNum", userMonthInfo.getAssignedCustomerNum()
                                    , "RewardCustomerNum", userMonthInfo.getRewardCustomerNum()));

                    if (userMonthInfo.getAssignedCustomerNum() >= (userMonthInfo.getBaseCustomerNum() + userMonthInfo.getRewardCustomerNum())) {
                        // 该业务员 已购数 >= 分配数
                        salesmanId = null;
                        continue;
                    }

                    // 校验，如果是从总队列中找，且也关注了特殊队列，需要校验满足特殊队列分配数 > 已分配数
                    if (flag) {

                        SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k35 + j + "$" + k);

                        UserMonthInfo e2 = new UserMonthInfo();
                        e2.setCompanyid(subCompanyId);
                        e2.setUserId(salesmanId);
                        e2.setMediaid(media_id);
                        e2.setEffectiveDate(currentMonthStr);
                        e2.setStatus(CommonEnum.entity_status1.getCode());
                        List<UserMonthInfo> select2 = userMonthInfoMapper.select(e2);

                        if (CollectionUtils.isEmpty(select2)
                                ) {
                            // 数据错误，直接忽略，给下一个业务员处理
                            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k36 + j + "$" + k
                                    , ImmutableMap.of("subCompanyId", subCompanyId, "salesmanId", salesmanId, "media_id", media_id, "currentMonthStr", currentMonthStr)
                                    , ImmutableMap.of("list", "数据为null or 0", "数据错误，直接忽略", "给下一个业务员处理"));
                            salesmanId = null;
                            continue;
                        }

                        UserMonthInfo userMonthInfo2 = select2.stream()
                                .filter(item -> item != null
                                        && item.getBaseCustomerNum() != null
                                        && item.getRewardCustomerNum() != null
                                        && item.getAssignedCustomerNum() != null
                                )
                                .findAny().orElse(null);
                        if (userMonthInfo2 == null) {
                            // 数据错误，直接忽略，给下一个业务员处理
                            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k36 + j + "$" + k
                                    , ImmutableMap.of("subCompanyId", subCompanyId, "salesmanId", salesmanId, "media_id", media_id, "currentMonthStr", currentMonthStr)
                                    , ImmutableMap.of("userMonthInfo2", "数据为null", "数据错误，直接忽略", "给下一个业务员处理"));
                            salesmanId = null;
                            continue;
                        }

                        if (userMonthInfo2.getAssignedCustomerNum() >= userMonthInfo2.getBaseCustomerNum() + userMonthInfo2.getRewardCustomerNum()) {
                            // 该业务员 已购数 >= 分配数
                            salesmanId = null;
                            continue;
                        }
                    }

                    // 找到业务员接待
                    if (salesmanId != null) {
                        SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k38 + j + "$" + k
                                , ImmutableMap.of("salesmanId", salesmanId));
                        break;
                    }
                }
            }

            if (salesmanId != null) {
                SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k39 + j
                        , ImmutableMap.of("salesmanId", salesmanId, "idFromCountQueue", idFromCountQueue));
                result.setCompanyid(subCompanyId);
                result.setSalesmanId(salesmanId);
                result.setMediaid(subCompanyId);
                result.setFrommediaid(idFromCountQueue ? CommonConst.COMPANY_MEDIA_QUEUE_COUNT : media_id);
                result.setSuccessOfAvatar(true);

                // 找到业务员，且实购数 == 已购数,需要发送手机短信
                if (orderNumOfCompany + 1 == orderNumber) {
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Req(SingleCutomerAllocateDevInfoUtil.k48 + j
                            , ImmutableMap.of("实购数", orderNumOfCompany, "订购数", orderNumber, "一级吧", subCompanyId, "媒体id", media_id));

                    JSONObject params = new JSONObject();
                    params.put("firstBarId", subCompanyId);
                    params.put("mediaId", media_id);
                    params.put("realNumber", orderNumOfCompany);
                    params.put("time", new Date().getTime());
                    avatarClientService.purchaseSmsNotice(token, params);
                }

                break;
            }
        }
        return result;
    }

    /**
     * 主动申请渠道添加交易
     *
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
        loanDTO.setUtmSource(customerDTO.getUtmSource());
        return theaClientService.insertLoan(loanDTO, token);
//        }
    }

    private Integer sendMessage(String customerName, Integer toId, SimpleUserInfoDTO ownerUser, String token) {
        theaClientService.sendMail(token,
                "房金所为您分配了客户名：" + customerName + "，请注意跟进。",
                0,
                0,
                "系统管理员",
                toId);

        return smsService.sendSmsForAutoAllocate(ownerUser.getTelephone(), customerName);
    }

    private void sendCRMAssistantMessage(String customerCity, String customerName, String token) {

        try {
            BaseUcDTO<List<CrmUserDTO>> crmUser = thorService.getCRMUser(token, customerCity);
            List<CrmUserDTO> crmUserDTOList = crmUser.getRetData();
            for (CrmUserDTO crmUserDTO :
                    crmUserDTOList) {
                smsService.sendCRMAssistant(crmUserDTO.getPhone());
            }
        } catch (Exception e) {
            logger.error("--sendCRMAssistantMessage:", e);
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
                BaseUcDTO<UserInfoDTO> thorApiDTO = thorUcService.getUserInfoByField(token, phone, null, null);
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

    /**
     * 老客户，会从总分配队列(借用商机系统的队列)找业务员.
     */
    private AllocateForAvatarDTO getAllocateUserV2(String token, String city, String currentMonthStr, Integer mediaid) {

        AllocateForAvatarDTO result = new AllocateForAvatarDTO();

        long size = allocateRedisService.getSubCompanyIdQueueSize(token, city, mediaid);
        SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k40
                , ImmutableMap.of("city", city, "mediaid", mediaid)
                , ImmutableMap.of("队列大小", size));
        for (int i = 0; i < size; i++) {

            Integer subCompanyId = allocateRedisService.getSubCompanyIdFromQueue(city, mediaid);
            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k41 + "$" + i
                    , ImmutableMap.of("city", city, "mediaid", mediaid)
                    , ImmutableMap.of("subCompanyId", subCompanyId));
            long size2 = allocateRedisService.getQueueSize(subCompanyId, CommonConst.COMPANY_MEDIA_QUEUE_COUNT, currentMonthStr);
            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k42 + "$" + i
                    , ImmutableMap.of("subCompanyId", subCompanyId, "currentMonthStr", currentMonthStr)
                    , ImmutableMap.of("队列大小", size));
            for (int j = 0; j < size2; j++) {
                Integer salesmanId = allocateRedisService.getAndPush2End(subCompanyId, CommonConst.COMPANY_MEDIA_QUEUE_COUNT, currentMonthStr);

                if (salesmanId != null) {
                    // 找到业务员
                    SingleCutomerAllocateDevInfoUtil.local.get().setInfo4Rep(SingleCutomerAllocateDevInfoUtil.k44 + "$" + i + "$" + j
                            , ImmutableMap.of("salesmanId", salesmanId));
                    result.setCompanyid(subCompanyId);
                    result.setSalesmanId(salesmanId);
                    result.setSuccessOfOldcustomer(true);
                    break;
                }
            }
            if (result.getSuccessOfOldcustomer()) {
                break;
            }
        }
        if (!result.getSuccessOfOldcustomer()) {
            SingleCutomerAllocateDevInfoUtil.local.get().setInfo(SingleCutomerAllocateDevInfoUtil.k43);
        }
        return result;
    }

    /**
     * 设置未沟通重新分配状态
     *
     * @param status
     */
    public boolean nonCommunicateAllocateStatus(String status) {
        boolean setStatus = false;
        ValueOperations<String, String> redisConfigOptions = stringRedisTemplate.opsForValue();
        if (status.equals("1")) {
            redisConfigOptions.set(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE, CommonEnum.YES.getCode().toString());
            setStatus = true;
        } else if (status.equals("0")) {
            redisConfigOptions.set(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE, CommonEnum.NO.getCode().toString());
            setStatus = true;
        }
        return setStatus;
    }

    /**
     * 获取未沟通状态
     *
     * @return
     */
    public String getNonCommunicateAllocateStatus() {
        ValueOperations<String, String> redisConfigOptions = stringRedisTemplate.opsForValue();
        return redisConfigOptions.get(CommonConst.NON_COMMUNICATE_AGAIN_ALLOCATE);
    }

    public boolean currentWorkDayAndTime(String token) {
        return currentWorkDayAndTime(token, new Date());
    }

    public boolean currentWorkDayAndTime(String token, Date date) {
        boolean value = false;
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

    /**
     * 添加15分钟未沟通Task到处理queue中.
     */
    private void addDelayAllocate(String token, String phone){
        Calendar now = Calendar.getInstance();

        // 业务校验：是否在工作日内
        if (!currentWorkDayAndTime(token, now.getTime())) {
            return;
        }

        // 业务校验：是否在工作时间内
        if (now.get(Calendar.HOUR_OF_DAY) > 18 || now.get(Calendar.HOUR_OF_DAY) < 10){
            return;
        }

        now.add(Calendar.MINUTE, 15);
        delayAllocateService.acceptData(phone, now.getTime());
    }
}
