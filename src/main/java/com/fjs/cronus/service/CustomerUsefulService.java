package com.fjs.cronus.service;

import com.fjs.cronus.dto.thea.CustomerUsefulDTO;
import com.fjs.cronus.mappers.CustomerUsefulMapper;
import com.fjs.cronus.model.CustomerUseful;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
     * @param customerId
     * @return
     */
    public CustomerUseful selectByCustomerId(Integer customerId){
        CustomerUseful customerUseful= null;
        Map<String,Object> map = new HashMap();
        map.put("customer_id",customerId);
        List<CustomerUseful> customerUsefuls = customerUsefulMapper.findByList(map);
        if (customerUsefuls != null && customerUsefuls.size() > 0){
            customerUseful = customerUsefuls.get(0);
        }
        return customerUseful;
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
        customerUseful.setCustomerId(customerUsefulDTO.getCustomerId());
        customerUseful.setHouseStatus(customerUsefulDTO.getHouseStatus());
        customerUseful.setLoanAmount(customerUsefulDTO.getLoanAmount());
        customerUseful.setPurpose(customerUsefulDTO.getPurpose());
        customerUseful.setPurposeDescribe(customerUsefulDTO.getPurposeDescribe());
        return customerUseful;
    }

    public List<CustomerUseful> countByMap(Map<String, Object> map){
        return customerUsefulMapper.countByMap(map);
    }

}
