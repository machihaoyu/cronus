package com.fjs.cronus.mappers;

import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.util.MyMapper;

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
}
