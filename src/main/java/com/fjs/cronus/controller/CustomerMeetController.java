package com.fjs.cronus.controller;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.PHPLoginDto;

import com.fjs.cronus.dto.thea.CustomerMeetDTO;

import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CustomerMeet;
import com.fjs.cronus.service.CustomerMeetService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yinzf on 2017/10/14.
 */
@Controller
@Api(description = "面见控制器")
@RequestMapping("customerMeet/v1")
public class CustomerMeetController {
    private  static  final Logger logger = LoggerFactory.getLogger(CustomerMeetController.class);

    @Autowired
    private CustomerMeetService customerMeetService;
    @Autowired
    private UcService thorUcService;
   /* @Autowired
    private LoanService loanService;*/


    @ApiOperation(value="根据交易id获取面见记录", notes="根据交易id获取面见记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "loanId", value = "交易id", required = true, paramType = "query",  dataType = "int"),
    })
    @RequestMapping(value = "/selectByLoanId", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<CustomerMeetDTO> selectByLoanId(@RequestParam(required = true) Integer loanId, HttpServletRequest request){
        CronusDto theaApiDTO=new CronusDto<>();
        List<CustomerMeet> customerMeetList=null;
        List<CustomerMeetDTO> customerMeetDTOList=new ArrayList<CustomerMeetDTO>();
        String token=request.getHeader("Authorization");
        UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        /*try{
            if (loanId != null){
                Loan loan=loanService.getByPrimaryKey(loanId);
                if(loan == null){
                    logger.error("该交易不存在");
                    throw new TheaException(TheaException.Type.MESSAGE_NOT_EXIST_LOAN);
                }
                customerMeetList = customerMeetService.listByLoanId(loanId,token);
                for (CustomerMeet customerMeet:customerMeetList){
                    CustomerMeetDTO customerMeetDTO=new CustomerMeetDTO();
                    customerMeetDTO=customerMeetService.copyProperty(customerMeet);
                    if (customerMeetDTO != null){
                        customerMeetDTOList.add(customerMeetDTO);
                    }
                }
                theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            }else{
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
            }
        }catch (Exception e){
            logger.error("根据交易id获取面见记录",e);
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        theaApiDTO.setData(customerMeetDTOList);*/
        return theaApiDTO;
    }

    @ApiOperation(value="新增面见", notes="新增面见")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/insertCustomerMeet", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public CronusDto inserCustomerMeet(@Valid @RequestBody CustomerMeetDTO customerMeetDTO, BindingResult result, HttpServletRequest request){
        logger.info("新增面见的数据：" + customerMeetDTO.toString());
        CronusDto theaApiDTO=new CronusDto();
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        String token=request.getHeader("Authorization");
        UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.ADD_LOAN_URL)){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        try{
            if (customerMeetDTO.getLoanId() == null){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage("交易id不能为空");
                return theaApiDTO;
            }
            //Loan loan = loanService.getByPrimaryKey(customerMeetDTO.getLoanId());
         /*   if (loan == null){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage("交易id对应的交易不存在");
                return theaApiDTO;
            }
            //权限控制
//            if (! loan.getOwnUserId().toString().equals(userInfoDTO.getUser_id())){
//
//            }
            customerMeetDTO.setCustomerId(loan.getCustomerId());
            int createResult = customerMeetService.addCustomerMeet(customerMeetDTO,userInfoDTO,loan);
            if (createResult >0) {
                theaApiDTO.setResult(CommonMessage.ADD_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->insertCustomerMeet创建面见失败");
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
                throw new TheaException(TheaException.Type.MESSAGE_OTHER_ERROR);
            }*/
        }catch (Exception e){
            logger.error("-------------->insertCustomerMeet创建面见失败",e);
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }
}
