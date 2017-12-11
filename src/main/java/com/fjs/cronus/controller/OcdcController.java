package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.crm.JSONData;
import com.fjs.cronus.dto.crm.OcdcData;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.model.CustomerSalePushLog;
import com.fjs.cronus.service.OcdcService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 客户
 *
 * Created by feng on 2017/7/14.
 */
@Controller
@RequestMapping(value = "/api/v1/ocdc")
public class OcdcController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private OcdcService ocdcService;

//    @Autowired
//    private ConfigService configService;

//    @Autowired
//    private ConfigRedisService configRedisService;

    /*@ApiOperation(value="获取配置信息", notes="根据英文名获取配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true,
                    paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "配置名", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getConfigByName", method = RequestMethod.GET)
    @ResponseBody
    public TheaApiDTO<Config> getConfigByName(@RequestParam String name){
        TheaApiDTO resultDto = new TheaApiDTO();
        if(StringUtils.isBlank(name)){
            throw new TheaException(TheaException.Type.THEA_PARAM_ERROR);
        }
        try{
//            Config config = configService.selectByName(name);
            String value = configRedisService.getConfigValue(name);
            resultDto.setResult(ResultDescription.CODE_SUCCESS);
            resultDto.setMessage(ResultDescription.MESSAGE_SUCCESS);
            resultDto.setData(value);
        } catch (Exception e){
            logger.error("-------------------获取配置信息失败:name="+name+"-------------------");
            throw new TheaException(TheaException.Type.THEA_SYSTEM_ERROR);
        }
        return resultDto;
    }*/


    @ApiOperation(value="OCDC推送", notes="OCDC推送客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "ocdcRawData", value = "JSON推送数据", required = true, paramType = "body", dataType = "OcdcData")
    })
    @RequestMapping(value = "/ocdcCustomerPush", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto ocdcCustomerPush(@RequestHeader("Authorization")String token, @RequestBody String ocdcRawData){
        OcdcData ocdcData = JSON.parseObject(ocdcRawData,OcdcData.class);
        CronusDto resultDto = new CronusDto();
        List<String> list = ocdcService.addOcdcCustomerNew(ocdcData,token);
        resultDto.setData(list);
        return resultDto;
    }

    public CronusDto serviceAllocate(@RequestBody CustomerDTO customerDTO){
        CronusDto resultDto = new CronusDto();
//        ocdcService.addOcdcCustomer(customerDTO);
        return resultDto;
    }

}
