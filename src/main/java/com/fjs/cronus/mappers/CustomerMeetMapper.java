package com.fjs.cronus.mappers;


import com.fjs.cronus.model.CustomerMeet;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：客户面见计划
 *
 * @author yinzf
 * @version 1.0 2017-09-20
 */
public interface CustomerMeetMapper extends MyMapper<CustomerMeet> {


    List<CustomerMeet> findByFeild(Map<String,Object> paramsMap);

    List<CustomerMeet> selectByTime(Map<String,Object> paramsMap);

    /**
     * 查询根据指定时间、顾客id最近的面见记录.
     */
    @Results({
        @Result(column = "loan_id", property = "loanId"),
        @Result(column = "customer_id", property = "customerId"),
        @Result(column = "meet_time", property = "meetTime"),
        @Result(column = "create_time", property = "createTime"),
        @Result(column = "last_update_time", property = "lastUpdateTime"),
        @Result(column = "create_user", property = "createUser"),
        @Result(column = "last_update_user", property = "lastUpdateUser"),
        @Result(column = "is_deleted", property = "isDeleted"),
    })
    @Select("SELECT * FROM customer_meet WHERE customer_id=#{customerId} AND is_deleted=0 AND `create_time` >= #{createTime,jdbcType=TIMESTAMP} ORDER BY create_time ASC LIMIT 1")
    CustomerMeet getByCustomerId(@Param("customerId") Integer customerId, @Param("createTime") Date createTime);

    /**
     * 查询该业务员，指定时间内面见客户数.
     */
    @Select("SELECT COUNT(DISTINCT`customer_id`) FROM `customer_meet` WHERE `meet_time` >= #{start} AND `meet_time` <= #{end} AND `create_user`=#{salesmanId} AND `is_deleted`=0")
    Integer getCountCustomerIdByCreateId(@Param("salesmanId") Integer salesmanId, @Param("start") Date start, @Param("end") Date end);

    /**
     * 查询指定时间范围内的面见记录.
     */
    @Results({
            @Result(column = "loan_id", property = "loanId"),
            @Result(column = "customer_id", property = "customerId"),
            @Result(column = "meet_time", property = "meetTime"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "last_update_time", property = "lastUpdateTime"),
            @Result(column = "create_user", property = "createUser"),
            @Result(column = "last_update_user", property = "lastUpdateUser"),
            @Result(column = "is_deleted", property = "isDeleted"),
    })
    @Select("SELECT * FROM customer_meet WHERE  is_deleted=0 AND `create_time` >= #{start,jdbcType=TIMESTAMP} AND `create_time` <= #{end,jdbcType=TIMESTAMP} ")
    List<CustomerMeet> findBydTime(@Param("start") Date start, @Param("end") Date end);
}