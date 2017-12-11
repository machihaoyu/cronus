package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.cronus.AddCustomerMeetDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.CustomerMeetDTO;
import com.fjs.cronus.mappers.CustomerMeetMapper;

import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerMeet;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.uc.UcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by yinzf on 2017/10/14.
 */
@Service
public class CustomerMeetService {
    @Autowired
    private CustomerMeetMapper customerMeetMapper;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    UcService ucService;
    @Autowired
    TheaService theaService;
    @Transactional
    public Integer addCustomerMeet(AddCustomerMeetDTO customerMeetDTO, PHPLoginDto userInfoDTO, CustomerInfo customerInfo,String token){
        Date date = new Date();
        CustomerMeet customerMeet=new CustomerMeet();
        customerMeet.setMeetTime(customerMeetDTO.getMeetTime());
        customerMeet.setContent(customerMeetDTO.getContent());
        customerMeet.setCustomerId(customerMeetDTO.getCustomerId());
        customerMeet.setCreateTime(date);
        customerMeet.setLastUpdateTime(date);
        Integer userId = Integer.parseInt(userInfoDTO.getUser_info().getUser_id());
        customerMeet.setCreateUser(userId);
        customerMeet.setLastUpdateUser(userId);
        customerMeet.setIsDeleted(CommonConst.DATA_NORMAIL);
        //开始更新交易的状态
        TheaApiDTO resultDTO = theaService.changeStatusByCustomerId(token,customerMeetDTO.getCustomerId());
        return customerMeetMapper.insert(customerMeet);
    }


    public List<CustomerMeet> listByCustomerId(Integer customerId, String token){
        Example example=new Example(CustomerMeet.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("customerId",customerId);
        example.setOrderByClause("create_time desc");
        return customerMeetMapper.selectByExample(example);
    }

    public CustomerMeetDTO copyProperty(CustomerMeet customerMeet,String token){
        CustomerMeetDTO customerMeetDTO=new CustomerMeetDTO();
        customerMeetDTO.setId(customerMeet.getId());
        customerMeetDTO.setMeetTime(customerMeet.getMeetTime());
        customerMeetDTO.setContent(customerMeet.getContent());
        customerMeetDTO.setUserId(customerMeet.getCreateUser());
        customerMeetDTO.setCustomerId(customerMeet.getCustomerId());
        UcUserDTO ucUserDTO = ucService.getUserInfoByID(token,customerMeet.getCreateUser());
        customerMeetDTO.setUserName(ucUserDTO.getName());
        customerMeetDTO.setCreateTime(customerMeet.getCreateTime());
        return customerMeetDTO;
    }
}
