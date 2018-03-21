package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by msi on 2017/9/15.
 */
public class CustomerInterVibaseInfoDTO {
    private Integer id;

    private Integer customerId;

    private Integer ownerUserId;

    private String ownerUserName;

    private String name;

    private String sex;

    private Integer age;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date birthDate;

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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastUpdateTime;

    private Integer createUser;

    private Integer lastUpdateUser;

    private Integer isDeleted;

    //


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getHouseholdRegister() {
        return householdRegister;
    }

    public void setHouseholdRegister(String householdRegister) {
        this.householdRegister = householdRegister;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getFeeChannelName() {
        return feeChannelName;
    }

    public void setFeeChannelName(String feeChannelName) {
        this.feeChannelName = feeChannelName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMonthInterestRate() {
        return monthInterestRate;
    }

    public void setMonthInterestRate(String monthInterestRate) {
        this.monthInterestRate = monthInterestRate;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Integer loanTime) {
        this.loanTime = loanTime;
    }

    public String getLoanUseTime() {
        return loanUseTime;
    }

    public void setLoanUseTime(String loanUseTime) {
        this.loanUseTime = loanUseTime;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCreditRecord() {
        return creditRecord;
    }

    public void setCreditRecord(String creditRecord) {
        this.creditRecord = creditRecord;
    }

    public Integer getZhimaCredit() {
        return zhimaCredit;
    }

    public void setZhimaCredit(Integer zhimaCredit) {
        this.zhimaCredit = zhimaCredit;
    }

    public Integer getCreditQueryNumTwoMonth() {
        return creditQueryNumTwoMonth;
    }

    public void setCreditQueryNumTwoMonth(Integer creditQueryNumTwoMonth) {
        this.creditQueryNumTwoMonth = creditQueryNumTwoMonth;
    }

    public Integer getCreditQueryNumSixMonth() {
        return creditQueryNumSixMonth;
    }

    public void setCreditQueryNumSixMonth(Integer creditQueryNumSixMonth) {
        this.creditQueryNumSixMonth = creditQueryNumSixMonth;
    }

    public Integer getContinuityOverdueNumTwoYear() {
        return continuityOverdueNumTwoYear;
    }

    public void setContinuityOverdueNumTwoYear(Integer continuityOverdueNumTwoYear) {
        this.continuityOverdueNumTwoYear = continuityOverdueNumTwoYear;
    }

    public Integer getTotalOverdueNumTwoYear() {
        return totalOverdueNumTwoYear;
    }

    public void setTotalOverdueNumTwoYear(Integer totalOverdueNumTwoYear) {
        this.totalOverdueNumTwoYear = totalOverdueNumTwoYear;
    }

    public Integer getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(Integer debtAmount) {
        this.debtAmount = debtAmount;
    }

    public String getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(String isOverdue) {
        this.isOverdue = isOverdue;
    }

    public Integer getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(Integer overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Integer getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Integer incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Integer getSocialSecurityDate() {
        return socialSecurityDate;
    }

    public void setSocialSecurityDate(Integer socialSecurityDate) {
        this.socialSecurityDate = socialSecurityDate;
    }

    public Integer getSocialSecurityPayment() {
        return socialSecurityPayment;
    }

    public void setSocialSecurityPayment(Integer socialSecurityPayment) {
        this.socialSecurityPayment = socialSecurityPayment;
    }

    public Integer getHousingFundDate() {
        return housingFundDate;
    }

    public void setHousingFundDate(Integer housingFundDate) {
        this.housingFundDate = housingFundDate;
    }

    public Integer getHousingFundPayment() {
        return housingFundPayment;
    }

    public void setHousingFundPayment(Integer housingFundPayment) {
        this.housingFundPayment = housingFundPayment;
    }

    public Integer getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Integer workDate) {
        this.workDate = workDate;
    }

    public Integer getCompanyRegisterDate() {
        return companyRegisterDate;
    }

    public void setCompanyRegisterDate(Integer companyRegisterDate) {
        this.companyRegisterDate = companyRegisterDate;
    }

    public BigDecimal getShareRate() {
        return shareRate;
    }

    public void setShareRate(BigDecimal shareRate) {
        this.shareRate = shareRate;
    }

    public Integer getPublicFlowYearAmount() {
        return publicFlowYearAmount;
    }

    public void setPublicFlowYearAmount(Integer publicFlowYearAmount) {
        this.publicFlowYearAmount = publicFlowYearAmount;
    }

    public Integer getPrivateFlowYearAmount() {
        return privateFlowYearAmount;
    }

    public void setPrivateFlowYearAmount(Integer privateFlowYearAmount) {
        this.privateFlowYearAmount = privateFlowYearAmount;
    }

    public String getIsLitigation() {
        return isLitigation;
    }

    public void setIsLitigation(String isLitigation) {
        this.isLitigation = isLitigation;
    }

    public Integer getRetireDate() {
        return retireDate;
    }

    public void setRetireDate(Integer retireDate) {
        this.retireDate = retireDate;
    }

    public Integer getRetirementPayMinAmount() {
        return retirementPayMinAmount;
    }

    public void setRetirementPayMinAmount(Integer retirementPayMinAmount) {
        this.retirementPayMinAmount = retirementPayMinAmount;
    }

    public String getIsRelativeKnown() {
        return isRelativeKnown;
    }

    public void setIsRelativeKnown(String isRelativeKnown) {
        this.isRelativeKnown = isRelativeKnown;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(Integer lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
