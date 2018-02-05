package com.fjs.cronus.mappers;

import com.fjs.cronus.api.thea.Loan;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.MyMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CustomerInfoMapper extends MyMapper<CustomerInfo> {


    List <CustomerInfo> customerList(Map<String,Object> paramMap);

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
}