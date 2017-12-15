package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.util.EntityToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2017/12/15.
 */
@Service
public class LookPoolService {


    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    TheaClientService theaClientService;

    public QueryResult<CustomerListDTO> unablePool(String token, String customerName, String telephonenumber, String utmSource, String ownUserName, String customerSource,
                                                           String level, Integer companyId, Integer page, Integer size){

        QueryResult<CustomerListDTO> queryResult = new  QueryResult();
        List<CustomerListDTO> resultList = new ArrayList<>();
        List<String> paramsList = new ArrayList<>();
        Map<String,Object> paramMap = new HashMap<>();
        if (!StringUtils.isEmpty(customerName)){
            paramMap.put("customerName",customerName);
        }
        if (!StringUtils.isEmpty(telephonenumber)){
            paramMap.put("telephonenumber",telephonenumber);
        }
        if (!StringUtils.isEmpty(utmSource)){
            paramMap.put("utmSource",utmSource);
        }
        if (!StringUtils.isEmpty(ownUserName)){
            paramMap.put("ownUserName",ownUserName);
        }
        if (!StringUtils.isEmpty(customerSource)){
            paramMap.put("customerSource",customerSource);
        }
        if (!StringUtils.isEmpty(level)){
            paramMap.put("level",level);
        }
        if (!StringUtils.isEmpty(companyId)){
            paramMap.put("companyId",companyId);
        }
        //获取三无客户盘的状态
        String result = theaClientService.findValueByName(token, CommonConst.CAN_NOT_ALLOCATE_CUSTOMER_CLASSIFY);
        if (result != null && !"".equals(result)) {
            String[] strArray = null;
            strArray = result.split(",");
            for (int i = 0; i < strArray.length; i++) {
                paramsList.add(strArray[i]);
            }
            paramMap.put("customerClassify", paramsList);
        }else {

        }
        paramMap.put("start",(page-1) * size);
        paramMap.put("size",size);
        List<CustomerInfo> customerInfoList = customerInfoMapper.customerList(paramMap);
        if (customerInfoList != null && customerInfoList.size() > 0){

            for (CustomerInfo customerInfo : customerInfoList) {
                CustomerListDTO customerDto = new CustomerListDTO();
                EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto);
                resultList.add(customerDto);
            }
            queryResult.setRows(resultList);
            Integer count = customerInfoMapper.customerListCount(paramMap);
            queryResult.setTotal(count.toString());
        }
        return  queryResult;
    }
}
