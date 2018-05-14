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

    public List<CustomerSalePushLog> findPageData(CustomerSalePushLog params, Integer pageNum, Integer pageSize) {

        if (pageNum <= 0) pageNum = 0;
        if (pageSize <= 0) pageSize = 10;
        List<CustomerSalePushLog> pageData = customerSalePushLogMapper.findPageData(params, pageNum*pageSize, pageSize);
        return pageData;
    }
}
