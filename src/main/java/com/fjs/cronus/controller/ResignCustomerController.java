package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.uc.CompanyDto;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.dto.cronus.CustomerSourceDTO;
import com.fjs.cronus.dto.cronus.ImportInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.LookPoolService;
import com.fjs.cronus.service.uc.UcService;
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

import java.util.List;

/**
 * Created by msi on 2017/12/1.
 */
@RequestMapping("/api/v1")
@Controller
@Api(description = "查看盘控制器")
public class ResignCustomerController {

    private static final Logger logger = LoggerFactory.getLogger(ResignCustomerController.class);
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    LookPoolService lookPoolService;

    @Autowired
    UcService ucService;

    @ApiOperation(value = "获取客户来源", notes = "获取客户来源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/quitCustomerSource", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CustomerSourceDTO> quitCustomerSource(@RequestHeader("Authorization") String token) {
        CronusDto resultDto = new CronusDto();
        CustomerSourceDTO customerSourceDTO = new CustomerSourceDTO();
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            customerSourceDTO = customerInfoService.quitCustomerSource(userId, token);
            resultDto.setData(customerSourceDTO);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->quitCustomerSource获取客户来源，公司", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }


    @ApiOperation(value = "离职客户列表", notes = "离职客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "media", value = "媒体", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "ownUserName", value = "负责人名称", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "客户状态 意向客户 协议客户 成交客户", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "createTimeStart",value = "创建时间开始日期",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "createTimeEnd",value = "创建时间结束日期",required = false,paramType = "query",dataType = "string"),
    })
    @RequestMapping(value = "/resignCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> resignCustomerList(@RequestHeader("Authorization") String token,
                                                                      @RequestParam(required = false) String customerName,
                                                                      @RequestParam(required = false) String telephonenumber,
                                                                      @RequestParam(required = false) String media,
                                                                      @RequestParam(required = false) String utmSource,
                                                                      @RequestParam(required = false) String ownUserName,
                                                                      @RequestParam(required = false) String customerSource,
                                                                      @RequestParam(required = false) String level,
                                                                      @RequestParam(required = false) Integer companyId,
                                                                      @RequestParam(required = false) Integer page,
                                                                      @RequestParam(value = "createTimeStart",required = false) String createTimeStart,
                                                                      @RequestParam(value = "createTimeEnd",required = false) String createTimeEnd,
                                                                      @RequestParam(required = false) Integer size) {
        CronusDto resultDto = new CronusDto();
        QueryResult<CustomerListDTO> result = new QueryResult<CustomerListDTO>();
        try {
            result = customerInfoService.resignCustomerList(token, customerName, telephonenumber, utmSource,media,
                    ownUserName, customerSource, level, companyId, page, size,createTimeStart,createTimeEnd);
            resultDto.setData(result);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->quitCustomerSource获取客户来源，公司", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }

    @ApiOperation(value = "三无客户盘列表", notes = "三无客户盘列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "media", value = "媒体", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "ownUserName", value = "负责人名称", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "客户状态 意向客户 协议客户 成交客户", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "createTimeStart",value = "创建时间开始日期",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "createTimeEnd",value = "创建时间结束日期",required = false,paramType = "query",dataType = "string")
    })
    @RequestMapping(value = "/unablePool", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> unablePool(@RequestHeader("Authorization") String token,
                                                              @RequestParam(required = false) String customerName,
                                                              @RequestParam(required = false) String telephonenumber,
                                                              @RequestParam(required = false) String media,
                                                              @RequestParam(required = false) String utmSource,
                                                              @RequestParam(required = false) String ownUserName,
                                                              @RequestParam(required = false) String customerSource,
                                                              @RequestParam(required = false) String level,
                                                              @RequestParam(required = false) Integer companyId,
                                                              @RequestParam(required = false) Integer page,
                                                              @RequestParam(value = "createTimeStart",required = false) String createTimeStart,
                                                              @RequestParam(value = "createTimeEnd",required = false) String createTimeEnd,
                                                              @RequestParam(required = false) Integer size) {
        CronusDto resultDto = new CronusDto();
        QueryResult<CustomerListDTO> result = new QueryResult<CustomerListDTO>();
        try {
            result = lookPoolService.unablePool(token, customerName, telephonenumber, utmSource,media,ownUserName, customerSource,
                    level, companyId, page, size,createTimeStart,createTimeEnd);
            resultDto.setData(result);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->unablePool三五客户盘列表公司", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }

    @ApiOperation(value = "全部客户盘", notes = "全部客户盘")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "media", value = "媒体", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "ownUserName", value = "负责人名称", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "客户状态 意向客户 协议客户 成交客户", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "createTimeStart",value = "创建时间开始日期",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "createTimeEnd",value = "创建时间结束日期",required = false,paramType = "query",dataType = "string")
    })
    @RequestMapping(value = "/allPool", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> allPool(@RequestHeader("Authorization") String token,
                                                           @RequestParam(required = false) String customerName,
                                                           @RequestParam(required = false) String telephonenumber,
                                                           @RequestParam(required = false) String media,
                                                           @RequestParam(required = false) String utmSource,
                                                           @RequestParam(required = false) String ownUserName,
                                                           @RequestParam(required = false) String customerSource,
                                                           @RequestParam(required = false) String level,
                                                           @RequestParam(required = false) Integer companyId,
                                                           @RequestParam(required = false) Integer page,
                                                           @RequestParam(required = false) Integer size,
                                                           @RequestParam(value = "createTimeStart",required = false) String createTimeStart,
                                                           @RequestParam(value = "createTimeEnd",required = false) String createTimeEnd
                                                            ) {
        CronusDto resultDto = new CronusDto();
        QueryResult<CustomerListDTO> result = new QueryResult<CustomerListDTO>();
        try {
            result = lookPoolService.allPool(token, customerName, telephonenumber, utmSource,media,ownUserName,
                    customerSource, level, companyId, page, size,createTimeStart,createTimeEnd);
            resultDto.setData(result);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->unablePool三五客户盘列表公司", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }

    @ApiOperation(value = "获取重要修改信息", notes = "获取重要修改信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "客户id", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/editImportInfo", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<ImportInfoDTO> editImportInfo(@RequestHeader("Authorization") String token,
                                                   @RequestParam(required = false) Integer id) {
        CronusDto resultDto = new CronusDto();
        ImportInfoDTO importInfoDTO = new ImportInfoDTO();
        try {
            importInfoDTO = lookPoolService.editImportInfo(id, token);
            resultDto.setData(importInfoDTO);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->获取重要修改信息失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }

    @ApiOperation(value = "获取重要修改信息", notes = "获取重要修改信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "importInfoDTO", value = "importInfoDTO", required = false, paramType = "body", dataType = "ImportInfoDTO"),
    })
    @RequestMapping(value = "/editImportInfoOk", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editImportInfoOk(@RequestHeader("Authorization") String token,
                                      @RequestBody ImportInfoDTO importInfoDTO) {
        CronusDto resultDto = new CronusDto();
        try {
            boolean flag = lookPoolService.editImportInfoOk(importInfoDTO, token);
            resultDto.setData(flag);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->重要修改信息失败修改", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }

    @ApiOperation(value = "获取所有的总公司", notes = "获取所有的总公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/listAllEnableCompany", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto listAllEnableCompany(@RequestHeader("Authorization") String token) {
        CronusDto resultDto = new CronusDto();
        try {
            List<CompanyDto> companyDtos = ucService.listAllEnableCompany(token);
            resultDto.setData(companyDtos);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->重要修改信息失败修改", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }

    @ApiOperation(value = "公司间用户转移", notes = "公司间用户转移")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'customer_ids':'1,2,3','sub_company':'12'}", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/allocateToCompany", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto allocateToCompany(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject) {
        CronusDto resultDto = new CronusDto();
        //教研参数
        String customer_ids = jsonObject.getString("customer_ids");
        Integer sub_company = jsonObject.getInteger("sub_company");
        if (StringUtils.isEmpty(customer_ids)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (StringUtils.isEmpty(sub_company)) {
            throw new CronusException(CronusException.Type.CRM_LOSESUBCOMPANTID_ERROR);
        }
        try {
            boolean flag = lookPoolService.allocateToCompany(token, customer_ids, sub_company);
            resultDto.setData(flag);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->重要修改信息失败修改", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }
    @ApiOperation(value = "测试全部客户盘", notes = "测试全部客户盘")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道来源(除公盘外的必须传)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "ownUserName", value = "负责人名称", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "客户状态 意向客户 协议客户 成交客户", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/allPoolTest", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerInfo>> allPoolTest(@RequestHeader("Authorization") String token,
                                                        @RequestParam(required = false) String customerName,
                                                        @RequestParam(required = false) String telephonenumber,
                                                        @RequestParam(required = false) String utmSource,
                                                        @RequestParam(required = false) String ownUserName,
                                                        @RequestParam(required = false) String customerSource,
                                                        @RequestParam(required = false) String level,
                                                        @RequestParam(required = false) Integer companyId,
                                                        @RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size) {
        CronusDto resultDto = new CronusDto();
        QueryResult<CustomerInfo> result = new QueryResult<CustomerInfo>();
        try {
            result = lookPoolService.allPoolTest(token, customerName, telephonenumber, utmSource, ownUserName, customerSource, level, companyId, page, size);
            resultDto.setData(result);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("-------------->unablePool三五客户盘列表公司", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }

}
