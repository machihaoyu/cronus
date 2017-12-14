package com.fjs.cronus.service.api;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.HttpClientHelper;
import com.fjs.cronus.util.MultiThreadedHttpConnection;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 对外接口
 * Created by msi on 2017/12/7.
 */
@Service
public class OutPutService {

    private static final String ocdcUrl = "http://beta-ocdc.fang-crm.com/Api/Index/save&key=366a192b7w17e14c54574d18c28d48e6123428ab";


    public void  synchronToOcdc(CustomerInfo customerInfo){
        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(customerInfo);
        Date date = new Date();
        Date time = DateUtils.parse(DateUtils.format(date,DateUtils.FORMAT_LONG),DateUtils.FORMAT_LONG);
        long ts = time.getTime()/1000;
        jsonObject.put("push_status",1);
        jsonObject.put("push_time",ts);
        //加上push时间
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
}
