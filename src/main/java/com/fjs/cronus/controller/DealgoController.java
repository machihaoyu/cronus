package com.fjs.cronus.controller;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.model.DealgoProfile;
import com.fjs.cronus.service.DealgoService;
import com.fjs.cronus.util.DEC3Util;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2018/5/18.
 */
@RequestMapping("/api/v1/dealgoController")
@Controller
public class DealgoController {


    @Autowired
    private DealgoService dealgoService;


    @ApiOperation(value="获取", notes="获取dealgo用户画像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "telephone", value = "手机号（加密）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "标签名", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getProfile", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getProfile(@RequestParam String telephone,@RequestParam String name) {
        CronusDto cronusDto = new CronusDto();
        try {
            String phone = DEC3Util.des3DecodeCBC(telephone);
            List<DealgoProfile> dealgoProfiles = dealgoService.getDealgoData(phone,name);
            cronusDto.setData(dealgoProfiles);
            cronusDto.setResult(0);
        } catch (Exception e) {
            cronusDto.setMessage("");
            cronusDto.setResult(1);
        }
        return  cronusDto;



    }


    @ApiOperation(value="初始化dealgo数据", notes="初始化dealgo数据（每次同步处理一天的数据）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/initProfile", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto initProfile() {
        CronusDto cronusDto = new CronusDto();
        try {
            dealgoService.initProfileTask();
            cronusDto.setResult(0);
        } catch (Exception e) {
            cronusDto.setMessage("");
            cronusDto.setResult(1);
        }
        return  cronusDto;



    }


}
