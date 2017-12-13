package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.crm.JSONData;
import com.fjs.cronus.dto.crm.OcdcData;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.entity.AllocateEntity;
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
 * 客户
 * <p>
 * Created by feng on 2017/7/14.
 */
@Controller
@RequestMapping(value = "/api/v1/ocdc")
public class OcdcController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private OcdcService ocdcService;

    @ApiOperation(value = "OCDC推送", notes = "OCDC推送客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "ocdcRawData", value = "JSON推送数据", required = true, paramType = "body", dataType = "OcdcData")
    })
    @RequestMapping(value = "/ocdcCustomerPush", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto ocdcCustomerPush(@RequestHeader("Authorization") String token, @RequestBody String ocdcRawData) {
        OcdcData ocdcData = JSON.parseObject(ocdcRawData, OcdcData.class);
        CronusDto resultDto = new CronusDto();
        try {
            ocdcService.addOcdcCustomerNew(ocdcData, token);
            resultDto.setResult(0);
        }catch (Exception e)
        {
            resultDto.setResult(1);
        }
        return resultDto;
    }

    @ApiOperation(value = "客服推送", notes = "客服推送客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customer", value = "JSON推送数据", required = true, paramType = "body", dataType = "String")
    })
    @RequestMapping(value = "/serviceAllocate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData serviceAllocate(@RequestHeader("Authorization") String token, @RequestBody String customer) {
        ResponseData responseData = new ResponseData();
        try {
            ocdcService.serviceAllocate(customer, token);
            responseData.setErrMsg("添加成功");
            responseData.setErrNum("0");
        } catch (Exception e) {
            responseData.setErrMsg("请联系上海城市CRM助理添加分配名额");
            responseData.setErrNum("1");
        }
        return responseData;
    }

}
