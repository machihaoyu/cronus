package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
                                  @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                  @RequestParam(value = "size",required = false,defaultValue = "10") Integer size) {


        CronusDto<QueryResult<CustomerListDTO>> cronusDto = new CronusDto();
        try {
            QueryResult queryResult = customerInfoService.customerList(customerName,telephonenumber,
                    utmSource, ownUserName, customerSource, circle,companyId,page,size);
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

    @ApiOperation(value="更改客户状态", notes="更改客户状态")
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
}
