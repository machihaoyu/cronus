package com.fjs.cronus.service;

import com.fjs.cronus.mappers.CustomerSalePushLogMapper;
import com.fjs.cronus.model.CustomerSalePushLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by feng on 2017/9/18.
 */
@Service
public class CustomerSalePushLogService {

    @Autowired
    private CustomerSalePushLogMapper customerSalePushLogMapper;

    /**
     * 批量添加日志
     * @param customerSalePushLogList
     * @return
     */
    public void insertList(List<CustomerSalePushLog> customerSalePushLogList){
        customerSalePushLogMapper.insertList(customerSalePushLogList);
//        return count;
    }
}