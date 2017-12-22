package com.fjs.cronus.service;


import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.api.thea.MailDTO;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.cronus.CommunicationDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.thea.CommunicationLogDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;

import com.fjs.cronus.dto.thea.CustomerUsefulDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CommunicationLogMapper;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.mappers.CustomerMeetMapper;
import com.fjs.cronus.model.*;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DEC3Util;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

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
    @Autowired
    UcService ucService;
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    CommentService commentService;
    //添加
    @Transactional
    public Integer addLog(CustomerUsefulDTO customerUsefulDTO, CustomerInfo customerDto, UserInfoDTO userInfoDTO, String token){
        Date date=new Date();
        //修改客户
        customerUsefulDTO.setHouseStatus(customerUsefulDTO.getHouseStatus());
        //修改客户
        //有效客户
        if(customerUsefulDTO.getLoanAmount() != null && Integer.valueOf(customerUsefulDTO.getLoanAmount().toString()) > 0){
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
        //开始更新沟通人
        customerDto.setCommunicateId(Integer.parseInt(userInfoDTO.getUser_id()));
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

    public CustomerUsefulDTO findByCustomerId(Integer customerId,String token){
        CustomerUsefulDTO customerUsefulDTO = new CustomerUsefulDTO();
        PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token,CommonConst.SYSTEM_NAME_ENGLISH);
        Integer lookphone = Integer.valueOf(userInfoDTO.getUser_info().getLook_phone());
        Integer userId = Integer.valueOf(userInfoDTO.getUser_info().getUser_id());
      /*  Map<String,Object> paramsMap = new HashMap<>();
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
        String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
        customerUsefulDTO.setTelephonenumber(telephone);
        customerUsefulDTO.setLoanAmount(customerUseful.getLoanAmount());
        customerUsefulDTO.setNextContactTime(communicationLog.getNextContactTime());
        customerUsefulDTO.setPurpose(customerUseful.getPurpose());
        customerUsefulDTO.setPurposeDescribe(customerUseful.getPurposeDescribe());*/
        Map<String,Object> paramsMap = new HashMap<>();
        Example example=new Example(CommunicationLog.class);
        CommunicationLog communicationLog = new CommunicationLog();
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("customerId",customerId);
        example.setOrderByClause("create_time desc");
        List<CommunicationLog> communicationLogList = communicationLogMapper.selectByExample(example);
        //取最近的一次
        if (communicationLogList != null && communicationLogList.size() > 0){
            communicationLog = communicationLogList.get(0);
            customerUsefulDTO.setNextContactTime(communicationLog.getNextContactTime());
        }
        paramsMap.put("customerId",customerId);
        List<CustomerMeet> customerMeets = customerMeetMapper.findByFeild(paramsMap);
        if (customerMeets == null || customerMeets.size() == 0){
            customerUsefulDTO.setIsMeet(CommonConst.IS_MEET_NO);
        }else {
            CustomerMeet customerMeet = customerMeets.get(0);
            customerUsefulDTO.setIsMeet(CommonConst.IS_MEET__YES);
            customerUsefulDTO.setMeetTime(customerMeet.getMeetTime());
        }
        CustomerInfo customerInfo = customerService.findCustomerById(customerId);
        customerUsefulDTO.setId(customerInfo.getId());
        customerUsefulDTO.setCustomerId(customerInfo.getId());
        customerUsefulDTO.setCooperationStatus(customerInfo.getCooperationStatus());
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        if (lookphone == 1) {//查看自己
            if (userId.equals(customerInfo.getOwnUserId())) {
                String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
                customerUsefulDTO.setTelephonenumber(telephone);
            }else {
                String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
                String phoneNumber = telephone.substring(0, 7) + "****";
                customerUsefulDTO.setTelephonenumber(phoneNumber);
            }
        }else if (lookphone == 2){//查看全部
            String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
            customerUsefulDTO.setTelephonenumber(telephone);
        }
        if (customerInfo.getConfirm() == 2 || customerInfo.getConfirm() == 3){
            //查询有效客户
            CustomerUseful customerUseful = customerUsefulService.selectByCustomerId(customerId);
            if (customerUseful != null){
                customerUsefulDTO.setHouseStatus(customerUseful.getHouseStatus());
                //判断手机号
                customerUsefulDTO.setLoanAmount(customerUseful.getLoanAmount());
                customerUsefulDTO.setPurposeDescribe(customerUseful.getPurposeDescribe());
                customerUsefulDTO.setPurpose(customerUseful.getPurpose());
            }
        }else {
            customerUsefulDTO.setLoanAmount(customerInfo.getLoanAmount());
            customerUsefulDTO.setHouseStatus(customerInfo.getHouseStatus());
        }


        return customerUsefulDTO;
    }
    //获取沟通日志列表
    public List<CommunicationDTO> findListByCustomerId(Integer customerId,String token){
        Map<String,Object> paramsMap = new HashMap<>();
        List<CommunicationDTO> communicationDTOS = new ArrayList<>();

        Example example=new Example(CommunicationLog.class);
        CustomerUsefulDTO customerUsefulDTO = new CustomerUsefulDTO();
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("customerId",customerId);
        example.setOrderByClause("create_time desc");
        List<CommunicationLog> communicationLogList = communicationLogMapper.selectByExample(example);
        //取最近的一次
        if (communicationLogList != null && communicationLogList.size() > 0){
            for (CommunicationLog communicationLog1 : communicationLogList) {
                CommunicationDTO communicationDTO = new CommunicationDTO();
                communicationDTO.setId(communicationLog1.getId());
                communicationDTO.setContent(communicationLog1.getContent());
                communicationDTO.setCreateTime(communicationLog1.getCreateTime());
                communicationDTO.setOwnUserId(communicationLog1.getCreateUser());
                //获取姓名
                UserInfoDTO userInfoDTO = ucService.getUserInfoByID(token,communicationLog1.getCreateUser());
                communicationDTO.setOwnUserName(userInfoDTO.getName());
                List<Comment> commentList = commentService.getByCommunicationLogId(communicationLog1.getId());
                if (commentList != null && commentList.size() > 0){
                    communicationDTO.setComments(commentList);
                }
                communicationDTOS.add(communicationDTO);
            }
        }

        return communicationDTOS;
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
    /**
     * 查找大于当前时间五分钟的数据并且发送短信
     *
     */

    public void  sendMessToCustomer(String token){
        Date date = new Date();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("comiaTime",date);
        List<CommunicationLog> communicationLogList = communicationLogMapper.selectByTime(paramsMap);
        try {
            if (communicationLogList != null && communicationLogList.size() >0){
                for (CommunicationLog communicationLog : communicationLogList) {
                    if (communicationLog.getNextContactTime() != null){
                        Long time1=Long.parseLong(DateUtils.format(communicationLog.getNextContactTime(),DateUtils.FORMAT_FULL_Long));
                        Long time2=Long.parseLong(DateUtils.format(date,DateUtils.FORMAT_FULL_Long));
                        if (time1 - time2 < 300){
                            //调用发送信息接口
                            CustomerInfo customerInfo = customerService.findCustomerById(communicationLog.getCustomerId());
                            String content = "您设置了"+ DateUtils.format(communicationLog.getNextContactTime(),DateUtils.FORMAT_LONG) + "与客户"+ customerInfo.getCustomerName()+"再次沟通，请注意沟通。";
                            theaClientService.sendMail(token,content,communicationLog.getCreateUser(),communicationLog.getCreateUser(),null,communicationLog.getCreateUser());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
