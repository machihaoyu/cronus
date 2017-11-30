package com.fjs.cronus.util;

import com.fjs.cronus.dto.cronus.*;
import com.fjs.cronus.dto.ocr.*;
import com.fjs.cronus.model.*;
import com.github.pagehelper.StringUtil;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by msi on 2017/9/14.
 */
public class EntityToDto {


    public static void customerEntityToCustomerListDto(CustomerInfo customerInfo, CustomerListDTO dto){

        dto.setId(customerInfo.getId());
        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            dto.setTelephonenumber(customerInfo.getTelephonenumber());
        }
        if (customerInfo.getLoanAmount() != null){
            dto.setLoanAmount(customerInfo.getLoanAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseStatus())){
            dto.setHouseStatus(customerInfo.getHouseStatus());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerSource())){
            dto.setCustomerSource(customerInfo.getCustomerSource());
        }
        if (!StringUtils.isEmpty(customerInfo.getUtmSource())){
            dto.setUtmSource(customerInfo.getUtmSource());
        }
        if (!StringUtils.isEmpty(customerInfo.getCity())){
            dto.setCity(customerInfo.getCity());
        }
        if (!StringUtil.isEmpty(customerInfo.getOwnUserName())){
            dto.setOwnUserName(customerInfo.getOwnUserName());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(customerInfo.getCreateTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getReceiveTime())){
            dto.setReceiveTime(customerInfo.getReceiveTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getRemain())){
            dto.setRemain(customerInfo.getRemain());
        }
        if (!StringUtils.isEmpty(customerInfo.getLevel())){
            dto.setLevel(customerInfo.getLevel());
        }
        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            dto.setLast_update_time(customerInfo.getLastUpdateTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getConfirm())){
            dto.setConfirm(customerInfo.getConfirm());
        }
        if (!StringUtils.isEmpty(customerInfo.getCommunicateTime())){
            dto.setCommunicateTime(customerInfo.getCommunicateTime());
        }


    }

    public static void customerEntityToCustomerDto(CustomerInfo customerInfo, CustomerDTO dto){

        dto.setId(customerInfo.getId());
        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            dto.setTelephonenumber(customerInfo.getTelephonenumber());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerName())){
            dto.setCustomerName(customerInfo.getCustomerName());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerLevel())){
            dto.setCustomerLevel(customerInfo.getCustomerLevel());
        }
        if (!StringUtils.isEmpty(customerInfo.getSparePhone())){
            dto.setSparePhone(customerInfo.getSparePhone());
        }
        if (!StringUtils.isEmpty(customerInfo.getAge())){
            dto.setAge(customerInfo.getAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getMarriage())){
            dto.setMarriage(customerInfo.getMarriage());
        }
        if (!StringUtils.isEmpty(customerInfo.getIdCard())){
            dto.setIdCard(customerInfo.getIdCard());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvinceHuji())){
            dto.setProvinceHuji(customerInfo.getProvinceHuji());
        }

        if (!StringUtils.isEmpty(customerInfo.getSex())){
            dto.setSex(customerInfo.getSex());
        }

        if (!StringUtils.isEmpty(customerInfo.getCustomerAddress())){
            dto.setCustomerAddress(customerInfo.getCustomerAddress());
        }

        if (!StringUtils.isEmpty(customerInfo.getPerDescription())){
            dto.setPerDescription(customerInfo.getPerDescription());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseStatus())){
            dto.setHouseStatus(customerInfo.getHouseStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAmount())){
            dto.setHouseAmount(customerInfo.getHouseAmount());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseType())){
            dto.setHouseType(customerInfo.getHouseType());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseValue())){
            dto.setHouseValue(customerInfo.getHouseValue());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseArea())){
            dto.setHouseArea(customerInfo.getHouseArea());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAge())){
            dto.setHouseAge(customerInfo.getHouseAge());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLoan())){
            dto.setHouseLoan(customerInfo.getHouseLoan());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLocation())){
            dto.setHouseLocation(customerInfo.getHouseLocation());
        }

        if (!StringUtils.isEmpty(customerInfo.getCity())){
            dto.setCity(customerInfo.getCity());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerClassify())){
            dto.setCustomerClassify(customerInfo.getCustomerClassify());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackStatus())){
            dto.setCallbackStatus(customerInfo.getCallbackStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackTime())){
            //date 转
            dto.setCallbackTime(customerInfo.getCallbackTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getSubCompanyId())){
            dto.setSubCompanyId(customerInfo.getSubCompanyId());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(customerInfo.getCreateTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            dto.setLastUpdateTime(customerInfo.getLastUpdateTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            dto.setCreateUser(customerInfo.getCreateUser());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateUser())){
            dto.setLastUpdateUser(customerInfo.getLastUpdateUser());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsDeleted())){
            dto.setIsDeleted(customerInfo.getIsDeleted());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseAlone())){
            dto.setHouseAlone(customerInfo.getHouseAlone());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(customerInfo.getCreateTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerType())){
            dto.setCustomerType(customerInfo.getCustomerType());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerStreet())){
            dto.setCustomerStreet(customerInfo.getCustomerStreet());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseLoanValue())){
            dto.setHouseLoanValue(customerInfo.getHouseLoanValue());
        }
        //新版新增加字段
        if (!StringUtils.isEmpty(customerInfo.getExpectMoneyTime())){
            dto.setExpectMoneyTime(customerInfo.getExpectMoneyTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getExpectLoanTime())){
            dto.setExpectLoanTime(customerInfo.getExpectLoanTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getExpectRepaymentWay())){
            dto.setExpectRepaymentWay(customerInfo.getExpectRepaymentWay());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseClear())){
            dto.setHouseClear(customerInfo.getHouseClear());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseOwner())){
            dto.setHouseOwner(customerInfo.getHouseOwner());
        }
        if (!StringUtils.isEmpty(customerInfo.getMortgageAmount())){
            dto.setMortgageAmount(customerInfo.getMortgageAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getMortgaeMonth())){
            dto.setMortgaeMonth(customerInfo.getMortgaeMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsHavaCar())){
            dto.setIsHavaCar(customerInfo.getIsHavaCar());
        }
        if (!StringUtils.isEmpty(customerInfo.getCarWorth())){
            dto.setCarWorth(customerInfo.getCarWorth());
        }
        if (!StringUtils.isEmpty(customerInfo.getCarAge())){
            dto.setCarAge(customerInfo.getCarAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsHavaInsurance())){
            dto.setIsHavaInsurance(customerInfo.getIsHavaInsurance());
        }
        if (!StringUtils.isEmpty(customerInfo.getYearPayAmount())){
            dto.setYearPayAmount(customerInfo.getYearPayAmount());
        }

        if (!StringUtils.isEmpty(customerInfo.getInsuranceTime())){
            dto.setInsuranceTime(customerInfo.getInsuranceTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsHavaDebt())){
            dto.setIsHavaDebt(customerInfo.getIsHavaDebt());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtMoney())){
            dto.setDebtMoney(customerInfo.getDebtMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtMonth())){
            dto.setDebtMonth(customerInfo.getDebtMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtOverdue())){
            dto.setDebtOverdue(customerInfo.getDebtOverdue());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtOverdueMoney())){
            dto.setDebtOverdueMoney(customerInfo.getDebtOverdueMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtTime())){
            dto.setDebtTime(customerInfo.getDebtTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getWorkStatus())){
            dto.setWorkStatus(customerInfo.getWorkStatus());
        }
        if (!StringUtils.isEmpty(customerInfo.getWagerCard())){
            dto.setWagerCard(customerInfo.getWagerCard());
        }
        if (!StringUtils.isEmpty(customerInfo.getEntryTime())){
            dto.setEntryTime(customerInfo.getEntryTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getCompanyType())){
            dto.setCompanyType(customerInfo.getCompanyType());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialSecurity())){
            dto.setSocialSecurity(customerInfo.getSocialSecurity());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialMoney())){
            dto.setSocialMoney(customerInfo.getSocialMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialTime())){
            dto.setSocialTime(customerInfo.getSocialTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvidentFund())){
            dto.setProvidentFund(customerInfo.getProvidentFund());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvidentMoney())){
            dto.setProvidentMoney(customerInfo.getProvidentMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvidentTime())){
            dto.setProvidentTime(customerInfo.getProvidentTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getCompnyName())){
            dto.setCompnyName(customerInfo.getCompnyName());
        }
        if (!StringUtils.isEmpty(customerInfo.getFixedPhone())){
            dto.setFixedPhone(customerInfo.getFixedPhone());
        }


    }

    public static void customerCustomerDtoToEntity(CustomerDTO customerInfo, CustomerInfo dto){

        dto.setId(customerInfo.getId());
        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            dto.setTelephonenumber(customerInfo.getTelephonenumber());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerName())){
            dto.setCustomerName(customerInfo.getCustomerName());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerLevel())){
            dto.setCustomerLevel(customerInfo.getCustomerLevel());
        }
        if (!StringUtils.isEmpty(customerInfo.getSparePhone())){
            dto.setSparePhone(customerInfo.getSparePhone());
        }
        if (!StringUtils.isEmpty(customerInfo.getAge())){
            dto.setAge(customerInfo.getAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getMarriage())){
            dto.setMarriage(customerInfo.getMarriage());
        }
        if (!StringUtils.isEmpty(customerInfo.getIdCard())){
            dto.setIdCard(customerInfo.getIdCard());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvinceHuji())){
            dto.setProvinceHuji(customerInfo.getProvinceHuji());
        }

        if (!StringUtils.isEmpty(customerInfo.getSex())){
            dto.setSex(customerInfo.getSex());
        }

        if (!StringUtils.isEmpty(customerInfo.getCustomerAddress())){
            dto.setCustomerAddress(customerInfo.getCustomerAddress());
        }

        if (!StringUtils.isEmpty(customerInfo.getPerDescription())){
            dto.setPerDescription(customerInfo.getPerDescription());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseStatus())){
            dto.setHouseStatus(customerInfo.getHouseStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAmount())){
            dto.setHouseAmount(customerInfo.getHouseAmount());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseType())){
            dto.setHouseType(customerInfo.getHouseType());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseValue())){
            dto.setHouseValue(customerInfo.getHouseValue());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseArea())){
            dto.setHouseArea(customerInfo.getHouseArea());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAge())){
            dto.setHouseAge(customerInfo.getHouseAge());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLoan())){
            dto.setHouseLoan(customerInfo.getHouseLoan());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLocation())){
            dto.setHouseLocation(customerInfo.getHouseLocation());
        }

        if (!StringUtils.isEmpty(customerInfo.getCity())){
            dto.setCity(customerInfo.getCity());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerClassify())){
            dto.setCustomerClassify(customerInfo.getCustomerClassify());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackStatus())){
            dto.setCallbackStatus(customerInfo.getCallbackStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackTime())){
            //date 转
            dto.setCallbackTime(customerInfo.getCallbackTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getSubCompanyId())){
            dto.setSubCompanyId(customerInfo.getSubCompanyId());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(customerInfo.getCreateTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            dto.setLastUpdateTime(customerInfo.getLastUpdateTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            dto.setCreateUser(customerInfo.getCreateUser());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateUser())){
            dto.setLastUpdateUser(customerInfo.getLastUpdateUser());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsDeleted())){
            dto.setIsDeleted(customerInfo.getIsDeleted());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseAlone())){
            dto.setHouseAlone(customerInfo.getHouseAlone());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(customerInfo.getCreateTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerStreet())){
            dto.setCustomerStreet(customerInfo.getCustomerStreet());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseLoanValue())){
            dto.setHouseLoanValue(customerInfo.getHouseLoanValue());
        }

        //新版新增加字段
        if (!StringUtils.isEmpty(customerInfo.getExpectMoneyTime())){
            dto.setExpectMoneyTime(customerInfo.getExpectMoneyTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getExpectLoanTime())){
            dto.setExpectLoanTime(customerInfo.getExpectLoanTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getExpectRepaymentWay())){
            dto.setExpectRepaymentWay(customerInfo.getExpectRepaymentWay());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseClear())){
            dto.setHouseClear(customerInfo.getHouseClear());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseOwner())){
            dto.setHouseOwner(customerInfo.getHouseOwner());
        }

        if (!StringUtils.isEmpty(customerInfo.getMortgageAmount())){
            dto.setMortgageAmount(customerInfo.getMortgageAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getMortgaeMonth())){
            dto.setMortgaeMonth(customerInfo.getMortgaeMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsHavaCar())){
            dto.setIsHavaCar(customerInfo.getIsHavaCar());
        }

        if (!StringUtils.isEmpty(customerInfo.getCarWorth())){
            dto.setCarWorth(customerInfo.getCarWorth());
        }
        if (!StringUtils.isEmpty(customerInfo.getCarAge())){
            dto.setCarAge(customerInfo.getCarAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsHavaInsurance())){
            dto.setIsHavaInsurance(customerInfo.getIsHavaInsurance());
        }

        if (!StringUtils.isEmpty(customerInfo.getYearPayAmount())){
            dto.setYearPayAmount(customerInfo.getYearPayAmount());
        }

        if (!StringUtils.isEmpty(customerInfo.getInsuranceTime())){
            dto.setInsuranceTime(customerInfo.getInsuranceTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsHavaDebt())){
            dto.setIsHavaDebt(customerInfo.getIsHavaDebt());
        }

        if (!StringUtils.isEmpty(customerInfo.getDebtMoney())){
            dto.setDebtMoney(customerInfo.getDebtMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtMonth())){
            dto.setDebtMonth(customerInfo.getDebtMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtOverdue())){
            dto.setDebtOverdue(customerInfo.getDebtOverdue());
        }

        if (!StringUtils.isEmpty(customerInfo.getDebtOverdueMoney())){
            dto.setDebtOverdueMoney(customerInfo.getDebtOverdueMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtTime())){
            dto.setDebtTime(customerInfo.getDebtTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getWorkStatus())){
            dto.setWorkStatus(customerInfo.getWorkStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getWagerCard())){
            dto.setWagerCard(customerInfo.getWagerCard());
        }
        if (!StringUtils.isEmpty(customerInfo.getEntryTime())){
            dto.setEntryTime(customerInfo.getEntryTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getCompanyType())){
            dto.setCompanyType(customerInfo.getCompanyType());
        }

        if (!StringUtils.isEmpty(customerInfo.getSocialSecurity())){
            dto.setSocialSecurity(customerInfo.getSocialSecurity());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialMoney())){
            dto.setSocialMoney(customerInfo.getSocialMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialTime())){
            dto.setSocialTime(customerInfo.getSocialTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getProvidentFund())){
            dto.setProvidentFund(customerInfo.getProvidentFund());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvidentMoney())){
            dto.setProvidentMoney(customerInfo.getProvidentMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvidentTime())){
            dto.setProvidentTime(customerInfo.getProvidentTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getCompnyName())){
            dto.setCompnyName(customerInfo.getCompnyName());
        }
        if (!StringUtils.isEmpty(customerInfo.getFixedPhone())){
            dto.setFixedPhone(customerInfo.getFixedPhone());
        }

    }

    public static void CustomerInterviewEntityToCustomerInterviewDto(CustomerInterviewBaseInfo customerInfo, CustomerInterVibaseInfoDTO dto){

        dto.setId(customerInfo.getId());
        if (!StringUtils.isEmpty(customerInfo.getCustomerId())){
            dto.setCustomerId(customerInfo.getCustomerId());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerId())){
            dto.setCustomerId(customerInfo.getCustomerId());
        }
        if (!StringUtils.isEmpty(customerInfo.getOwnerUserId())){
            dto.setOwnerUserId(customerInfo.getOwnerUserId());
        }
        if (!StringUtils.isEmpty(customerInfo.getOwnerUserName())){
            dto.setOwnerUserName(customerInfo.getOwnerUserName());
        }
        if (!StringUtils.isEmpty(customerInfo.getName())){
            dto.setName(customerInfo.getName());
        }
        if (!StringUtils.isEmpty(customerInfo.getSex())){
            dto.setSex(customerInfo.getSex());
        }
        if (!StringUtils.isEmpty(customerInfo.getAge())){
            dto.setAge(customerInfo.getAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getBirthDate())){
            //TODO int 转为时间
            dto.setBirthDate(customerInfo.getBirthDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            dto.setTelephonenumber(customerInfo.getTelephonenumber());
        }
        if (!StringUtils.isEmpty(customerInfo.getMaritalStatus())){
            dto.setMaritalStatus(customerInfo.getMaritalStatus());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseholdRegister())){
            dto.setHouseholdRegister(customerInfo.getHouseholdRegister());
        }
        if (!StringUtils.isEmpty(customerInfo.getEducation())){
            dto.setEducation(customerInfo.getEducation());
        }
        if (!StringUtils.isEmpty(customerInfo.getFeeChannelName())){
            dto.setFeeChannelName(customerInfo.getFeeChannelName());
        }
        if (!StringUtils.isEmpty(customerInfo.getProductName())){
            dto.setProductName(customerInfo.getProductName());
        }
        if (!StringUtils.isEmpty(customerInfo.getMonthInterestRate())){
            dto.setMonthInterestRate(customerInfo.getMonthInterestRate());
        }
        if (!StringUtils.isEmpty(customerInfo.getServiceCharge())){
            dto.setServiceCharge(customerInfo.getServiceCharge());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanAmount())){
            dto.setLoanAmount(customerInfo.getLoanAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanTime())){
            //TODO int 转为时间
            dto.setLoanTime(customerInfo.getLoanTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanUseTime())){
            dto.setLoanUseTime(customerInfo.getLoanUseTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanPurpose())){
            dto.setLoanPurpose(customerInfo.getLoanPurpose());
        }
        if (!StringUtils.isEmpty(customerInfo.getPaymentType())){
            dto.setPaymentType(customerInfo.getPaymentType());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditRecord())){
            dto.setCreditRecord(customerInfo.getCreditRecord());
        }  if (!StringUtils.isEmpty(customerInfo.getZhimaCredit())){
            dto.setZhimaCredit(customerInfo.getZhimaCredit());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditQueryNumTwoMonth())){
            dto.setCreditQueryNumTwoMonth(customerInfo.getCreditQueryNumTwoMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditQueryNumSixMonth())){
            dto.setCreditQueryNumSixMonth(customerInfo.getCreditQueryNumSixMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getContinuityOverdueNumTwoYear())){
            dto.setContinuityOverdueNumTwoYear(customerInfo.getContinuityOverdueNumTwoYear());
        }
        if (!StringUtils.isEmpty(customerInfo.getTotalOverdueNumTwoYear())){
            dto.setTotalOverdueNumTwoYear(customerInfo.getTotalOverdueNumTwoYear());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtAmount())){
            dto.setDebtAmount(customerInfo.getDebtAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsOverdue())){
            dto.setIsOverdue(customerInfo.getIsOverdue());
        }
        if (!StringUtils.isEmpty(customerInfo.getOverdueAmount())){
            dto.setOverdueAmount(customerInfo.getOverdueAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getIndustry())){
            dto.setIndustry(customerInfo.getIndustry());
        }
        if (!StringUtils.isEmpty(customerInfo.getIncomeAmount())){
            dto.setIncomeAmount(customerInfo.getIncomeAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialSecurityDate())){
            //TODO int 转为时间
            dto.setSocialSecurityDate(customerInfo.getSocialSecurityDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialSecurityPayment())){
            dto.setSocialSecurityPayment(customerInfo.getSocialSecurityPayment());
        }
        if (!StringUtils.isEmpty(customerInfo.getHousingFundDate())){
            dto.setHousingFundDate(customerInfo.getHousingFundDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getHousingFundPayment())){
            dto.setHousingFundPayment(customerInfo.getHousingFundPayment());
        }
        if (!StringUtils.isEmpty(customerInfo.getWorkDate())){
            dto.setWorkDate(customerInfo.getWorkDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getCompanyRegisterDate())){
            dto.setCompanyRegisterDate(customerInfo.getCompanyRegisterDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getShareRate())){
            dto.setShareRate(customerInfo.getShareRate());
        }
        if (!StringUtils.isEmpty(customerInfo.getPublicFlowYearAmount())){
            dto.setPublicFlowYearAmount(customerInfo.getPublicFlowYearAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getPrivateFlowYearAmount())){
            dto.setPrivateFlowYearAmount(customerInfo.getPrivateFlowYearAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsLitigation())){
             dto.setIsLitigation(customerInfo.getIsLitigation());
        }
        if (!StringUtils.isEmpty(customerInfo.getRetireDate())){
             dto.setRetireDate(customerInfo.getRetireDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getRetirementPayMinAmount())){
             dto.setRetirementPayMinAmount(customerInfo.getRetirementPayMinAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsRelativeKnown())){
             dto.setIsRelativeKnown(customerInfo.getIsRelativeKnown());
        }
        if (!StringUtils.isEmpty(customerInfo.getRemark())){
             dto.setRemark(customerInfo.getRemark());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(customerInfo.getCreateTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            dto.setLastUpdateTime(customerInfo.getLastUpdateTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            dto.setCreateUser(customerInfo.getCreateUser());
        }
        if (!StringUtils.isEmpty(customerInfo.getLastUpdateUser())){
           dto.setLastUpdateUser(customerInfo.getLastUpdateUser());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsDeleted())){
           dto.setIsDeleted(customerInfo.getIsDeleted());
        }
    }
    public static void CustomerInterviewEntityToCustomerInterviewAllInfoDto(CustomerInterViewBaseCarHouseInsturDTO dto,
                                                                            CustomerInterviewBaseInfo customerInfo,
                                                                            List<CustomerInterviewCarInfo> customerInterviewCarInfoList,
                                                                            List<CustomerInterviewHouseInfo> customerInterviewHouseInfoList,
                                                                            List<CustomerInterviewInsuranceInfo> customerInterviewInsuranceInfoList){
        dto.setId(customerInfo.getId());
        if (!StringUtils.isEmpty(customerInfo.getCustomerId())){
            dto.setCustomerId(customerInfo.getCustomerId());
        }
        if (!StringUtils.isEmpty(customerInfo.getOwnerUserId())){
            dto.setOwnerUserId(customerInfo.getOwnerUserId());
        }
        if (!StringUtils.isEmpty(customerInfo.getOwnerUserName())){
            dto.setOwnerUserName(customerInfo.getOwnerUserName());
        }
        if (!StringUtils.isEmpty(customerInfo.getName())){
            dto.setName(customerInfo.getName());
        }
        if (!StringUtils.isEmpty(customerInfo.getSex())){
            dto.setSex(customerInfo.getSex());
        }
        if (!StringUtils.isEmpty(customerInfo.getAge())){
            dto.setAge(customerInfo.getAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getBirthDate())){
            //TODO int 转为时间
            dto.setBirthDate(customerInfo.getBirthDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            dto.setTelephonenumber(customerInfo.getTelephonenumber());
        }
        if (!StringUtils.isEmpty(customerInfo.getMaritalStatus())){
            dto.setMaritalStatus(customerInfo.getMaritalStatus());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseholdRegister())){
            dto.setHouseholdRegister(customerInfo.getHouseholdRegister());
        }
        if (!StringUtils.isEmpty(customerInfo.getEducation())){
            dto.setEducation(customerInfo.getEducation());
        }
        if (!StringUtils.isEmpty(customerInfo.getFeeChannelName())){
            dto.setFeeChannelName(customerInfo.getFeeChannelName());
        }
        if (!StringUtils.isEmpty(customerInfo.getProductName())){
            dto.setProductName(customerInfo.getProductName());
        }
        if (!StringUtils.isEmpty(customerInfo.getMonthInterestRate())){
            dto.setMonthInterestRate(customerInfo.getMonthInterestRate());
        }
        if (!StringUtils.isEmpty(customerInfo.getServiceCharge())){
            dto.setServiceCharge(customerInfo.getServiceCharge());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanAmount())){
            dto.setLoanAmount(customerInfo.getLoanAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanTime())){
            //TODO int 转为时间
            dto.setLoanTime(customerInfo.getLoanTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanUseTime())){
            dto.setLoanUseTime(customerInfo.getLoanUseTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanPurpose())){
            dto.setLoanPurpose(customerInfo.getLoanPurpose());
        }
        if (!StringUtils.isEmpty(customerInfo.getPaymentType())){
            dto.setPaymentType(customerInfo.getPaymentType());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditRecord())){
            dto.setCreditRecord(customerInfo.getCreditRecord());
        }  if (!StringUtils.isEmpty(customerInfo.getZhimaCredit())){
            dto.setZhimaCredit(customerInfo.getZhimaCredit());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditQueryNumTwoMonth())){
            dto.setCreditQueryNumTwoMonth(customerInfo.getCreditQueryNumTwoMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditQueryNumSixMonth())){
            dto.setCreditQueryNumSixMonth(customerInfo.getCreditQueryNumSixMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getContinuityOverdueNumTwoYear())){
            dto.setContinuityOverdueNumTwoYear(customerInfo.getContinuityOverdueNumTwoYear());
        }
        if (!StringUtils.isEmpty(customerInfo.getTotalOverdueNumTwoYear())){
            dto.setTotalOverdueNumTwoYear(customerInfo.getTotalOverdueNumTwoYear());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtAmount())){
            dto.setDebtAmount(customerInfo.getDebtAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsOverdue())){
            dto.setIsOverdue(customerInfo.getIsOverdue());
        }
        if (!StringUtils.isEmpty(customerInfo.getOverdueAmount())){
            dto.setOverdueAmount(customerInfo.getOverdueAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getIndustry())){
            dto.setIndustry(customerInfo.getIndustry());
        }
        if (!StringUtils.isEmpty(customerInfo.getIncomeAmount())){
            dto.setIncomeAmount(customerInfo.getIncomeAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialSecurityDate())){
            //TODO int 转为时间
            dto.setSocialSecurityDate(customerInfo.getSocialSecurityDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialSecurityPayment())){
            dto.setSocialSecurityPayment(customerInfo.getSocialSecurityPayment());
        }
        if (!StringUtils.isEmpty(customerInfo.getHousingFundDate())){
            dto.setHousingFundDate(customerInfo.getHousingFundDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getHousingFundPayment())){
            dto.setHousingFundPayment(customerInfo.getHousingFundPayment());
        }
        if (!StringUtils.isEmpty(customerInfo.getWorkDate())){
            dto.setWorkDate(customerInfo.getWorkDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getCompanyRegisterDate())){
            dto.setCompanyRegisterDate(customerInfo.getCompanyRegisterDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getShareRate())){
            dto.setShareRate(customerInfo.getShareRate());
        }
        if (!StringUtils.isEmpty(customerInfo.getPublicFlowYearAmount())){
            dto.setPublicFlowYearAmount(customerInfo.getPublicFlowYearAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getPrivateFlowYearAmount())){
            dto.setPrivateFlowYearAmount(customerInfo.getPrivateFlowYearAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsLitigation())){
            dto.setIsLitigation(customerInfo.getIsLitigation());
        }
        if (!StringUtils.isEmpty(customerInfo.getRetireDate())){
            dto.setRetireDate(customerInfo.getRetireDate());
        }
        if (!StringUtils.isEmpty(customerInfo.getRetirementPayMinAmount())){
            dto.setRetirementPayMinAmount(customerInfo.getRetirementPayMinAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsRelativeKnown())){
            dto.setIsRelativeKnown(customerInfo.getIsRelativeKnown());
        }
        if (!StringUtils.isEmpty(customerInfo.getRemark())){
            dto.setRemark(customerInfo.getRemark());
        }
        //开始存放车辆信息
        if (customerInterviewCarInfoList != null && customerInterviewCarInfoList.size() > 0){
            CustomerInterviewCarInfo customerInterviewCarInfo = customerInterviewCarInfoList.get(0);
            if (!StringUtils.isEmpty(customerInterviewCarInfo.getId())){
                dto.setCarInfoid(customerInterviewCarInfo.getId());
            }
            if (!StringUtils.isEmpty(customerInterviewCarInfo.getBuyDate())){
                dto.setBuyDate(customerInterviewCarInfo.getBuyDate());
            }
            if (!StringUtils.isEmpty(customerInterviewCarInfo.getCarMortgageMonthAmount())){
                dto.setCarMortgageMonthAmount(customerInterviewCarInfo.getCarMortgageMonthAmount());
            }
            if (!StringUtils.isEmpty(customerInterviewCarInfo.getCarMortgagePaidNum())){
                dto.setCarMortgagePaidNum(customerInterviewCarInfo.getCarMortgagePaidNum());
            }
            if (!StringUtils.isEmpty(customerInterviewCarInfo.getCarType())){
                dto.setCarType(customerInterviewCarInfo.getCarType());
            }
            if (!StringUtils.isEmpty(customerInterviewCarInfo.getCustomerInterviewBaseInfoId())){
                dto.setCustomerInterviewBaseInfoId(customerInterviewCarInfo.getCustomerInterviewBaseInfoId());
            }
            if (!StringUtils.isEmpty(customerInterviewCarInfo.getIsFullInsurance())){
                dto.setIsFullInsurance(customerInterviewCarInfo.getIsFullInsurance());
            }
            if (!StringUtils.isEmpty(customerInterviewCarInfo.getLicencePlateLocation())){
                dto.setLicencePlateLocation(customerInterviewCarInfo.getLicencePlateLocation());
            }
            if (!StringUtils.isEmpty(customerInterviewCarInfo.getPriceNow())){
                dto.setPriceNow(customerInterviewCarInfo.getPriceNow());
            }

        }
        //存放房产信息
        if (customerInterviewHouseInfoList != null && customerInterviewHouseInfoList.size() > 0){
            CustomerInterviewHouseInfo customerInterviewHouseInfo = customerInterviewHouseInfoList.get(0);
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getId())){
                dto.setHouseInfoId(customerInterviewHouseInfo.getId());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getAccepthousearea())){
                dto.setAccepthousearea(customerInterviewHouseInfo.getAccepthousearea());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getHouseStatus())){
                dto.setHouseStatus(customerInterviewHouseInfo.getHouseStatus());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getHousePropertyType())){
                dto.setHousePropertyType(customerInterviewHouseInfo.getHousePropertyType());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getArea())){
                dto.setArea(customerInterviewHouseInfo.getArea());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getBuildDate())){
                dto.setBuildDate(customerInterviewHouseInfo.getBuildDate());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getHousePropertyRightsNum())){
                dto.setHousePropertyRightsNum(customerInterviewHouseInfo.getHousePropertyRightsNum());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getIsChildInPropertyRigths())){
                dto.setIsChildInPropertyRigths(customerInterviewHouseInfo.getIsChildInPropertyRigths());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getIsOldInPropertyRigths())){
                dto.setIsOldInPropertyRigths(customerInterviewHouseInfo.getIsOldInPropertyRigths());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getIsPropertyRightsClear())){
                dto.setIsPropertyRightsClear(customerInterviewHouseInfo.getIsPropertyRightsClear());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getIsOtherHouse())){
                dto.setIsOtherHouse(customerInterviewHouseInfo.getIsOtherHouse());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getIsBankFlow())){
                dto.setIsBankFlow(customerInterviewHouseInfo.getIsBankFlow());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getBankFlowMonthAmount())){
                dto.setBankFlowMonthAmount(customerInterviewHouseInfo.getBankFlowMonthAmount());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getHouseMortgageMonthAmount())){
                dto.setHouseMortgageMonthAmount(customerInterviewHouseInfo.getHouseMortgageMonthAmount());
            }
            if (!StringUtils.isEmpty(customerInterviewHouseInfo.getHouseMortgagePaidNum())){
                dto.setHouseMortgagePaidNum(customerInterviewHouseInfo.getHouseMortgagePaidNum());
            }
        }
        //判断保险的信息转为dto
        if (customerInterviewInsuranceInfoList != null && customerInterviewInsuranceInfoList.size() > 0){
            CustomerInterviewInsuranceInfo customerInterviewInsuranceInfo = customerInterviewInsuranceInfoList.get(0);
            if (!StringUtils.isEmpty(customerInterviewInsuranceInfo.getId())){
                dto.setInsuranceInfoId(customerInterviewInsuranceInfo.getId());
            }
            if(!StringUtils.isEmpty(customerInterviewInsuranceInfo.getEffectDate())){
                dto.setEffectDate(customerInterviewInsuranceInfo.getEffectDate());
            }
            if(!StringUtils.isEmpty(customerInterviewInsuranceInfo.getInsuranceCompany())){
                dto.setInsuranceCompany(customerInterviewInsuranceInfo.getInsuranceCompany());
            }
            if(!StringUtils.isEmpty(customerInterviewInsuranceInfo.getInsuranceType())){
                dto.setInsuranceType(customerInterviewInsuranceInfo.getInsuranceType());
            }
            if(!StringUtils.isEmpty(customerInterviewInsuranceInfo.getIsSuspend())){
                dto.setIsSuspend(customerInterviewInsuranceInfo.getIsSuspend());
            }
            if(!StringUtils.isEmpty(customerInterviewInsuranceInfo.getMonthPayAmount())){
                dto.setMonthPayAmount(customerInterviewInsuranceInfo.getMonthPayAmount());
            }
            if(!StringUtils.isEmpty(customerInterviewInsuranceInfo.getPayType())){
                dto.setPayType(customerInterviewInsuranceInfo.getPayType());
            }
            if(!StringUtils.isEmpty(customerInterviewInsuranceInfo.getYearPayAmount())){
                dto.setYearPayAmount(customerInterviewInsuranceInfo.getYearPayAmount());
            }
        }

    }
    public static void CustomerInterviewDtoToCustomerInterviewAllInfoEntity(CustomerInterViewBaseCarHouseInsturDTO dto,
                                                                            CustomerInterviewBaseInfo customerInfo,
                                                                            CustomerInterviewCarInfo customerInterviewCarInfo,
                                                                            CustomerInterviewHouseInfo customerInterviewHouseInfo,
                                                                            CustomerInterviewInsuranceInfo customerInterviewInsuranceInfo){
      //  dto.setId(customerInfo.getId());
        if (!StringUtils.isEmpty(dto.getCustomerId())){
            customerInfo.setCustomerId(dto.getCustomerId());
        }
        if (!StringUtils.isEmpty(dto.getOwnerUserId())){
            customerInfo.setOwnerUserId(dto.getOwnerUserId());
        }
        if (!StringUtils.isEmpty(dto.getOwnerUserName())){
            customerInfo.setOwnerUserName(dto.getOwnerUserName());
        }
        if (!StringUtils.isEmpty(dto.getName())){
            customerInfo.setName(dto.getName());
        }
        if (!StringUtils.isEmpty(dto.getSex())){
            customerInfo.setSex(dto.getSex());
        }
        if (!StringUtils.isEmpty(dto.getAge())){
            customerInfo.setAge(dto.getAge());
        }
        if (!StringUtils.isEmpty(dto.getBirthDate())){
            //TODO int 转为时间
            customerInfo.setBirthDate(dto.getBirthDate());
        }
        if (!StringUtils.isEmpty(dto.getTelephonenumber())){
            customerInfo.setTelephonenumber(dto.getTelephonenumber());
        }
        if (!StringUtils.isEmpty(dto.getMaritalStatus())){
            customerInfo.setMaritalStatus(dto.getMaritalStatus());
        }
        if (!StringUtils.isEmpty(dto.getHouseholdRegister())){
            customerInfo.setHouseholdRegister(dto.getHouseholdRegister());
        }
        if (!StringUtils.isEmpty(dto.getEducation())){
            customerInfo.setEducation(dto.getEducation());
        }
        if (!StringUtils.isEmpty(dto.getFeeChannelName())){
            customerInfo.setFeeChannelName(dto.getFeeChannelName());
        }
        if (!StringUtils.isEmpty(dto.getProductName())){
            customerInfo.setProductName(dto.getProductName());
        }
        if (!StringUtils.isEmpty(dto.getMonthInterestRate())){
            customerInfo.setMonthInterestRate(dto.getMonthInterestRate());
        }
        if (!StringUtils.isEmpty(dto.getServiceCharge())){
            customerInfo.setServiceCharge(dto.getServiceCharge());
        }
        if (!StringUtils.isEmpty(dto.getLoanAmount())){
            customerInfo.setLoanAmount(dto.getLoanAmount());
        }
        if (!StringUtils.isEmpty(dto.getLoanTime())){
            //TODO int 转为时间
            customerInfo.setLoanTime(dto.getLoanTime());
        }
        if (!StringUtils.isEmpty(dto.getLoanUseTime())){
            customerInfo.setLoanUseTime(dto.getLoanUseTime());
        }
        if (!StringUtils.isEmpty(dto.getLoanPurpose())){
            customerInfo.setLoanPurpose(dto.getLoanPurpose());
        }
        if (!StringUtils.isEmpty(dto.getPaymentType())){
            customerInfo.setPaymentType(dto.getPaymentType());
        }
        if (!StringUtils.isEmpty(dto.getCreditRecord())){
            customerInfo.setCreditRecord(dto.getCreditRecord());
        }  if (!StringUtils.isEmpty(dto.getZhimaCredit())){
            customerInfo.setZhimaCredit(dto.getZhimaCredit());
        }
        if (!StringUtils.isEmpty(dto.getCreditQueryNumTwoMonth())){
            customerInfo.setCreditQueryNumTwoMonth(dto.getCreditQueryNumTwoMonth());
        }
        if (!StringUtils.isEmpty(dto.getCreditQueryNumSixMonth())){
            customerInfo.setCreditQueryNumSixMonth(dto.getCreditQueryNumSixMonth());
        }
        if (!StringUtils.isEmpty(dto.getContinuityOverdueNumTwoYear())){
            customerInfo.setContinuityOverdueNumTwoYear(dto.getContinuityOverdueNumTwoYear());
        }
        if (!StringUtils.isEmpty(dto.getTotalOverdueNumTwoYear())){
            customerInfo.setTotalOverdueNumTwoYear(dto.getTotalOverdueNumTwoYear());
        }
        if (!StringUtils.isEmpty(dto.getDebtAmount())){
            customerInfo.setDebtAmount(dto.getDebtAmount());
        }
        if (!StringUtils.isEmpty(dto.getIsOverdue())){
            customerInfo.setIsOverdue(dto.getIsOverdue());
        }
        if (!StringUtils.isEmpty(dto.getOverdueAmount())){
            customerInfo.setOverdueAmount(dto.getOverdueAmount());
        }
        if (!StringUtils.isEmpty(dto.getIndustry())){
            customerInfo.setIndustry(dto.getIndustry());
        }
        if (!StringUtils.isEmpty(dto.getIncomeAmount())){
            customerInfo.setIncomeAmount(dto.getIncomeAmount());
        }
        if (!StringUtils.isEmpty(dto.getSocialSecurityDate())){
            //TODO int 转为时间
            customerInfo.setSocialSecurityDate(dto.getSocialSecurityDate());
        }
        if (!StringUtils.isEmpty(dto.getSocialSecurityPayment())){
            customerInfo.setSocialSecurityPayment(dto.getSocialSecurityPayment());
        }
        if (!StringUtils.isEmpty(dto.getHousingFundDate())){
            customerInfo.setHousingFundDate(dto.getHousingFundDate());
        }
        if (!StringUtils.isEmpty(dto.getHousingFundPayment())){
            customerInfo.setHousingFundPayment(dto.getHousingFundPayment());
        }
        if (!StringUtils.isEmpty(dto.getWorkDate())){
            customerInfo.setWorkDate(dto.getWorkDate());
        }
        if (!StringUtils.isEmpty(dto.getCompanyRegisterDate())){
            customerInfo.setCompanyRegisterDate(dto.getCompanyRegisterDate());
        }
        if (!StringUtils.isEmpty(dto.getShareRate())){
            customerInfo.setShareRate(dto.getShareRate());
        }
        if (!StringUtils.isEmpty(dto.getPublicFlowYearAmount())){
            customerInfo.setPublicFlowYearAmount(dto.getPublicFlowYearAmount());
        }
        if (!StringUtils.isEmpty(dto.getPrivateFlowYearAmount())){
            customerInfo.setPrivateFlowYearAmount(dto.getPrivateFlowYearAmount());
        }
        if (!StringUtils.isEmpty(dto.getIsLitigation())){
            customerInfo.setIsLitigation(dto.getIsLitigation());
        }
        if (!StringUtils.isEmpty(dto.getRetireDate())){
            customerInfo.setRetireDate(dto.getRetireDate());
        }
        if (!StringUtils.isEmpty(dto.getRetirementPayMinAmount())){
            customerInfo.setRetirementPayMinAmount(dto.getRetirementPayMinAmount());
        }
        if (!StringUtils.isEmpty(dto.getIsRelativeKnown())){
            customerInfo.setIsRelativeKnown(dto.getIsRelativeKnown());
        }
        if (!StringUtils.isEmpty(dto.getRemark())){
            customerInfo.setRemark(dto.getRemark());
        }
        //开始存放车辆信息


            if (!StringUtils.isEmpty(dto.getBuyDate())){
                customerInterviewCarInfo.setBuyDate(dto.getBuyDate());
            }
            if (!StringUtils.isEmpty(dto.getCarMortgageMonthAmount())){
                customerInterviewCarInfo.setCarMortgageMonthAmount(dto.getCarMortgageMonthAmount());
            }
            if (!StringUtils.isEmpty(dto.getCarMortgagePaidNum())){
                customerInterviewCarInfo.setCarMortgagePaidNum(dto.getCarMortgagePaidNum());
            }
            if (!StringUtils.isEmpty(dto.getCarType())){
                customerInterviewCarInfo.setCarType(dto.getCarType());
            }
            if (!StringUtils.isEmpty(dto.getCustomerInterviewBaseInfoId())){
                customerInterviewCarInfo.setCustomerInterviewBaseInfoId(dto.getCustomerInterviewBaseInfoId());
            }
            if (!StringUtils.isEmpty(dto.getIsFullInsurance())){
                customerInterviewCarInfo.setIsFullInsurance(dto.getIsFullInsurance());
            }
            if (!StringUtils.isEmpty(dto.getLicencePlateLocation())){
                customerInterviewCarInfo.setLicencePlateLocation(dto.getLicencePlateLocation());
            }
            if (!StringUtils.isEmpty(dto.getPriceNow())){
                customerInterviewCarInfo.setPriceNow(dto.getPriceNow());
            }

        //存放房产信息


            if (!StringUtils.isEmpty(dto.getAccepthousearea())){
                customerInterviewHouseInfo.setAccepthousearea(dto.getAccepthousearea());
            }
            if (!StringUtils.isEmpty(dto.getHouseStatus())){
                customerInterviewHouseInfo.setHouseStatus(dto.getHouseStatus());
            }
            if (!StringUtils.isEmpty(dto.getHousePropertyType())){
                customerInterviewHouseInfo.setHousePropertyType(dto.getHousePropertyType());
            }
            if (!StringUtils.isEmpty(dto.getArea())){
                customerInterviewHouseInfo.setArea(dto.getArea());
            }
            if (!StringUtils.isEmpty(dto.getBuildDate())){
                customerInterviewHouseInfo.setBuildDate(dto.getBuildDate());
            }
            if (!StringUtils.isEmpty(dto.getHousePropertyRightsNum())){
                customerInterviewHouseInfo.setHousePropertyRightsNum(dto.getHousePropertyRightsNum());
            }
            if (!StringUtils.isEmpty(dto.getIsChildInPropertyRigths())){
                customerInterviewHouseInfo.setIsChildInPropertyRigths(dto.getIsChildInPropertyRigths());
            }
            if (!StringUtils.isEmpty(dto.getIsOldInPropertyRigths())){
                customerInterviewHouseInfo.setIsOldInPropertyRigths(dto.getIsOldInPropertyRigths());
            }
            if (!StringUtils.isEmpty(dto.getIsPropertyRightsClear())){
                customerInterviewHouseInfo.setIsPropertyRightsClear(dto.getIsPropertyRightsClear());
            }
            if (!StringUtils.isEmpty(dto.getIsOtherHouse())){
                customerInterviewHouseInfo.setIsOtherHouse(dto.getIsOtherHouse());
            }
            if (!StringUtils.isEmpty(dto.getIsBankFlow())){
                customerInterviewHouseInfo.setIsBankFlow(dto.getIsBankFlow());
            }
            if (!StringUtils.isEmpty(dto.getBankFlowMonthAmount())){
                customerInterviewHouseInfo.setBankFlowMonthAmount(dto.getBankFlowMonthAmount());
            }
            if (!StringUtils.isEmpty(dto.getHouseMortgageMonthAmount())){
                customerInterviewHouseInfo.setHouseMortgageMonthAmount(dto.getHouseMortgageMonthAmount());
            }
            if (!StringUtils.isEmpty(dto.getHouseMortgagePaidNum())){
                customerInterviewHouseInfo.setHouseMortgagePaidNum(dto.getHouseMortgagePaidNum());
            }

        //判断保险的信息转为dto


            if(!StringUtils.isEmpty(dto.getEffectDate())){
                customerInterviewInsuranceInfo.setEffectDate(dto.getEffectDate());
            }
            if(!StringUtils.isEmpty(dto.getInsuranceCompany())){
                customerInterviewInsuranceInfo.setInsuranceCompany(dto.getInsuranceCompany());
            }
            if(!StringUtils.isEmpty(dto.getInsuranceType())){
                customerInterviewInsuranceInfo.setInsuranceType(dto.getInsuranceType());
            }
            if(!StringUtils.isEmpty(dto.getIsSuspend())){
                customerInterviewInsuranceInfo.setIsSuspend(dto.getIsSuspend());
            }
            if(!StringUtils.isEmpty(dto.getMonthPayAmount())){
                customerInterviewInsuranceInfo.setMonthPayAmount(dto.getMonthPayAmount());
            }
            if(!StringUtils.isEmpty(dto.getPayType())){
                customerInterviewInsuranceInfo.setPayType(dto.getPayType());
            }
            if(!StringUtils.isEmpty(dto.getYearPayAmount())){
                customerInterviewInsuranceInfo.setYearPayAmount(dto.getYearPayAmount());
            }
        }
    public static void  ConyDtoToEntityHOuseReg(HouseholdRegisterDTO dto,OcrHouseholdRegister ocrHouseholdRegister){

        if (!StringUtils.isEmpty(dto.getHousehold_birthday())){
            ocrHouseholdRegister.setHouseholdBirthday(dto.getHousehold_birthday());
        }

        if (!StringUtils.isEmpty(dto.getHousehold_name())){
            ocrHouseholdRegister.setHouseholdName(dto.getHousehold_name());
        }

        if (!StringUtils.isEmpty(dto.getHousehold_sex())){
            ocrHouseholdRegister.setHouseholdSex(dto.getHousehold_sex());
        }
        if (!StringUtils.isEmpty(dto.getHousehold_native_place())){
            ocrHouseholdRegister.setHouseholdNativePlace(dto.getHousehold_native_place());
        }
        if (!StringUtils.isEmpty(dto.getHousehold_id_number())){
            ocrHouseholdRegister.setHouseholdIdNumber(dto.getHousehold_id_number());
        }

        if (!StringUtils.isEmpty(dto.getHousehold_people())){
            ocrHouseholdRegister.setHouseholdPeople(dto.getHousehold_people());
        }
        if (!StringUtils.isEmpty(dto.getHousehold_job())){
            ocrHouseholdRegister.setHouseholdJob(dto.getHousehold_job());
        }
        if (!StringUtils.isEmpty(dto.getHousehold_merriage())){
            ocrHouseholdRegister.setHouseholdMerriage(dto.getHousehold_merriage());
        }

        if (!StringUtils.isEmpty(dto.getHousehold_education())){
            ocrHouseholdRegister.setHouseholdEducation(dto.getHousehold_education());
        }
       if (!StringUtils.isEmpty(dto.getCustomer_id())){
           ocrHouseholdRegister.setCustomerId(Integer.parseInt(dto.getCustomer_id().toString()));
       }
       if (!StringUtils.isEmpty(dto.getCustomer_name())){
           ocrHouseholdRegister.setCustomerName(dto.getCustomer_name());
       }
        if (!StringUtils.isEmpty(dto.getCustomer_telephone())){
            ocrHouseholdRegister.setCustomerTelephone(dto.getCustomer_telephone());
        }
        if (!StringUtils.isEmpty(dto.getCrm_attach_id())){
            ocrHouseholdRegister.setDocumentId(dto.getCrm_attach_id().toString());
        }
        if (!StringUtils.isEmpty(dto.getCreate_user_id())){
            ocrHouseholdRegister.setCreateUser(Integer.parseInt(dto.getCreate_user_id().toString()));
        }
        if (!StringUtils.isEmpty(dto.getUpdate_user_id())){
            ocrHouseholdRegister.setLastUpdateUser(Integer.parseInt(dto.getUpdate_user_id().toString()));
        }
    }
      public static void copyDriverLienceToDto(DriverLicenseDTO dto ,OcrDriverLicense ocrDriverLicense){

        if (!StringUtils.isEmpty(dto.getDriver_name())){
            ocrDriverLicense.setDriverName(dto.getDriver_name());
        }

          if (!StringUtils.isEmpty(dto.getDriver_num())){
              ocrDriverLicense.setDriverNum(dto.getDriver_num());
          }

          if (!StringUtils.isEmpty(dto.getDriver_vehicle_type())){
              ocrDriverLicense.setDriverVehicleType(dto.getDriver_vehicle_type());
          }

          if (!StringUtils.isEmpty(dto.getDriver_start_date())){
              ocrDriverLicense.setDriverStartDate(dto.getDriver_start_date());
          }

          if (!StringUtils.isEmpty(dto.getDriver_end_date())){
              ocrDriverLicense.setDriverEndDate(dto.getDriver_end_date());
          }

          if (!StringUtils.isEmpty(dto.getCustomer_id())){
              ocrDriverLicense.setCustomerId(Integer.parseInt(dto.getCustomer_id().toString()));
          }
          if (!StringUtils.isEmpty(dto.getCustomer_name())){
              ocrDriverLicense.setCustomerName(dto.getCustomer_name());
          }
          if (!StringUtils.isEmpty(dto.getCustomer_telephone())){
              ocrDriverLicense.setCustomerTelephone(dto.getCustomer_telephone());
          }
          if (!StringUtils.isEmpty(dto.getCrm_attach_id())){
              ocrDriverLicense.setDocumentId(Integer.parseInt(dto.getCrm_attach_id().toString()));
          }
          if (!StringUtils.isEmpty(dto.getCreate_user_id())){
              ocrDriverLicense.setCreateUserId(Integer.parseInt(dto.getCreate_user_id().toString()));
          }
          if (!StringUtils.isEmpty(dto.getCreate_user_name())){
              ocrDriverLicense.setCreateUserName(dto.getCreate_user_name());
          }
          if (!StringUtils.isEmpty(dto.getUpdate_user_name())){
              ocrDriverLicense.setUpdateUserName(dto.getUpdate_user_name());
          }
          if (!StringUtils.isEmpty(dto.getUpdate_user_id())){
              ocrDriverLicense.setUpdateUserId(Integer.parseInt(dto.getUpdate_user_id().toString()));
          }
      }

    public static void copyDriverVehToDto(DriverVehicleDTO dto,OcrDriverVehicle ocrDriverVehicle){

          if (!StringUtils.isEmpty(dto.getDriver_owner())){
              ocrDriverVehicle.setDriverOwner(dto.getDriver_owner());
          }
        if (!StringUtils.isEmpty(dto.getDriver_plate_num())){
            ocrDriverVehicle.setDriverPlateNum(dto.getDriver_plate_num());
        }

        if (!StringUtils.isEmpty(dto.getDriver_vehicle_type())){
            ocrDriverVehicle.setDriverVehicleType(dto.getDriver_vehicle_type());
        }

        if (!StringUtils.isEmpty(dto.getDriver_vin())){
            ocrDriverVehicle.setDriverVin(dto.getDriver_vin());
        }

        if (!StringUtils.isEmpty(dto.getDriver_engine_num())){
            ocrDriverVehicle.setDriverEngineNum(dto.getDriver_engine_num());
        }

        if (!StringUtils.isEmpty(dto.getDriver_register_date())){
            ocrDriverVehicle.setDriverRegisterDate(dto.getDriver_register_date());
        }
        if (!StringUtils.isEmpty(dto.getCustomer_id())){
            ocrDriverVehicle.setCustomerId(Integer.parseInt(dto.getCustomer_id().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCustomer_name())){
            ocrDriverVehicle.setCustomerName(dto.getCustomer_name());
        }
        if (!StringUtils.isEmpty(dto.getCustomer_telephone())){
            ocrDriverVehicle.setCustomerTelephone(dto.getCustomer_telephone());
        }
        if (!StringUtils.isEmpty(dto.getCrm_attach_id())){
            ocrDriverVehicle.setDocumentId(Integer.parseInt(dto.getCrm_attach_id().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCreate_user_id())){
            ocrDriverVehicle.setCreateUser(Integer.parseInt(dto.getCreate_user_id().toString()));
        }
        if (!StringUtils.isEmpty(dto.getUpdate_user_id())){
            ocrDriverVehicle.setLastUpdateUser(Integer.parseInt(dto.getUpdate_user_id().toString()));
        }

    }
    public static void copyHouseRegToDto(HouseRegisterDTO dto, OcrHouseRegistration ocrHouseRegistration) {

        if (StringUtils.isEmpty(dto.getHouse_ownner())){
            ocrHouseRegistration.setHouseOwnner(dto.getHouse_ownner());
        }
        if (StringUtils.isEmpty(dto.getHouse_address())){
            ocrHouseRegistration.setHouseAddress(dto.getHouse_address());
        }
        if (StringUtils.isEmpty(dto.getHouse_purpose())){
            ocrHouseRegistration.setHousePurpose(dto.getHouse_purpose());
        }
        if (StringUtils.isEmpty(dto.getHouse_usage_term())){
            ocrHouseRegistration.setHouseUsageTerm(dto.getHouse_usage_term());
        }
        if (StringUtils.isEmpty(dto.getHouse_area())){
            ocrHouseRegistration.setHouseArea(dto.getHouse_area());
        }
        if (StringUtils.isEmpty(dto.getHouse_type())){
            ocrHouseRegistration.setHouseType(dto.getHouse_type());
        }
        if (StringUtils.isEmpty(dto.getHouse_completion_date())){
            ocrHouseRegistration.setHouseCompletionDate(dto.getHouse_completion_date());
        }
        if (!StringUtils.isEmpty(dto.getCustomer_id())){
            ocrHouseRegistration.setCustomerId(Integer.parseInt(dto.getCustomer_id().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCustomer_name())){
            ocrHouseRegistration.setCustomerName(dto.getCustomer_name());
        }
        if (!StringUtils.isEmpty(dto.getCustomer_telephone())){
            ocrHouseRegistration.setCustomerTelephone(dto.getCustomer_telephone());
        }
        if (!StringUtils.isEmpty(dto.getCrm_attach_id())){
            ocrHouseRegistration.setDocumentId(Integer.parseInt(dto.getCrm_attach_id().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCreate_user_id())){
            ocrHouseRegistration.setCreateUser(Integer.parseInt(dto.getCreate_user_id().toString()));
        }
        if (!StringUtils.isEmpty(dto.getUpdate_user_id())){
            ocrHouseRegistration.setLastUpdateUser(Integer.parseInt(dto.getUpdate_user_id().toString()));
        }
    }
     public static  void coptIdCardEntityToDto(OcrIdentity ocrIdentity,OcrIDdentityDTO dto){

        if (!StringUtils.isEmpty(ocrIdentity.getId())){
            dto.setId(ocrIdentity.getId());
        }
         if (!StringUtils.isEmpty(ocrIdentity.getCardName())){
             dto.setCard_name(ocrIdentity.getCardName());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCardSex())){
             dto.setCard_sex(ocrIdentity.getCardSex());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCardNation())){
             dto.setCard_nation(ocrIdentity.getCardNation());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCardBirth())){
             dto.setCard_birth(ocrIdentity.getCardBirth());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCardAddress())){
             dto.setCard_address(ocrIdentity.getCardAddress());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCardNum())){
             dto.setCard_num(ocrIdentity.getCardNum());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCardSignOrg())){
             dto.setCard_sign_org(ocrIdentity.getCardSignOrg());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCardValidStart())){
             dto.setCard_valid_start(ocrIdentity.getCardValidStart());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCardValidEnd())){
             dto.setCard_valid_end(ocrIdentity.getCardValidEnd());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCardNum())){
             dto.setCard_num(ocrIdentity.getCardNum());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCustomerId())){
             dto.setCustomer_id(ocrIdentity.getCustomerId());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCustomerName())){
             dto.setCustomer_name(ocrIdentity.getCustomerName());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCustomerTelephone())){
             //解密并隐藏
             String phoneNumber = ocrIdentity.getCustomerTelephone().substring(0, 3) + "****" + ocrIdentity.getCustomerTelephone().substring(7, ocrIdentity.getCustomerTelephone().length());
             dto.setCustomer_telephone(phoneNumber);
         }

         if (!StringUtils.isEmpty(ocrIdentity.getCustomerId())){
             dto.setCustomer_id(ocrIdentity.getCustomerId());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCustomerName())){
             dto.setCustomer_name(ocrIdentity.getCustomerName());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCreateTime())){
             dto.setCreate_time(ocrIdentity.getCreateTime());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getCreateUser())){
             dto.setCreate_user_id(ocrIdentity.getCreateUser());
         }
         if (!StringUtils.isEmpty(ocrIdentity.getStatus())){
             dto.setStatus(ocrIdentity.getStatus());
         }
     }

    public static void  EntityHOuseRegToDTo(OcrHouseholdRegister dto,HouseholdRegisterDTO ocrHouseholdRegister){
        ocrHouseholdRegister.setId(Long.parseLong(dto.getId().toString()));
        if (!StringUtils.isEmpty(dto.getHouseholdBirthday())){
            ocrHouseholdRegister.setHousehold_birthday(dto.getHouseholdBirthday());
        }

        if (!StringUtils.isEmpty(dto.getHouseholdName())){
            ocrHouseholdRegister.setHousehold_name(dto.getHouseholdName());
        }

        if (!StringUtils.isEmpty(dto.getHouseholdSex())){
            ocrHouseholdRegister.setHousehold_sex(dto.getHouseholdSex());
        }
        if (!StringUtils.isEmpty(dto.getHouseholdNativePlace())){
            ocrHouseholdRegister.setHousehold_native_place(dto.getHouseholdNativePlace());
        }
        if (!StringUtils.isEmpty(dto.getHouseholdIdNumber())){
            ocrHouseholdRegister.setHousehold_id_number(dto.getHouseholdIdNumber());
        }

        if (!StringUtils.isEmpty(dto.getHouseholdPeople())){
            ocrHouseholdRegister.setHousehold_people(dto.getHouseholdPeople());
        }
        if (!StringUtils.isEmpty(dto.getHouseholdJob())){
            ocrHouseholdRegister.setHousehold_job(dto.getHouseholdJob());
        }
        if (!StringUtils.isEmpty(dto.getHouseholdMerriage())){
            ocrHouseholdRegister.setHousehold_merriage(dto.getHouseholdMerriage());
        }

        if (!StringUtils.isEmpty(dto.getHouseholdEducation())){
            ocrHouseholdRegister.setHousehold_education(dto.getHouseholdEducation());
        }
        if (!StringUtils.isEmpty(dto.getCustomerId())){
            ocrHouseholdRegister.setCustomer_id(Long.parseLong(dto.getCustomerId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCustomerName())){
            ocrHouseholdRegister.setCustomer_name(dto.getCustomerName());
        }
        if (!StringUtils.isEmpty(dto.getCustomerTelephone())){
            String phoneNumber = dto.getCustomerTelephone().substring(0, 3) + "****" + dto.getCustomerTelephone().substring(7, dto.getCustomerTelephone().length());
            ocrHouseholdRegister.setCustomer_telephone(phoneNumber);
        }
        if (!StringUtils.isEmpty(dto.getDocumentId())){
            ocrHouseholdRegister.setCrm_attach_id(Long.parseLong(dto.getDocumentId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCreateUser())){
            ocrHouseholdRegister.setCreate_user_id(Long.parseLong(dto.getCreateUser().toString()));
        }
        if (!StringUtils.isEmpty(dto.getLastUpdateUser())){
            ocrHouseholdRegister.setUpdate_user_id(Long.parseLong(dto.getLastUpdateUser().toString()));
        }
        if (!StringUtils.isEmpty(dto.getStatus())){
            ocrHouseholdRegister.setStatus(dto.getStatus());
        }
    }
    public static void copyDtoToDriverLience(OcrDriverLicense dto ,DriverLicenseDTO ocrDriverLicense){
            ocrDriverLicense.setId(Long.parseLong(dto.getId().toString()));
        if (!StringUtils.isEmpty(dto.getDriverName())){
            ocrDriverLicense.setDriver_name(dto.getDriverName());
        }

        if (!StringUtils.isEmpty(dto.getDriverNum())){
            ocrDriverLicense.setDriver_num(dto.getDriverNum());
        }

        if (!StringUtils.isEmpty(dto.getDriverVehicleType())){
            ocrDriverLicense.setDriver_vehicle_type(dto.getDriverVehicleType());
        }

        if (!StringUtils.isEmpty(dto.getDriverStartDate())){
            ocrDriverLicense.setDriver_start_date(dto.getDriverStartDate());
        }

        if (!StringUtils.isEmpty(dto.getDriverEndDate())){
            ocrDriverLicense.setDriver_end_date(dto.getDriverEndDate());
        }

        if (!StringUtils.isEmpty(dto.getCustomerId())){
            ocrDriverLicense.setCustomer_id(Long.parseLong(dto.getCustomerId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCustomerName())){
            ocrDriverLicense.setCustomer_name(dto.getCustomerName());
        }
        if (!StringUtils.isEmpty(dto.getCustomerTelephone())){
            //TODO 解密
            String phoneNumber = dto.getCustomerTelephone().substring(0, 3) + "****" + dto.getCustomerTelephone().substring(7, dto.getCustomerTelephone().length());
            ocrDriverLicense.setCustomer_telephone(phoneNumber);
        }
        if (!StringUtils.isEmpty(dto.getDocumentId())){
            ocrDriverLicense.setCrm_attach_id(Long.parseLong(dto.getDocumentId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCreateUserId())){
            ocrDriverLicense.setCreate_user_id(Long.parseLong(dto.getCreateUserId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getUpdateUserId())){
            ocrDriverLicense.setUpdate_user_id(Long.parseLong(dto.getUpdateUserId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getStatus())){
            ocrDriverLicense.setStatus(dto.getStatus());
        }
    }
    public static void copyDtoToEntity(OcrDriverVehicle dto,DriverVehicleDTO ocrDriverVehicle){
        ocrDriverVehicle.setId(Long.parseLong(dto.getId().toString()));
        if (!StringUtils.isEmpty(dto.getDriverOwner())){
            ocrDriverVehicle.setDriver_owner(dto.getDriverOwner());
        }
        if (!StringUtils.isEmpty(dto.getDriverPlateNum())){
            ocrDriverVehicle.setDriver_plate_num(dto.getDriverPlateNum());
        }

        if (!StringUtils.isEmpty(dto.getDriverVehicleType())){
            ocrDriverVehicle.setDriver_vehicle_type(dto.getDriverVehicleType());
        }

        if (!StringUtils.isEmpty(dto.getDriverVin())){
            ocrDriverVehicle.setDriver_vin(dto.getDriverVin());
        }

        if (!StringUtils.isEmpty(dto.getDriverEngineNum())){
            ocrDriverVehicle.setDriver_engine_num(dto.getDriverEngineNum());
        }

        if (!StringUtils.isEmpty(dto.getDriverRegisterDate())){
            ocrDriverVehicle.setDriver_register_date(dto.getDriverRegisterDate());
        }
        if (!StringUtils.isEmpty(dto.getCustomerId())){
            ocrDriverVehicle.setCustomer_id(Long.parseLong(dto.getCustomerId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCustomerName())){
            ocrDriverVehicle.setCustomer_name(dto.getCustomerName());
        }
        if (!StringUtils.isEmpty(dto.getCustomerTelephone())){
            String phoneNumber = dto.getCustomerTelephone().substring(0, 3) + "****" + dto.getCustomerTelephone().substring(7, dto.getCustomerTelephone().length());
            ocrDriverVehicle.setCustomer_telephone(phoneNumber);
        }
        if (!StringUtils.isEmpty(dto.getDocumentId())){
            ocrDriverVehicle.setCrm_attach_id(Long.parseLong(dto.getDocumentId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCreateUser())){
            ocrDriverVehicle.setCreate_user_id(Long.parseLong(dto.getCreateUser().toString()));
        }
        if (!StringUtils.isEmpty(dto.getLastUpdateUser())){
            ocrDriverVehicle.setUpdate_user_id(Long.parseLong(dto.getLastUpdateUser().toString()));
        }
        if (!StringUtils.isEmpty(dto.getStatus())){
            ocrDriverVehicle.setStatus(dto.getStatus());
        }
    }
    public static void copyDtoToHouseReg(OcrHouseRegistration dto, HouseRegisterDTO ocrHouseRegistration) {
        ocrHouseRegistration.setId(Long.parseLong(dto.getId().toString()));
        if (StringUtils.isEmpty(dto.getHouseOwnner())){
            ocrHouseRegistration.setHouse_ownner(dto.getHouseOwnner());
        }
        if (StringUtils.isEmpty(dto.getHouseAddress())){
            ocrHouseRegistration.setHouse_address(dto.getHouseAddress());
        }
        if (StringUtils.isEmpty(dto.getHousePurpose())){
            ocrHouseRegistration.setHouse_purpose(dto.getHousePurpose());
        }
        if (StringUtils.isEmpty(dto.getHouseUsageTerm())){
            ocrHouseRegistration.setHouse_usage_term(dto.getHouseUsageTerm());
        }
        if (StringUtils.isEmpty(dto.getHouseArea())){
            ocrHouseRegistration.setHouse_area(dto.getHouseArea());
        }
        if (StringUtils.isEmpty(dto.getHouseType())){
            ocrHouseRegistration.setHouse_type(dto.getHouseType());
        }
        if (StringUtils.isEmpty(dto.getHouseCompletionDate())){
            ocrHouseRegistration.setHouse_completion_date(dto.getHouseCompletionDate());
        }
        if (!StringUtils.isEmpty(dto.getCustomerId())){
            ocrHouseRegistration.setCustomer_id(Long.parseLong(dto.getCustomerId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCustomerName())){
            ocrHouseRegistration.setCustomer_name(dto.getCustomerName());
        }
        if (!StringUtils.isEmpty(dto.getCustomerTelephone())){
            String phoneNumber = dto.getCustomerTelephone().substring(0, 3) + "****" + dto.getCustomerTelephone().substring(7, dto.getCustomerTelephone().length());
            ocrHouseRegistration.setCustomer_telephone(phoneNumber);
        }
        if (!StringUtils.isEmpty(dto.getDocumentId())){
            ocrHouseRegistration.setCrm_attach_id(Long.parseLong(dto.getDocumentId().toString()));
        }
        if (!StringUtils.isEmpty(dto.getCreateUser())){
            ocrHouseRegistration.setCreate_user_id(Long.parseLong(dto.getCreateUser().toString()));
        }
        if (!StringUtils.isEmpty(dto.getLastUpdateUser())){
            ocrHouseRegistration.setUpdate_user_id(Long.parseLong(dto.getLastUpdateUser().toString()));
        }
        if (!StringUtils.isEmpty(dto.getStatus())){
            ocrHouseRegistration.setStatus(dto.getStatus());
        }
    }

    public static void customerEntityToCustomerLog(CustomerInfo customerInfo, CustomerInfoLog log){


        log.setCustomerId(customerInfo.getId());

        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            log.setTelephonenumber(customerInfo.getTelephonenumber());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerName())){
            log.setCustomerName(customerInfo.getCustomerName());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerLevel())){
            log.setCustomerLevel(customerInfo.getCustomerLevel());
        }
        if (!StringUtils.isEmpty(customerInfo.getSparePhone())){
            log.setSparePhone(customerInfo.getSparePhone());
        }
        if (!StringUtils.isEmpty(customerInfo.getAge())){
            log.setAge(customerInfo.getAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getMarriage())){
            log.setMarriage(customerInfo.getMarriage());
        }
        if (!StringUtils.isEmpty(customerInfo.getIdCard())){
            log.setIdCard(customerInfo.getIdCard());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvinceHuji())){
            log.setProvinceHuji(customerInfo.getProvinceHuji());
        }

        if (!StringUtils.isEmpty(customerInfo.getSex())){
            log.setSex(customerInfo.getSex());
        }

        if (!StringUtils.isEmpty(customerInfo.getCustomerAddress())){
            log.setCustomerAddress(customerInfo.getCustomerAddress());
        }

        if (!StringUtils.isEmpty(customerInfo.getPerDescription())){
            log.setPerDescription(customerInfo.getPerDescription());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseStatus())){
            log.setHouseStatus(customerInfo.getHouseStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAmount())){
            log.setHouseAmount(customerInfo.getHouseAmount());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseType())){
            log.setHouseType(customerInfo.getHouseType());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseValue())){
            log.setHouseValue(customerInfo.getHouseValue());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseArea())){
            log.setHouseArea(customerInfo.getHouseArea());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAge())){
            log.setHouseAge(customerInfo.getHouseAge());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLoan())){
            log.setHouseLoan(customerInfo.getHouseLoan());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLocation())){
            log.setHouseLocation(customerInfo.getHouseLocation());
        }

        if (!StringUtils.isEmpty(customerInfo.getCity())){
            log.setCity(customerInfo.getCity());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerClassify())){
            log.setCustomerClassify(customerInfo.getCustomerClassify());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackStatus())){
            log.setCallbackStatus(customerInfo.getCallbackStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackTime())){
            //date 转
            log.setCallbackTime(customerInfo.getCallbackTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getSubCompanyId())){
            log.setSubCompanyId(customerInfo.getSubCompanyId());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            log.setCreateTime(customerInfo.getCreateTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            log.setLastUpdateTime(customerInfo.getLastUpdateTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            log.setCreateUser(customerInfo.getCreateUser());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateUser())){
            log.setLastUpdateUser(customerInfo.getLastUpdateUser());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsDeleted())){
            log.setIsDeleted(customerInfo.getIsDeleted());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseAlone())){
            log.setHouseAlone(customerInfo.getHouseAlone());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            log.setCreateTime(customerInfo.getCreateTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseLoanValue())){
            log.setHouseLoanValue(customerInfo.getHouseLoanValue());
        }
        //新版新增加字段
        if (!StringUtils.isEmpty(customerInfo.getExpectMoneyTime())){
            log.setExpectMoneyTime(customerInfo.getExpectMoneyTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getExpectLoanTime())){
            log.setExpectLoanTime(customerInfo.getExpectLoanTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getExpectRepaymentWay())){
            log.setExpectRepaymentWay(customerInfo.getExpectRepaymentWay());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseClear())){
            log.setHouseClear(customerInfo.getHouseClear());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseOwner())){
            log.setHouseOwner(customerInfo.getHouseOwner());
        }

        if (!StringUtils.isEmpty(customerInfo.getMortgageAmount())){
            log.setMortgageAmount(customerInfo.getMortgageAmount());
        }
        if (!StringUtils.isEmpty(customerInfo.getMortgaeMonth())){
            log.setMortgaeMonth(customerInfo.getMortgaeMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsHavaCar())){
            log.setIsHavaCar(customerInfo.getIsHavaCar());
        }

        if (!StringUtils.isEmpty(customerInfo.getCarWorth())){
            log.setCarWorth(customerInfo.getCarWorth());
        }
        if (!StringUtils.isEmpty(customerInfo.getCarAge())){
            log.setCarAge(customerInfo.getCarAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsHavaInsurance())){
            log.setIsHavaInsurance(customerInfo.getIsHavaInsurance());
        }

        if (!StringUtils.isEmpty(customerInfo.getYearPayAmount())){
            log.setYearPayAmount(customerInfo.getYearPayAmount());
        }

        if (!StringUtils.isEmpty(customerInfo.getInsuranceTime())){
            log.setInsuranceTime(customerInfo.getInsuranceTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsHavaDebt())){
            log.setIsHavaDebt(customerInfo.getIsHavaDebt());
        }

        if (!StringUtils.isEmpty(customerInfo.getDebtMoney())){
            log.setDebtMoney(customerInfo.getDebtMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtMonth())){
            log.setDebtMonth(customerInfo.getDebtMonth());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtOverdue())){
            log.setDebtOverdue(customerInfo.getDebtOverdue());
        }

        if (!StringUtils.isEmpty(customerInfo.getDebtOverdueMoney())){
            log.setDebtOverdueMoney(customerInfo.getDebtOverdueMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtTime())){
            log.setDebtTime(customerInfo.getDebtTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getWorkStatus())){
            log.setWorkStatus(customerInfo.getWorkStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getWagerCard())){
            log.setWagerCard(customerInfo.getWagerCard());
        }
        if (!StringUtils.isEmpty(customerInfo.getEntryTime())){
            log.setEntryTime(customerInfo.getEntryTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getCompanyType())){
            log.setCompanyType(customerInfo.getCompanyType());
        }

        if (!StringUtils.isEmpty(customerInfo.getSocialSecurity())){
            log.setSocialSecurity(customerInfo.getSocialSecurity());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialMoney())){
            log.setSocialMoney(customerInfo.getSocialMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialTime())){
            log.setSocialTime(customerInfo.getSocialTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getProvidentFund())){
            log.setProvidentFund(customerInfo.getProvidentFund());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvidentMoney())){
            log.setProvidentMoney(customerInfo.getProvidentMoney());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvidentTime())){
            log.setProvidentTime(customerInfo.getProvidentTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getCompnyName())){
            log.setCompnyName(customerInfo.getCompnyName());
        }
        if (!StringUtils.isEmpty(customerInfo.getFixedPhone())){
            log.setFixedPhone(customerInfo.getFixedPhone());
        }

    }

    public static void customerEntityToCallbackCustomerDto(CustomerInfo customerInfo, CallbackCusLoanDTO dto){

        dto.setCustomerId(customerInfo.getId());
        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            dto.setTelephonenumber(customerInfo.getTelephonenumber());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerName())){
            dto.setCustomerName(customerInfo.getCustomerName());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerLevel())){
            dto.setCustomerLevel(customerInfo.getCustomerLevel());
        }
        if (!StringUtils.isEmpty(customerInfo.getSparePhone())){
            dto.setSparePhone(customerInfo.getSparePhone());
        }
        if (!StringUtils.isEmpty(customerInfo.getAge())){
            dto.setAge(customerInfo.getAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getMarriage())){
            dto.setMarriage(customerInfo.getMarriage());
        }
        if (!StringUtils.isEmpty(customerInfo.getIdCard())){
            dto.setIdCard(customerInfo.getIdCard());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvinceHuji())){
            dto.setProvinceHuji(customerInfo.getProvinceHuji());
        }

        if (!StringUtils.isEmpty(customerInfo.getSex())){
            dto.setSex(customerInfo.getSex());
        }

        if (!StringUtils.isEmpty(customerInfo.getCustomerAddress())){
            dto.setCustomerAddress(customerInfo.getCustomerAddress());
        }

        if (!StringUtils.isEmpty(customerInfo.getPerDescription())){
            dto.setPerDescription(customerInfo.getPerDescription());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseStatus())){
            dto.setHouseStatus(customerInfo.getHouseStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAmount())){
            dto.setHouseAmount(customerInfo.getHouseAmount());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseType())){
            dto.setHouseType(customerInfo.getHouseType());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseValue())){
            dto.setHouseValue(customerInfo.getHouseValue());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseArea())){
            dto.setHouseArea(customerInfo.getHouseArea());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAge())){
            dto.setHouseAge(customerInfo.getHouseAge());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLoan())){
            dto.setHouseLoan(customerInfo.getHouseLoan());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLocation())){
            dto.setHouseLocation(customerInfo.getHouseLocation());
        }

        if (!StringUtils.isEmpty(customerInfo.getCity())){
            dto.setCity(customerInfo.getCity());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerClassify())){
            dto.setCustomerClassify(customerInfo.getCustomerClassify());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackStatus())){
            dto.setCallbackStatus(customerInfo.getCallbackStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackTime())){
            //date 转
            dto.setCallbackTime(customerInfo.getCallbackTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getSubCompanyId())){
            dto.setSubCompanyId(customerInfo.getSubCompanyId());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(customerInfo.getCreateTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            dto.setLastUpdateTime(customerInfo.getLastUpdateTime());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            dto.setCreateUser(customerInfo.getCreateUser());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateUser())){
            dto.setLastUpdateUser(customerInfo.getLastUpdateUser());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseAlone())){
            dto.setHouseAlone(customerInfo.getHouseAlone());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(customerInfo.getCreateTime());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerType())){
            dto.setCustomerType(customerInfo.getCustomerType());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerStreet())){
            dto.setCustomerStreet(customerInfo.getCustomerStreet());
        }
    }
}
