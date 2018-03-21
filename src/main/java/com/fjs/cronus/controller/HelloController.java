package com.fjs.cronus.controller;

import com.fjs.cronus.service.AutoCleanService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private AutoCleanService autoCleanService;

    @ApiOperation(value = "test", notes = "test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/api/v1/hello", method = RequestMethod.GET)
    public void index(@RequestHeader("Authorization") String token) {

        autoCleanService.autoClean(token);
    }


}