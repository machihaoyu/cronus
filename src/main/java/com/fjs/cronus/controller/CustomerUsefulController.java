package com.fjs.cronus.controller;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.thea.CustomerUsefulDTO;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.CustomerUsefulService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 有效客户控制类
 * Created by yinzf on 2017/10/14.
 */
@Controller
@RequestMapping("customerUseful/v1")
public class CustomerUsefulController {
    private  static  final Logger logger = LoggerFactory.getLogger(CustomerUsefulController.class);

    @Autowired
    private CustomerUsefulService customerUsefulService;
    @Autowired
    private UcService thorUcService;
   /* @Autowired
    private LoanService loanService;*/
    @Autowired
    private CustomerInfoService iCustomerService;

    @ApiOperation(value="根据交易id获取确认信息", notes="根据交易id获取确认信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "loanId", value = "交易id", required = true, paramType = "query",  dataType = "int"),
    })
    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerUsefulDTO>> selectById(@RequestParam(required = true) Integer loanId, HttpServletRequest request){
        CronusDto theaApiDTO=new CronusDto<>();
        CustomerUsefulDTO customerUsefulDTO=null;
       /* try{
            String token=request.getHeader("Authorization");
            UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);

//            customerUsefulDTO=new CustomerUsefulDTO();
            if (loanId != null){
                Loan loan=loanService.getByPrimaryKey(loanId);
                if (loan == null){
                    logger.error("该交易不存在");
                    throw new CronusException(CronusException.Type.MESSAGE_NOT_EXIST_LOAN);
                }
                if (StringUtils.isNotEmpty(userInfoDTO.getUser_id()) && loan.getOwnUserId() != null
                        && loan.getOwnUserId() != Integer.parseInt(userInfoDTO.getUser_id())){
                    theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                    theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                    return theaApiDTO;
                }
                CustomerUseful customerUseful=customerUsefulService.selectByLoanId(loanId);
                if (customerUseful != null){
                    customerUsefulDTO=customerUsefulService.copyProperty(customerUseful);
                    CronusDto<CustomerDTO> cronusDto=iCustomerService.findCustomerByFeild(token,customerUsefulDTO.getCustomerId());
                    CustomerDTO customerDto=cronusDto.getData();
                    customerUsefulDTO.setTelephonenumber(customerDto.getTelephonenumber());
                }
                theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            }
        }catch(Exception e){
            logger.error("根据交易id获取确认信息",e);
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        theaApiDTO.setData(customerUsefulDTO);*/
        return theaApiDTO;
    }
}
