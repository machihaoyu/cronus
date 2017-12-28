package com.fjs.cronus.controller.app;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.controller.AllocateController;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.cronus.RemoveDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.App.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by msi on 2017/12/28.
 */

@Controller
@Api(description = "App端接口控制器")
@RequestMapping("/api/v1")
public class AppController {

    @Autowired
    AppService appService;
    private  static  final Logger logger = LoggerFactory.getLogger(AppController.class);

    @ApiOperation(value="App获取业务员当天的沟通数与领取数", notes="App获取业务员当天的沟通数与领取数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})

    @RequestMapping(value = "/getReceiveAndKeepCount", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto removeCustomerAll(@RequestParam(value = "customerId")  Integer customerId,
                                       @RequestHeader("Authorization")String token){

        CronusDto cronusDto = new CronusDto();
        //校验权限
        try {
            cronusDto  = appService.getReceiveAndKeepCount(customerId);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->removeCustomer离职员工批量转移操作失败",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

    }





}
