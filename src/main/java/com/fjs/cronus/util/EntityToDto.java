package com.fjs.cronus.util;

import com.fjs.cronus.dto.cronus.CustomerDto;
import com.fjs.cronus.dto.cronus.CustomerInterVibaseInfoDto;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerInterviewBaseInfo;
import org.springframework.util.StringUtils;

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

      /*  dto.setId(customerInfo.getId().toString());
        if (!StringUtils.isEmpty(customerInfo.getCustomerId())){
            dto.setCustomerId(customerInfo.getCustomerId().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerId())){
            dto.setCustomerId();
        }
        if (!StringUtils.isEmpty(customerInfo.getOwnerUserId())){
            dto.setOwnerUserId();
        }
        if (!StringUtils.isEmpty(customerInfo.getOwnerUserName())){
            dto.setOwnerUserName();
        }
        if (!StringUtils.isEmpty(customerInfo.getName())){
            dto.setName();
        }
        if (!StringUtils.isEmpty(customerInfo.getSex())){
            dto.setSex();
        }
        if (!StringUtils.isEmpty(customerInfo.getAge())){
            dto.setAge();
        }
        if (!StringUtils.isEmpty(customerInfo.getBirthDate())){
            dto.setBirthDate();
        }
        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            dto.setTelephonenumber();
        }
        if (!StringUtils.isEmpty(customerInfo.getMaritalStatus())){
            dto.setMaritalStatus();
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseholdRegister())){
            dto.setHouseholdRegister();
        }
        if (!StringUtils.isEmpty(customerInfo.getEducation())){
            dto.setEducation();
        }
        if (!StringUtils.isEmpty(customerInfo.getFeeChannelName())){
            dto.setFeeChannelName();
        }
        if (!StringUtils.isEmpty(customerInfo.getProductName())){
            dto.setProductName();
        }
        if (!StringUtils.isEmpty(customerInfo.getMonthInterestRate())){
            dto.setMonthInterestRate();
        }
        if (!StringUtils.isEmpty(customerInfo.getServiceCharge())){
            dto.setServiceCharge();
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanAmount())){
            dto.setLoanAmount();
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanTime())){
            dto.setLoanTime();
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanUseTime())){
            dto.setLoanUseTime();
        }
        if (!StringUtils.isEmpty(customerInfo.getLoanPurpose())){
            dto.setLoanPurpose();
        }
        if (!StringUtils.isEmpty(customerInfo.getPaymentType())){
            dto.setPaymentType();
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditRecord())){
            dto.setCreditRecord();
        }  if (!StringUtils.isEmpty(customerInfo.getZhimaCredit())){
            dto.setZhimaCredit();
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditQueryNumTwoMonth())){
            dto.setCreditQueryNumTwoMonth();
        }
        if (!StringUtils.isEmpty(customerInfo.getCreditQueryNumSixMonth())){
            dto.setCreditQueryNumSixMonth();
        }
        if (!StringUtils.isEmpty(customerInfo.getContinuityOverdueNumTwoYear())){
            dto.setContinuityOverdueNumTwoYear();
        }
        if (!StringUtils.isEmpty(customerInfo.getTotalOverdueNumTwoYear())){
            dto.setTotalOverdueNumTwoYear();
        }
        if (!StringUtils.isEmpty(customerInfo.getDebtAmount())){
            dto.setDebtAmount();
        }
        if (!StringUtils.isEmpty(customerInfo.getIsOverdue())){
            dto.setIsOverdue();
        }
        if (!StringUtils.isEmpty(customerInfo.getOverdueAmount())){
            dto.setOverdueAmount();
        }
        if (!StringUtils.isEmpty(customerInfo.getIndustry())){
            dto.setIndustry();
        }
        if (!StringUtils.isEmpty(customerInfo.getIncomeAmount())){
            dto.setIncomeAmount();
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialSecurityDate())){
            dto.setSocialSecurityDate();
        }
        if (!StringUtils.isEmpty(customerInfo.getSocialSecurityPayment())){
            dto.setSocialSecurityPayment();
        }
        if (!StringUtils.isEmpty(customerInfo.getHousingFundDate())){
            dto.setHousingFundDate();
        }
        if (!StringUtils.isEmpty(customerInfo.getHousingFundPayment())){
            dto.setHousingFundPayment();
        }
        if (!StringUtils.isEmpty(customerInfo.getWorkDate())){
            dto.setWorkDate();
        }
        if (!StringUtils.isEmpty(customerInfo.getCompanyRegisterDate())){
            dto.setCompanyRegisterDate();
        }
        if (!StringUtils.isEmpty(customerInfo.getShareRate())){
            dto.setShareRate();
        }
        if (!StringUtils.isEmpty(customerInfo.getPublicFlowYearAmount())){
            dto.setPublicFlowYearAmount();
        }
        if (!StringUtils.isEmpty(customerInfo.getPrivateFlowYearAmount())){
            dto.setPrivateFlowYearAmount();
        }
        if (!StringUtils.isEmpty(customerInfo.getIsLitigation())){
             dto.setIsLitigation();
        }
        if (!StringUtils.isEmpty(customerInfo.getRetireDate())){
             dto.setRetireDate();
        }
        if (!StringUtils.isEmpty(customerInfo.getRetirementPayMinAmount())){
             dto.setRetirementPayMinAmount();
        }
        if (!StringUtils.isEmpty(customerInfo.getIsRelativeKnown())){
             dto.setIsRelativeKnown();
        }
        if (!StringUtils.isEmpty(customerInfo.getRemark())){
             dto.setRemark();
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime();
        }
        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            dto.setLastUpdateTime();
        }
        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            dto.setCreateUser();
        }
        if (!StringUtils.isEmpty(customerInfo.getLastUpdateUser())){
           dto.setLastUpdateUser();
        }
        if (!StringUtils.isEmpty(customerInfo.getIsDeleted())){
           dto.setIsDeleted();
        }
*/



    }

}
