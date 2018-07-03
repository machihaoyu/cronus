package com.fjs.cronus.service.redis;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.dto.avatar.AvatarApiDTO;
import com.fjs.cronus.dto.avatar.FirstBarDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.BaseCommonDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.client.AvatarClientService;
import com.fjs.cronus.service.client.TheaService;
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
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;
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
public class AllocateRedisServiceV2 {


    @Resource
    RedisTemplate redisTemplateOps;

    @Resource
    private AvatarClientService avatarClientService;

    @Resource
    private TheaService theaService;

    /**
     * 获取queue第一个，并放入到queue尾部.
     * （lua脚本，保证原子性操作)
     */
    private String script = "local result = redis.call('lpop', KEYS[1])\n" +
            "if result then\n" +
            "redis.call('rpush', KEYS[1], result);\n" +
            "return result;\n" +
            "else\n" +
            "return nil;\n" +
            "end";

    private RedisScript getRedisScript(String scriptStr) {
        return new RedisScript<String>() {
            @Override
            public String getSha1() {
                return DigestUtils.sha1DigestAsHex(scriptStr);
            }

            @Override
            public Class<String> getResultType() {
                return String.class;
            }

            @Override
            public String getScriptAsString() {
                return scriptStr;
            }
        };
    }

    private Object getAndPush2End(String key) {
        if (StringUtils.isBlank(key)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "key 不能为null");
        }
        return redisTemplateOps.execute(getRedisScript(script), new StringRedisSerializer(), new StringRedisSerializer(), Collections.singletonList(key), String.valueOf("1"));
    }

    /**
     * 媒体业务员queue：添加到队列尾部.
     */
    public void addUserToAllocateTemplete2(Integer userId, Integer companyId, Integer medial, String monthFlag) {
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "userId 不能为null");
        }

        String effectiveDate = this.getMonthStr(monthFlag);

        String key = this.getKey(companyId, medial, effectiveDate);
        ListOperations<String, Integer> listOperations = redisTemplateOps.opsForList();
        listOperations.remove(key, 10, userId);
        listOperations.rightPush(key, userId);
    }

    /**
     * 媒体业务员queue：移除.
     */
    public void delUserToAllocateTemplete2(Integer userId, Integer companyId, Integer medial, String monthFlag) {
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "userId 不能为null");
        }

        String effectiveDate = this.getMonthStr(monthFlag);

        String key = this.getKey(companyId, medial, effectiveDate);
        ListOperations<String, Integer> listOperations = redisTemplateOps.opsForList();
        listOperations.remove(key, 10, userId);
    }

    /**
     * 媒体业务员queue：获取all.
     */
    public List<Integer> finaAllFromQueue(Integer companyId, Integer medial, String effectiveDate) {

        String key = this.getKey(companyId, medial, effectiveDate);
        ListOperations<String, Integer> listOperations = redisTemplateOps.opsForList();
        List<Integer> range = listOperations.range(key, 0, -1);
        return range;
    }

    /**
     * 媒体业务员queue：获取用户,且移到队列尾部.
     */
    public Integer getAndPush2End(Integer companyId, Integer medial, String effectiveDate) {

        String key = this.getKey(companyId, medial, effectiveDate);

        Object result = getAndPush2End(key);
        if (result != null && StringUtils.isNumeric(result.toString().trim())) {
            return Integer.valueOf(result.toString().trim());
        }
        return null;
    }

    /**
     * 媒体业务员queue：队列长度.
     */
    public long getQueueSize(Integer companyId, Integer medial, String effectiveDate) {

        String key = this.getKey(companyId, medial, effectiveDate);
        Long size = redisTemplateOps.opsForList().size(key);
        return size == null ? 0 : size;
    }

    /**
     * 媒体业务员queue：删除队列.
     */
    public void delCompanyMediaQueueRedisQueue(Integer companyId, Integer medial, String effectiveDate) {
        String key = this.getKey(companyId, medial, effectiveDate);
        redisTemplateOps.delete(key);
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
    public String getMonthStr(String monthFlag) {
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
     * 转为商机的时间格式.
     * yyyyMM   -->  yyyy-MM
     */
    public String getMonthStr4avatar(String month) {
        if (StringUtils.isBlank(month)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "month 不能为null");
        }
        month = month.trim();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");

        Date parse = null;
        try {
            parse = sdf.parse(month);
        } catch (ParseException e) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "日期转换异常");
        }
        String format = sdf2.format(parse);

        return format;
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

            ListOperations<String, Integer> listOperations = redisTemplateOps.opsForList();

            String currentMonthQueueKey = this.getKey(companyId, medial, currentMonthStr);
            String nextMonthQueueKey = this.getKey(companyId, medial, nextMonthStr);

            List<Integer> currentMonthQueue = listOperations.range(currentMonthQueueKey, 0, -1);
            Set<Integer> nextMonthQueue = currentMonthQueue.stream().filter(item -> item != null).collect(toSet());

            if (CollectionUtils.isNotEmpty(nextMonthQueue)) {
                redisTemplateOps.delete(nextMonthQueueKey);
                listOperations.leftPushAll(nextMonthQueueKey, nextMonthQueue);
            }
        }
    }

    /**
     * 城市一级吧queue：找第一个一级吧.
     */
    public Integer getSubCompanyIdFromQueue(String cityName, Integer mediaid) {
        String key = CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat("$").concat(mediaid.toString()).concat("$").concat(cityName);
        Object result = getAndPush2End(key);
        if (result != null && StringUtils.isNumeric(result.toString().trim())) {
            return Integer.valueOf(result.toString().trim());
        }
        return null;
    }

    /**
     * 城市一级吧queue：队列长度.
     */
    public long getSubCompanyIdQueueSize(String token, String cityName, Integer mediaid) {

        Long size = 0L;
        if (StringUtils.isNotBlank(cityName) && StringUtils.isNotBlank(token)) {

            ListOperations<String, Integer> listOperations = redisTemplateOps.opsForList();

            // 目标数据缓存key
            String key = CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat("$").concat(mediaid.toString()).concat("$").concat(cityName);

            Long temp = listOperations.size(key);
            if ( temp > 0) {
                // 当缓存中有，取出然后移到queue尾部
                size = temp;
            } else {
                // 当缓存无，去库中取并放入到缓存中
                Map<String, List<Integer>> subCompanyByCityName = this.findSubCompanyByCityName(token);
                for (Map.Entry<String, List<Integer>> entry : subCompanyByCityName.entrySet()) {
                    String cityNameTemp = entry.getKey();
                    List<Integer> subCompanyIdList = entry.getValue();
                    Set<Integer> subCompanyIdList2 = CollectionUtils.isEmpty(subCompanyIdList) ? new HashSet<>() : subCompanyIdList.stream().filter(item -> item != null).collect(toSet());

                    if (cityName.equals(cityNameTemp) && CollectionUtils.isNotEmpty(subCompanyIdList2)) {
                        redisTemplateOps.delete(key);
                        listOperations.leftPushAll(key, subCompanyIdList2);
                        size = Long.valueOf(subCompanyIdList2.size());
                        break;
                    }
                }
            }
        }

        return size == null ? 0 : size;
    }

    /**
     * 城市一级吧queue：获取所有一级吧.
     */
    private Map<String, List<Integer>> findSubCompanyByCityName(String token) {
        if (StringUtils.isNotBlank(token)) {
            // 获取所有一级吧
            AvatarApiDTO<List<FirstBarDTO>> allSubCompany = avatarClientService.findAllSubCompany(token);
            List<FirstBarDTO> data = null;
            if (allSubCompany != null && allSubCompany.getResult() == 0 && allSubCompany.getData() != null) {
                data = allSubCompany.getData();
            }
            return CollectionUtils.isEmpty(data) ? new HashMap<>() : data.stream()
                    .filter(
                            item -> item != null
                                    && StringUtils.isNotBlank(item.getCity())
                                    && item.getId() != null
                    )
                    .collect(groupingBy(FirstBarDTO::getCity, mapping(FirstBarDTO::getId, toList())));
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

        // 获取系统所有媒体
        TheaApiDTO<List<BaseCommonDTO>> allMedia = theaService.getAllMedia(token);
        if (!CommonMessage.SUCCESS.getCode().equals(allMedia.getResult())) {
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, allMedia.getMessage());
        }
        List<BaseCommonDTO> allMediaList = allMedia.getData();
        Set<Integer> mediaids = CollectionUtils.isEmpty(allMediaList) ? null : allMediaList.stream().filter(i -> i != null && i.getId() != null).map(BaseCommonDTO::getId).collect(toSet());

        if (CollectionUtils.isEmpty(mediaids)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "系统数据异常，系统中未找到媒体数据");
        }

        Map<String, Set<Integer>> cityNameMappingSubCompanyId = CollectionUtils.isEmpty(data) ? new HashMap<>() : data.stream()
                .filter(item -> item != null && item.getId() != null && StringUtils.isNotBlank(item.getCity()))
                .collect(groupingBy(FirstBarDTO::getCity, mapping(FirstBarDTO::getId, toSet())));

        for (Map.Entry<String, Set<Integer>> entry : cityNameMappingSubCompanyId.entrySet()) {
            Set<Integer> value = entry.getValue();
            String cityName = entry.getKey();

            Set<Integer> subCompanyIdSet = CollectionUtils.isEmpty(value) ? new HashSet<>() : value;

            if (StringUtils.isNotBlank(cityName) && CollectionUtils.isNotEmpty(subCompanyIdSet)) {

                for (Integer mediaid : mediaids) {
                    String key = CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat("$").concat(mediaid.toString()).concat("$").concat(cityName);
                    ListOperations<String, Integer> listOperations = redisTemplateOps.opsForList();
                    redisTemplateOps.delete(key);
                    listOperations.leftPushAll(key, subCompanyIdSet);
                }

            }

        }
    }

    /**
     * 从redis中获取一级吧id.
     */
    public List<Integer> findFirstBarByCityAndMedia(String token, String cityName, Integer mediaId) {

        ListOperations<String, Integer> listOperations = redisTemplateOps.opsForList();
        // 目标数据缓存key
        String key = CommonRedisConst.ALLOCATE_SUBCOMPANYID.concat("$").concat(mediaId.toString()).concat("$").concat(cityName);
        return listOperations.range(key, 0, -1);

    }
}
