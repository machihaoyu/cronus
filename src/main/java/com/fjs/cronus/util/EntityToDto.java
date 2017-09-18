package com.fjs.cronus.util;

import com.fjs.cronus.dto.cronus.CustomerDto;
import com.fjs.cronus.dto.cronus.CustomerInterVibaseInfoDto;
import com.fjs.cronus.dto.cronus.CustomerInterViewBaseCarHouseInsturDto;
import com.fjs.cronus.model.*;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by msi on 2017/9/14.
 */
public class EntityToDto {


    public static void customerEntityToCustomerDto(CustomerInfo customerInfo, CustomerDto dto){

            dto.setId(customerInfo.getId().toString());
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
            dto.setCallbackTime(DateUtils.format(customerInfo.getCallbackTime()));
        }

        if (!StringUtils.isEmpty(customerInfo.getSubCompanyId())){
            dto.setSubCompanyId(customerInfo.getSubCompanyId().toString());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(DateUtils.format(customerInfo.getCreateTime(),DateUtils.FORMAT_LONG));
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            dto.setLastUpdateTime(DateUtils.format(customerInfo.getLastUpdateTime()));
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            dto.setCreateUser(customerInfo.getCreateUser().toString());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateUser())){
            dto.setLastUpdateUser(customerInfo.getLastUpdateUser().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsDeleted())){
            dto.setIsDeleted(customerInfo.getIsDeleted().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseAlone())){
            dto.setHouseAlone(customerInfo.getHouseAlone());
        }
    }

    public static void CustomerInterviewEntityToCustomerInterviewDto(CustomerInterviewBaseInfo customerInfo, CustomerInterVibaseInfoDto dto){

        dto.setId(customerInfo.getId().toString());
        if (!StringUtils.isEmpty(customerInfo.getCustomerId())){
            dto.setCustomerId(customerInfo.getCustomerId().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerId())){
            dto.setCustomerId(customerInfo.getCustomerId().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getOwnerUserId())){
            dto.setOwnerUserId(customerInfo.getOwnerUserId().toString());
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
            dto.setAge(customerInfo.getAge().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getBirthDate())){
            //TODO int 转为时间
            dto.setBirthDate(customerInfo.getBirthDate().toString());
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
            dto.setLoanTime(customerInfo.getLoanTime().toString());
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
            dto.setZhimaCredit(customerInfo.getZhimaCredit().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditQueryNumTwoMonth())){
            dto.setCreditQueryNumTwoMonth(customerInfo.getCreditQueryNumTwoMonth().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditQueryNumSixMonth())){
            dto.setCreditQueryNumSixMonth(customerInfo.getCreditQueryNumSixMonth().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getContinuityOverdueNumTwoYear())){
            dto.setContinuityOverdueNumTwoYear(customerInfo.getContinuityOverdueNumTwoYear().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getTotalOverdueNumTwoYear())){
            dto.setTotalOverdueNumTwoYear(customerInfo.getTotalOverdueNumTwoYear().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtAmount())){
            dto.setDebtAmount(customerInfo.getDebtAmount().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsOverdue())){
            dto.setIsOverdue(customerInfo.getIsOverdue());
        }
        if (!StringUtils.isEmpty(customerInfo.getOverdueAmount())){
            dto.setOverdueAmount(customerInfo.getOverdueAmount().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getIndustry())){
            dto.setIndustry(customerInfo.getIndustry());
        }
        if (!StringUtils.isEmpty(customerInfo.getIncomeAmount())){
            dto.setIncomeAmount(customerInfo.getIncomeAmount().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialSecurityDate())){
            //TODO int 转为时间
            dto.setSocialSecurityDate(customerInfo.getSocialSecurityDate().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialSecurityPayment())){
            dto.setSocialSecurityPayment(customerInfo.getSocialSecurityPayment().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getHousingFundDate())){
            dto.setHousingFundDate(customerInfo.getHousingFundDate().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getHousingFundPayment())){
            dto.setHousingFundPayment(customerInfo.getHousingFundPayment().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getWorkDate())){
            dto.setWorkDate(customerInfo.getWorkDate().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getCompanyRegisterDate())){
            dto.setCompanyRegisterDate(customerInfo.getCompanyRegisterDate().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getShareRate())){
            dto.setShareRate(customerInfo.getShareRate().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getPublicFlowYearAmount())){
            dto.setPublicFlowYearAmount(customerInfo.getPublicFlowYearAmount().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getPrivateFlowYearAmount())){
            dto.setPrivateFlowYearAmount(customerInfo.getPrivateFlowYearAmount().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsLitigation())){
             dto.setIsLitigation(customerInfo.getIsLitigation());
        }
        if (!StringUtils.isEmpty(customerInfo.getRetireDate())){
             dto.setRetireDate(customerInfo.getRetireDate().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getRetirementPayMinAmount())){
             dto.setRetirementPayMinAmount(customerInfo.getRetirementPayMinAmount().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsRelativeKnown())){
             dto.setIsRelativeKnown(customerInfo.getIsRelativeKnown());
        }
        if (!StringUtils.isEmpty(customerInfo.getRemark())){
             dto.setRemark(customerInfo.getRemark());
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(DateUtils.format(customerInfo.getCreateTime()));
        }
        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            dto.setLastUpdateTime(DateUtils.format(customerInfo.getLastUpdateTime()));
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            dto.setCreateUser(customerInfo.getCreateUser().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getLastUpdateUser())){
           dto.setLastUpdateUser(customerInfo.getLastUpdateUser().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsDeleted())){
           dto.setIsDeleted(customerInfo.getIsDeleted().toString());
        }
    }
    public static void CustomerInterviewEntityToCustomerInterviewAllInfoDto(CustomerInterViewBaseCarHouseInsturDto dto,
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
                dto.setHouseInfoId(customerInterviewInsuranceInfo.getId());
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
    public static void CustomerInterviewDtoToCustomerInterviewAllInfoEntity(CustomerInterViewBaseCarHouseInsturDto dto,
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


}
