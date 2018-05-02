package com.fjs.cronus.controller;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;

import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPLoginDto;

import com.fjs.cronus.dto.cronus.AddPrdCustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;

import com.fjs.cronus.dto.cronus.PrdCustomerDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.PrdCustomer;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.PrdCustomerService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yinzf on 2017/11/2.
 */

@Controller
@RequestMapping("/api/v1")
@Api(description = "市场推广盘")
public class PrdCustomerController {
    private static final Logger logger = LoggerFactory.getLogger(PrdCustomerController.class);

    @Autowired
    private PrdCustomerService prdCustomerService;
    @Autowired
    private UcService thorUcService;
    @Autowired
    private CustomerInfoService iCustomerService;

    @ApiOperation(value = "保存推广盘", notes = "保存推广盘")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/updatePrdCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto updatePrdCustomer(@Valid @RequestBody AddPrdCustomerDTO prdCustomerDTO, BindingResult result, HttpServletRequest request) {
        CronusDto theaApiDTO = new CronusDto();
        if (result.hasErrors()) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        String token = request.getHeader("Authorization");
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        String[] authority = resultDto.getAuthority();
        if (authority.length > 0) {
            List<String> authList = Arrays.asList(authority);
            if (authList.contains(CommonConst.UPDATE_PRDCUSTOMER_URL)) {
                theaApiDTO.setResult(CommonMessage.UPDATE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        UserInfoDTO userInfoDTO = resultDto.getUser_info();
        try {
            int createResult = prdCustomerService.updatePrdCustomer(prdCustomerDTO, userInfoDTO);
            if (createResult > 0) {
                theaApiDTO.setResult(CommonMessage.UPDATE_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.UPDATE_SUCCESS.getCodeDesc());
            } else {
                theaApiDTO.setResult(CommonMessage.UPDATE_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.UPDATE_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        } catch (Exception e) {
            logger.error("-------------->updatePrdCustomer更新市场推广盘失败:" + e);
            if (e instanceof CronusException) {
                theaApiDTO.setResult(CommonMessage.UPDATE_FAIL_OWNER.getCode());
                theaApiDTO.setMessage(CommonMessage.UPDATE_FAIL_OWNER.getCodeDesc());
                return theaApiDTO;
            }
            theaApiDTO.setResult(CommonMessage.UPDATE_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.UPDATE_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }

    @ApiOperation(value = "删除推广盘", notes = "删除推广盘")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "推广盘id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/deletePrdCustomer", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto deletePrdCustomer(@RequestParam(value = "id") Integer id, HttpServletRequest request) {
        CronusDto theaApiDTO = new CronusDto();
        String token = request.getHeader("Authorization");
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        String[] authority = resultDto.getAuthority();
        if (authority.length > 0) {
            List<String> authList = Arrays.asList(authority);
            if (authList.contains(CommonConst.DELETE_PRDCUSTOMER_URL)) {
                theaApiDTO.setResult(CommonMessage.DELETE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        UserInfoDTO userInfoDTO = resultDto.getUser_info();
        try {
            if (id != null) {
                int deleteResult = prdCustomerService.delete(id, userInfoDTO);
                if (deleteResult > 0) {
                    theaApiDTO.setResult(CommonMessage.DELETE_SUCCESS.getCode());
                    theaApiDTO.setMessage(CommonMessage.DELETE_SUCCESS.getCodeDesc());
                } else {
                    theaApiDTO.setResult(CommonMessage.DELETE_FAIL.getCode());
                    theaApiDTO.setMessage(CommonMessage.DELETE_FAIL.getCodeDesc());
                    throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
                }
            } else {
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.ID_NULL);
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        } catch (Exception e) {
            logger.error("-------------->DELETEPrdCustomer删除市场推广盘失败:" + e);
            theaApiDTO.setResult(CommonMessage.DELETE_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.DELETE_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }

    @ApiOperation(value = "获取市场推广盘列表", notes = "获取市场推广盘列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerType", value = "客户类型", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "电话号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "houseStatus", value = "有无房产", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "等级", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "mountLevle", value = "1：0-20万，2：20-50万，3:50-100万，4:100-500万，5：大于五百万 ", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "type", value = "市场推广盘类型,工作盘传1，沉淀盘传2", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/prdCustomeList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<PrdCustomerDTO>> listPrdCustome(HttpServletRequest request, @RequestParam(required = false) String customerName,
                                                                 @RequestParam(required = false) String telephonenumber,
                                                                 @RequestParam(required = false) String customerType,
                                                                 @RequestParam(required = false) String level,
                                                                 @RequestParam(required = false) String houseStatus,
                                                                 @RequestParam(required = false) String city,
                                                                 @RequestParam(required = true) Integer type,
                                                                 @RequestParam(required = false) Integer mountLevle,
                                                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                                                 @RequestHeader("Authorization") String token) {
        CronusDto<QueryResult<PrdCustomerDTO>> cronusDto = new CronusDto<QueryResult<PrdCustomerDTO>>();
        QueryResult<PrdCustomerDTO> result = null;
        try {
            result = prdCustomerService.listByCondition(customerName, telephonenumber, customerType, level, houseStatus, city, type, mountLevle, page, size, token);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        } catch (Exception e) {
            logger.error("获取市场推广盘列表失败", e);
            cronusDto.setResult(CommonMessage.FAIL.getCode());
            cronusDto.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        cronusDto.setData(result);

        return cronusDto;
    }


    @ApiOperation(value = "跟进", notes = "跟进")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/decayPrdCustomer", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto decayPrdCustomer(@RequestParam Integer id, @RequestHeader("Authorization") String token) {
        CronusDto theaApiDTO = new CronusDto();
        UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        }
        try {
            PrdCustomerDTO prdCustomerDTO = prdCustomerService.decayPrdCustomer(id, Integer.valueOf(userInfoDTO.getUser_id()), token);
            theaApiDTO.setData(prdCustomerDTO);
            theaApiDTO.setMessage(ResultResource.MESSAGE_SUCCESS);
            theaApiDTO.setResult(ResultResource.CODE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->获取信息失败:" + e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;

            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return theaApiDTO;
    }


    @ApiOperation(value = "获取市场推广盘列表", notes = "获取市场推广盘列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerType", value = "客户类型", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "电话号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "houseStatus", value = "有无房产", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "等级", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "mountLevle", value = "1：0-20万，2：20-50万，3:50-100万，4:100-500万，5：大于五百万 ", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "type", value = "市场推广盘类型,工作盘传1，沉淀盘传2", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "orderField", value = "排序字段(receive_time,create_time,last_update_time)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sort", value = "asc ,desc", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "createTimeStart",value = "创建时间开始日期",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "createTimeEnd",value = "创建时间结束日期",required = false,paramType = "query",dataType = "string")
    })
    @RequestMapping(value = "/listPrdCustomeNew", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<PrdCustomerDTO>> listPrdCustomeNew(HttpServletRequest request, @RequestParam(required = false) String customerName,
                                                                 @RequestParam(required = false) String telephonenumber,
                                                                 @RequestParam(required = false) String customerType,
                                                                 @RequestParam(required = false) String level,
                                                                 @RequestParam(required = false) String houseStatus,
                                                                 @RequestParam(required = false) String city,
                                                                 @RequestParam(required = true) Integer type,
                                                                 @RequestParam(required = false) Integer mountLevle,
                                                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                                                 @RequestParam(value = "orderField", required = false) String orderField,
                                                                 @RequestParam(value = "sort", required = false) String sort,
                                                                    @RequestParam(value = "createTimeStart",required = false) String createTimeStart,
                                                                    @RequestParam(value = "createTimeEnd",required = false) String createTimeEnd,
                                                                 @RequestHeader("Authorization") String token) {
        CronusDto<QueryResult<PrdCustomerDTO>> cronusDto = new CronusDto<QueryResult<PrdCustomerDTO>>();
        QueryResult<PrdCustomerDTO> result = null;
        try {
            result = prdCustomerService.listByConditionNew(customerName, telephonenumber, customerType, level, houseStatus, city,
                    type, mountLevle, page, size, token,orderField,sort,createTimeStart,createTimeEnd);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        } catch (Exception e) {
            logger.error("获取市场推广盘列表失败", e);
            cronusDto.setResult(CommonMessage.FAIL.getCode());
            cronusDto.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        cronusDto.setData(result);

        return cronusDto;
    }
}
