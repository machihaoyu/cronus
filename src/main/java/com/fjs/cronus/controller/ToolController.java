package com.fjs.cronus.controller;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.SmsService;
import com.fjs.cronus.service.ToolService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by msi on 2017/10/12.
 */
@RequestMapping("/api/v1")
@Controller
public class ToolController {

    private  static  final Logger logger = LoggerFactory.getLogger(ToolController.class);

    @Autowired
    ToolService toolService;

    @Autowired
    private SmsService smsService;

    @ApiOperation(value="获取手机归属地", notes="获取手机归属地")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/getPhineArea", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getPhineArea(@RequestParam Integer customerId) {

        CronusDto cronusDto = new CronusDto();
        if (customerId == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto = toolService.getPhineArea(customerId);
        } catch (Exception e) {
            logger.error("--------------->getPhineArea获取手机号归属地出错", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }

    @ApiOperation(value="手机号加密接口", notes="手机号加密接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "telephone", value = "手机号", required = true, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/getPhoneDesc", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getPhoneDesc(@RequestParam String telephone) {

        CronusDto cronusDto = new CronusDto();
        if (telephone == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto = toolService.getPhoneDesc(telephone);
        } catch (Exception e) {
            logger.error("--------------->getPhineArea获取手机号归属地出错", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;



    }

    @ApiOperation(value="手机号解密接口", notes="手机号解密接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "telephone", value = "手机号", required = true, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/getPhoneDecode", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getPhoneDecode(@RequestParam String telephone) {

        CronusDto cronusDto = new CronusDto();
        if (telephone == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto = toolService.getPhoneDecode(telephone);
        } catch (Exception e) {
            logger.error("--------------->getPhineArea获取手机号归属地出错", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;



    }

    @ApiOperation(value="手机号解密接口", notes="手机号解密接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "telephone", value = "手机号", required = true, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/getPhoneDecode", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto sendMessage(@RequestParam String telephoneNumber,@RequestParam String content) {
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto.setData(smsService.sendCommunication(telephoneNumber,content));
            cronusDto.setResult(0);
        } catch (Exception e) {
            cronusDto.setMessage("");
            cronusDto.setResult(1);
        }
        return  cronusDto;



    }

}
