package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.dto.thea.AllocateLogDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.mappers.AllocateLogMapper;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerInfoLog;
import com.fjs.cronus.util.EntityToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by feng on 2017/9/20.
 */
@Service
public class AllocateLogService {

    @Autowired
    private AllocateLogMapper allocateLogMapper;
    @Autowired
    CustomerInfoLogMapper customerInfoLogMapper;

    /**
     * 添加记录
     *
     * @param allocateLog
     * @return
     */
    public Integer insertOne(AllocateLog allocateLog) {
        Integer count = allocateLogMapper.insertOne(allocateLog);
        return count;
    }

    public List<AllocateLog> selectByParamsMap(Map<String, Object> map) {
        List<AllocateLog> allocateLogList = new ArrayList<>();
        allocateLogList = allocateLogMapper.selectByParamsMap(map);
        return allocateLogList;
    }

    /**
     * @param customerInfo
     * @param newOwnerUserId
     * @param operationCode
     * @param userInfoDTO
     * @return
     */
    @Transactional
    public boolean addAllocatelog(CustomerInfo customerInfo, Integer newOwnerUserId, Integer operationCode, UserInfoDTO userInfoDTO) {
        Date date = new Date();
        boolean flag = false;
        if (null == customerInfo.getId() || 0 == customerInfo.getId()) {
            return flag;
        }
        //添加分配日志
        AllocateLog allocateLog = new AllocateLog();
        allocateLog.setCreateTime(new Date());
        allocateLog.setCustomerId(customerInfo.getId());
        allocateLog.setOldOwnerId(customerInfo.getOwnUserId());
        allocateLog.setNewOwnerId(newOwnerUserId);

        //添加客户操作日志
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setIsDeleted(0);

        if (null != userInfoDTO) {
            allocateLog.setCreateUserId(Integer.valueOf(userInfoDTO.getUser_id()));
            allocateLog.setCreateUserName(userInfoDTO.getName());

            customerInfoLog.setLogUserId(Integer.valueOf(userInfoDTO.getUser_id()));
            customerInfoLog.setLogCreateTime(new Date());
        } else {
            allocateLog.setCreateUserId(CommonConst.SYSTEM_ID);
            allocateLog.setCreateUserName(CommonConst.SYSTEM_NAME);
        }
        switch (operationCode) {
            case 1:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCodeDesc());
                customerInfoLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCodeDesc());
                break;
            case 2:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_2.getCodeDesc());
                customerInfoLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_2.getCodeDesc());
                break;
            case 3:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCodeDesc());
                customerInfoLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCodeDesc());
                break;
            case 4:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_4.getCodeDesc());
                customerInfoLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_4.getCodeDesc());
                break;
            case 5:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCodeDesc());
                customerInfoLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCodeDesc());
                break;
            case 6:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_6.getCodeDesc());
                customerInfoLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_6.getCodeDesc());
                break;
            default:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_7.getCodeDesc());
                customerInfoLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_7.getCodeDesc());
                break;
        }
        Integer insertAllocateLog = allocateLogMapper.insert(allocateLog);
        if (null != insertAllocateLog) {
            //TODO 发送分配短信
            customerInfoLogMapper.addCustomerLog(customerInfoLog);
            flag = true;
        }
        return flag;
    }

    @Transactional
    public Integer autoAllocateAddAllocatelog(Integer customerId, Integer newOwnerUserId, Integer operationCode) {
        Date date = new Date();
        //添加分配日志
        AllocateLog allocateLog = new AllocateLog();
        allocateLog.setCreateTime(new Date());
        allocateLog.setCustomerId(customerId);
        allocateLog.setNewOwnerId(newOwnerUserId);

        allocateLog.setCreateUserId(CommonConst.SYSTEM_ID);
        allocateLog.setCreateUserName(CommonConst.SYSTEM_NAME);
        switch (operationCode) {
            case 1:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCodeDesc());
                break;
            case 2:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_2.getCodeDesc());
                break;
            case 3:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCodeDesc());
                break;
            case 4:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_4.getCodeDesc());
                break;
            case 5:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCodeDesc());
                break;
            case 6:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_6.getCodeDesc());
                break;
            default:
                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_7.getCodeDesc());
                break;
        }
        return allocateLogMapper.insert(allocateLog);
    }

    public Integer insertBatch(List<AllocateLog> allocateLogs) {
        return allocateLogMapper.insertBatch(allocateLogs);
    }

    //根据条件查找分配日志
    public List<AllocateLog> listByCondition(Integer customerId) {
        Example example = new Example(AllocateLog.class);
        Example.Criteria criteria = example.createCriteria();
        if (customerId != null) {
            criteria.andEqualTo("customerId", customerId);
        }
        example.setOrderByClause("create_time desc");
        return allocateLogMapper.selectByExample(example);
    }

    public AllocateLogDTO copyProperty(AllocateLog allocateLog) {
        AllocateLogDTO allocateLogDTO = new AllocateLogDTO();
        allocateLogDTO.setOperation(allocateLog.getOperation());
        allocateLogDTO.setOldOwnerId(allocateLog.getOldOwnerId());
        allocateLogDTO.setNewOwnerId(allocateLog.getNewOwnerId());
        allocateLogDTO.setCreateUserName(allocateLog.getCreateUserName());
        allocateLogDTO.setCreateTime(allocateLog.getCreateTime());
        return allocateLogDTO;
    }


    /**
     * 统计今天分配客户数
     *
     * @param userId
     * @return
     */
    public Integer getTodayData(List<String> userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", userId);
        Integer todayCount = 0;
        todayCount = allocateLogMapper.selectToday(map);
        return todayCount;
    }

    /**
     * 统计历史分配客户数
     *
     * @param userId
     * @return
     */
    public Integer getHistoryData(List<String> userId, String start, String end) {
        Example example = new Example(AllocateLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("newOwnerId", userId);
        criteria.andBetween("createTime", start, end);
        Integer historyCount = 0;
        historyCount = allocateLogMapper.selectCountByExample(example);
        return historyCount;
    }

    /**
     * 统计今天分配沟通客户数
     *
     * @param userId
     * @return
     */
    public Integer getTodayCommunicateData(List<String> userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", userId);
        Integer todayCount = 0;
        todayCount = allocateLogMapper.selectCommunicateToday(map);
        return todayCount;
    }

    /**
     * 统计历史分配客户数
     *
     * @param userId
     * @return
     */
    public Integer getHistoryCommunicateData(List<String> userId, String start, String end) {
        Map<String, Object> map = new HashMap<>();
        map.put("newOwnerId", userId);
        map.put("createTimeBegin", start);
        map.put("createTimeEnd", end);
        Integer todayCount = 0;
        todayCount = allocateLogMapper.selectCommunicateHistory(map);
        return todayCount;
    }


    public Map<Integer, AllocateLog> getNewestAllocateLogByCustomerIds(String customerIds) {
        Map<Integer, AllocateLog> allocateLogs = new HashMap<>();
        Map<String, Object> paramsMap = new HashMap<>();
        if (StringUtils.isEmpty(customerIds)) {
            return allocateLogs;
        }
        List<Integer> ids = new ArrayList<>();
        String[] strArray = null;
        strArray = customerIds.split(",");
        for (int i = 0; i < strArray.length; i++) {
            ids.add(Integer.parseInt(strArray[i]));
        }
        paramsMap.put("paramsList", ids);
        List<AllocateLog> allocateLogList = allocateLogMapper.getNewestAllocateLogByCustomerIds(paramsMap);
        if (allocateLogList != null && allocateLogList.size() > 0) {
            for (AllocateLog allocateLog : allocateLogList) {
                Map<Integer, AllocateLog> map = new HashMap<>();
                map.put(allocateLog.getCustomerId(), allocateLog);
            }
            return allocateLogs;
        }
        return allocateLogs;
    }

    public boolean newestAllocateLog(Integer customerId){
        boolean flag = false;
        AllocateLog allocateLog = new AllocateLog();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("customerId",customerId);
        List<AllocateLog> allocateLogs = allocateLogMapper.selectByCustomerId(paramsMap);
        if (allocateLogs != null && allocateLogs.size() > 0){
            allocateLog = allocateLogs.get(0);
            //
            String operation = allocateLog.getOperation();
            if ("自动分配".equals(operation) || "未沟通分配".equals(operation)){
                flag = true;
                return  flag;
            }
        }
        return flag;

    }
}
