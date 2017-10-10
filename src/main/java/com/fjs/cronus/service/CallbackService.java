package com.fjs.cronus.service;

import com.fjs.cronus.Common.CustomerEnum;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CallbackQueryDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CallbackPhoneLogMapper;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.StringAsciiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by msi on 2017/10/10.
 */
@Service
public class CallbackService {



    @Autowired
    CallbackPhoneLogMapper phoneLogMapper;




    public CronusDto callbackCustomerList(String callback_user,String callback_start_time,String callback_end_time,String search_name,
                                          Integer type,String search_city,String search_telephone,String search_callback_status,Integer page,Integer size,Integer communication_order){
        CronusDto resultDto = new CronusDto();
        //筛选回访人
        List  CustomerIdList = new ArrayList();
        Map<String,Object> paramsMap = new HashMap<>();
        if (type == null || "".equals(type)){
            throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
        }

        if (!StringUtils.isEmpty(callback_start_time)){
            Date startDate = DateUtils.parse(callback_start_time,DateUtils.FORMAT_LONG);
            paramsMap.put("createTimeStart",startDate);
        }
        if (!StringUtils.isEmpty(callback_end_time)){
            Date endDate = DateUtils.parse(callback_end_time,DateUtils.FORMAT_LONG);
            paramsMap.put("createTimeEnd",endDate);
        }
        if (!StringUtils.isEmpty(callback_user)){
            paramsMap.put("callback_user",callback_user);
        }
         if (paramsMap != null && paramsMap.size() > 0 ){
             //从phonelog中查询到customerId
             CustomerIdList = phoneLogMapper.getCustomerId(paramsMap);
         }
         if (CustomerIdList != null  && CustomerIdList.size() > 0){
             paramsMap.put("CustomerIdList",CustomerIdList);
         }
         paramsMap.put("customer_type", CustomerEnum.getByValue(type).getName());
         if (!StringUtils.isEmpty(search_city)){
             paramsMap.put("search_city",search_city);
         }else {
             List<String> cityList = Arrays.<String> asList(ResultResource.CITYS);
             paramsMap.put("cityList",cityList);
         }
         if (!StringUtils.isEmpty(search_name)){
            paramsMap.put("search_name",search_name);
          }
        if (!StringUtils.isEmpty(search_callback_status)){
            paramsMap.put("search_callback_status",search_callback_status);
        }
        if (!StringUtils.isEmpty(search_telephone)){
            List phonelist = new ArrayList();
            //TODO 手机号加密操作
            String encodePhone = "";
            phonelist.add(encodePhone);
            phonelist.add(search_telephone);
            paramsMap.put("phonelist",phonelist);
        }
       //计算分页
        if (communication_order == 1){


        }

        paramsMap.put("start",(page-1) * size);
        paramsMap.put("size",size);





        return  resultDto;

    }

}
