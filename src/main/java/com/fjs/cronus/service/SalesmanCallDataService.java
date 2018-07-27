package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.PHPUserDto;
import com.fjs.cronus.dto.api.ThorApiDTO;
import com.fjs.cronus.dto.uc.CronusUserInfoDto;
import com.fjs.cronus.dto.uc.LightUserInfoDTO;
import com.fjs.cronus.dto.uc.ThorQueryDto;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.entity.SalesmanCallData;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerMeetMapper;
import com.fjs.cronus.mappers.SalesmanCallDataMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerMeet;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DEC3Util;
import com.google.common.base.Joiner;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toSet;

@Service
public class SalesmanCallDataService {

    private static final Logger logger = LoggerFactory.getLogger(SalesmanCallDataService.class);

    @Value("${token.current}")
    private String publicToken;

    @Autowired
    private SalesmanCallDataMapper salesmanCallDataMapper;

    @Autowired
    private CustomerInfoService customerInfoService;

    @Autowired
    private EzucDataDetailService ezucDataDetailService;

    @Autowired
    private CustomerMeetMapper customerMeetMapper;

    @Autowired
    private ThorService thorService;

    @Autowired
    private SalesmanCallTimeService salesmanCallTimeService;
    @Autowired
    private SalesmanCallNumService salesmanCallNumService;
    @Autowired
    private SalesmanMeetNumService salesmanMeetNumService;
    @Autowired
    private TheaClientService theaClientService;

    @Autowired
    private UcService ucService;

    @Resource
    private RedisTemplate redisTemplateOps;

    /**
     * 暴露服务给b端Android接口，增加数据.
     */
    public void addSingle(String token,
                          Long salesManId,
                          Long customerid,
                          Long startTime,
                          Long answerTime,
                          Long endTime,
                          Long duration,
                          Long totalDuration,
                          Integer callType,
                          String recordingUrl,
                          Integer systype,
                          Long customerPhoneNum
    ) {

        // 参数校验
        if (StringUtils.isBlank(token)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "token 不能为空");
        }
        if (salesManId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "salesManId 不能为空");
        }
        if (customerid == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "customerid 不能为空");
        }
        if (startTime == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "startTime 不能为空");
        }
        if (answerTime == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "answerTime 不能为空");
        }
        if (endTime == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "endTime 不能为空");
        }
        if (duration == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "duration 不能为空");
        }
        if (totalDuration == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "totalDuration 不能为空");
        }
        if (callType == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "callType 不能为空");
        }
        if (systype == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "systype 不能为空");
        }
        if (customerPhoneNum == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "customerPhoneNum 不能为空");
        }

        // 准备数据
        CronusDto<UserInfoDTO> userInfoByToken = thorService.getUserInfoByToken(token, null);
        if (userInfoByToken == null
                || userInfoByToken.getResult() != 0
                || userInfoByToken.getData() == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "请求Thor服务，服务异常");
        }
        if (StringUtils.isBlank(userInfoByToken.getData().getSub_company_id())) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "请求Thor服务，数据异常。一级吧为空");
        }
        if (StringUtils.isBlank(userInfoByToken.getData().getName())) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "请求Thor服务，数据异常。业务员名称为空");
        }

        String salesManName = userInfoByToken.getData().getName();
        Long subCompanyId = Long.valueOf(userInfoByToken.getData().getSub_company_id());

        CustomerInfo customerInfo = customerInfoService.findCustomerById(customerid.intValue()); // 获取顾客信息
        if (customerInfo == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "客户不存在");
        }
        if (StringUtils.isBlank(customerInfo.getTelephonenumber())) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "数据异常，客户无手机号");
        }
        String customerPhoneNumTemp = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
        if (!customerPhoneNumTemp.trim().equalsIgnoreCase(customerPhoneNum.toString())) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "数据异常，参数中手机号与数据库中手机号不匹配");
        }

        // 去重:同一个业务员，同时间则视为重复记录
        SalesmanCallData i2 = new SalesmanCallData();
        i2.setSalesManId(salesManId);
        i2.setStartTime(startTime);
        i2.setStatus(CommonEnum.entity_status1.getCode());
        int i3 = salesmanCallDataMapper.selectCount(i2);
        if (i3 > 0) return;

        // 数据入库
        SalesmanCallData data = new SalesmanCallData();
        data.setSalesManId(salesManId);
        data.setSalesManName(salesManName);
        data.setSubCompanyid(subCompanyId);
        data.setCustomerid(customerid);
        data.setCustomerPhoneNum(customerPhoneNumTemp);
        data.setStartTime(startTime / 1000);
        data.setAnswerTime(answerTime / 1000);
        data.setEndTime(endTime / 1000);
        data.setDuration(duration);
        data.setTotalDuration(totalDuration);
        data.setCallType(callType);
        data.setRecordingUrl(recordingUrl);
        data.setSystype(systype);
        data.setCreated(new Date());
        data.setCreateid(salesManId);

        salesmanCallDataMapper.insertSelective(data);

        // 统计通话时长、通话次数
        new Thread(() -> {
            countData(subCompanyId, salesManId, salesManName);
        }).start();
    }

    /**
     * 统计通话时长、通话次数.
     */
    private void countData(Long subCompanyId, Long salesManId, String salesManName) {

        // 通话时长
        salesmanCallTimeService.countData(subCompanyId, salesManId, salesManName.trim());
        salesmanCallTimeService.reflushCache(subCompanyId, salesManId, salesManName.trim(), new Date());

        // 通话次数
        salesmanCallNumService.countData(subCompanyId, salesManId, salesManName.trim());
        salesmanCallNumService.reflushCache(subCompanyId, salesManId, salesManName.trim(), new Date());
    }

    /**
     * 刷新缓存.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Deprecated
    public void refreshCache(Date date) {
/*
        Date startTime = ezucDataDetailService.getStartTime(date);
        List<SalesmanCallData> allDuration = salesmanCallDataMapper.findAllDuration(startTime.getTime() / 1000, ezucDataDetailService.getEndTime(date).getTime() / 1000, CommonEnum.entity_status1.getCode());
        allDuration = CollectionUtils.isEmpty(allDuration) ? new ArrayList() : allDuration;
        Map<String, Long> nameMappingDuration = allDuration.stream()
                .filter(item -> item != null && StringUtils.isNotBlank(item.getSalesManName()) && item.getDuration() != null)
                .collect(toMap(SalesmanCallData::getSalesManName, SalesmanCallData::getDuration, (x, y) -> x));

        if (nameMappingDuration != null && nameMappingDuration.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String key = CommonRedisConst.EZUC_DURATION_KEY + sdf.format(startTime);
            HashOperations<String, String, Long> hashOperations = redisTemplateOps.opsForHash();
            hashOperations.putAll(key, nameMappingDuration);
            redisTemplateOps.expire(key, 2, TimeUnit.DAYS);
        }*/
    }

    /**
     * 定时任务，插入单条数据.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addSigle4Qurtz(JSONObject jsonObject) {

        try {
            String callerDispName = jsonObject.getString("callerDispName");
            if (StringUtils.isBlank(callerDispName)) {
                // 无业务员名称数据，不录入系统
                return;
            }
            callerDispName = callerDispName.trim();
            Long startTime = jsonObject.getLong("startTime");

            Boolean isexist = null;
            if (StringUtils.isNotBlank(callerDispName) && startTime != null) {
                SalesmanCallData i2 = new SalesmanCallData();
                i2.setSalesManName(callerDispName);
                i2.setStartTime(startTime);
                i2.setStatus(CommonEnum.entity_status1.getCode());
                int i3 = salesmanCallDataMapper.selectCount(i2);
                if (i3 > 0) {
                    isexist = true; // 已存在的记录，不录入系统
                } else {
                    isexist = false;
                }
            }

            if (isexist == null || !isexist) {
                // 记录到 salesman_call_data 表
                SalesmanCallData data = new SalesmanCallData();
                data.setSalesManName(callerDispName);
                data.setCustomerPhoneNum(jsonObject.getString("calleeExt"));
                data.setStartTime(startTime);
                data.setAnswerTime(jsonObject.getLong("answerTime"));
                data.setEndTime(jsonObject.getLong("endTime"));
                data.setDuration(jsonObject.getLong("duration"));
                data.setTotalDuration(jsonObject.getLong("totalDuration"));
                data.setCallType(0);
                data.setRecordingUrl(jsonObject.getString("recordingUrl"));
                data.setSystype(CommonConst.SYSTYPE_EZUC);
                data.setCreated(new Date());
                salesmanCallDataMapper.insertSelective(data);
            }
        } catch (Exception e) {
            logger.error("定时任务，插入单条数据", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

    }

    /**
     * 根据业务员名称，获取昨天的通话时长.
     *
     * @param name 业务员名
     * @param date 指定的哪天的数据；null为取昨天
     */
    @Deprecated
    public long getDurationByName(String name, Date date) {
/*
        if (StringUtils.isBlank(name)) {
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "业务员名称不能为空");
        }
        name = name.trim();

        // 缓存中取
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date startTime = ezucDataDetailService.getStartTime(date);
        String key = CommonRedisConst.EZUC_DURATION_KEY + sdf.format(startTime);
        HashOperations<String, String, Long> hashOperations = redisTemplateOps.opsForHash();
        Number temp = hashOperations.get(key, name);
        if (temp != null) {
            return temp.longValue();
        }

        Long duration = null;
        // db中查
        duration = salesmanCallDataMapper.getDurationByName(name, startTime.getTime() / 1000, ezucDataDetailService.getEndTime(date).getTime() / 1000, CommonEnum.entity_status1.getCode());
        duration = duration == null ? 0 : duration;

        return duration;*/
        return 0;
    }

    /**
     * 获取指定业务员，指定时间面见次数.
     */
    @Deprecated
    public int getMeetingCount(Long salesmanId, Date start, Date end) {

        /*HashOperations<String, Long, Number> hashOperations = redisTemplateOps.opsForHash();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String key = CommonRedisConst.EZUC_DURATION_MEETINGCOUNT.concat(sdf.format(start));
        Number number = hashOperations.get(key, salesmanId);
        if (number == null) {
            number = customerMeetMapper.getCountCustomerIdByCreateId(salesmanId.intValue(), start, end); // 获取面见次数
            number = number == null ? 0 : number;

            hashOperations.put(key, salesmanId, number);
        }
        return number.intValue();*/

        return 0;
    }

    /**
     * 获取今日通话数据.
     */
    public Map<String, Object> getSaleManCallData(String token, Long userId, String type) {

        CronusDto<UserInfoDTO> userInfoByToken = thorService.getUserInfoByToken(token, null);
        String salemanName = userInfoByToken.getData().getName();

        if ("day".equalsIgnoreCase(type)) {
            return getSaleManCallData4Day(token, userId, salemanName.trim());
        } else if ("week".equalsIgnoreCase(type)) {
            return getSaleManCallData4Week(token, userId, salemanName.trim());
        } else if ("month".equalsIgnoreCase(type)) {
            return getSaleManCallData4Month(token, userId, salemanName.trim());
        }
        return null;
    }

    /**
     * 日统计.
     */
    private Map<String, Object> getSaleManCallData4Day(String token, Long userId, String salemanName) {
        Map<String, Object> result = new HashMap<>();

        // 通话限制
        String tt = theaClientService.getConfigByName(CommonConst.SALESMAN_CALL_TIME_LIMIT);
        long t = Integer.valueOf(tt) * 60;

        result.put("callTimeLimit", t);

        // 截止同步数据时间
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        result.put("lastSyncTime", instance.getTime().getTime());

        // 通话时长
        result.put("currTime", salesmanCallTimeService.getCallTimeOfNow(salemanName));

        // 昨日通话时长
        result.put("perTime", salesmanCallTimeService.getCallTimeOfYesterday(salemanName));

        // 今日通话次数
        result.put("currNum", salesmanCallNumService.getCallNumOfNow(salemanName));

        // 昨日通话次数
        result.put("perNum", salesmanCallNumService.getCallNumOfYesterday(salemanName));

        // 今日面见次数
        result.put("currMeetNum", salesmanMeetNumService.getMeetNumOfNow(userId, salemanName));

        // 昨日面见次数
        result.put("perMeetNum", salesmanMeetNumService.getMeetNumOfYestday(userId, salemanName));

        return result;
    }

    /**
     * 周统计.
     */
    private Map<String, Object> getSaleManCallData4Week(String token, Long userId, String salemanName) {
        Map<String, Object> result = new HashMap<>();

        // 本周通话时长
        result.put("currTime", salesmanCallTimeService.getCallTimeOfCurrWeek(salemanName));

        // 上周通话时长
        result.put("perTime", salesmanCallTimeService.getCallTimeOfPreWeek(salemanName));

        // 本周通话次数
        result.put("currNum", salesmanCallNumService.getCallNumOfCurrWeek(salemanName));

        // 上周通话次数
        result.put("perNum", salesmanCallNumService.getCallNumOfPreWeek(salemanName));

        // 本周面见次数
        result.put("currMeetNum", salesmanMeetNumService.getMeetNumOfCurrWeek(salemanName));

        // 上周面见次数
        result.put("perMeetNum", salesmanMeetNumService.getMeetNumOfPreWeek(salemanName));

        return result;
    }

    /**
     * 月统计.
     */
    private Map<String, Object> getSaleManCallData4Month(String token, Long userId, String salemanName) {
        Map<String, Object> result = new HashMap<>();

        // 本月通话时长
        result.put("currTime", salesmanCallTimeService.getCallTimeOfCurrMonth(salemanName));

        // 上月通话时长
        result.put("perTime", salesmanCallTimeService.getCallTimeOfPreMonth(salemanName));

        // 本月通话次数
        result.put("currNum", salesmanCallNumService.getCallNumOfCurrMonth(salemanName));

        // 上月通话次数
        result.put("perNum", salesmanCallNumService.getCallNumOfPreMonth(salemanName));

        // 本月面见次数
        result.put("currMeetNum", salesmanMeetNumService.getMeetNumOfCurrMonth(salemanName));

        // 上月面见次数
        result.put("perMeetNum", salesmanMeetNumService.getMeetNumOfPreMonth(salemanName));

        return result;
    }

    /**
     * 团队数据，获取指定条件的数据.
     */
    public List<Map<String, Object>> findSaleManCallData(String token, Long userId, String salesmanName, Integer departmentId, Boolean finish) {
        List<Map<String, Object>> result = new ArrayList<>();

        // 获取下属
        List<Integer> ids = ucService.getSubUserByUserId(token, userId.intValue());
        if (CollectionUtils.isEmpty(ids)) return result;

        Joiner joiner = Joiner.on(",");
        String join = joiner.join(ids);

        // 筛选，获取有效下属
        List<PHPUserDto> myUsers = new ArrayList<>();
        CronusUserInfoDto cronusUserInfoDto = new CronusUserInfoDto();
        cronusUserInfoDto.setUser_ids(join);
        ThorQueryDto<List<PHPUserDto>> userByIds = thorService.getUserByIds(token, cronusUserInfoDto);
        if (userByIds != null && userByIds.getRetData() != null) {
            for (PHPUserDto p : userByIds.getRetData()) {
                if (StringUtils.isNotBlank(p.getName()) && StringUtils.isNotBlank(p.getUser_id()) ) {
                    if (StringUtils.isNotBlank(salesmanName) && !salesmanName.equalsIgnoreCase(p.getName())) {
                        continue;
                    }
                    if (departmentId != null && !departmentId.equals(p.getDepartment_id())) {
                        continue;
                    }
                    myUsers.add(p);
                }
            }
        }

        String tt = theaClientService.getConfigByName(CommonConst.SALESMAN_CALL_TIME_LIMIT);
        long t = Integer.valueOf(tt) * 60;
        for (PHPUserDto e : myUsers) {
            long callTimeOfNow = salesmanCallTimeService.getCallTimeOfNow(e.getName());
            if (finish != null) {
                if (finish && callTimeOfNow > t) {
                    // 大于限制的值
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("salesmanName", e.getName());
                    temp.put("departmen", e.getDepartment_name());
                    temp.put("todayCallTime", callTimeOfNow);
                    temp.put("todayCallNum", salesmanCallNumService.getCallNumOfNow(e.getName()));
                    temp.put("weekCallTime", salesmanCallTimeService.getCallTimeOfCurrWeek(e.getName()));
                    temp.put("weekCallNum", salesmanCallNumService.getCallNumOfCurrWeek(e.getName()));
                    temp.put("todayMeetNum", salesmanMeetNumService.getMeetNumOfNow(Long.valueOf(e.getUser_id()), e.getName()));
                    temp.put("callTimeLimit", t);
                    result.add(temp);
                }
                if (!finish && callTimeOfNow < t) {
                    // 小于限制的值
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("salesmanName", e.getName());
                    temp.put("departmen", e.getDepartment_name());
                    temp.put("todayCallTime", callTimeOfNow);
                    temp.put("todayCallNum", salesmanCallNumService.getCallNumOfNow(e.getName()));
                    temp.put("weekCallNum", salesmanCallNumService.getCallNumOfCurrWeek(e.getName()));
                    temp.put("weekCallTime", salesmanCallTimeService.getCallTimeOfCurrWeek(e.getName()));
                    temp.put("todayMeetNum", salesmanMeetNumService.getMeetNumOfNow(Long.valueOf(e.getUser_id()), e.getName()));
                    temp.put("callTimeLimit", t);
                    result.add(temp);
                }
            } else {
                Map<String, Object> temp = new HashMap<>();
                temp.put("salesmanName", e.getName());
                temp.put("departmen", e.getDepartment_name());
                temp.put("todayCallTime", callTimeOfNow);
                temp.put("todayCallNum", salesmanCallNumService.getCallNumOfNow(e.getName()));
                temp.put("weekCallTime", salesmanCallTimeService.getCallTimeOfCurrWeek(e.getName()));
                temp.put("todayMeetNum", salesmanMeetNumService.getMeetNumOfNow(Long.valueOf(e.getUser_id()), e.getName()));
                temp.put("callTimeLimit", t);
                temp.put("weekCallNum", salesmanCallNumService.getCallNumOfCurrWeek(e.getName()));
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * 非业务接口-管理接口:
     * <p>
     * 数据同步：由于是新增功能，导致新增表，需要拉下数据到新表中.
     */
    public String initSyncData(String type, Date date, String token) {

        ValueOperations operations = redisTemplateOps.opsForValue();
        String key = "salesmanCall_initdata_lock";

        Object o = operations.get(key);
        if (o != null) {
            return "被锁，正计算中";
        }
        try {
            operations.set(key, 1);
            redisTemplateOps.expire(key, 1, TimeUnit.MINUTES);
            Date now = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 得到该月总天数
            Calendar a = Calendar.getInstance();
            a.setTime(date);
            a.set(Calendar.DATE, 1);//把日期设置为当月第一天
            a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            int maxDate = a.get(Calendar.DATE);

            // 循环同步该月每天数据
            for (int i = 1; i <= maxDate; i++) {
                Calendar start = Calendar.getInstance();
                start.setTime(date);
                start.set(Calendar.DATE, i);
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);

                Calendar end = Calendar.getInstance();
                end.setTime(date);
                end.set(Calendar.DATE, i);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);

                if ("day".equalsIgnoreCase(type)) {
                    // 如果指定同步哪天数据
                    Calendar temp = Calendar.getInstance();
                    temp.setTime(date);
                    int i1 = temp.get(Calendar.DATE);
                    if (i1 != i) {
                        continue;
                    }
                }

                // init通话时长
                List<SalesmanCallData> allDuration = salesmanCallDataMapper.findAllDuration(start.getTime().getTime() / 1000, end.getTime().getTime() / 1000, CommonEnum.entity_status1.getCode());
                if (!CollectionUtils.isEmpty(allDuration)) {
                    for (SalesmanCallData salesmanCallData : allDuration) {
                        if (salesmanCallData != null && StringUtils.isNotBlank(salesmanCallData.getSalesManName()) && salesmanCallData.getDuration() != null) {
                            salesmanCallTimeService.addSingle4Qurtz(salesmanCallData.getSalesManName(), salesmanCallData.getDuration(), start.getTime(), now);
                        }
                    }
                }

                // init通话次数
                List<SalesmanCallData> allNum = salesmanCallDataMapper.findAllNum(start.getTime().getTime() / 1000, end.getTime().getTime() / 1000, CommonEnum.entity_status1.getCode());
                if (!CollectionUtils.isEmpty(allNum)) {
                    for (SalesmanCallData salesmanCallData : allNum) {
                        if (salesmanCallData != null && StringUtils.isNotBlank(salesmanCallData.getSalesManName()) && salesmanCallData.getDuration() != null) {
                            salesmanCallNumService.addSingle4Qurtz(salesmanCallData.getSalesManName(), salesmanCallData.getDuration().intValue(), start.getTime(), now);
                        }
                    }
                }

                // init面见次数
                List<CustomerMeet> list = customerMeetMapper.findBydTime(start.getTime(), end.getTime());
                list = CollectionUtils.isEmpty(list) ? new ArrayList<>() : list;

                Set<Integer> salemanIds = list.stream().filter(item -> item != null && item.getCreateUser() != null).map(CustomerMeet::getCreateUser).collect(toSet());
                if (!CollectionUtils.isEmpty(salemanIds)) {
                    Joiner joiner = Joiner.on(",");
                    String join = joiner.join(salemanIds);

                    if (StringUtils.isNotBlank(join)) {
                        CronusUserInfoDto cronusUserInfoDto = new CronusUserInfoDto();
                        cronusUserInfoDto.setUser_ids(join);
                        ThorQueryDto<List<PHPUserDto>> userByIds = thorService.getUserByIds(token, cronusUserInfoDto);
                        if (userByIds != null && userByIds.getRetData() != null) {
                            for (PHPUserDto p : userByIds.getRetData()) {
                                String name = p.getName();
                                String sub_company_id = p.getSub_company_id();
                                String user_id = p.getUser_id();
                                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(sub_company_id) && StringUtils.isNotBlank(user_id)) {
                                    salesmanMeetNumService.countData(Long.valueOf(sub_company_id), Long.valueOf(user_id), name, start.getTime());
                                }
                            }
                        }
                    }
                }

                // 通话时长：构建今日缓存数据
                salesmanCallTimeService.buildDayCatch(start.getTime());

                // 通话时长：构建当周缓存数据
                salesmanCallTimeService.buildWeekCatch(start.getTime());

                // 通话次数：构建今日缓存数据
                salesmanCallNumService.buildDayCatch(start.getTime());

                // 通话次数：构建当周缓存数据
                salesmanCallNumService.buildWeekCatch(start.getTime());

                // 面见次数：构建今日缓存数据
                salesmanMeetNumService.buildDayCatch(start.getTime());

                // 面见次数：构建当周缓存数据
                salesmanMeetNumService.buildWeekCatch(start.getTime());
            }

            // 通话时长：构建当月缓存数据
            salesmanCallTimeService.buildMonthCatch(date);

            // 通话次数：构建当月缓存数据
            salesmanCallNumService.buildMonthCatch(date);

            // 面见次数：构建当月缓存数据
            salesmanMeetNumService.buildMonthCatch(date);

        } catch (Exception e) {
            throw e;
        } finally {
            redisTemplateOps.delete(key);
        }

        return "成功";
    }

    public void rebuildCatch(String type, Date time, String token) {
        if ("day".equalsIgnoreCase(type)) {
            salesmanCallTimeService.buildDayCatch(time);
            salesmanCallNumService.buildDayCatch(time);
            salesmanMeetNumService.buildDayCatch(time);
        } else if ("week".equalsIgnoreCase(type)) {
            salesmanCallTimeService.buildWeekCatch(time);
            salesmanCallNumService.buildWeekCatch(time);
            salesmanMeetNumService.buildWeekCatch(time);
        } else if ("month".equalsIgnoreCase(type)) {
            salesmanCallTimeService.buildMonthCatch(time);
            salesmanCallNumService.buildMonthCatch(time);
            salesmanMeetNumService.buildMonthCatch(time);
        }
    }
}
