package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.UserDTO;
import com.fjs.cronus.dto.cronus.BaseUcDto;
import com.fjs.cronus.dto.cronus.CustomerInterVibaseInfoDto;
import com.fjs.cronus.dto.cronus.CustomerInterViewBaseCarHouseInsturDto;
import com.fjs.cronus.dto.cronus.UcUserDto;
import com.fjs.cronus.dto.php.CustomerInterviewBaseInfoDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInterviewBaseInfoMapper;
import com.fjs.cronus.mappers.CustomerInterviewCarInfoMapper;
import com.fjs.cronus.mappers.CustomerInterviewHouseInfoMapper;
import com.fjs.cronus.mappers.CustomerInterviewInsuranceInfoMapper;
import com.fjs.cronus.model.CustomerInterviewBaseInfo;
import com.fjs.cronus.model.CustomerInterviewCarInfo;
import com.fjs.cronus.model.CustomerInterviewHouseInfo;
import com.fjs.cronus.model.CustomerInterviewInsuranceInfo;
import com.fjs.cronus.service.client.ThorInterfaceService;
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
    ThorInterfaceService thorInterfaceService;
    @Autowired
    UcService ucService;
    @Autowired
    CustomerInterviewHouseInfoMapper customerInterviewHouseInfoMapper;
    @Autowired
    CustomerInterviewCarInfoMapper customerInterviewCarInfoMapper;
    @Autowired
    CustomerInterviewInsuranceInfoMapper customerInterviewInsuranceInfoMapper;
    public CronusDto customerInterviewList(String token,String name,String loanAmount,String industry,String feeChannelName,String productName,String ownerUserName,
                                           String telephonenumber,String householdRegister,Integer page,Integer size){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap =  new HashMap<>();
        //TODO 通过token查询到用户的id 查询自己以及下属员工
        String  result = thorInterfaceService.getCurrentUserInfo(token,null);
        try {
            BaseUcDto dto = FastJsonUtils.getSingleBean(result,BaseUcDto.class);
            UcUserDto userDTO = FastJsonUtils.getSingleBean(dto.getData().toString(),UcUserDto.class);
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
            List<CustomerInterVibaseInfoDto> resultList = new ArrayList<>();
            if (customerInterviewBaseInfos !=null && customerInterviewBaseInfos.size() > 0){
                for (CustomerInterviewBaseInfo customerInterviewBaseInfo : customerInterviewBaseInfos) {
                    CustomerInterVibaseInfoDto customerInterviewBaseInfoDTO = new CustomerInterVibaseInfoDto();
                    EntityToDto.CustomerInterviewEntityToCustomerInterviewDto(customerInterviewBaseInfo,customerInterviewBaseInfoDTO);
                }
                resultDto.setData(resultList);
                resultDto.setResult(ResultResource.CODE_SUCCESS);
                resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
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
        CustomerInterViewBaseCarHouseInsturDto customerInterViewBaseCarHouseInsturDto = new CustomerInterViewBaseCarHouseInsturDto();
        EntityToDto.CustomerInterviewEntityToCustomerInterviewAllInfoDto(customerInterViewBaseCarHouseInsturDto,customerInterviewBaseInfo,
                customerInterviewCarInfos,customerInterviewHouseInfos,customerInterviewInsuranceInfos);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData(customerInterViewBaseCarHouseInsturDto);
        return  resultDto;

    }
    @Transactional
    public CronusDto  addCustomerView ( CustomerInterViewBaseCarHouseInsturDto customerInterViewBaseCarHouseInsturDto,String token){
        CronusDto resultDto = new CronusDto();
        //根据token查询当前用户id
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户面谈信息出错!");
        }
        //json 转为Dto
     /*   System.out.println(jsonObject.toString());
        CustomerInterViewBaseCarHouseInsturDto customerInterViewBaseCarHouseInsturDto = FastJsonUtils.getSingleBean(jsonObject.toJSONString(),CustomerInterViewBaseCarHouseInsturDto.class);*/
        //dto 与实体互相转换
        //实例化

        CustomerInterviewBaseInfo customerInterviewBaseInfo = new CustomerInterviewBaseInfo();
        CustomerInterviewCarInfo customerInterviewCarInfo   = new CustomerInterviewCarInfo();
        CustomerInterviewHouseInfo customerInterviewHouseInfo = new CustomerInterviewHouseInfo();
        CustomerInterviewInsuranceInfo customerInterviewInsuranceInfo = new CustomerInterviewInsuranceInfo();
        EntityToDto.CustomerInterviewDtoToCustomerInterviewAllInfoEntity(customerInterViewBaseCarHouseInsturDto,customerInterviewBaseInfo,customerInterviewCarInfo,
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
        List<CustomerInterviewHouseInfo> customerInterviewHouseInfos = customerInterviewHouseInfoMapper.findByCustomerInterviewByFeild(paramsMap);
        //查找车辆信息
        List<CustomerInterviewCarInfo> customerInterviewCarInfos = customerInterviewCarInfoMapper.findByCustomerInterviewCarByFeild(paramsMap);
        //查找保单信息
        List<CustomerInterviewInsuranceInfo> customerInterviewInsuranceInfos = customerInterviewInsuranceInfoMapper.findByCustomerInterviewInsurByFeild(paramsMap);
        //拼装参数
        CustomerInterViewBaseCarHouseInsturDto customerInterViewBaseCarHouseInsturDto = new CustomerInterViewBaseCarHouseInsturDto();
        EntityToDto.CustomerInterviewEntityToCustomerInterviewAllInfoDto(customerInterViewBaseCarHouseInsturDto,customerInterviewBaseInfo,
                customerInterviewCarInfos,customerInterviewHouseInfos,customerInterviewInsuranceInfos);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData(customerInterViewBaseCarHouseInsturDto);
        return  resultDto;

    }
    @Transactional
    public CronusDto edditCustomerViewOk ( CustomerInterViewBaseCarHouseInsturDto customerInterViewBaseCarHouseInsturDto,String token){
        CronusDto resultDto = new CronusDto();
        //根据token查询当前用户id
        Integer user_id = ucService.getUserIdByToken(token);
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR, "新增客户面谈信息出错!");
        }
        //json转成dto
       // CustomerInterViewBaseCarHouseInsturDto customerInterViewBaseCarHouseInsturDto = FastJsonUtils.getSingleBean(jsonObject.toString(),CustomerInterViewBaseCarHouseInsturDto.class);

        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("customerInterviewBaseInfoId",customerInterViewBaseCarHouseInsturDto.getId());
        CustomerInterviewBaseInfo customerInterviewBaseInfo = customerInterviewBaseInfoMapper.customerInterviewByFeild(paramsMap);
        if (customerInterviewBaseInfo ==null){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }else {
            paramsMap.clear();
        }
        paramsMap.put("id",customerInterViewBaseCarHouseInsturDto.getCarInfoid());
        CustomerInterviewCarInfo customerInterviewCarInfo = customerInterviewCarInfoMapper.findByCustomerByFeild(paramsMap);
        paramsMap.clear();
        paramsMap.put("id",customerInterViewBaseCarHouseInsturDto.getHouseInfoId());
        CustomerInterviewHouseInfo customerInterviewHouseInfo = customerInterviewHouseInfoMapper.findByFeild(paramsMap);
        paramsMap.clear();
        CustomerInterviewInsuranceInfo customerInterviewInsuranceInfo = customerInterviewInsuranceInfoMapper.findByFeild(paramsMap);
        //dto 转为实体
        EntityToDto.CustomerInterviewDtoToCustomerInterviewAllInfoEntity(customerInterViewBaseCarHouseInsturDto,customerInterviewBaseInfo,customerInterviewCarInfo,
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
}
