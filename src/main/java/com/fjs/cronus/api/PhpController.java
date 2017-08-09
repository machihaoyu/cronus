package com.fjs.cronus.api;

import com.alibaba.fastjson.JSON;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.dto.php.CustomerInterviewBaseInfoDTO;
import com.fjs.cronus.dto.php.CustomerInterviewCarInfoDTO;
import com.fjs.cronus.dto.php.CustomerInterviewHouseInfoDTO;
import com.fjs.cronus.dto.php.CustomerInterviewInsuranceInfoDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static com.fjs.cronus.exception.ExceptionValidate.validateResponse;

/**
 * Created by Administrator on 2017/7/12 0012.
 */
@RestController
@RequestMapping("/php/api/v1")
public class PhpController {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${sale.url}")
    private String saleUrl;

    @Value("${sale.key}")
    private String saleKey;

    @Autowired
    RestTemplate restTemplate;

    //添加和修改客户面谈信息
    @ApiOperation(value="PHP客户面谈基础信息", notes="添加/修改客户面谈基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer test8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "customerInterviewBaseInfoDTO", value = "",
                    required = true, paramType = "body",  dataType = "CustomerInterviewBaseInfoDTO"),
            @ApiImplicitParam(name = "user_id", value = "",
                    required = true, paramType = "query",  dataType = "int"),

    })
    @RequestMapping(value = "/addCustomerInterviewBaseInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData addCustomerInterviewBaseInfo(@RequestBody CustomerInterviewBaseInfoDTO customerInterviewBaseInfoDTO,
                                                     @RequestParam Integer user_id) {
        String url = saleUrl + "addCustomerInterviewBaseInfo";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("user_id", user_id);
        param.add("customer_interview_base_info_id", customerInterviewBaseInfoDTO.getCustomer_interview_base_info_id());
        param.add("customer_id", customerInterviewBaseInfoDTO.getCustomer_id());
        param.add("owner_user_id", customerInterviewBaseInfoDTO.getOwner_user_id());
        param.add("owner_user_name", customerInterviewBaseInfoDTO.getOwner_user_name());
        param.add("name", customerInterviewBaseInfoDTO.getName());
        param.add("sex", customerInterviewBaseInfoDTO.getSex());
        param.add("birth_date", customerInterviewBaseInfoDTO.getBirth_date());
        param.add("telephonenumber", customerInterviewBaseInfoDTO.getTelephonenumber());
        param.add("marital_status", customerInterviewBaseInfoDTO.getMarital_status());
        param.add("household_register", customerInterviewBaseInfoDTO.getHousehold_register());
        param.add("education", customerInterviewBaseInfoDTO.getEducation());
        param.add("fee_channel_name", customerInterviewBaseInfoDTO.getFee_channel_name());
        param.add("product_name", customerInterviewBaseInfoDTO.getProduct_name());
        param.add("loan_amount", customerInterviewBaseInfoDTO.getLoan_amount());
        param.add("loan_time", customerInterviewBaseInfoDTO.getLoan_time());
        param.add("loan_use_time", customerInterviewBaseInfoDTO.getLoan_use_time());
        param.add("loan_purpose", customerInterviewBaseInfoDTO.getLoan_purpose());
        param.add("payment_type", customerInterviewBaseInfoDTO.getPayment_type());
        param.add("credit_record", customerInterviewBaseInfoDTO.getCredit_record());
        param.add("zhima_credit", customerInterviewBaseInfoDTO.getZhima_credit());
        param.add("credit_query_num_two_month", customerInterviewBaseInfoDTO.getCredit_query_num_two_month());
        param.add("credit_query_num_six_month", customerInterviewBaseInfoDTO.getCredit_query_num_six_month());
        param.add("continuity_overdue_num_two_year", customerInterviewBaseInfoDTO.getContinuity_overdue_num_two_year());
        param.add("total_overdue_num_two_year", customerInterviewBaseInfoDTO.getTotal_overdue_num_two_year());
        param.add("debt_amount", customerInterviewBaseInfoDTO.getDebt_amount());
        param.add("is_overdue", customerInterviewBaseInfoDTO.getIs_overdue());
        param.add("overdue_amount", customerInterviewBaseInfoDTO.getOverdue_amount());
        param.add("industry", customerInterviewBaseInfoDTO.getIndustry());
        param.add("income_amount", customerInterviewBaseInfoDTO.getIncome_amount());
        param.add("social_security_date", customerInterviewBaseInfoDTO.getSocial_security_date());
        param.add("social_security_payment", customerInterviewBaseInfoDTO.getSocial_security_payment());
        param.add("housing_fund_date",customerInterviewBaseInfoDTO.getHousing_fund_date());
        param.add("housing_fund_payment",customerInterviewBaseInfoDTO.getHousing_fund_payment());
        param.add("work_date",customerInterviewBaseInfoDTO.getWork_date());
        param.add("company_register_date", customerInterviewBaseInfoDTO.getCompany_register_date());
        param.add("share_rate",customerInterviewBaseInfoDTO.getShare_rate());
        param.add("public_flow_year_amount",customerInterviewBaseInfoDTO.getPublic_flow_year_amount());
        param.add("private_flow_year_amount",customerInterviewBaseInfoDTO.getPrivate_flow_year_amount());
        param.add("is_litigation",customerInterviewBaseInfoDTO.getIs_litigation());
        param.add("retire_date",customerInterviewBaseInfoDTO.getRetire_date());
        param.add("retirement_pay_min_amount",customerInterviewBaseInfoDTO.getRetirement_pay_min_amount());
        param.add("is_relative_known",customerInterviewBaseInfoDTO.getIs_relative_known());
        param.add("remark",customerInterviewBaseInfoDTO.getRemark());
        param.add("create_time",customerInterviewBaseInfoDTO.getCreate_time());
        param.add("create_user_id",customerInterviewBaseInfoDTO.getCreate_user_id());
        param.add("update_time",customerInterviewBaseInfoDTO.getUpdate_time());
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        return data;
    }


    //添加和修改客户面谈房产信息
    @ApiOperation(value="PHP客户面谈保险信息", notes="添加/修改客户面谈保险信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer test8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "customerInterviewInsuranceInfoDTO", value = "",
                    required = true, paramType = "body",  dataType = "CustomerInterviewInsuranceInfoDTO"),
            @ApiImplicitParam(name = "user_id", value = "",
                    required = true, paramType = "query",  dataType = "int"),

    })
    @RequestMapping(value = "/addCustomerInterviewInsuranceInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData addCustomerInterviewInsuranceInfo(@RequestBody CustomerInterviewInsuranceInfoDTO customerInterviewInsuranceInfoDTO,
                                                     @RequestParam Integer user_id) {
        String url = saleUrl + "addCustomerInterviewInsuranceInfo";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("user_id",user_id);
        param.add("customer_interview_insurance_info_id",customerInterviewInsuranceInfoDTO.getCustomer_interview_insurance_info_id());
        param.add("customer_interview_base_info_id",customerInterviewInsuranceInfoDTO.getCustomer_interview_base_info_id());
        param.add("insurance_type",customerInterviewInsuranceInfoDTO.getInsurance_type());
        param.add("pay_type",customerInterviewInsuranceInfoDTO.getPay_type());
        param.add("year_pay_amount",customerInterviewInsuranceInfoDTO.getYear_pay_amount());
        param.add("month_pay_amount",customerInterviewInsuranceInfoDTO.getMonth_pay_amount());
        param.add("effect_date",customerInterviewInsuranceInfoDTO.getEffect_date());
        param.add("is_suspend",customerInterviewInsuranceInfoDTO.getIs_suspend());
        param.add("create_time",customerInterviewInsuranceInfoDTO.getCreate_time());
        param.add("create_user_id",customerInterviewInsuranceInfoDTO.getCreate_user_id());
        param.add("update_time",customerInterviewInsuranceInfoDTO.getUpdate_time());
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        return data;
    }

    //添加和修改客户面谈房产信息
    @ApiOperation(value="PHP客户面谈房产信息", notes="添加/修改客户面谈房产信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer test8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "customerInterviewHouseInfoDTO", value = "",
                    required = true, paramType = "body",  dataType = "CustomerInterviewHouseInfoDTO"),
            @ApiImplicitParam(name = "user_id", value = "",
                    required = true, paramType = "query",  dataType = "int"),

    })
    @RequestMapping(value = "/addCustomerInterviewHouseInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData addCustomerInterviewHouseInfo(@RequestBody CustomerInterviewHouseInfoDTO customerInterviewHouseInfoDTO,
                                                          @RequestParam Integer user_id) {
        String url = saleUrl + "addCustomerInterviewHouseInfo";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("user_id",user_id);
        param.add("customer_interview_insurance_info_id",customerInterviewHouseInfoDTO.getCustomer_interview_house_info_id());
        param.add("customer_interview_base_info_id",customerInterviewHouseInfoDTO.getCustomer_interview_base_info_id());
        param.add("house_status",customerInterviewHouseInfoDTO.getHouse_status());
        param.add("house_property_type",customerInterviewHouseInfoDTO.getHouse_property_type());
        param.add("area",customerInterviewHouseInfoDTO.getArea());
        param.add("build_date",customerInterviewHouseInfoDTO.getBuild_date());
        param.add("house_property_rights_num",customerInterviewHouseInfoDTO.getHouse_property_rights_num());
        param.add("is_child_in_property_rigths",customerInterviewHouseInfoDTO.getIs_child_in_property_rigths());
        param.add("is_old_in_property_rigths",customerInterviewHouseInfoDTO.getIs_old_in_property_rigths());
        param.add("is_property_rights_clear",customerInterviewHouseInfoDTO.getIs_property_rights_clear());
        param.add("is_other_house",customerInterviewHouseInfoDTO.getIs_other_house());
        param.add("is_bank_flow",customerInterviewHouseInfoDTO.getIs_bank_flow());
        param.add("bank_flow_month_amount",customerInterviewHouseInfoDTO.getBank_flow_month_amount());
        param.add("mortgage_month_amount",customerInterviewHouseInfoDTO.getMortgage_month_amount());
        param.add("mortgage_paid_num",customerInterviewHouseInfoDTO.getMortgage_paid_num());
        param.add("create_time",customerInterviewHouseInfoDTO.getCreate_time());
        param.add("create_user_id",customerInterviewHouseInfoDTO.getCreate_user_id());
        param.add("update_time",customerInterviewHouseInfoDTO.getUpdate_time());
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        return data;
    }

    //添加和修改客户面谈房产信息
    @ApiOperation(value="PHP客户面谈车辆信息", notes="添加/修改客户面谈车辆信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer test8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "customerInterviewCarInfoDTO", value = "",
                    required = true, paramType = "body",  dataType = "CustomerInterviewCarInfoDTO"),
            @ApiImplicitParam(name = "user_id", value = "",
                    required = true, paramType = "query",  dataType = "int"),

    })
    @RequestMapping(value = "/addCustomerInterviewCarInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData addCustomerInterviewCarInfo(@RequestBody CustomerInterviewCarInfoDTO customerInterviewCarInfoDTO,
                                                      @RequestParam Integer user_id) {
        String url = saleUrl + "addCustomerInterviewHouseInfo";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("user_id",user_id);
        param.add("customer_interview_car_info_id",customerInterviewCarInfoDTO.getCustomer_interview_car_info_id());
        param.add("customer_interview_base_info_id",customerInterviewCarInfoDTO.getCustomer_interview_base_info_id());
        param.add("car_type",customerInterviewCarInfoDTO.getCar_type());
        param.add("licence_plate_location",customerInterviewCarInfoDTO.getLicence_plate_location());
        param.add("buy_date",customerInterviewCarInfoDTO.getBuy_date());
        param.add("mortgage_paid_num",customerInterviewCarInfoDTO.getMortgage_paid_num());
        param.add("mortgage_month_amount",customerInterviewCarInfoDTO.getMortgage_month_amount());
        param.add("price_now",customerInterviewCarInfoDTO.getPrice_now());
        param.add("is_full_insurance",customerInterviewCarInfoDTO.getIs_full_insurance());
        param.add("create_time",customerInterviewCarInfoDTO.getCreate_time());
        param.add("create_user_id",customerInterviewCarInfoDTO.getCreate_user_id());
        param.add("update_time",customerInterviewCarInfoDTO.getUpdate_time());
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        return data;
    }


}
