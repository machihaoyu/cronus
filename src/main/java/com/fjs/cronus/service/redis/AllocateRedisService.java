package com.fjs.cronus.service.redis;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.dto.avatar.AvatarApiDTO;
import com.fjs.cronus.dto.avatar.FirstBarDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.client.AvatarClientService;
import com.fjs.cronus.util.CommonUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.sun.javafx.binding.IntegerConstant;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.*;

/**
 * 自动分配队列Redis服务
 * Created by feng on 2017/9/16.
 */
@Service
public class AllocateRedisService {


    @Resource
    private RedisTemplate<String, String> redisAllocateTemplete;

    @Resource
    private AvatarClientService avatarClientService;

    @Resource
    private CRMRedisLockHelp cRMRedisLockHelp;

    /**
     * 切割器.
     */
    private Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

    /**
     * 连接器.
     */
    private Joiner joiner = Joiner.on(",").skipNulls();


    /**
     * 添加用户至城市自动分配队列中
     *
     * @param userId
     * @param city
     * @return
     */
    @Deprecated
    public String addUserToAllocateTemplete(Integer userId, String city) {
        if (null == userId || StringUtils.isBlank(city)) {
            return null;
        }
        //获取Redis中对应城市的分配列表
        String userIds = this.getAllocateTemplet(city);
        if (StringUtils.isBlank(userIds)) {
            userIds = userId.toString();
        } else {//拼接新的队列value
            //先判断添加userID 是否包含在原队列中
            String[] userIdsArray = userIds.split(",");
            if (!ArrayUtils.contains(userIdsArray, userId.toString())) {
                userIds = userIds + "," + userId.toString();
            }
        }
        this.setAllocateTemplete(userIds, city);
        return userIds;
    }

    /**
     * 设置城市分配队列
     *
     * @param userIds
     * @param city
     */
    @Deprecated
    public String setAllocateTemplete(String userIds, String city) {
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, String> redisAllocateOptions = redisAllocateTemplete.opsForValue();
        redisAllocateOptions.set(CommonRedisConst.ALLOCATE_LIST + city, userIds);
        return userIds;
    }

    /**
     * 移除用户至城市自动分配队列中
     *
     * @param userId
     * @param city
     * @return
     */
    public String delUserToAllocateTemplete(Integer userId, String city) {
        //从Redis中取参数
        String userIds = this.getAllocateTemplet(city);
        if (StringUtils.isBlank(userIds)) {
            return null;
        } else {
            String[] userIdsArray = userIds.split(",");
            userIdsArray = ArrayUtils.removeElement(userIdsArray, userId.toString());
            userIds = "";
            if (userIdsArray.length == 0) {//新队列参数为空则移除队列
                this.removeAllocateTemplet(CommonRedisConst.ALLOCATE_LIST + city);
            } else {//新队列参数不为空，则分情况重新设置
                userIds = CommonUtil.queryStrArrayToStr(userIdsArray);
            }
        }
        this.setAllocateTemplete(userIds, city);
        return userIds;
    }

    /**
     * 自动分配修改队列
     *
     * @param city
     * @return
     */
    public String changeAllocateTemplet(Integer userId, String city) {
        String userIds = this.getAllocateTemplet(city);
        if (StringUtils.isBlank(userIds)) {
            return null;
        } else {
            String[] userIdsArray = userIds.split(",");
            String[] newUserIdsArray = null;
            String userIdStr = userId.toString();
            for (int i = 0; i < userIdsArray.length; i++) {
                if (userIdStr.equals(userIdsArray[i])) {
                    newUserIdsArray = ArrayUtils.remove(userIdsArray, i);
                    break;
                }
            }
//            userIdsArray = ArrayUtils.remove(userIdsArray,0);
            newUserIdsArray = ArrayUtils.add(newUserIdsArray, userIdStr);
            //拼接
            userIds = CommonUtil.queryStrArrayToStr(newUserIdsArray);
            userIds = this.setAllocateTemplete(userIds, city);
        }
        return userIds;
    }

    /**
     * 移除城市自动分配队列
     *
     * @param city
     */
    @Deprecated
    public void removeAllocateTemplet(String city) {
        redisAllocateTemplete.delete(CommonRedisConst.ALLOCATE_LIST + city);
    }

    /**
     * 从Redis中根据城市取队列
     *
     * @param city
     * @return
     */
    public String getAllocateTemplet(String city) {
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, String> redisAllocateOptions = redisAllocateTemplete.opsForValue();
        String userIds = redisAllocateOptions.get(CommonRedisConst.ALLOCATE_LIST + city);
        return userIds;
    }

    /**
     * 媒体业务员queue：添加到队列尾部.
     */
    public void addUserToAllocateTemplete2(Integer userId, Integer companyId, Integer medial, String monthFlag) {
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "userId 不能为null");
        }

        String effectiveDate = this.getMonthStr(monthFlag);

        redisAllocateTemplete.setKeySerializer(new StringRedisSerializer());
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());

        String key = this.getKey(companyId, medial, effectiveDate);
        ListOperations<String, String> listOperations = redisAllocateTemplete.opsForList();
        listOperations.remove(key, 500, userId.toString());
        listOperations.rightPush(key, userId.toString());
    }

    /**
     * 媒体业务员queue：移除.
     */
    public void delUserToAllocateTemplete2(Integer userId, Integer companyId, Integer medial, String monthFlag) {
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "userId 不能为null");
        }

        String effectiveDate = this.getMonthStr(monthFlag);

        redisAllocateTemplete.setKeySerializer(new StringRedisSerializer());
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());

        String key = this.getKey(companyId, medial, effectiveDate);
        ListOperations<String, String> listOperations = redisAllocateTemplete.opsForList();
        listOperations.remove(key, 500, userId.toString());
    }

    /**
     * 媒体业务员queue：获取all.
     */
    public List<Integer> finaAllFromQueue(Integer companyId, Integer medial, String effectiveDate) {
        redisAllocateTemplete.setKeySerializer(new StringRedisSerializer());
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());

        String key = this.getKey(companyId, medial, effectiveDate);
        ListOperations<String, String> listOperations = redisAllocateTemplete.opsForList();
        List<String> range = listOperations.range(key, 0, -1);

        return range.stream().filter(item -> item != null).map(Integer::valueOf).collect(toList());
    }

    /**
     * 媒体业务员queue：获取用户,且移到队列尾部.
     */
    public Integer getAndPush2End(Integer companyId, Integer medial, String effectiveDate) {

        redisAllocateTemplete.setKeySerializer(new StringRedisSerializer());
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());

        String key = this.getKey(companyId, medial, effectiveDate);
        ListOperations<String, String> listOperations = redisAllocateTemplete.opsForList();
        String s = listOperations.leftPop(key);
        if (StringUtils.isNumeric(s)) {
            listOperations.remove(key, 500, s);
            listOperations.rightPush(key, s);
            return Integer.valueOf(s);
        }
        return null;
    }

    /**
     * 媒体业务员queue：删除队列.
     */
    public void delCompanyMediaQueueRedisQueue(Integer companyId, Integer medial, String effectiveDate) {
        String key = this.getKey(companyId, medial, effectiveDate);
        redisAllocateTemplete.delete(key);
    }

    /**
     * 媒体业务员queue：获取缓存key.
     */
    private String getKey(Integer companyId, Integer medial, String effectiveDate) {
        if (companyId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "获取队列key错误，companyId 不能为null");
        }
        if (medial == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "获取队列key错误，medial 不能为null");
        }
        if (StringUtils.isBlank(effectiveDate)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "获取队列key错误，effectiveDate 不能为null");
        }
        if (!effectiveDate.matches("[0-9]{6}")) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "effectiveDate 格式不正确，需要是 yyyyMM");
        }
        // key结构：指定公司、指定媒体、指定月
        return CommonRedisConst.ALLOCATE_LIST.concat("$").concat(companyId.toString()).concat("$").concat(medial.toString()).concat("$").concat(effectiveDate);
    }

    /**
     * 媒体业务员queue：获取队列当月的，时间串.
     */
    public String getMonthStr(String monthFlag){
        if (StringUtils.isBlank(monthFlag)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "monthFlag 不能为null");
        }
        monthFlag = monthFlag.trim();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        String effectiveDate = null;
        if (CommonConst.USER_MONTH_INFO_MONTH_CURRENT.equalsIgnoreCase(monthFlag)) {
            effectiveDate = sdf.format(new Date());
        } else if (CommonConst.USER_MONTH_INFO_MONTH_NEXT.equalsIgnoreCase(monthFlag)) {

            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MONTH, 1);
            Date nextMoth = instance.getTime();

            effectiveDate = sdf.format(nextMoth);
        }

        if (StringUtils.isBlank(effectiveDate)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "monthFlag 格式不正确");
        }

        return effectiveDate;
    }

    /**
     * 媒体业务员queue：获取队列下月的，时间串.
     */
    public void checkMonthStr(String yearMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        try {
            Date parse = sdf.parse(yearMonth);
        } catch (ParseException e) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "格式不正确，需要是 yyyyMM");
        }
    }

    /**
     * 媒体业务员queue：复制当前队列到下月.
     */
    public void copyCurrentMonthQueue(Integer companyId, Integer medial) {

        String currentMonthStr = this.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_CURRENT);
        String nextMonthStr = this.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_NEXT);

        if (companyId != null && medial != null && StringUtils.isNotBlank(currentMonthStr) && StringUtils.isNotBlank(nextMonthStr)) {

            redisAllocateTemplete.setKeySerializer(new StringRedisSerializer());
            redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
            ListOperations<String, String> listOperations = redisAllocateTemplete.opsForList();

            String currentMonthQueueKey = this.getKey(companyId, medial, currentMonthStr);
            String nextMonthQueueKey = this.getKey(companyId, medial, nextMonthStr);

            List<String> currentMonthQueue = listOperations.range(currentMonthQueueKey, 0, -1);
            Set<String> nextMonthQueue = currentMonthQueue.stream().filter(item -> StringUtils.isNotBlank(item) ).collect(toSet());

            if (CollectionUtils.isNotEmpty(nextMonthQueue) ){
                redisAllocateTemplete.delete(nextMonthQueueKey);
                listOperations.leftPushAll(nextMonthQueueKey, nextMonthQueue);
            }
        }
    }

    /**
     * 城市一级吧queue：根据城市id，获取一级吧.
     */
    public Integer getSubCompanyIdFromQueue(String token, String cityName) {
        String subCompanyId = null;
        if (StringUtils.isNotBlank(cityName) && StringUtils.isNotBlank(token)) {

            redisAllocateTemplete.setKeySerializer(new StringRedisSerializer());
            redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
            ListOperations<String, String> listOperations = redisAllocateTemplete.opsForList();

            // 目标数据缓存key
            String key = CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat(cityName);

            if (listOperations.size(key) > 0) {
                // 当缓存中有，取出然后移到queue尾部
                subCompanyId = listOperations.leftPop(key);
                listOperations.rightPush(key, subCompanyId);
            } else {
                // 当缓存无，去库中取并放入到缓存中
                Map<String, List<Integer>> subCompanyByCityName = this.findSubCompanyByCityName(token, cityName);
                for (Map.Entry<String, List<Integer>> entry : subCompanyByCityName.entrySet()) {
                    String cityNameTemp = entry.getKey();
                    List<Integer> subCompanyIdList = entry.getValue();
                    Set<String> subCompanyIdList2 = CollectionUtils.isEmpty(subCompanyIdList) ? new HashSet<>() : subCompanyIdList.stream().filter(item -> item != null).map(String::valueOf).collect(toSet());

                    if (cityName.equals(cityNameTemp) && CollectionUtils.isNotEmpty(subCompanyIdList2)) {
                        redisAllocateTemplete.delete(CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat(cityNameTemp));
                        subCompanyId = subCompanyIdList2.iterator().next(); // 取出1个
                        redisAllocateTemplete.delete(CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat(cityNameTemp));
                        listOperations.leftPushAll(CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat(cityNameTemp), subCompanyIdList2);
                        break;
                    }
                }
            }
        }
        return StringUtils.isBlank(subCompanyId) ? null : Integer.valueOf(subCompanyId);
    }

    /**
     * 城市一级吧queue：获取所有一级吧.
     */
    private Map<String, List<Integer>> findSubCompanyByCityName(String token, String city) {
        if (StringUtils.isNotBlank(city) && StringUtils.isNotBlank(token)) {
            // 获取所有一级吧
            AvatarApiDTO<List<FirstBarDTO>> allSubCompany = avatarClientService.findAllSubCompany(token);
            List<FirstBarDTO> data = null;
            if (allSubCompany != null && allSubCompany.getResult() == 0 && allSubCompany.getData() != null) {
                data = allSubCompany.getData();
            }
            Map<String, List<Integer>> cityNameMappingSubCompanyId = CollectionUtils.isEmpty(data) ? new HashMap<>() : data.stream().collect(groupingBy(FirstBarDTO::getCity, mapping(FirstBarDTO::getId, toList())));
            return cityNameMappingSubCompanyId;
        }
        return new HashMap<>();
    }

    /**
     * 城市下一级吧queue：刷新数据.
     * <p>
     * 由于一级吧数据来源于商机系统，需要暴露一个刷新缓存给商机系统接口.
     */
    public void flushSubCompanyQueue(String token) {

        AvatarApiDTO<List<FirstBarDTO>> allSubCompany = avatarClientService.findAllSubCompany(token);
        List<FirstBarDTO> data = null;
        if (allSubCompany != null && allSubCompany.getResult() == 0 && allSubCompany.getData() != null) {
            data = allSubCompany.getData();
        }

        Map<String, List<Integer>> cityNameMappingSubCompanyId = CollectionUtils.isEmpty(data) ? new HashMap<>() : data.stream().collect(groupingBy(FirstBarDTO::getCity, mapping(FirstBarDTO::getId, toList())));
        for (Map.Entry<String, List<Integer>> entry : cityNameMappingSubCompanyId.entrySet()) {
            List<Integer> value = entry.getValue();
            String cityName = entry.getKey();

            Set<String> subCompanyIdSet = CollectionUtils.isEmpty(value) ? new HashSet<>() : value.stream().filter(item -> item != null).map(String::valueOf).collect(toSet());

            if (StringUtils.isNotBlank(cityName) && CollectionUtils.isNotEmpty(subCompanyIdSet)) {
                redisAllocateTemplete.setKeySerializer(new StringRedisSerializer());
                redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
                ListOperations<String, String> listOperations = redisAllocateTemplete.opsForList();
                redisAllocateTemplete.delete(CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat(cityName));
                listOperations.leftPushAll(CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat(cityName), subCompanyIdSet);
            }

        }
    }
}
