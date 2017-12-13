package com.fjs.cronus.mappers;


import com.fjs.cronus.model.CustomerMeet;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

/**
 * 描述：客户面见计划
 *
 * @author yinzf
 * @version 1.0 2017-09-20
 */
public interface CustomerMeetMapper extends MyMapper<CustomerMeet> {


    List<CustomerMeet> findByFeild(Map<String,Object> paramsMap);

    List<CustomerMeet> selectByTime(Map<String,Object> paramsMap);
}