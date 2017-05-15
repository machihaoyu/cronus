package com.fjs.cronus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fjs.cronus.dto.crm.CRMData;
import com.fjs.cronus.dto.crm.FileData;
import com.fjs.cronus.dto.*;
import com.fjs.cronus.dto.login.AuthorityDTO;
import com.fjs.cronus.dto.login.LoginInfoDTO;
import com.fjs.cronus.dto.param.CustomerSaleParamDTO;
import com.fjs.cronus.enums.ErrorNumEnum;
import com.fjs.cronus.entity.Agreement;
import com.fjs.cronus.entity.CustomerSale;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.exception.ExceptionValidate;
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

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class CrmController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @Autowired
    RestTemplate restTemplate;

    //用户登录
    @RequestMapping(value = "/userLogin", method = RequestMethod.GET)
    public LoginInfoDTO userLogin(@RequestParam String username, @RequestParam String password) {
        String url = baseUrl + "userLoginForApp?key=" + baseKey + "&system=sale&username="+ username +"&password="+ password ;
        logger.info("用户登录信息URL: " + url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("用户登录信息URL返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponseBase(data);
        LoginInfoDTO loginInfoDTO = JSONObject.parseObject(data.getRetData(), LoginInfoDTO.class);
        List<AuthorityDTO> authorLists = getLoginAuthor(loginInfoDTO.getAuthority());
        loginInfoDTO.setAuthorityDTOS(authorLists);
        return loginInfoDTO;
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
        if (null != customerSaleParamDTO.getPerpage() && customerSaleParamDTO.getPerpage().intValue() != 0) {
            url += "&perpage=" + customerSaleParamDTO.getPerpage();
        }
        logger.info("获取客户列表url: " + url);
        String str = restTemplate.getForObject(url, String.class);
        logger.info("获取客户列表url响应: " + str);
        ResponseData responseData = JSON.parseObject(str, ResponseData.class);
        validateResponse(responseData);
        return JSONObject.parseObject(responseData.getRetData(), new PageBeanDTO<CustomerSaleDTO>().getClass());
    }

    //获取客户详情
    @RequestMapping(value = "/getCustomerInfo", method = RequestMethod.GET)
    public CustomerSaleDTO getCustomerInfo(@RequestParam Integer customerId){
        String url = saleUrl + "getCustomerInfo?key=" + saleKey + "&customer_id=" + customerId;
        String str = restTemplate.getForObject(url, String.class);
        logger.info(str);
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
        logger.info("添加|修改客户信息: url = " + url + ", 参数列表 = " + ReflectionToStringBuilder.toString(param));
        String str = restTemplate.postForObject(url,param,String.class);
        logger.info("添加|修改客户信息返回信息: res = " + str);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        return data;
    }

    //新增客户沟通
    @RequestMapping(value = "/addCommunicationLog", method = RequestMethod.POST)
    public ResponseData addCommunicationLog(@RequestBody CommunicationLogDTO communicationLogDTO){
        logger.info("新增客户沟通:　" + ReflectionToStringBuilder.toString(communicationLogDTO));
        String url = saleUrl + "addCommunicationLog";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("customer_id",communicationLogDTO.getCustomer_id());
        param.add("create_user_id",communicationLogDTO.getCreate_user_id());
        param.add("type",communicationLogDTO.getType());
        param.add("content",communicationLogDTO.getContent());
        param.add("next_time",communicationLogDTO.getNext_contact_time());
        param.add("next_contact_content",communicationLogDTO.getNext_contact_content());
        param.add("house_status",communicationLogDTO.getHouse_status());
        param.add("loan_amount",communicationLogDTO.getLoan_amount());
        param.add("meet",communicationLogDTO.getMeet());
        param.add("meet_time",communicationLogDTO.getMeet_time());
        param.add("user_id",communicationLogDTO.getUser_id());
        param.add("user_name", communicationLogDTO.getUser_name());

        String str = restTemplate.postForObject(url,param,String.class);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        return data;
    }


    //保留客户
    @RequestMapping(value = "/retain", method = RequestMethod.POST)
    public ResponseData retain(@RequestParam Integer userId,@RequestParam Integer customerId){
        String url = saleUrl + "retain";
        logger.info("保留客户请求url: " + url);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("user_id",userId);
        param.add("customer_id",customerId);
        logger.info("保留客户请求url[POST]提交参数：key=" + saleKey + ", user_id=" + userId + ", customer_id=" + customerId);
        String res = restTemplate.postForObject(url,param,String.class);
        logger.info("保留客户请求url的返回响应: " + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return data;
    }


    //领取客户
    @RequestMapping(value = "/receiveCustomer", method = RequestMethod.POST)
    public void receiveCustomer(@RequestParam Integer userId,@RequestParam Integer customerId){
        String url = saleUrl + "receiveCustomer";
        logger.info("领取客户请求url: " + url);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", saleKey);
        param.add("user_id",userId);
        param.add("customer_id",customerId);
        logger.info("领取客户请求url[POST]提交参数：key=" + saleKey + ", user_id=" + userId + ", customer_id=" + customerId);
        String res = restTemplate.postForObject(url,param,String.class);
        logger.info("领取客户请求url的返回响应: " + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }


    //获取客户沟通日志
    @RequestMapping(value = "/getCustomerCommunication", method = RequestMethod.GET)
    public List<CommunicationLogDTO> getCustomerCommunication(@RequestParam Integer customerId){
        String url = saleUrl + "getCustomerCommunication?key=" + saleKey + "&customer_id=" + customerId;
        logger.info("获取沟通日志请求url：" + url);
        String str = restTemplate.getForObject(url, String.class);
        logger.info("获取沟通日志请求url返回响应：" + str);
        ResponseData data = JSON.parseObject(str, ResponseData.class);
        validateResponse(data);
        List<CommunicationLogDTO> list = JSON.parseArray(data.getRetData(), CommunicationLogDTO.class);
        return list;
    }

    //获取客户面见记录
    @RequestMapping(value = "/getCustomerMeetLog", method = RequestMethod.GET)
    public List<CustomerMeetDTO> getCustomerMeetLog(@RequestParam Integer customerId){
        String url = saleUrl + "getCustomerMeetLog?key=" + saleKey + "&customer_id=" + customerId;
        logger.info("获取面见记录请求url: " + url);
        String str = restTemplate.getForObject(url, String.class);
        logger.info("获取面见记录请求url返回响应：" + str);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        List<CustomerMeetDTO> list = JSON.parseArray(data.getRetData(), CustomerMeetDTO.class);
        return list;
    }

    //获取客户回访记录
    @RequestMapping(value = "/getCustomerCallbackPhoneLog", method = RequestMethod.GET)
    public List<CallbackLogDTO> getCustomerCallbackPhoneLog(@RequestParam Integer customerId){
        String url = saleUrl + "getCustomerCallbackPhoneLog?key=356a192b7913b06c54574d18c28d46e6395428ab&customer_id=" + customerId;
        logger.info("获取客户回访记录url: " + url);
        String str = restTemplate.getForObject(url, String.class);
        logger.info("获取客户回访记录url返回响应：" + str);
        ResponseData data = JSON.parseObject(str,ResponseData.class);
        validateResponse(data);
        List<CallbackLogDTO> list = JSON.parseArray(data.getRetData(), CallbackLogDTO.class);
        return list;
    }

    //获取客户协议
    @RequestMapping(value = "/getAgreementInfo", method = RequestMethod.GET)
    public AgreementDTO getAgreement(@RequestParam Integer agreementId){
        String url = "http://beta-sale.fang-crm.com/Api/App/getAgreementInfo?key=356a192b7913b06c54574d18c28d46e6395428ab&agreement_id=" + agreementId;
        logger.info("获取客户协议 : url = " + url);
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),AgreementDTO.class);
    }

    //新增客户协议
    @RequestMapping(value = "/addAgreementByCustomer", method = RequestMethod.POST)
    public void addAgreeMent(@RequestBody AgreementDTO agreementDTO) {
        String url = "http://beta-sale.fang-crm.com/Api/App/addAgreementByCustomer";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key","356a192b7913b06c54574d18c28d46e6395428ab");
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
        logger.info("新增修改客户协议 : url = " + url + ", param = " + param.toString());
        String res = restTemplate.postForObject(url,param,String.class);
        logger.info("新增修改客户协议返回值 : res = " + res);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
    }

    //获取居间协议列表
    @RequestMapping(value = "/getAgreementList", method = RequestMethod.GET)
    public Agreement getAgreementList (@RequestParam String users, @RequestParam String search, @RequestParam Integer p){
        String url = "http://beta-sale.fang-crm.com/Api/App/getAgreementList?key=356a192b7913b06c54574d18c28d46e6395428ab&users=" + users
                + "&search=" + search + "&p=" + p;
        logger.info("获取居间协议列表:url="+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("获取居间协议列表返回:res="+res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),Agreement.class);
    }

    //获取客户协议列表
    @RequestMapping(value = "/getAgreementListByCustomerIds", method = RequestMethod.GET)
    public Agreement getAgreementListByCustomerIds(@RequestParam String customerIds, @RequestParam String search, @RequestParam Integer p) {
        String url = "http://beta-sale.fang-crm.com/Api/App/getAgreementList?key=356a192b7913b06c54574d18c28d46e6395428ab&customer_ids=" + customerIds
                + "&search=" + search + "&p=" + p;
        logger.info("获取居间协议列表:url=" + url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("获取居间协议列表返回:res=" + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(), Agreement.class);
    }


    //附件列表
    @RequestMapping(value = "/getCustomerFile", method = RequestMethod.GET)
    public String getCustomerFile (@RequestParam Integer customerId){
        String url = saleUrl + "getDocumentByCustomerId?key=" + saleKey + "&customer_id=" + customerId;
        logger.info("获取附件列表请求url: " + url);
        String str = restTemplate.getForObject(url, String.class);
        logger.info("获取附件列表请求url返回响应：" + str);
        ResponseData data = JSON.parseObject(str, ResponseData.class);
        validateResponse(data);
        return data.getRetData();
    }

    //预览|下载单个附件
    @RequestMapping(value = "/preViewDocument", method = RequestMethod.GET)
    public String preViewDocument(@RequestParam Integer id){
        String url = saleUrl + "preViewDocument?key=" + saleKey + "&id=" + id;
        logger.info("获取下载单个附件请求url: " + url);
        return url;
    }

    //获取附件分类
    @RequestMapping(value = "/getNextCategory", method = RequestMethod.GET)
    public List<DocumentCategoryDTO> getNextCategory(@RequestParam Integer cateId){
        String url = saleUrl + "getNextCategory?key=" + saleKey + "&cate_id=" + cateId;
        logger.info("获取附件分类请求url: " + url);
        String str = restTemplate.getForObject(url, String.class);
        logger.info("获取附件分类请求url返回响应：" + str);
        ResponseData data = JSON.parseObject(str, ResponseData.class);
        validateResponse(data);
        List<DocumentCategoryDTO> list = JSON.parseArray(data.getRetData(), DocumentCategoryDTO.class);
        return list;
    }

    //获取附件分类-上传附件后弹出选择分类
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    public String getCategory() {
        String url = "http://beta-sale.fang-crm.com/Api/Document/getCategoryAll?key=" + saleKey;
        logger.info("获取附件分类-上传附件后弹出选择分类url: " + url);
        String str = restTemplate.getForObject(url, String.class);
        logger.info("获取附件分类-上传附件后弹出选择分类url返回响应：" + str);
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
        logger.info("删除附件：url = " + url + ", 参数对象[ key = " + saleKey + ", id = " + id + ", user_id = " + userId + "]");
        String res = restTemplate.postForObject(url, param, String.class);
        logger.info("删除附件返回的响应：" + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return data;
    }

    //上传附件
    @RequestMapping(value = "/addCustomerFile", method = RequestMethod.POST)
    public Integer addCustomerFile(@RequestBody RContractDocumentDTO rContractDocumentDTO){
        String url =  saleUrl + "addCustomerFile";
        logger.info("上传附件：url = " + url + ", 参数对象 = " + ReflectionToStringBuilder.toString(rContractDocumentDTO));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("cate_id",rContractDocumentDTO.getCate_id());
        param.add("user_id",rContractDocumentDTO.getUser_id());
        param.add("contract_id",rContractDocumentDTO.getContract_id());
        param.add("customer_id",rContractDocumentDTO.getCustomer_id());
        param.add("image_base64",rContractDocumentDTO.getImage_base64());
        String res = restTemplate.postForObject(url, param, String.class);
        logger.info("上传附件：响应 = " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        UploadResponseDTO[] usa2 = JSON.parseObject(data.getRetData(), new TypeReference<UploadResponseDTO[]>(){});
        if (null != usa2 && usa2.length > 0) return usa2[0].getContract_document_id();
        return null;
    }

    //使用附件分类头categoryId更新上传附件分类的
    @RequestMapping(value = "/updateFileCateId", method = RequestMethod.GET)
    public void updateCateIdByDocumentId(@RequestParam Integer documentId,@RequestParam String categoryId) {
        String url =  baseDocumentUrl + "changeCategoryForMobile?key=" + saleKey + "&id=" + documentId + "&category=" + categoryId;
        logger.info("更新上传附件分类的url: " + url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("更新上传附件分类的url返回响应: " + res);
        CRMData crmData = JSONObject.parseObject(res, CRMData.class);
        ExceptionValidate.validateCRMData(crmData);
    }



    //协议定金转佣金
    @RequestMapping(value = "/addToReceivable", method = RequestMethod.POST)
    public void addToReceivable(@RequestParam Integer userId,@RequestParam String userName,
                                  @RequestParam Integer agreementId,@RequestParam Integer contractId,
                                  @RequestParam BigDecimal deposit){
        String url = "http://beta-sale.fang-crm.com/Api/App/addToReceivable";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key","356a192b7913b06c54574d18c28d46e6395428ab");
        param.add("user_id",userId);
        param.add("user_name",userName);
        param.add("agreement_id",agreementId);
        param.add("contract_id",contractId);
        param.add("deposit",deposit);
        logger.info("协议定金转佣金:url=" + url + "param" + param.toString());
        String res = restTemplate.postForObject(url,param,String.class);
        logger.info("协议定金转佣金返回:res=" + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //协议设为失败
    @RequestMapping(value = "/updataStatus", method = RequestMethod.POST)
    public void updataStatus(@RequestParam Integer userId,@RequestParam Integer agreementId,
                               @RequestParam Integer status){
        String url = "http://beta-sale.fang-crm.com/Api/App/updataStatus";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key","356a192b7913b06c54574d18c28d46e6395428ab");
        param.add("user_id",userId);
        param.add("agreement_id",agreementId);
        param.add("status",status);
        logger.info("协议设为失败 : url = " + url + ", param = " + param.toString());
        String res = restTemplate.postForObject(url,param,String.class);
        logger.info("协议设为失败返回值 : res = " + res );
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
        String url =  "http://beta-base.fang-crm.com/Api/App/getUserInfoWhereIsJson";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key", "356o192c191db04c54513b0lc28d46ee63954iab");
        if(map.size()>0){
            String where = JSON.toJSONString(map);
            param.add("where", where);
        }
        param.add("type", type);
        param.add("page", page);
        param.add("size", size);
        logger.info("获取用户信息 : url = " + url + ", param = " +param.toString());
        String res = restTemplate.postForObject(url, param, String.class);
        logger.info("获取用户信息返回值 : res = " + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<UserInfoDTO> pageBeanDTO = new PageBeanDTO<>();
        return JSON.parseObject(data.getRetData(),pageBeanDTO.getClass());
    }

    //合同列表
    @RequestMapping(value = "/contractList",method = RequestMethod.GET)
    public PageBeanDTO<ContractDTO> contractList(@RequestParam Integer p , @RequestParam Integer contract_type , @RequestParam Integer is_special , @RequestParam String users , @RequestParam String search){
        String url = "http://beta-sale.fang-crm.com/Api/App/contractList?key=356a192b7913b06c54574d18c28d46e6395428ab&p="+ p  + "&users=" + users + "&is_special=" + is_special + "&search=" + search + "&contract_type=" + contract_type;
        logger.info("合同列表请求url: " + url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("合同列表请求url返回响应: " + res);
        ResponseData responseData = JSON.parseObject(res, ResponseData.class);
        validateResponse(responseData);
        return JSON.parseObject(responseData.getRetData(),new PageBeanDTO<ContractDTO>().getClass());
    }

    //获取用户组;
    @RequestMapping(value = "/getSubUserByUserId" ,method = RequestMethod.POST)
    public String getSubUserByUserId(@RequestParam String dataType ,@RequestParam Integer userId){
        String url = "http://beta-base.fang-crm.com/Api/Index/getSubUserByUserId";
        logger.info("获取用户组:url="+url+",dataType=" + dataType +",userId="+userId);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key","356o192c191db04c54513b0lc28d46ee63954iab");
        param.add("user_id",userId);
        param.add("data_type",dataType);
        String res = restTemplate.postForObject(url, param,String.class);
        ResponseData responseData = JSON.parseObject(res, ResponseData.class);
        List<Integer> rs = JSONObject.parseArray(responseData.getRetData(), Integer.class);
        String s = rs.toString();
        String users = s.substring(1, s.length()-1);
        logger.info("获取用户组返回：users="+users);
        return users;
    }

    //合同详情
    @RequestMapping(value = "/getContractInfo",method = RequestMethod.GET)
    public ContractDTO getContractInfo(@RequestParam Integer contractId){
        String url = "http://beta-sale.fang-crm.com/Api/App/getContractInfo?key=356a192b7913b06c54574d18c28d46e6395428ab&contract_id=" + contractId;
        logger.info("合同详情:url="+url);
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        logger.info("合同详情返回值:res=" + res);
        return JSON.parseObject(data.getRetData(),ContractDTO.class);
    }

    //根据协议获得合同
    @RequestMapping(value = "/getContractListByAgreement",method = RequestMethod.GET)
    public List<ContractDTO> getContractListByAgreement(@RequestParam Integer agreementId){
        String url = "http://beta-sale.fang-crm.com/Api/App/getContractListByAgreement?key=356a192b7913b06c54574d18c28d46e6395428ab&agreement_id="+agreementId;
        logger.info("根据协议获得合同:url=" + url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("根据协议获得合同返回:res=" + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        return JSON.parseArray(data.getRetData(),ContractDTO.class);
    }

    //添加回款
    @RequestMapping(value = "/addReceivables",method = RequestMethod.POST)
    public void addReceivables(@RequestBody ReceivablesDTO receivablesDTO,@RequestParam String userName , @RequestParam Integer userId){
        String url = saleUrl + "addReceivables";
        logger.info("添加回款:url="+url);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key","356a192b7913b06c54574d18c28d46e6395428ab");
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
        logger.info("添加回款:param="+param.toString());
        String res = restTemplate.postForObject(url, param, String.class);
        logger.info("添加回款返回:res=" +res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //获取"我的"面板数据
    @RequestMapping(value = "/getMine",method = RequestMethod.GET)
    public MineDTO getMineData(@RequestParam Integer userId) {
        String url = saleUrl + "getMine?key=" + saleKey + "&user_id=" + userId;
        logger.info("获取'我的'面板数据url: "+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("获取'我的'面板数据url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseObject(data.getRetData(), MineDTO.class);
    }



    /************************审核-start**************************/
    //获取待我审核列表
    @RequestMapping(value = "/myCheckList",method = RequestMethod.GET)
    public PageBeanDTO<CheckDTO> getMyCheckList(@RequestParam Integer userId, @RequestParam Integer page) {
        String url = saleUrl + "myCheckList?key=" + saleKey + "&user_id=" + userId;
        logger.info("获取'审核'列表数据url: "+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("获取'审核'列表数据url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<CheckDTO> checkSaleDTO = new PageBeanDTO<CheckDTO>();
        return JSONObject.parseObject(data.getRetData(), checkSaleDTO.getClass());
    }
    //我已审批的列表
    @RequestMapping(value = "/myAlreadyCheckList",method = RequestMethod.GET)
    public PageBeanDTO<CheckFinishDTO> getMyCheckFinishList(@RequestParam Integer userId, @RequestParam Integer page) {
        String url = saleUrl + "myAlreadyCheckList?key=" + saleKey + "&user_id=" + userId;
        logger.info("我已审批的列表url: "+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("我已审批的列表url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<CheckFinishDTO> checkSaleDTO = new PageBeanDTO<CheckFinishDTO>();
        return JSONObject.parseObject(data.getRetData(), checkSaleDTO.getClass());
    }
    //我已提交结案的列表
    @RequestMapping(value = "/myAlreadyBeginList",method = RequestMethod.GET)
    public PageBeanDTO<CheckClosedDTO> getMyCheckClosedList(@RequestParam Integer userId, @RequestParam Integer page) {
        String url = saleUrl + "myAlreadyBeginList?key=" + saleKey + "&user_id=" + userId;
        logger.info("我已提交结案的列表url: "+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("我已提交结案的列表返回响应: " + res);
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
        logger.info("审核操作url: "+url + ", 参数：" + ReflectionToStringBuilder.toString(checkActionDTO));
        String res = restTemplate.postForObject(url, param, String.class);
        logger.info("审核操作url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
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
        logger.info("结案申请url: "+url + ", 参数：" + ReflectionToStringBuilder.toString(referCheckActionDTO));
        String res = restTemplate.postForObject(url, param, String.class);
        logger.info("结案申请url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
    }
    //获取审核步骤
    @RequestMapping(value = "/getCheckProcess",method = RequestMethod.GET)
    public ApprovalProcessDTO getApprovalCheckProcess(@RequestParam Integer contract_id, @RequestParam String action) {
        String url = saleUrl + "getCheckProcess?key=" + saleKey + "&contract_id=" + contract_id + "&action=" + action;
        logger.info("获取审核步骤url: "+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("获取审核步骤url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseObject(data.getRetData(), ApprovalProcessDTO.class);
    }
    //获取'当时'业绩的-合同、协议、回款信息
    @RequestMapping(value = "/getAchievementInfoById",method = RequestMethod.GET)
    public AchievementInfoDTO getAchievementInfo(@RequestParam Integer achievement_id) {
        String url = saleUrl + "getAchievementInfoById?key=" + saleKey + "&achievement_id=" + achievement_id;
        logger.info("审核-获取'当时'业绩的url: "+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("审核-获取'当时'业绩的url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseObject(data.getRetData(), AchievementInfoDTO.class);
    }
    //获取'当时'业绩的-审核流程记录
    @RequestMapping(value = "/getCheckListByAchievementId",method = RequestMethod.GET)
    public List<CheckDTO> getAchievementRecord(@RequestParam Integer achievement_id) {
        String url = saleUrl + "getCheckListByAchievementId?key=" + saleKey + "&achievement_id=" + achievement_id;
        logger.info("获取'当时'业绩的-审核流程记录的url: "+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("获取'当时'业绩的-审核流程记录的url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseArray(data.getRetData(), CheckDTO.class);
    }
    //审核-我发起的-撤销申请
    @RequestMapping(value = "/cancelCheck",method = RequestMethod.POST)
    public void postCancelCheck(@RequestParam Integer achievement_id, @RequestParam Integer user_id) {
        String url = saleUrl + "cancelCheck?key=" + saleKey + "&achievement_id=" + achievement_id + "&user_id=" + user_id;
        logger.info("审核-我发起的-撤销申请的url: "+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("审核-我发起的-撤销申请url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
    }
    /************************审核-end**************************/


    //添加协议页面，获取客户列表；
    @RequestMapping(value = "/agreementGetCustomerList",method = RequestMethod.GET)
    public CustomerSale agreementGetCustomerList(@RequestParam String search , @RequestParam Integer p,@RequestParam Integer userId,@RequestParam String type,@RequestParam String users,@RequestParam String dataType){
        String url = saleUrl + "getCustomerList?" + "key=" + saleKey + "&user_id=" + userId + "&type=" + type + "&p=" + p + "&user_ids=" + users + "&data_type=" + dataType + "&app_search=" + search + "&perpage=" + 4;
        //String url = saleUrl + "getCustomerList?" + "key=" + saleKey + "&user_id=" + userId + "&type=" + type + "&p=" + p + "&user_ids=" + users + "&data_type=" + dataType + "&app_search=" + search;
        logger.info("添加协议页面，获取客户列表 : url = " + url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("添加协议页面，获取客户列表返回值 : res = " + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseObject(data.getRetData(),CustomerSale.class);
    }

    //获取分公司列表;
    @RequestMapping(value = "/getDepartmentByWhere",method = RequestMethod.POST)
    public List<BaseDepartmentDTO> getDepartmentByWhere(@RequestParam String type ,@RequestParam String search){
        String url =  baseUrl + "getDepartmentByWhere";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key","356o192c191db04c54513b0lc28d46ee63954iab");
        param.add("type",type);
        param.add("where",search);
        logger.info("获取分公司列表 : url = " + url + ", param = " + param.toString());
        String res = restTemplate.postForObject(url,param,String.class);
        logger.info("获取分公司列表返回值 : res = " + res);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        return JSON.parseArray(data.getRetData(),BaseDepartmentDTO.class);
    }

    //合同异常
    @RequestMapping(value = "/contractAbnormalAction",method = RequestMethod.POST)
    public void contractAbnormalAction(@RequestParam Integer userId,@RequestParam String userName,@RequestBody ContractDTO contractDTO){
        String url =  saleUrl + "contractAbnormalAction";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key","356a192b7913b06c54574d18c28d46e6395428ab");
        param.add("user_id",userId);
        param.add("user_name",userName);
        param.add("contract_id",contractDTO.getContract_id());
        param.add("service_money",contractDTO.getService_money());
        param.add("return_fee",contractDTO.getReturn_fee());
        param.add("packing",contractDTO.getPacking());
        param.add("channel_money",contractDTO.getChannel_money());
        logger.info("合同异常 : url = " + url + ", param = " + param.toString());
        String res = restTemplate.postForObject(url,param,String.class);
        logger.info("合同异常返回值: res = " + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //通过合同获得回款信息
    @RequestMapping(value = "/getReceivablesListByContractId",method = RequestMethod.GET)
    public List<ReceivablesDTO> getReceivablesListByContractId(@RequestParam Integer contractId){
        String url = saleUrl + "getReceivablesListByContractId?key=356a192b7913b06c54574d18c28d46e6395428ab&contract_id=" + contractId;
        logger.info("通过合同获得回款信息 : contractId = " + contractId);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("通过合同获得回款信息返回值 : res = " + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSON.parseArray(data.getRetData(),ReceivablesDTO.class);
    }

    //删除回款
    @RequestMapping(value = "/updateReceivablesStatus",method = RequestMethod.POST)
    public void updateReceivablesStatus(@RequestParam Integer receivablesId, @RequestParam String status ,@RequestParam Integer userId){
        String url = saleUrl + "updateReceivablesStatus";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key","356a192b7913b06c54574d18c28d46e6395428ab");
        param.add("id",receivablesId);
        param.add("toStatus",status);
        param.add("user_id",userId);
        logger.info("删除回款 : url = " + url + ", param = " + param.toString());
        String res = restTemplate.postForObject(url, param, String.class);
        logger.info("删除回款返回值 : res = " + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
    }

    //获取模板信息 templateType(contract: 合同模板， agreement: 协议模板)
    @RequestMapping(value = "/getTemplates",method = RequestMethod.GET)
    public List<TemplateDTO> getTemplates(@RequestParam String templateType, @RequestParam Integer userId) {
        if (StringUtils.isEmpty(templateType) || userId == 0) throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, "模板类型、用户不能为空");
        String url = saleUrl + "getTemplates?key=" + saleKey + "&keyword=" + templateType + "&user_id=" + userId;
        logger.info("获取模板信息 : url = " + url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("获取模板信息返回响应 : res = " + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        List<TemplateDTO> list = new ArrayList<>();
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
        return list;
    }

    //获取产品列表
    @RequestMapping(value = "/getProductList",method = RequestMethod.GET)
    public PageBeanDTO<ProductDTO> getProductList(@RequestParam String productName, @RequestParam String loanTime ,@RequestParam String risk,@RequestParam Integer p,@RequestParam Integer size){
        String url = saleUrl + "getProductList?key=356a192b7913b06c54574d18c28d46e6395428ab&product_name=" + productName + "&loan_time=" + loanTime + "&risk=" + risk +"&p=" + p + "&perpage=" + size;
        logger.info("获取产品列表 : url = " + url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("获取产品列表返回值 : res = " + res);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        PageBeanDTO<ProductDTO> pageBeanDTO = new PageBeanDTO();
        return JSON.parseObject(data.getRetData(),pageBeanDTO.getClass());
    }

    //添加普通合同;
    @RequestMapping(value = "/addContract",method = RequestMethod.POST)
    public void addContract(@RequestBody ContractDTO contractDTO,@RequestParam Integer userId,@RequestParam String userName){
        String url = saleUrl + "addContract";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key","356a192b7913b06c54574d18c28d46e6395428ab");
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
        logger.info("添加普通合同 : url = " + url + ", param = " + param.toString());
        String res = restTemplate.postForObject(url, param, String.class);
        logger.info("添加普通合同返回值 : res = " + res);
        ResponseData data = JSON.parseObject(res,ResponseData.class);
        validateResponse(data);
    }

    //添加利差合同
    @RequestMapping(value = "/addSpecialContract",method = RequestMethod.POST)
    public void addSpecialContract(@RequestBody ContractDTO contractDTO,@RequestParam Integer userId,@RequestParam String userName,@RequestParam Integer customerId){
        String url = saleUrl + "addSpecialContract";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key","356a192b7913b06c54574d18c28d46e6395428ab");
        param.add("partake_serialize",contractDTO.getPartake_serialize());
        param.add("template_serialize",contractDTO.getTemplate_serialize());
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

    /********************************-----产品相关---start----*********************************/
    //通过产品ID获取单个产品信息
    @RequestMapping(value = "/getProductInfoById",method = RequestMethod.GET)
    public ProductDTO getProductInfoById(@RequestParam Integer productId) {
        String url = saleUrl + "getProductInfo?key=" + saleKey + "&product_id=" + productId;
        logger.info("通过产品ID获取单个产品信息url: "+url);
        String res = restTemplate.getForObject(url, String.class);
        logger.info("通过产品ID获取单个产品信息url返回响应: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        return JSONObject.parseObject(data.getRetData(), ProductDTO.class);
    }
    /********************************-----产品相关---end------*********************************/



    /**
     * 异常处理sale
     * @param data
     */
    private static void validateResponse(ResponseData data) {
        if (null != data){
            if (!ErrorNumEnum.SUCCESS.getCode().equals(data.getErrNum())){
                throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, StringAsciiUtil.asciiToString(data.getRetData()));
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
                throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, StringAsciiUtil.asciiToString(data.getErrMsg()));
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
