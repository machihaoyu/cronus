package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.uc.CompanyDto;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.dto.cronus.ImportInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.AllocateLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.EntityToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by msi on 2017/12/15.
 */
@Service
public class LookPoolService {


    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    UcService ucService;
    @Autowired
    AllocateLogMapper allocateLogMapper;
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
            throw new CronusException(CronusException.Type.MESSAGE_CONNECTTHEASYSTEM_ERROR);
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

    public  QueryResult<CustomerListDTO> allPool(String token, String customerName, String telephonenumber, String utmSource, String ownUserName, String customerSource,
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


    public ImportInfoDTO editImportInfo(Integer id, String token){
        //获取当前用户的信息
        CustomerInfo customerInfo = customerInfoService.findCustomerById(id);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        ImportInfoDTO infoDTO = new ImportInfoDTO();
        infoDTO.setId(customerInfo.getId());
        infoDTO.setCity(customerInfo.getCity());
        infoDTO.setUtmSource(customerInfo.getUtmSource());
        infoDTO.setCustomerSource(customerInfo.getCustomerSource());
        return  infoDTO;
    }

    public boolean editImportInfoOk(ImportInfoDTO importInfoDTO,String token){
         boolean flag = false;
        //校验参数
        Date date = new Date();
        if (StringUtils.isEmpty(importInfoDTO.getId())||StringUtils.isEmpty(importInfoDTO.getCity())|| StringUtils.isEmpty(importInfoDTO.getCustomerSource())||StringUtils.isEmpty(importInfoDTO.getUtmSource())){
            throw new CronusException(CronusException.Type.MESSAGE_PARAMSCUSTOMER_ERROR);
        }
        //开始查询信息
        CustomerInfo customerInfo = customerInfoService.findCustomerById(importInfoDTO.getId());
        UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token,CommonConst.SYSTEM_NAME_ENGLISH);
        customerInfo.setCity(importInfoDTO.getCity());
        customerInfo.setUtmSource(importInfoDTO.getUtmSource());
        customerInfo.setCustomerSource(importInfoDTO.getCustomerSource());
        customerInfo.setLastUpdateTime(date);
        customerInfo.setLastUpdateUser(Integer.valueOf(userInfoDTO.getUser_id()));
        customerInfoMapper.updateCustomer(customerInfo);
        //添加日志
        customerInfoService.insertLog(customerInfo,Integer.valueOf(userInfoDTO.getUser_id()));
        flag = true;
        return  flag;
    }

    public boolean allocateToCompany(String token,String customer_ids,Integer sub_company){
        boolean flag = false;
        Date date = new Date();
        Map<String,Object> paramsMap = new HashMap<>();
        List list = new ArrayList();
        //paramsList
        if (customer_ids != null && !"".equals(customer_ids)) {
            String[] strArray = null;
            strArray = customer_ids.split(",");
            for (int i = 0; i < strArray.length; i++) {
                list.add(Integer.parseInt(strArray[i]));
            }
            paramsMap.put("paramsList", list);
        }
        UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token,CommonConst.SYSTEM_NAME_ENGLISH);
        List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerListByFeild(paramsMap);
        if (customerInfoList != null && customerInfoList.size() > 0){
            for (CustomerInfo customerInfo : customerInfoList) {

                //添加分配日志
                AllocateLog allocateLog = new AllocateLog();
                allocateLog.setOperation(CommonConst.HANDOPERATION);
                allocateLog.setCustomerId(customerInfo.getId());
                /*
                   'customer_name'=>$customerInfos[$customer_id]['customer_name'],
                    'old_owner_id'=>$customerInfos[$customer_id]['owner_user_id'],
                    'new_owner_id' =>0,
                    'create_user_id' =>session("user_info.user_id"),
                    'create_user_name'=>(new UserModel())->getUserInfoByID(session("user_info.user_id"))['name'],
                    'create_time' =>time(),
                 */
                allocateLog.setOldOwnerId(customerInfo.getOwnUserId());
                allocateLog.setNewOwnerId(0);
                allocateLog.setCreateUserId(Integer.valueOf(userInfoDTO.getUser_id()));
                allocateLog.setCreateUserName(userInfoDTO.getName());
                allocateLog.setCreateTime(date);
                //
                allocateLogMapper.insert(allocateLog);
                //开始转移客户
         ///       customerInfo.set
            }

        }




        flag = true;
        return  flag;

    }
}
