package com.fjs.cronus.service;


import com.fjs.cronus.dto.thea.CommunicationLogDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;

import com.fjs.cronus.dto.thea.CustomerUsefulDTO;
import com.fjs.cronus.mappers.CommunicationLogMapper;
import com.fjs.cronus.mappers.CustomerMeetMapper;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerUseful;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
    private CustomerInfoService iCustomerService;
    @Autowired
    private CustomerUsefulService customerUsefulService;
    @Autowired
    private CustomerMeetMapper customerMeetMapper;

    //添加
    @Transactional
    public Integer addLog(CustomerUsefulDTO customerUsefulDTO, CustomerInfo customerDto, UserInfoDTO userInfoDTO, String token){
    /*    Date date=new Date();
        //修改客户
        customerUsefulDTO.setHouseStatus(customerUsefulDTO.getHouseStatus());
        CronusDto<CustomerDTO> cronusDto=iCustomerService.findCustomerByFeild(token,customerUsefulDTO.getCustomerId());
        //交易修改
        Loan loan=loanService.getByPrimaryKey(customerUsefulDTO.getLoanId());
        //有效客户
        if(customerUsefulDTO.getLoanAmount() != null){
            CustomerUseful customerUseful = customerUsefulService.selectByLoanId(customerUsefulDTO.getLoanId());
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
            loan.setLastUpdateUser(Integer.parseInt(userInfoDTO.getUser_id()));

            customerMeet.setCreateUser(Integer.parseInt(userInfoDTO.getUser_id()));
            customerMeet.setLastUpdateUser(Integer.parseInt(userInfoDTO.getUser_id()));
        }
        loan.setLoanAmount(customerUsefulDTO.getLoanAmount());
        loan.setHouseStatus(customerUsefulDTO.getHouseStatus());
        loan.setCooperationStatus(customerUsefulDTO.getCooperationStatus());
        //判断是否是首次沟通
        List<CommunicationLog> communicationLogList = listByLoanId(customerUsefulDTO.getLoanId(),token);
        if (communicationLogList.size() == 0){
            loan.setFirstCommunicateTime(date);
        }
        loan.setCommunicateTime(date);
        //确认状态
        if (customerUsefulDTO.getLoanAmount() != null && customerUsefulDTO.getLoanAmount().intValue() > 0){
            loan.setConfirm(CommonConst.CONFIRM__STATUS_EFFECT);
        }
        if (customerUsefulDTO.getLoanAmount() != null && customerUsefulDTO.getLoanAmount().intValue() == 0){
            loan.setConfirm(CommonConst.CONFIRM__STATUS_NO);
        }
//        loan.setStatus(CommonConst.LOAN_STATUS_COMMUNICATION);
        loan.setLastUpdateTime(date);
        loanService.update(loan,userInfoDTO);

        //面见
        if (customerUsefulDTO.getIsMeet() != null && customerUsefulDTO.getIsMeet() == CommonConst.IS_MEET__YES){
            customerMeet.setLoanId(customerUsefulDTO.getLoanId());
            customerMeet.setCustomerId(customerUsefulDTO.getCustomerId());
            customerMeet.setMeetTime(customerUsefulDTO.getMeetTime());
            customerMeet.setCreateTime(date);
            customerMeet.setLastUpdateTime(date);
            customerMeet.setIsDeleted(CommonConst.DATA_NORMAIL);
            customerMeetMapper.insert(customerMeet);
            //发送消息
        }

        //沟通日志
        CommunicationLog communicationLog=new CommunicationLog();
        Integer userId=null;
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            userId=Integer.parseInt(userInfoDTO.getUser_id());
        }
        communicationLog.setLoanId(loan.getId());
        communicationLog.setCustomerId(customerDto.getId());
        communicationLog.setHouseStatus(customerUsefulDTO.getHouseStatus());
        communicationLog.setLoanAmount(customerUsefulDTO.getLoanAmount());
        communicationLog.setType(CommonConst.COMMUNICATION_LOG_TYPE0);
        communicationLog.setContent(customerUsefulDTO.getContent());
        communicationLog.setCreateUser(userId);
        communicationLog.setCreateTime(date);
        return communicationLogMapper.insert(communicationLog);*/
    return  null;
    }
    /**
     * 根据客户id查找沟通日志
     * @param
     * @return
     */
    public List<CommunicationLog> listByLoanId(Integer customerId, String token){
        Example example=new Example(CommunicationLog.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("customerId",customerId);
        example.setOrderByClause("create_time desc");
        return communicationLogMapper.selectByExample(example);
    }


    /**
     * 根据交易id和用户查找
     * @param loanId
     * @param usertId
     * @param token
     * @return
     */
    public List<CommunicationLog> listByLoanId(Integer loanId, Integer usertId,String token){
        Example example=new Example(CommunicationLog.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("loanId",loanId);
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
