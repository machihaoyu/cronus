package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CallbackLogDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CallbackDTO;
import com.fjs.cronus.dto.cronus.RepeatChildDTO;
import com.fjs.cronus.dto.cronus.RepeatCustomerDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.CallbackService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by msi on 2017/10/10.
 */
@RequestMapping("/api/v1")
@Controller
public class CallbackController {

    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    CallbackService callbackService;

    @ApiOperation(value = "回访客户列表", notes = "回访客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "callback_start_time", value = "起始日期", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "callback_end_time", value = "结束日期", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "search_name", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "1 意向用户，2 协议用户，3 成交客户", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "search_city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "search_telephone", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "search_callback_status", value = "回防状态", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "communication_order", value = "1 从未回访，2 需要重新回访，3已经回访", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "cycle", value = "回访配置的周期", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "ownUserId", value = "负责人id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "isHaveOwn", value = "是否拥有负责人0没有，有", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "subCompanyId", value = "分公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/callbackCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CallbackDTO>> callbackCustomerList(@RequestParam(value = "callback_start_time", required = false) String callback_start_time,
                                                                    @RequestParam(value = "callback_end_time", required = false) String callback_end_time,
                                                                    @RequestParam(value = "search_name", required = false) String search_name,
                                                                    @RequestParam(value = "type", required = false) Integer type,
                                                                    @RequestParam(value = "search_city", required = false) String search_city,
                                                                    @RequestParam(value = "search_telephone", required = false) String search_telephone,
                                                                    @RequestParam(value = "search_callback_status", required = false) String search_callback_status,
                                                                    @RequestParam(value = "communication_order", required = false, defaultValue = "99") Integer communication_order,
                                                                    @RequestParam(value = "cycle", required = false) Integer cycle,
                                                                    @RequestParam(value = "ownUserId", required = false) Integer ownUserId,
                                                                    @RequestParam(value = "isHaveOwn", required = false) Integer isHaveOwn,
                                                                    @RequestParam(value = "subCompanyId", required = false) Integer subCompanyId,
                                                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                                                    @RequestHeader("Authorization") String token) {
        CronusDto<QueryResult<CallbackDTO>> resultCronusDto = new CronusDto<>();
        QueryResult<CallbackDTO> queryResult = new QueryResult();
        try {
            queryResult = callbackService.callbackCustomerList(callback_start_time, callback_end_time, search_name, type, search_city, search_telephone, search_callback_status, page, size, communication_order, cycle,
                    ownUserId, isHaveOwn, subCompanyId, token);
            resultCronusDto.setData(queryResult);
            resultCronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultCronusDto.setResult(ResultResource.CODE_SUCCESS);
            return resultCronusDto;
        } catch (Exception e) {
            logger.error("--------------->callbackCustomerList获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    /* @ApiOperation(value="打开回访页面", notes="打开回访页面")
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

     }*/
   /* @ApiOperation(value="获取回访信息详情", notes="获取回访信息详情")
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
    }*/
   /* @ApiOperation(value="获取正常状态问题回访详情", notes="获取正常状态问题回访详情")
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
    }*/
    /*@ApiOperation(value="获取正常状态所有问题", notes="获取正常状态所有问题")
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
*/
    @ApiOperation(value = "编辑回访信息接口", notes = "编辑回访信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'customerId':'客户id', 'callback_status';'回访状态','userId':'当前操作人id'}", required = true, paramType = "body", dataType = "JSONObject")
    })
    @RequestMapping(value = "/editCallbackOk", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCallbackOk(@RequestBody JSONObject jsonObject, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        Integer customerId = jsonObject.getInteger("customerId");
        String callback_status = jsonObject.getString("callback_status");
        Integer userId = jsonObject.getInteger("userId");
        if (customerId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (callback_status == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto = callbackService.editCallbackOk(customerId, callback_status, userId);
        } catch (Exception e) {
            logger.error("--------------->editCallbackOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return cronusDto;
    }

    @ApiOperation(value = "重复客户咨询", notes = "重复客户咨询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "repeat_start_time", value = "起始日期", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "repeat_end_time", value = "结束日期", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customer_name", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "repeat_callback_status", value = "1 未拨打，2 已拨打，3 不限", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/repeatcustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<RepeatCustomerDTO>> repeatcustomerList(@RequestParam(value = "telephonenumber", required = false) String telephonenumber,
                                                                        @RequestParam(value = "repeat_start_time", required = false) String repeat_start_time,
                                                                        @RequestParam(value = "repeat_end_time", required = false) String repeat_end_time,
                                                                        @RequestParam(value = "customer_name", required = false) String customer_name,
                                                                        @RequestParam(value = "repeat_callback_status", required = false) Integer repeat_callback_status,
                                                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                        @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                                                        @RequestHeader("Authorization") String token) {
        QueryResult<RepeatCustomerDTO> queryResult = new QueryResult();
        CronusDto<QueryResult<RepeatCustomerDTO>> resultCronusDto = new CronusDto<>();
        try {
            queryResult = callbackService.repeatcustomerList(telephonenumber, repeat_start_time, repeat_end_time, customer_name, repeat_callback_status, page, size);
            resultCronusDto.setData(queryResult);
            resultCronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultCronusDto.setResult(ResultResource.CODE_SUCCESS);
            return resultCronusDto;
        } catch (Exception e) {
            logger.error("--------------->repeatcustomerList获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "获取单个客户的申请列表", notes = "获取单个客户的申请列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getchildInfo", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<RepeatChildDTO>> getchildInfo(@RequestParam(value = "customerId", required = false) Integer customerId,
                                                        @RequestHeader("Authorization") String token) {
        CronusDto<List<RepeatChildDTO>> resultCronusDto = new CronusDto<>();
        try {
            List<RepeatChildDTO> list = callbackService.getchildInfo(customerId);
            resultCronusDto.setData(list);
            resultCronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultCronusDto.setResult(ResultResource.CODE_SUCCESS);
            return resultCronusDto;
        } catch (Exception e) {
            logger.error("--------------->repeatcustomerList获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

}
