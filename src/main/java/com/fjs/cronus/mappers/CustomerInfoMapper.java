package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface CustomerInfoMapper extends MyMapper<CustomerInfo> {


    List <CustomerInfo> customerList(Map<String,Object> paramMap);

    Integer customerListCount(Map<String,Object> paramMap);

    void insertCustomer(CustomerInfo customerInfo);

    CustomerInfo findByFeild(Map<String,Object> paramMap);

    List<CustomerInfo> findCustomerListByFeild(Map<String,Object> paramMap);

    void updateCustomer(CustomerInfo customerInfo);

    List <CustomerInfo> getListByWhere(Map<String,Object> paramMap);

    List<Integer> findCustomerByType(Map<String,Object> paramMap);

    Integer getListByWhereCount(Map<String,Object> paramMap);

    List<CustomerInfo> findCustomerByOtherCity(Map<String,Object> paramMap);

    /**
     * 根据电话号码查找
     * @param paramMap
     * @return
     */
    List <CustomerInfo> selectByPhone(Map<String,Object> paramMap);
}