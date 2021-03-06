package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSON;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.crm.OcdcData;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.enums.AllocateSource;
import com.fjs.cronus.service.AutoCleanService;
import com.fjs.cronus.service.allocatecustomer.v1.AutoAllocateService;
import com.fjs.cronus.service.allocatecustomer.v1.OcdcService;
import com.fjs.cronus.service.allocatecustomer.v2.AutoAllocateServiceV2;
import com.fjs.cronus.service.allocatecustomer.v2.OcdcServiceV2;
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
public class OcdcControllerV2 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private OcdcService ocdcService;

    @Autowired
    private OcdcServiceV2 ocdcServiceV2;

    @Autowired
    private AutoAllocateService autoAllocateService;

    @Autowired
    private AutoAllocateServiceV2 autoAllocateServiceV2;

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
            resultDto.setData(ocdcServiceV2.addOcdcCustomer(ocdcData, AllocateSource.OCDC, token));
            resultDto.setResult(0);
        } catch (Exception e) {
            resultDto.setResult(1);
        }
        return resultDto;
    }

    /*@ApiOperation(value = "未沟通分配开关", notes = "未沟通分配开关")
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
    }*/

    /*@ApiOperation(value = "未沟通分配开关", notes = "未沟通分配开关")
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
    }*/




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
            String key = ocdcServiceV2.serviceAllocate(customer, token);
            responseData.setRetData(key);
            responseData.setErrMsg("添加成功");
            responseData.setErrNum("0");
        } catch (Exception e) {
            responseData.setErrMsg("请联系上海城市CRM助理添加分配名额");
            responseData.setErrNum("1");
        }
        return responseData;
    }

    /*@ApiOperation(value = "未沟通分配", notes = "未沟通分配")
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
    }*/

    /*@ApiOperation(value = "待分配池分配", notes = "待分配池分配")
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
    }*/

    /*@ApiOperation(value = "待分配池分配锁开关", notes = "待分配池分配锁开关（0 运行，1停止）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/switchWaitingPoolAllocate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData switchWaitingPoolAllocate(@RequestHeader("Authorization") String token) {
        ResponseData responseData = new ResponseData();
        try {
            responseData.setRetData(ocdcService.switchWaitingPoolAllocate());
            responseData.setErrNum("0");
        } catch (Exception e) {
            responseData.setErrMsg(e.getMessage());
            responseData.setErrNum("1");
        }
        return responseData;
    }*/




    @ApiOperation(value = "当前是否是工作时间", notes = "当前是否是工作时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/currentWorkDayAndTime", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData currentWorkDayAndTime(@RequestHeader("Authorization") String token) {
        ResponseData responseData = new ResponseData();
        try {
            Boolean workTime = autoAllocateServiceV2.currentWorkDayAndTime(token);
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

    /*@ApiOperation(value = "获取时间范围内客户", notes = "获取时间范围内客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "start", value = "start", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "end", value = "end", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getCustomerPhone", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<String>> getCustomerPhone(@RequestHeader("Authorization") String token, @RequestParam(value = "start", required = false) String start,
                                                    @RequestParam(value = "end", required = false) String end) {

        CronusDto<List<String>> cronusDto = new CronusDto();
        try {
            cronusDto.setData(new ArrayList<>());
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        }
        catch (Exception e)
        {
            cronusDto.setResult(CommonMessage.FAIL.getCode());
            cronusDto.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        return cronusDto;
    }*/

}
