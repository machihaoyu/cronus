package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.ResultDescription;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.TheaApiDTO;
import com.fjs.cronus.entity.EzucQurtzLog;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.EzucDataDetailService;
import com.fjs.cronus.service.EzucQurtzLogService;
import com.fjs.cronus.service.quartz.EzucQurtzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(description = "EZUC控制器")
@RequestMapping("/api/v1/ezucQurtz")
@RestController
public class EzucDataDetailController {

    private static final Logger logger = LoggerFactory.getLogger(EzucDataDetailController.class);

    @Autowired
    private EzucQurtzService ezucQurtzService;

    @Autowired
    private EzucDataDetailService ezucDataDetailService;

    @Autowired
    private EzucQurtzLogService ezucQurtzLogService;


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

            result.setData(ezucDataDetailService.getDurationByName(name, date));
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

    @ApiOperation(value = "[非业务接口-管理接口]查询某天所有业务员通话时长", notes = "查询某天所有业务员通话时长 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据,{\"time\":\"2018-06-20\"}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/findAllFromCacheByDate", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    public CronusDto findAllFromCacheByDate(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
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

            result.setData(ezucDataDetailService.findAllFromCacheByDate(date));
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
                logger.error("查询某天所有业务员通话时长", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "[非业务接口-管理接口]触发一次同步EZUC数据", notes = "触发一次同步EZUC数据 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据,{\"time\":\"2018-06-20 13\"}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/syncData", method = RequestMethod.POST)
    @ResponseBody
    public TheaApiDTO syncData(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        TheaApiDTO resultDTO = new TheaApiDTO<>();

        try {
            Date date = null;
            String time = jsonObject.getString("time");
            if (StringUtils.isBlank(time)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time 不能为空");
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
                date = sdf.parse(time);
            } catch (Exception e) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time格式不正确，需要yyyy-MM-dd HH");
            }

            final Date temp = date;
            new Thread(() -> {
                this.ezucQurtzService.syncData(token, temp);
            }).start();
            resultDTO.setData("成功");
            resultDTO.setResult(ResultDescription.CODE_SUCCESS);
            resultDTO.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("触发一次同步EZUC数据", e);
            resultDTO.setData(null);
            resultDTO.setResult(ResultDescription.CODE_FAIL);
            resultDTO.setMessage(e.getMessage());
        }
        return resultDTO;
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

            ezucDataDetailService.refreshCache(date);
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

    @ApiOperation(value = "[非业务接口-测试接口]手动添加某业务员某日通话时长", notes = "[非业务接口-测试接口]手动添加某业务员某日通话时长 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据(duration是通话时长【分钟】),{\"name\":\"tome\",\"time\":\"2018-06-20\",\"duration\":1}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/addSingleData", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    public CronusDto addSingleData(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {
            String name = jsonObject.getString("name");
            String time = jsonObject.getString("time");
            Long duration = jsonObject.getLong("duration");
            duration = duration < 0 ? 0 : duration;
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

            ezucDataDetailService.addSingleData(name, date, duration);
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
                logger.error("手动添加某业务员某日通话时长", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "[非业务接口-管理接口]查询定时同步数据log", notes = "查询定时同步数据log api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string")
    })
    @RequestMapping(value = "/findEzucSyncLog", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto findEzucSyncLog(@RequestHeader("Authorization") String token, @RequestBody(required = false) EzucQurtzLog params, @RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize) {
        CronusDto result = new CronusDto();

        try {

            result.setData(ezucQurtzLogService.findEzucSyncLog(params, pageNum, pageSize));
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
                logger.error("查询定时同步数据log", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }
}
