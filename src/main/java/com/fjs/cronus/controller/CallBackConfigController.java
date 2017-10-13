package com.fjs.cronus.controller;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CallbackConfigDTO;
import com.fjs.cronus.dto.cronus.CallbackConfigList;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CallbackConfig;
import com.fjs.cronus.service.CallbackConfigService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by msi on 2017/10/11.
 */
@RequestMapping("/api/v1")
@Controller
public class CallBackConfigController {


    @Autowired
    CallbackConfigService callbackConfigService;
    private  static  final Logger logger = LoggerFactory.getLogger(CallBackConfigController.class);
    @ApiOperation(value="打开回访配置页面", notes="打开回访配置页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/editCallbackConfig", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto editCallbackConfig() {
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto  = callbackConfigService.editCallbackConfig();
        } catch (Exception e) {
            logger.error("--------------->editCallbackConfig打开配置页面失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

        return  cronusDto;


    }

    @ApiOperation(value="提交回访配置页面", notes="提交回访配置页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "callbackConfigList", value = "", required = true, paramType = "body", dataType = "CallbackConfigList")

    })
    @RequestMapping(value = "/editCallbackConfigOk", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCallbackConfigOk(@RequestBody CallbackConfigList callbackConfigList,@RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();

        if(callbackConfigList == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {

            cronusDto  = callbackConfigService.editCallbackConfigOk(callbackConfigList,token);

        } catch (Exception e) {
            logger.error("--------------->callbackCustomerList获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

        return  cronusDto;


    }
}
