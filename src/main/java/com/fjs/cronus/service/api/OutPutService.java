package com.fjs.cronus.service.api;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.HttpClientHelper;
import org.springframework.stereotype.Service;

/**
 * 对外接口
 * Created by msi on 2017/12/7.
 */
@Service
public class OutPutService {

    private static final String ocdcUrl = "http://beta-ocdc.fang-crm.com/Api/Index/save";
    private static final String key = "366a192b7w17e14c54574d18c28d48e6123428ab";

    public static void  synchronToOcdc(CustomerInfo customerInfo){
        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(customerInfo);
        HttpClientHelper httpClientHelper = HttpClientHelper.getInstance();
        String result  = httpClientHelper.sendJsonHttpPost(ocdcUrl,jsonObject.toJSONString());
        System.out.println(result);
    }
    public static void main(String args[]){
        CustomerInfo customerInfo = new CustomerInfo();
        synchronToOcdc(customerInfo);

    }

}
