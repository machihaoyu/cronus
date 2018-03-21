package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSON;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.crm.OcdcData;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.enums.AllocateSource;
import com.fjs.cronus.service.AutoAllocateService;
import com.fjs.cronus.service.AutoCleanService;
import com.fjs.cronus.service.OcdcService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private AutoAllocateService autoAllocateService;

    @Autowired
    private AutoCleanService autoCleanService;

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
            resultDto.setData(ocdcService.addOcdcCustomer(ocdcData, AllocateSource.OCDC, token));
            resultDto.setResult(0);
        } catch (Exception e) {
            resultDto.setResult(1);
        }
        return resultDto;
    }

    @ApiOperation(value = "未沟通分配开关", notes = "未沟通分配开关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "状态（1，关闭；0，开启）", required = true, paramType = "body", dataType = "status")
    })
    @RequestMapping(value = "/nonCommunicateAgainStatus", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto nonCommunicateAgainStatus(@RequestHeader("Authorization") String token, @RequestBody String status) {
        CronusDto resultDto = new CronusDto();
        try {
            resultDto.setData(autoAllocateService.nonCommunicateAllocateStatus(status));
            resultDto.setResult(0);
        } catch (Exception e) {
            resultDto.setResult(1);
        }
        return resultDto;
    }

    @ApiOperation(value = "未沟通分配开关", notes = "未沟通分配开关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")
    })
    @RequestMapping(value = "/getNonCommunicateAgainStatus", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getNonCommunicateAgainStatus(@RequestHeader("Authorization") String token) {
        CronusDto resultDto = new CronusDto();
        try {
            resultDto.setData(autoAllocateService.getNonCommunicateAllocateStatus());
            resultDto.setResult(0);
        } catch (Exception e) {
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
            String key = ocdcService.serviceAllocate(customer, token);
            responseData.setRetData(key);
            responseData.setErrMsg("添加成功");
            responseData.setErrNum("0");
        } catch (Exception e) {
            responseData.setErrMsg("请联系上海城市CRM助理添加分配名额");
            responseData.setErrNum("1");
        }
        return responseData;
    }

    @ApiOperation(value = "未沟通分配", notes = "未沟通分配")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/nonCommunicateAgainAllocate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData nonCommunicateAgainAllocate(@RequestHeader("Authorization") String token) {
        ResponseData responseData = new ResponseData();
        try {
            autoAllocateService.nonCommunicateAgainAllocate(token);
            responseData.setErrNum("0");
        } catch (Exception e) {
            responseData.setErrMsg("");
            responseData.setErrNum("1");
        }
        return responseData;
    }

    @ApiOperation(value = "待分配池分配", notes = "待分配池分配")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/waitingPoolAllocate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData waitingPoolAllocate(@RequestHeader("Authorization") String token) {
        ResponseData responseData = new ResponseData();
        try {
            ocdcService.waitingPoolAllocate(token);
            responseData.setErrNum("0");
        } catch (Exception e) {
            responseData.setErrMsg("");
            responseData.setErrNum("1");
        }
        return responseData;
    }

    @ApiOperation(value = "当前是否是工作时间", notes = "当前是否是工作时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/currentWorkDayAndTime", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData currentWorkDayAndTime(@RequestHeader("Authorization") String token) {
        ResponseData responseData = new ResponseData();
        try {
            Boolean workTime = autoAllocateService.currentWorkDayAndTime(token);
            responseData.setRetData(workTime.toString());
            responseData.setErrNum("0");
        } catch (Exception e) {
            responseData.setErrMsg("");
            responseData.setErrNum("1");
        }
        return responseData;
    }

    @ApiOperation(value = "客户清洗", notes = "客户清洗")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", dataType = "string")
    })
    @RequestMapping(value = "/autoClean", method = RequestMethod.GET)
    public String autoClean(@RequestHeader("Authorization") String token) {

        return autoCleanService.autoClean(token);
    }

}
