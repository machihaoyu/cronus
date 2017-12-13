package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.util.MyMapper;
import java.util.List;
import java.util.Map;

/**
 * Created by yinzf on 2017/9/19.
 */
public interface CommunicationLogMapper extends MyMapper<CommunicationLog> {
    public Integer selectToday(Map<String, Object> map);

    public List<CommunicationLog> selectTodayCustomer(Map<String, Object> map);

    public List<CommunicationLog> selectHistoryCustomer(Map<String, Object> map);

    public List<CommunicationLog> selectByTime(Map<String, Object> map);
}
