package com.fjs.cronus.service;


import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.thea.CommunicationLogDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;

import com.fjs.cronus.dto.thea.CustomerUsefulDTO;
import com.fjs.cronus.mappers.CommunicationLogMapper;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.mappers.CustomerMeetMapper;
import com.fjs.cronus.model.*;
import com.fjs.cronus.util.EntityToDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinzf on 2017/9/19.
 */
@Service
public class CommunicationLogService {
    @Autowired
    private CommunicationLogMapper communicationLogMapper;
/*    @Autowired
    private LoanService loanService;*/
    @Autowired
    private CustomerInfoService customerService;
    @Autowired
    private CustomerUsefulService customerUsefulService;
    @Autowired
    private CustomerMeetMapper customerMeetMapper;
    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    CustomerInfoLogMapper customerInfoLogMapper;

    //添加
    @Transactional
    public Integer addLog(CustomerUsefulDTO customerUsefulDTO, CustomerInfo customerDto, UserInfoDTO userInfoDTO, String token){
        Date date=new Date();
        //修改客户
        customerUsefulDTO.setHouseStatus(customerUsefulDTO.getHouseStatus());
        //修改客户
        //有效客户
        if(customerUsefulDTO.getLoanAmount() != null){
            CustomerUseful customerUseful = customerUsefulService.selectByCustomerId(customerUsefulDTO.getCustomerId());
            if (customerUseful == null){
                customerUseful=customerUsefulService.copyProperty(customerUsefulDTO);
                customerUseful.setUsefulTime(date);
                customerUseful.setCreateTime(date);
                customerUseful.setLastUpdateTime(date);
                customerUseful.setIsDeleted(CommonConst.DATA_NORMAIL);
                if (StringUtils.isNotEmpty(userInfoDTO.getUser_id() )){
                    customerUseful.setCreateUser(Integer.parseInt(userInfoDTO.getUser_id()));
                    customerUseful.setLastUpdateUser(Integer.parseInt(userInfoDTO.getUser_id()));
                }
                customerUsefulService.addCustomerUseful(customerUseful);
            }
        }
        CustomerMeet customerMeet=new CustomerMeet();
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id() )){
            customerDto.setLastUpdateUser(Integer.parseInt(userInfoDTO.getUser_id()));

            customerMeet.setCreateUser(Integer.parseInt(userInfoDTO.getUser_id()));
            customerMeet.setLastUpdateUser(Integer.parseInt(userInfoDTO.getUser_id()));
        }
        customerDto.setLoanAmount(customerUsefulDTO.getLoanAmount());
        customerDto.setHouseStatus(customerUsefulDTO.getHouseStatus());
        customerDto.setCooperationStatus(customerUsefulDTO.getCooperationStatus());
        //判断是否是首次沟通
        List<CommunicationLog> communicationLogList = listByCustomerId(customerUsefulDTO.getCustomerId(),token);
        if (communicationLogList.size() == 0){
            customerDto.setFirstCommunicateTime(date);
        }
        customerDto.setCommunicateTime(date);
        //确认状态
        if (customerUsefulDTO.getLoanAmount() != null && customerUsefulDTO.getLoanAmount().intValue() > 0){
            customerDto.setConfirm(CommonConst.CONFIRM__STATUS_EFFECT);
        }
        if (customerUsefulDTO.getLoanAmount() != null && customerUsefulDTO.getLoanAmount().intValue() == 0){
            customerDto.setConfirm(CommonConst.CONFIRM__STATUS_NO);
        }
//        loan.setStatus(CommonConst.LOAN_STATUS_COMMUNICATION);
        customerDto.setLastUpdateTime(date);
        customerDto.setLastUpdateUser(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfoMapper.updateCustomer(customerDto);
        //插入客户日志表
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerDto,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("编辑交易信息");
        customerInfoLog.setLogUserId(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);

        //面见
        if (customerUsefulDTO.getIsMeet() != null && customerUsefulDTO.getIsMeet() == CommonConst.IS_MEET__YES){
            customerMeet.setCustomerId(customerUsefulDTO.getCustomerId());
            customerMeet.setMeetTime(customerUsefulDTO.getMeetTime());
            customerMeet.setCreateTime(date);
            customerMeet.setLastUpdateTime(date);
            customerMeet.setIsDeleted(CommonConst.DATA_NORMAIL);
            customerMeetMapper.insert(customerMeet);
            //发送一条短信
        }

        //沟通日志
        CommunicationLog communicationLog=new CommunicationLog();
        Integer userId=null;
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            userId=Integer.parseInt(userInfoDTO.getUser_id());
        }
        communicationLog.setCustomerId(customerDto.getId());
        communicationLog.setHouseStatus(customerUsefulDTO.getHouseStatus());
        communicationLog.setLoanAmount(customerUsefulDTO.getLoanAmount());
        communicationLog.setType(CommonConst.COMMUNICATION_LOG_TYPE0);
        communicationLog.setContent(customerUsefulDTO.getContent());
        communicationLog.setCreateUser(userId);
        communicationLog.setCreateTime(date);
        communicationLog.setNextContactTime(customerUsefulDTO.getNextContactTime());
        return communicationLogMapper.insert(communicationLog);

    }

    public CustomerUsefulDTO findByCustomerId(Integer customerId){
        Map<String,Object> paramsMap = new HashMap<>();
        Example example=new Example(CommunicationLog.class);
        CustomerUsefulDTO customerUsefulDTO = new CustomerUsefulDTO();
        CommunicationLog communicationLog = new CommunicationLog();
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("customerId",customerId);
        example.setOrderByClause("create_time desc");
        List<CommunicationLog> communicationLogList = communicationLogMapper.selectByExample(example);
        //取最近的一次
        if (communicationLogList != null && communicationLogList.size() > 0){
            communicationLog = communicationLogList.get(0);
        }
        CustomerInfo customerInfo = customerService.findCustomerById(customerId);
        //查询userful
        CustomerUseful customerUseful = customerUsefulService.selectByCustomerId(customerId);
        customerUsefulDTO.setId(communicationLog.getId());
        customerUsefulDTO.setContent(communicationLog.getContent());
        customerUsefulDTO.setCooperationStatus(customerInfo.getCooperationStatus());
        customerUsefulDTO.setCreateTime(communicationLog.getCreateTime());
        customerUsefulDTO.setCustomerId(customerId);
        customerUsefulDTO.setHouseStatus(customerUseful.getHouseStatus());
        //查询面见表 最新的一条
        paramsMap.put("customerId",customerId);
        List<CustomerMeet> customerMeets = customerMeetMapper.findByFeild(paramsMap);
        if (customerMeets == null || customerMeets.size() == 0){
            customerUsefulDTO.setIsMeet(CommonConst.IS_MEET_NO);
        }else {
            CustomerMeet customerMeet = customerMeets.get(0);
            customerUsefulDTO.setIsMeet(CommonConst.IS_MEET__YES);
            customerUsefulDTO.setMeetTime(customerMeet.getMeetTime());
        }

        customerUsefulDTO.setTelephonenumber(customerInfo.getTelephonenumber());
        customerUsefulDTO.setLoanAmount(customerUseful.getLoanAmount());
        customerUsefulDTO.setNextContactTime(communicationLog.getNextContactTime());
        customerUsefulDTO.setPurpose(customerUseful.getPurpose());
        customerUsefulDTO.setPurposeDescribe(customerUseful.getPurposeDescribe());

        return customerUsefulDTO;
    }

    /**
     * 根据客户id查找沟通日志
     * @param
     * @return
     */
    public List<CommunicationLog> listByCustomerId(Integer customerId, String token){
        Example example=new Example(CommunicationLog.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("customerId",customerId);
        example.setOrderByClause("create_time desc");
        return communicationLogMapper.selectByExample(example);
    }

    public List<CommunicationLog> listByCustomerIdAndUserId(Integer customerId, Integer usertId,String token){
        Example example=new Example(CommunicationLog.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("customerId",customerId);
        criteria.andEqualTo("createUser",usertId);
        example.setOrderByClause("create_time desc");
        return communicationLogMapper.selectByExample(example);
    }


    public CommunicationLogDTO copyProperty(CommunicationLog communicationLog , CustomerUseful customerUseful){
        CommunicationLogDTO communicationLogDTO=new CommunicationLogDTO();
        communicationLogDTO.setHouseStatus(communicationLog.getHouseStatus());
        communicationLogDTO.setLoanAmount(communicationLog.getLoanAmount());
        communicationLogDTO.setPurpose(customerUseful.getPurpose());
        communicationLogDTO.setCreateTime(communicationLog.getCreateTime());
        return communicationLogDTO;
    }

    //统计沟通信息
    public Integer getTodayData(List<String> userId){
        Map<String,Object> map=new HashMap<>();
        map.put("list",userId);
        Integer todayCount=0;
        todayCount=communicationLogMapper.selectToday(map);
        return  todayCount;
    }

    /**
     * 统计历史沟通客户数
     * @param userId
     * @return
     */
    public Integer getHistoryData(List<String> userId,String start,String end){
        Example example = new Example(CommunicationLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("createUser",userId);
        criteria.andBetween("createTime",start,end);
        Integer historyCount=0;
        historyCount=communicationLogMapper.selectCountByExample(example);
        return  historyCount;
    }

    //统计沟通客户信息
    public Integer getTodayCustomerData(List<String> userId){
        Map<String,Object> map=new HashMap<>();
        map.put("list",userId);
        Integer todayCount=0;
        List<CommunicationLog> communicationLogList=communicationLogMapper.selectTodayCustomer(map);
        todayCount=communicationLogList.size();
        return  todayCount;
    }

    //统计历史沟通客户信息
    public Integer getHistoryCustomerData(List<String> userId,String start,String end){
        Map<String,Object> map=new HashMap<>();
        map.put("createUser",userId);
        map.put("createTimeBegin", start);
        map.put("createTimeEnd",end);
        Integer todayCount=0;
        List<CommunicationLog> communicationLogList=communicationLogMapper.selectHistoryCustomer(map);
        todayCount=communicationLogList.size();
        return  todayCount;
    }

    public CommunicationLog getByPrimaryKey(Integer id) {
        CommunicationLog communicationLog = new CommunicationLog();
        communicationLog.setId(id);
        return communicationLogMapper.selectOne(communicationLog);
    }
}
