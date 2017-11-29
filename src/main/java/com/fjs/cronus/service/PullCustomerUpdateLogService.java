package com.fjs.cronus.service;

import com.fjs.cronus.mappers.PullCustomerUpdateLogMapper;
import com.fjs.cronus.model.PullCustomer;
import com.fjs.cronus.model.PullCustomerUpdateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yinzf on 2017/10/31.
 */
@Service
public class PullCustomerUpdateLogService {
    @Autowired
    private PullCustomerUpdateLogMapper pullCustomerUpdateLogMapper;

    public Integer addLog(PullCustomer pullCustomer, Integer userIsd, String operation){
        Date date=new Date();
        PullCustomerUpdateLog pullCustomerUpdateLog=copyProperty(pullCustomer);
        pullCustomerUpdateLog.setCreateUser(userIsd);
        pullCustomerUpdateLog.setCreateTime(date);
        pullCustomerUpdateLog.setOperation(operation);
        return pullCustomerUpdateLogMapper.insert(pullCustomerUpdateLog);
    }

    public PullCustomerUpdateLog copyProperty(PullCustomer pullCustomer){
        PullCustomerUpdateLog pullCustomerUpdateLog=new PullCustomerUpdateLog();
        pullCustomerUpdateLog.setPullCustId(pullCustomer.getId());
        pullCustomerUpdateLog.setSaleId(pullCustomer.getSaleId());
        pullCustomerUpdateLog.setName(pullCustomer.getName());
        pullCustomerUpdateLog.setTelephone(pullCustomer.getTelephone());
        pullCustomerUpdateLog.setLoanAmount(pullCustomer.getLoanAmount());
        pullCustomerUpdateLog.setCity(pullCustomer.getCity());
        pullCustomerUpdateLog.setCustomerSource(pullCustomer.getCustomerSource());
        pullCustomerUpdateLog.setUtmSource(pullCustomer.getUtmSource());
        pullCustomerUpdateLog.setStatus(pullCustomer.getStatus());
        pullCustomerUpdateLog.setExendText(pullCustomer.getExtendText());

        return pullCustomerUpdateLog;
    }
}
