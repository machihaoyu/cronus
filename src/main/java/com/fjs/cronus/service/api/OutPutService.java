package com.fjs.cronus.service.api;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 对外接口
 * Created by msi on 2017/12/7.
 */
@Service
public class OutPutService {

    private static final String ocdcUrl = "http://beta-ocdc.fang-crm.com/Api/Index/save&key=356a192b7oo3b06c54574d18c28d46e63as428ab";


    public void  synchronToOcdc(CustomerInfo customerInfo){
        Map jsonObject = entityToJsonObject(customerInfo);
        CronusDto result  = MultiThreadedHttpConnection.getInstance().sendPostByMap(ocdcUrl,jsonObject);
        System.out.println(result);
    }
  /*  public static void main(String args[]){
        OutPutService outPutService = new OutPutService();
        CustomerInfo customerInfo = new CustomerInfo();
   *//*     customerInfo.setId(101);
        customerInfo.setTelephonenumber("13162706810");
        customerInfo.setCustomerName("zl");
        customerInfo.setHouseStatus("无");
        customerInfo.setCustomerSource("京东");
        customerInfo.setUtmSource("京东");*//*
        String str = "{\n" +
                "    \"customer_id\":\"131\",\n" +
                "    \"telephonenumber\":\"filUhyU9JwtGrZ13MHuXOw==\",\n" +
                "    \"customer_name\":\"名字\",\n" +
                "    \"loan_amount\":\"100000\",\n" +
                "    \"house_status\":\"有\",\n" +
                "    \"customer_classify\":\"空号\",\n" +
                "    \"cooperation_status\":\"暂未接通\",\n" +
                "    \"age\":\"12\",\n" +
                "    \"id_card\":\"362527199011232512\",\n" +
                "    \"marriage\":\"已婚\",\n" +
                "    \"sex\":\"男\",\n" +
                "    \"province_huji\":\"\",\n" +
                "    \"spare_phone\":\"\",\n" +
                "    \"customer_source\":\"线下\",\n" +
                "    \"utm_source\":\"渠道11\",\n" +
                "    \"address\":null,\n" +
                "    \"quick_city\":\"\",\n" +
                "    \"house_amount\":\"一套房\",\n" +
                "    \"house_type\":\"12\",\n" +
                "    \"house_value\":\"12\",\n" +
                "    \"house_area\":\"12\",\n" +
                "    \"house_age\":\"12\",\n" +
                "    \"house_loan\":\"是\",\n" +
                "    \"house_alone\":\"否\",\n" +
                "    \"house_loan_value\":\"12\",\n" +
                "    \"quick_citys\":\"\",\n" +
                "    \"per_description\":\"1212\",\n" +
                "    \"city\":null,\n" +
                "    \"owner_user_id\":\"1\",\n" +
                "    \"sub_company_id\":\"1\",\n" +
                "    \"creater_user_id\":\"1\",\n" +
                "    \"create_time\":1512098404,\n" +
                "    \"receive_time\":1512098404,\n" +
                "    \"customer_address\":\"天津市\n" +
                "市辖县\n" +
                "宁河县\n" +
                "\n" +
                "\",\n" +
                "    \"house_location\":\"内蒙古自治区\n" +
                "呼和浩特市\n" +
                "市辖区\n" +
                "\n" +
                "\",\n" +
                "    \"name\":\"名字\",\n" +
                "    \"push_status\":1,\n" +
                "    \"push_time\":1512098404\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        customerInfo = FastJsonUtils.getSingleBean(jsonObject.toJSONString(),CustomerInfo.class);
        customerInfo.setId(jsonObject.getInteger("customer_id"));
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        System.out.println(jsonObject.toJSONString());
        Map map = outPutService.entityToJsonObject(customerInfo);
        CronusDto result  = MultiThreadedHttpConnection.getInstance().sendPostByMap(ocdcUrl,map);
        //outPutService.synchronToOcdc(customerInfo);
        System.out.println(result.getData());
    }*/

    public Map entityToJsonObject(CustomerInfo customerInfo){

        Map<String,String> json = new HashMap();
        json.put("customer_id",customerInfo.getId().toString());
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
            json.put("sub_company_id",customerInfo.getSubCompanyId().toString());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            Date time = DateUtils.parse(DateUtils.format(customerInfo.getCreateTime(),DateUtils.FORMAT_LONG),DateUtils.FORMAT_LONG);
            long ts = time.getTime()/1000;
            json.put("create_time",String.valueOf(ts));
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            json.put("creater_user_id",customerInfo.getCreateUser().toString());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAlone())){
           json.put("house_alone",customerInfo.getHouseAlone());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseLoanValue())){
           json.put("house_loan_value",customerInfo.getHouseLoanValue());
        }

        if (!StringUtils.isEmpty(customerInfo.getOwnUserId())){
            json.put("owner_user_id",customerInfo.getOwnUserId().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getUtmSource())){
            json.put("utm_source",customerInfo.getUtmSource());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerSource())){
            json.put("customer_source",customerInfo.getCustomerSource());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanAmount())){
            json.put("loan_amount",customerInfo.getLoanAmount().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getReceiveTime())){
            Date time = DateUtils.parse(DateUtils.format(customerInfo.getReceiveTime(),DateUtils.FORMAT_LONG),DateUtils.FORMAT_LONG);
            long ts = time.getTime()/1000;
            json.put("receive_time",String.valueOf(ts));
        }
        Date date = new Date();
        Date time = DateUtils.parse(DateUtils.format(date,DateUtils.FORMAT_LONG),DateUtils.FORMAT_LONG);
        long ts = time.getTime()/1000;
        json.put("push_status","1");
        json.put("push_time",String.valueOf(ts));
        json.put("cooperation_status",customerInfo.getCooperationStatus());
        return  json;
    }
}
