package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInterviewInsuranceInfo;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface CustomerInterviewInsuranceInfoMapper extends MyMapper<CustomerInterviewInsuranceInfo> {

    List<CustomerInterviewInsuranceInfo> findByCustomerInterviewInsurByFeild(Map<String,Object> paramsMap);

    void addCustomerInsura(CustomerInterviewInsuranceInfo customerInterviewInsuranceInfo);

    CustomerInterviewInsuranceInfo findByFeild(Map<String,Object> paramsMap);

    void updateCustomerInsura(CustomerInterviewInsuranceInfo customerInterviewInsuranceInfo);
}