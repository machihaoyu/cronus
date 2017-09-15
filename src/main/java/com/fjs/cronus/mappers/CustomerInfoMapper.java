package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface CustomerInfoMapper extends MyMapper<CustomerInfo> {


    List <CustomerInfo> customerList(Map<String,Object> paramMap);
    Integer customerListCount(Map<String,Object> paramMap);
    CustomerInfo insertCustomer(Map<String,Object> paramMap);
    CustomerInfo fingByFeild(Map<String,Object> paramMap);
    List<CustomerInfo> findCustomerListByFeild(Map<String,Object> paramMap);
}