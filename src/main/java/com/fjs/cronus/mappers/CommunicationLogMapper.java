package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.*;

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

    public List<Integer> allocateCommunication(Map<String, Object> map);

    public List<CommunicationLog>  queryByCustomerId(Map<String, Object> map);

    @Results({
            @Result(column = "loan_id", property = "loanId"),
            @Result(column = "customer_id", property = "customerId"),
            @Result(column = "create_user", property = "createUser"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "house_status", property = "houseStatus"),
            @Result(column = "loan_amount", property = "loanAmount"),
            @Result(column = "next_contact_time", property = "nextContactTime"),
            @Result(column = "next_contact_content", property = "nextContactContent"),
    })
    @Select("SELECT * FROM `communication_log` WHERE `customer_id`=#{customerId} ORDER BY `create_time` DESC LIMIT 1")
    CommunicationLog getByCustomerId(@Param("customerId") Integer customerId);
}
