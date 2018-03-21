package com.fjs.cronus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fjs.cronus.dto.crm.CRMData;
import com.fjs.cronus.dto.crm.FileData;
import com.fjs.cronus.dto.*;
import com.fjs.cronus.dto.customer.*;
import com.fjs.cronus.dto.login.AuthorityDTO;
import com.fjs.cronus.dto.login.LoginInfoDTO;
import com.fjs.cronus.dto.param.CustomerSaleParamDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.enums.ErrorNumEnum;
import com.fjs.cronus.entity.Agreement;
import com.fjs.cronus.entity.CustomerSale;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.exception.ExceptionValidate;
import com.fjs.cronus.util.DownloadFileUtil;
import com.fjs.cronus.util.StringAsciiUtil;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class CrmController {

    @Value("${base.url}")
    private String baseUrl;

    @Value("${base.key}")
    private String baseKey;

    @Value("${sale.url}")
    private String saleUrl;

    @Value("${sale.key}")
    private String saleKey;

    @Value("${base.url.document}")
    private String baseDocumentUrl;

    @Value("${base.url.index}")
    private String baseIndexUrl;

    @Autowired
    RestTemplate restTemplate;

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //用户登录
    @RequestMapping(value = "/userLogin", method = RequestMethod.GET)
    public LoginInfoDTO userLogin(@RequestParam String username, @RequestParam String password) {
        String url = baseUrl + "userLoginForApp?key=" + baseKey + "&system=sale&username="+ username +"&password="+ password ;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponseBase(data);
        LoginInfoDTO loginInfoDTO = JSONObject.parseObject(data.getRetData(), LoginInfoDTO.class);
        List<AuthorityDTO> authorLists = getLoginAuthor(loginInfoDTO.getAuthority());
        loginInfoDTO.setAuthorityDTOS(authorLists);
        return loginInfoDTO;
    }

    //用户登录并获取线上线下签章模式;
    @RequestMapping(value = "/userLoginNew",method = RequestMethod.GET)
    public LoginInfoDTO userLoginNew(@RequestParam String username, @RequestParam String password){
        String url = baseUrl + "userLoginForApp?key=" + baseKey + "&system=sale&username="+ username +"&password="+ password ;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponseBase(data);
        LoginInfoDTO loginInfoDTO = JSONObject.parseObject(data.getRetData(), LoginInfoDTO.class);
        List<AuthorityDTO> authorLists = getLoginAuthor(loginInfoDTO.getAuthority());
        loginInfoDTO.setAuthorityDTOS(authorLists);
        url = saleUrl + "getEnableOnLine&key=" + saleKey + "&user_id=" + loginInfoDTO.getUser_id();
        res = restTemplate.getForObject(url, String.class);
        data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        Integer model = JSON.parseObject(data.getRetData(),Integer.class);
        loginInfoDTO.setModel(model);
        return loginInfoDTO;
    }

    //通过id或name获取用户登录信息(登录接口入库，记住密码入口)
    @RequestMapping(value = "/getLoginUserByNameOrId",method = RequestMethod.GET)
    public LoginInfoDTO getLoginUserByNameOrId(@RequestParam Integer userId, @RequestParam String name){
        if(StringUtils.isNotEmpty(name) && null != userId && userId.intValue() > 0) throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, "用户名和ID不能同时传入!");
        if(StringUtils.isEmpty(name) && null == userId && userId.intValue() == 0) throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, "用户名或id必须传入一个!");
        String url = baseUrl + "getUserLoginInfoById";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",baseKey);
        param.add("system","sale");
        param.add("user_id",userId);
        param.add("name", name);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        LoginInfoDTO loginInfoDTO = JSON.parseObject(data.getRetData(),LoginInfoDTO.class);
        List<AuthorityDTO> authorLists = getLoginAuthor(loginInfoDTO.getAuthority());
        loginInfoDTO.setAuthorityDTOS(authorLists);
        url = saleUrl + "getEnableOnLine&key=" + saleKey + "&user_id=" + loginInfoDTO.getUser_id();
        res = restTemplate.getForObject(url, String.class);
        data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        Integer model = JSON.parseObject(data.getRetData(),Integer.class);
        loginInfoDTO.setModel(model);
        return loginInfoDTO;
    }

    //用户退出登录清crm缓存
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logoutCache(@RequestParam Integer userId) {
        String url = saleUrl + "logout?key=" + saleKey + "&user_id="+ userId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponseBase(data);
    }

    //修改密码
    @RequestMapping(value = "/editUserPassword", method = RequestMethod.POST)
    public void changePWD(@RequestParam Integer userId, @RequestParam String oldPWD, @RequestParam String newPWD){
        String url = saleUrl + "editUserPassword";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",userId);
        param.add("old_pw",oldPWD);
        param.add("new_pw",newPWD);
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
    }


    //获取客户列表
    @RequestMapping(value = "/getCustomerList", method = RequestMethod.POST)
    public PageBeanDTO<CustomerSaleDTO> getCustomerList(@RequestBody CustomerSaleParamDTO customerSaleParamDTO){
        String url = saleUrl + "getCustomerList?key=" + saleKey;
        if (null == customerSaleParamDTO.getUser_id() || customerSaleParamDTO.getUser_id().intValue() == 0) {
            throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, "用户不存在!");
        } else {
            url += "&user_id=" + customerSaleParamDTO.getUser_id();
        }
        if (StringUtils.isEmpty(customerSaleParamDTO.getType())) {
            throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, "请选择客户类型!");
        } else {
            url += "&type=" + customerSaleParamDTO.getType();
        }
        if (null != customerSaleParamDTO.getP() && customerSaleParamDTO.getP().intValue() != 0) {
            url += "&p=" + customerSaleParamDTO.getP();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getUser_ids())) {
            url += "&user_ids=" + customerSaleParamDTO.getUser_ids();
        }
        if (null != customerSaleParamDTO.getData_type() && customerSaleParamDTO.getData_type().intValue() != 0) {
            url += "&data_type=" + customerSaleParamDTO.getData_type();
        }
        if (null != customerSaleParamDTO.getLook_phone() && customerSaleParamDTO.getLook_phone().intValue() != 0) {
            url += "&look_phone=" + customerSaleParamDTO.getLook_phone();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getCooperation_status())) {
            url += "&cooperation_status=" + customerSaleParamDTO.getCooperation_status();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getRetain())) {
            url += "&retain=" + customerSaleParamDTO.getRetain();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getCustomer_name())) {
            url += "&customer_name=" + customerSaleParamDTO.getCustomer_name();
        }
        if (null != customerSaleParamDTO.getCustomer_level() && customerSaleParamDTO.getCustomer_level().intValue() != 0) {
            url += "&customer_level=" + customerSaleParamDTO.getCustomer_level();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getCustomer_classify())) {
            url += "&customer_classify=" + customerSaleParamDTO.getCustomer_classify();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getCustomer_type())) {
            url += "&customer_type=" + customerSaleParamDTO.getCustomer_type();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getTelephonenumber())) {
            url += "&telephonenumber=" + customerSaleParamDTO.getTelephonenumber();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getCustomer_source())) {
            url += "&customer_source=" + customerSaleParamDTO.getCustomer_source();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getAgreement_number())) {
            url += "&agreement_number=" + customerSaleParamDTO.getAgreement_number();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getOrderby())) {
            url += "&orderby=" + customerSaleParamDTO.getOrderby();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getPot())) {
            url += "&pot=" + customerSaleParamDTO.getPot();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getApp_search())) {
            url += "&app_search=" + customerSaleParamDTO.getApp_search();
        }
        if(StringUtils.isNotEmpty(customerSaleParamDTO.getOwner_user_name())){
            url +="&owner_user_name=" + customerSaleParamDTO.getOwner_user_name();
        }
        if(StringUtils.isNotEmpty(customerSaleParamDTO.getUtm_source())){
            url +="&utm_source=" + customerSaleParamDTO.getUtm_source();
        }
        if (null != customerSaleParamDTO.getPerpage() && customerSaleParamDTO.getPerpage().intValue() != 0) {
            url += "&perpage=" + customerSaleParamDTO.getPerpage();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getCommunication_order())) {
            url += "&communication_order=" + customerSaleParamDTO.getCommunication_order();
        }
        if (StringUtils.isNotEmpty(customerSaleParamDTO.getWant_communit())) {
            url += "&want_communit=" + customerSaleParamDTO.getWant_communit();
        }
        if (null != customerSaleParamDTO.getCustomer_id() && customerSaleParamDTO.getCustomer_id().intValue() != 0) {
            url += "&customer_id=" + customerSaleParamDTO.getCustomer_id();
        }
        String str = restTemplate.getForObject(url, String.class);
        ResponseData responseData = JSON.parseObject(str, ResponseData.class);
        validateResponse(responseData);
        return JSONObject.parseObject(responseData.getRetData(), new PageBeanDTO<CustomerSaleDTO>().getClass());
    }

    //获取客户详情
    @RequestMapping(value = "/getCustomerInfo", method = RequestMethod.GET)
    public CustomerSaleDTO getCustomerInfo(@RequestParam Integer customerId){
        String url = saleUrl + "getCustomerInfo?key=" + saleKey + "&customer_id=" + customerId;
        String str = restTemplate.getForObject(url, String.class);
        ResponseData responseData = JSON.parseObject(str, ResponseData.class);
        if (null != responseData && ErrorNumEnum.SUCCESS.getCode().equals(responseData.getErrNum()) ){
            return JSON.parseObject(responseData.getRetData(),CustomerSaleDTO.class);
        } else {
            throw new RuntimeException(responseData.getErrMsg());
        }
    }


    //添加和修改客户
    @RequestMapping(value="/addCustomer", method = RequestMethod.POST)
    public ResponseData addCustomer(@RequestBody CustomerSaleDTO customerSaleDTO, @RequestParam Integer userId, @RequestParam Integer dataType){
        String url = saleUrl + "addCustomer";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("customer_id",customerSaleDTO.getCustomer_id());
        param.add("user_id",userId);
        param.add("telephonenumber", customerSaleDTO.getTelephonenumber());
        param.add("customer_name", customerSaleDTO.getCustomer_name());
        param.add("loan_amount", customerSaleDTO.getLoan_amount());
        param.add("house_status", customerSaleDTO.getHouse_status());
        param.add("customer_classify", customerSaleDTO.getCustomer_classify());
        param.add("cooperation_status", customerSaleDTO.getCooperation_status());
        param.add("age",customerSaleDTO.getAge());
        param.add("id_card",customerSaleDTO.getId_card());
        param.add("marriage",customerSaleDTO.getMarriage());
        param.add("sex",customerSaleDTO.getSex());
        param.add("province_huji",customerSaleDTO.getProvince_huji());
        param.add("spare_phone",customerSaleDTO.getSpare_phone());
        param.add("customer_source",customerSaleDTO.getCustomer_source());
        param.add("utm_source", customerSaleDTO.getUtm_source());
        param.add("province", customerSaleDTO.getProvince());
        param.add("city", customerSaleDTO.getCity());
        param.add("area", customerSaleDTO.getArea());
        param.add("street", customerSaleDTO.getStreet());
        param.add("provinces", customerSaleDTO.getProvinces());
        param.add("citys", customerSaleDTO.getCitys());
        param.add("areas", customerSaleDTO.getAreas());
        param.add("streets", customerSaleDTO.getStreets());
        param.add("house_amount", customerSaleDTO.getHouse_amount());
        param.add("house_type", customerSaleDTO.getHouse_type());
        param.add("house_value", customerSaleDTO.getHouse_value());
        param.add("house_area", customerSaleDTO.getHouse_area());
        param.add("house_age", customerSaleDTO.getHouse_age());
        param.add("house_loan", customerSaleDTO.getHouse_loan());
        param.add("house_alone", customerSaleDTO.getHouse_alone());
        param.add("per_description", customerSaleDTO.getPer_description());
        param.add("data_type", dataType);
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        return data;
    }

    //新增客户沟通
    @RequestMapping(value = "/addCommunicationLog", method = RequestMethod.POST)
    public ResponseData addCommunicationLog(@RequestBody CommunicationLogDTO communicationLogDTO, String houseStatus, String loanAmount, String purpose, String cooperationStatus){
        String url = saleUrl + "addCommunicationLog";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("customer_id",communicationLogDTO.getCustomer_id());
        param.add("create_user_id",communicationLogDTO.getCreate_user_id());
        param.add("type",communicationLogDTO.getType());
        param.add("content",communicationLogDTO.getContent());
        param.add("next_time",communicationLogDTO.getNext_contact_time());
        param.add("next_contact_content",communicationLogDTO.getNext_contact_content());
        param.add("house_status",houseStatus);
        param.add("loan_amount",loanAmount);
        param.add("meet",communicationLogDTO.getMeet());
        param.add("meet_time",communicationLogDTO.getMeet_time());
        param.add("user_id", communicationLogDTO.getUser_id());
        param.add("purpose", purpose);
        param.add("cooperation_status", cooperationStatus);
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        return data;
    }


    //保留客户
    @RequestMapping(value = "/retain", method = RequestMethod.POST)
    public ResponseData retain(@RequestParam Integer userId,@RequestParam Integer customerId){
        String url = saleUrl + "retain";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("user_id",userId);
        param.add("customer_id",customerId);
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return data;
    }


    //领取客户
    @RequestMapping(value = "/receiveCustomer", method = RequestMethod.POST)
    public void receiveCustomer(@RequestParam Integer userId,@RequestParam Integer customerId){
        String url = saleUrl + "receiveCustomer";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("user_id",userId);
        param.add("customer_id",customerId);
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    /**
     * 客户扔回公盘(移除客户)
     */
    @RequestMapping(value = "/removeCustomer", method = RequestMethod.POST)
    public void removeCustomer(@RequestParam Integer customerId, @RequestParam Integer userId) {
        String url = saleUrl + "removeCustomer";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("customer_id",customerId);
        param.add("user_id",userId);
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }


    /**
     * 取消保留客户接口
     */
    @RequestMapping(value = "/unRetainCustomer", method = RequestMethod.POST)
    public void unRetainCustomer(@RequestParam Integer customerId, @RequestParam Integer userId) {
        String url = saleUrl + "unretain";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("customer_id",customerId);
        param.add("user_id",userId);
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //获取客户沟通日志
    @RequestMapping(value = "/getCustomerCommunication", method = RequestMethod.GET)
    public CommunicationInfoDTO getCustomerCommunication(@RequestParam Integer customerId,@RequestParam Integer userId){
        String url = saleUrl + "getCustomerCommunication?key=" + saleKey + "&customer_id=" + customerId + "&user_id=" + userId;
        String str = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(str, ResponseData.class);
        validateResponse(data);
        CommunicationInfoDTO communicationInfoDTO = JSON.parseObject(data.getRetData(), CommunicationInfoDTO.class);
        return communicationInfoDTO;
    }

    //获取客户面见记录
    @RequestMapping(value = "/getCustomerMeetLog", method = RequestMethod.GET)
    public List<CustomerMeetDTO> getCustomerMeetLog(@RequestParam Integer customerId){
        String url = saleUrl + "getCustomerMeetLog?key=" + saleKey + "&customer_id=" + customerId;
        String str = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        List<CustomerMeetDTO> list = JSON.parseArray(data.getRetData(), CustomerMeetDTO.class);
        return list;
    }

    //获取客户回访记录
    @RequestMapping(value = "/getCustomerCallbackPhoneLog", method = RequestMethod.GET)
    public List<CallbackLogDTO> getCustomerCallbackPhoneLog(@RequestParam Integer customerId){
        String url = saleUrl + "getCustomerCallbackPhoneLog?key=" + saleKey + "&customer_id=" + customerId;
        String str = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        List<CallbackLogDTO> list = JSON.parseArray(data.getRetData(), CallbackLogDTO.class);
        return list;
    }

    //获取沟通界面数据
    @RequestMapping(value = "/getCommunicationInfo", method = RequestMethod.GET)
    public CustomerCommunicationDTO getCustomerCommun(@RequestParam Integer customerId, @RequestParam Integer userId) {
        CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String separator = "|";
        //沟通
        String url = saleUrl + "getCustomerCommunication?key=" + saleKey + "&customer_id=" + customerId + "&user_id=" + userId;
        String str = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(str, ResponseData.class);
        validateResponse(data);
        CommunicationInfoDTO communicationInfoDTO = JSON.parseObject(data.getRetData(), CommunicationInfoDTO.class);
        //会面
        url = saleUrl + "getCustomerMeetLog?key=" + saleKey + "&customer_id=" + customerId;
        str = restTemplate.getForObject(url, String.class);
        data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        List<CustomerMeetDTO> meetList = JSON.parseArray(data.getRetData(), CustomerMeetDTO.class);
        //回访
        url = saleUrl + "getCustomerCallbackPhoneLog?key=" + saleKey + "&customer_id=" + customerId;
        str = restTemplate.getForObject(url, String.class);
        data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        List<CallbackLogDTO> backList = JSON.parseArray(data.getRetData(), CallbackLogDTO.class);

        if (null != communicationInfoDTO) {
            List<String> resList = new ArrayList<>();
            if (null != communicationInfoDTO.getData() && communicationInfoDTO.getData().size() > 0 ) {
                for (CommunicationLogDTO dto : communicationInfoDTO.getData()) {
                    String date = sdf.format(new Date(Long.valueOf(dto.getCreate_time() * 1000)));
                    String createName = dto.getCreate_user_name() == null ? "" : dto.getCreate_user_name();
                    resList.add(new String(date + separator + createName + separator + dto.getContent()));
                }
                customerCommunicationDTO.setCommunts(resList);
            }

            // 设置有无房产、意向金额、资金用途、电话号码
            customerCommunicationDTO.setHouseStatus(communicationInfoDTO.getHouse_status());
            if (null != communicationInfoDTO.getLoan_amount()) {
                if (communicationInfoDTO.getLoan_amount() != 0)
                communicationInfoDTO.setLoan_amount(new BigDecimal(communicationInfoDTO.getLoan_amount()).divide(new BigDecimal(10000)).setScale(4).doubleValue());
                String amount = communicationInfoDTO.getLoan_amount().toString();
                String suffer = amount.substring(amount.indexOf(".") + 1);
                if ("0".equals(suffer)) {
                    customerCommunicationDTO.setLoanAmount(amount.substring(0, amount.indexOf(".")));
                } else {
                    customerCommunicationDTO.setLoanAmount(amount);
                }
            }
            customerCommunicationDTO.setPurpose(communicationInfoDTO.getPurpose());
            customerCommunicationDTO.setTelPhone(communicationInfoDTO.getTelephonenumber());
        }


        //回访日志表
        if (null != backList && backList.size() > 0) {
            List<String> resList = new ArrayList<>();
            for (CallbackLogDTO dto : backList) {
                String date = sdf.format(new Date(Long.valueOf(dto.getCreate_time() * 1000)));
                String operaterName = dto.getCreate_user_name() == null ? "" : dto.getCreate_user_name();
                resList.add(new String(date + separator + operaterName));
            }
            customerCommunicationDTO.setVisits(resList);
        }


        //面见表
        if (null != meetList && meetList.size() > 0) {
            List<String> resList = new ArrayList<>();
            for (CustomerMeetDTO dto : meetList) {
                String date = sdf.format(new Date(Long.valueOf(dto.getCreate_time() * 1000)));
                String operaterName = dto.getUser_name() == null ? "" : dto.getUser_name();
                resList.add(new String(date + separator + operaterName));
            }
            customerCommunicationDTO.setMeets(resList);
        }
        return customerCommunicationDTO;
    }

    //获取客户协议
    @RequestMapping(value = "/getAgreementInfo", method = RequestMethod.GET)
    public AgreementDTO getAgreement(@RequestParam Integer agreementId){
        String url = saleUrl + "getAgreementInfo?key=" + saleKey + "&agreement_id=" + agreementId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
        AgreementDTO agreementDTO = JSON.parseObject(data.getRetData(), AgreementDTO.class);
        String haidaiCustomer = agreementDTO.getHaidaiCustomer();
        if(haidaiCustomer.length()>2){
            HaidaiCustomerDTO haidaiCustomerDTO = JSON.parseObject(haidaiCustomer, HaidaiCustomerDTO.class);
            agreementDTO.setHaidaiCustomerDTO(haidaiCustomerDTO);
        }
        return agreementDTO;
    }

    //新增客户协议
    @RequestMapping(value = "/addAgreementByCustomer", method = RequestMethod.POST)
    public void addAgreeMent(@RequestBody AgreementDTO agreementDTO) {
        LOGGER.warn("新增客户协议=" + ReflectionToStringBuilder.toString(agreementDTO));
        String url = saleUrl + "addAgreementByCustomer";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",agreementDTO.getUser_id());
        param.add("customer_id",agreementDTO.getCustomer_id());
        param.add("agreement_id",agreementDTO.getAgreement_id());
        param.add("number",agreementDTO.getNumber());
        param.add("product_type",agreementDTO.getProduct_type());
        param.add("borrower",agreementDTO.getBorrower());
        param.add("telephone",agreementDTO.getTelephone());
        param.add("plan_money",agreementDTO.getPlan_money());
        param.add("identity",agreementDTO.getIdentity());
        param.add("commission",agreementDTO.getCommission());
        param.add("rate",agreementDTO.getRate());
        param.add("deposit",agreementDTO.getDeposit());
        param.add("pay_type",agreementDTO.getPay_type());
        param.add("pay_time",agreementDTO.getTime());
        param.add("payee",agreementDTO.getPayee());
        param.add("payee_account",agreementDTO.getPayee_account());
        param.add("template_serialize",agreementDTO.getTemplate_serialize());
        if(agreementDTO.getLoan_id()!=null){
            param.add("loan_id",agreementDTO.getLoan_id());
        }
        if(agreementDTO.getPull_customer_id() != null){
            param.add("pull_customer_id",agreementDTO.getPull_customer_id());
        }
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
    }

    //将协议信息保存到缓存
    @RequestMapping(value = "/agreementAgainChapterSaveInfo",method = RequestMethod.POST)
    public void agreementAgainChapterSaveInfo(@RequestBody AgreementDTO agreementDTO){
        LOGGER.warn(" 将协议信息保存到缓存 " + ReflectionToStringBuilder.toString(agreementDTO));
        String url = saleUrl + "agreementAgainChapterSaveInfo";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",agreementDTO.getUser_id());
        param.add("agreement_id",agreementDTO.getAgreement_id());
        param.add("product_type",agreementDTO.getProduct_type());
        param.add("borrower",agreementDTO.getBorrower());
        param.add("telephone",agreementDTO.getTelephone());
        param.add("plan_money",agreementDTO.getPlan_money());
        param.add("identity",agreementDTO.getIdentity());
        param.add("commission",agreementDTO.getCommission());
        param.add("rate",agreementDTO.getRate());
        param.add("deposit",agreementDTO.getDeposit());
        param.add("pay_type",agreementDTO.getPay_type());
        param.add("pay_time",agreementDTO.getTime());
        param.add("payee",agreementDTO.getPayee());
        param.add("payee_account",agreementDTO.getPayee_account());
        param.add("template_serialize",agreementDTO.getTemplate_serialize());
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
    }

    //设置协议缓存，并发送短信;
    @RequestMapping(value = "/agreementChapterAndsendMobile",method = RequestMethod.POST)
    public void agreementChapterAndsendMobile(@RequestBody AgreementDTO agreementDTO){
        LOGGER.warn("设置协议缓存，并发送短信=" + ReflectionToStringBuilder.toString(agreementDTO));
        String url = saleUrl + "agreementAgainChapterSaveInfo";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",agreementDTO.getUser_id());
        param.add("agreement_id",agreementDTO.getAgreement_id());
        param.add("product_type",agreementDTO.getProduct_type());
        param.add("borrower",agreementDTO.getBorrower());
        param.add("telephone",agreementDTO.getTelephone());
        param.add("plan_money",agreementDTO.getPlan_money());
        param.add("identity",agreementDTO.getIdentity());
        param.add("commission",agreementDTO.getCommission());
        param.add("rate",agreementDTO.getRate());
        param.add("deposit",agreementDTO.getDeposit());
        param.add("pay_type",agreementDTO.getPay_type());
        param.add("pay_time",agreementDTO.getTime());
        param.add("payee",agreementDTO.getPayee());
        param.add("payee_account",agreementDTO.getPayee_account());
        param.add("template_serialize",agreementDTO.getTemplate_serialize());
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
        url = saleUrl + "sendMobileCodeForAgreementAgainChapter";
        MultiValueMap<String, Object> paramn = new LinkedMultiValueMap<>();
        paramn.add("key",saleKey);
        paramn.add("agreement_id",agreementDTO.getAgreement_id());
        paramn.add("user_id",agreementDTO.getUser_id());
        res = restTemplate.postForObject(url,paramn,String.class);
        data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
    }

    //协议意向书重签盖章
    @RequestMapping(value = "/againChapterAgreementForApp",method = RequestMethod.POST)
    public void againChapterAgreementForApp(@RequestParam Integer agreementId,@RequestParam Integer userId,@RequestParam String code){
        LOGGER.warn("userId=" + userId + ", code=" + code + ", param=" + ReflectionToStringBuilder.toString(agreementId));
        String url = saleUrl + "againChapterAgreementForApp";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("agreement_id",agreementId);
        param.add("user_id",userId);
        param.add("code",code);
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
    }

    //重签发送短信
    @RequestMapping(value = "/sendMobileCodeForAgreementAgainChapter",method = RequestMethod.POST)
    public void sendMobileCodeForAgreementAgainChapter(@RequestParam Integer agreementId,@RequestParam Integer userId){
        String url = saleUrl + "sendMobileCodeForAgreementAgainChapter";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("agreement_id",agreementId);
        param.add("user_id",userId);
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
    }

    //获取居间协议列表
    @RequestMapping(value = "/getAgreementList", method = RequestMethod.GET)
    public Agreement getAgreementList (@RequestParam String users, @RequestParam String search, @RequestParam Integer p){
        String url = saleUrl + "getAgreementList?key=" + saleKey + "&users=" + users
                + "&search=" + search + "&p=" + p;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),Agreement.class);
    }

    //获取客户协议列表
    @RequestMapping(value = "/getAgreementListByCustomerIds", method = RequestMethod.GET)
    public Agreement getAgreementListByCustomerIds(@RequestParam String customerIds, @RequestParam String search, @RequestParam Integer p,@RequestParam Integer userId) {
        String url = saleUrl + "getAgreementList?key=" + saleKey + "&customer_ids=" + customerIds
                + "&search=" + search + "&p=" + p + "&users=" + userId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(), Agreement.class);
    }


    //附件列表
    @RequestMapping(value = "/getCustomerFile", method = RequestMethod.GET)
    public String getCustomerFile (@RequestParam Integer customerId){
        String url = saleUrl + "getDocumentByCustomerId?key=" + saleKey + "&customer_id=" + customerId;
        String str = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(str, ResponseData.class);
        validateResponse(data);
        return data.getRetData();
    }

    //预览|下载单个附件
    @RequestMapping(value = "/preViewDocument", method = RequestMethod.GET)
    public String preViewDocument(@RequestParam Integer id){
        String url = saleUrl + "preViewDocument?key=" + saleKey + "&id=" + id;
        return url;
    }

    //获取附件分类
    @RequestMapping(value = "/getNextCategory", method = RequestMethod.GET)
    public List<DocumentCategoryDTO> getNextCategory(@RequestParam Integer cateId){
        String url = saleUrl + "getNextCategory?key=" + saleKey + "&cate_id=" + cateId;
        String str = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(str, ResponseData.class);
        validateResponse(data);
        List<DocumentCategoryDTO> list = JSON.parseArray(data.getRetData(), DocumentCategoryDTO.class);
        return list;
    }

    //获取附件分类-上传附件后弹出选择分类
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    public String getCategory() {
        String url =  baseDocumentUrl + "getCategoryAll?key=" + saleKey;
        String str = restTemplate.getForObject(url, String.class);
        FileData data = JSON.parseObject(str, FileData.class);
        ExceptionValidate.validateFileData(data);
        return data.getData();
    }


    //删除附件
    @RequestMapping(value = "/delectCustomerFile", method = RequestMethod.DELETE)
    public ResponseData delAttachment (@RequestParam Integer id,@RequestParam Integer userId){
        String url = saleUrl + "delectCustomerFile";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("id",id);
        param.add("user_id",userId);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return data;
    }

    //上传附件
    @RequestMapping(value = "/addCustomerFile", method = RequestMethod.POST)
    public Integer addCustomerFile(@RequestBody RContractDocumentDTO rContractDocumentDTO){
        String url =  saleUrl + "addCustomerFile";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("cate_id",rContractDocumentDTO.getCate_id());
        param.add("user_id",rContractDocumentDTO.getUser_id());
        param.add("contract_id",rContractDocumentDTO.getContract_id());
        param.add("customer_id",rContractDocumentDTO.getCustomer_id());
        param.add("image_base64",rContractDocumentDTO.getImage_base64());
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        UploadResponseDTO[] usa2 = JSON.parseObject(data.getRetData(), new TypeReference<UploadResponseDTO[]>(){});
        if (null != usa2 && usa2.length > 0) return usa2[0].getContract_document_id();
        return null;
    }

    //使用附件分类头categoryId更新上传附件分类的
    @RequestMapping(value = "/updateFileCateId", method = RequestMethod.GET)
    public void updateCateIdByDocumentId(@RequestParam Integer documentId,@RequestParam String categoryId, @RequestParam Integer userId, @RequestParam Integer customerId) {
        String url =  baseDocumentUrl + "changeCategoryForMobile?key=" + saleKey + "&id=" + documentId + "&category=" + categoryId + "&user_id=" + userId + "&customer_id=" + customerId;
        String res = restTemplate.getForObject(url, String.class);
        CRMData crmData = JSONObject.parseObject(res, CRMData.class);
        ExceptionValidate.validateCRMData(crmData);
    }



    //协议定金转佣金
    @RequestMapping(value = "/addToReceivable", method = RequestMethod.POST)
    public void addToReceivable(@RequestParam Integer userId,@RequestParam String userName,
                                  @RequestParam Integer agreementId,@RequestParam Integer contractId,
                                  @RequestParam BigDecimal deposit){
        String url = saleUrl + "addToReceivable";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",userId);
        param.add("user_name",userName);
        param.add("agreement_id",agreementId);
        param.add("contract_id",contractId);
        param.add("deposit",deposit);
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //协议设为失败
    @RequestMapping(value = "/updataStatus", method = RequestMethod.POST)
    public void updataStatus(@RequestParam Integer userId,@RequestParam Integer agreementId,
                               @RequestParam Integer status){
        String url = saleUrl + "updataStatus";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",userId);
        param.add("agreement_id",agreementId);
        param.add("status",status);
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
    }

    //获取用户信息
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public PageBeanDTO<UserInfoDTO> getUserInfo(@RequestParam Integer departmentId ,@RequestParam String search,
                                  @RequestParam Integer type,@RequestParam Integer page,
                                  @RequestParam Integer size) throws InvocationTargetException, IllegalAccessException {
        HashMap<String,Object> map = new HashMap<>();
        if(StringUtils.isNotEmpty(search)){
            map.put("name",search);
        }
        if(departmentId != 0){
            map.put("sub_company_id",departmentId);
        }
        String url =  baseUrl + "getUserInfoWhereIsJson";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", baseKey);
        if(map.size()>0){
            String where = JSON.toJSONString(map);
            param.add("where", where);
        }
        param.add("type", type);
        param.add("page", page);
        param.add("size", size);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<UserInfoDTO> pageBeanDTO = new PageBeanDTO<>();
        return JSON.parseObject(data.getRetData(),pageBeanDTO.getClass());
    }

    //合同列表
    @RequestMapping(value = "/contractList",method = RequestMethod.GET)
    public PageBeanDTO<ContractDTO> contractList(@RequestParam(required = false) Integer p , @RequestParam(required = false) Integer contract_type , @RequestParam(required = false) Integer is_special , @RequestParam String users , @RequestParam(required = false) String search,@RequestParam(required = false) Integer agreementId){
        String url;
        if(agreementId == null){
            url = saleUrl + "contractList?key=" + saleKey + "&p="+ p  + "&users=" + users + "&is_special=" + is_special + "&search=" + search + "&contract_type=" + contract_type;
        }else{
            url = saleUrl + "contractList?key=" + saleKey + "&users=" + users + "&agreement_ids=" + agreementId;
        }
        String res = restTemplate.getForObject(url, String.class);
        ResponseData responseData = JSON.parseObject(res, ResponseData.class);
        validateResponse(responseData);
        return JSON.parseObject(responseData.getRetData(),new PageBeanDTO<ContractDTO>().getClass());
    }

    //修改合同
    @RequestMapping(value = "/editContract",method = RequestMethod.POST)
    public void editContract(@RequestBody ContractDTO contractDTO,@RequestParam Integer userId,@RequestParam String userName){
        LOGGER.warn(" 修改合同：userId = " + userId + ", userName = " + userName + ", 信息 = " + ReflectionToStringBuilder.toString(contractDTO));
        String url = saleUrl + "/editContract";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap();
        param.add("key",saleKey);
        param.add("partake_serialize",contractDTO.getPartake_serialize());
        param.add("template_serialize",contractDTO.getTemplate_serialize());
        //param.add("template_serialize","{\"template_id\":\"3\",\"a_name\":\"1\",\"b_name\":\"1\",\"borrow_money_upper\":\"1\",\"borrow_money_lower\":\"147\",\"service_money_upper\":\"1\",\"service_money_lower\":\"1\",\"a_pen_name\":\"1\",\"a_year\":\"1\",\"a_mouth\":\"1\",\"a_day\":\"1\",\"b_pen_name\":\"1\",\"b_year\":\"1\",\"b_mouth\":\"1\",\"b_day\":\"1\"}");
        param.add("contract_id",contractDTO.getContract_id());
        param.add("borrower",contractDTO.getBorrower());
        param.add("identity",contractDTO.getIdentity());
        param.add("phone",contractDTO.getPhone());
        param.add("per_address",contractDTO.getPer_address());
        param.add("address",contractDTO.getAddress());
        param.add("contract_type",contractDTO.getContract_type());
        param.add("product_type",contractDTO.getProduct_type());
        param.add("borrow_money",contractDTO.getBorrow_money());
        param.add("month_rate",contractDTO.getMonth_rate());
        param.add("year_rate",contractDTO.getYear_rate());
        param.add("duration",contractDTO.getDuration());
        param.add("duration_unit",contractDTO.getDuration_unit());
        param.add("s_year_rate",contractDTO.getS_year_rate());
        param.add("s_duration",contractDTO.getS_duration());
        param.add("s_duration_unit",contractDTO.getS_duration_unit());
        param.add("service_money",contractDTO.getService_money());
        param.add("return_fee",contractDTO.getReturn_fee());
        param.add("packing",contractDTO.getPacking());
        param.add("channel_money",contractDTO.getChannel_money());
        param.add("give_money",contractDTO.getGive_money());
        param.add("give_time",contractDTO.getGive_time_str());
        param.add("expire_time",contractDTO.getExpire_time_str());
        param.add("pay_type",contractDTO.getPay_type());
        param.add("house_address",contractDTO.getHouse_address());
        param.add("house_age",contractDTO.getHouse_age());
        param.add("house_area",contractDTO.getHouse_area());
        param.add("house_value",contractDTO.getHouse_value());
        param.add("user_id",userId);
        param.add("user_name",userName);
        param.add("product_name",contractDTO.getProduct_name());
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //重签修改合同
    @RequestMapping(value = "/contractAgainChapterSaveInfo",method = RequestMethod.POST)
    public void contractAgainChapterSaveInfo(@RequestBody ContractDTO contractDTO,@RequestParam Integer userId,@RequestParam String userName){
        String url1 = saleUrl + "/contractAgainChapterSaveInfo";
        LOGGER.warn(" 重签修改合同：userId = " + userId + ", userName = " + userName + ", 信息 = " + ReflectionToStringBuilder.toString(contractDTO));
        MultiValueMap<String,Object> param1 = new LinkedMultiValueMap();
        param1.add("key",saleKey);
        param1.add("partake_serialize",contractDTO.getPartake_serialize());
        param1.add("template_serialize",contractDTO.getTemplate_serialize());
        param1.add("contract_id",contractDTO.getContract_id());
        param1.add("borrower",contractDTO.getBorrower());
        param1.add("identity",contractDTO.getIdentity());
        param1.add("phone",contractDTO.getPhone());
        param1.add("per_address",contractDTO.getPer_address());
        param1.add("address",contractDTO.getAddress());
        param1.add("contract_type",contractDTO.getContract_type());
        param1.add("product_type",contractDTO.getProduct_type());
        param1.add("borrow_money",contractDTO.getBorrow_money());
        param1.add("month_rate",contractDTO.getMonth_rate());
        param1.add("year_rate",contractDTO.getYear_rate());
        param1.add("duration",contractDTO.getDuration());
        param1.add("duration_unit",contractDTO.getDuration_unit());
        param1.add("s_year_rate",contractDTO.getS_year_rate());
        param1.add("s_duration",contractDTO.getS_duration());
        param1.add("s_duration_unit",contractDTO.getS_duration_unit());
        param1.add("service_money",contractDTO.getService_money());
        param1.add("return_fee",contractDTO.getReturn_fee());
        param1.add("packing",contractDTO.getPacking());
        param1.add("channel_money",contractDTO.getChannel_money());
        param1.add("give_money",contractDTO.getGive_money());
        param1.add("give_time",contractDTO.getGive_time_str());
        param1.add("expire_time",contractDTO.getExpire_time_str());
        param1.add("pay_type",contractDTO.getPay_type());
        param1.add("house_address",contractDTO.getHouse_address());
        param1.add("house_age",contractDTO.getHouse_age());
        param1.add("house_area",contractDTO.getHouse_area());
        param1.add("house_value",contractDTO.getHouse_value());
        param1.add("user_id",userId);
        param1.add("user_name",userName);
        param1.add("product_name",contractDTO.getProduct_name());
        String res1 = restTemplate.postForObject(url1, param1, String.class);
        ResponseData data1 = JSON.parseObject(res1, ResponseData.class);
        validateResponse(data1);
        //发送短信验证码
        String url2 = saleUrl + "/sendMobileCodeForContractAgainChapter";
        MultiValueMap<String,Object> param2 = new LinkedMultiValueMap();
        param2.add("key",saleKey);
        param2.add("contract_id",contractDTO.getContract_id());
        param2.add("user_id",userId);
        String res = restTemplate.postForObject(url2, param2, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);

    }

    @RequestMapping(value = "/sendMobileCodeForContractAgainChapter",method = RequestMethod.POST)
    public void sendMobileCodeForContractAgainChapter(@RequestParam Integer contractId,
                                                      @RequestParam Integer userId){
        String url = saleUrl + "/sendMobileCodeForContractAgainChapter";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap();
        param.add("key",saleKey);
        param.add("contract_id",contractId);
        param.add("user_id",userId);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    @RequestMapping(value = "/againChapterContractForApp",method = RequestMethod.POST)
    public void againChapterContractForApp(@RequestParam Integer contractId,
                                           @RequestParam Integer userId,
                                           @RequestParam String code){
        String url = saleUrl + "/againChapterContractForApp";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap();
        param.add("key",saleKey);
        param.add("contract_id",contractId);
        param.add("user_id",userId);
        param.add("code",code);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //获取用户组;
    @RequestMapping(value = "/getSubUserByUserId" ,method = RequestMethod.POST)
    public String getSubUserByUserId(@RequestParam String dataType ,@RequestParam Integer userId){
        String url = baseIndexUrl + "getSubUserByUserId";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",baseKey);
        param.add("user_id",userId);
        param.add("data_type",dataType);
        String res = restTemplate.postForObject(url, param,String.class);
        ResponseData responseData = JSON.parseObject(res, ResponseData.class);
        List<Integer> rs = JSONObject.parseArray(responseData.getRetData(), Integer.class);
        String users = "";
        if (null != rs && rs.size() > 0) {
            String s = rs.toString();
            users = s.substring(1, s.length()-1);
        }

        return users;
    }

    //合同详情
    @RequestMapping(value = "/getContractInfo",method = RequestMethod.GET)
    public ContractDTO getContractInfo(@RequestParam Integer contractId){
        String url = saleUrl + "getContractInfo?key=" + saleKey + "&contract_id=" + contractId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),ContractDTO.class);
    }

    //根据协议获得合同
    @RequestMapping(value = "/getContractListByAgreement",method = RequestMethod.GET)
    public List<ContractDTO> getContractListByAgreement(@RequestParam Integer agreementId){
        String url = saleUrl + "getContractListByAgreement?key=" + saleKey + "&agreement_id="+agreementId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        return JSON.parseArray(data.getRetData(),ContractDTO.class);
    }

    //添加回款
    @RequestMapping(value = "/addReceivables",method = RequestMethod.POST)
    public void addReceivables(@RequestBody ReceivablesDTO receivablesDTO,@RequestParam String userName , @RequestParam Integer userId){
        String url = saleUrl + "addReceivables";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",userId);
        param.add("user_name",userName);
        param.add("contract_id",receivablesDTO.getContract_id());
        param.add("commission",receivablesDTO.getCommission());
        param.add("return_fee",receivablesDTO.getReturn_fee());
        param.add("packing",receivablesDTO.getPacking());
        param.add("channel_money",receivablesDTO.getChannel_money());
        param.add("pay_type",receivablesDTO.getPay_type());
        if(StringUtils.isNotEmpty(receivablesDTO.getPayee())){
        param.add("payee",receivablesDTO.getPayee());
        }
        if(StringUtils.isNotEmpty(receivablesDTO.getPayee_account())){
            param.add("payee_account",receivablesDTO.getPayee_account());
        }
        param.add("pay_time",receivablesDTO.getPay_time_str());
        if (StringUtils.isNotEmpty(receivablesDTO.getMark())){
        param.add("mark",receivablesDTO.getMark());
        }
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //获取"我的"面板数据
    @RequestMapping(value = "/getMine",method = RequestMethod.GET)
    public MineDTO getMineData(@RequestParam Integer userId) {
        String url = saleUrl + "getMine?key=" + saleKey + "&user_id=" + userId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseObject(data.getRetData(), MineDTO.class);
    }

    //判断客户是否上传了房产证或身份证
    @RequestMapping(value = "/checkCustomerIsUpload",method = RequestMethod.GET)
    public Boolean checkCustomerIsUpload(@RequestParam Integer customerId){
        String url = saleUrl + "checkCustomerIsUpload?key=" + saleKey + "&customer_id="+customerId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),Boolean.class);
    }


    /************************审核-start**************************/
    //获取待我审核列表
    @RequestMapping(value = "/myCheckList",method = RequestMethod.GET)
    public PageBeanDTO<CheckDTO> getMyCheckList(@RequestParam Integer userId, @RequestParam Integer page) {
        String url = saleUrl + "myCheckList?key=" + saleKey + "&user_id=" + userId + "&p=" + page;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<CheckDTO> checkSaleDTO = new PageBeanDTO<CheckDTO>();
        return JSONObject.parseObject(data.getRetData(), checkSaleDTO.getClass());
    }
    //我已审批的列表
    @RequestMapping(value = "/myAlreadyCheckList",method = RequestMethod.GET)
    public PageBeanDTO<CheckFinishDTO> getMyCheckFinishList(@RequestParam Integer userId, @RequestParam Integer page) {
        String url = saleUrl + "myAlreadyCheckList?key=" + saleKey + "&user_id=" + userId + "&p=" + page;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<CheckFinishDTO> checkSaleDTO = new PageBeanDTO<CheckFinishDTO>();
        return JSONObject.parseObject(data.getRetData(), checkSaleDTO.getClass());
    }
    //我已提交结案的列表
    @RequestMapping(value = "/myAlreadyBeginList",method = RequestMethod.GET)
    public PageBeanDTO<CheckClosedDTO> getMyCheckClosedList(@RequestParam Integer userId, @RequestParam Integer page) {
        String url = saleUrl + "myAlreadyBeginList?key=" + saleKey + "&user_id=" + userId + "&p=" + page;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<CheckClosedDTO> checkSaleDTO = new PageBeanDTO<CheckClosedDTO>();
        return JSONObject.parseObject(data.getRetData(), checkSaleDTO.getClass());
    }
    //审核操作
    @RequestMapping(value = "/checkAction",method = RequestMethod.POST)
    public void postCheckAction(@RequestBody CheckActionDTO checkActionDTO) {
        String url = saleUrl + "checkAction";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("achievement_id", checkActionDTO.getAchievement_id());
        param.add("contract_id", checkActionDTO.getContract_id());
        param.add("check_process", checkActionDTO.getCheck_process());
        param.add("check_status", checkActionDTO.getCheck_status());
        param.add("suggestion", checkActionDTO.getSuggestion());
        param.add("next_process", checkActionDTO.getNext_process());
        param.add("to_user_id", checkActionDTO.getTo_user_id());
        param.add("user_id", checkActionDTO.getUser_id());
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
    }
    //获取合同的审核记录;
    @RequestMapping(value = "/getCheckListByContractId",method = RequestMethod.GET)
    public List<ContractCheckDTO> getCheckListByContractId(@RequestParam Integer contractId){
        String url = saleUrl + "getCheckListByContractId?key=" + saleKey + "&contract_id="+contractId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseArray(data.getRetData(),ContractCheckDTO.class);
    }

    //结案申请
    @RequestMapping(value = "/referCheckAction",method = RequestMethod.POST)
    public void postReferCheckAction(@RequestBody ReferCheckActionDTO referCheckActionDTO) {
        String url = saleUrl + "referCheckAction";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("contract_id", referCheckActionDTO.getContract_id());
        param.add("check_process", referCheckActionDTO.getCheck_process());
        param.add("check_status", referCheckActionDTO.getCheck_status());
        param.add("suggestion", referCheckActionDTO.getSuggestion());
        param.add("next_process", referCheckActionDTO.getNext_process());
        param.add("to_user_id", referCheckActionDTO.getTo_user_id());
        param.add("user_id", referCheckActionDTO.getUser_id());
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
    }
    //获取审核步骤
    @RequestMapping(value = "/getCheckProcess",method = RequestMethod.GET)
    public ApprovalProcessDTO getApprovalCheckProcess(@RequestParam Integer contract_id, @RequestParam String action) {
        String url = saleUrl + "getCheckProcess?key=" + saleKey + "&contract_id=" + contract_id + "&action=" + action;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseObject(data.getRetData(), ApprovalProcessDTO.class);
    }
    //获取'当时'业绩的-合同、协议、回款信息
    @RequestMapping(value = "/getAchievementInfoById",method = RequestMethod.GET)
    public AchievementInfoDTO getAchievementInfo(@RequestParam Integer achievement_id) {
        String url = saleUrl + "getAchievementInfoById?key=" + saleKey + "&achievement_id=" + achievement_id;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseObject(data.getRetData(), AchievementInfoDTO.class);
    }
    //获取'当时'业绩的-审核流程记录
    @RequestMapping(value = "/getCheckListByAchievementId",method = RequestMethod.GET)
    public List<CheckDTO> getAchievementRecord(@RequestParam Integer achievement_id) {
        String url = saleUrl + "getCheckListByAchievementId?key=" + saleKey + "&achievement_id=" + achievement_id;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseArray(data.getRetData(), CheckDTO.class);
    }
    //审核-我发起的-撤销申请
    @RequestMapping(value = "/cancelCheck",method = RequestMethod.POST)
    public void postCancelCheck(@RequestParam(value = "achievement_id") Integer achievement_id, @RequestParam(value = "user_id") Integer user_id) {
        String url = saleUrl + "cancelCheck";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("user_id", user_id);
        param.add("achievement_id", achievement_id);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
    }
    /************************审核-end**************************/


    //添加协议页面，获取客户列表；
    @RequestMapping(value = "/agreementGetCustomerList",method = RequestMethod.GET)
    public CustomerSale agreementGetCustomerList(@RequestParam String search , @RequestParam Integer p,@RequestParam Integer userId,@RequestParam String type){
        String url = saleUrl + "getCustomerList?" + "key=" + saleKey + "&user_id=" + userId + "&type=" + type + "&p=" + p + "&app_search=" + search + "&perpage=" + 4;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),CustomerSale.class);
    }

    //获取分公司列表;
    @RequestMapping(value = "/getDepartmentByWhere",method = RequestMethod.POST)
    public List<BaseDepartmentDTO> getDepartmentByWhere(@RequestParam String type ,@RequestParam String search){
        String url =  baseUrl + "getDepartmentByWhere";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",baseKey);
        param.add("type",type);
        param.add("where",search);
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        return JSON.parseArray(data.getRetData(),BaseDepartmentDTO.class);
    }

    //合同异常
    @RequestMapping(value = "/contractAbnormalAction",method = RequestMethod.POST)
    public void contractAbnormalAction(@RequestParam Integer userId,@RequestParam String userName,@RequestBody ContractDTO contractDTO){
        String url =  saleUrl + "contractAbnormalAction";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",userId);
        param.add("user_name",userName);
        param.add("contract_id",contractDTO.getContract_id());
        param.add("service_money",contractDTO.getService_money());
        param.add("return_fee",contractDTO.getReturn_fee());
        param.add("packing",contractDTO.getPacking());
        param.add("channel_money",contractDTO.getChannel_money());
        String res = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //通过合同获得回款信息
    @RequestMapping(value = "/getReceivablesListByContractId",method = RequestMethod.GET)
    public List<ReceivablesDTO> getReceivablesListByContractId(@RequestParam Integer contractId){
        String url = saleUrl + "getReceivablesListByContractId?key=" + saleKey + "&contract_id=" + contractId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseArray(data.getRetData(),ReceivablesDTO.class);
    }

    //删除回款
    @RequestMapping(value = "/updateReceivablesStatus",method = RequestMethod.POST)
    public void updateReceivablesStatus(@RequestParam Integer receivablesId, @RequestParam String status ,@RequestParam Integer userId){
        String url = saleUrl + "updateReceivablesStatus";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("id",receivablesId);
        param.add("toStatus",status);
        param.add("user_id",userId);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //获取模板信息 templateType(contract: 合同模板， agreement: 协议模板)
    @RequestMapping(value = "/getTemplates",method = RequestMethod.GET)
    public List<TemplateDTO> getTemplates(@RequestParam String templateType, @RequestParam Integer userId) {
        if (StringUtils.isEmpty(templateType) || userId == 0) throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, "模板类型、用户不能为空");
        String url = saleUrl + "getTemplates?key=" + saleKey + "&keyword=" + templateType + "&user_id=" + userId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        List<TemplateDTO> list = new ArrayList<>();
        try {
            List<TemplateOriginalDTO> templateDTOS = JSONObject.parseArray(data.getRetData(), TemplateOriginalDTO.class);
            if (null != templateDTOS && templateDTOS.size() > 0) {

                TemplateDTO templateDTO = null;
                for (TemplateOriginalDTO templateOriginalDTO : templateDTOS) {
                    templateDTO = new TemplateDTO();
                    BeanUtils.copyProperties(templateOriginalDTO, templateDTO);
                    List<ConfigFieldDTO> configFieldDTOS = getTemplateConfig(templateOriginalDTO.getConfig());
                    Collections.sort(configFieldDTOS);
                    templateDTO.setConfig(configFieldDTOS);
                    list.add(templateDTO);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, CronusException.Type.SYSTEM_CRM_ERROR.getError());
        }
        return list;
    }

    //获取单个模板信息;
    @RequestMapping(value = "/getTemplateInfo",method = RequestMethod.GET)
    public TemplateDTO getTemplateInfo(@RequestParam Integer templateId){
        String url = saleUrl + "getTemplateInfo?key=" + saleKey + "&template_id=" + templateId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        TemplateOriginalDTO templateOriginalDTO = JSON.parseObject(data.getRetData(),TemplateOriginalDTO.class);
        TemplateDTO templateDTO = new TemplateDTO();
        BeanUtils.copyProperties(templateOriginalDTO, templateDTO);
        List<ConfigFieldDTO> configFieldDTOS = getTemplateConfig(templateOriginalDTO.getConfig());
        Collections.sort(configFieldDTOS);
        templateDTO.setConfig(configFieldDTOS);
        return templateDTO;
    }

    //获取产品列表
    @RequestMapping(value = "/getProductList",method = RequestMethod.GET)
    public PageBeanDTO<ProductDTO> getProductList(@RequestParam String search, @RequestParam Integer subCompanyId ,@RequestParam Integer p,@RequestParam Integer size, @RequestParam Integer userId, @RequestParam String token){
        String url = saleUrl + "getProductList?key=" + saleKey + "&search=" + search + "&sub_company_id=" + subCompanyId +"&p=" + p + "&perpage=" + size + "&user_id=" + userId + "&token=" + token;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<ProductDTO> pageBeanDTO = new PageBeanDTO();
        pageBeanDTO = JSON.parseObject(data.getRetData(), pageBeanDTO.getClass());
        return pageBeanDTO;
    }

    //添加普通合同;
    @RequestMapping(value = "/addContract",method = RequestMethod.POST)
    public void addContract(@RequestBody ContractDTO contractDTO,@RequestParam Integer userId,@RequestParam String userName){
        LOGGER.warn(" 添加普通合同：userId = " + userId + ", userName = " + userName + ", 信息 = " + ReflectionToStringBuilder.toString(contractDTO));
        String url = saleUrl + "addContract";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("partake_serialize",contractDTO.getPartake_serialize());
        param.add("template_serialize",contractDTO.getTemplate_serialize());
        param.add("agreement_id",contractDTO.getAgreement_id());
        param.add("borrower",contractDTO.getBorrower());
        param.add("identity",contractDTO.getIdentity());
        param.add("phone",contractDTO.getPhone());
        param.add("per_address",contractDTO.getPer_address());
        param.add("address",contractDTO.getAddress());
        param.add("contract_type",contractDTO.getContract_type());
        param.add("product_type",contractDTO.getProduct_type());
        param.add("borrow_money",contractDTO.getBorrow_money());
        param.add("duration",contractDTO.getDuration());
        param.add("month_rate",contractDTO.getMonth_rate());
        param.add("year_rate",contractDTO.getYear_rate());
        param.add("service_money",contractDTO.getService_money());
        param.add("return_fee",contractDTO.getReturn_fee());
        param.add("packing",contractDTO.getPacking());
        param.add("channel_money",contractDTO.getChannel_money());
        param.add("give_money",contractDTO.getGive_money());
        param.add("give_time",contractDTO.getGive_time_str());
        param.add("expire_time",contractDTO.getExpire_time_str());
        param.add("pay_type",contractDTO.getPay_type());
        param.add("house_address",contractDTO.getHouse_address());
        param.add("house_age",contractDTO.getHouse_age());
        param.add("house_area",contractDTO.getHouse_area());
        param.add("house_value",contractDTO.getHouse_value());
        param.add("user_id",userId);
        param.add("user_name",userName);
        if(contractDTO.getProduct_name()!=null){
            param.add("product_name",contractDTO.getProduct_name());
        }
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
    }

    //添加利差合同
    @RequestMapping(value = "/addSpecialContract",method = RequestMethod.POST)
    public void addSpecialContract(@RequestBody ContractDTO contractDTO,@RequestParam Integer userId,@RequestParam String userName,@RequestParam Integer customerId){
        LOGGER.warn(" 添加利差合同：userId = " + userId + ", userName = " + userName + ", 信息 = " + ReflectionToStringBuilder.toString(contractDTO));
        String url = saleUrl + "addSpecialContract";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("partake_serialize",contractDTO.getPartake_serialize());
        param.add("customer_id",customerId);
        param.add("borrower",contractDTO.getBorrower());
        param.add("identity",contractDTO.getIdentity());
        param.add("phone",contractDTO.getPhone());
        param.add("per_address",contractDTO.getPer_address());
        param.add("address",contractDTO.getAddress());
        param.add("contract_type",contractDTO.getContract_type());
        param.add("product_type",contractDTO.getProduct_type());
        param.add("borrow_money",contractDTO.getBorrow_money());
        param.add("year_rate",contractDTO.getYear_rate());
        param.add("duration",contractDTO.getDuration());
        param.add("duration_unit",contractDTO.getDuration_unit());
        param.add("s_year_rate",contractDTO.getS_year_rate());
        param.add("s_duration",contractDTO.getS_duration());
        param.add("s_duration_unit",contractDTO.getS_duration_unit());
        param.add("service_money",contractDTO.getService_money());
        param.add("return_fee",contractDTO.getReturn_fee());
        param.add("packing",contractDTO.getPacking());
        param.add("channel_money",contractDTO.getChannel_money());
        param.add("give_money",contractDTO.getGive_money());
        param.add("give_time",contractDTO.getGive_time_str());
        param.add("expire_time",contractDTO.getExpire_time_str());
        param.add("pay_type",contractDTO.getPay_type());
        param.add("house_address",contractDTO.getHouse_address());
        param.add("house_age",contractDTO.getHouse_age());
        param.add("house_area",contractDTO.getHouse_area());
        param.add("house_value",contractDTO.getHouse_value());
        param.add("user_id",userId);
        param.add("user_name",userName);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //判断客户是不是特殊渠道来源;是:返回返费支出计算利率,不是:返回数值小于0;
    @RequestMapping(value = "/getCustomerUtmSourceRate",method = RequestMethod.GET)
    public Float getCustomerUtmSourceRate(@RequestParam Integer customerId){
        String url = saleUrl + "getCustomerUtmSourceRate?key=" + saleKey + "&customer_id=" + customerId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),Float.class);
    }


    /**
     * 改变自动分配客户的状态
     * @param userId 当前登录用户id
     * @param customerId 操作的自动分配的客户的id
     */
    @RequestMapping(value = "/clickCommunitButton",method = RequestMethod.POST)
    public void clickCommunitButton(@RequestParam Integer userId,
                                    @RequestParam Integer customerId){
        String url = saleUrl + "clickCommunitButton";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",userId);
        param.add("customer_id",customerId);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //根据当前登录用户id获得用户信息;
    @RequestMapping(value = "/getUserInfoById",method = RequestMethod.GET)
    public UserInfoDTO getUserInfoById(@RequestParam Integer userId){
        Map<String,Object> map = new HashMap();
        map.put("user_id",userId);
        String where = JSON.toJSONString(map);
        String url = baseUrl + "getUserInfoWhereIsJson";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",baseKey);
        param.add("where",where);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),UserInfoDTO.class);
    }

    //获取线上线下签章模式(1-线上签章,0-线下签章)
    @RequestMapping(value = "/getEnableOnLine",method = RequestMethod.GET)
    public Integer getEnableOnLine(Integer userId){
        String url = saleUrl + "getEnableOnLine&key=" + saleKey + "&user_id=" + userId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),Integer.class);
    }

    /**
     * 获取协议签章信息
     * @author xulu
     */
    @RequestMapping(value = "/getSign",method = RequestMethod.GET)
    public ElecAgreementDTO getSign(@RequestParam Integer userId, @RequestParam String agreementId){

        String url = saleUrl + "getAgreementExt?key=" + saleKey + "&agreement_id=" + agreementId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        ElecAgreementDTO dto = JSON.parseObject(data.getRetData(), ElecAgreementDTO.class);
        dto.setKey(saleKey);
        if("1".equals(dto.getIs_chapter())){
            //盖章过，则调用生成的章
            //调用生成的章
            String signUrl = saleUrl + "getSealByAgreement?key=" + saleKey + "&agreement_id="+agreementId;
            String res2 = restTemplate.getForObject(signUrl, String.class);
            ResponseData data2 = JSON.parseObject(res2, ResponseData.class);
            validateResponse(data2);
            Map<String, String> map = JSON.parseObject(data2.getRetData(),Map.class);
            dto.setCustomer_seal(map.get("customer_seal"));
            dto.setCompany_seal(map.get("company_seal"));
        }
        return dto;
    }

    /**
     * 协议签章发送短信
     * @author xulu
     */
    @RequestMapping(value = "/sendSmS",method = RequestMethod.POST)
    public Map<String, String> sendSmS(@RequestBody ElecSignRequestDTO elecSignRequestDTO){
        String url = saleUrl + "agreementChapterSendSignCode";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("agreement_id",elecSignRequestDTO.getAgreementId());
        param.add("user_id",elecSignRequestDTO.getUserId());

        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("result", data.getErrMsg());
        map.put("msg", data.getRetData());
        return map;
    }

    /**
     * 进行协议签章
     * @author xulu
     */
    @RequestMapping(value = "/elecSign",method = RequestMethod.POST)
    public Map<String, String> elecSign(@RequestBody ElecSignRequestDTO elecSignRequestDTO){
        //先调用生成pdf接口
        String PDFUrl = saleUrl + "makeAgreementPdf";
        MultiValueMap<String,Object> param1 = new LinkedMultiValueMap<>();
        param1.add("key", saleKey);
        param1.add("agreement_id", elecSignRequestDTO.getAgreementId());
        param1.add("user_id", elecSignRequestDTO.getUserId());
        String res1 = restTemplate.postForObject(PDFUrl, param1, String.class);
        ResponseData data1 = JSON.parseObject(res1, ResponseData.class);
        validateResponse(data1);
        Map<String, String> map = new HashMap<String, String>();
        //再调用电子盖章
        String url = saleUrl + "agreementChapter";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("agreement_id",elecSignRequestDTO.getAgreementId());
        param.add("user_id",elecSignRequestDTO.getUserId());
        param.add("code", elecSignRequestDTO.getCode());

        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        //调用生成的章
        String signUrl = saleUrl + "getSealByAgreement?key=" + saleKey + "&agreement_id="+elecSignRequestDTO.getAgreementId();
        String res2 = restTemplate.getForObject(signUrl, String.class);
        ResponseData data2 = JSON.parseObject(res2, ResponseData.class);
        validateResponse(data2);
        map = JSON.parseObject(data2.getRetData(),Map.class);
        return map;
    }

    /**
     * 获取合同签章信息
     * @author xulu
     */
    @RequestMapping(value = "/getElecContract",method = RequestMethod.GET)
    public ElecContractDTO getElecContract(@RequestParam String contractId){
        String url = saleUrl + "getContractExt?key=" + saleKey + "&contract_id=" + contractId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        ElecContractDTO dto = JSON.parseObject(data.getRetData(), ElecContractDTO.class);
        dto.setKey(saleKey);
        if("1".equals(dto.getIs_chapter())){
            //盖章过，则调用生成的章
            //调用生成的章
            String signUrl = saleUrl + "getSealByContract?key=" + saleKey + "&contract_id="+contractId;
            String res2 = restTemplate.getForObject(signUrl, String.class);
            ResponseData data2 = JSON.parseObject(res2, ResponseData.class);
            validateResponse(data2);
            Map<String, String> map = JSON.parseObject(data2.getRetData(),Map.class);
            dto.setPerson_sign(map.get("customer_seal"));
            dto.setCompany_sign(map.get("company_seal"));
        }
        LOGGER.warn(" 获取合同签章信息 : " + ReflectionToStringBuilder.toString(data));
        return dto;
    }

    /**
     * 合同签章发送短信
     * @author xulu
     */
    @RequestMapping(value = "/sendElecContractSms",method = RequestMethod.POST)
    public Map<String, String> sendElecContractSms(@RequestBody ElecContractRequestDTO elecContractRequestDTO){
        String url = saleUrl + "contractChapterSendSignCode";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("contract_id",elecContractRequestDTO.getContractId());
        param.add("user_id",elecContractRequestDTO.getUserId());

        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("result", data.getErrMsg());
        map.put("msg", data.getRetData());
        return map;
    }

    /**
     * 进行合同签章
     * @author xulu
     */
    @RequestMapping(value = "/elecContractSign",method = RequestMethod.POST)
    public SignDTO elecContractSign(@RequestBody ElecContractRequestDTO elecContractRequestDTO){
        LOGGER.warn(" 进行合同签章 " + ReflectionToStringBuilder.toString(elecContractRequestDTO));
        //先调用生成pdf接口
        String PDFUrl = saleUrl + "makeContractPdf";
        String contractId = elecContractRequestDTO.getContractId();
        MultiValueMap<String,Object> param1 = new LinkedMultiValueMap<>();
        param1.add("key", saleKey);
        param1.add("contract_id", contractId);
        param1.add("user_id", elecContractRequestDTO.getUserId());
        String res1 = restTemplate.postForObject(PDFUrl, param1, String.class);
        ResponseData data1 = JSON.parseObject(res1, ResponseData.class);
        validateResponse(data1);

        //再调用电子盖章
        String url = saleUrl + "contractChapter";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("contract_id",contractId);
        param.add("user_id",elecContractRequestDTO.getUserId());
        param.add("code", elecContractRequestDTO.getCode());

        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);

        //调用生成的章
        String signUrl = saleUrl + "getSealByContract?key=" + saleKey + "&contract_id="+contractId;
        String res2 = restTemplate.getForObject(signUrl, String.class);
        ResponseData data2 = JSON.parseObject(res2, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data2.getRetData(), SignDTO.class);
    }


    /**
     * 下载合同、协议前检查校验
     * @param attachType type (agreement contract)
     * @param id 合同或者协议的id
     * @return
     */
    @RequestMapping(value = "/validCanDownChapterPdf",method = RequestMethod.GET)
    public String validCanDownChapterPdf(@RequestParam Integer id, @RequestParam String attachType){
        if(StringUtils.isNotEmpty(attachType) && null != id && id.intValue() > 0 ) {
            String validUrl = saleUrl + "validCanDownChapterPdf?key=" + saleKey + "&type=" + attachType + "&id=" + id;
            String res = restTemplate.getForObject(validUrl, String.class);
            ResponseData data = JSON.parseObject(res, ResponseData.class);
            validateResponse(data);
            String downUrl = "";
            if ("contract".equals(attachType)) {
                downUrl = saleUrl + "printContract?"+"key="+saleKey+"&contract_id="+id;
            } else {
                downUrl = saleUrl + "printAgreement?"+"key="+saleKey+"&agreement_id="+id;
            }
            return downUrl;
        } else {
           throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, CronusException.Type.SYSTEM_CRM_ERROR.getError());
        }
    }

    /**
     * 下载合同
     * @param contractId
     * @return
     */
    @RequestMapping(value = "/downloadContract",method = RequestMethod.POST)
    public byte[] downloadContract(@RequestParam Integer contractId){
        //String url = "http://beta-sale.fang-crm.com/Api/App/" + "printContract?"+"key="+saleKey+"&contract_id="+contractId;

        String signUrl = saleUrl + "printContract?"+"key="+saleKey+"&contract_id="+contractId;
//        String res = restTemplate.getForObject(signUrl, String.class);
//        ResponseData data = JSON.parseObject(res, ResponseData.class);
//        validateResponse(data);
        try{
            ByteArrayOutputStream byteArrayOutputStream = DownloadFileUtil.downloadFile1(new URL(signUrl));
            byteArrayOutputStream.toByteArray();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e){
            throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, "下载合同文件失败!");
        }
    }

    /**
     * 下载协议
     * @param agreementId
     * @return
     */
    @RequestMapping(value = "/downloadAgreement",method = RequestMethod.POST)
    public byte[] downloadAgreement(@RequestParam Integer agreementId){
        //String url = "http://beta-sale.fang-crm.com/Api/App/" + "printContract?"+"key="+saleKey+"&contract_id="+contractId;

        String signUrl = saleUrl + "printAgreement?"+"key="+saleKey+"&agreement_id="+agreementId;
//        String res = restTemplate.getForObject(signUrl, String.class);
//        ResponseData data = JSON.parseObject(res, ResponseData.class);
//        validateResponse(data);
        try{
            ByteArrayOutputStream byteArrayOutputStream = DownloadFileUtil.downloadFile1(new URL(signUrl));
            byteArrayOutputStream.toByteArray();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e){
            throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, "下载协议文件失败!");
        }
    }


    //通过id获取用户登录信息
    @RequestMapping(value = "/getUserLoginInfoById",method = RequestMethod.POST)
    public LoginInfoDTO getUserLoginInfoById(@RequestParam Integer userId){
        String url = baseUrl + "getUserLoginInfoById";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",baseKey);
        param.add("system","sale");
        param.add("user_id",userId);
        String res = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),LoginInfoDTO.class);
    }



    /********************************-----产品相关---start----*********************************/
    //通过产品ID获取单个产品信息
    @RequestMapping(value = "/getProductInfoById",method = RequestMethod.GET)
    public ProductDTO getProductInfoById(@RequestParam Integer productId, @RequestParam String productType, @RequestParam String token) {
        String url = saleUrl + "getProductInfo?key=" + saleKey + "&product_id=" + productId + "&product_type=" + productType + "&token=" + token;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseObject(data.getRetData(), ProductDTO.class);
    }
    /********************************-----产品相关---end------*********************************/



    /********************************海贷魔方盘---start----************************************************/
    //获取海贷魔方客户
    @RequestMapping(value = "/getHaidaiList",method = RequestMethod.GET)
    public PageBeanDTO<HaidaiCustomerDTO> getHaidaiList(@RequestParam Integer userId, @RequestParam Integer page,
                                                        @RequestParam String status, @RequestParam String city, @RequestParam String where, @RequestParam Integer id){
        String url = saleUrl + "getHaidaiList?key=" + saleKey + "&user_id=" + userId + "&p=" + page + "&status="
                + status + "&city=" + city + "&where=" + where;

        if (null != id && id.intValue() != 0) {
            url += "&id=" + id;
        }

        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<HaidaiCustomerDTO> pageBeanDTO = new PageBeanDTO();
        return JSON.parseObject(data.getRetData(),pageBeanDTO.getClass());
    }
    //获取海贷魔方的客户信息
    @RequestMapping(value = "/getHaidaiCustomerInfo",method = RequestMethod.GET)
    public HaidaiCustomerDTO getHaidaiCustomerInfo(@RequestParam Integer customerId){
        String url = saleUrl + "getHaidaiCustomerInfo?key=" + saleKey + "&id=" + customerId;

        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),HaidaiCustomerDTO.class);
    }

    //修改海贷魔方客户信息
    @RequestMapping(value="/saveHaidaiInfo", method = RequestMethod.POST)
    public void saveHaidaiInfo(@RequestBody HaidaiCustomerParamDTO paramDTO){
        String url = saleUrl + "saveHaidaiInfo";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",paramDTO.getUser_id());
        param.add("id", paramDTO.getCustomerId());
        param.add("telephone", paramDTO.getTelephone());
        param.add("name", paramDTO.getName());
        param.add("loan_amount", paramDTO.getLoanAmount());
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
    }

    //更新海贷魔方客户的状态(toStatus:-2(无效) 0(正常))
    @RequestMapping(value="/updataHaidaiStatus", method = RequestMethod.POST)
    public void updataHaidaiStatus(@RequestParam Integer customerId, @RequestParam Integer userId, @RequestParam String toStatus){
        String url = saleUrl + "updataHaidaiStatus";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("id",customerId);
        param.add("user_id",userId);
        param.add("toStatus", toStatus);
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
    }

    //海贷魔方客户转入到客户盘(只有正常状态的客户才能转入)
    @RequestMapping(value="/haiDaimoveToSale", method = RequestMethod.POST)
    public void haiDaimoveToSale(@RequestParam Integer customerId, @RequestParam Integer userId){
        String url = saleUrl + "haiDaimoveToSale";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("id",customerId);
        param.add("user_id",userId);
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
    }

    //通过客户id获取海贷魔方可选的订单
    @RequestMapping(value = "/getPullListByCustomerSaleId",method = RequestMethod.GET)
    public List<HaidaiOrderDTO> getPullListByCustomerSaleId(@RequestParam Integer customerId){
        String url = saleUrl + "getPullListByCustomerSaleId?key=" + saleKey + "&customer_id="+customerId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseArray(data.getRetData(),HaidaiOrderDTO.class);
    }

    /********************************海贷魔方盘---end----**************************************************/



    /********************************市场推广盘列表---start----**************************************************/
    //市场推广盘列表  type(public-工作盘 other-沉淀池)市场推广盘有两个盘 工作盘和沉淀池
    @RequestMapping(value = "/getPrdPanCustomerList",method = RequestMethod.GET)
    public List<MarketCustomerDTO> getPrdPanCustomerList(@RequestParam String panType, @RequestParam Integer userId, @RequestParam String customerType,
                                                         @RequestParam String houseStatus, @RequestParam String level, @RequestParam String communStatus,
                                                         @RequestParam String searchKey, @RequestParam Integer id){
        String url = saleUrl + "getPrdPanCustomerList?key=" + saleKey + "&type=" + panType + "&user_id=" + userId + "&customer_type=" + customerType +
                "&house_status=" + houseStatus + "&level=" + level + "&communication_order=" + communStatus + "&search_where=" + searchKey;

        if (null != id && id.intValue() != 0) {
            url += "&id=" + id;
        }

        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseArray(data.getRetData(),MarketCustomerDTO.class);
    }

    //获取市场推广盘客户信息
    @RequestMapping(value = "/getPrdCustomerInfo",method = RequestMethod.GET)
    public MarketCustomerInfoDTO getPrdCustomerInfo(@RequestParam Integer customerId, @RequestParam Integer userId){
        String url = saleUrl + "getPrdCustomerInfo?key=" + saleKey + "&id=" + customerId + "&user_id=" + userId;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(), MarketCustomerInfoDTO.class);
    }

    //添加/删除/转入客户修改  submit 操作类型:(save-修改 delectCustomer-删除 pullToSale-推到客户盘)
    @RequestMapping(value="/addPrdCustomerByAction", method = RequestMethod.POST)
    public void addPrdCustomerByAction(@RequestBody MarketCustomerParamDTO paramDTO){
        String url = saleUrl + "addPrdCustomerByAction";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("user_id",paramDTO.getUserId());
        param.add("id",paramDTO.getId());
        param.add("customer_name",paramDTO.getCustomer_name());
        param.add("customer_type",paramDTO.getCustomer_type());
        param.add("sex",paramDTO.getSex());
        param.add("loan_amount",paramDTO.getLoan_amount());
        param.add("city",paramDTO.getCity());
        param.add("content",paramDTO.getContent());
        param.add("submit",paramDTO.getSubmit());
        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
    }
    /********************************市场推广盘列表---end----****************************************************/



    /**
     * 异常处理sale
     * @param data
     */
    private static void validateResponse(ResponseData data) {
        if (null != data){
            if (!ErrorNumEnum.SUCCESS.getCode().equals(data.getErrNum())){
                if (StringUtils.isNotEmpty(data.getRetData())) {
                    throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, StringAsciiUtil.asciiToString(data.getRetData()));
                } else {
                    throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, CronusException.Type.SYSTEM_CRM_ERROR.getError()+"返回异常");
                }
            }
        }
    }

    /**
     * 异常处理base
     * @param data
     */
    private static void validateResponseBase(ResponseData data) {
        if (null != data){
            if (!ErrorNumEnum.SUCCESS.getCode().equals(data.getErrNum())){
                if (StringUtils.isNotEmpty(data.getErrMsg())) {
                    throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, StringAsciiUtil.asciiToString(data.getErrMsg()));
                } else {
                    throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, CronusException.Type.SYSTEM_CRM_ERROR.getError()+"返回异常");
                }
            }
        }
    }

    private List<ConfigFieldDTO> getTemplateConfig(String config) {
        //截取前后{、}符号
        config = config.replace("\"", "'").substring(1, config.length() - 1);
        //按照"],"切割字符串
        String[] subStr = config.replace("'" ,"").split("],");
        List<ConfigFieldDTO> list = new ArrayList<>();
        ConfigFieldDTO configFieldDTO = null;
        for (String first : subStr) {
            //按照":["切割字符串, 得到表单元素的名字， 如name=""
            // other:[其他约定,,1320,1135,1,970,20,input,11,26
            // a_identity:[甲方身份证号,,440,281,1,180,20,input,11,3]

            String[] parts = first.split("\\:\\[");

            configFieldDTO = new ConfigFieldDTO();
            configFieldDTO.setEleName(parts[0]);

            //得到表单的类型及表单前的显示名词
            String[] part1 = parts[1].replace("]", "").split(",");
            configFieldDTO.setFieldEleName(part1[0]);
            configFieldDTO.setEleValue(part1[1]);
            configFieldDTO.setValidate(part1[4]);
            configFieldDTO.setEleType(part1[7]);
            configFieldDTO.setSort(Integer.valueOf(part1[9]));

            list.add(configFieldDTO);
        }
        return list;
    }


    /**
     * 根据登录的权限信息转换成对象
     * @param authorStr
     * @return
     */
    private List<AuthorityDTO> getLoginAuthor(String authorStr){
        List<AuthorityDTO> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(authorStr)) {
            AuthorityDTO authorityDTO;
            String orinal = authorStr.substring(1, authorStr.length() - 1).replace("\"", "");
            if (StringUtils.isNotEmpty(orinal)) {
                String[] firstStr = orinal.split("},");
                if (null != firstStr && firstStr.length > 0 ) {
                    for (int i = 0; i < firstStr.length; i++) {
                        String[] parent = firstStr[i].split(":\\{");
                        authorityDTO = new AuthorityDTO();
                        authorityDTO.setAuthorUrl(parent[0]);

                        String[] secondStr = parent[1].replaceAll("\\{", "").replaceAll("}", "").split(",");
                        authorityDTO.setActionTitle(secondStr[0].split(":")[1]);
                        authorityDTO.setEnable(Integer.valueOf(secondStr[1].split(":")[1]));
                        list.add(authorityDTO);
                    }
                }
            }
        }
        return list;

    }

}
