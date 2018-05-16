package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONArray;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.BaseCommonDTO;
import com.fjs.cronus.entity.CompanyMediaQueue;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CompanyMediaQueueMapper;
import com.fjs.cronus.mappers.UserMonthInfoMapper;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.redis.AllocateRedisService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
    private UserMonthInfoMapper userMonthInfoMapper;

    @Autowired
    private AllocateRedisService allocateRedisService;

    public List<Map<String, Object>> findByCompanyId(String token, Integer currentUserId, Integer subCompanyId) {

        // 获取指定一级吧其下的特殊队列
        CompanyMediaQueue e = new CompanyMediaQueue();
        e.setCompanyid(subCompanyId);
        e.setStatus(CommonEnum.entity_status1.getCode());
        List<CompanyMediaQueue> companyMediaQueueList = companyMediaQueueMapper.select(e);
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
            TheaApiDTO<List<BaseCommonDTO>> allMedia = theaService.getAllMedia(token);
            if (!CommonMessage.SUCCESS.getCode().equals(allMedia.getResult())) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, allMedia.getMessage());
            }
            List<BaseCommonDTO> allMediaList = allMedia.getData();
            allMediaList = CollectionUtils.isEmpty(allMediaList) ? new ArrayList<>() : allMediaList;

            for (BaseCommonDTO baseCommonDTO : allMediaList) {
                if (baseCommonDTO != null && baseCommonDTO.getId() != null && mediaIds.contains(baseCommonDTO.getId())) {
                    Map<String, Object> e2 = new HashMap<>();
                    e2.put("id", baseCommonDTO.getId());
                    e2.put("name", baseCommonDTO.getName());
                    result.add(e2);
                }
            }
        }

        return result;
    }

    public void addCompanyMediaQueue(String token, Integer currentUserId, Integer companyid, Set<Integer> mediaIds) {

        // 获取指定一级吧其下的特殊队列
        CompanyMediaQueue e = new CompanyMediaQueue();
        e.setCompanyid(companyid);
        e.setStatus(CommonEnum.entity_status1.getCode());
        List<CompanyMediaQueue> companyMediaQueueList = companyMediaQueueMapper.select(e);
        Set<Integer> mediaIdsDB = CollectionUtils.isEmpty(companyMediaQueueList) ? new HashSet<>() : companyMediaQueueList.stream().filter(item -> item != null && item.getMediaid() != null).map(CompanyMediaQueue::getMediaid).collect(toSet());

        Collection nowData = CollectionUtils.subtract(mediaIds, mediaIdsDB);

        // 准备入库数据
        List<CompanyMediaQueue> toDB = new ArrayList<>(nowData.size());

        Iterator<Integer> iterator = nowData.iterator();
        Date now = new Date();
        while (iterator.hasNext()) {
            CompanyMediaQueue e2 = new CompanyMediaQueue();
            e2.setCreated(now);
            e2.setCreateid(currentUserId);
            e2.setUpdated(now);
            e2.setUpdateid(currentUserId);
            e2.setStatus(CommonEnum.entity_status1.getCode());
            e2.setCompanyid(companyid);
            e2.setMediaid(iterator.next());
            toDB.add(e2);
        }

        if (CollectionUtils.isNotEmpty(toDB)) this.insertList(toDB);
    }

    public void insertList(List<CompanyMediaQueue> data){
        // 包一层，批量插入，无值的字段，会插入null，便于后期处理
        companyMediaQueueMapper.insertList(data);
    }

    /**
     * 获取一级吧关注的queue、不包括总队列.
     */
    public Set<Integer> findFollowMediaidFromDB(Integer companyid){
        // 获取用户关注的媒体
        CompanyMediaQueue e = new CompanyMediaQueue();
        e.setCompanyid(companyid);
        e.setStatus(CommonEnum.entity_status1.getCode());
        List<CompanyMediaQueue> companyMediaQueueList = companyMediaQueueMapper.select(e);
        return CollectionUtils.isEmpty(companyMediaQueueList) ? new HashSet<>() : companyMediaQueueList.stream().map(CompanyMediaQueue::getMediaid).collect(toSet());

    }

    /**
     * 获取一级吧关注的queue、包括总队列.
     */
    public Set<Integer> findFollowMediaidAll(Integer companyid){
        // 获取用户关注的媒体
        Set<Integer> followMediaSet = this.findFollowMediaidFromDB(companyid);
        followMediaSet.add(CommonConst.COMPANY_MEDIA_QUEUE_COUNT);
        return followMediaSet;
    }

    public void delCompanyMediaQueue(Integer currentUserId, Integer companyid, Integer mediaId) {

        if (CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(mediaId)) {
            return;// 总队列不能删除
        }

        Date now = new Date();
        // 逻辑删除company_media_queue表数据
        CompanyMediaQueue valuesParams = new CompanyMediaQueue();
        valuesParams.setUpdated(now);
        valuesParams.setUpdateid(currentUserId);
        valuesParams.setStatus(CommonEnum.entity_status0.getCode());

        Example example = new Example(CompanyMediaQueue.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("companyid", companyid);
        c.andEqualTo("mediaid", mediaId);
        c.andEqualTo("status", CommonEnum.entity_status1.getCode());
        companyMediaQueueMapper.updateByExampleSelective(valuesParams, example);

        // 获取当月、下月字符串
        String currentMonthStr = this.allocateRedisService.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_CURRENT);
        String nextMonthStr = this.allocateRedisService.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_NEXT);

        // 对业务员分配数据的影响：当月的不动，下月的全初始化为0
        UserMonthInfo u = new UserMonthInfo();
        u.setCompanyid(companyid);
        u.setMediaid(mediaId);
        u.setEffectiveDate(nextMonthStr);
        u.setStatus(CommonEnum.entity_status1.getCode());
        List<UserMonthInfo> nextMontMediaDataList = userMonthInfoMapper.select(u);
        nextMontMediaDataList = CollectionUtils.isEmpty(nextMontMediaDataList) ? new ArrayList<>() : nextMontMediaDataList;

        Set<Integer> ids = nextMontMediaDataList.stream().map(UserMonthInfo::getId).collect(toSet());

        if (CollectionUtils.isNotEmpty(ids)) {
            UserMonthInfo valuesParams2 = new UserMonthInfo();
            valuesParams2.setBaseCustomerNum(0);
            valuesParams2.setRewardCustomerNum(0);
            valuesParams2.setLastUpdateUser(currentUserId);
            valuesParams2.setLastUpdateTime(now);

            Example whereParams = new Example(UserMonthInfo.class);
            Example.Criteria criteria = whereParams.createCriteria();
            criteria.andIn("id", ids);
            criteria.andEqualTo("status", CommonEnum.entity_status1.getCode());

            userMonthInfoMapper.updateByExampleSelective(valuesParams2, whereParams);
        }


        // 删除Redis队列
        allocateRedisService.delCompanyMediaQueueRedisQueue(companyid, mediaId, currentMonthStr);
        allocateRedisService.delCompanyMediaQueueRedisQueue(companyid, mediaId, nextMonthStr);
    }

    public void test() {

        CompanyMediaQueue e = new CompanyMediaQueue();
        e.setCompanyid(2);
        e.setMediaid(2);
        e.setCreated(new Date());

        //System.out.println(companyMediaQueueMapper.insertUseGeneratedKeys(e));
        //System.out.println(e.getId());
    }

}
