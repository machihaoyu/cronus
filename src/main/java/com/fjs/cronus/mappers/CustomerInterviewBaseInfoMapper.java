package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInterviewBaseInfo;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;


public interface CustomerInterviewBaseInfoMapper extends MyMapper<CustomerInterviewBaseInfo> {

    List<CustomerInterviewBaseInfo>  customerInterviewList (Map<String, Object> paramsMap);
}