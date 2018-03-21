package com.fjs.cronus.mappers;


import com.fjs.cronus.model.CustomerInterviewCarInfo;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface CustomerInterviewCarInfoMapper  extends MyMapper<CustomerInterviewCarInfo> {

    List<CustomerInterviewCarInfo> findByCustomerInterviewCarByFeild(Map<String,Object> paramsMap);

    void addCustomerInteviewCarInfo(CustomerInterviewCarInfo customerInterviewCarInfo);

    CustomerInterviewCarInfo findByCustomerByFeild(Map<String,Object> paramsMap);

    void updateCustomerInteviewCarInfo(CustomerInterviewCarInfo customerInterviewCarInfo);

}