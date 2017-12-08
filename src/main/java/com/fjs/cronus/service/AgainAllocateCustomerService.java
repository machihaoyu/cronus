package com.fjs.cronus.service;

import com.fjs.cronus.mappers.AgainAllocateCustomerMapper;
import com.fjs.cronus.model.AgainAllocateCustomer;
import com.fjs.cronus.model.CustomerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by feng on 2017/10/9.
 */

@Service
public class AgainAllocateCustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AgainAllocateCustomerMapper againAllocateCustomerMapper;

    /**
     * 根据数据ID保存再分配盘数据
     * @param
     * @return
     */
    public Integer saveStatusByDataId(Map<String, Object> map){
        return againAllocateCustomerMapper.saveStatusByDataId(map);
    }

    /**
     * 添加客戶到再分配池
     * @param againAllocateCustomer
     * @return
     */
    public Integer addAgainAllocateCustomer(AgainAllocateCustomer againAllocateCustomer){
        return againAllocateCustomerMapper.addAgainAllocateCustomer(againAllocateCustomer);
    }
}
