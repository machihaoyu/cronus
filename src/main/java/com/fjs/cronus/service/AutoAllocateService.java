package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;

import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.avatar.AvatarApiDTO;
import com.fjs.cronus.dto.avatar.OrderNumberDTO;
import com.fjs.cronus.dto.avatar.OrderNumberDetailDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.thea.BaseChannelDTO;
import com.fjs.cronus.dto.thea.WorkDayDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.CrmUserDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.entity.AllocateEntity;
import com.fjs.cronus.enums.AllocateEnum;
import com.fjs.cronus.enums.AllocateSource;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.UserMonthInfoMapper;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by feng on 2017/9/21.
 */
@Service
public class AutoAllocateService {

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

    @Autowired
    private CompanyMediaQueueService companyMediaQueueService;

    @Autowired
    private UserMonthInfoMapper userMonthInfoMapper;

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

            // 需要去不同方法取数据(初始化为null，知道里面key是什么)
            Map<String, Object> needDataBox = new HashMap<>();
            needDataBox.put(companyIdKey, null); // 一级吧id
            needDataBox.put(salesmanIdKey, null);   // 业务员id

            BaseChannelDTO baseChannelDTO = userMonthInfoService.getChannelInfoByChannelName(token, customerDTO.getUtmSource());; // 根据渠道获取来源、媒体、渠道
            String currentMonthStr = this.allocateRedisService.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_CURRENT); // 当月字符串
            boolean isNewCustomerFromAvatar = false;


            // 分配规则
            if ( customerDTO.getId() == null || customerDTO.getId().equals(0) ) {
                // 新客户：走商机系统规则

                // 商机系统分支
                // 规则：
                // 1、新客户，在有效城市范围内--->走商机分配规则找业务员
                // 1.1、找到，分配给业务员
                // 1.2、未找到、进待分配池
                // 2、新客户，不在有效城市范围内--->进客服系统
                if (StringUtils.contains(allocateCities, customerDTO.getCity())) {

                    if (this.allocateForAvatar(token, customerDTO, baseChannelDTO,  needDataBox, currentMonthStr)) {
                        // 找到被分配的业务员
                        allocateEntity.setAllocateStatus(AllocateEnum.ALLOCATE_TO_OWNER);
                        customerDTO.setOwnerUserId((Integer) needDataBox.get(salesmanIdKey));

                        // 标记
                        isNewCustomerFromAvatar = true;
                    } else {
                        // 未找到，进商机池
                        allocateEntity.setAllocateStatus(AllocateEnum.AVATAR_POOL);
                        customerDTO.setOwnerUserId(-1); // 标记是商机池，-1
                    }
                } else {
                    // 进客服系统
                    allocateEntity.setAllocateStatus(AllocateEnum.TO_SERVICE_SYSTEM);
                }
            } else {
                // 老客户

                // 规则：
                // 1、根据ocdc的传的特殊标记找业务员，找打就分给该业务员
                // 2、在有效城市范围内（根据城市queue找一级吧，再根据一级吧分配queue找业务员）找业务员
                // 2.1、找到，分配给业务员
                // 2.2、未找到、进待分配池
                // 3、进客户系统

                UserInfoDTO ownerUser = this.getOwnerUser(customerDTO, token); // 获取负责人
                //boolean allocateToPublic = this.isAllocateToPublic(customerDTO.getUtmSource()); // 根据渠道，判断是否需要自动分配

                if (StringUtils.isNotEmpty(ownerUser.getUser_id())) { // 存在这个在职负责人

                        needDataBox.put(salesmanIdKey, ownerUser.getSub_company_id());
                        needDataBox.put(salesmanIdKey, ownerUser.getUser_id());
                        customerDTO.setOwnerUserId((Integer) needDataBox.get(salesmanIdKey));
                        allocateEntity.setAllocateStatus(AllocateEnum.EXIST_OWNER);
                } else if (StringUtils.isNotEmpty(customerDTO.getCity()) && StringUtils.contains(allocateCities, customerDTO.getCity())) { // 在有效分配城市内

                    // 根据城市，去找一级吧下业务员
                    if (this.getAllocateUserV2(token, customerDTO.getCity(), currentMonthStr, needDataBox, baseChannelDTO.getMedia_id())) { //找到业务员
                        allocateEntity.setAllocateStatus(AllocateEnum.ALLOCATE_TO_OWNER);
                        customerDTO.setOwnerUserId((Integer) needDataBox.get(salesmanIdKey));
                    } else { // 未找到，进入待分配池
                        customerDTO.setOwnerUserId(0);
                        allocateEntity.setAllocateStatus(AllocateEnum.WAITING_POOL);
                    }
                } else {
                    customerDTO.setOwnerUserId(0);
                    allocateEntity.setAllocateStatus(AllocateEnum.TO_SERVICE_SYSTEM);
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
                            if (allocateEntity.getAllocateStatus().getCode().equals("3")||allocateEntity.getAllocateStatus().getCode().equals("1")){
                                if (this.isActiveApplicationChannel(customerDTO)) {
                                    customerDTO.setRemain(CommonConst.REMAIN_STATUS_YES);
                                }
                            }

                            // 新建用户信息
                            CronusDto cronusDto1 = customerInfoService.addOcdcCustomer(customerDTO, token);
                            if (cronusDto1.getResult() == 0) {
                                customerDTO.setId(Integer.parseInt(cronusDto1.getData().toString()));
                                if (isNewCustomerFromAvatar) {
                                    // 新客户已找到业务员，记录分配数
                                    this.userMonthInfoService.incrNum2DBForOCDCPush((Integer) needDataBox.get(companyIdKey), baseChannelDTO, (Integer) needDataBox.get(salesmanIdKey), currentMonthStr, customerDTO);
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
//                    String[] cityStrArrayAll = StringUtils.split(allocateCities, ",");
//                    if (ArrayUtils.contains(cityStrArrayAll, customerDTO.getCity())) {
                    if (allocateCities.contains(customerDTO.getCity())) {
                        allocateRedisService.changeAllocateTemplet(customerDTO.getOwnerUserId(), customerDTO.getCity());
                    }
                    // 添加分配日志
                    CustomerInfo customerInfo = new CustomerInfo();
                    EntityToDto.customerCustomerDtoToEntity(customerDTO, customerInfo);
                    allocateLogService.autoAllocateAddAllocatelog(customerInfo.getId(), customerDTO.getOwnerUserId(),
                            CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCode());

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
            allocateEntity.setDescription("自动分配失败: errorMessage=" + e.getMessage() + " telephonenumber=" + customerDTO.getTelephonenumber() + " ocdcId=" + customerDTO.getOcdcId());
            allocateEntity.setSuccess(false);
        }
        return allocateEntity;
    }

    /**
     * 商机系统分配规则.
     */
    private Boolean allocateForAvatar(String token, CustomerDTO customerDTO, BaseChannelDTO baseChannelDTO, Map<String, Object> needDataBox, String currentMonthStr) {
        // 商机分配规则（前提：新客户、在有效城市范围内）
        // 1、 根据城市，从城市queue中获取一级吧
        // 2、 要求该一级吧媒体的订购数（商机系统获取） > 已购数
        // 3、 从该媒体的业务员分配queue中，找业务员
        // 4、 要求业务员 分配数 > 已购数
        // 5、 如4不满足，则从总queue中，找业务员
        // 5、 要求业务员 分配数 > 已购数
        // 6、 最终要么为客户找到业务员，要么进入待分配池

        Integer media_id = baseChannelDTO.getMedia_id(); // 媒体id
        boolean isFindSuccess = false; // 找到接待的业务员

        Integer subCompanyId = null; // 要找的一级吧
        Integer salesmanId = null;   // 要找的业务员

        Set<Integer> existCompanyid = new HashSet<>();
        while(!existCompanyid.contains(subCompanyId)) {
            // 循环城市下一级吧queue

            // 从queue获取一级吧
            subCompanyId = this.allocateRedisService.getSubCompanyIdFromQueue(token, customerDTO.getCity(), media_id);
            if (subCompanyId == null) {
                // queue中无一级吧，城市下无一级吧
                break;
            }

            // 获取当月已分配数
            Integer orderNumOfCompany = userMonthInfoMapper.getOrderNum(subCompanyId, currentMonthStr, CommonEnum.entity_status1.getCode());
            orderNumOfCompany = orderNumOfCompany == null ? 0 : orderNumOfCompany;

            // 从商机系统获取
            JSONObject json = new JSONObject();
            json.put("firstBarId", subCompanyId);
            json.put("month", allocateRedisService.getMonthStr4avatar(currentMonthStr));
            AvatarApiDTO<OrderNumberDTO> orderNumberDTOAvatarApiDTO = this.avatarClientService.queryOrderNumber(token, json);
            if (orderNumberDTOAvatarApiDTO == null || orderNumberDTOAvatarApiDTO.getResult() != 0) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "请求商机系统异常：响应为null");
            }
            if (orderNumberDTOAvatarApiDTO.getResult() != 0) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "请求商机系统异常:" + " result:" +orderNumberDTOAvatarApiDTO.getResult() + " message:" + orderNumberDTOAvatarApiDTO.getMessage());
            }
            OrderNumberDTO data = orderNumberDTOAvatarApiDTO.getData();
            if (data == null || CollectionUtils.isEmpty(data.getOrderNumberList())) {
                // 响应正常，但无数据视为未订购
                existCompanyid.add(subCompanyId);
                continue;
            }
            List<OrderNumberDetailDTO> orderNumberList = data.getOrderNumberList();
            Integer orderNumber = orderNumberList.stream().filter(i -> i != null && media_id.equals(i.getMeidaId())).map(OrderNumberDetailDTO::getOrderNumber).findAny().orElse(0);

            // 业务校验：一级吧已购数、订购数
            if (orderNumOfCompany >= orderNumber) {
                // 已购数 >= 订购数
                existCompanyid.add(subCompanyId);
                continue;
            }

            String saleFlag = null; // 由于业务员可以在不同媒体队列中出现，所以需要联合媒体id一起作为标记；结构：mediaid$saleid
            Set<String> existSale = new HashSet<>();
            while(!existSale.contains(saleFlag)){
                // 业务员queue

                boolean idFromCountQueue = false; // 记录业务员是从特殊媒体queue取出，还是总queue中取出.

                // 找业务员
                salesmanId = this.allocateRedisService.getAndPush2End(subCompanyId, media_id, currentMonthStr);
                if (salesmanId == null) {
                    // 业务需求，特殊queue中找不到就去总队列中找
                    salesmanId = this.allocateRedisService.getAndPush2End(subCompanyId, CommonConst.COMPANY_MEDIA_QUEUE_COUNT, currentMonthStr);
                    idFromCountQueue = true;
                }
                if (salesmanId == null){
                    // 特殊队列、总队列都没业务员
                    break;
                }

                Integer temp = idFromCountQueue ? CommonConst.COMPANY_MEDIA_QUEUE_COUNT : media_id;

                // 比较该业务员的 已购数据、分配数
                UserMonthInfo e = new UserMonthInfo();
                e.setCompanyid(subCompanyId);
                e.setUserId(salesmanId);
                e.setMediaid(temp);
                e.setEffectiveDate(currentMonthStr);
                e.setStatus(CommonEnum.entity_status1.getCode());
                List<UserMonthInfo> select = userMonthInfoMapper.select(e);
                if (CollectionUtils.isEmpty(select)
                        ) {
                    // 数据错误，直接忽略，给下一个业务员处理
                    existSale.add(temp + "$" + salesmanId);
                    continue;
                }

                UserMonthInfo userMonthInfo = select.get(0);
                if (userMonthInfo == null
                        || userMonthInfo.getBaseCustomerNum() == null
                        || userMonthInfo.getRewardCustomerNum() == null
                        || userMonthInfo.getAssignedCustomerNum() == null
                        ) {
                    // 数据错误，直接忽略，给下一个业务员处理
                    existSale.add(temp + "$" + salesmanId);
                    continue;
                }

                if (userMonthInfo.getAssignedCustomerNum() >=  (userMonthInfo.getBaseCustomerNum() + userMonthInfo.getRewardCustomerNum()) ) {
                    // 该业务员 已购数 >= 分配数
                    existSale.add(temp + "$" + salesmanId);
                    continue;
                }

                // 该客户由该业务员接待
                isFindSuccess = true;
                break;
            }

            if (isFindSuccess) {
                // 已找到接待的业务员
                needDataBox.put(companyIdKey, subCompanyId);
                needDataBox.put(salesmanIdKey, salesmanId);
                break;
            }
        }

        return isFindSuccess;
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

    private boolean getAllocateUserV2(String token, String city, String currentMonthStr, Map<String, Object> needDataBox, Integer mediaid) {

        // 该客户由该业务员接待
        boolean isFindSuccess = false;

        Integer subCompanyId = null; // 要找的一级吧
        Integer salesmanId = null;   // 要找的业务员

        Set<Integer> existCompanyid = new HashSet<>();
        while(!existCompanyid.contains(subCompanyId)) {
            // 从城市queue获取一级吧

            subCompanyId = this.allocateRedisService.getSubCompanyIdFromQueue(token, city, mediaid);
            if (subCompanyId == null) break;

            // 从一级吧下总队列中找业务员
            salesmanId = this.allocateRedisService.getAndPush2End(subCompanyId, CommonConst.COMPANY_MEDIA_QUEUE_COUNT, currentMonthStr);
            if (salesmanId != null) {
                // 找到业务员
                needDataBox.put(companyIdKey, subCompanyId);
                needDataBox.put(salesmanIdKey, salesmanId);
                isFindSuccess = true;
                break;
            }
            existCompanyid.add(subCompanyId);
        }
        return isFindSuccess;
    }

    /**
     * 根据城市获取分配队列信息
     *
     * @param city
     */
    @Deprecated
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
