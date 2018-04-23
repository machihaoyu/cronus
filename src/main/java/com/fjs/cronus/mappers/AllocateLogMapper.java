package com.fjs.cronus.mappers;

import com.fjs.cronus.dto.customer.CustomerComDTO;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.util.MyMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/9/20.
 */
public interface AllocateLogMapper extends MyMapper<AllocateLog> {

    //添加日志
    public Integer insertOne(AllocateLog allocateLog);

    public List<AllocateLog> selectByParamsMap(Map<String, Object> map);

    public Integer insertBatch(List<AllocateLog> allocateLogs);

    public Integer selectToday(Map<String, Object> map);

    public Integer selectCommunicateToday(Map<String, Object> map);

    public Integer selectCommunicateHistory(Map<String, Object> map);

    public List<AllocateLog> getNewestAllocateLogByCustomerIds(Map<String,Object> paramsMap);

    public Integer receiveCountByWhere(Map<String,Object> paramsMap);


    /**
     * t统计业务员分配的
     * @param paramsMap
     * @return
     */
    public List<Integer> getReceiveCount(Map<String,Object> paramsMap);

    public List<Integer> receiveIds(Map<String,Object> paramsMap);

    public List<AllocateLog> selectByCustomerId (Map<String,Object> paramsMap);

    List<CustomerComDTO> getHistoryCount(Map<String, Object> paramMap);


    List<CustomerComDTO> getTodayCount(Map<String, Object> paramMap);

    Integer getCountByCondition(Integer customerId);
}
