package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CustomerInterViewBaseCarHouseInsturDto;
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
@RequestMapping("api/v1")
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
    public CronusDto customerInterviewList(@RequestHeader("Authorization") String token,
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
        CronusDto resultDto = new CronusDto();
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
            @ApiImplicitParam(name = "JSONObject", value = "{\n" +
                    "\t\"customerId\":\"1\",\n" +
                    "\t\"ownerUserId\":\"1\",\n" +
                    "\t\"ownerUserName\":\"zhanglei\",\n" +
                    "\t\"name\":\"sdafad\",\n" +
                    "\t\"sex\":\"nan\",\n" +
                    "\t\"age\":\"18\",\n" +
                    "\t\"birthDate\":\"\",\n" +
                    "\t\"telephonenumber\":\"1112221200\",\n" +
                    "\t\"maritalStatus\":\"1\",\n" +
                    "\t\"householdRegister\":\"asdasd\",\n" +
                    "\t\"education\":\"20\",\n" +
                    "\t\"feeChannelName\":\"asasdd\",\n" +
                    "\t\"productName\":\"asdasdad\",\n" +
                    "\t\"monthInterestRate\":\"asdasd\",\n" +
                    "\t\"serviceCharge\":\"asdad\",\n" +
                    "\t\"loanAmount\":\"asdas\",\n" +
                    "\t\"loanTime\":\"6\",\n" +
                    "\t\"loanUseTime\":\"6\",\n" +
                    "\t\"loanPurpose\":\"22\",\n" +
                    "\t\"paymentType\":\"1212\",\n" +
                    "\t\"creditRecord\":\"as\",\n" +
                    "\t\"zhimaCredit\":\"68\",\n" +
                    "\t\"creditQueryNumTwoMonth\":\"6\",\n" +
                    "\t\"creditQueryNumSixMonth\":\"12\",\n" +
                    "\t\"continuityOverdueNumTwoYear\":\"2\",\n" +
                    "\t\"totalOverdueNumTwoYear\":\"3\",\n" +
                    "\t\"debtAmount\":\"8\",\n" +
                    "\t\"isOverdue\":\"12\",\n" +
                    "\t\"overdueAmount\":\"12\",\n" +
                    "\t\"industry\":\"1\",\n" +
                    "\t\"incomeAmount\":\"1\",\n" +
                    "\t\"socialSecurityDate\":\"2017-8-12\",\n" +
                    "\t\"socialSecurityPayment\":\"1\",\n" +
                    "\t\"housingFundDate\":\"2017-8-12\",\n" +
                    "\t\"housingFundPayment\":\"1\",\n" +
                    "\t\"companyRegisterDate\":\"2017-8-12\",\n" +
                    "\t\"shareRate\":\"1.02\",\n" +
                    "\t\"publicFlowYearAmount\":\"1\",\n" +
                    "\t\"privateFlowYearAmount\":\"2\",\n" +
                    "\t\"isLitigation\":\"121\",\n" +
                    "\t\"retireDate\":\"2017-8-12\",\n" +
                    "\t\"retirementPayMinAmount\":\"1\",\n" +
                    "\t\"isRelativeKnown\":\"121\",\n" +
                    "\t\"remark\":\"1\",\n" +
                    "\t\"customerInterviewBaseInfoId\":\"2\",\n" +
                    "\t\"carType\":\"2\",\n" +
                    "\t\"licencePlateLocation\":\"asdad\",\n" +
                    "\t\"buyDate\":\"2017-8-12\",\n" +
                    "\t\"carMortgagePaidNum\":\"1\",\n" +
                    "\t\"carMortgageMonthAmount\":\"2\",\n" +
                    "\t\"priceNow\":\"12.00\",\n" +
                    "\t\"isFullInsurance\":\"1\",\n" +
                    "\t\"accepthousearea\":\"2\",\n" +
                    "\t\"houseStatus\":\"1\",\n" +
                    "\t\"housePropertyType\":\"1\",\n" +
                    "\t\"area\":\"12.00\",\n" +
                    "\t\"buildDate\":\"2017-8-12\",\n" +
                    "\t\"housePropertyRightsNum\":\"1\",\n" +
                    "\t\"isChildInPropertyRigths\":\"2\",\n" +
                    "\t\"isOldInPropertyRigths\":\"2\",\n" +
                    "\t\"isPropertyRightsClear\":\"1\",\n" +
                    "\t\"isOtherHouse\":\"2\",\n" +
                    "\t\"isBankFlow\":\"asda\",\n" +
                    "\t\"bankFlowMonthAmount\":\"5\",\n" +
                    "\t\"houseMortgageMonthAmount\":\"6\",\n" +
                    "\t\"houseMortgagePaidNum\":\"5\",\n" +
                    "\t\"insuranceCompany\":\"2\",\n" +
                    "\t\"insuranceType\":\"asdad\",\n" +
                    "\t\"payType\":\"12\",\n" +
                    "\t\"yearPayAmount\":\"5\",\n" +
                    "\t\"monthPayAmount\":\"5\",\n" +
                    "\t\"effectDate\":\"2017-8-12\",\n" +
                    "\t\"isSuspend\":\"1\"\n" +
                    "}", required = true, paramType = "query", dataType = "JSONObject")
    })
    @RequestMapping(value = "/addCustomerView", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto addCustomerView(@RequestBody JSONObject jsonObject,@RequestHeader("Authorization") String token){
        CronusDto cronusDto = new CronusDto();
        try{
            cronusDto = customerInterviewService.addCustomerView(jsonObject,token);
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
    @RequestMapping(value = "/editCustomerinteVIew", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto editCustomerinteView(@RequestParam Integer customerid) {
        CronusDto cronusDto = new CronusDto();
        try {
            //   String customerids = jsonObject.getString("customerids");
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
            @ApiImplicitParam(name = "JSONObject", value = " {     \n" +
                    " \t    \"id\": 3,\n" +
                    "        \"customerId\": 1,\n" +
                    "        \"ownerUserId\": 1,\n" +
                    "        \"ownerUserName\": \"shanghai\",\n" +
                    "        \"name\": \"sdafad\",\n" +
                    "        \"sex\": \"nan\",\n" +
                    "        \"age\": 18,\n" +
                    "        \"birthDate\": null,\n" +
                    "        \"telephonenumber\": \"1112221200\",\n" +
                    "        \"maritalStatus\": \"1\",\n" +
                    "        \"householdRegister\": \"asdasd\",\n" +
                    "        \"education\": \"20\",\n" +
                    "        \"feeChannelName\": \"asasdd\",\n" +
                    "        \"productName\": \"asdasdad\",\n" +
                    "        \"monthInterestRate\": \"asdasd\",\n" +
                    "        \"serviceCharge\": \"asdad\",\n" +
                    "        \"loanAmount\": \"asdas\",\n" +
                    "        \"loanTime\": 6,\n" +
                    "        \"loanUseTime\": \"6\",\n" +
                    "        \"loanPurpose\": \"22\",\n" +
                    "        \"paymentType\": \"1212\",\n" +
                    "        \"creditRecord\": \"as\",\n" +
                    "        \"zhimaCredit\": 68,\n" +
                    "        \"creditQueryNumTwoMonth\": 6,\n" +
                    "        \"creditQueryNumSixMonth\": 12,\n" +
                    "        \"continuityOverdueNumTwoYear\": 2,\n" +
                    "        \"totalOverdueNumTwoYear\": 3,\n" +
                    "        \"debtAmount\": 8,\n" +
                    "        \"isOverdue\": \"12\",\n" +
                    "        \"overdueAmount\": 12,\n" +
                    "        \"industry\": \"1\",\n" +
                    "        \"incomeAmount\": 1,\n" +
                    "        \"socialSecurityDate\": 1502467200000,\n" +
                    "        \"socialSecurityPayment\": 1,\n" +
                    "        \"housingFundDate\": 1502467200000,\n" +
                    "        \"housingFundPayment\": 1,\n" +
                    "        \"workDate\": null,\n" +
                    "        \"companyRegisterDate\": 1502467200000,\n" +
                    "        \"shareRate\": 1.02,\n" +
                    "        \"publicFlowYearAmount\": 1,\n" +
                    "        \"privateFlowYearAmount\": 2,\n" +
                    "        \"isLitigation\": \"121\",\n" +
                    "        \"retireDate\": 1502467200000,\n" +
                    "        \"retirementPayMinAmount\": 1,\n" +
                    "        \"isRelativeKnown\": \"121\",\n" +
                    "        \"remark\": \"1\",\n" +
                    "        \"carInfoid\": 1,\n" +
                    "        \"customerInterviewBaseInfoId\": 3,\n" +
                    "        \"carType\": \"2\",\n" +
                    "        \"licencePlateLocation\": \"asdad\",\n" +
                    "        \"buyDate\": 1502467200000,\n" +
                    "        \"carMortgagePaidNum\": 1,\n" +
                    "        \"carMortgageMonthAmount\": 2,\n" +
                    "        \"priceNow\": 12,\n" +
                    "        \"isFullInsurance\": \"1\",\n" +
                    "        \"houseInfoId\": 1,\n" +
                    "        \"accepthousearea\": \"2\",\n" +
                    "        \"houseStatus\": \"1\",\n" +
                    "        \"housePropertyType\": \"1\",\n" +
                    "        \"area\": 12,\n" +
                    "        \"buildDate\": 1502467200000,\n" +
                    "        \"housePropertyRightsNum\": 1,\n" +
                    "        \"isChildInPropertyRigths\": \"2\",\n" +
                    "        \"isOldInPropertyRigths\": \"2\",\n" +
                    "        \"isPropertyRightsClear\": \"1\",\n" +
                    "        \"isOtherHouse\": \"2\",\n" +
                    "        \"isBankFlow\": \"asda\",\n" +
                    "        \"bankFlowMonthAmount\": 5,\n" +
                    "        \"houseMortgageMonthAmount\": 6,\n" +
                    "        \"houseMortgagePaidNum\": 5,\n" +
                    "        \"insuranceInfoId\": null,\n" +
                    "        \"insuranceCompany\": \"2\",\n" +
                    "        \"insuranceType\": \"asdad\",\n" +
                    "        \"payType\": \"12\",\n" +
                    "        \"yearPayAmount\": 5,\n" +
                    "        \"monthPayAmount\": 5,\n" +
                    "        \"effectDate\": 1502467200000,\n" +
                    "        \"isSuspend\": \"1\"\n" +
                    " }", required = true, paramType = "query", dataType = "JSONObject")
    })
    @RequestMapping(value = "/edditCustomerViewOk", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto edditCustomerViewOk(@RequestBody JSONObject jsonObject,@RequestHeader("Authorization") String token){
        CronusDto cronusDto = new CronusDto();
        try{
            cronusDto = customerInterviewService.edditCustomerViewOk(jsonObject,token);
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
