package com.fjs.cronus.mappers;

import com.fjs.cronus.dto.CustomerBasicDTO;
import com.fjs.cronus.dto.CustomerPartDTO;
import com.fjs.cronus.model.BusinessPool;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CustomerInfoMapper extends MyMapper<CustomerInfo> {


    List <CustomerInfo> customerList(Map<String,Object> paramMap);

    List <CustomerInfo> bCustomerList(Map<String,Object> paramMap);

    Integer customerListCount(Map<String,Object> paramMap);

    void insertCustomer(CustomerInfo customerInfo);

    CustomerInfo findByFeild(Map<String,Object> paramMap);

    List<CustomerInfo> findCustomerListByFeild(Map<String,Object> paramMap);

    void updateCustomer(CustomerInfo customerInfo);

    void updateCustomerSys(CustomerInfo customerInfo);

    void updateCustomerNonCommunicate(CustomerInfo customerInfo);

    List <CustomerInfo> getListByWhere(Map<String,Object> paramMap);

    List<Integer> findCustomerByType(Map<String,Object> paramMap);

    Integer getListByWhereCount(Map<String,Object> paramMap);

    List<CustomerInfo> findCustomerByOtherCity(Map<String,Object> paramMap);

    List<CustomerInfo> allocationCustomerList(Map<String,Object> paramsMap);

    Integer  allocationCustomerListCount(Map<String,Object> paramsMap);

    List<CustomerInfo> communicatedList(Map<String,Object> paramsMap); //已沟通客户

    Integer communicatedListCount(Map<String,Object> paramsMap);

    List<CustomerInfo> publicOfferList(Map<String,Object> parmsMap);

    Integer publicOfferCount(Map<String,Object> parmsMap);

    List<Integer> selectForAutoClean(Map<String, Object> map);
    /**
     * 根据电话号码查找
     * @param paramMap
     * @return
     */
//    List <CustomerInfo> selectByPhone(Map<String,Object> paramMap);

    /**
     * 根据OCDC电话号码查找
     * @param
     * @return
     */
    List <CustomerInfo> selectByOCDCPhone(Map<String,Object> parmsMap);


    List <CustomerInfo> selectNonCommunicateInTime();

    /**
     * 分组获取客户来源
     * @return
     */
    List<String> customerSourceByGroup();

    public void batchAllocate(Map<String, Object> map);

    public void batchRemove(Map<String, Object> map);

    public void batchUpdate(Map<String,Object> paramsMap);

    public Map<String, Integer> countForAutoClean();

    public List<CustomerInfo> selectByParams(Map<String, Object> telStr);

    List<CustomerInfo> specialListByOffer(Map<String,Object> paramsMap);

    Integer  specialListByOfferCount(Map<String,Object> paramsMap);
    /**
     * Vip渠道客户列表
     */
    List<CustomerInfo> utmCustomerList(Map<String,Object> paramsMap);

    Integer utmCustomerListCount(Map<String,Object> paramsMap);

    void insertBatch(List<CustomerInfo> customerInfos);


    List<CustomerInfo> getNewCustomer(HashMap<String, Object> map);

    //根据客户的手机号码查询一些信息
    CustomerPartDTO selectCustomerDTOByPhone(@Param("phone") String phone);

    CustomerBasicDTO selectCustomerById(@Param("id") Integer id);


    @Select("SELECT c.telephonenumber from customer_info c where c.create_time BETWEEN #{start} and #{end}")
    List<String> getCustomerPhone(@Param("start") String start, @Param("end") String end);

    Integer updateCustomersFromDiscard(Map<String,Object> paramsMap);

    List<CustomerInfo> getCustomerPush(@Param("ownerId") Integer ownerId);

    Integer businessPoolListCount(Map<String, Object> map);

    //将客户设置为已推送
//    @Update("UPDATE customer_info SET is_push = 1 WHERE is_deleted = 0 AND id = #{id}")
//    void updateIsPush(@Param("id") Integer id);

    List<BusinessPool> businessPoolList(Map<String, Object> map);

    //查找商机池客户
    CustomerInfo getCustomer(@Param("customerId") Integer customerId);

//    @Select("SELECT DISTINCT customer_source FROM customer_info WHERE is_deleted = 0 AND own_user_id = -1")
//    List<String> getCustomerSourceList();
//
//    @Select("SELECT DISTINCT utm_source FROM customer_info WHERE is_deleted = 0 AND own_user_id = -1")
//    List<String> getAllUtmSourceList();

}