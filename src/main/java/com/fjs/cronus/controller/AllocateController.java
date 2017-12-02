package com.fjs.cronus.controller;

import com.fjs.cronus.dto.CronusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by msi on 2017/12/2.
 */
@Controller
@Api(description = "批量分配")
@RequestMapping("/api/v1")
public class AllocateController {

    @ApiOperation(value="批量分配", notes="批量分配")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customer_ids", value = "客户id，逗号隔开 1,2,3", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "action", value = "removeCustomerAll：离职员工批量转移，allocateAll：批量分配", paramType = "query", dataType = "string"),
    })
    @RequestMapping(value = "/selUser", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto selUser(@RequestHeader("Authorization")String token,
                             @RequestParam(value = "customer_ids",required = false)String customer_ids,
                             @RequestParam(value = "action",required = false)String action){
        CronusDto cronusDto = new CronusDto();

        return  cronusDto;

    }


}
