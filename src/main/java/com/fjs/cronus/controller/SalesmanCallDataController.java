package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.ResultDescription;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.SalemanRecordUploadLogService;
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

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(description = "业务员通话记录-控制器")
@RequestMapping("/api/v1/salesmanCallData")
@RestController
public class SalesmanCallDataController {

    private static final Logger logger = LoggerFactory.getLogger(SalesmanCallDataController.class);

    @Autowired
    private SalesmanCallDataService salesmanCallDataService;

    @Autowired
    private SalemanRecordUploadLogService salemanRecordUploadLogService;

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

            result.setData(salesmanCallDataService.addSingle(token, userId, customerid, startTime, answerTime, endTime, duration, totalDuration, callType, recordingUrl, CommonConst.SYSTYPE_B_ANDROID, customerPhoneNum));
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
    @Deprecated
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
    @Deprecated
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
                logger.error("[非业务接口-管理接口]查询某个业务员通话某天通话时长", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "[非业务接口-管理接口]init通话时长、通话次数、面见次数", notes = "init通话时长、通话次数、面见次数 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据,{\"time\":\"2018-06-01 01:01:01\",\"type\":\"day 或 month\"}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/initSyncData", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto initSyncData(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {
            Date time = jsonObject.getDate("time");
            if (time == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time 不能为空");
            }

            String type = jsonObject.getString("type");
            if (StringUtils.isBlank(type)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "type 不能为空");
            }
            List<String> strings = Arrays.asList("day", "month");
            if (!strings.contains(type)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "type 非法");
            }

            result.setData(salesmanCallDataService.initSyncData(type, time, token));
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
                logger.error("[非业务接口-管理接口]init通话时长、通话次数、面见次数", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "[非业务接口-管理接口]重新构建通话时长、通话次数、面见次数cache", notes = "重新构建通话时长、通话次数、面见次数cache api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据,{\"time\":\"2018-06-01 01:01:01\",\"type\":\"day 或 week 或 month \"}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/rebuildCatch", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto rebuildCatch(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {
            Date time = jsonObject.getDate("time");
            if (time == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time 不能为空");
            }

            String type = jsonObject.getString("type");
            if (StringUtils.isBlank(type)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "type 不能为空");
            }
            List<String> strings = Arrays.asList("day", "week", "month");
            if (!strings.contains(type)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "type 非法");
            }

            salesmanCallDataService.rebuildCatch(type, time, token);
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
                logger.error("[非业务接口-管理接口]重新构建通话时长、通话次数、面见次数cache", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "获取通话数据", notes = "获取通话数据 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据,{\"type\":\"day 或 week 或 month\"}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/getSaleManCallData", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto getSaleManCallData(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {

            String type = jsonObject.getString("type");
            if (StringUtils.isBlank(type)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "type 不能为空");
            }
            List<String> strings = Arrays.asList("day", "week", "month");
            if (!strings.contains(type)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "type 非法");
            }

            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

            result.setData(salesmanCallDataService.getSaleManCallData(token, userId, type.trim()));
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
                logger.error("获取通话数据", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "获取团队通话数据", notes = "获取团队通话数据 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据,{\"salesmanName\":\"业务员名\",\"departmentId\":1,\" finish\":\"false 或 true\"}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/findSaleManCallData", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto findSaleManCallData(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {

            String salesmanName = jsonObject.getString("salesmanName");
            Integer departmentId = jsonObject.getInteger("departmentId");
            Boolean finish = jsonObject.getBoolean("finish");

            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

            result.setData(salesmanCallDataService.findSaleManCallData(token, userId, salesmanName, departmentId, finish));
            result.setResult(ResultDescription.CODE_SUCCESS);
            result.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (e instanceof CronusException) {
                logger.error("获取团队通话数据", e);
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("获取团队通话数据", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "业务员通话语音上传", notes = "业务员通话语音上传 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "salesmanCallDataId", value = "记录通话数据后，服务器响应的id", paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/updateLoad", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto updateLoad(@RequestHeader("Authorization") String token, HttpServletRequest request, @RequestParam("salesmanCallDataId") Long salesmanCallDataId) {
        CronusDto result = new CronusDto();
        try {

            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

            result.setData(salemanRecordUploadLogService.updateLoad(request, salesmanCallDataId, userId));
            result.setResult(ResultDescription.CODE_SUCCESS);
            result.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (e instanceof CronusException) {
                logger.error("业务员通话语音上传", e);
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("业务员通话语音上传", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "查询通话记录（已上传语音）;b端Android", notes = "查询通话记录（已上传语音）;b端Android api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{" +
                    "\"customerPhone\":\"顾客手机号\",\"" +
                    "\"salemanPhone\":\"业务员手机号\"" +
                    "}", required = true, dataType = "com.alibaba.fastjson.JSONObject")

    })
    @RequestMapping(value = "/findBySalemanphoneAndCustomerphone", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto findBySalemanidAndCustomerid(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();
        try {

            Long customerPhone = jsonObject.getLong("customerPhone");
            Long salemanPhone = jsonObject.getLong("SalemanPhone");
            if (customerPhone == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "顾客手机号不能为空");
            }
            if (salemanPhone == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "业务员手机号不能为空");
            }

            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

            result.setData(salesmanCallDataService.findBySalemanidAndCustomerid(customerPhone, salemanPhone, userId));
            result.setResult(ResultDescription.CODE_SUCCESS);
            result.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (e instanceof CronusException) {
                logger.error("业务员通话语音上传", e);
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("业务员通话语音上传", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "查询通话记录（未上传语音）;b端Android", notes = "查询通话记录（未上传语音）;b端Android api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{" +
                    "\"salemanPhone\":\"业务员手机号\"" +
                    "}", required = true, dataType = "com.alibaba.fastjson.JSONObject")

    })
    @RequestMapping(value = "/findBySalemanphone", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto findBySalemanphone(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();
        try {

            Long salemanPhone = jsonObject.getLong("SalemanPhone");
            if (salemanPhone == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "业务员手机号不能为空");
            }

            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

            result.setData(salesmanCallDataService.findBySalemanphone(salemanPhone, userId));
            result.setResult(ResultDescription.CODE_SUCCESS);
            result.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            if (e instanceof CronusException) {
                logger.error("业务员通话语音上传", e);
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("业务员通话语音上传", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

}
