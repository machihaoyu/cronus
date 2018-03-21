package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInfoLog;
import com.fjs.cronus.util.MyMapper;

import java.util.List;

public interface CustomerInfoLogMapper extends MyMapper<CustomerInfoLog> {


    void  addCustomerLog(CustomerInfoLog record);

    Integer insertBatch(List<CustomerInfoLog> customerInfoLogList);

}