package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CustomerSalePushLog;
import com.fjs.cronus.service.CustomerSalePushLogService;
import com.google.common.base.Splitter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(description = "分配信息日志查询")
@RequestMapping("/api/v1/customerSalePushLog")
@RestController
public class CustomerSalePushLogController {

    private static final Logger logger = LoggerFactory.getLogger(ResignCustomerController.class);

    @Autowired
    private CustomerSalePushLogService customerSalePushLogService;

    @ApiOperation(value = "根据条件查询日志信息", notes = " 根据条件查询日志信息 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{'mediaIds':'1,2,3','companyid':123}", paramType = "body", dataType = "JSONObject", required = false),
            @ApiImplicitParam(name = "pageNum", value = "页码", paramType = "query", dataType = "Integer", required = false),
            @ApiImplicitParam(name = "pageSize", value = "每页展示数量", paramType = "query", dataType = "Integer", required = false),
    })
    @PostMapping(value = "/findPageData")
    public CronusDto findPageData(@RequestBody(required = false) CustomerSalePushLog params, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize) {
        CronusDto result = new CronusDto();
        try {
            // 参数处理

            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            result.setData(customerSalePushLogService.findPageData(params, pageNum, pageSize));
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("创建特殊队列", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

}
