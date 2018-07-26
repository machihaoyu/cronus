package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.entity.SalesmanCallNum;
import com.fjs.cronus.entity.SalesmanCallTime;
import com.fjs.cronus.mappers.SalesmanCallDataMapper;
import com.fjs.cronus.mappers.SalesmanCallNumMapper;
import com.fjs.cronus.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class SalesmanCallNumService {

    @Resource
    private RedisTemplate redisTemplateOps;

    @Autowired
    private SalesmanCallNumMapper salesmanCallNumMapper;

    @Autowired
    private SalesmanCallDataMapper salesmanCallDataMapper;

    /**
     * 今日：通话次数.
     */
    public int getCallTimeOfNow(String salemanName) {

        // 获取缓存key
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_DAY);
        String format = sdf.format(instance.getTime()); // 获取当天
        String cacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_DAY + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.intValue();

        // 从数据库中取数据
        SalesmanCallNum record = new SalesmanCallNum();
        record.setSalesManName(salemanName);
        record.setTime(instance.getTime());
        record.setStatus(CommonEnum.entity_status1.getCode());
        SalesmanCallNum salesmanCallNum = salesmanCallNumMapper.selectOne(record);

        int result = 0;
        if (salesmanCallNum != null && salesmanCallNum.getNum() != null) {
            result = salesmanCallNum.getNum();
        }

        hash.put(cacheKey, salemanName,result);
        redisTemplateOps.expire(cacheKey, 3, TimeUnit.DAYS);

        return result;
    }

    /**
     * 昨日：通话次数.
     */
    public int getCallTimeOfYesterday(String salemanName) {

        // 获取缓存key
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_YEAR, -1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_DAY);
        String format = sdf.format(instance.getTime());
        String cacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_DAY + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.intValue();

        // 从数据库中取数据
        SalesmanCallNum record = new SalesmanCallNum();
        record.setSalesManName(salemanName);
        record.setTime(instance.getTime());
        record.setStatus(CommonEnum.entity_status1.getCode());
        SalesmanCallNum salesmanCallNum = salesmanCallNumMapper.selectOne(record);

        int result = 0;
        if (salesmanCallNum != null && salesmanCallNum.getNum() != null) {
            result = salesmanCallNum.getNum();
        }

        hash.put(cacheKey, salemanName,result);
        redisTemplateOps.expire(cacheKey, 2, TimeUnit.DAYS);

        return 0;
    }

    /**
     * 本周：通话次数.
     */
    public int getCallTimeOfCurrWeek(String salemanName) {

        // 获取缓存key
        Date now = new Date();
        Date weekStart = DateUtils.getWeekStart(now); // 获取当周周一
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_WEEK);
        String format = sdf.format(weekStart);
        String cacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_WEEK + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.intValue();

        // 从数据库中取数据
        Date weekEnd = DateUtils.getWeekEnd(now);
        Long result = salesmanCallNumMapper.getByTimeAndName(salemanName, weekStart, weekEnd, CommonEnum.entity_status1.getCode());
        result = result == null ? 0 : result;

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 15, TimeUnit.DAYS);

        return result.intValue();
    }

    /**
     * 上周：通话次数.
     */
    public int getCallTimeOfPreWeek(String salemanName) {

        // 获取缓存key
        Date now = new Date();
        Date currWeekStart = DateUtils.getWeekStart(now);

        Calendar instance = Calendar.getInstance();
        instance.setTime(currWeekStart);
        instance.add(Calendar.DAY_OF_YEAR, -7);
        Date preWeekStart = instance.getTime(); //获取上周一

        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_WEEK);
        String format = sdf.format(preWeekStart);
        String cacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_WEEK + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.intValue();

        // 从数据库中取数据
        Date preWeekEnd = DateUtils.getWeekEnd(preWeekStart);
        Long result = salesmanCallNumMapper.getByTimeAndName(salemanName, preWeekStart, preWeekEnd, CommonEnum.entity_status1.getCode());
        result = result == null ? 0 : result;

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 8, TimeUnit.DAYS);

        return result.intValue();
    }

    /**
     * 本月：通话次数
     */
    public int getCallTimeOfCurrMonth(String salemanName) {

        // 获取缓存key
        Date now = new Date();
        Date currMonthStart = DateUtils.getMonthStart(now); // 获取当月第一天

        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_MONTH);
        String format = sdf.format(currMonthStart);
        String cacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_MONTH + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.intValue();

        // 从数据库中取数据
        Date currMonthEnd = DateUtils.getMonthEnd(now);
        Long result = salesmanCallNumMapper.getByTimeAndName(salemanName, currMonthStart, currMonthEnd, CommonEnum.entity_status1.getCode());
        result = result == null ? 0 : result;

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 63, TimeUnit.DAYS);

        return result.intValue();
    }

    /**
     * 上月：通话次数
     */
    public int getCallTimeOfPreMonth(String salemanName) {

        // 获取缓存key
        Date now = new Date();
        Date currMonthStart = DateUtils.getMonthStart(now);
        Calendar instance = Calendar.getInstance();
        instance.setTime(currMonthStart);
        instance.add(Calendar.DAY_OF_YEAR, -1);
        Date preMonthStart = DateUtils.getMonthStart(instance.getTime()); // 上月第一天

        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_MONTH);
        String format = sdf.format(preMonthStart);
        String cacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_MONTH + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.intValue();

        // 从数据库中取数据
        Date preMonthEnd = DateUtils.getMonthEnd(instance.getTime());
        Long result = salesmanCallNumMapper.getByTimeAndName(salemanName, currMonthStart, preMonthEnd, CommonEnum.entity_status1.getCode());
        result = result == null ? 0 : result;

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 32, TimeUnit.DAYS);

        return result.intValue();
    }

    /**
     * 定时任务：通话次数.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addSingle4Qurtz(String salesManName, Integer num, Date date, Date now) {

        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        Date formatDate = instance.getTime(); // 格式化时间，不需要时分秒

        SalesmanCallNum e = new SalesmanCallNum();
        e.setSalesManName(salesManName);
        e.setTime(formatDate);
        e.setStatus(CommonEnum.entity_status1.getCode());

        SalesmanCallNum db = salesmanCallNumMapper.selectOne(e);
        if (db != null) {

            SalesmanCallNum recode = new SalesmanCallNum();
            recode.setNum(num);
            recode.setUpdated(now);

            Example example = new Example(SalesmanCallNum.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", db.getId());

            salesmanCallNumMapper.updateByExampleSelective(recode, example);
        } else {
            SalesmanCallNum i = new SalesmanCallNum();
            i.setCreated(now);
            i.setNum(num);
            i.setSalesManName(salesManName);
            i.setTime(formatDate);
            i.setStatus(CommonEnum.entity_status1.getCode());
            salesmanCallNumMapper.insertSelective(i);
        }
    }

    /**
     * 构建天级别缓存.
     */
    public void buildDayCatch(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        Date formatDate = instance.getTime(); // 格式化时间，不需要时分秒

        List<SalesmanCallNum> list = salesmanCallNumMapper.findByTime(formatDate, formatDate, CommonEnum.entity_status1.getCode());

        if (!CollectionUtils.isEmpty(list)) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_DAY);

            String cacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_DAY + ":" + sdf.format(formatDate);
            HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();

            Map<String, Number> map = list.stream()
                    .filter(item -> item != null && item.getNum() != null && StringUtils.isNotBlank(item.getSalesManName()))
                    .collect(Collectors.toMap(SalesmanCallNum::getSalesManName, SalesmanCallNum::getNum, (x, y) -> x));

            if (map.size() > 0) {
                hash.putAll(cacheKey, map);
                redisTemplateOps.expire(cacheKey, 3, TimeUnit.DAYS);
            }
        }
    }

    /**
     * 构建周级别缓存.
     */
    public void buildWeekCatch(Date date) {
        Date weekStart = DateUtils.getWeekStart(date); // 获取当周周一
        Date weekEnd = DateUtils.getWeekEnd(date); // 获取当周周日

        List<SalesmanCallNum> list = salesmanCallNumMapper.findByTime(weekStart, weekEnd, CommonEnum.entity_status1.getCode());

        if (!CollectionUtils.isEmpty(list)) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_WEEK);

            String cacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_WEEK + ":" + sdf.format(weekStart);
            HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();

            Map<String, Number> map = list.stream()
                    .filter(item -> item != null && item.getNum() != null && StringUtils.isNotBlank(item.getSalesManName()))
                    .collect(Collectors.toMap(SalesmanCallNum::getSalesManName, SalesmanCallNum::getNum, (x, y) -> x));

            if (map.size() > 0) {
                hash.putAll(cacheKey, map);
                redisTemplateOps.expire(cacheKey, 15, TimeUnit.DAYS);
            }
        }
    }

    /**
     * 构建月级别缓存.
     */
    public void buildMonthCatch(Date date) {
        Date currMonthStart = DateUtils.getMonthStart(date); // 获取当月第一天
        Date currMonthEnd = DateUtils.getMonthEnd(date); // 获取当月最后一天

        List<SalesmanCallNum> list = salesmanCallNumMapper.findByTime(currMonthStart, currMonthEnd, CommonEnum.entity_status1.getCode());

        if (!CollectionUtils.isEmpty(list)) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_MONTH);

            String cacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_MONTH + ":" + sdf.format(currMonthStart);
            HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();

            Map<String, Number> map = list.stream()
                    .filter(item -> item != null && item.getNum() != null && StringUtils.isNotBlank(item.getSalesManName()))
                    .collect(Collectors.toMap(SalesmanCallNum::getSalesManName, SalesmanCallNum::getNum, (x, y) -> x));

            if (map.size() > 0) {
                hash.putAll(cacheKey, map);
                redisTemplateOps.expire(cacheKey, 63, TimeUnit.DAYS);
            }
        }
    }

    /**
     * 统计数据：指定业务员.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void countData(Long subCompanyId, Long salesManId, String salesManName) {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        Date start = instance.getTime();

        Calendar instance2 = Calendar.getInstance();
        instance2.set(Calendar.MINUTE, 59);
        instance2.set(Calendar.SECOND, 59);
        Date end = instance2.getTime();

        Long duration = salesmanCallDataMapper.getNumByName(salesManName, start.getTime() / 1000, end.getTime() / 1000, CommonEnum.entity_status1.getCode());

        if (duration != null) {
            addSingle4Qurtz(salesManName, duration.intValue(), start, start);
        }
    }

    /**
     * 刷新指定业务员cache.
     */
    public void reflushCache(Long subCompanyid, Long salesmanId, String salesmanName, Date data){
        SimpleDateFormat sdfDay = new SimpleDateFormat(CommonConst.SALESMAN_CALL_DAY);
        SimpleDateFormat sdfWeek = new SimpleDateFormat(CommonConst.SALESMAN_CALL_WEEK);
        SimpleDateFormat sdfMonth = new SimpleDateFormat(CommonConst.SALESMAN_CALL_MONTH);
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();

        // 刷新今日
        Calendar instance = Calendar.getInstance();
        instance.setTime(data);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);

        Long today = salesmanCallNumMapper.getByTimeAndName(salesmanName, instance.getTime(), instance.getTime(), CommonEnum.entity_status1.getCode());
        today = today == null ? 0 : today;

        String todayCacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_DAY + ":" + sdfDay.format(instance.getTime());
        hash.put(todayCacheKey, salesmanName, today);
        redisTemplateOps.expire(todayCacheKey, 3, TimeUnit.DAYS);

        // 刷新本周
        Date weekStart = DateUtils.getWeekStart(instance.getTime()); // 获取当周周一
        Date weekEnd = DateUtils.getWeekEnd(instance.getTime());

        Long weekToday = salesmanCallNumMapper.getByTimeAndName(salesmanName, weekStart, weekEnd, CommonEnum.entity_status1.getCode());
        weekToday = weekToday == null ? 0 : weekToday;

        String weekCacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_WEEK + ":" + sdfWeek.format(weekStart);
        hash.put(weekCacheKey, salesmanName, weekToday);
        redisTemplateOps.expire(weekCacheKey, 15, TimeUnit.DAYS);


        // 刷新本月
        Date monthStart = DateUtils.getMonthStart(instance.getTime()); // 获取当周周一
        Date monthEnd = DateUtils.getMonthEnd(instance.getTime());

        Long month = salesmanCallNumMapper.getByTimeAndName(salesmanName, monthStart, monthEnd, CommonEnum.entity_status1.getCode());
        month = month == null ? 0 : month;

        String monthCacheKey = CommonRedisConst.SALES_MAN_CALL_NUM_MONTH + ":" + sdfMonth.format(monthStart);
        hash.put(monthCacheKey, salesmanName, month);
        redisTemplateOps.expire(monthCacheKey, 63, TimeUnit.DAYS);

    }
}
