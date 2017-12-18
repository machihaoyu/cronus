package com.fjs.cronus.service.api;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.DEC3Util;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.HttpClientHelper;
import com.fjs.cronus.util.MultiThreadedHttpConnection;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 对外接口
 * Created by msi on 2017/12/7.
 */
@Service
public class OutPutService {

    private static final String ocdcUrl = "http://beta-ocdc.fang-crm.com/Api/Index/save&key=366a192b7w17e14c54574d18c28d48e6123428ab";


    public void  synchronToOcdc(CustomerInfo customerInfo){
        JSONObject jsonObject = entityToJsonObject(customerInfo);
        CronusDto result  = MultiThreadedHttpConnection.getInstance().sendDataByPost(ocdcUrl,jsonObject.toJSONString());
        System.out.println(result);
    }
    public static void main(String args[]){
        OutPutService outPutService = new OutPutService();
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId(101);
        customerInfo.setTelephonenumber("13162706810");
        customerInfo.setCustomerName("zl");
        customerInfo.setHouseStatus("无");
        outPutService.synchronToOcdc(customerInfo);
    }

    public JSONObject entityToJsonObject(CustomerInfo customerInfo){

        JSONObject json = new JSONObject();
        json.put("customer_id",customerInfo.getId());
        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
            json.put("telephonenumber",telephone);
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerName())){
            json.put("customer_name",customerInfo.getCustomerName());
        }
        if (!StringUtils.isEmpty(customerInfo.getSparePhone())){
            json.put("spare_phone",customerInfo.getSparePhone());
        }
        if (!StringUtils.isEmpty(customerInfo.getAge())){
            json.put("age",customerInfo.getAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getMarriage())){
            json.put("marriage",customerInfo.getMarriage());
        }
        if (!StringUtils.isEmpty(customerInfo.getIdCard())){
            json.put("id_card",customerInfo.getIdCard());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvinceHuji())){
            json.put("province_huji",customerInfo.getProvinceHuji());
        }

        if (!StringUtils.isEmpty(customerInfo.getSex())){
            json.put("sex",customerInfo.getSex());
        }

        if (!StringUtils.isEmpty(customerInfo.getCustomerAddress())){
            json.put("address",customerInfo.getCustomerAddress());
        }

        if (!StringUtils.isEmpty(customerInfo.getPerDescription())){
          json.put("per_description",customerInfo.getPerDescription());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseStatus())){
            json.put("house_status",customerInfo.getHouseStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAmount())){
            json.put("house_amount",customerInfo.getHouseAmount());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseType())){
            json.put("house_type",customerInfo.getHouseType());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseValue())){
            json.put("house_value",customerInfo.getHouseValue());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseArea())){
            json.put("house_area",customerInfo.getHouseArea());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAge())){
            json.put("house_age",customerInfo.getHouseAge());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLoan())){
           json.put("house_loan",customerInfo.getHouseLoan());
        }
        if (!StringUtils.isEmpty(customerInfo.getCity())){
           json.put("city",customerInfo.getCity());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerClassify())){
            json.put("customer_classify",customerInfo.getCustomerClassify());
        }
        if (!StringUtils.isEmpty(customerInfo.getSubCompanyId())){
            json.put("sub_company_id",customerInfo.getSubCompanyId());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            Date time = DateUtils.parse(DateUtils.format(customerInfo.getCreateTime(),DateUtils.FORMAT_LONG),DateUtils.FORMAT_LONG);
            long ts = time.getTime()/1000;
            json.put("create_time",ts);
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            json.put("creater_user_id",customerInfo.getCreateUser());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAlone())){
           json.put("house_alone",customerInfo.getHouseAlone());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseLoanValue())){
           json.put("house_loan_value",customerInfo.getHouseLoanValue());
        }

        if (!StringUtils.isEmpty(customerInfo.getOwnUserId())){
            json.put("owner_user_id",customerInfo.getOwnUserId());
        }
        if (!StringUtils.isEmpty(customerInfo.getUtmSource())){
            json.put("utm_source",customerInfo.getUtmSource());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerSource())){
            json.put("customer_source",customerInfo.getCustomerSource());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanAmount())){
            json.put("loan_amount",customerInfo.getLoanAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getReceiveTime())){
            Date time = DateUtils.parse(DateUtils.format(customerInfo.getReceiveTime(),DateUtils.FORMAT_LONG),DateUtils.FORMAT_LONG);
            long ts = time.getTime()/1000;
            json.put("receive_time",ts);
        }
        Date date = new Date();
        Date time = DateUtils.parse(DateUtils.format(date,DateUtils.FORMAT_LONG),DateUtils.FORMAT_LONG);
        long ts = time.getTime()/1000;
        json.put("push_status",1);
        json.put("push_time",ts);
        json.put("cooperation_status",customerInfo.getCooperationStatus());
        return  json;


    }
}
