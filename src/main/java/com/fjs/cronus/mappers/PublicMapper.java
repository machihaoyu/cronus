package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/27.
 */
public interface PublicMapper {

    @Select("SELECT id from customer_info c where c.own_user_id=-2 AND DATE_ADD(c.view_time, Interval 5 DAY) < NOW() ORDER BY create_time DESC LIMIT 0,500")
    List<Integer> getCustomersFromDiscard();

    /**
     * 获取公盘优选客户 own_user_id = -3
     * @return
     */
    @Select("SELECT * from customer_info c where c.own_user_id = -3 limit #{start},#{size} ")
    @Results(id = "getPublicSelect", value = {
            @Result(property = "customerName", column = "customer_name"),
            @Result(property = "customerLevel", column = "customer_level"),
            @Result(property = "loanAmount", column = "loan_amount"),
            @Result(property = "houseStatus", column = "house_status"),
            @Result(property = "customerSource", column = "customer_source"),
            @Result(property = "utmSource", column = "utm_source"),
            @Result(property = "ownUserName", column = "own_user_name"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "firstAllocateTime", column = "first_allocate_time"),
            @Result(property = "cooperationStatus", column = "cooperation_status"),
            @Result(property = "communicateTime", column = "communicate_time")
    })
    List<CustomerInfo> getPublicSelect(@Param("start") Integer start,@Param("size") Integer size);

    /**
     * 获取公盘优选客户数
     * @return
     */
    @Select("SELECT count(1) FROM customer_info c WHERE c.own_user_id = -3")
    Integer getPublicSelectCount();

}
