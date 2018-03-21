package com.fjs.cronus.service;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.BaseUcDTO;
import com.fjs.cronus.dto.cronus.CustomerInterVibaseInfoDTO;
import com.fjs.cronus.dto.cronus.CustomerInterViewBaseCarHouseInsturDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInterviewBaseInfoMapper;
import com.fjs.cronus.mappers.CustomerInterviewCarInfoMapper;
import com.fjs.cronus.mappers.CustomerInterviewHouseInfoMapper;
import com.fjs.cronus.mappers.CustomerInterviewInsuranceInfoMapper;
import com.fjs.cronus.model.CustomerInterviewBaseInfo;
import com.fjs.cronus.model.CustomerInterviewCarInfo;
import com.fjs.cronus.model.CustomerInterviewHouseInfo;
import com.fjs.cronus.model.CustomerInterviewInsuranceInfo;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.EntityToDto;
import com.fjs.cronus.util.FastJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by msi on 2017/9/15.
 */
@Service
public class CustomerInterviewService {
    @Autowired
    CustomerInterviewBaseInfoMapper customerInterviewBaseInfoMapper;
    @Autowired
    ThorService thorService;
    @Autowired
    UcService ucService;
    @Autowired
    CustomerInterviewHouseInfoMapper customerInterviewHouseInfoMapper;
    @Autowired
    CustomerInterviewCarInfoMapper customerInterviewCarInfoMapper;
    @Autowired
    CustomerInterviewInsuranceInfoMapper customerInterviewInsuranceInfoMapper;
    public QueryResult customerInterviewList(String token, String name, String loanAmount, String industry, String feeChannelName, String productName, String ownerUserName,
                                             String telephonenumber, String householdRegister, Integer page, Integer size){
        QueryResult resultDto = new QueryResult();
        Map<String,Object> paramsMap =  new HashMap<>();
        //TODO 通过token查询到用户的id 查询自己以及下属员工
        String  result = thorService.getCurrentUserInfo(token,null);
        try {
            BaseUcDTO dto = FastJsonUtils.getSingleBean(result,BaseUcDTO.class);
            UcUserDTO userDTO = FastJsonUtils.getSingleBean(dto.getData().toString(),UcUserDTO.class);
            List idList = new ArrayList();
            idList = ucService.getSubUserByUserId(token,Integer.valueOf(userDTO.getUser_id()));
            //拼装参数
            if (idList.size() > 0){
                paramsMap.put("idList",idList);
            }
            if (!StringUtils.isEmpty(name)){
                paramsMap.put("name",name);
            }
            if (!StringUtils.isEmpty(loanAmount)){
                paramsMap.put("loanAmount",loanAmount);
            }
            if (!StringUtils.isEmpty(industry)){
                paramsMap.put("industry",industry);
            }
            if (!StringUtils.isEmpty(feeChannelName)){
                paramsMap.put("feeChannelName",feeChannelName);
            }
            if (!StringUtils.isEmpty(productName)){
                paramsMap.put("productName",productName);
            }
            if (!StringUtils.isEmpty(ownerUserName)){
                paramsMap.put("ownerUserName",ownerUserName);
            }
            if (!StringUtils.isEmpty(telephonenumber)){
                //TODO 手机号需要经过加密处理
                String encrptTelephonenumber = "";
                List paramsList = new ArrayList();
                paramsList.add(encrptTelephonenumber);
                paramsList.add(telephonenumber);
                paramsMap.put("paramsList",paramsList);

            }
            if (!StringUtils.isEmpty(householdRegister)){
                paramsMap.put("householdRegister",householdRegister);
            }
            paramsMap.put("start",(page-1) * size);
            paramsMap.put("size",size);
            List<CustomerInterviewBaseInfo> customerInterviewBaseInfos = customerInterviewBaseInfoMapper.customerInterviewList(paramsMap);
            Integer count = customerInterviewBaseInfoMapper.customerInterviewListCount(paramsMap);
            List<CustomerInterVibaseInfoDTO> resultList = new ArrayList<>();
            if (customerInterviewBaseInfos !=null && customerInterviewBaseInfos.size() > 0){
                for (CustomerInterviewBaseInfo customerInterviewBaseInfo : customerInterviewBaseInfos) {
                    CustomerInterVibaseInfoDTO customerInterviewBaseInfoDTO = new CustomerInterVibaseInfoDTO();
                    EntityToDto.CustomerInterviewEntityToCustomerInterviewDto(customerInterviewBaseInfo,customerInterviewBaseInfoDTO);
                    resultList.add(customerInterviewBaseInfoDTO);
                }
                resultDto.setRows(resultList);
                resultDto.setTotal(count.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  resultDto;
    }
    public CronusDto findCustomerinteViewById(Integer customerInterviewBaseInfoId){
        CronusDto resultDto = new CronusDto();
        //查找指定id的用户
        Map<String,Object> paramsMap = new HashMap<>();
        //封装参数
        if (!StringUtils.isEmpty(customerInterviewBaseInfoId)){
            paramsMap.put("customerInterviewBaseInfoId",customerInterviewBaseInfoId);
        }
        //TODO 手机号的加密解密
        CustomerInterviewBaseInfo customerInterviewBaseInfo = customerInterviewBaseInfoMapper.customerInterviewByFeild(paramsMap);
        //查找房产信息
        if (customerInterviewBaseInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR, "该用户不存在!");
        }
        List<CustomerInterviewHouseInfo> customerInterviewHouseInfos = customerInterviewHouseInfoMapper.findByCustomerInterviewByFeild(paramsMap);
        //查找车辆信息
        List<CustomerInterviewCarInfo> customerInterviewCarInfos = customerInterviewCarInfoMapper.findByCustomerInterviewCarByFeild(paramsMap);
        //查找保单信息
        List<CustomerInterviewInsuranceInfo> customerInterviewInsuranceInfos = customerInterviewInsuranceInfoMapper.findByCustomerInterviewInsurByFeild(paramsMap);
        //拼装参数
        CustomerInterViewBaseCarHouseInsturDTO customerInterViewBaseCarHouseInsturDTO = new CustomerInterViewBaseCarHouseInsturDTO();
        EntityToDto.CustomerInterviewEntityToCustomerInterviewAllInfoDto(customerInterViewBaseCarHouseInsturDTO,customerInterviewBaseInfo,
                customerInterviewCarInfos,customerInterviewHouseInfos,customerInterviewInsuranceInfos);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData(customerInterViewBaseCarHouseInsturDTO);
        return  resultDto;

    }
    @Transactional
    public CronusDto  addCustomerView (CustomerInterViewBaseCarHouseInsturDTO customerInterViewBaseCarHouseInsturDTO, String token){
        CronusDto resultDto = new CronusDto();
        //根据token查询当前用户id
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户面谈信息出错!");
        }
        //教研参数
        validParamsInteview(customerInterViewBaseCarHouseInsturDTO);
        //json 转为Dto
     /*   System.out.println(jsonObject.toString());
        CustomerInterViewBaseCarHouseInsturDto customerInterViewBaseCarHouseInsturDto = FastJsonUtils.getSingleBean(jsonObject.toJSONString(),CustomerInterViewBaseCarHouseInsturDto.class);*/
        //dto 与实体互相转换
        //实例化

        CustomerInterviewBaseInfo customerInterviewBaseInfo = new CustomerInterviewBaseInfo();
        CustomerInterviewCarInfo customerInterviewCarInfo   = new CustomerInterviewCarInfo();
        CustomerInterviewHouseInfo customerInterviewHouseInfo = new CustomerInterviewHouseInfo();
        CustomerInterviewInsuranceInfo customerInterviewInsuranceInfo = new CustomerInterviewInsuranceInfo();
        EntityToDto.CustomerInterviewDtoToCustomerInterviewAllInfoEntity(customerInterViewBaseCarHouseInsturDTO,customerInterviewBaseInfo,customerInterviewCarInfo,
                customerInterviewHouseInfo,customerInterviewInsuranceInfo);
        //
        Date date = new Date();
        if (customerInterviewBaseInfo == null){  throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW); }
            customerInterviewBaseInfo.setCreateTime(date);
            customerInterviewBaseInfo.setCreateUser(user_id);
            customerInterviewBaseInfo.setLastUpdateTime(date);
            customerInterviewBaseInfo.setLastUpdateUser(user_id);
            customerInterviewBaseInfo.setIsDeleted(0);
            //存入数据库
           customerInterviewBaseInfoMapper.addCustomerInteview(customerInterviewBaseInfo);
           //存入车辆信息
           if (customerInterviewCarInfo  !=null ){
               customerInterviewCarInfo.setCustomerInterviewBaseInfoId(customerInterviewBaseInfo.getId());
               customerInterviewCarInfo.setCreateTime(date);
               customerInterviewCarInfo.setCreateUser(user_id);
               customerInterviewCarInfo.setLastUpdateTime(date);
               customerInterviewCarInfo.setLastUpdateUser(user_id);
               customerInterviewCarInfo.setIsDeleted(0);
               customerInterviewCarInfoMapper.addCustomerInteviewCarInfo(customerInterviewCarInfo);
           }
           //存入房产信息
           if (customerInterviewHouseInfo != null){
               customerInterviewHouseInfo.setCustomerInterviewBaseInfoId(customerInterviewBaseInfo.getId());
               customerInterviewHouseInfo.setCreateTime(date);
               customerInterviewHouseInfo.setCreateUser(user_id);
               customerInterviewHouseInfo.setLastUpdateTime(date);
               customerInterviewHouseInfo.setLastUpdateUser(user_id);
               customerInterviewHouseInfo.setIsDeleted(0);
               customerInterviewHouseInfoMapper.addCustomerInterviewHouseInfo(customerInterviewHouseInfo);

           }
           //存入保险信息
           if (customerInterviewInsuranceInfo != null){
               customerInterviewInsuranceInfo.setCustomerInterviewBaseInfoId(customerInterviewBaseInfo.getId());
               customerInterviewInsuranceInfo.setCreateTime(date);
               customerInterviewInsuranceInfo.setCreateUser(user_id);
               customerInterviewInsuranceInfo.setLastUpdateTime(date);
               customerInterviewInsuranceInfo.setLastUpdateUser(user_id);
               customerInterviewInsuranceInfo.setIsDeleted(0);
               customerInterviewInsuranceInfoMapper.addCustomerInsura(customerInterviewInsuranceInfo);

           }
           resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
           resultDto.setResult(ResultResource.CODE_SUCCESS);
           return  resultDto;
    }

    public CronusDto editCustomerinteView(Integer customerInterviewBaseInfoId){
        CronusDto resultDto = new CronusDto();
        //查找指定id的用户
        Map<String,Object> paramsMap = new HashMap<>();
        //封装参数
        if (!StringUtils.isEmpty(customerInterviewBaseInfoId)){
            paramsMap.put("customerInterviewBaseInfoId",customerInterviewBaseInfoId);
        }
        //TODO 手机号的加密解密
        CustomerInterviewBaseInfo customerInterviewBaseInfo = customerInterviewBaseInfoMapper.customerInterviewByFeild(paramsMap);
        //查找房产信息
        if (customerInterviewBaseInfo == null){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERBASEINFO_ERROR);
        }
        List<CustomerInterviewHouseInfo> customerInterviewHouseInfos = customerInterviewHouseInfoMapper.findByCustomerInterviewByFeild(paramsMap);
        //查找车辆信息
        List<CustomerInterviewCarInfo> customerInterviewCarInfos = customerInterviewCarInfoMapper.findByCustomerInterviewCarByFeild(paramsMap);
        //查找保单信息
        List<CustomerInterviewInsuranceInfo> customerInterviewInsuranceInfos = customerInterviewInsuranceInfoMapper.findByCustomerInterviewInsurByFeild(paramsMap);
        //拼装参数
        CustomerInterViewBaseCarHouseInsturDTO customerInterViewBaseCarHouseInsturDTO = new CustomerInterViewBaseCarHouseInsturDTO();
        EntityToDto.CustomerInterviewEntityToCustomerInterviewAllInfoDto(customerInterViewBaseCarHouseInsturDTO,customerInterviewBaseInfo,
                customerInterviewCarInfos,customerInterviewHouseInfos,customerInterviewInsuranceInfos);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData(customerInterViewBaseCarHouseInsturDTO);
        return  resultDto;

    }
    @Transactional
    public CronusDto edditCustomerViewOk (CustomerInterViewBaseCarHouseInsturDTO customerInterViewBaseCarHouseInsturDTO, String token){
        CronusDto resultDto = new CronusDto();
        //根据token查询当前用户id
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户面谈信息出错!");
        }
        //json转成dto
       // CustomerInterViewBaseCarHouseInsturDto customerInterViewBaseCarHouseInsturDto = FastJsonUtils.getSingleBean(jsonObject.toString(),CustomerInterViewBaseCarHouseInsturDto.class);
        validParamsInteview(customerInterViewBaseCarHouseInsturDTO);
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("customerInterviewBaseInfoId", customerInterViewBaseCarHouseInsturDTO.getId());
        CustomerInterviewBaseInfo customerInterviewBaseInfo = customerInterviewBaseInfoMapper.customerInterviewByFeild(paramsMap);
        if (customerInterviewBaseInfo ==null){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }else {
            paramsMap.clear();
        }
        Map<String,Object> paramsMap1 = new HashMap<>();
        CustomerInterviewCarInfo customerInterviewCarInfo = null;
        if (customerInterViewBaseCarHouseInsturDTO.getCarInfoid() != null) {
            paramsMap1.put("id", customerInterViewBaseCarHouseInsturDTO.getCarInfoid());
            customerInterviewCarInfo = customerInterviewCarInfoMapper.findByCustomerByFeild(paramsMap1);
        }

        Map<String,Object> paramsMap2 = new HashMap<>();
        CustomerInterviewHouseInfo customerInterviewHouseInfo = null;
        if (customerInterViewBaseCarHouseInsturDTO.getHouseInfoId()!= null) {
            paramsMap2.put("id", customerInterViewBaseCarHouseInsturDTO.getHouseInfoId());
             customerInterviewHouseInfo = customerInterviewHouseInfoMapper.findByFeild(paramsMap2);
        }
        Map<String,Object> paramsMap3 = new HashMap<>();
        CustomerInterviewInsuranceInfo customerInterviewInsuranceInfo = null;
        if (customerInterViewBaseCarHouseInsturDTO.getInsuranceInfoId() != null) {
            paramsMap3.put("id", customerInterViewBaseCarHouseInsturDTO.getInsuranceInfoId());
             customerInterviewInsuranceInfo = customerInterviewInsuranceInfoMapper.findByFeild(paramsMap3);
        }
        //dto 转为实体
        EntityToDto.CustomerInterviewDtoToCustomerInterviewAllInfoEntity(customerInterViewBaseCarHouseInsturDTO,customerInterviewBaseInfo,customerInterviewCarInfo,
                customerInterviewHouseInfo,customerInterviewInsuranceInfo);
        //
        Date date = new Date();
        if (customerInterviewBaseInfo == null){  throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW); }
        customerInterviewBaseInfo.setLastUpdateTime(date);
        customerInterviewBaseInfo.setLastUpdateUser(user_id);
        customerInterviewBaseInfo.setIsDeleted(0);
        //存入数据库
        customerInterviewBaseInfoMapper.updateCustomerInteview(customerInterviewBaseInfo);
        //存入车辆信息
        if (customerInterviewCarInfo  !=null ){
            customerInterviewCarInfo.setCustomerInterviewBaseInfoId(customerInterviewBaseInfo.getId());
            customerInterviewCarInfo.setLastUpdateTime(date);
            customerInterviewCarInfo.setLastUpdateUser(user_id);
            customerInterviewCarInfo.setIsDeleted(0);
            customerInterviewCarInfoMapper.updateCustomerInteviewCarInfo(customerInterviewCarInfo);
        }
        //存入房产信息
        if (customerInterviewHouseInfo != null){
            customerInterviewHouseInfo.setCustomerInterviewBaseInfoId(customerInterviewBaseInfo.getId());
            customerInterviewHouseInfo.setLastUpdateTime(date);
            customerInterviewHouseInfo.setLastUpdateUser(user_id);
            customerInterviewHouseInfo.setIsDeleted(0);
            customerInterviewHouseInfoMapper.updateCustomerInterviewHouseInfo(customerInterviewHouseInfo);

        }
        //存入保险信息
        if (customerInterviewInsuranceInfo != null){
            customerInterviewInsuranceInfo.setCustomerInterviewBaseInfoId(customerInterviewBaseInfo.getId());
            customerInterviewInsuranceInfo.setLastUpdateTime(date);
            customerInterviewInsuranceInfo.setLastUpdateUser(user_id);
            customerInterviewInsuranceInfo.setIsDeleted(0);
            customerInterviewInsuranceInfoMapper.updateCustomerInsura(customerInterviewInsuranceInfo);
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        return  resultDto;
    }


    public void validParamsInteview(CustomerInterViewBaseCarHouseInsturDTO customerInterViewBaseCarHouseInsturDTO){

        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getName())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERNAME_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getSex())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERSEX_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getAge())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERAGE_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getMaritalStatus())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERMERAEATATUS_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getEducation())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEREDUCATION_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getLoanAmount())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERLOANMOUNT_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getLoanTime())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERLOANTIME_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getLoanUseTime())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERLOANUSETIME_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getLoanPurpose())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERLOANPURPOSE_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getCreditRecord())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERCREATERECORD_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getDebtAmount())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERCDEBETAMOUNT_ERROR);
        }
        if (StringUtils.isEmpty(customerInterViewBaseCarHouseInsturDTO.getIsOverdue())){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERISOVER_ERROR);
        }
    }
}
