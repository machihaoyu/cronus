package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.CompanyMediaQueueService;
import com.google.common.base.Splitter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Api(description = "分配队列基本信息-控制器")
@RequestMapping("/api/v1/companyMediaQueue")
@RestController
public class CompanyMediaQueueController {

    private static final Logger logger = LoggerFactory.getLogger(ResignCustomerController.class);

    @Autowired
    private CompanyMediaQueueService companyMediaQueueService;

    @ApiOperation(value = "根据companyid找队列信息", notes = "根据companyid找队列信息 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "subCompanyId", value = "分公司id;一级吧id", required = true, paramType = "query", dataType = "int"),
    })
    @GetMapping(value = "/findByCompanyId")
    public CronusDto findByCompanyId(@RequestHeader(name = "Authorization") String token, Integer subCompanyId) {
        CronusDto result = new CronusDto();
        try {
            // 参加校验
            if (subCompanyId == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "subCompanyId 不能为null");
            }

            String userId = SecurityContextHolder.getContext().getAuthentication().getName();

            List<Map<String, Object>> data = companyMediaQueueService.findByCompanyId(token, Integer.valueOf(userId), subCompanyId);
            result.setData(data);
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

    @ApiOperation(value = "创建特殊队列", notes = " 创建特殊队列 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据", required = false, paramType = "body", dataType = "object", example = "{'mediaIds':'1,2,3','companyid':'123'}"),
    })
    @PostMapping(value = "/addCompanyMediaQueue")
    public CronusDto addCompanyMediaQueue(@RequestHeader(name = "Authorization") String token, @RequestBody JSONObject params) {
        CronusDto result = new CronusDto();
        try {
            // 参数处理
            String mediaIds = params.getString("mediaIds");
            if (StringUtils.isBlank(mediaIds)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "mediaIds 不能为null");
            }

            Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
            List<String> mediaIdsList = splitter.splitToList(mediaIds);
            Set<Integer> mediaIdsSet = mediaIdsList.stream().map(Integer::valueOf).collect(Collectors.toSet());

            Integer companyid = params.getInteger("companyid");
            if (companyid == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "companyid 不能为null");
            }

            String userId = SecurityContextHolder.getContext().getAuthentication().getName();

            companyMediaQueueService.addCompanyMediaQueue(token, Integer.valueOf(userId), companyid, mediaIdsSet);

            result.setData(CommonMessage.SUCCESS.getCodeDesc());
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

    @ApiOperation(value = "删除特殊队列", notes = "删除特殊队列 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据", required = false, paramType = "body", dataType = "object", example = "{'mediaId':'1','companyid':'123'}"),
    })
    @PostMapping(value = "/delCompanyMediaQueue")
    public CronusDto delCompanyMediaQueue(@RequestHeader(name = "Authorization") String token,@RequestBody JSONObject params) {
        CronusDto result = new CronusDto();
        try {
            // 参加校验
            Integer mediaId = params.getInteger("mediaId");
            Integer companyid = params.getInteger("companyid");
            if(mediaId == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "mediaId 不能为null");
            }
            if (companyid == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "companyid 不能为null");
            }

            String userId = SecurityContextHolder.getContext().getAuthentication().getName();

            companyMediaQueueService.delCompanyMediaQueue(Integer.valueOf(userId), companyid, mediaId);

            result.setData(CommonMessage.SUCCESS.getCodeDesc());
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
