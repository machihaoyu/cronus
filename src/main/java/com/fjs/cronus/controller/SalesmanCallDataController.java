package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.ResultDescription;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.SalesmanCallDataService;
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

import java.text.SimpleDateFormat;
import java.util.Date;

@Api(description = "业务员通话记录-控制器")
@RequestMapping("/api/v1/salesmanCallData")
@RestController
public class SalesmanCallDataController {

    private static final Logger logger = LoggerFactory.getLogger(SalesmanCallDataController.class);

    @Autowired
    private SalesmanCallDataService salesmanCallDataService;

    @ApiOperation(value = "[b端Android]录入通话记录", notes = "[b端Android]录入通话记录 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "" +
                    "{" +
                    "\"customerid\":顾客id," +
                    "\"startTime\":通话拨打时间，时间戳。例如：1531012442000," +
                    "\"answerTime\":通话拨通时间，时间戳。例如：1531012442000," +
                    "\"endTime\":通话结束时间，时间戳。例如：1531012442000," +
                    "\"duration\":通话时长，指拨通时间到结束时间。单位为秒，例如：449," +
                    "\"totalDuration\":总时间，指拨打时间到结束时间。单位为秒，例如：123," +
                    "\"customerPhoneNum\":客户手机号" +
                    "}"
                    , required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/saveCallData", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto saveCallData(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {
            // 提取数据
            Long customerid = jsonObject.getLong("customerid");
            Long startTime = jsonObject.getLong("startTime");
            Long answerTime = jsonObject.getLong("answerTime");
            Long endTime = jsonObject.getLong("endTime");
            Long duration = jsonObject.getLong("duration");
            Long totalDuration = jsonObject.getLong("totalDuration");
            Long customerPhoneNum = jsonObject.getLong("customerPhoneNum");
            Integer callType = 0; // 目前只记录成功的数据
            String recordingUrl = null; // 目前无录音文件地址数据

            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());


            salesmanCallDataService.addSingle(token, userId, customerid, startTime, answerTime, endTime, duration, totalDuration, callType, recordingUrl, CommonConst.SYSTYPE_B_ANDROID, customerPhoneNum);

            result.setData("成功");
            result.setResult(ResultDescription.CODE_SUCCESS);
            result.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("查询某个业务员通话某天通话时长", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "[非业务接口-管理接口]刷新all业务员通话某天通话时长cache", notes = "刷新all业务员通话某天通话时长cache api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据,{\"time\":\"2018-06-20\"}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/refreshCache", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto refreshCache(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {
            String time = jsonObject.getString("time");
            Date date = null;
            if (StringUtils.isBlank(time)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time 不能为空");
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(time);
            } catch (Exception e) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time格式不正确，需要yyyy-MM-dd");
            }

            salesmanCallDataService.refreshCache(date);
            result.setData("成功");
            result.setResult(ResultDescription.CODE_SUCCESS);
            result.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("刷新all业务员通话某天通话时长cache", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "[非业务接口-管理接口]查询某个业务员通话某天通话时长", notes = "查询某个业务员通话某天通话时长 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据,{\"name\":\"tome\",\"time\":\"2018-06-20\"}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/getDurationByName", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto getDurationByName(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {
            String name = jsonObject.getString("name");
            String time = jsonObject.getString("time");
            Date date = null;
            if (StringUtils.isBlank(name)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "name 不能为空");
            }
            if (StringUtils.isBlank(time)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time 不能为空");
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(time);
            } catch (Exception e) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time格式不正确，需要yyyy-MM-dd");
            }

            result.setData(salesmanCallDataService.getDurationByName(name, date));
            result.setResult(ResultDescription.CODE_SUCCESS);
            result.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("查询某个业务员通话某天通话时长", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

}
