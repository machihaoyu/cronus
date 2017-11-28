package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerSalePushLog;
import com.fjs.cronus.service.client.LoanService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by feng on 2017/9/15.
 */
@Service
public class OcdcService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private ConfigRedisService configRedisService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private CustomerSalePushLogService customerSalePushLogService;

    @Autowired
    RestTemplate restTemplate;

//    @Autowired
//    private AllocateLogService allocateLogService;

//    @Autowired
//    private AutoAllocateService autoAllocateService;
    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    /**
     * 添加OCDC推送客户信息
     */
    @Transactional
    public void addOcdcCustomer(List<Map<String, Object>> listObj) {
        //获取推送数据字段信息
//        String[] logArray = CommonConst.CUSTOMER_SALE_PUSH_LOG;
        //遍历OCDC数据信息
        List<CustomerSalePushLog> customerSalePushLogList = new ArrayList<CustomerSalePushLog>();
        for (Map<String, Object> map : listObj) {
            CustomerSalePushLog customerSalePushLog = this.queryCustomerSalePushLogByOcdcPushData(map);
            customerSalePushLogList.add(customerSalePushLog);
        }
        //保存OCDC推送日志
        customerSalePushLogService.insertList(customerSalePushLogList);
        //获取主要城市及异地城市配置
//        String mainCityStr = configRedisService.getConfigValue(CommonConst.MAIN_CITY);
//        String remoteCityStr = configRedisService.getConfigValue(CommonConst.REMOTE_CITY);
        String mainCityStr = "";
        String remoteCityStr = "";
        String mainAndRemoteCityStr = mainCityStr + remoteCityStr;
        List<CustomerSalePushLog> theaCustomerPushList = new ArrayList<>();
        List<CustomerSalePushLog> serviceCustomerPushList = new ArrayList<>();
        //将推送的列表分成两部分，客户系统（crm）和客服系统
        for (CustomerSalePushLog customerSalePushLog : customerSalePushLogList) {
            if (StringUtils.isNotBlank(customerSalePushLog.getCity())) {
                if (StringUtils.contains(mainAndRemoteCityStr, customerSalePushLog.getCity())) {
                    theaCustomerPushList.add(customerSalePushLog);
                } else {
                    serviceCustomerPushList.add(customerSalePushLog);
                }
            } else {
                serviceCustomerPushList.add(customerSalePushLog);
            }
        }
        //将需要推送至Thea系统的集合转换为Loan集合
//        if (null != theaCustomerPushList && theaCustomerPushList.size() > 0) {
//            List<Loan> ocdcPushList = this.queryLoanListByOcdcPushData(theaCustomerPushList);
//        }
        this.addLoanByOcdcPush(theaCustomerPushList);

    }


    /**
     * 接收从OCDC接收的客户信息
     *
     * @param customerSalePushLogList
     */
    public void addLoanByOcdcPush(List<CustomerSalePushLog> customerSalePushLogList) {
        //先判断输入的数据对象是否已经存在交易
        //根据电话号码查询交易系统数据
        String telListStr = customerSalePushLogList.get(0).getTelephonenumber();
        for (int i = 1; i < customerSalePushLogList.size(); i++) {
            telListStr += "," + customerSalePushLogList.get(i).getTelephonenumber();
        }
        List<CustomerInfo> ocdcPushLoanList = this.queryLoanListByOcdcPushData(customerSalePushLogList);
        Map<String, Object> map = new HashMap<>();
        map.put("inPhone",telListStr);
        List<CustomerInfo> customerInfoList = customerInfoMapper.selectByPhone(map);
        String loanListTelStr = "";
        //系统已存在的交易电话号码集合
        if (null != customerInfoList && customerInfoList.size() > 0) {
            loanListTelStr = customerInfoList.get(0).getTelephonenumber();
            for (int i = 1; i < customerInfoList.size(); i++) {
                loanListTelStr += "," + customerInfoList.get(i).getTelephonenumber();
            }
        }
        List<CustomerInfo> newList = new ArrayList<>();//新客户
        List<CustomerInfo> oldList = new ArrayList<>();//老客户

        for (CustomerInfo customerInfo : ocdcPushLoanList) {
            if (StringUtils.contains(loanListTelStr, customerInfo.getTelephonenumber())) {
                oldList.add(customerInfo);
            } else {
                newList.add(customerInfo);
            }
        }


        //批量处理重复客户申请
        List<Integer> successIds = new ArrayList<>();
        List<Integer> failIds = new ArrayList<>();
        if ((null != customerInfoList && customerInfoList.size() > 0) && (null != oldList && oldList.size() > 0)) {
            //两组集合对比，规划出对应的比例
//            for (Loan ocdcLoan : oldLoanList) {
//                for (Loan loan : loanList) {
//
//                }
//            }
        }
        //批量处理新客户
        if (null != newList && newList.size() > 0) {
//            for (Loan loan : newLoanList) {
//                loan.setOwnUserId(0);
//                loan.setFirstAllocateTime(new Date());
//                Boolean falge =  autoAllocateService.autoAllocate(loan);
//                if (falge) {
//                    successIds.add(loan.getOcdcDataId());
//                } else {
//                    failIds.add(loan.getOcdcDataId());
//                }
//            }
        }
        //回调OCDC

    }

    /**
     * OCDC推送开关
     */
    public void ocdcPushLock() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*String value = configRedisService.getConfigValue(CommonRedisConst.OCDC_PUSH_LOCK);
                if (value.equals(CommonRedisConst.OCDC_PUSH_LOCK_1)) {//向OCDC发送推送延时请求(系统正在处理上一次的数据)
                }*/
            }
        }

        ).start();

    }


    /**
     * 重复的客户是不是需要走自动分配
     */
    /*public Boolean checkRepeatLoanDoAutoAllocate(Loan pushLoan, Loan localLoan) {
        String configValue = configRedisService.getConfigValue(CommonConst.CAN_NOT_ALLOCATE_CUSTOMER_CLASSIFY);
        if (StringUtils.isNotBlank(localLoan.getCustomerClassify())) {
            //重复的客户在特定的时间内之前如果有被自动分配过,就不能再分配,直接让他成功
            //获取配置
            configValue = configRedisService.getConfigValue(CommonConst.R_CUSTOMER_CANNOT_INTO_SALE_TIME);
            //获取该客户的分配日志
            Map<String, Object> map = new HashMap();
            map.put("loanId", localLoan.getId());
            map.put("operation", CommonEnum.LOAN_OPERATION_TYPE_0.getCodeDesc());
            List<AllocateLog> allocateLogList = allocateLogService.selectByParamsMap(map);
            if (null != allocateLogList && allocateLogList.size() > 0) {
                if (null != allocateLogList.get(0).getCreateTime() && StringUtils.isNotBlank(configValue)) {
                    //时间小于配置中的推送时间间隔
                    if ((System.currentTimeMillis() - allocateLogList.get(0).getCreateTime().getTime())
                            <= Integer.valueOf(configValue) * 1000) {
                        //添加日志

                        return true;
                    }
                }
            }
            //对于重复客户的逻辑,如果存在存在负责人,提示,不存在则重新分配
            if (null != localLoan.getOwnUserId() && 0 != localLoan.getOwnUserId()) {
                this.repeatCustomerHasOwnerOperate();
            } else {//否则走自动分配
                pushLoan.setFirstAllocateTime(new Date());
                pushLoan.setOwnUserId(CommonEnum.NO.getCode());
                //调用自动分配逻辑走自动分配
                Boolean flage = autoAllocateService.autoAllocate(pushLoan);
                return flage;
            }
        }
        return false;
    }*/


    /**
     * 根据OCDC推送数据生成交易原始数据
     *
     * @param customerSalePushLogList
     * @return
     */
    public List<CustomerInfo> queryLoanListByOcdcPushData(List<CustomerSalePushLog> customerSalePushLogList) {
        List<CustomerInfo> customerInfoList = new ArrayList<>();
        /*for (CustomerSalePushLog customerSalePushLog : customerSalePushLogList) {
            Loan loan = new Loan();
            if (null != customerSalePushLog.getCustomerId()) {
                loan.setCustomerId(customerSalePushLog.getCustomerId());
            }
            loan.setStatus(CommonEnum.LOAN_STATUE_1.getCode());
            if (null != customerSalePushLog.getLoanAmount()) {
                loan.setLoanAmount(customerSalePushLog.getLoanAmount());
            }
            if (null != customerSalePushLog.getTelephonenumber()) {
                loan.setTelephonenumber(customerSalePushLog.getTelephonenumber());
            }
            if (null != customerSalePushLog.getCustomerName()) {
                loan.setCustomerName(customerSalePushLog.getCustomerName());
            }
            if (null != customerSalePushLog.getCustomerLevel()) {
                loan.setLevel(customerSalePushLog.getCustomerLevel());
            } else {
                loan.setLevel(CommonEnum.CUSTOMER_LEVEL_0.getCodeDesc());
            }
            if (null != customerSalePushLog.getHouseStatus()) {
                loan.setHouseStatus(customerSalePushLog.getHouseStatus());
            }
            if (null != customerSalePushLog.getRetain()) {
                loan.setRemain(customerSalePushLog.getRetain());
            }
            loan.setIsLock(CommonEnum.NO.getCode());
            loan.setAutostatus(CommonEnum.YES.getCode());
            if (null != customerSalePushLog.getCustomerSource()) {
                loan.setCustomerSource(customerSalePushLog.getCustomerSource());
            }
            if (null != customerSalePushLog.getUtmSource()) {
                loan.setUtmSource(customerSalePushLog.getUtmSource());
            }
            if (null != customerSalePushLog.getCustomerClassify()) {
                loan.setCustomerClassify(customerSalePushLog.getCustomerClassify());
            }
            if (null != customerSalePushLog.getCity()) {
                loan.setCity(customerSalePushLog.getCity());
            }
            loan.setClickCommunicateButton(CommonEnum.NO.getCode());
            loan.setCreateTime(new Date());
            loan.setLastUpdateTime(new Date());
            if (null != customerSalePushLog.getExt()) {
                loan.setExt(customerSalePushLog.getExt());
            }
            if(null != customerSalePushLog.getOcdcId()){
                loan.setOcdcDataId(customerSalePushLog.getOcdcId());
            }
            loanList.add(loan);
        }*/
        return customerInfoList;
    }

    /**
     * OCDC推送对象转换
     *
     * @param map
     * @return
     */
    public CustomerSalePushLog queryCustomerSalePushLogByOcdcPushData(Map<String, Object> map) {
        CustomerSalePushLog customerSalePushLog = new CustomerSalePushLog();
        try {
            if (null != map.get("id") && StringUtils.isNotBlank(map.get("id").toString())) {
                customerSalePushLog.setOcdcId(Integer.valueOf(map.get("id").toString()));
            } else {
                customerSalePushLog.setOcdcId(CommonEnum.NO.getCode());
            }
            if (null != map.get("customer_id") && StringUtils.isNotBlank(map.get("customer_id").toString())) {
                customerSalePushLog.setCustomerId((Integer) map.get("customer_id"));
            } else {
                customerSalePushLog.setCustomerId(CommonEnum.NO.getCode());
            }
            customerSalePushLog.setLoanId(CommonEnum.NO.getCode());
            if (null != map.get("telephonenumber") && StringUtils.isNotBlank(map.get("telephonenumber").toString())) {
                customerSalePushLog.setTelephonenumber(map.get("telephonenumber").toString());
            }
            //customer_name
            if (null != map.get("name") && StringUtils.isNotBlank(map.get("name").toString())) {
                customerSalePushLog.setCustomerName(map.get("name").toString());
            } else {
                customerSalePushLog.setCustomerName(CommonConst.DEFAULT_CUSTOMER_NAME + map.get("data_id").toString());
            }
            if (null != map.get("owner_user_id") && StringUtils.isNotBlank(map.get("owner_user_id").toString())) {
                customerSalePushLog.setOwnerUserId((Integer) map.get("owner_user_id"));
            } else {
                customerSalePushLog.setOwnerUserId(CommonEnum.NO.getCode());
            }
            if (null != map.get("owner_user_name") && StringUtils.isNotBlank(map.get("owner_user_name").toString())) {
                customerSalePushLog.setOwnerUserName(map.get("owner_user_name").toString());
            }
            if (null != map.get("creater_user_id") && StringUtils.isNotBlank(map.get("creater_user_id").toString())) {
                customerSalePushLog.setCreaterUserId((Integer) map.get("creater_user_id"));
            } else {
                customerSalePushLog.setCreaterUserId(CommonEnum.NO.getCode());
            }
            if (null != map.get("customer_level") && StringUtils.isNotBlank(map.get("customer_level").toString())) {
                customerSalePushLog.setCustomerLevel(map.get("customer_level").toString());
            } else {
                customerSalePushLog.setCustomerLevel(CommonEnum.CUSTOMER_LEVEL_0.getCodeDesc());
            }
            if (null != map.get("loan_amount") && StringUtils.isNotBlank(map.get("loan_amount").toString())) {
                customerSalePushLog.setLoanAmount(new BigDecimal(map.get("loan_amount").toString()));
            }
            if (null != map.get("spare_phone") && StringUtils.isNotBlank(map.get("spare_phone").toString())) {
                customerSalePushLog.setSparePhone(map.get("spare_phone").toString());
            }
            if (null != map.get("age") && StringUtils.isNotBlank(map.get("age").toString())) {
                customerSalePushLog.setAge(map.get("age").toString());
            }
            if (null != map.get("marriage") && StringUtils.isNotBlank(map.get("marriage").toString())) {
                customerSalePushLog.setMarriage(map.get("marriage").toString());
            }
            if (null != map.get("id_card") && StringUtils.isNotBlank(map.get("id_card").toString())) {
                customerSalePushLog.setIdCard(map.get("id_card").toString());
            }
            if (null != map.get("province_huji") && StringUtils.isNotBlank(map.get("province_huji").toString())) {
                customerSalePushLog.setProvinceHuji(map.get("province_huji").toString());
            }
            if (null != map.get("sex") && StringUtils.isNotBlank(map.get("sex").toString())) {
                customerSalePushLog.setSex(map.get("sex").toString());
            } else {
                customerSalePushLog.setSex(CommonEnum.SEX_MALE.getCodeDesc());
            }
            if (null != map.get("customer_address") && StringUtils.isNotBlank(map.get("customer_address").toString())) {
                customerSalePushLog.setCustomerAddress(map.get("customer_address").toString());
            }
            if (null != map.get("per_description") && StringUtils.isNotBlank(map.get("per_description").toString())) {
                customerSalePushLog.setPerDescription(map.get("per_description").toString());
            }
            if (null != map.get("house_amount") && StringUtils.isNotBlank(map.get("house_amount").toString())) {
                customerSalePushLog.setHouseAmount(map.get("house_amount").toString());
            }
            if (null != map.get("house_type") && StringUtils.isNotEmpty(map.get("house_type").toString())){
                customerSalePushLog.setHouseType(map.get("house_type").toString());
            }
            if (null != map.get("house_value") && StringUtils.isNotBlank(map.get("house_value").toString())) {
                customerSalePushLog.setHouseValue(map.get("house_value").toString());
            }
            if (null != map.get("house_area") && StringUtils.isNotBlank(map.get("house_area").toString())) {
                customerSalePushLog.setHouseArea(map.get("house_area").toString());
            }
            if (null != map.get("house_age") && StringUtils.isNotBlank(map.get("house_age").toString())) {
                customerSalePushLog.setHouseAge(map.get("house_age").toString());
            }
            if (null != map.get("house_loan") && StringUtils.isNotBlank(map.get("house_loan").toString())) {
                customerSalePushLog.setHouseLoan(map.get("house_loan").toString());
            } else {
                customerSalePushLog.setHouseLoan(CommonEnum.HOUSE_LOAN_0.getCodeDesc());
            }
            if (null != map.get("house_alone") && StringUtils.isNotBlank(map.get("house_alone").toString())) {
                customerSalePushLog.setHouseAlone(map.get("house_alone").toString());
            } else {
                customerSalePushLog.setHouseAlone(CommonEnum.HOUSE_ALONE_0.getCodeDesc());
            }
            if (null != map.get("house_location") && StringUtils.isNotBlank(map.get("house_location").toString())) {
                customerSalePushLog.setHouseLoan(map.get("house_location").toString());
            }
            if (null != map.get("city") && StringUtils.isNotBlank(map.get("city").toString())) {
                customerSalePushLog.setCity(map.get("city").toString());
            }
            if (null != map.get("retain") && StringUtils.isNotBlank(map.get("retain").toString())) {
                customerSalePushLog.setRetain((Integer) map.get("retain"));
            } else {
                customerSalePushLog.setRetain(CommonEnum.RETAIN_STATUE_0.getCode());
            }
            customerSalePushLog.setCreateTime(new Date());
            customerSalePushLog.setUpdateTime(new Date());
            customerSalePushLog.setReceiveTime(new Date());
            if (null != map.get("is_lock") && StringUtils.isNotBlank(map.get("is_lock").toString())) {
                customerSalePushLog.setIsLock((Integer) map.get("is_lock"));
            } else {
                customerSalePushLog.setIsLock(CommonEnum.NO.getCode());
            }
            if (null != map.get("phone_view_time") && StringUtils.isNotBlank(map.get("phone_view_time").toString())) {
                customerSalePushLog.setPhoneViewTime((Date) map.get("phone_view_time"));
            }
            if (null != map.get("phone_view_uid") && StringUtils.isNotBlank(map.get("phone_view_uid").toString())) {
                customerSalePushLog.setPhoneViewUid((Integer) map.get("phone_view_uid"));
            } else {
                customerSalePushLog.setPhoneViewUid(CommonEnum.NO.getCode());
            }
            if (null != map.get("phone_view_count") && StringUtils.isNotBlank(map.get("phone_view_count").toString())) {
                customerSalePushLog.setPhoneViewCount((Integer) map.get("phone_view_count"));
            } else {
                customerSalePushLog.setPhoneViewCount(CommonEnum.NO.getCode());
            }
            if (null != map.get("autostatus") && StringUtils.isNotBlank(map.get("autostatus").toString())) {
                customerSalePushLog.setAutostatus((Integer) map.get("autostatus"));
            } else {
                customerSalePushLog.setAutostatus(CommonEnum.YES.getCode());
            }
            if (null != map.get("utm_source") && StringUtils.isNotBlank(map.get("utm_source").toString())) {
                customerSalePushLog.setUtmSource(map.get("utm_source").toString());
            }
            if (null != map.get("customer_source") && StringUtils.isNotBlank(map.get("customer_source").toString())) {
                customerSalePushLog.setCustomerSource(map.get("customer_source").toString());
            }
            if (null != map.get("customer_classify") && StringUtils.isNotBlank(map.get("customer_classify").toString())) {
                customerSalePushLog.setCustomerClassify(map.get("customer_classify").toString());
            } else {
                customerSalePushLog.setCustomerClassify(CommonEnum.CUSTOMER_CLASSIFY_1.getCodeDesc());
            }
            if (null != map.get("laiyuan") && StringUtils.isNotBlank(map.get("laiyuan").toString())) {
                customerSalePushLog.setLaiyuan((Integer) map.get("laiyuan"));
            } else {
                customerSalePushLog.setLaiyuan(CommonEnum.LAIYUAN_0.getCode());
            }
            //ext
            if (null != map.get("exend_text") && StringUtils.isNotBlank(map.get("exend_text").toString())) {
                customerSalePushLog.setExt(map.get("exend_text").toString());
            }
            if (null != map.get("repeat_callback_time") && StringUtils.isNotBlank(map.get("repeat_callback_time").toString())) {
                customerSalePushLog.setRepeatCallbackTime((Date) map.get("repeat_callback_time"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("--------------------------OCDC转CustomerPush失败！！");
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        }
        return customerSalePushLog;
    }

    public static void main(String[] args) {
//        JSONObject json = new JSONObject();
//        json.put("customer_source","qudao");
//        json.put("utm_source","juhkl");
//        json.put("id",10001);
//        json.put("telephonenumber","13866549800");
//        json.put("name","dng");
//        json.put("loan_amount","1000");
//        json.put("city","上海");
//
//        JSONObject json2 = new JSONObject();
//        json2.put("customer_source","qudao");
//        json2.put("utm_source","juhkl");
//        json2.put("id",10002);
//        json2.put("telephonenumber","13866549801");
//        json2.put("name","dng");
//        json2.put("loan_amount","1000");
//        json2.put("city","上海");
//
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.add(json);
//        jsonArray.add(json2);
//
//        JSONObject json1= new JSONObject();
//        json1.put("data",jsonArray);
//
//        System.out.println(json1.toString());

        String test = "{\"data\":[{\"telephonenumber\":\"13866549800\",\"city\":\"上海\",\"name\":\"dng\",\"customer_source\":\"qudao\",\"loan_amount\":\"1000\",\"id\":10001,\"utm_source\":\"juhkl\"}," +
                "{\"telephonenumber\":\"13866549801\",\"city\":\"上海\",\"name\":\"dng\",\"customer_source\":\"qudao\",\"loan_amount\":\"1000\",\"id\":10002,\"utm_source\":\"juhkl\"}]}";
        JSONObject jsonObject = JSON.parseObject(test);
        String list = jsonObject.get("data").toString();
        JSONArray jarr=JSONArray.parseArray(list);
//        System.out.println(jarr);
        int arraySize = jarr.size();
        for (int i = 0 ;i < arraySize;i++){
            String jsonObject1 = jarr.get(i).toString();
            System.out.println(jsonObject1);
        }
    }
}
