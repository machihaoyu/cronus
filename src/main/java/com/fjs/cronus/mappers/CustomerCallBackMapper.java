package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInfo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by qiaoxin on 2018/3/19.
 */
public interface CustomerCallBackMapper {

    List<CustomerInfo> customerList(Map<String,Object> paramMap);

    long customerListCount(Map<String,Object> paramMap);

    CustomerInfo selectCustomerById(@Param("customerId")Integer customerId);

    Integer updateCallbackStatus(@Param("callback_status")String callback_status,
                                 @Param("per_description")String per_description,
                                 @Param("customerId")Integer customerId);
}
