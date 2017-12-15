package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.dto.cronus.RemoveDTO;
import com.fjs.cronus.dto.thea.AllocateDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.CommunicationLogService;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.DocumentService;
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
import javax.validation.Valid;
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
    private  static  final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    DocumentService documentService;
    @Autowired
    UcService thorUcService;
    @Autowired
    CommunicationLogService communicationLogService;
    @ApiOperation(value="获取客户列表", notes="获取客户列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "电话号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道", paramType = "query",  dataType = "int"),
            @ApiImplicitParam(name = "ownUserName", value = "负责人", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "circle", value = "客户周期 1新分配 2已领取 3待见面 4已签约", required = false, paramType = "query",  dataType = "int"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = false, paramType = "query",  dataType = "int"),
            @ApiImplicitParam(name = "remain", value = "是否保留  0不保留1保留2已签合同", required = false, paramType = "query",  dataType = "int"),
            @ApiImplicitParam(name = "level", value = "客户状态 意向客户 协议客户 成交客户", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/customerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> customerList(@RequestParam(value = "customerName",required = false) String customerName,
                                  @RequestParam(value = "telephonenumber",required = false) String telephonenumber,
                                  @RequestParam(value = "utmSource",required = false) String utmSource,
                                  @RequestParam(value = "ownUserName",required = false) String ownUserName,
                                  @RequestParam(value = "customerSource",required = false) String customerSource,
                                  @RequestParam(value = "circle",required = false) Integer circle,
                                  @RequestParam(value = "companyId",required = false) Integer companyId,
                                  @RequestParam(value = "remain",required = false) Integer remain,
                                  @RequestParam(value = "level",required = false) String level,
                                  @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                  @RequestParam(value = "size",required = false,defaultValue = "10") Integer size,
                                  @RequestHeader("Authorization")String token) {


        CronusDto<QueryResult<CustomerListDTO>> cronusDto = new CronusDto();
        //获取当前用户登录的id
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            QueryResult queryResult = customerInfoService.customerList(userId,customerName,telephonenumber,
                    utmSource, ownUserName, customerSource, circle,companyId,page,size, remain,level,token);
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
            @ApiImplicitParam(name = "customerids", value = "1,2,3", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "customerName", value = "XXXXXX", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/findCustomerListByIds", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerListByIds(@RequestParam(required = false) String customerids,@RequestParam(required = false) String customerName ) {
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto = customerInfoService.findCustomerListByIds(customerids,customerName);
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
    public CronusDto<CustomerDTO> editCustomer(@RequestParam Integer customerId) {
        CronusDto<CustomerDTO> cronusDto = new CronusDto();
        try {
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
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/findCustomerByFeild", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerByFeild(@RequestParam Integer customerId) {
        CronusDto cronusDto = new CronusDto();
        try {
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
    @ApiOperation(value="根据客户类型获取客户信息", notes="根据客户类型获取客户信息")
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
            if (list != null && list.size() > 0){
                cronusDto.setData(list);
                cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
                cronusDto.setResult(ResultResource.CODE_SUCCESS);
            }
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

    @ApiOperation(value="改为为协议状态", notes="改为为协议状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "JSONObject", value = "{'customer_id':'客户id','user_id':'操作人id','customerTypeSta':'','customerTypeEnd':''}", required = true, paramType = "body", dataType = "JSONObject")
    })
    @RequestMapping(value = "/editCustomerType", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCustomerType(@RequestBody JSONObject jsonObject) {
        CronusDto cronusDto = new CronusDto();
        Integer customer_id = jsonObject.getInteger("customer_id");
        Integer user_id = jsonObject.getInteger("user_id");
        String customerTypeSta = jsonObject.getString("customerTypeSta");
        String customerTypeEnd = jsonObject.getString("customerTypeEnd");
        if (customer_id == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (customerTypeSta == null || "".equals(customerTypeSta)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (customerTypeEnd == null || "".equals(customerTypeEnd)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }

        try {
            cronusDto = customerInfoService.editCustomerType(customer_id,user_id,customerTypeSta,customerTypeEnd);
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
    @ApiOperation(value="协议状态TO成交状态", notes="协议状态TO成交状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "JSONObject", value = "{'customer_id':'客户id','user_id':'操作人id','customerTypeSta':'','customerTypeEnd':''}", required = true, paramType = "body", dataType = "JSONObject")
    })
    @RequestMapping(value = "/editCustomerTypeTOCon", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCustomerTypeTOCon(@RequestBody JSONObject jsonObject) {
        CronusDto cronusDto = new CronusDto();
        Integer customer_id = jsonObject.getInteger("customer_id");
        Integer user_id = jsonObject.getInteger("user_id");
        String customerTypeSta = jsonObject.getString("customerTypeSta");
        String customerTypeEnd = jsonObject.getString("customerTypeEnd");
        if (customer_id == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (customerTypeSta == null || "".equals(customerTypeSta)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (customerTypeEnd == null || "".equals(customerTypeEnd)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }

        try {
            cronusDto = customerInfoService.editCustomerTypeTOConversion(customer_id,user_id);
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
    @ApiOperation(value="判断客户的附件上传情况", notes="判断客户的附件上传情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "productType", value = "产品类型1：信用，2：抵押，3：赎楼", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/validDocumentToContract", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto validDocumentToContract(@RequestParam Integer customerId,@RequestParam Integer productType,@RequestHeader("Authorization") String token) {
        CronusDto cronusDto = new CronusDto();
        //教研参数
        if (customerId == null || customerId == 0 ){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (productType == null || productType == 0 ){
            throw new CronusException(CronusException.Type.CRM_VALIDAOCUMENRTOCON_ERROR);
        }
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            cronusDto = documentService.validDocumentToContract(customerId,productType,token);
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

    @ApiOperation(value="根据城市获取所有的客户的ids", notes="根据城市获取所有的客户的ids")
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
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value="获取其他城市的客户ids", notes="获取其他城市的客户ids")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "citys", value = "城市名,逗号隔开", required = true, paramType = "query", dataType = "string"),

    })
    @RequestMapping(value = "/findCustomerByOtherCity", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerByOtherCity(@RequestParam(required = true) String citys) {
        CronusDto cronusDto = new CronusDto();
        if (StringUtils.isEmpty(citys)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            cronusDto = customerInfoService.findCustomerByOtherCity(citys);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->findCustomerByCity获取其他城市的客户ids查询失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value="获取分配客户列表", notes="获取分配客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道 自申请客户传入'自申请'", paramType = "query",  dataType = "int"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源 ", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "autostatus", value = "1 新分配客户", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "type", value = "1已沟通客户，2 ：else", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/AllocationCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>>  AllocationCustomerList(@RequestParam(value = "customerName",required = false) String customerName,
                                                                           @RequestParam(value = "utmSource",required = false) String utmSource,
                                                                           @RequestParam(value = "customerSource",required = false) String customerSource,
                                                                           @RequestParam(value = "autostatus",required = false) Integer autostatus,
                                                                           @RequestParam(value = "type",required = true) Integer type,
                                                                           @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                                                           @RequestParam(value = "size",required = false,defaultValue = "10") Integer size,
                                                                           @RequestHeader("Authorization")String token) {


        CronusDto<QueryResult<CustomerListDTO>> cronusDto = new CronusDto();
        //获取当前用户登录的id
      /*  Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }*/
      if (type == null){
          throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
      }
        try {
            QueryResult<CustomerListDTO> queryResult  = customerInfoService.allocationCustomerList(customerName,utmSource,customerSource,autostatus,page,size,type);
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

    @ApiOperation(value="保留客户", notes="保留客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'customerId':'customerId'}", required = true, paramType = "body", dataType = "JSONObject"),

    })
    @RequestMapping(value = "/keepCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto keepLoan(@RequestBody JSONObject jsonObject, HttpServletRequest request){
        CronusDto  theaApiDTO=new CronusDto ();
        String token=request.getHeader("Authorization");
        Integer customerId = jsonObject.getInteger("customerId");
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.KEEP_LOAN_URL)){
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token,CommonConst.SYSTEMNAME);
        CustomerInfo customerInfo = new CustomerInfo();
        try{
            customerInfo.setRemain(CommonConst.REMAIN_STATUS_YES);
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
                customerInfo.setOwnUserId(Integer.parseInt(userInfoDTO.getUser_id()));
            }
            List<CustomerInfo> customerInfoList = customerInfoService.listByCondition(customerInfo,userInfoDTO,token,CommonConst.SYSTEMNAME);
            if (customerInfoList.size() > CommonConst.REMAIN_MAX_NUM){
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage("您保留的客户已满，不能保留");
                return theaApiDTO;
            }
            customerInfo = customerInfoService.findCustomerById(customerId);
            if (customerInfo == null){
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage(CronusException.Type.CRM_CUSTOMEINFO_ERROR.toString());
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id()) && customerInfo.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id())){
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }

            //刚分配未沟通的客户不能保留
            List<CommunicationLog> communicationLog=new ArrayList<CommunicationLog>();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
                if (customerInfo.getRemain() == CommonConst.REMAIN_STATUS_YES){
                    theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                    theaApiDTO.setMessage("该客户已保留，不能重复保留");
                    return theaApiDTO;
                }
                communicationLog=communicationLogService.listByCustomerIdAndUserId(customerInfo.getId(),Integer.parseInt(userInfoDTO.getUser_id()),token);
            }
            if (communicationLog.size() == 0){
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage("刚分配未沟通的客户不能保留");
                return theaApiDTO;
            }

            boolean updateResult = customerInfoService.keepCustomer(customerId,userInfoDTO);
            if (updateResult == true) {
                theaApiDTO.setResult(CommonMessage.KEEP_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.KEEP_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->keepLoan保留失败");
                theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.KEEP_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        }catch (Exception e){
            logger.error("-------------->keepLoan保留失败",e);
            theaApiDTO.setResult(CommonMessage.KEEP_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.KEEP_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }

    @ApiOperation(value="取消保留客户", notes="取消保留客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'customerId':'customerId'}", required = true, paramType = "body", dataType = "JSONObject"),
    })
    @RequestMapping(value = "/cancelkeepCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto cancelkeepCustomer(@RequestBody JSONObject jsonObject,@RequestHeader("Authorization")String token){
        CronusDto theaApiDTO=new CronusDto();
        Integer customerId = jsonObject.getInteger("customerId");
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.CANCEL_LOAN_URL)){
                theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token,CommonConst.SYSTEMNAME);

        CustomerInfo customerInfo=null;
        try{
            customerInfo = customerInfoService.findCustomerById(customerId);
            if (customerInfo == null){
                theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
                theaApiDTO.setMessage(CronusException.Type.CRM_CUSTOMEINFO_ERROR.toString());
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id()) && customerInfo.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id())){
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
            boolean updateResult = customerInfoService.cancelkeepCustomer(customerId,userInfoDTO);
            if (updateResult == true) {
                theaApiDTO.setResult(CommonMessage.CANCEL_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.CANCEL_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->cancelLoan取消保留失败");
                theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.CANCEL_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        }catch (Exception e){
            logger.error("-------------->cancelLoan取消保留失败",e);
            theaApiDTO.setResult(CommonMessage.CANCEL_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.CANCEL_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }

    @ApiOperation(value="批量扔回公盘", notes="批量扔回公盘")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'ids':'1,3,4'}", required = true, paramType = "body", dataType = "JSONObject"),

    })

    @RequestMapping(value = "/briefCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto briefCustomer(@RequestBody JSONObject jsonObject,
                                    @RequestHeader("Authorization")String token){

        CronusDto cronusDto = new CronusDto();
        //校验权限
        String ids = jsonObject.getString("ids");
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.REMOVE_LOAN_URL)){
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage(CommonConst.NO_AUTHORIZE);
                return cronusDto;
            }
        }
        try {
            cronusDto  = customerInfoService.removeCustomer(ids,token);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->removeCustomer批量扔回公盘操作失败",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

    }

    @ApiOperation(value="离职员工批量转移", notes="离职员工批量转移")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/removeCustomerAll", method = RequestMethod.POST)
    @ResponseBody

    public CronusDto removeCustomerAll(@RequestBody RemoveDTO removeDTO,
                                   @RequestHeader("Authorization")String token){

        CronusDto cronusDto = new CronusDto();
        //校验权限
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.REMOVE_LOAN_URL)){
                cronusDto.setResult(CommonMessage.REMOVE_FAIL.getCode());
                cronusDto.setMessage(CommonConst.NO_AUTHORIZE);
                return cronusDto;
            }
        }
        try {
            boolean result  = customerInfoService.removeCustomerAll(removeDTO,token);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->removeCustomer离职员工批量转移操作失败",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

    }


}
