package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerSalePushLog;

import java.util.List;

/**
 * Created by feng on 2017/9/18.
 */
public interface CustomerSalePushLogMapper {

    public void insertList(List<CustomerSalePushLog> customerSalePushLogList);
}
