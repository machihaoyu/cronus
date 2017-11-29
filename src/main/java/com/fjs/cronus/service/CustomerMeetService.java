package com.fjs.cronus.service;

import com.fjs.cronus.mappers.CustomerMeetMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yinzf on 2017/10/14.
 */
@Service
public class CustomerMeetService {
    @Autowired
    private CustomerMeetMapper customerMeetMapper;
    @Autowired
    private CustomerInfoService loanService;

 /*   @Transactional
    public Integer addCustomerMeet(CustomerMeetDTO customerMeetDTO, UserInfoDTO userInfoDTO, Loan loan){
        Date date = new Date();
        CustomerMeet customerMeet = copyProperty(customerMeetDTO);
        customerMeet.setCreateTime(date);
        customerMeet.setLastUpdateTime(date);
        Integer userId = Integer.parseInt(userInfoDTO.getUser_id());
        customerMeet.setCreateUser(userId);
        customerMeet.setLastUpdateUser(userId);
        customerMeet.setIsDeleted(CommonConst.DATA_NORMAIL);

       *//* loan.setStatus(CommonConst.LOAN_STATUS_MEET);
        loanService.update(loan,userInfoDTO);*//*
        return customerMeetMapper.insert(customerMeet);
    }

    *//**
     * 根据交易id查找面见记录
     * @param
     * @return
     *//*
    public List<CustomerMeet> listByLoanId(Integer loanId, String token){
        Example example=new Example(CustomerMeet.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("loanId",loanId);
        example.setOrderByClause("create_time desc");
        return customerMeetMapper.selectByExample(example);
    }

    //将DTO转为实体
    public CustomerMeet copyProperty(CustomerMeetDTO customerMeetDTO){
        CustomerMeet customerMeet=new CustomerMeet();
        customerMeet.setLoanId(customerMeetDTO.getLoanId());
        customerMeet.setMeetTime(customerMeetDTO.getMeetTime());
        customerMeet.setContent(customerMeetDTO.getContent());
        customerMeet.setCustomerId(customerMeetDTO.getCustomerId());
        return customerMeet;
    }

    public CustomerMeetDTO copyProperty(CustomerMeet customerMeet){
        CustomerMeetDTO customerMeetDTO=new CustomerMeetDTO();
        customerMeetDTO.setId(customerMeet.getId());
        customerMeetDTO.setMeetTime(customerMeet.getMeetTime());
        return customerMeetDTO;
    }*/
}
