package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.vipUtm.*;
import com.fjs.cronus.service.VipUtmSourceMangerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msi on 2018/2/26.
 */
@Controller
@Api(description = "Vip渠道配置管理")
@RequestMapping("/api/v1")
public class VipUtmSourceMangerController {


    private static final Logger logger = LoggerFactory.getLogger(VipUtmSourceMangerController.class);

    @Autowired
    VipUtmSourceMangerService vipUtmSourceMangerService;



    @ApiOperation(value = "渠道配置管理列表", notes = "渠道配置管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "page", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "size", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/vipUserManList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<VipUtmManListDTO>> VipUserManList(@RequestHeader("Authorization")String token,
                                                            @RequestParam Integer page,
                                                            @RequestParam Integer size){
        CronusDto<List<VipUtmManListDTO>> resultDTO = new CronusDto<>();
        List<VipUtmManListDTO> resultList = new ArrayList<>();

        try {
            resultList    = vipUtmSourceMangerService.vipUserManList(token,page,size);
            resultDTO.setData(resultList);
            resultDTO.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDTO.setResult(ResultResource.CODE_SUCCESS);
        }catch (Exception e) {
            logger.warn("------------>VipUserManList获取信息出错", e);
            resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
        }
        return  resultDTO;
    }


    @ApiOperation(value = "获取展开列表", notes = "获取展开列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id",paramType = "query", required = true, dataType = "string")
    })
    @RequestMapping(value = "/getChildInfo", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<ChildInfoDTO>> GetChildInfo(@RequestHeader("Authorization")String token,
                                                      @RequestParam(value = "userId",required = true) String userId){
        CronusDto<List<ChildInfoDTO>> resultDTO = new CronusDto<>();
        List<ChildInfoDTO> resultList = new ArrayList<>();
        if (StringUtils.isEmpty(userId)){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_SUCCESS);
            return resultDTO;
        }
        try {
            resultList = vipUtmSourceMangerService.getChildInfo(token,userId);
            resultDTO.setData(resultList);
            resultDTO.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDTO.setResult(ResultResource.CODE_SUCCESS);
        }catch (Exception e) {
            logger.warn("------------>GetChildInfo获取信息出错", e);
            resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
        }
        return  resultDTO;
    }

    @ApiOperation(value = "添加能管理的渠道", notes = "添加能管理的渠道")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "utmSourceDTO", value = "utmSourceDTO", paramType = "body",required = true, dataType = "UtmSourceDTO")
    })
    @RequestMapping(value = "/addUtmsource", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto AddUtmsource(@RequestHeader("Authorization")String token,
                                  @RequestBody UtmSourceDTO utmSourceDTO){
        CronusDto resultDTO = new CronusDto<>();
        if (StringUtils.isEmpty(utmSourceDTO.getName())){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        if (StringUtils.isEmpty(utmSourceDTO.getUtmSource())){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        try {
            resultDTO = vipUtmSourceMangerService.addUtmsource(token,utmSourceDTO);
        }catch (Exception e) {
            logger.warn("------------>AddUtmsource添加能管理的渠道", e);
            resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        return  resultDTO;
    }

    @ApiOperation(value = "删除渠道", notes = "删除渠道")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'id',XXXX}", paramType = "body",required = true, dataType = "JSONObject")
    })
    @RequestMapping(value = "/delOneUtm", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto DelOneUtm(@RequestHeader("Authorization")String token,
                                  @RequestBody JSONObject jsonObject){
        CronusDto resultDTO = new CronusDto<>();
        Integer id = jsonObject.getInteger("id");
        if (id == null){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        try {
            resultDTO = vipUtmSourceMangerService.delOneUtm(token,id);
        }catch (Exception e) {
            logger.warn("------------>delOneUtm删除渠道出错", e);
            resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        return  resultDTO;
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'userId',XXXX}", paramType = "body",required = true, dataType = "JSONObject")
    })
    @RequestMapping(value = "/delOneUser", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto DelOneUser(@RequestHeader("Authorization")String token,
                               @RequestBody JSONObject jsonObject){
        CronusDto resultDTO = new CronusDto<>();
        String userId = jsonObject.getString("userId");
        if (StringUtils.isEmpty(userId)){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        try {
            resultDTO = vipUtmSourceMangerService.delOneUser(token,userId);
        }catch (Exception e) {
            logger.warn("------------>DelOneUser删除用户出错", e);
            resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        return  resultDTO;
    }
    @ApiOperation(value = "添加一个用户", notes = "添加一个用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'userId',XXXX}", paramType = "body",required = true, dataType = "JSONObject")
    })
    @RequestMapping(value = "/addOneUser", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto AddOneUser(@RequestHeader("Authorization")String token,
                                @RequestBody JSONObject jsonObject){
        CronusDto resultDTO = new CronusDto<>();
        String userId = jsonObject.getString("userId");
        if (StringUtils.isEmpty(userId)){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        try {
            resultDTO = vipUtmSourceMangerService.addOneUser(token,userId);
        }catch (Exception e) {
            logger.warn("------------>addOneUser添加一个用户", e);
            resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        return  resultDTO;
    }
    @ApiOperation(value = "获取能管理的渠道", notes = "获取能管理的渠道")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/canMangerUtm", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto CanMangerUtm(@RequestHeader("Authorization")String token){
        CronusDto resultDTO = new CronusDto<>();
        try {
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (StringUtils.isEmpty(userId.toString())){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
            resultDTO = vipUtmSourceMangerService.canMangerUtm(token,userId.toString());
        }catch (Exception e) {
            logger.warn("------------>canMangerUtm获取渠道信息出错", e);
            if (e instanceof NumberFormatException){
                resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
                resultDTO.setResult(ResultResource.CODE_ERROR);
                return resultDTO;
            }
            resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        return  resultDTO;
    }

    @ApiOperation(value = "获取管理推送客户列表", notes = "获取管理推送客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道名称", required = true, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "p", value = "页数", required = true, paramType = "query",  dataType = "int"),
    })
    @RequestMapping(value = "/getOcdcCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<OcdcReturnDTO> GetOcdcCustomerList(@RequestHeader("Authorization")String token,
                                         @RequestParam String utmSource,
                                         @RequestParam Integer p){
        CronusDto<OcdcReturnDTO> resultDTO = new CronusDto<>();
        if (StringUtils.isEmpty(utmSource)){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        if (p == null){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        try {
            Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            if (StringUtils.isEmpty(userId.toString())){
                resultDTO.setMessage(ResultResource.PARAMS_ERROR);
                resultDTO.setResult(ResultResource.CODE_ERROR);
                return resultDTO;
            }
            resultDTO = vipUtmSourceMangerService.getOcdcCustomerList(token,userId.toString(),utmSource,p);
        }catch (Exception e) {
            logger.warn("------------>canMangerUtm获取渠道信息出错", e);
            if (e instanceof NumberFormatException){
                resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
                resultDTO.setResult(ResultResource.CODE_ERROR);
                return resultDTO;
            }
            resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        return  resultDTO;
    }

    @ApiOperation(value = "获取渠道分配客户列表", notes = "获取渠道分配客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道名称", required = true, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "customerType", value = "客户类型(意向客户。。。)", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "startTime", value = "起始日期", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "endTime", value = "结束日期", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name="cooperationStatus",value = "跟进状态",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "电话号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "页数", required = true, paramType = "query",  dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示", required = true, paramType = "query",  dataType = "int"),
    })
    @RequestMapping(value = "/utmCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<UtmCustomerDTO>> utmCustomerList(@RequestHeader("Authorization")String token,
                                     @RequestParam(required = true) String utmSource,
                                     @RequestParam(required = false) String customerName,
                                     @RequestParam(required = false) String customerType,
                                     @RequestParam(required = false) String startTime,
                                     @RequestParam(required = false) String endTime,
                                     @RequestParam(required = false) String cooperationStatus,
                                     @RequestParam(required = false) String telephonenumber,
                                     @RequestParam(required = true) Integer page,
                                     @RequestParam(required = true) Integer size){
        CronusDto<QueryResult<UtmCustomerDTO>> resultDTO = new CronusDto<>();
        if (StringUtils.isEmpty(utmSource)){
            resultDTO.setMessage(ResultResource.PARAMS_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        try {
            Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            if (StringUtils.isEmpty(userId.toString())){
                resultDTO.setMessage(ResultResource.PARAMS_ERROR);
                resultDTO.setResult(ResultResource.CODE_ERROR);
                return resultDTO;
            }
            resultDTO = vipUtmSourceMangerService.utmCustomerList(token,userId.toString(),utmSource,customerName,customerType,startTime,endTime,
                                                                  cooperationStatus,telephonenumber,page,size);
        }catch (Exception e) {
            logger.warn("------------>utmCustomerList获取渠道客户信息出错", e);
            if (e instanceof NumberFormatException){
                resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
                resultDTO.setResult(ResultResource.CODE_ERROR);
                return resultDTO;
            }
            return resultDTO;
        }
        return  resultDTO;
    }
}
