package com.fjs.cronus.controller;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.ServiceConstant;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.PHPLoginDto;

import com.fjs.cronus.dto.api.WalletApiDTO;
import com.fjs.cronus.dto.cronus.AddCustomerMeetDTO;
import com.fjs.cronus.dto.thea.CustomerMeetDTO;

import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerMeet;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.CustomerMeetService;
import com.fjs.cronus.service.client.WalletService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DEC3Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/v1")
public class CustomerMeetController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerMeetController.class);

    @Autowired
    private CustomerMeetService customerMeetService;
    @Autowired
    private UcService thorUcService;
    @Autowired
    private CustomerInfoService customerInfoService;
//    @Autowired
//    private WalletService walletService;

    @ApiOperation(value = "根据客户id获取面见记录", notes = "根据客户id获取面见记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/selectCustomerMeetByCustomerId", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<CustomerMeetDTO>> selectByCustomerId(@RequestParam(required = true) Integer customerId, @RequestHeader("Authorization") String token) {
        CronusDto theaApiDTO = new CronusDto<>();
        List<CustomerMeet> customerMeetList = null;
        List<CustomerMeetDTO> customerMeetDTOList = new ArrayList<CustomerMeetDTO>();
        try {
            if (customerId != null) {
                CustomerInfo customerInfo = customerInfoService.findCustomerById(customerId);
                if (customerInfo == null) {
                    logger.error("该客户不存在");
                    throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
                }
                customerMeetList = customerMeetService.listByCustomerId(customerId, token);
                for (CustomerMeet customerMeet : customerMeetList) {
                    CustomerMeetDTO customerMeetDTO = new CustomerMeetDTO();
                    customerMeetDTO = customerMeetService.copyProperty(customerMeet, token);
                    if (customerMeetDTO != null) {
                        customerMeetDTOList.add(customerMeetDTO);
                    }
                }
                theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            } else {
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
            }
        } catch (Exception e) {
            logger.error("根据id获取面见记录", e);
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        theaApiDTO.setData(customerMeetDTOList);
        return theaApiDTO;
    }

    @ApiOperation(value = "新增面见", notes = "新增面见")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/insertCustomerMeet", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public CronusDto inserCustomerMeet(@Valid @RequestBody AddCustomerMeetDTO customerMeetDTO, BindingResult result, HttpServletRequest request) {
        logger.info("新增面见的数据：" + customerMeetDTO.toString());
        CronusDto theaApiDTO = new CronusDto();
        if (result.hasErrors()) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        String token = request.getHeader("Authorization");
        //  UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        //没有保留的用户不能面见
        try {
            if (customerMeetDTO.getCustomerId() == null) {
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage("客户id不能为空");
                return theaApiDTO;
            }
            CustomerInfo customerInfo = customerInfoService.findCustomerById(customerMeetDTO.getCustomerId());
            if (customerInfo == null) {
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage("客户不存在");
                return theaApiDTO;
            }
            //只有业务员本人才能添加
            if (customerInfo.getOwnUserId() == null) {
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE_COMMUNICATE);
                return theaApiDTO;
            }
            if (StringUtils.isNotEmpty(resultDto.getUser_info().getUser_id().toString()) && customerInfo.getOwnUserId() != Integer.parseInt(resultDto.getUser_info().getUser_id().toString())) {
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NOAUTHCUSTOMERMEET);
                return theaApiDTO;
            }
//            if (customerInfo.getRemain() != 1) {
//                throw new CronusException(CronusException.Type.CRM_KEEPCUSTOMER_ERROR);
//            }
            customerMeetDTO.setCustomerId(customerInfo.getId());
            int createResult = customerMeetService.addCustomerMeet(customerMeetDTO, resultDto, customerInfo, token);
            if (createResult > 0) {
////                面见的时候发送数据，客户是mgm来源调用
//                if (ServiceConstant.MGMCUSTOMERSOURCE.equals(customerInfo.getCustomerSource())){
//                    //获取电话号码
//                    String phone = customerInfo.getTelephonenumber();
//                    //解析,发送
//                    WalletApiDTO walletApiDTO = walletService.confirmEffective(token, DEC3Util.des3DecodeCBC(phone));
//                    if (walletApiDTO.getCode() != 200){
//                        logger.error("调用MGM错误返回数据："+walletApiDTO.toString());
//                    }
//                }
                theaApiDTO.setResult(CommonMessage.ADD_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->insertCustomerMeet创建面见失败");
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        } catch (Exception e) {
            logger.error("-------------->insertCustomerMeet创建面见失败", e);
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }


    @ApiOperation(value = "c端：根据客户id获取最近1条面见记录", notes = "c端：根据客户id获取最近1条面见记录 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getCustomerMeetByCustomerId", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getCustomerMeetByCustomerId(
            @RequestParam(required = true) Integer customerId,
            @RequestParam(required = true) Long loanCreatTime,
            @RequestHeader("Authorization") String token
    ) {
        try {
            Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

            return customerMeetService.getCustomerMeetByCustomerId(customerId, userId, loanCreatTime);
        }catch (Exception e) {
            CronusDto result = new CronusDto();
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                result.setResult(Integer.valueOf(cronusException.getResponseError().getStatus()));
                result.setMessage(cronusException.getResponseError().getMessage());
                return result;
            } else {
                logger.error(CronusException.Type.THEA_SYSTEM_ERROR.getError(), e);
                result.setResult(Integer.valueOf(CronusException.Type.THEA_SYSTEM_ERROR.getStatus()));
                result.setMessage(e.getMessage());
                return result;
            }
        }
    }
}
