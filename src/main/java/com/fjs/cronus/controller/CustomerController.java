package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.CustomerInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

/**
 * Created by msi on 2017/9/13.
 */
@RestController
@RequestMapping(value = "/api/v1")
public class CustomerController {
    private  static  final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    CustomerInfoService customerInfoService;

    @ApiOperation(value="获取客户列表", notes="获取客户列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "createTime", value = "创建日期", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "电话号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/customerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto customerList(@RequestParam(value = "customerName",required = false) String customerName,
                                  @RequestParam(value = "createTimeStart",required = false) String createTimeStart,
                                  @RequestParam(value = "createTimeEnd",required = false) String createTimeEnd,
                                  @RequestParam(value = "telephonenumber",required = false) String telephonenumber,
                                  @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                  @RequestParam(value = "size",required = false,defaultValue = "10") Integer size) {


        CronusDto cronusDto = new CronusDto();
        try {
            QueryResult queryResult = customerInfoService.customerList(customerName,createTimeStart,createTimeEnd,telephonenumber,page,size);
            cronusDto.setData(queryResult);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取列表信息操作失败",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }



    @ApiOperation(value="手动添加客户", notes="手动添加客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerDTO", value = "", required = true, paramType = "body", dataType = "CustomerDTO")
    })
    @RequestMapping(value = "/addCrmCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto addCrmCustomer(@RequestBody CustomerDTO customerDTO, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto = customerInfoService.addCustomer(customerDTO,token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取列表信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value="根据手机号匹配客户", notes="根据手机号匹配客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "s手机号", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/findBytelephone", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findBytelephone(@RequestParam(required = false) String telephonenumber) {
        CronusDto cronusDto = new CronusDto();
        try {
           // JSONObject json = JSONObject.parseObject(jsonObject);
           // String telephonenumber = jsonObject.getString("telephonenumber");
            if (telephonenumber == null || "".equals(telephonenumber)) {
                throw new CronusException(CronusException.Type.CRM_CUSTOMERPHONE_ERROR);
            }
            cronusDto = customerInfoService.fingBytelephone(telephonenumber);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->fingBytelephone获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }
    @ApiOperation(value="根据客户id查找客户信息", notes="根据客户id查找客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerids", value = "1,2,3", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/findCustomerListByIds", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerListByIds(@RequestParam String customerids) {
        CronusDto cronusDto = new CronusDto();
        try {
         //   String customerids = jsonObject.getString("customerids");
            if (customerids == null || "".equals(customerids)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            cronusDto = customerInfoService.findCustomerListByIds(customerids);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->fingBytelephone获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }
    @ApiOperation(value="编辑客户信息", notes="编辑客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/editCustomer", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto editCustomer(@RequestParam Integer customerId) {
        CronusDto cronusDto = new CronusDto();
        try {
            //   String customerids = jsonObject.getString("customerids");
            if (customerId == null || "".equals(customerId)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            cronusDto = customerInfoService.editCustomer(customerId);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }
    @ApiOperation(value="根据属性获取客户信息", notes="根据属性获取客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/findCustomerByFeild", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerByFeild(@RequestParam Integer customerId) {
        CronusDto cronusDto = new CronusDto();
        try {
            if (customerId == null || "".equals(customerId)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            cronusDto = customerInfoService.findCustomerByFeild(customerId);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value="提交编辑客户信息", notes="提交编辑客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerDTO", value = "", required = true, paramType = "body", dataType = "CustomerDTO")
    })
    @RequestMapping(value = "/editCustomerOk", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCustomerOk(@RequestBody CustomerDTO customerDTO, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto = customerInfoService.editCustomerOk(customerDTO,token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->editCustomerOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


}
