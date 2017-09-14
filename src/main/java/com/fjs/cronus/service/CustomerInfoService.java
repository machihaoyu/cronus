package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerDto;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import com.fjs.cronus.util.FastJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

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
        if (resultList.size() > 0){
            for (CustomerInfo customerInfo : resultList) {
                CustomerDto customerDto = new CustomerDto();
                EntityToDto.customerEntityToCustomerDto(customerInfo,customerDto);
                dtoList.add(customerDto);
            }
        }
        Integer count = customerInfoMapper.customerListCount(paramsMap);
        result.setRows(dtoList);
        result.setTotal(count.toString());
        return  result;
    }

    public CronusDto addCustomer(JSONObject jsonObject){
        CronusDto cronusDto = new CronusDto();
        //校验参数
         String telephonenumber = jsonObject.getString("telephonenumber");
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
         //判断必传字段



        return  cronusDto;
    }
}
