package com.fjs.cronus.mappers;

import com.fjs.cronus.mappers.provider.PanDataProvider;
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
     * 获取公盘优选客户、商机盘、扔回公盘客户。。
     * @return
     */
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
    @SelectProvider(type = PanDataProvider.class, method = "getPublicSelect")
    List<CustomerInfo> getPublicSelect(@Param("ownUser") Integer ownUser,
                                       @Param("start") Integer start,
                                       @Param("size") Integer size,
                                       @Param("order") String order,
                                       @Param("telephone") String telephone,
                                       @Param("customerName") String customerName,
                                       @Param("subCompanyIds") List<Integer> subCompanyIds,
                                       @Param("canMangerMainCity") List<String> canMangerMainCity);

    /**
     * 获取公盘客户数
     * @return
     */
    @SelectProvider(type = PanDataProvider.class, method = "getPublicSelectCount")
    Integer getPublicSelectCount(@Param("ownUser") Integer ownUser,
                                 @Param("telephone") String telephone,
                                 @Param("customerName") String customerName,
                                 @Param("subCompanyIds") List<Integer> subCompanyIds,
                                 @Param("canMangerMainCity") List<String> canMangerMainCity);

}
