package com.fjs.cronus.service;

import com.fjs.cronus.dto.thea.CustomerUsefulDTO;
import com.fjs.cronus.mappers.CustomerUsefulMapper;
import com.fjs.cronus.model.CustomerUseful;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yinzf on 2017/10/14.
 */
@Service
public class CustomerUsefulService {
    @Autowired
    private CustomerUsefulMapper customerUsefulMapper;
    @Autowired
    private CustomerInfoService iCustomerService;

    /**
     * 根据交易id获取客户有效信息
     * @param loanId
     * @return
     */
    public CustomerUseful selectByLoanId(Integer loanId){
        CustomerUseful customerUseful=new CustomerUseful();
        customerUseful.setLoanId(loanId);
        return customerUsefulMapper.selectOne(customerUseful);
    }

    public Integer addCustomerUseful(CustomerUseful customerUseful){
        return customerUsefulMapper.insert(customerUseful);
    }

    //实体类转为DTO
    public CustomerUsefulDTO copyProperty(CustomerUseful customerUseful){
        CustomerUsefulDTO customerUsefulDTO=new CustomerUsefulDTO();
//        customerUsefulDTO.setId(customerUseful.getId());
//        customerUsefulDTO.setLoanId(customerUseful.getLoanId());
        customerUsefulDTO.setCustomerId(customerUseful.getCustomerId());
        customerUsefulDTO.setHouseStatus(customerUseful.getHouseStatus());
        customerUsefulDTO.setLoanAmount(customerUseful.getLoanAmount());
        customerUsefulDTO.setPurpose(customerUseful.getPurpose());
        customerUsefulDTO.setCreateTime(customerUseful.getLastUpdateTime());
        return customerUsefulDTO;
    }

    //实体类转为DTO
    public CustomerUseful copyProperty(CustomerUsefulDTO customerUsefulDTO){
        CustomerUseful customerUseful=new CustomerUseful();
        customerUseful.setId(customerUsefulDTO.getId());
        customerUseful.setLoanId(customerUsefulDTO.getLoanId());
        customerUseful.setCustomerId(customerUsefulDTO.getCustomerId());
        customerUseful.setHouseStatus(customerUsefulDTO.getHouseStatus());
        customerUseful.setLoanAmount(customerUsefulDTO.getLoanAmount());
        customerUseful.setPurpose(customerUsefulDTO.getPurpose());
        return customerUseful;
    }

    public List<CustomerUseful> countByMap(Map<String, Object> map){
        return customerUsefulMapper.countByMap(map);
    }

}
