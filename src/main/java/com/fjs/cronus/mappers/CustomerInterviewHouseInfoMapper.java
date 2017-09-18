package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInterviewHouseInfo;
import com.fjs.cronus.util.MyMapper;

import javax.crypto.interfaces.PBEKey;
import java.util.List;
import java.util.Map;

public interface CustomerInterviewHouseInfoMapper extends MyMapper<CustomerInterviewHouseInfo> {

    List<CustomerInterviewHouseInfo> findByCustomerInterviewByFeild(Map<String,Object> paramsMap);
    void addCustomerInterviewHouseInfo(CustomerInterviewHouseInfo customerInterviewHouseInfo);
}