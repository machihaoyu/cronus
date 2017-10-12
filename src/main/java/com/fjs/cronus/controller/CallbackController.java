package com.fjs.cronus.controller;

import com.fjs.cronus.dto.CallbackLogDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CallbackDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.CallbackService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by msi on 2017/10/10.
 */
@RequestMapping("/api/v1")
@Controller
public class CallbackController {

    private  static  final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    CallbackService callbackService;

    @ApiOperation(value="回访客户列表", notes="回访客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "callback_user", value = "回访人", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "callback_start_time", value = "起始日期", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "callback_end_time", value = "结束日期", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "search_name", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "1 意向用户，2 协议用户，3 成交客户", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "search_city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "search_telephone", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "search_callback_status", value = "回防状态", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "communication_order", value = "1 从未回访，2 需要重新回访，3已经回访", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/callbackCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public QueryResult callbackCustomerList(@RequestParam(value = "callback_user",required = false) String callback_user,
                                            @RequestParam(value = "callback_start_time",required = false) String callback_start_time,
                                            @RequestParam(value = "callback_end_time",required = false) String callback_end_time,
                                            @RequestParam(value = "search_name",required = false) String search_name,
                                            @RequestParam(value = "type",required = false) Integer type,
                                            @RequestParam(value = "search_city",required = false) String search_city,
                                            @RequestParam(value = "search_telephone",required = false) String search_telephone,
                                            @RequestParam(value = "search_callback_status",required = false) String search_callback_status,
                                            @RequestParam(value = "communication_order",required = false,defaultValue = "99") Integer communication_order,
                                            @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                            @RequestParam(value = "size",required = false,defaultValue = "20") Integer size,
                                            @RequestHeader("Authorization") String token){
        QueryResult queryResult = new QueryResult();
        try {
            queryResult  = callbackService.callbackCustomerList(callback_user,callback_start_time,callback_end_time,search_name,type,search_city,search_telephone,search_callback_status,page,size,communication_order,token);
            return queryResult;
        } catch (Exception e) {
            logger.error("--------------->callbackCustomerList获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value="打开回访页面", notes="打开回访页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/editCallback", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto editCallback(@RequestParam Integer customerId, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();

        if (customerId == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {

            cronusDto  = callbackService.editCallback(customerId,token);

        } catch (Exception e) {
            logger.error("--------------->editCallback打开回访页面失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;

    }
    @ApiOperation(value="获取回访信息详情", notes="获取回访信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/getCalledRecordInclude", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getCalledRecordInclude(@RequestParam Integer customerId, @RequestHeader("Authorization") String token){
        CronusDto cronusDto = new CronusDto();
        if (customerId == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto  = callbackService.getCalledRecordInclude(customerId,token);
        } catch (Exception e) {
            logger.error("--------------->editCallback打开回访页面失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }
    @ApiOperation(value="获取正常状态问题回访详情", notes="获取正常状态问题回访详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "日志id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/getOneQuestion", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto getOneQuestion(@RequestParam Integer id, @RequestHeader("Authorization") String token){
        CronusDto cronusDto = new CronusDto();
        if (id == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto  = callbackService.getOneQuestion(id,token);
        } catch (Exception e) {
            logger.error("--------------->getOneQuestion获取详情失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }
    @ApiOperation(value="获取正常状态所有问题", notes="获取正常状态所有问题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/getQuestion", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getQuestion(@RequestParam Integer customerId, @RequestHeader("Authorization") String token){
        CronusDto cronusDto = new CronusDto();
        if (customerId == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto  = callbackService.getQuestion(customerId,token);
        } catch (Exception e) {
            logger.error("--------------->editCallback打开回访页面失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }

    @ApiOperation(value="提交回访页面成功", notes="提交回访页面成功")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "callbackDTO", value = "", required = true, paramType = "body", dataType = "CallbackDTO")
    })
    @RequestMapping(value = "/editCallbackOk", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCallbackOk(@RequestBody CallbackDTO callbackDTO, @RequestHeader("Authorization") String token){
        CronusDto cronusDto = new CronusDto();
        if (callbackDTO.getCustomerId() == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto  = callbackService.editCallbackOk(callbackDTO,token);
        } catch (Exception e) {
            logger.error("--------------->editCallbackOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }

}
