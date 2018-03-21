package com.fjs.cronus.controller;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.callback.CallBackCustomerDTO;
import com.fjs.cronus.dto.callback.CustomerBaseInfoDTO;
import com.fjs.cronus.dto.callback.CustomerHouseInfoDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.callback.CallBackService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qiaoxin on 2018/3/20.
 */
@RestController
@RequestMapping(value = "/api/v1")
public class CustomerCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerCallbackController.class);

    @Autowired
    CallBackService callBackService;

    @ApiOperation(value = "获取客户列表", notes = "获取客户列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "callbackStartTime", value = "回访时间开始(yyyy-MM-dd)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "callbackEndTime", value = "回访时间结束(yyyy-MM-dd)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "createStartTime", value = "创建时间开始(yyyy-MM-dd)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "createEndTime", value = "创建时间结束(yyyy-MM-dd)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "ownUserName", value = "所有者名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "isHaveOwner", value = "是否拥有负责人(1:有 0:没有)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "callbackStatus", value = "回访状态", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "subCompanyId", value = "分公司ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "cycle", value = "回访间隔", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "type", value = "类型", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/callbackCustomerInfoList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CallBackCustomerDTO>> callbackCustomerInfoList(@RequestParam(required = false) String callbackStartTime, @RequestParam(required = false) String callbackEndTime,
                                                                     @RequestParam(required = false) String createStartTime, @RequestParam(required = false) String createEndTime,
                                                                     @RequestParam(required = false) String customerName, @RequestParam(required = false) String ownUserName, @RequestParam(required = false) String isHaveOwner,
                                                                     @RequestParam(required = false) String city, @RequestParam(required = false) String callbackStatus,
                                                                     @RequestParam(required = false) Integer companyId, @RequestParam(required = false) Integer subCompanyId, @RequestParam(required = true)Integer cycle, @RequestParam(required = true)String type,@RequestParam(required = true) Integer page, @RequestParam(required = true)Integer size) {

        CronusDto<QueryResult<CallBackCustomerDTO>> resultCronusDto = new CronusDto<QueryResult<CallBackCustomerDTO>>();

        try {
            Integer start = (page - 1) * size;
            QueryResult<CallBackCustomerDTO> callBackCustomerDTOQueryResult =
                    callBackService.getCustomerList(callbackStartTime, callbackEndTime,
                            createStartTime, createEndTime, customerName,
                            ownUserName, isHaveOwner, city, callbackStatus,
                            companyId, subCompanyId, type, start, size, cycle);

            resultCronusDto.setData(callBackCustomerDTOQueryResult);
            resultCronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultCronusDto.setResult(ResultResource.CODE_SUCCESS);
        } catch (Exception e) {
            logger.error("取得回访客户列表失败", e);

            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultCronusDto;
    }

    @ApiOperation(value = "获取客户基本信息", notes = "获取客户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/customerBaseInfo", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CustomerBaseInfoDTO> customerBaseInfo(@RequestParam Integer customerId) {
        CronusDto<CustomerBaseInfoDTO> cronusDto = new CronusDto();
        try {
            CustomerBaseInfoDTO customerBaseInfoDTO =
                    callBackService.getCustomerBaseInfo(customerId);
            cronusDto.setData(customerBaseInfoDTO);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
        } catch (Exception e) {
            logger.error("取得回访客户基本信息失败", e);

            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return cronusDto;
    }

    @ApiOperation(value = "获取客户房产信息", notes = "获取客户房产信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/customerHouseInfo", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CustomerHouseInfoDTO> customerHouseInfo(@RequestParam Integer customerId) {
        CronusDto<CustomerHouseInfoDTO> cronusDto = new CronusDto();
        try {
            CustomerHouseInfoDTO customerHouseInfoDTO =
                    callBackService.getCustomerHouseInfo(customerId);
            cronusDto.setData(customerHouseInfoDTO);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
        } catch (Exception e) {
            logger.error("取得回访客户房产信息失败", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return cronusDto;
    }

    @ApiOperation(value = "更新回访状态", notes = "更新回访状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "客户", required = true, paramType = "body", dataType = "JSONObject"),
    })
    @RequestMapping(value = "/updateCallBackStatus", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto updateCallBackStatus(@RequestBody JSONObject jsonObject) {
        CronusDto cronusDto = new CronusDto();

        Integer customerId = jsonObject.getInt("customerId");
        String callback_status = jsonObject.getString("callbackStatus");
        String per_description = jsonObject.getString("perDescription");

        try {
            callBackService.updateCallbackStatus(callback_status, per_description, customerId);
        } catch (Exception e) {
            logger.error("更新回访客户状态失败", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return cronusDto;
    }
}
