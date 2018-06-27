package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.entity.EzucDataDetail;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.EzucDataDetailMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class EzucDataDetailService {

    @Autowired
    private EzucDataDetailMapper ezucDataDetailMapper;

    @Resource
    RedisTemplate redisTemplateOps;

    /**
     * 根据业务员名称，获取昨天的通话时长.
     *
     * @param name 业务员名
     *             @param date 指定的哪天的数据；null为取昨天
     */
    public long getDurationByName(String name, Date date){
        if (StringUtils.isBlank(name)) {
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "业务员名称不能为空");
        }
        name = name.trim();

        // 缓存中取
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date startTime = getStartTime(date);
        String key = CommonRedisConst.EZUC_DURATION_KEY + sdf.format(startTime);
        HashOperations<String, String, Long> hashOperations = redisTemplateOps.opsForHash();
        Number temp = hashOperations.get(key, name);
        if (temp != null) {
            return temp.longValue();
        }

        Long duration = null;
        // db中查
        duration = ezucDataDetailMapper.getDurationByName(name, startTime.getTime() / 1000, getEndTime(date).getTime() / 1000, CommonEnum.entity_status1.getCode());
        duration = duration == null ? 0 : duration;

        return duration;
    }

    public Map<String, Long> findAllFromCacheByDate(Date date){
        if (date == null) {
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "date 不能为null");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String key = CommonRedisConst.EZUC_DURATION_KEY + sdf.format(date);
        HashOperations<String, String, Long> hashOperations = redisTemplateOps.opsForHash();
        Map<String, Long> result = hashOperations.entries(key);

        return result;
    }

    /**
     * 刷新缓存.
     */
    public void refreshCache(Date date){
        Date startTime = getStartTime(date);
        List<EzucDataDetail> allDuration = ezucDataDetailMapper.findAllDuration(startTime.getTime() / 1000, getEndTime(date).getTime() / 1000, CommonEnum.entity_status1.getCode());
        allDuration = CollectionUtils.isEmpty(allDuration) ? new ArrayList() : allDuration;
        Map<String, Long> nameMappingDuration = allDuration.stream()
                .filter(item -> item != null && StringUtils.isNotBlank(item.getCallerDispName()) && item.getDuration() != null)
                .collect(Collectors.toMap(EzucDataDetail::getCallerDispName, EzucDataDetail::getDuration, (x, y) -> x));

        if (nameMappingDuration != null && nameMappingDuration.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String key = CommonRedisConst.EZUC_DURATION_KEY + sdf.format(startTime);
            HashOperations<String, String, Long> hashOperations = redisTemplateOps.opsForHash();
            hashOperations.putAll(key, nameMappingDuration);
            redisTemplateOps.expire(key, 2, TimeUnit.DAYS);
        }
    }

    /**
     * 获取url中请求的endtime参数.
     */
    private Date getEndTime(Date date) {
        Calendar now = Calendar.getInstance();
        if (date != null) {
            // 空就使用当前系统时间
            now.setTime(date);
        } else {
            now.add(Calendar.DAY_OF_MONTH, -1);
        }
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.MILLISECOND, 59);

        return now.getTime();
    }

    /**
     * 获取url中请求的startTime参数.
     */
    public Date getStartTime(Date date) {
        Calendar now = Calendar.getInstance();
        if (date != null) {
            // 空就使用当前系统时间
            now.setTime(date);
        } else {
            now.add(Calendar.DAY_OF_MONTH, -1);
        }
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.MILLISECOND, 0);

        return now.getTime();
    }

    /**
     * 手动添加某业务员某日通话时长，注意，是测试接口,用于开发测试使用.
     */
    public void addSingleData(String name, Date time, Long duration) {

        EzucDataDetail temp = new EzucDataDetail();
        temp.setCallerDispName(name);
        temp.setStartTime(time.getTime() / 1000);
        ezucDataDetailMapper.delete(temp);

        EzucDataDetail recode = new EzucDataDetail();
        recode.setStatus(CommonEnum.entity_status1.getCode());
        recode.setCreated(new Date());
        recode.setCallerDispName(name);
        recode.setDuration(duration * 60);
        long l = time.getTime() / 1000;
        recode.setStartTime(l);

        JSONObject map = new JSONObject();
        map.put("后台手动插入", "非定时跑批产生数据");
        recode.setData(map.toString());
        ezucDataDetailMapper.insertSelective(recode);

        refreshCache(time);
    }
}
