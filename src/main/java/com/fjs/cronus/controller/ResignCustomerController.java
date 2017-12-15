package com.fjs.cronus.controller;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.dto.cronus.CustomerSourceDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.LookPoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by msi on 2017/12/1.
 */
@RequestMapping("/api/v1")
@Controller
@Api(description = "查看盘控制器")
public class ResignCustomerController {

    private  static  final Logger logger = LoggerFactory.getLogger(ResignCustomerController.class);
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    LookPoolService lookPoolService;
    @ApiOperation(value="获取客户来源", notes="获取客户来源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/quitCustomerSource",method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CustomerSourceDTO> quitCustomerSource(@RequestHeader("Authorization")String token){
        CronusDto resultDto = new CronusDto();
        CustomerSourceDTO customerSourceDTO = new CustomerSourceDTO();
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try{
            customerSourceDTO  = customerInfoService.quitCustomerSource(userId,token);
            resultDto.setData(customerSourceDTO);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        }catch (Exception e){
        logger.error("-------------->quitCustomerSource获取客户来源，公司",e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  resultDto;
    }


    @ApiOperation(value="离职客户列表", notes="离职客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道来源(除公盘外的必须传)", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "ownUserName", value = "负责人名称", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "客户状态 意向客户 协议客户 成交客户", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/resignCustomerList",method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> resignCustomerList(@RequestHeader("Authorization")String token,
                                                                      @RequestParam(required = false) String customerName,
                                                                      @RequestParam(required = false) String telephonenumber,
                                                                      @RequestParam(required = false) String utmSource,
                                                                      @RequestParam(required = false) String ownUserName,
                                                                      @RequestParam(required = false) String customerSource,
                                                                      @RequestParam(required = false) String level,
                                                                      @RequestParam(required = false) Integer companyId,
                                                                      @RequestParam(required = false) Integer page,
                                                                      @RequestParam(required = false) Integer size){
        CronusDto resultDto = new CronusDto();
        QueryResult<CustomerListDTO> result = new  QueryResult<CustomerListDTO>();
        try{
            result  = customerInfoService.resignCustomerList(token,customerName,telephonenumber,utmSource,ownUserName,customerSource,level,companyId,page,size);
            resultDto.setData(result);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        }catch (Exception e){
            logger.error("-------------->quitCustomerSource获取客户来源，公司",e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  resultDto;
    }

    @ApiOperation(value="三无客户盘列表", notes="三无客户盘列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道来源(除公盘外的必须传)", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "ownUserName", value = "负责人名称", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "客户状态 意向客户 协议客户 成交客户", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/unablePool",method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> unablePool(@RequestHeader("Authorization")String token,
                                                                      @RequestParam(required = false) String customerName,
                                                                      @RequestParam(required = false) String telephonenumber,
                                                                      @RequestParam(required = false) String utmSource,
                                                                      @RequestParam(required = false) String ownUserName,
                                                                      @RequestParam(required = false) String customerSource,
                                                                      @RequestParam(required = false) String level,
                                                                      @RequestParam(required = false) Integer companyId,
                                                                      @RequestParam(required = false) Integer page,
                                                                      @RequestParam(required = false) Integer size){
        CronusDto resultDto = new CronusDto();
        QueryResult<CustomerListDTO> result = new  QueryResult<CustomerListDTO>();
        try{
            result  = lookPoolService.unablePool(token,customerName,telephonenumber,utmSource,ownUserName,customerSource,level,companyId,page,size);
            resultDto.setData(result);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        }catch (Exception e){
            logger.error("-------------->unablePool三五客户盘列表公司",e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  resultDto;
    }

}
