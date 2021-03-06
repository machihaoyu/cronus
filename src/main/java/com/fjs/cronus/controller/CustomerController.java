package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.*;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.api.ThorApiDTO;
import com.fjs.cronus.dto.api.WalletApiDTO;
import com.fjs.cronus.dto.api.uc.SubCompanyDto;
import com.fjs.cronus.dto.cronus.*;
import com.fjs.cronus.dto.customer.CustomerCountDTO;
import com.fjs.cronus.dto.customer.CustomerDTO3;
import com.fjs.cronus.dto.ourea.CrmPushCustomerDTO;
import com.fjs.cronus.dto.thea.LoanDTO6;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.uc.UserModelDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.*;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by msi on 2017/9/13.
 */
@RestController
@Api(description = "客户控制器")
@RequestMapping(value = "/api/v1")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    DocumentService documentService;
    @Autowired
    UcService thorUcService;
    @Autowired
    CommunicationLogService communicationLogService;
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    AllocateLogService allocateLogService;
    @Autowired
    private ThorService thorService;

    @Autowired
    SmsService smsService;
    @ApiOperation(value = "获取客户列表", notes = "获取客户列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "电话号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "ownUserName", value = "负责人", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "circle", value = "客户周期 1新分配 2已领取 3待见面 4已签约", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "remain", value = "是否保留  0不保留1保留2已签合同", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "level", value = "客户状态 意向客户 协议客户 成交客户", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/customerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> customerList(@RequestParam(value = "customerName", required = false) String customerName,
                                                                @RequestParam(value = "telephonenumber", required = false) String telephonenumber,
                                                                @RequestParam(value = "utmSource", required = false) String utmSource,
                                                                @RequestParam(value = "ownUserName", required = false) String ownUserName,
                                                                @RequestParam(value = "customerSource", required = false) String customerSource,
                                                                @RequestParam(value = "circle", required = false) Integer circle,
                                                                @RequestParam(value = "companyId", required = false) Integer companyId,
                                                                @RequestParam(value = "remain", required = false) Integer remain,
                                                                @RequestParam(value = "level", required = false) String level,
                                                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                                @RequestHeader("Authorization") String token) {


        CronusDto<QueryResult<CustomerListDTO>> cronusDto = new CronusDto();
        //获取当前用户登录的id
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            QueryResult queryResult = customerInfoService.customerList(userId, customerName, telephonenumber,
                    utmSource, ownUserName, customerSource, circle, companyId, page, size, remain, level, token);
            cronusDto.setData(queryResult);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取列表信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "获取客户列表(增加排序)", notes = "获取客户列表信息(增加排序)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "电话号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "ownUserName", value = "负责人", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "circle", value = "客户周期 1新分配 2已领取 3待见面 4已签约", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "remain", value = "是否保留  0不保留1保留2已签合同", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "level", value = "客户状态 意向客户 协议客户 成交客户", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name="cooperationStatus",value = "跟进状态",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="communication_order",value = "沟通状态0未沟通未确认，1，已沟通未确认，3，已沟通已确认",required = false,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "orderField", value = "排序字段(receive_time,create_time,communicate_time)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sort", value = "asc ,desc", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "createTimeStart",value = "创建时间开始日期",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "createTimeEnd",value = "创建时间结束日期",required = false,paramType = "query",dataType = "string")

    })
    @RequestMapping(value = "/customerListNew", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> customerList(@RequestParam(value = "customerName", required = false) String customerName,
                                                                @RequestParam(value = "telephonenumber", required = false) String telephonenumber,
                                                                @RequestParam(value = "utmSource", required = false) String utmSource,
                                                                @RequestParam(value = "ownUserName", required = false) String ownUserName,
                                                                @RequestParam(value = "customerSource", required = false) String customerSource,
                                                                @RequestParam(value = "circle", required = false) Integer circle,
                                                                @RequestParam(value = "companyId", required = false) Integer companyId,
                                                                @RequestParam(value = "remain", required = false) Integer remain,
                                                                @RequestParam(value = "level", required = false) String level,
                                                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                                @RequestParam(value = "orderField", required = false) String orderField,
                                                                @RequestParam(value = "sort", required = false) String sort,
                                                                @RequestParam(value = "cooperationStatus",required = false) String cooperationStatus,
                                                                @RequestParam(value = "communication_order",required = false) Integer communication_order,
                                                                @RequestParam(value = "createTimeStart",required = false) String createTimeStart,
                                                                @RequestParam(value = "createTimeEnd",required = false) String createTimeEnd,
                                                                @RequestHeader("Authorization") String token) {


        CronusDto<QueryResult<CustomerListDTO>> cronusDto = new CronusDto();
        //获取当前用户登录的id
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            QueryResult queryResult = customerInfoService.customerListNew(userId, customerName, telephonenumber,
                    utmSource, ownUserName, customerSource, circle, companyId, page, size, remain, level, token,
                    orderField,sort,cooperationStatus,communication_order,createTimeStart,createTimeEnd);
            cronusDto.setData(queryResult);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取列表信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "手动添加客户", notes = "手动添加客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerDTO", value = "", required = true, paramType = "body", dataType = "CustomerDTO")
    })
    @RequestMapping(value = "/addCrmCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto addCrmCustomer(@RequestBody CustomerDTO customerDTO, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto = customerInfoService.addCustomer(customerDTO, token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取列表信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "根据手机号匹配客户", notes = "根据手机号匹配客户")
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
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "根据客户id查找客户信息", notes = "根据客户id查找客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerids", value = "1,2,3", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "customerName", value = "XXXXXX", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/findCustomerListByIds", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerListByIds(@RequestParam(required = false) String customerids, @RequestParam(required = false) String customerName) {
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto = customerInfoService.findCustomerListByIds(customerids, customerName);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->fingBytelephone获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "编辑客户信息", notes = "编辑客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/editCustomer", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CustomerDTO> editCustomer(@RequestParam Integer customerId, @RequestHeader("Authorization") String token) {
        CronusDto<CustomerDTO> cronusDto = new CronusDto();
        try {
            if (customerId == null || "".equals(customerId)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            cronusDto = customerInfoService.editCustomer(customerId, token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "根据属性获取客户信息", notes = "根据属性获取客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "telephone", value = "手机号", required = false, paramType = "query", dataType = "string"),
    })
    @RequestMapping(value = "/findCustomerByFeild", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CustomerDTO> findCustomerByFeild(@RequestParam(value = "customerId", required = false) Integer customerId,
                                                      @RequestParam(value = "telephone", required = false) String telephone) {
        CronusDto cronusDto = new CronusDto();
        if (StringUtils.isEmpty(customerId) && StringUtils.isEmpty(telephone)) {

            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        try {
            cronusDto = customerInfoService.findCustomerByFeild(customerId, telephone);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "提交编辑客户信息", notes = "提交编辑客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerDTO", value = "", required = true, paramType = "body", dataType = "CustomerDTO")
    })
    @RequestMapping(value = "/editCustomerOk", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCustomerOk(@RequestBody CustomerDTO customerDTO, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        try {
            PHPLoginDto resultDto = thorUcService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
            String[] authority = resultDto.getAuthority();
            if (authority.length > 0) {
                List<String> authList = Arrays.asList(authority);
                if (authList.contains(CommonConst.EDIT_CUSTOMER_URL)) {
                    cronusDto.setResult(CommonMessage.ADD_FAIL.getCode());
                    cronusDto.setMessage(CommonConst.NO_AUTHORIZE);
                    return cronusDto;
                }
            }
            UserInfoDTO userInfoDTO = resultDto.getUser_info();
            cronusDto = customerInfoService.editCustomerOk(customerDTO, userInfoDTO, token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->editCustomerOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "根据客户类型获取客户信息", notes = "根据客户类型获取客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerType", value = "客户类型", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/findCustomerByType", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerByType(@RequestParam String customerType) {
        CronusDto cronusDto = new CronusDto();
        try {
            List<Integer> list = customerInfoService.findCustomerByType(customerType);
            if (list != null && list.size() > 0) {
                cronusDto.setData(list);
                cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
                cronusDto.setResult(ResultResource.CODE_SUCCESS);
            }
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "改为为协议状态", notes = "改为为协议状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'customer_id':'客户id','user_id':'操作人id'}", required = true, paramType = "body", dataType = "JSONObject")
    })
    @RequestMapping(value = "/editCustomerType", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCustomerType(@RequestBody JSONObject jsonObject) {
        CronusDto cronusDto = new CronusDto();
        Integer customer_id = jsonObject.getInteger("customer_id");
        Integer user_id = jsonObject.getInteger("user_id");
        if (customer_id == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (user_id == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto = customerInfoService.editCustomerType(customer_id, user_id);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->editCustomerOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "协议状态TO成交状态", notes = "协议状态TO成交状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'customer_id':'客户id','user_id':'操作人id'}", required = true, paramType = "body", dataType = "JSONObject")
    })
    @RequestMapping(value = "/editCustomerTypeTOCon", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCustomerTypeTOCon(@RequestBody JSONObject jsonObject) {
        CronusDto cronusDto = new CronusDto();
        Integer customer_id = jsonObject.getInteger("customer_id");
        Integer user_id = jsonObject.getInteger("user_id");
        if (customer_id == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (user_id == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto = customerInfoService.editCustomerTypeTOConversion(customer_id, user_id);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->editCustomerOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "判断客户的附件上传情况", notes = "判断客户的附件上传情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "productType", value = "产品类型1：信用，2：抵押，3：赎楼", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/validDocumentToContract", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto validDocumentToContract(@RequestParam Integer customerId, @RequestParam Integer productType, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        //教研参数
        if (customerId == null || customerId == 0) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (productType == null || productType == 0) {
            throw new CronusException(CronusException.Type.CRM_VALIDAOCUMENRTOCON_ERROR);
        }
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            cronusDto = documentService.validDocumentToContract(customerId, productType, token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "根据城市获取所有的客户的ids", notes = "根据城市获取所有的客户的ids")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市名", required = false, paramType = "query", dataType = "string"),

    })
    @RequestMapping(value = "/findCustomerByCity", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerByCity(@RequestParam(required = false) String city) {
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto = customerInfoService.findCustomerByCity(city);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->findCustomerByCity根据城市获取所有的客户的ids查询失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "获取其他城市的客户ids", notes = "获取其他城市的客户ids")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "citys", value = "城市名,逗号隔开", required = true, paramType = "query", dataType = "string"),

    })
    @RequestMapping(value = "/findCustomerByOtherCity", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerByOtherCity(@RequestParam(required = true) String citys) {
        CronusDto cronusDto = new CronusDto();
        if (StringUtils.isEmpty(citys)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto = customerInfoService.findCustomerByOtherCity(citys);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->findCustomerByCity获取其他城市的客户ids查询失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "获取分配客户列表", notes = "获取分配客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道 自申请客户传入'自申请'", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源 ", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机 ", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "autostatus", value = "1 新分配客户", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "type", value = "1已沟通客户，2 ：else", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/AllocationCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> AllocationCustomerList(@RequestParam(value = "customerName", required = false) String customerName,
                                                                          @RequestParam(value = "utmSource", required = false) String utmSource,
                                                                          @RequestParam(value = "customerSource", required = false) String customerSource,
                                                                          @RequestParam(value = "telephonenumber", required = false) String telephonenumber,
                                                                          @RequestParam(value = "autostatus", required = false) Integer autostatus,
                                                                          @RequestParam(value = "type", required = true) Integer type,
                                                                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                                          @RequestHeader("Authorization") String token) {


        CronusDto<QueryResult<CustomerListDTO>> cronusDto = new CronusDto();
        if (type == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            QueryResult<CustomerListDTO> queryResult = customerInfoService.allocationCustomerList(customerName, utmSource, customerSource, autostatus, page, size, type, telephonenumber, token);
            cronusDto.setData(queryResult);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取列表信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }
    @ApiOperation(value = "获取分配客户列表(新分配客户增加排序)", notes = "获取分配客户列表(新分配客户增加排序)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道 自申请客户传入'自申请'", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源 ", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机 ", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "autostatus", value = "1 新分配客户", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "type", value = "1已沟通客户，2 ：else", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "orderField", value = "排序字段(receive_time,create_time,last_update_time)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sort", value = "asc ,desc", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "createTimeStart",value = "创建时间开始日期",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "createTimeEnd",value = "创建时间结束日期",required = false,paramType = "query",dataType = "string")
    })
    @RequestMapping(value = "/AllocationCustomerListNew", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> AllocationCustomerListNew(@RequestParam(value = "customerName", required = false) String customerName,
                                                                          @RequestParam(value = "utmSource", required = false) String utmSource,
                                                                          @RequestParam(value = "customerSource", required = false) String customerSource,
                                                                          @RequestParam(value = "telephonenumber", required = false) String telephonenumber,
                                                                          @RequestParam(value = "autostatus", required = false) Integer autostatus,
                                                                          @RequestParam(value = "type", required = true) Integer type,
                                                                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                                          @RequestParam(value = "orderField", required = false) String orderField,
                                                                          @RequestParam(value = "sort", required = false) String sort,
                                                                          @RequestParam(value = "createTimeStart",required = false) String createTimeStart,
                                                                          @RequestParam(value = "createTimeEnd",required = false) String createTimeEnd,
                                                                          @RequestHeader("Authorization") String token
                                                                           ) {


        CronusDto<QueryResult<CustomerListDTO>> cronusDto = new CronusDto();
        if (type == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            QueryResult<CustomerListDTO> queryResult = customerInfoService.allocationCustomerListNew(customerName, utmSource, customerSource, autostatus, page, size,
                    type, telephonenumber,orderField,sort,token,createTimeStart,createTimeEnd);
            cronusDto.setData(queryResult);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取列表信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "保留客户", notes = "保留客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'customerId':'customerId'}", required = true, paramType = "body", dataType = "JSONObject"),

    })
    @RequestMapping(value = "/keepCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto keepLoan(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        logger.warn("开始保留客户-------》");
        CronusDto theaApiDTO = new CronusDto();
        String token = request.getHeader("Authorization");
        Integer customerId = jsonObject.getInteger("customerId");
        /*PHPLoginDto resultDto = thorUcService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.KEEP_LOAN_URL)){
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }*/
        UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        logger.warn("查询UC系统-------》");
        CustomerInfo customerInfo = new CustomerInfo();
        try {
            customerInfo.setRemain(CommonConst.REMAIN_STATUS_YES);
            customerInfo.setCustomerType(CommonConst.CUSTOMER_TYPE_MIND);
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                customerInfo.setOwnUserId(Integer.parseInt(userInfoDTO.getUser_id()));
            }
            Integer customerCount  = customerInfoService.getKeepCount(userInfoDTO);
            logger.warn("获取当期保留数目-------》");
            String maxCount = theaClientService.findValueByName(token, CommonConst.KEEPPARAMS);
            if (customerCount >= Integer.valueOf(maxCount)) {
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage("您保留的客户已满，不能保留");
                return theaApiDTO;
            }
            customerInfo = customerInfoService.findCustomerById(customerId);
            logger.warn("查询客户信息-------》");
            if (customerInfo == null) {
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage(CronusException.Type.CRM_CUSTOMEINFO_ERROR.toString());
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id()) && customerInfo.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id())) {
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
            //刚分配未沟通的客户不能保留
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                if (customerInfo.getRemain() == CommonConst.REMAIN_STATUS_YES) {
                    theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                    theaApiDTO.setMessage("该客户已保留，不能重复保留");
                    return theaApiDTO;
                }
            }

            //查最后一次的分配记录
            boolean flag = allocateLogService.newestAllocateLog(customerId);
            logger.warn("查最后一次的分配记录-------》");
            if ((flag == true && customerInfo.getConfirm() == 3) || customerInfo.getCommunicateTime() == null) {
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage("刚分配的无效和未沟通客户不能保留");
                return theaApiDTO;
            }
            if (customerInfo.getConfirm() == 1) {
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage("刚沟通未确认的客户不能保留");
                return theaApiDTO;
            }
            theaApiDTO = customerInfoService.keepCustomer(customerId, userInfoDTO, token);
        } catch (Exception e) {
            logger.error("-------------->keepLoan保留失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.KEEP_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }

    @ApiOperation(value = "取消保留客户", notes = "取消保留客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'customerId':'customerId'}", required = true, paramType = "body", dataType = "JSONObject"),
    })
    @RequestMapping(value = "/cancelkeepCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto cancelkeepCustomer(@RequestBody JSONObject jsonObject, @RequestHeader("Authorization") String token) {
        CronusDto theaApiDTO = new CronusDto();
        Integer customerId = jsonObject.getInteger("customerId");
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        String[] authority = resultDto.getAuthority();
      /*  if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.CANCEL_LOAN_URL)){
                theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }*/
        UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);

        CustomerInfo customerInfo = null;
        try {
            customerInfo = customerInfoService.findCustomerById(customerId);
            if (customerInfo == null) {
                theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
                theaApiDTO.setMessage(CronusException.Type.CRM_CUSTOMEINFO_ERROR.toString());
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id()) && customerInfo.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id())) {
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
            boolean updateResult = customerInfoService.cancelkeepCustomer(customerId, userInfoDTO, token);
            if (updateResult == true) {
                theaApiDTO.setResult(CommonMessage.CANCEL_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.CANCEL_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->cancelLoan取消保留失败");
                theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.CANCEL_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        } catch (Exception e) {
            logger.error("-------------->cancelLoan取消保留失败", e);
            theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.CANCEL_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }

    @ApiOperation(value = "批量扔回公盘", notes = "批量扔回公盘")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'ids':'1,3,4'}", required = true, paramType = "body", dataType = "JSONObject"),

    })

    @RequestMapping(value = "/briefCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto briefCustomer(@RequestBody JSONObject jsonObject,
                                   @RequestHeader("Authorization") String token) {

        CronusDto cronusDto = new CronusDto();
        //校验权限
        String ids = jsonObject.getString("ids");
        try {
            cronusDto = customerInfoService.removeCustomer(ids, token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->removeCustomer批量扔回公盘操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

    }

    @ApiOperation(value = "离职员工批量转移", notes = "离职员工批量转移")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/removeCustomerAll", method = RequestMethod.POST)
    @ResponseBody

    public CronusDto removeCustomerAll(@RequestBody RemoveDTO removeDTO,
                                       @RequestHeader("Authorization") String token) {

        CronusDto cronusDto = new CronusDto();
        //校验权限
//        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        SimpleUserInfoDTO systemUserInfo = thorUcService.getSystemUserInfo(token, removeDTO.getEmpId());
//        String[] authority = resultDto.getAuthority();
//        if (authority.length > 0) {
//            List<String> authList = Arrays.asList(authority);
//            if (authList.contains(CommonConst.REMOVE_CUSTOMER)) {
//                cronusDto.setResult(ResultResource.CODE_OTHER_ERROR);
//                cronusDto.setMessage(CommonConst.NO_AUTHORIZE);
//                return cronusDto;
//            }
//        }
//        UserInfoDTO userInfoDTO = resultDto.getUser_info();

        try {
            cronusDto = customerInfoService.removeCustomerAll(removeDTO, systemUserInfo, token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->removeCustomer离职员工批量转移操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

    }

    @ApiOperation(value = "客户系统手动添加客户", notes = "客户系统手动添加客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerDTO", value = "", required = true, paramType = "body", dataType = "AddCustomerDTO")
    })
    @RequestMapping(value = "/addCRMCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto addCRMCustomer(@RequestBody AddCustomerDTO customerDTO, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        //校验权限 Customer/add
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        String[] authority = resultDto.getAuthority();
        if (authority.length > 0) {
            List<String> authList = Arrays.asList(authority);
            if (authList.contains(CommonConst.ADD_CUSTOMER_URL)) {
                cronusDto.setResult(CommonMessage.ADD_FAIL.getCode());
                cronusDto.setMessage(CommonConst.NO_AUTHORIZE);
                return cronusDto;
            }
        }
        UserInfoDTO userInfoDTO = resultDto.getUser_info();
        try {
            cronusDto = customerInfoService.addCRMCustomer(customerDTO, userInfoDTO, token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->addCRMCustomer新增客户失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "获取当前登录客户的所有总公司", notes = "获取当前登录客户的所有总公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/getAllCompany", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<SubCompanyDto>> getAllCompany(@RequestHeader("Authorization") String token) {
        CronusDto<List<SubCompanyDto>> cronusDto = new CronusDto();
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            List<SubCompanyDto> companyDtos = customerInfoService.getAllCompany(token, userId);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setData(companyDtos);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取列表信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "获取客户列表", notes = "获取客户列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", paramType = "query", dataType = "string"),
    })
    @RequestMapping(value = "/customerListToCheck", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<Integer>> customerListToCheck(@RequestParam(value = "customerName", required = false) String customerName,
                                                        @RequestParam(value = "utmSource", required = false) String utmSource,
                                                        @RequestParam(value = "city", required = false) String city,
                                                        @RequestHeader("Authorization") String token) {


        CronusDto<List<Integer>> cronusDto = new CronusDto();
        //获取当前用户登录的id
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (StringUtils.isEmpty(customerName) && StringUtils.isEmpty(utmSource) && StringUtils.isEmpty(city)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            List<Integer> resultList = customerInfoService.customerListToCheck(customerName, utmSource, city, token);
            cronusDto.setData(resultList);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取列表信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "获取所有来源", notes = "获取所有来源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/getAllCustomerSource", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<String>> getAllCustomerSource(@RequestHeader("Authorization") String token) {


        CronusDto<List<String>> cronusDto = new CronusDto();
        //获取当前用户登录的id
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            List<String> resultList = customerInfoService.getAllCustomerSource();
            cronusDto.setData(resultList);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->获取所有来源失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "C端提交编辑客户信息", notes = "C端提交编辑客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerDTO", value = "customerDTO", required = true, paramType = "body", dataType = "CustomerDTO")
    })
    @RequestMapping(value = "/editClientCustomerOk", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editClientCustomerOk(@RequestBody CustomerDTO customerDTO, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto = customerInfoService.editClientCustomerOk(customerDTO, token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->editCustomerOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "ezphone获取手机号", notes = "ezphone获取手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "customerId", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/getTelePhone", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getTelePhone(@RequestParam Integer customerId, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        try {
            String result = customerInfoService.getTelePhone(customerId, token);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            cronusDto.setData(result);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->editCustomerOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "根据客户id获取沟通状态", notes = "根据客户id获取沟通状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "customerId", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/getCommunByCustomerId", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<ScrmbDTO> getCommunByCustomerId(@RequestParam Integer customerId, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        ScrmbDTO scrmbDTO = new ScrmbDTO();
        try {
            scrmbDTO = customerInfoService.getCommunByCustomerId(customerId);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            cronusDto.setData(scrmbDTO);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->editCustomerOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "根据多个客户id获取沟通状态", notes = "根据多个客户id获取沟通状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerIds", value = "customerIds", required = true, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/getCommunByCustomerIds", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<ScrmbDTO> getCommunByCustomerIds(@RequestParam String customerIds, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        List<ScrmbDTO> scrmbDTO = new ArrayList<>();
        try {
            scrmbDTO = customerInfoService.getCommunByCustomerIds(customerIds);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            cronusDto.setData(scrmbDTO);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->editCustomerOk提交失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "手动添加交易", notes = "手动添加交易")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", dataType = "string"),

    })
    @RequestMapping(value = "/addLoan", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto addLoan(@RequestBody LoanDTO6 loanDTO, HttpServletRequest request) {
        logger.warn("手动添加交易数据-------》" + loanDTO.toString());
        CronusDto theaApiDTO = new CronusDto();
        String token = request.getHeader("Authorization");
        BigDecimal loanAmount = loanDTO.getLoanAmount();
        if (loanAmount == null){
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage("拟贷款金额不能为空");
            return theaApiDTO;
        }
        if (loanAmount.compareTo(BigDecimal.ZERO) == -1){
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage("拟贷款金额不能为负数");
            return theaApiDTO;
        }
        Integer customerId = loanDTO.getCustomerId();
        if (customerId == null){
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage("客户id不能为空");
            return theaApiDTO;
        }
        String customerName = loanDTO.getCustomerName();
        if (StringUtils.isEmpty(customerName)){
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage("客户姓名不能为空");
            return theaApiDTO;
        }
        String phone = loanDTO.getTelephonenumber();
        if (StringUtils.isEmpty(phone)){
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage("电话不能为空");
            return theaApiDTO;
        }
        Integer ownUserId = loanDTO.getOwnUserId();
        if (ownUserId == null){
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage("负责人id不能为空");
            return theaApiDTO;
        }

        UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        if (userInfoDTO != null ){
            if (userInfoDTO.getUser_id() != null && !userInfoDTO.getUser_id().toString().equals(loanDTO.getOwnUserId().toString())){
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage("没有权限");
                return theaApiDTO;
            }
        }
        logger.warn("查询UC系统-------》");
        try {
            theaApiDTO = customerInfoService.addLoan(loanDTO, userInfoDTO, token);
        } catch (Exception e) {
            logger.error("-------------->addLoan手动添加交易失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }


    @ApiOperation(value="今日统计", notes="今日统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")
    })
    @RequestMapping(value = "/getTodayCount", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CustomerCountDTO> getTodayCount(@RequestHeader("Authorization")String token){
        CronusDto<CustomerCountDTO> cronusDto = new CronusDto();
        //校验权限
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null){
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        }

        try {
            cronusDto = customerInfoService.getTodayCount(token,userId);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->todayCount失败",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    //根据手机号码查询客户id，来源及创建时间
    @ApiOperation(value = "查询用户一些信息", notes = "根据手机号码查询客户id，来源及创建时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/queryCustomerInfo", method = RequestMethod.GET)
    @ResponseBody
    public WalletApiDTO<CustomerPartDTO> queryCustomerInfo(@RequestParam(value = "phone",required = true) String phone){
        WalletApiDTO<CustomerPartDTO> theaApiDTO = new WalletApiDTO<>();
        if (StringUtils.isEmpty(phone)){
            theaApiDTO.setCode(Integer.valueOf(CronusException.Type.CRM_PARAMS_ERROR.getStatus()));
            theaApiDTO.setMsg(CronusException.Type.CRM_PARAMS_ERROR.getError());
            return theaApiDTO;
        }

        theaApiDTO.setData(customerInfoService.selectCustomerDTOByPhone(phone));
        theaApiDTO.setMsg(CommonMessage.SUCCESS.getCodeDesc());
        return theaApiDTO;
    }

    //根据客户id，查询电话号码及来源
    @ApiOperation(value = "查询用户信息", notes = "根据客户id，查询电话号码及来源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "客户id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/queryCustomerBasic", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CustomerBasicDTO> queryCustomerBasic(@RequestParam(value = "id",required = true) String id){
        CronusDto<CustomerBasicDTO> theaApiDTO = new CronusDto<>();
        if (StringUtils.isEmpty(id)){
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc()+",客户id不能为空");
            return theaApiDTO;
        }
        theaApiDTO.setData(customerInfoService.selectCustomerById(Integer.valueOf(id)));
        theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
        theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        return theaApiDTO;
    }



    @ApiOperation(value = "查询用户信息", notes = "根据客户id，查询电话号码及来源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),

    })
    @RequestMapping(value = "/querykdsgn", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto querykdsgn(){
        String message = "尊敬的客户您好，因公司人员调整，房金所新的融资经理张三，18701780932，将继续为您服务，感谢您对房金所的支持与信赖。";
//                    Integer count = smsService.sendCommunication(customerInfo.getTelephonenumber(), message);
        Integer count = smsService.sendCommunication("XEDU5hxi9bwBoyJ6ynon5w==", message);

        return  null;
    }

    @ApiOperation(value = "判断渠道交易的附件上传情况", notes = "判断渠道交易的附件上传情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "serviceContractId", value = "协议id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "productType", value = "产品类型1：信用，2：抵押，3：赎楼", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/validType", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto validType(@RequestParam Integer serviceContractId, @RequestParam Integer productType, @RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        if (serviceContractId == null || serviceContractId == 0) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (productType == null || productType == 0) {
            throw new CronusException(CronusException.Type.CRM_VALIDAOCUMENRTOCON_ERROR);
        }
        try {
            cronusDto = documentService.validType(serviceContractId, productType, token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->判断渠道交易的附件上传情况失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "查询用户信息", notes = "根据客户id，查询电话号码及来源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "业务员手机号", required = true, paramType = "query", dataType = "string"),
    })
    @RequestMapping(value = "/queryByPhone", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<CrmPushCustomerDTO>> queryByPhone(HttpServletRequest request,String phone){
        CronusDto cronusDto = new CronusDto();
        String token = request.getHeader("Authorization");
        ThorApiDTO<UserModelDTO> result = thorService.getUserInfoByMobile(token,phone);
        UserModelDTO userModel = result.getData();
        List<CrmPushCustomerDTO> crmPushCustomerDTOList = new ArrayList<>();
        try{
            Integer userId = null;
            if (!StringUtils.isEmpty(userModel.getUser_id())) {
                userId = Integer.parseInt(userModel.getUser_id());
                List<CustomerInfo> customerInfoList = customerInfoService.selectByOwnId(userId);
                for (CustomerInfo customerInfo:customerInfoList){
                    CrmPushCustomerDTO crmPushCustomerDTO = customerInfoService.copyProperty(customerInfo);
                    crmPushCustomerDTOList.add(crmPushCustomerDTO);
                }
            }
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
        }catch (Exception e) {
            logger.error("--------------->queryByPhone查询失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        cronusDto.setData(crmPushCustomerDTOList);
        return cronusDto;
    }

    @ApiOperation(value = "B端客户列表", notes = "B端客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "电话号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "客户状态 (意向客户 协议客户 成交客户)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "houseStatus", value = "房产情况（有或者无）", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "remain", value = "是否保留  0不保留1保留", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "loanAmount", value = "交易金额（0:0万，1:0-5万，2:5-10万，3:10-50万，4:50-100万，5:100-500万，6:500万以上）",required = false, paramType = "query",  dataType = "int"),
            @ApiImplicitParam(name="cooperationStatus",value = "跟进状态(暂未接通，无意向，有意向待跟踪，资质差无法操作,空号，外地，同业，内部员工，其他)",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示多少条记录", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "ownCustomer", value = "是否查看自己的客户(0-自己的客户,其他-全部客户)", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/busniess/customerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerDTO2>> bCustomerList(
                                                        @RequestParam(value = "customerName", required = false) String customerName,
                                                        @RequestParam(value = "telephonenumber", required = false) String telephonenumber,
                                                        @RequestParam(value = "level", required = false) String level,
                                                        @RequestParam(value = "houseStatus", required = false) String houseStatus,
                                                        @RequestParam(value = "remain", required = false) Integer remain,
                                                        @RequestParam(value = "loanAmount", required = false) Integer loanAmount,
                                                        @RequestParam(value = "cooperationStatus",required = false) String cooperationStatus,
                                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                        @RequestParam(value = "ownCustomer",required = false) Integer ownCustomer,
                                                        @RequestHeader("Authorization") String token) {


        CronusDto<QueryResult<CustomerDTO2>> cronusDto = new CronusDto();
        //获取当前用户登录的id
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            QueryResult queryResult = customerInfoService.bCustomerList(userId, customerName, telephonenumber,page, size, remain, level, token,
                    cooperationStatus,houseStatus,loanAmount,ownCustomer);
            cronusDto.setData(queryResult);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->customerList获取b端列表信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException) e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value = "保留客户", notes = "保留客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", dataType = "string"),
    })
    @RequestMapping(value = "/busniess/keepCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto bKeepLoan(@RequestBody CustomerDTO3 customerDTO, BindingResult result, HttpServletRequest request) {
        logger.warn("b端保留客户:" + customerDTO.toString());
        CronusDto theaApiDTO = new CronusDto();
        String token = request.getHeader("Authorization");
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        Integer customerId = customerDTO.getCustomerId();
        UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        CustomerInfo customerInfo = new CustomerInfo();
        try {
            customerInfo.setRemain(CommonConst.REMAIN_STATUS_YES);
            customerInfo.setCustomerType(CommonConst.CUSTOMER_TYPE_MIND);
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                customerInfo.setOwnUserId(Integer.parseInt(userInfoDTO.getUser_id()));
            }
            Integer customerCount  = customerInfoService.getKeepCount(userInfoDTO);
            logger.warn("获取当期保留数目-------》");
            String maxCount = theaClientService.findValueByName(token, CommonConst.KEEPPARAMS);
            if (customerCount >= Integer.valueOf(maxCount)) {
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage("您保留的客户已满，不能保留");
                return theaApiDTO;
            }
            customerInfo = customerInfoService.findCustomerById(customerId);
            logger.warn("查询客户信息-------》");
            if (customerInfo == null) {
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage(CronusException.Type.CRM_CUSTOMEINFO_ERROR.toString());
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id()) && customerInfo.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id())) {
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
            //刚分配未沟通的客户不能保留
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                if (customerInfo.getRemain() == CommonConst.REMAIN_STATUS_YES) {
                    theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                    theaApiDTO.setMessage("该客户已保留，不能重复保留");
                    return theaApiDTO;
                }
            }
            //查最后一次的分配记录
            boolean flag = allocateLogService.newestAllocateLog(customerId);
            logger.warn("查最后一次的分配记录-------》");
            if ((flag == true && customerInfo.getConfirm() == 3) || customerInfo.getCommunicateTime() == null) {
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage("刚分配的无效和未沟通客户不能保留");
                return theaApiDTO;
            }
            if (customerInfo.getConfirm() == 1) {
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage("刚沟通未确认的客户不能保留");
                return theaApiDTO;
            }
            theaApiDTO = customerInfoService.keepCustomer(customerId, userInfoDTO, token);
        } catch (Exception e) {
            logger.error("b端keepLoan保留失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.KEEP_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }

    @ApiOperation(value = "b端取消保留客户", notes = "b端取消保留客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", dataType = "string"),
    })
    @RequestMapping(value = "/busniess/cancelKeepCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto bCancelKeepCustomer(@RequestBody CustomerDTO3 customerDTO, BindingResult result, @RequestHeader("Authorization") String token) {
        CronusDto theaApiDTO = new CronusDto();
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        Integer customerId = customerDTO.getCustomerId();
        UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        CustomerInfo customerInfo = null;
        try {
            customerInfo = customerInfoService.findCustomerById(customerId);
            if (customerInfo == null) {
                theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
                theaApiDTO.setMessage(CronusException.Type.CRM_CUSTOMEINFO_ERROR.toString());
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id()) && customerInfo.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id())) {
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
            boolean updateResult = customerInfoService.cancelkeepCustomer(customerId, userInfoDTO, token);
            if (updateResult == true) {
                theaApiDTO.setResult(CommonMessage.CANCEL_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.CANCEL_SUCCESS.getCodeDesc());
            } else {
                logger.error("b端cancelLoan取消保留失败");
                theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.CANCEL_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        } catch (Exception e) {
            logger.error("b端cancelLoan取消保留失败", e);
            theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.CANCEL_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }
}
