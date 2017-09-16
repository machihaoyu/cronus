package com.fjs.cronus.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerInterviewBaseInfo extends  BaseModel {
    private Integer id;

    private Integer customerId;

    private Integer ownerUserId;

    private String ownerUserName;

    private String name;

    private String sex;

    private Integer age;

    private Integer birthDate;

    private String telephonenumber;

    private String maritalStatus;

    private String householdRegister;

    private String education;

    private String feeChannelName;

    private String productName;

    private String monthInterestRate;

    private String serviceCharge;

    private String loanAmount;

    private Integer loanTime;

    private String loanUseTime;

    private String loanPurpose;

    private String paymentType;

    private String creditRecord;

    private Integer zhimaCredit;

    private Integer creditQueryNumTwoMonth;

    private Integer creditQueryNumSixMonth;

    private Integer continuityOverdueNumTwoYear;

    private Integer totalOverdueNumTwoYear;

    private Integer debtAmount;

    private String isOverdue;

    private Integer overdueAmount;

    private String industry;

    private Integer incomeAmount;

    private Integer socialSecurityDate;

    private Integer socialSecurityPayment;

    private Integer housingFundDate;

    private Integer housingFundPayment;

    private Integer workDate;

    private Integer companyRegisterDate;

    private BigDecimal shareRate;

    private Integer publicFlowYearAmount;

    private Integer privateFlowYearAmount;

    private String isLitigation;

    private Integer retireDate;

    private Integer retirementPayMinAmount;

    private String isRelativeKnown;

    private String remark;

    public Integer getId() {
        return id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getBirthDate() {
        return birthDate;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getHouseholdRegister() {
        return householdRegister;
    }

    public String getEducation() {
        return education;
    }

    public String getFeeChannelName() {
        return feeChannelName;
    }

    public String getProductName() {
        return productName;
    }

    public String getMonthInterestRate() {
        return monthInterestRate;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public Integer getLoanTime() {
        return loanTime;
    }

    public String getLoanUseTime() {
        return loanUseTime;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getCreditRecord() {
        return creditRecord;
    }

    public Integer getZhimaCredit() {
        return zhimaCredit;
    }

    public Integer getCreditQueryNumTwoMonth() {
        return creditQueryNumTwoMonth;
    }

    public Integer getCreditQueryNumSixMonth() {
        return creditQueryNumSixMonth;
    }

    public Integer getContinuityOverdueNumTwoYear() {
        return continuityOverdueNumTwoYear;
    }

    public Integer getTotalOverdueNumTwoYear() {
        return totalOverdueNumTwoYear;
    }

    public Integer getDebtAmount() {
        return debtAmount;
    }

    public String getIsOverdue() {
        return isOverdue;
    }

    public Integer getOverdueAmount() {
        return overdueAmount;
    }

    public String getIndustry() {
        return industry;
    }

    public Integer getIncomeAmount() {
        return incomeAmount;
    }

    public Integer getSocialSecurityDate() {
        return socialSecurityDate;
    }

    public Integer getSocialSecurityPayment() {
        return socialSecurityPayment;
    }

    public Integer getHousingFundDate() {
        return housingFundDate;
    }

    public Integer getHousingFundPayment() {
        return housingFundPayment;
    }

    public Integer getWorkDate() {
        return workDate;
    }

    public Integer getCompanyRegisterDate() {
        return companyRegisterDate;
    }

    public BigDecimal getShareRate() {
        return shareRate;
    }

    public Integer getPublicFlowYearAmount() {
        return publicFlowYearAmount;
    }

    public Integer getPrivateFlowYearAmount() {
        return privateFlowYearAmount;
    }

    public String getIsLitigation() {
        return isLitigation;
    }

    public Integer getRetireDate() {
        return retireDate;
    }

    public Integer getRetirementPayMinAmount() {
        return retirementPayMinAmount;
    }

    public String getIsRelativeKnown() {
        return isRelativeKnown;
    }

    public String getRemark() {
        return remark;
    }
}