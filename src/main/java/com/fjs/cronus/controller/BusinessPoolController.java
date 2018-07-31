package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.*;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.BusinessPoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "商机池")
@RequestMapping(value = "/api/v1")
public class BusinessPoolController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BusinessPoolService businessPoolService;


    @ApiOperation(value = "商机池列表",notes = "商机池列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "nameOrTelephone",value = "客户姓名/手机号",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "customerSource",value = "客户来源",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "utmSource",value = "媒体",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "houseStatus",value = "有房(有,无)",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "loanAmount",value = "贷款金额(0-20)",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "city",value = "所属城市",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "createTime",value = "注册开始时间",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "createTimeEnd",value = "注册结束时间",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/businessPoolList",method = RequestMethod.GET)
    public CronusDto<QueryResult<BusinessPoolDTO>> businessPoolList(@RequestHeader("Authorization")String token,
                                                                    @RequestParam(value = "nameOrTelephone",required = false) String nameOrTelephone,
                                                                    @RequestParam(value = "customerSource",required = false) String customerSource,
                                                                    @RequestParam(value = "utmSource",required = false) String utmSource,
                                                                    @RequestParam(value = "houseStatus",required = false) String houseStatus,
                                                                    @RequestParam(value = "loanAmount",required = false) String loanAmount,
                                                                    @RequestParam(value = "city",required = false) String city,
                                                                    @RequestParam(value = "createTime",required = false) String createTime,
                                                                    @RequestParam(value = "createTimeEnd",required = false) String createTimeEnd,
                                                                    @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "size",required = false,defaultValue = "10") Integer size ){

        try {
            CronusDto<QueryResult<BusinessPoolDTO>> cronusDto = new CronusDto<>();
            QueryResult<BusinessPoolDTO> queryResult = businessPoolService.businessPoolList(token,nameOrTelephone,customerSource,utmSource,houseStatus,loanAmount,city,createTime,createTimeEnd,page,size);
            cronusDto.setData(queryResult);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            return cronusDto;
        } catch (Exception e) {
            logger.error("businessPoolList 商机池列表失败 >>>>>> " + e.getMessage(),e);
            if (e instanceof CronusException){
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "媒体列表",notes = "媒体列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "utmSource",value = "媒体",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/utmSourceList",method = RequestMethod.GET)
    public CronusDto<QueryResult<MediaCustomerCountDTO>> utmSourceList(@RequestHeader("Authorization")String token,
                                                                    @RequestParam(value = "utmSource",required = false) String utmSource,
                                                                    @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "size",required = false,defaultValue = "10") Integer size ){

        try {
            CronusDto<QueryResult<MediaCustomerCountDTO>> cronusDto = new CronusDto<>();
            QueryResult<MediaCustomerCountDTO> queryResult = businessPoolService.utmSourceList(token,utmSource,page,size);
            cronusDto.setData(queryResult);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            return cronusDto;
        } catch (Exception e) {
            logger.error("utmSourceList 媒体列表失败 >>>>>> " + e.getMessage(),e);
            if (e instanceof CronusException){
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "修改媒体价格",notes = "修改媒体价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "mediaPriceDTO",value = "修改媒体价格DTO",required = true,paramType = "body",dataType = "MediaPriceDTO")
    })
    @RequestMapping(value = "/editUtmSourcePrice",method = RequestMethod.POST)
    public CronusDto editUtmSourcePrice(@RequestHeader("Authorization")String token,@RequestBody MediaPriceDTO mediaPriceDTO){

        logger.warn("editUtmSourcePrice 参数 >>>>>> " + ReflectionToStringBuilder.toString(mediaPriceDTO));
        try {
            CronusDto cronusDto = new CronusDto<>();
            businessPoolService.editUtmSourcePrice(token,mediaPriceDTO);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            return cronusDto;
        } catch (Exception e) {
            logger.error("editUtmSourcePrice 修改媒体价格失败 >>>>>> " + e.getMessage(),e);
            if (e instanceof CronusException){
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "修改客户价格",notes = "修改客户价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "mediaPriceDTO",value = "修改媒体价格DTO",required = true,paramType = "body",dataType = "MediaPriceDTO")
    })
    @RequestMapping(value = "/editCustomerPrice",method = RequestMethod.POST)
    public CronusDto editCustomerPrice(@RequestHeader("Authorization")String token,@RequestBody MediaPriceDTO mediaPriceDTO){

        logger.warn("editCustomerPrice 参数 >>>>>> " + ReflectionToStringBuilder.toString(mediaPriceDTO));
        try {
            CronusDto cronusDto = new CronusDto<>();
            businessPoolService.editCustomerPrice(token,mediaPriceDTO);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            return cronusDto;
        } catch (Exception e) {
            logger.error("editCustomerPrice 修改客户价格失败 >>>>>> " + e.getMessage(),e);
            if (e instanceof CronusException){
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "商机池领取客户",notes = "商机池领取客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject",value = "包含客户id的json对象,例:{\"customerId\":\"123\"}",required = true,paramType = "body",dataType = "JSONObject")
    })
    @RequestMapping(value = "/receiveCustomerInPool",method = RequestMethod.POST)
    public CronusDto receiveCustomerInPool(@RequestHeader("Authorization")String token,@RequestBody JSONObject jsonObject){

        logger.warn("receiveCustomerInPool 参数 >>>>>> " + jsonObject);
        try {
            CronusDto cronusDto = new CronusDto<>();
            businessPoolService.receiveCustomerInPool(token,jsonObject);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            return cronusDto;
        } catch (Exception e) {
            logger.error("receiveCustomerInPool 领取客户失败 >>>>>> " + e.getMessage(),e);
            if (e instanceof CronusException){
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "商机池所有客户来源",notes = "商机池所有客户来源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/getAllCustomerSourceList",method = RequestMethod.GET)
    public CronusDto<List<String>> getAllCustomerSourceList(@RequestHeader("Authorization")String token ){

        try {
            CronusDto<List<String>> cronusDto = new CronusDto<>();
            List<String> customerSourceList = businessPoolService.getCustomerSourceList(token);
            cronusDto.setData(customerSourceList);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            return cronusDto;
        } catch (Exception e) {
            logger.error("getAllCustomerSourceList 获取所有媒体失败 >>>>>> " + e.getMessage(),e);
            if (e instanceof CronusException){
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


    @ApiOperation(value = "商机池所有媒体",notes = "商机池所有媒体")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/getAllUtmSourceList",method = RequestMethod.GET)
    public CronusDto<List<String>> getAllUtmSourceList(@RequestHeader("Authorization")String token ){

        try {
            CronusDto<List<String>> cronusDto = new CronusDto<>();
            List<String> utmSourceList = businessPoolService.getAllUtmSourceList(token);
            cronusDto.setData(utmSourceList);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            return cronusDto;
        } catch (Exception e) {
            logger.error("getAllUtmSourceList 获取所有媒体失败 >>>>>> " + e.getMessage(),e);
            if (e instanceof CronusException){
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }


}
