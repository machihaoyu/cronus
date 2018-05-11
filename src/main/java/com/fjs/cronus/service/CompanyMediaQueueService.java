package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.BaseCommonDTO;
import com.fjs.cronus.entity.CompanyMediaQueue;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CompanyMediaQueueMapper;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.redis.AllocateRedisService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * 单位、媒体的队列表；一级吧、媒体的队列表 service.
 */
@Service
public class CompanyMediaQueueService {

    @Autowired
    private CompanyMediaQueueMapper companyMediaQueueMapper;

    @Autowired
    private TheaService theaService;

    @Autowired
    private UserMonthInfoService userMonthInfoService;

    @Autowired
    private AllocateRedisService allocateRedisService;

    public List<Map<String, Object>> findByCompanyId(String token, Integer currentUserId, Integer subCompanyId, String yearMonth) {

        this.allocateRedisService.checkMonthStr(yearMonth);

        // 获取指定一级吧其下的特殊队列
        List<CompanyMediaQueue> companyMediaQueueList = companyMediaQueueMapper.findByCompanyId(subCompanyId, CommonEnum.entity_status1.getCode(), yearMonth);
        companyMediaQueueList = CollectionUtils.isEmpty(companyMediaQueueList) ? new ArrayList<>() : companyMediaQueueList;

        Set<Integer> mediaIds = companyMediaQueueList.stream().filter(item -> item != null && item.getMediaid() != null).map(CompanyMediaQueue::getMediaid).collect(toSet());

        List<Map<String, Object>> result = new ArrayList<>();
        // 每个一级吧，默认有一个总分配队列
        Map<String, Object> e1 = new HashMap<>();
        e1.put("id", CommonConst.COMPANY_MEDIA_QUEUE_COUNT);
        e1.put("name", "总分配队列");
        result.add(e1);

        // 获取媒体的name
        if (CollectionUtils.isNotEmpty(mediaIds)) {
            // 获取系统所有媒体
            TheaApiDTO allMedia = theaService.getAllMedia(token);
            if (!CommonMessage.SUCCESS.getCode().equals(allMedia.getResult())) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, allMedia.getMessage());
            }
            List<BaseCommonDTO> allMediaList = (List<BaseCommonDTO>) allMedia.getData();
            allMediaList = CollectionUtils.isEmpty(allMediaList) ? new ArrayList<>() : allMediaList;

            for (BaseCommonDTO baseCommonDTO : allMediaList) {
                if (baseCommonDTO != null && baseCommonDTO.getId() != null && mediaIds.contains(baseCommonDTO.getId())) {
                    Map<String, Object> e = new HashMap<>();
                    e.put("id", baseCommonDTO.getId());
                    e.put("name", baseCommonDTO.getName());
                    result.add(e);
                }
            }
        }

        return result;
    }

    public void addCompanyMediaQueue(String token, Integer currentUserId, Integer companyid, Set<Integer> mediaIds, String yearmonth) {

        this.allocateRedisService.checkMonthStr(yearmonth);

        // 获取指定一级吧其下的特殊队列
        List<CompanyMediaQueue> companyMediaQueueList = companyMediaQueueMapper.findByCompanyId(companyid, CommonEnum.entity_status1.getCode(), yearmonth);
        Set<Integer> mediaIdsDB = CollectionUtils.isEmpty(companyMediaQueueList) ? new HashSet<>() : companyMediaQueueList.stream().filter(item -> item != null && item.getMediaid() != null).map(CompanyMediaQueue::getMediaid).collect(toSet());

        Collection nowData = CollectionUtils.subtract(mediaIds, mediaIdsDB);

        // 准备入库数据
        List<CompanyMediaQueue> data = new ArrayList<>(nowData.size());

        Iterator<Integer> iterator = nowData.iterator();
        Date now = new Date();
        while (iterator.hasNext()) {
            CompanyMediaQueue e = new CompanyMediaQueue();
            e.setCreated(now);
            e.setCreateid(currentUserId);
            e.setUpdated(now);
            e.setUpdateid(currentUserId);
            e.setStatus(CommonEnum.entity_status1.getCode());
            e.setCompanyid(companyid);
            e.setMediaid(iterator.next());
            e.setYearmonth(yearmonth);
        }

        if (CollectionUtils.isNotEmpty(data)) companyMediaQueueMapper.addBatchCompanyMediaQueue(data);
    }

    public void delCompanyMediaQueue(Integer currentUserId, Integer companyid, Integer mediaId, String yearmonth) {

        if (CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(mediaId)) {
            return;// 总队列不能删除
        }

        this.allocateRedisService.checkMonthStr(yearmonth);

        Date now = new Date();
        // 逻辑删除company_media_queue表数据
        CompanyMediaQueue valuesParams = new CompanyMediaQueue();
        valuesParams.setUpdated(now);
        valuesParams.setUpdateid(currentUserId);
        valuesParams.setStatus(CommonEnum.entity_status0.getCode());

        CompanyMediaQueue whereParams = new CompanyMediaQueue();
        whereParams.setCompanyid(companyid);
        whereParams.setMediaid(mediaId);
        whereParams.setStatus(CommonEnum.entity_status1.getCode());

        companyMediaQueueMapper.updateCompanyMediaQueue(valuesParams, whereParams);

        // 不用删除
        // 逻辑删除user_month_info表数据
        /*UserMonthInfo whereParamsUserMonthInfo = new UserMonthInfo();
        whereParamsUserMonthInfo.setCompanyid(companyid);
        whereParamsUserMonthInfo.setMediaid(mediaId);
        whereParamsUserMonthInfo.setStatus(CommonEnum.entity_status1.getCode());

        UserMonthInfo valueParamsUserMonthInfo = new UserMonthInfo();
        valueParamsUserMonthInfo.setLastUpdateUser(currentUserId);
        valueParamsUserMonthInfo.setLastUpdateTime(now);
        valueParamsUserMonthInfo.setStatus(CommonEnum.entity_status0.getCode());

        userMonthInfoService.updateUserMonthInfo(whereParamsUserMonthInfo, valueParamsUserMonthInfo);*/

        // 删除Redis队列
        allocateRedisService.delCompanyMediaQueueRedisQueue(companyid, mediaId, yearmonth);
    }

}
