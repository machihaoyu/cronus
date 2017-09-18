package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import com.fjs.cronus.util.FastJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.security.provider.MD5;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by msi on 2017/9/13.
 */
@Service
public class CustomerInfoService {

    @Autowired
    CustomerInfoMapper customerInfoMapper;

    public  List<CustomerInfo> findList(){
        List<CustomerInfo> resultList = new ArrayList();
        resultList = customerInfoMapper.selectAll();
        return  resultList;
    }

    public QueryResult customerList(String customerName,String createTimeStart,String createTimeEnd,String telephonenumber,Integer page,Integer size){
        QueryResult result = new QueryResult();
        Map<String,Object> paramsMap = new HashMap<>();
        List<CustomerInfo> resultList = new ArrayList<>();
        List<CustomerDto> dtoList = new ArrayList<>();
        if (!StringUtils.isEmpty(customerName)){
            paramsMap.put("customerName",customerName);
        }
        if (!StringUtils.isEmpty(createTimeStart)){
            Date startDate = DateUtils.parse(createTimeStart,DateUtils.FORMAT_LONG);
            paramsMap.put("createTimeStart",startDate);
        }
        if (!StringUtils.isEmpty(createTimeEnd)){
            Date startEnd = DateUtils.parse(createTimeEnd,DateUtils.FORMAT_LONG);
            paramsMap.put("createTimeEnd",startEnd);
        }
        if (!StringUtils.isEmpty(telephonenumber)){
            paramsMap.put("telephonenumber",telephonenumber);
        }
        paramsMap.put("start",(page-1) * size);
        paramsMap.put("size",size);
        resultList = customerInfoMapper.customerList(paramsMap);
        if (resultList != null && resultList.size() > 0){
            for (CustomerInfo customerInfo : resultList) {
                CustomerDto customerDto = new CustomerDto();
                EntityToDto.customerEntityToCustomerDto(customerInfo,customerDto);
                dtoList.add(customerDto);
            }
            Integer count = customerInfoMapper.customerListCount(paramsMap);
            result.setRows(dtoList);
            result.setTotal(count.toString());
        }
        Integer count = customerInfoMapper.customerListCount(paramsMap);
        result.setRows(dtoList);
        result.setTotal(count.toString());
        return  result;
    }

    public CronusDto addCustomer(JSONObject jsonObject){
        CronusDto cronusDto = new CronusDto();
        //校验参数
         /*String telephonenumber = jsonObject.getString("telephonenumber");
         String customerName = jsonObject.getString("customerName");
         String customerLevel  = jsonObject.getString("customerName");
         String sparePhone = jsonObject.getString("customerName");
         String age = jsonObject.getString("customerName");
         String marriage = jsonObject.getString("customerName");
         String idCard = jsonObject.getString("customerName");
         String provinceHuji = jsonObject.getString("customerName");
         String sex = jsonObject.getString("customerName");
         String customerAddress = jsonObject.getString("customerName");
         String houseStatus = jsonObject.getString("customerName");
         String houseAmount = jsonObject.getString("customerName");
         String houseType = jsonObject.getString("customerName");
         String houseValue = jsonObject.getString("customerName");
         String houseArea = jsonObject.getString("customerName");
         String houseAge = jsonObject.getString("customerName");
         String houseLoan = jsonObject.getString("customerName");
         String houseAlone = jsonObject.getString("customerName");
         String houseLocation = jsonObject.getString("customerName");
         String city = jsonObject.getString("customerName");
         String customerClassify = jsonObject.getString("customerName");
         String callbackStatus = jsonObject.getString("customerName");
         Date callbackTime = jsonObject.getDate("customerName");
         Integer subCompanyId = jsonObject.getInteger("customerName");
         String perDescription = jsonObject.getString("customerName");
         //判断必传字段*/
         //json转map 参数，教研参数
         validAddData(jsonObject);
         Map<String,Object> paramsMap = FastJsonUtils.json2Map(jsonObject.toJSONString());
         if (paramsMap.isEmpty()){
             throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
         }
         CustomerInfo customerInfo = customerInfoMapper.insertCustomer(paramsMap);

         if (customerInfo == null){
             throw new CronusException(CronusException.Type.CRM_CUSTOMER_ERROR);
         }
        cronusDto.setResult(ResultResource.CODE_SUCCESS);
        cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return  cronusDto;
    }

    public CronusDto fingBytelephone(String telephonenumber){
        CronusDto resultDto =  new CronusDto();
        //手机需要加密
        Map<String,Object> paramsMap = new HashMap<>();
        CustomerDto dto= new CustomerDto();
        String encryptTelephone = "";//加密后的
        List paramsList = new ArrayList();
        paramsList.add(encryptTelephone);
        paramsList.add(telephonenumber);
        paramsMap.put("paramsList",paramsList);
        CustomerInfo customerInfo = customerInfoMapper.fingByFeild(paramsMap);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        EntityToDto.customerEntityToCustomerDto(customerInfo,dto);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(customerInfo.getId());
        return  resultDto;
    }
    public CronusDto findCustomerListByIds (String customerids){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        List paramsList = new ArrayList();
        //截取逗号
        String[] strArray = null;
        strArray = customerids.split(",");
        for (int i= 0;i<strArray.length;i++){
            paramsList.add(Integer.parseInt(strArray[i]));
        }
        paramsMap.put("paramsList",paramsList);
        List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerListByFeild(paramsMap);
        //遍历
        List<CustomerDto> customerDtos = new ArrayList<>();
       for (CustomerInfo customerInfo: customerInfoList) {
            CustomerDto customerDto = new CustomerDto();
            EntityToDto.customerEntityToCustomerDto(customerInfo,customerDto);
            //TODO  增加source 调用接口 来源渠道
            customerDtos.add(customerDto);
        }
        if (customerInfoList != null && customerInfoList.size() > 0) {
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setData(customerInfoList);
        }
        return  resultDto;
    }
    public void validAddData(JSONObject jsonObject){
        String customerName = jsonObject.getString("customerName");
        String telephonenumber = jsonObject.getString("telephonenumber");
        Integer customerId = jsonObject.getInteger("id");

        if (customerName == null || "".equals(customerName)){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERNAME_ERROR);
        }
        if (telephonenumber == null || "".equals(telephonenumber) ||  Pattern.compile("/[0-9]{11}$/").matcher(telephonenumber).find() == false) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONE_ERROR);
        }
        //判断手机号是否被注册
        if (customerId == null){
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("telephonenumber",telephonenumber);
            List<CustomerInfo> customerInfos = customerInfoMapper.customerList(paramsMap);
            if (customerInfos.size() > 0){
                throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONERE_ERROR);
            }
        }

    }
}
