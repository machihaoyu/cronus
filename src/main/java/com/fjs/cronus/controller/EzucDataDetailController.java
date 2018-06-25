package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.ResultDescription;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.TheaApiDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.EzucDataDetailService;
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

@Api(description = "EZUC控制器")
@RequestMapping("/api/v1/ezucQurtz")
@RestController
public class EzucDataDetailController {

    private static final Logger logger = LoggerFactory.getLogger(EzucDataDetailController.class);

    @Autowired
    private EzucQurtzService ezucQurtzService;

    @Autowired
    private EzucDataDetailService ezucDataDetailService;


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
            String time = jsonObject.getString("time"); // 可为空
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
                logger.error("查询一级吧、某月、某来源、某媒体实购数（分配数）:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "[非业务接口-管理接口]触发一次同步EZUC数据", notes = "触发一次同步EZUC数据 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "提交数据,{\"time\":\"2018-06-20\"}", required = true, dataType = "com.alibaba.fastjson.JSONObject")
    })
    @RequestMapping(value = "/syncData", method = RequestMethod.POST)
    @ResponseBody
    public TheaApiDTO syncData(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        TheaApiDTO resultDTO = new TheaApiDTO<>();

        try {
            Date date = null;
            String time = jsonObject.getString("time"); // 可为空
            if (StringUtils.isBlank(time)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time 不能为空");
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(time);
            } catch (Exception e) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time格式不正确，需要yyyy-MM-dd");
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
            logger.error("-----------触发一次同步EZUC数据--------" + e);
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
    public CronusDto refreshCache(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {
            String time = jsonObject.getString("time"); // 可为空
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
                logger.error("查询一级吧、某月、某来源、某媒体实购数（分配数）:", e);
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
    public CronusDto addSingleData(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto result = new CronusDto();

        try {
            String name = jsonObject.getString("name");
            String time = jsonObject.getString("time"); // 可为空
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
                logger.error("查询一级吧、某月、某来源、某媒体实购数（分配数）:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }
}
