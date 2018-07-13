package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.entity.SalesmanCallData;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.mappers.CustomerMeetMapper;
import com.fjs.cronus.mappers.SalesmanCallDataMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.util.DEC3Util;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.*;

@Service
public class SalesmanCallDataService {

    @Autowired
    private SalesmanCallDataMapper salesmanCallDataMapper;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Autowired
    private ThorService thorService;

    @Autowired
    private EzucDataDetailService ezucDataDetailService;

    @Autowired
    private CustomerMeetMapper customerMeetMapper;

    @Resource
    RedisTemplate redisTemplateOps;

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

        CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerid); // 获取顾客信息
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
    }

    /**
     * 刷新缓存.
     */
    public void refreshCache(Date date) {

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
        }
    }

    /**
     * 根据业务员名称，获取昨天的通话时长.
     *
     * @param name 业务员名
     * @param date 指定的哪天的数据；null为取昨天
     */
    public long getDurationByName(String name, Date date) {

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

        return duration;
    }

    /**
     * 获取指定业务员，指定时间面见次数.
     */
    public int getMeetingCount(Long salesmanId, Date start, Date end){

        HashOperations<String, Long, Number> hashOperations = redisTemplateOps.opsForHash();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String key = CommonRedisConst.EZUC_DURATION_MEETINGCOUNT.concat(sdf.format(start));
        Number number = hashOperations.get(key, salesmanId);
        if (number == null) {
            number = customerMeetMapper.getCountCustomerIdByCreateId(salesmanId.intValue(), start, end); // 获取面见次数
            number = number == null ? 0 : number;

            hashOperations.put(key, salesmanId, number);
        }
        return number.intValue();
    }

}
