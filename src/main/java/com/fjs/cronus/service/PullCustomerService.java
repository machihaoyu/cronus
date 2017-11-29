package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.PullCustomerDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;

import com.fjs.cronus.dto.api.SimpleUserInfoDTO;




import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.mappers.PullCustomerMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.PullCustomer;
import com.fjs.cronus.service.uc.UcService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PullCustomerDTO copyProperty(PullCustomer pullCustomer){
        PullCustomerDTO pullCustomerDTO=new PullCustomerDTO();
        pullCustomerDTO.setId(pullCustomer.getId());
        pullCustomerDTO.setName(pullCustomer.getName());
        pullCustomerDTO.setTelephone(pullCustomer.getTelephone());
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

    public QueryResult<PullCustomerDTO> listByCondition(PullCustomer pullCustomer, UserInfoDTO userInfoDTO, String token, String system, Integer pageNum, Integer size) {
        Integer userId = null;
        Integer companyId = null;
        Integer total = null;
        //公司
//        if (StringUtils.isNotEmpty(userInfoDTO.getCompany_id())) {
//            companyId = Integer.parseInt(userInfoDTO.getCompany_id());
//            pullCustomer.setCompanyId(companyId);
//        }
        QueryResult<PullCustomerDTO> pullCustomerQueryResult = null;
        List<PullCustomer> pullCustomerList = null;
        List<PullCustomerDTO> pullCustomerDTOList=new ArrayList<PullCustomerDTO>();
        Map<String,Object> map=new HashedMap();
        if (pullCustomer != null){
            if (StringUtils.isNotEmpty(pullCustomer.getName())){
                map.put("name",pullCustomer.getName());
            }
            if (StringUtils.isNotEmpty(pullCustomer.getCreateTimeBegin())){
                map.put("createTimeBegin",pullCustomer.getCreateTimeBegin());
            }
            if (StringUtils.isNotEmpty(pullCustomer.getCreateTimeEnd())){
                map.put("createTimeEnd",pullCustomer.getCreateTimeEnd());
            }
            if (StringUtils.isNotEmpty(pullCustomer.getTelephone())){
                map.put("telephone",pullCustomer.getTelephone());
            }
            if (pullCustomer.getStatus()!=null){
                map.put("status",pullCustomer.getStatus());
            }
            if (StringUtils.isNotEmpty(pullCustomer.getCity())){
                map.put("city",pullCustomer.getCity());
            }
            map.put("startNum",(pageNum-1)*size);
            map.put("size",size);
            pullCustomerList=pullCustomerMapper.listByCondition(map);
            for (PullCustomer selectPull:pullCustomerList){
                Integer saleId=selectPull.getSaleId();
                SimpleUserInfoDTO simpleUserInfoDTO =thorUcService.getSystemUserInfo(token,saleId);
                String name=simpleUserInfoDTO.getName();
                PullCustomerDTO pullCustomerDTO=copyProperty(selectPull);
                pullCustomerDTO.setOwnUserName(name);
                pullCustomerDTOList.add(pullCustomerDTO);
            }
            // 总数
            total = pullCustomerMapper.countByCondition(map);

            pullCustomerQueryResult = new QueryResult<PullCustomerDTO>();
            pullCustomerQueryResult.setRows(pullCustomerDTOList);
            pullCustomerQueryResult.setTotal(total + "");
        }
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

    @Transactional
    public Integer transfer(PullCustomer pullCustomer, com.fjs.cronus.dto.uc.UserInfoDTO userInfoDTO, String token){
        Map<String,Object> paramsMap = new HashMap<>();
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            Date date=new Date();
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
        //转入交易
        String encryptTelephone = "";//加密后的
        List paramsList = new ArrayList();
        paramsList.add(encryptTelephone);
        paramsList.add(pullCustomer.getTelephone());
        paramsMap.put("paramsList",paramsList);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
       /* if (customerInfo == null){
            loan=copyProperty(loan,pullCustomer);
            CronusDto<Integer> cronusDto = iCustomerService.addCrmCustomer(token,customerDTO);
            if (cronusDto != null){
                loan.setCustomerId(cronusDto.getData());
            }
            loanService.add(loan,userInfoDTO);
        }else{
            loan=loanService.copyProperty(loan,pullCustomer);
            //查找客户
            CronusDto<CustomerDTO> cronusDto=iCustomerService.findCustomerByFeild(token,loan.getCustomerId());
            customerDTO=cronusDto.getData();
            customerDTO.setHouseStatus("无");
            cronusDto = iCustomerService.editCustomerOk(token,customerDTO);
            loanService.update(loan,userInfoDTO);
        }*/
        return result;
    }

    @Transactional
    public Integer changeStatus(PullCustomer pullCustomer,UserInfoDTO userInfoDTO,Integer status){
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            Date date=new Date();
            pullCustomer.setLastUpdateTime(date);
            pullCustomer.setLastUpdateUser(Integer.parseInt(userInfoDTO.getUser_id()));
            pullCustomer.setStatus(status);
        }
        Integer result= update(pullCustomer,userInfoDTO);
        //原始盘日志
        Integer userId=null;
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            userId=Integer.parseInt(userInfoDTO.getUser_id());
        }
        pullCustomerUpdateLogService.addLog(pullCustomer,userId,CommonConst.STATUS_PULL_CUSTOMER);
        return result;
    }

    public CustomerDTO copy2CustomerDto(PullCustomer pullCustomer){
        CustomerDTO customerDTO=new CustomerDTO();
        customerDTO.setCustomerName(pullCustomer.getName());
        customerDTO.setTelephonenumber(pullCustomer.getTelephone());
        customerDTO.setCity(pullCustomer.getCity());
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
}
