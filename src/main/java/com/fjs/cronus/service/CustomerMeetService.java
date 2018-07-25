package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.api.thea.MailDTO;
import com.fjs.cronus.controller.CustomerInterviewController;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.Echo.MsgTmplDTO;
import com.fjs.cronus.dto.Echo.StationMsgReqDTO;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.api.uc.AppUserDto;
import com.fjs.cronus.dto.cronus.AddCustomerMeetDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.CustomerMeetDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.mappers.CustomerMeetMapper;

import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerMeet;
import com.fjs.cronus.service.Echo.EchoService;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DEC3Util;
import com.fjs.cronus.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    EchoService echoService;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;
    @Autowired
    private SalesmanMeetNumService salesmanMeetNumService;
    @Autowired
    private ThorService thorService;
    @Value("${Echo.meetsuccess}")
    private String meetsuccess;

    private static final Logger logger = LoggerFactory.getLogger(CustomerMeetService.class);
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerId",customerMeetDTO.getCustomerId());
        TheaApiDTO resultDTO = theaService.changeStatusByCustomerId(token,jsonObject);
        try{
            MsgTmplDTO msgTmplDTO = echoService.queryMsgTmpl(meetsuccess);
            StationMsgReqDTO stationMsgReqDTO = new StationMsgReqDTO();
            stationMsgReqDTO.setMsgClassify(meetsuccess);
            stationMsgReqDTO.setMsgTitle(msgTmplDTO.getTitle());
            stationMsgReqDTO.setSource("C");
            stationMsgReqDTO.setMsgContent(msgTmplDTO.getTmpl());
            String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
            stationMsgReqDTO.setUserPhone(telephone);
            echoService.addStationMsg(stationMsgReqDTO);
            logger.debug("发送短信成功" +stationMsgReqDTO.toString() );
        }catch (Exception e){e.printStackTrace();}

        int insert = customerMeetMapper.insert(customerMeet);
        if (insert > 0) {

            // 统计面见次数
            CronusDto<UserInfoDTO> userInfoByToken = thorService.getUserInfoByToken(token, null);
            UserInfoDTO data = userInfoByToken.getData();
            salesmanMeetNumService.countData(Long.valueOf(data.getSub_company_id()), Long.valueOf(data.getUser_id()), data.getName(), date);
            salesmanMeetNumService.reflushCache(Long.valueOf(data.getSub_company_id()), Long.valueOf(data.getUser_id()), data.getName(), date);
        }

        return insert;
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
        AppUserDto ucUserDTO = ucService.getUserInfoByID(token,customerMeet.getCreateUser());
        customerMeetDTO.setUserName(ucUserDTO.getName());
        customerMeetDTO.setCreateTime(customerMeet.getCreateTime());
        return customerMeetDTO;
    }

    /**
     *
     * m面见发送消息定时任务
     */

    public void sendMessMeetToCustomer(String token){
        Date date = new Date();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("meetTime",date);
        List<CustomerMeet> customerMeets = customerMeetMapper.selectByTime(paramsMap);
        try {
            if (customerMeets != null && customerMeets.size() >0){
                for (CustomerMeet customerMeet : customerMeets) {
                    if (customerMeet.getMeetTime() != null){
                        Long time1=Long.parseLong(DateUtils.format(customerMeet.getMeetTime(),DateUtils.FORMAT_FULL_Long));
                        Long time2=Long.parseLong(DateUtils.format(date,DateUtils.FORMAT_FULL_Long));
                        if (time1 - time2 <= 2100 && time1 - time2 >= 1800){
                            //调用发送信息接口
                            MailDTO mailDTO = new MailDTO();
                            CustomerInfo customerInfo = customerInfoService.findCustomerById(customerMeet.getCustomerId());
                            String content = "您定于"+ DateUtils.format(customerMeet.getMeetTime(),DateUtils.FORMAT_LONG) + "面见客户"+ customerInfo.getCustomerName()+"请注意面见。";
                            theaClientService.sendMail(token,content,customerMeet.getCreateUser(),customerMeet.getCreateUser(),null,customerMeet.getCreateUser());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public CronusDto getCustomerMeetByCustomerId(Integer customerId, Long userId, Long loanCreatTime) {
        // 参数校验
        if (customerId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "customerId 不能为空");
        }
        if (loanCreatTime == null || loanCreatTime.equals(0L)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "loadCreatTime 不能为空");
        }

        // 获取c端用户，业务经理id
        /*CustomerInfo temp = new CustomerInfo();
        temp.setId(customerId);
        CustomerInfo customerInfo = customerInfoMapper.selectOne(temp);*/

        CronusDto result = new CronusDto();
        CustomerMeet customerMeet = customerMeetMapper.getByCustomerId(customerId, new Date(loanCreatTime));
        result.setData(customerMeet);

        return result;
    }
}
