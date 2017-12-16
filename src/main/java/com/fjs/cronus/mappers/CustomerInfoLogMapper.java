package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInfoLog;
import com.fjs.cronus.util.MyMapper;

public interface CustomerInfoLogMapper extends MyMapper<CustomerInfoLog> {


    void  addCustomerLog(CustomerInfoLog record);


}