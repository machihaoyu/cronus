package com.fjs.cronus.mappers;

import com.fjs.cronus.dto.cronus.RepeatCustomerSaleDTO;
import com.fjs.cronus.model.CustomerSalePushLog;

import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/9/18.
 */
public interface CustomerSalePushLogMapper {

    public void insertList(List<CustomerSalePushLog> customerSalePushLogList);

    public List<RepeatCustomerSaleDTO> repeatcustomerList(Map<String,Object> paramsMap);

    public Integer repeatcustomerListConut(Map<String,Object> paramsMap);

    public List<CustomerSalePushLog> findByFeild(Map<String,Object> paramsMap);
}
