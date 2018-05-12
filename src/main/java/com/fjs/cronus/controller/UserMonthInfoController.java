package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.UserMonthInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "月分配队列-控制器")
@RequestMapping("/api/v1/userMonthInfo")
@RestController
public class UserMonthInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ResignCustomerController.class);

    @Autowired
    private UserMonthInfoService userMonthInfoService;

    @ApiOperation(value = "查询一级吧、某月、某来源、某媒体实购数（分配数）", notes = "查询一级吧、某月、某来源、某媒体实购数（分配数） api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{'companyid':1,'month':201805,'sourceid':2,'mediaid':4}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findAssignedCustomerNum")
    public CronusDto findAssignedCustomerNum(@RequestHeader(name = "Authorization") String token, @RequestBody JSONObject params) {
        CronusDto result = new CronusDto();
        try {
            // 参加校验
            Integer companyid = params.getInteger("companyid");
            Integer month = params.getInteger("month");
            Integer sourceid = params.getInteger("sourceid");
            Integer mediaid = params.getInteger("mediaid");

            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("根据companyid找队列信息:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }


}
