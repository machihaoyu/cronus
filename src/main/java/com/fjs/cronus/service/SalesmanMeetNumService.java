package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.entity.SalesmanCallTime;
import com.fjs.cronus.entity.SalesmanMeetNum;
import com.fjs.cronus.mappers.CustomerMeetMapper;
import com.fjs.cronus.mappers.SalesmanMeetNumMapper;
import com.fjs.cronus.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
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
public class SalesmanMeetNumService {

    @Resource
    private RedisTemplate redisTemplateOps;

    @Autowired
    private SalesmanMeetNumMapper salesmanMeetNumMapper;
    @Autowired
    private CustomerMeetMapper customerMeetMapper;

    /**
     * 数据统计:统计指定天面见次数.
     */
    public void countData(Long subCompanyid, Long salesmanId, String salesmanName, Date date) {
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);

        Calendar end = Calendar.getInstance();
        end.setTime(date);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        Integer num = customerMeetMapper.getCountCustomerIdByCreateId(salesmanId.intValue(), start.getTime(), end.getTime());
        num = num == null ? 0 : num;

        SalesmanMeetNum temp = new SalesmanMeetNum();
        temp.setSubCompanyid(subCompanyid);
        temp.setSalesManId(salesmanId);
        temp.setTime(start.getTime());
        temp.setStatus(CommonEnum.entity_status1.getCode());
        SalesmanMeetNum db = salesmanMeetNumMapper.selectOne(temp);

        if (db != null) {
            SalesmanMeetNum record = new SalesmanMeetNum();
            record.setUpdated(new Date());
            record.setNum(num);

            Example example = new Example(SalesmanMeetNum.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", db.getId());

            salesmanMeetNumMapper.updateByExampleSelective(record, example);
        } else {
            SalesmanMeetNum record = new SalesmanMeetNum();
            record.setNum(num);
            record.setCreated(new Date());
            record.setTime(start.getTime());
            record.setSubCompanyid(subCompanyid);
            record.setSalesManName(salesmanName);
            record.setSalesManId(salesmanId);
            record.setStatus(CommonEnum.entity_status1.getCode());

            salesmanMeetNumMapper.insertSelective(record);
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

        Long todayNum = salesmanMeetNumMapper.getByTimeAndName(salesmanName, instance.getTime(), instance.getTime(), CommonEnum.entity_status1.getCode());
        todayNum = todayNum == null ? 0 : todayNum;

        String format = sdfDay.format(instance.getTime()); // 获取当天
        String todayCacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_DAY + ":" + format;
        hash.put(todayCacheKey, salesmanName, todayNum);
        redisTemplateOps.expire(todayCacheKey, 3, TimeUnit.DAYS);

        // 刷新本周
        Date weekStart = DateUtils.getWeekStart(instance.getTime()); // 获取当周周一
        Date weekEnd = DateUtils.getWeekEnd(instance.getTime());

        Long weekNum = salesmanMeetNumMapper.getByTimeAndName(salesmanName, weekStart, weekEnd, CommonEnum.entity_status1.getCode());
        weekNum = weekNum == null ? 0 : weekNum;

        String weekFormat = sdfWeek.format(weekStart);
        String weekCacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_WEEK + ":" + weekFormat;
        hash.put(weekCacheKey, salesmanName, weekNum);
        redisTemplateOps.expire(weekCacheKey, 15, TimeUnit.DAYS);

        // 刷新本月
        Date currMonthStart = DateUtils.getMonthStart(instance.getTime()); // 获取当月第一天
        Date currMonthEnd = DateUtils.getMonthEnd(instance.getTime());

        String monthFormat = sdfMonth.format(currMonthStart);
        String monthCacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_MONTH + ":" + monthFormat;

        Long monthNum = salesmanMeetNumMapper.getByTimeAndName(salesmanName, currMonthStart, currMonthEnd, CommonEnum.entity_status1.getCode());
        monthNum = monthNum == null ? 0 : monthNum;

        hash.put(monthCacheKey, salesmanName, monthNum);
        redisTemplateOps.expire(monthCacheKey, 63, TimeUnit.DAYS);
    }

    /**
     * 今日：面见次数.
     */
    public long getMeetNumOfNow(Long userId, String salemanName) {

        // 获取缓存key
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_DAY);
        String format = sdf.format(instance.getTime()); // 获取当天
        String cacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_DAY + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.intValue();

        // 从数据库中取数据
        SalesmanMeetNum temp = new SalesmanMeetNum();
        temp.setSalesManName(salemanName);
        temp.setSalesManId(userId);
        temp.setTime(instance.getTime());
        temp.setStatus(CommonEnum.entity_status1.getCode());
        SalesmanMeetNum db = salesmanMeetNumMapper.selectOne(temp);

        Long result = 0L;
        if (db != null && db.getNum() != null) {
            result = db.getNum().longValue();
        }

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 3, TimeUnit.DAYS);

        return 0;
    }

    /**
     * 昨日：面见次数.
     */
    public long getMeetNumOfYestday(Long userId, String salemanName) {

        // 获取缓存key
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_YEAR, -1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_DAY);
        String format = sdf.format(instance.getTime());
        String cacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_DAY + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.intValue();

        // 从数据库中取数据
        SalesmanMeetNum temp = new SalesmanMeetNum();
        temp.setSalesManName(salemanName);
        temp.setTime(instance.getTime());
        temp.setSalesManId(userId);
        temp.setStatus(CommonEnum.entity_status1.getCode());
        SalesmanMeetNum db = salesmanMeetNumMapper.selectOne(temp);

        Long result = 0L;
        if (db != null && db.getNum() != null) {
            result = db.getNum().longValue();
        }

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 3, TimeUnit.DAYS);

        return 0;
    }

    /**
     * 本周：面见次数.
     */
    public long getMeetNumOfCurrWeek(String salemanName) {

        // 获取缓存key
        Date now = new Date();
        Date weekStart = DateUtils.getWeekStart(now); // 获取当周周一
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_WEEK);
        String format = sdf.format(weekStart);
        String cacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_WEEK + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.longValue();

        // 从数据库中取数据
        Date weekEnd = DateUtils.getWeekEnd(now);
        Long result = salesmanMeetNumMapper.getByTimeAndName(salemanName, weekStart, weekEnd, CommonEnum.entity_status1.getCode());
        result = result == null ? 0 : result;

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 15, TimeUnit.DAYS);

        return result;
    }

    /**
     * 上周：面见次数.
     */
    public long getMeetNumOfPreWeek(String salemanName) {

        // 获取缓存key
        Date now = new Date();
        Date currWeekStart = DateUtils.getWeekStart(now);

        Calendar instance = Calendar.getInstance();
        instance.setTime(currWeekStart);
        instance.add(Calendar.DAY_OF_YEAR, -7);
        Date preWeekStart = instance.getTime(); //获取上周一

        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_WEEK);
        String format = sdf.format(preWeekStart);
        String cacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_WEEK + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.longValue();

        // 从数据库中取数据
        Date preWeekEnd = DateUtils.getWeekEnd(preWeekStart);
        Long result = salesmanMeetNumMapper.getByTimeAndName(salemanName, preWeekStart, preWeekEnd, CommonEnum.entity_status1.getCode());
        result = result == null ? 0 : result;

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 15, TimeUnit.DAYS);

        return result;
    }

    /**
     * 本月：面见次数.
     */
    public long getMeetNumOfCurrMonth(String salemanName) {

        // 获取缓存key
        Date now = new Date();
        Date currMonthStart = DateUtils.getMonthStart(now); // 获取当月第一天

        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_MONTH);
        String format = sdf.format(currMonthStart);
        String cacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_MONTH + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.longValue();

        // 从数据库中取数据
        Date currMonthEnd = DateUtils.getMonthEnd(now);
        Long result = salesmanMeetNumMapper.getByTimeAndName(salemanName, currMonthStart, currMonthEnd, CommonEnum.entity_status1.getCode());
        result = result == null ? 0 : result;

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 63, TimeUnit.DAYS);

        return result;
    }

    /**
     * 上月：面见次数.
     */
    public long getMeetNumOfPreMonth(String salemanName) {

        // 获取缓存key
        Date now = new Date();
        Date currMonthStart = DateUtils.getMonthStart(now);
        Calendar instance = Calendar.getInstance();
        instance.setTime(currMonthStart);
        instance.add(Calendar.DAY_OF_YEAR, -1);
        Date preMonthStart = DateUtils.getMonthStart(instance.getTime()); // 上月第一天

        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_MONTH);
        String format = sdf.format(preMonthStart);
        String cacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_MONTH + ":" + format;

        // 从缓存中取
        HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();
        Number number = hash.get(cacheKey, salemanName);
        if (number != null) return number.longValue();

        // 从数据库中取数据
        Date preMonthEnd = DateUtils.getMonthEnd(instance.getTime());
        Long result = salesmanMeetNumMapper.getByTimeAndName(salemanName, preMonthStart, preMonthEnd, CommonEnum.entity_status1.getCode());
        result = result == null ? 0 : result;

        hash.put(cacheKey, salemanName, result);
        redisTemplateOps.expire(cacheKey, 32, TimeUnit.DAYS);

        return result;
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

        List<SalesmanMeetNum> list = salesmanMeetNumMapper.findByTime(formatDate, formatDate, CommonEnum.entity_status1.getCode());

        if (!CollectionUtils.isEmpty(list)) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_DAY);

            String cacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_DAY + ":" + sdf.format(formatDate);
            HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();

            Map<String, Number> map = list.stream()
                    .filter(item -> item != null && item.getNum() != null && StringUtils.isNotBlank(item.getSalesManName()))
                    .collect(Collectors.toMap(SalesmanMeetNum::getSalesManName, SalesmanMeetNum::getNum, (x, y) -> x));

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

        List<SalesmanMeetNum> list = salesmanMeetNumMapper.findByTime(weekStart, weekEnd, CommonEnum.entity_status1.getCode());

        if (!CollectionUtils.isEmpty(list)) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_WEEK);

            String cacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_WEEK + ":" + sdf.format(weekStart);
            HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();

            Map<String, Number> map = list.stream()
                    .filter(item -> item != null && item.getNum() != null && StringUtils.isNotBlank(item.getSalesManName()))
                    .collect(Collectors.toMap(SalesmanMeetNum::getSalesManName, SalesmanMeetNum::getNum, (x, y) -> x));

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

        List<SalesmanMeetNum> list = salesmanMeetNumMapper.findByTime(currMonthStart, currMonthEnd, CommonEnum.entity_status1.getCode());

        if (!CollectionUtils.isEmpty(list)) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.SALESMAN_CALL_MONTH);

            String cacheKey = CommonRedisConst.SALES_MAN_MEET_NUM_MONTH + ":" + sdf.format(currMonthStart);
            HashOperations<String, String, Number> hash = redisTemplateOps.opsForHash();


            Map<String, Number> map = list.stream()
                    .filter(item -> item != null && item.getNum() != null && StringUtils.isNotBlank(item.getSalesManName()))
                    .collect(Collectors.toMap(SalesmanMeetNum::getSalesManName, SalesmanMeetNum::getNum, (x, y) -> x));


            if (map.size() > 0) {
                hash.putAll(cacheKey, map);
                redisTemplateOps.expire(cacheKey, 63, TimeUnit.DAYS);
            }
        }
    }

}
