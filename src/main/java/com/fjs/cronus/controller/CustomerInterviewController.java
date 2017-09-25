package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerInterViewBaseCarHouseInsturDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.CustomerInterviewService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by msi on 2017/9/15.
 */
@Controller
@RequestMapping("/api/v1")
public class CustomerInterviewController {

    private  static  final Logger logger = LoggerFactory.getLogger(CustomerInterviewController.class);

    @Autowired
    CustomerInterviewService customerInterviewService;
    @ApiOperation(value="获取客户面谈信息列表", notes="获取客户面谈列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "loanAmount", value = "借款金额", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "industry", value = "工作类型", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "feeChannelName", value = "资金渠道名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "productName", value = "产品名称", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "ownerUserName", value = "业务员名称", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "householdRegister", value = "城市户籍", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页(从1开始)", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少件", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/customerInterviewList", method = RequestMethod.GET)
    @ResponseBody
    public QueryResult customerInterviewList(@RequestHeader("Authorization") String token,
                                             @RequestParam(value = "name",required = false) String name,
                                             @RequestParam(value = "loanAmount",required = false) String loanAmount,
                                             @RequestParam(value = "industry",required = false) String industry,
                                             @RequestParam(value = "feeChannelName",required = false) String feeChannelName,
                                             @RequestParam(value = "productName",required = false) String productName,
                                             @RequestParam(value = "ownerUserName",required = false) String ownerUserName,
                                             @RequestParam(value = "telephonenumber",required = false) String telephonenumber,
                                             @RequestParam(value = "householdRegister",required = false) String householdRegister,
                                             @RequestParam(value = "page",required = true) Integer page,
                                             @RequestParam(value = "size",required = true) Integer size){
        QueryResult resultDto = new QueryResult();
        try {
            resultDto= customerInterviewService.customerInterviewList(token,name,loanAmount,industry,feeChannelName,productName,ownerUserName,telephonenumber,householdRegister,page,size);

        }catch (Exception e){
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            logger.error("--------------->customerList获取列表信息操作失败",e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);

        }

        return  resultDto;
    }
    @ApiOperation(value="根据客户id查找客户信息", notes="根据客户id查找客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerid", value = "1", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/findCustomerinteViewById", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto findCustomerinteViewById(@RequestParam Integer customerid) {
        CronusDto cronusDto = new CronusDto();
        try {
            //   String customerids = jsonObject.getString("customerids");
            if (customerid == null || "".equals(customerid)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            cronusDto = customerInterviewService.findCustomerinteViewById(customerid);
            return cronusDto;
        } catch (Exception e) {
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            logger.error("--------------->findCustomerinteVIewById 获取用户信息失败", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }
    @ApiOperation(value="添加客户面谈信息", notes="添加客户面谈信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerInterViewBaseCarHouseInsturDto", value = "", required = true, paramType = "body", dataType = "CustomerInterViewBaseCarHouseInsturDto")
    })
    @RequestMapping(value = "/addCustomerView", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto addCustomerView(@RequestBody CustomerInterViewBaseCarHouseInsturDTO customerInterViewBaseCarHouseInsturDTO, @RequestHeader("Authorization") String token){
        CronusDto cronusDto = new CronusDto();
        try{
            cronusDto = customerInterviewService.addCustomerView(customerInterViewBaseCarHouseInsturDTO,token);
        }catch (Exception e){
            logger.error("--------------->addCustomerView 客户面谈信息添加失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }

    @ApiOperation(value="编辑客户面谈信息", notes="编辑客户面谈信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerid", value = "1", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/editCustomerinteView", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCustomerinteView(@RequestBody JSONObject jsonObject) {
        CronusDto cronusDto = new CronusDto();
        try {
            Integer customerid = jsonObject.getInteger("customerid");
            if (customerid == null || "".equals(customerid)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            cronusDto = customerInterviewService.editCustomerinteView(customerid);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->findCustomerinteVIewById 获取用户信息失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }
    @ApiOperation(value="提交编辑客户面谈信息", notes="提交编辑客户面谈信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerInterViewBaseCarHouseInsturDTO", value = "", required = true, paramType = "body", dataType = "CustomerInterViewBaseCarHouseInsturDTO")
    })
    @RequestMapping(value = "/editCustomerViewOk", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto edditCustomerViewOk(@RequestBody CustomerInterViewBaseCarHouseInsturDTO customerInterViewBaseCarHouseInsturDTO, @RequestHeader("Authorization") String token){
        CronusDto cronusDto = new CronusDto();
        try{
            cronusDto = customerInterviewService.edditCustomerViewOk(customerInterViewBaseCarHouseInsturDTO,token);
        }catch (Exception e){
            logger.error("--------------->edditCustomerViewOk 提交客户面谈信信息失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }
}
