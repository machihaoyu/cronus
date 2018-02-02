package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.AddPullCustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.PullCustomerDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;

import com.fjs.cronus.dto.api.SimpleUserInfoDTO;


import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.mappers.PullCustomerMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerInfoLog;
import com.fjs.cronus.model.CustomerUseful;
import com.fjs.cronus.model.PullCustomer;
import com.fjs.cronus.service.api.OutPutService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by yinzf on 2017/10/24.
 */
@Service
public class PullCustomerService {
    @Autowired
    private UcService thorUcService;
    @Autowired
    private PullCustomerMapper pullCustomerMapper;
    @Autowired
    private CustomerInfoService iCustomerService;
    @Autowired
    private PullCustomerUpdateLogService pullCustomerUpdateLogService;
    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    CustomerInfoLogMapper customerInfoLogMapper;
    @Autowired
    OutPutService outPutService;

    @Value("${sysn.haidaiUrl}")
    private String haidaiUrl;
    @Value("${sysn.haidaiKey}")
    private String haidaiKey;
    public PullCustomerDTO copyProperty(PullCustomer pullCustomer){
        PullCustomerDTO pullCustomerDTO=new PullCustomerDTO();
        pullCustomerDTO.setId(pullCustomer.getId());
        pullCustomerDTO.setName(pullCustomer.getName());
        //TODO 对手机号进行隐藏
        String phoneNumber = pullCustomer.getTelephone().substring(0, 3) + "****" + pullCustomer.getTelephone().substring(7, pullCustomer.getTelephone().length());
        pullCustomerDTO.setTelephone(phoneNumber);
        pullCustomerDTO.setLoanAmount(pullCustomer.getLoanAmount());
        pullCustomerDTO.setSaleId(pullCustomer.getSaleId());
        pullCustomerDTO.setCity(pullCustomer.getCity());
        pullCustomerDTO.setCustomerSource(pullCustomer.getCustomerSource());
        pullCustomerDTO.setUtmSource(pullCustomer.getUtmSource());

        String extendText= pullCustomer.getExtendText();
        if (StringUtils.isNotEmpty(extendText)){
            JSONObject jsonObject= JSON.parseObject(extendText);
            if (jsonObject.containsKey("cust_house_city")){
                String custHouseCity=jsonObject.get("cust_house_city").toString();
                pullCustomerDTO.setCustHouseCity(custHouseCity);
            }
            if (jsonObject.containsKey("cust_city")){
                String custCity=jsonObject.get("cust_city").toString();
                pullCustomerDTO.setCustCity(custCity);
            }
        }
        pullCustomerDTO.setStatus(pullCustomer.getStatus());
        pullCustomerDTO.setCreateTime(pullCustomer.getCreateTime());
        pullCustomerDTO.setLastUpdateTime(pullCustomer.getLastUpdateTime());

        return pullCustomerDTO;
    }

    public QueryResult<PullCustomerDTO> listByCondition(String telephonenumber,Integer status, String name, String token,String systemName,String city,Integer mountLevle,String createTime,Integer page, Integer size,Integer userId) {
        Integer companyId = null;
        Integer total = null;
        QueryResult<PullCustomerDTO> pullCustomerQueryResult = null;
        List<PullCustomer> pullCustomerList = null;
        List<PullCustomerDTO> pullCustomerDTOList=new ArrayList<PullCustomerDTO>();
        Map<String,Object> map=new HashedMap();
        //获取下属id
        List<Integer> ids = thorUcService.getSubUserByUserId(token,userId);
        map.put("saleIds",ids);
            if (StringUtils.isNotEmpty(name)){
                map.put("name",name);
            }
            if (StringUtils.isNotEmpty(createTime)){
                //转时间格式
                Date date = DateUtils.parse(createTime,DateUtils.FORMAT_LONG);
                map.put("createTime",date);
            }
            if (StringUtils.isNotEmpty(telephonenumber)){
                map.put("telephone",telephonenumber);
            }
            if (status!=null){
                map.put("status",status);
            }
            if (StringUtils.isNotEmpty(city)){
                map.put("city",city);
            }
            if (mountLevle != null){
                map.put("mountLevle",mountLevle);
            }
            map.put("start",(page-1)*size);
            map.put("size",size);
            pullCustomerList=pullCustomerMapper.listByCondition(map);
            for (PullCustomer selectPull:pullCustomerList){
                Integer saleId=selectPull.getSaleId();
                SimpleUserInfoDTO simpleUserInfoDTO =thorUcService.getSystemUserInfo(token,saleId);
                String saleManName=simpleUserInfoDTO.getName();
                PullCustomerDTO pullCustomerDTO=copyProperty(selectPull);
                pullCustomerDTO.setOwnUserName(saleManName);
                pullCustomerDTOList.add(pullCustomerDTO);
            }
            // 总数
            total = pullCustomerMapper.countByCondition(map);

            pullCustomerQueryResult = new QueryResult<PullCustomerDTO>();
            pullCustomerQueryResult.setRows(pullCustomerDTOList);
            pullCustomerQueryResult.setTotal(total + "");
        return pullCustomerQueryResult;
    }

    /**
     * 按id查找原始盘
     * @param id
     * @return
     */
    public PullCustomer selectById(Integer id){
        PullCustomer pullCustomer=new PullCustomer();
        pullCustomer.setId(id);
        return pullCustomerMapper.selectOne(pullCustomer);
    }

    public PullCustomer copyProperty(PullCustomerDTO pullCustomerDTO){
        PullCustomer pullCustomer=selectById(pullCustomerDTO.getId());
        pullCustomer.setId(pullCustomerDTO.getId());
        pullCustomer.setName(pullCustomerDTO.getName());
        pullCustomer.setLoanAmount(pullCustomerDTO.getLoanAmount());
        return pullCustomer;
    }
    public PullCustomer copyPropertyAdd(AddPullCustomerDTO pullCustomerDTO){
        PullCustomer pullCustomer=selectById(pullCustomerDTO.getId());
        if (pullCustomer.getStatus() == 1){
            throw new CronusException(CronusException.Type.MESSAGE_PULLCUSTOMERUPPDATE_ERROR);
        }
        //判断是否更新手机号
        if (!pullCustomer.getTelephone().equals(pullCustomerDTO.getTelephone())){
            pullCustomer.setOldTelephone(pullCustomer.getTelephone());
            pullCustomer.setTelephone(pullCustomerDTO.getTelephone());
            if (pullCustomer.getStatus() == -1){
                pullCustomer.setStatus(0);
            }
        }
        pullCustomer.setOldTelephone(pullCustomer.getTelephone());
        pullCustomer.setTelephone(pullCustomerDTO.getTelephone());
        pullCustomer.setName(pullCustomerDTO.getName());
        pullCustomer.setLoanAmount(pullCustomerDTO.getLoanAmount());
        return pullCustomer;
    }

    @Transactional
    public  Integer update(PullCustomer pullCustomer,UserInfoDTO userInfoDTO){
        Integer userId = null;
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id().toString())) {
            userId = Integer.parseInt(userInfoDTO.getUser_id().toString());
        }
        pullCustomer.setLastUpdateUser(userId);
        Date date = new Date();
        pullCustomer.setLastUpdateTime(date);
        pullCustomerUpdateLogService.addLog(pullCustomer,userId,CommonConst.UPDATE_PULL_CUSTOMER);
        return pullCustomerMapper.update(pullCustomer);
    }

    /**
     *
     * @param pullCustomer
     * @param userInfoDTO
     * @param token
     * @return
     */
    @Transactional
    public CronusDto transfer(PullCustomer pullCustomer, com.fjs.cronus.dto.uc.UserInfoDTO userInfoDTO, String token){
        Map<String,Object> paramsMap = new HashMap<>();
        boolean flag = false;
        String operationReturn="转入成功";
        CronusDto resultDto = new CronusDto();
        Date date = new Date();
        if (pullCustomer.getStatus() != 0){
            throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
        }
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            pullCustomer.setLastUpdateTime(date);
            pullCustomer.setLastUpdateUser(Integer.parseInt(userInfoDTO.getUser_id()));
            pullCustomer.setStatus(CommonConst.PULL_CUSTOMER_STASTUS_TRANSFER);
        }
        CustomerDTO customerDTO=copy2CustomerDto(pullCustomer);
        Integer result= update(pullCustomer,userInfoDTO);
        //原始盘日志
        Integer userId=null;
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            userId=Integer.parseInt(userInfoDTO.getUser_id());
        }
        pullCustomerUpdateLogService.addLog(pullCustomer,userId,CommonConst.TRANSFER_PULL_CUSTOMER1);
        //转入客户现根据手机号判断是否包含此信息
        String encryptTelephone =DEC3Util.des3EncodeCBC(pullCustomer.getTelephone()) ;//加密后的
        paramsMap.put("telephonenumber",encryptTelephone);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        if (customerInfo != null){
            //开始更新客户信息
            flag= updateCustomer(customerInfo,pullCustomer,userId);
        }else {
            //新增一条客户信息
            CustomerInfo customer = new CustomerInfo();
            flag = addCustomer(customer,pullCustomer,userInfoDTO);
        }
        if (flag == true){
            //修改客户的状态
            pullCustomer.setStatus(1);
            pullCustomer.setLastUpdateTime(date);
            pullCustomer.setLastUpdateUser(userId);
            pullCustomerMapper.update(pullCustomer);
            //插入日志
            pullCustomerUpdateLogService.addLog(pullCustomer,userId,CommonConst.UPDATE_PULL_CUSTOMER);
            //TODO 通知海贷魔方
            Integer haidairesult = publicToHaidai(pullCustomer,1);
            if (haidairesult != 0){
                operationReturn ="转入成功,但推送到海贷魔方失败";
                resultDto.setData(operationReturn);
                resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
                resultDto.setResult(ResultResource.CODE_SUCCESS);
                return resultDto;
            }
        }
        resultDto.setData(operationReturn);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        return resultDto;
    }

    @Transactional
    public boolean changeStatus(PullCustomer pullCustomer,UserInfoDTO userInfoDTO,Integer status){
        boolean flag = false;
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            Date date=new Date();
            pullCustomer.setLastUpdateTime(date);
            pullCustomer.setLastUpdateUser(Integer.parseInt(userInfoDTO.getUser_id()));
            pullCustomer.setStatus(status);
        }
        Integer result= update(pullCustomer,userInfoDTO);
        if (result == null){
          return flag;
        }
        //原始盘日志
        Integer userId=null;
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            userId=Integer.parseInt(userInfoDTO.getUser_id());
        }
        pullCustomerUpdateLogService.addLog(pullCustomer,userId,CommonConst.STATUS_PULL_CUSTOMER);
        flag = true;
        return flag;
    }

    public CustomerDTO copy2CustomerDto(PullCustomer pullCustomer){
        CustomerDTO customerDTO=new CustomerDTO();
        customerDTO.setCustomerName(pullCustomer.getName());
        customerDTO.setTelephonenumber(pullCustomer.getTelephone());
        customerDTO.setCity(pullCustomer.getCity());
        customerDTO.setHouseStatus("无");
        //customerDTO.setLoa
        return customerDTO;
    }
    public CustomerInfo copyProperty(CustomerInfo loan, PullCustomer pullCustomer){
        if (loan == null){
            loan=new CustomerInfo();
        }
        loan.setOwnUserId(pullCustomer.getSaleId());
        loan.setCustomerName(pullCustomer.getName());
        loan.setTelephonenumber(pullCustomer.getTelephone());
        loan.setLoanAmount(pullCustomer.getLoanAmount());
        loan.setCity(pullCustomer.getCity());
        loan.setCustomerSource(pullCustomer.getCustomerSource());
        loan.setUtmSource(pullCustomer.getUtmSource());
        loan.setExt(pullCustomer.getExtendText());
        return loan;
    }

    @Transactional
    public boolean updateCustomer(CustomerInfo customerInfo,PullCustomer pullCustomer, Integer userId){
        boolean flag = false;
        Date date = new Date();
        String telephone = DEC3Util.des3EncodeCBC(pullCustomer.getTelephone());
        customerInfo.setTelephonenumber(telephone);
        customerInfo.setCustomerName(pullCustomer.getName());
        customerInfo.setHouseStatus("无");
        customerInfo.setLoanAmount(pullCustomer.getLoanAmount());
        customerInfo.setCity(pullCustomer.getCity());
        customerInfo.setCustomerSource(pullCustomer.getCustomerSource());
        customerInfo.setUtmSource(pullCustomer.getUtmSource());
        customerInfo.setOwnUserId(userId);
        customerInfo.setLastUpdateTime(date);
        customerInfo.setLastUpdateUser(userId);
        customerInfoMapper.updateCustomer(customerInfo);
        //插入日志
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("编辑客户信息");
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        flag = true;
        return  flag;
    }
    @Transactional
    public boolean addCustomer(CustomerInfo customerInfo,PullCustomer pullCustomer, UserInfoDTO userInfoDTO){
        boolean flag = false;
        Date date = new Date();
        Integer userId = Integer.valueOf(userInfoDTO.getUser_id());
        String telephone = DEC3Util.des3EncodeCBC(pullCustomer.getTelephone());
        customerInfo.setTelephonenumber(telephone);
        customerInfo.setCustomerName(pullCustomer.getName());
        customerInfo.setHouseStatus("无");
        customerInfo.setLoanAmount(pullCustomer.getLoanAmount());
        customerInfo.setCity(pullCustomer.getCity());
        customerInfo.setCustomerSource(pullCustomer.getCustomerSource());
        customerInfo.setUtmSource(pullCustomer.getUtmSource());
        customerInfo.setOwnUserId(userId);
        customerInfo.setLastUpdateTime(date);
        customerInfo.setLastUpdateUser(userId);
        customerInfo.setCreateTime(date);
        customerInfo.setCreateUser(userId);
        customerInfo.setCompanyId(Integer.valueOf(userInfoDTO.getCompany_id()));
        customerInfo.setSubCompanyId(Integer.valueOf(userInfoDTO.getSub_company_id()));
        customerInfo.setRemain(CommonConst.REMAIN_STATUS_NO);
        customerInfo.setConfirm(CommonConst.CONFIRM__STATUS_NO);
        customerInfo.setCustomerType(ResultResource.CUSTOMERTYPE);
        customerInfo.setReceiveId(0);
        customerInfo.setCommunicateId(0);
        customerInfo.setAutostatus(0);
        customerInfoMapper.insertCustomer(customerInfo);
        //插入日志
        //生成日志记录
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription("新增一条客户信息");
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
        try{
            outPutService.synchronToOcdc(customerInfo);
        }catch (Exception e){e.printStackTrace();}
        flag = true;
        return  flag;
    }
    public Integer publicToHaidai(PullCustomer pullCustomer,Integer status){

        Integer result = null;
        JSONObject jsonObject = new JSONObject();
        String extendText = pullCustomer.getExtendText();
        String orderID = null;
        if (StringUtils.isNotEmpty(extendText)){
            JSONObject   json= JSON.parseObject(extendText);
            if (json.containsKey("loan_id")){
                 orderID=json.get("loan_id").toString();
            }
        }
        jsonObject.put("orderID",orderID);
        jsonObject.put("phone",pullCustomer.getTelephone());
        jsonObject.put("status",status);
        jsonObject.put("key",haidaiKey);
        //发送post请求
        CronusDto cronusDto = MultiThreadedHttpConnection.getInstance().sendDataByPost(haidaiUrl,jsonObject.toJSONString());
        if (cronusDto != null) {
            if (!StringUtils.isEmpty( cronusDto.getData().toString())) {
                result = Integer.parseInt(cronusDto.getData().toString());
            }
        }
        return  result;
    }
}
