package com.fjs.cronus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.dto.customer.HaidaiCustomerDTO;
import com.fjs.cronus.dto.saas.SaasApiDTO;
import com.fjs.cronus.dto.saas.SaasIndexDTO;
import com.fjs.cronus.enums.ErrorNumEnum;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.exception.ExceptionValidate;
import com.fjs.cronus.util.StringAsciiUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2017/7/12 0012.
 */
@RestController
@RequestMapping("/saas/api/v1")
public class SaasController {

//    @Value("${sale.url}")
    private String saleUrl = "http://beta-sale.fang-crm.com/Api/Api/";

//    @Value("${saas.key}")
    private String saasKey = "67gXBstOju5LX7WOxSaQnjZguNs2citrJ99ZSJl8";

    @Autowired
    RestTemplate restTemplate;

    @ApiOperation(value="Saas首页数据", notes="Saas首页数据接口API")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
//    })
    @RequestMapping(value = "/getSaasIndexData", method = RequestMethod.GET)
    @ResponseBody
    public SaasApiDTO getSaasIndexData() {
        String url = saleUrl + "crmCountData?key=" + saasKey;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        ExceptionValidate.validateResponse(data);
        SaasIndexDTO saasIndexDTO = JSON.parseObject(data.getRetData(), SaasIndexDTO.class);
        return new SaasApiDTO(saasIndexDTO);
    }




}
