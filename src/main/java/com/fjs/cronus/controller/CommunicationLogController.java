package com.fjs.cronus.controller;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;

import com.fjs.cronus.dto.api.PHPLoginDto;

import com.fjs.cronus.dto.thea.CustomerUsefulDTO;

import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.CommunicationLogService;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yinzf on 2017/9/19.
 */
@Controller
@Api(description = "沟通记录控制器")
@RequestMapping("communicationLog/v1")
public class CommunicationLogController {
    private  static  final Logger logger = LoggerFactory.getLogger(CommunicationLogController.class);

    @Autowired
    private CommunicationLogService communicationLogService;
//    @Autowired
//    private LoanService loanService;
    @Autowired
    private UcService thorUcService;
    @Autowired
    private CustomerInfoService iCustomerService;

    @ApiOperation(value="根据客户id获取沟通日志", notes="根据客户id获取沟通日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query",  dataType = "int"),
    })
    @RequestMapping(value = "/selectByCustomerId", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CommunicationLog> selectByCustomerId(@RequestParam(required = true) Integer customerId, HttpServletRequest request){
        CronusDto theaApiDTO=new CronusDto<>();
        List<CommunicationLog> communicationLogList=null;
        String token=request.getHeader("Authorization");
        com.fjs.cronus.dto.uc.UserInfoDTO userInfoDTO =thorUcService.getUserIdByToken(token,CommonConst.SYSTEMNAME);
        try{
            if (customerId != null){
              /*  Loan loan=loanService.getByCustomerId(customerId);
                if(loan == null){
                    logger.error("该交易不存在");
                    throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
                }*/
                communicationLogList=communicationLogService.listByLoanId(customerId,token);
                theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            }else{
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
            }
        }catch (Exception e){
            logger.error("根据交易id获取沟通日志失败",e);
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        theaApiDTO.setData(communicationLogList);
        return theaApiDTO;
    }

    @ApiOperation(value="新增沟通日志", notes="新增沟通日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/insertLog", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto inserLog(@Valid @RequestBody CustomerUsefulDTO customerUsefulDTO, BindingResult result, HttpServletRequest request){
        logger.info("新增沟通日志的数据："+customerUsefulDTO.toString());
        CronusDto theaApiDTO=new CronusDto ();
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        if (customerUsefulDTO.getLoanId() == null){
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage("loanId不能为空");
            return theaApiDTO;
        }
        if (customerUsefulDTO.getCustomerId() == null){
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage("customerId不能为空");
            return theaApiDTO;
        }
        if (StringUtils.isEmpty(customerUsefulDTO.getCooperationStatus()) ){
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonConst.COOPERATION_STATUS_NULL);
            return theaApiDTO;
        }
        if (StringUtils.isNotEmpty(customerUsefulDTO.getHouseStatus()) || customerUsefulDTO.getLoanAmount() != null
                || StringUtils.isNotEmpty(customerUsefulDTO.getContent())){
            //沟通内容
            if (StringUtils.isEmpty(customerUsefulDTO.getContent()) ){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.CONTENT_NULL);
                return theaApiDTO;
            }
        }
        String token=request.getHeader("Authorization");
        UserInfoDTO userInfoDTO =thorUcService.getUserIdByToken(token,CommonConst.SYSTEMNAME);

        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.ADD_COMM_LOG_URL)){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        CustomerInfo customerInfo = iCustomerService.findCustomerById(customerUsefulDTO.getCustomerId());
        if(customerInfo == null){
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            return theaApiDTO;
        }
        //只有业务员本人才能添加
        if (customerInfo.getOwnUserId() == null){
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE_COMMUNICATE);
            return theaApiDTO;
        }
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id().toString()) && customerInfo.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id().toString())){
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
            return theaApiDTO;
        }
        try{
            CustomerInfo customer=iCustomerService.findCustomerById(customerUsefulDTO.getCustomerId());
            int createResult = communicationLogService.addLog(customerUsefulDTO,customer,userInfoDTO,token);
            if (createResult >0) {
                theaApiDTO.setResult(CommonMessage.ADD_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->insertLog创建沟通记录失败");
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        }catch (Exception e){
            logger.error("新增沟通日志失败",e);
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }
}
