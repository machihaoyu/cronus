package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by msi on 2017/9/18.
 */
public class CustomerInterViewBaseCarHouseInsturDTO implements Serializable{
    private static final long serialVersionUID = 6841315540181335028L;
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

    /**
     * 车辆信息
     */
    private Integer carInfoid;

    private Integer customerInterviewBaseInfoId;

    private String carType;

    private String licencePlateLocation;

    private Integer buyDate;

    private Integer carMortgagePaidNum;

    private Integer carMortgageMonthAmount;

    private BigDecimal priceNow;

    private String isFullInsurance;

    /**
     * 房产信息
     */
    private Integer houseInfoId;

    private String accepthousearea;

    private String houseStatus;

    private String housePropertyType;

    private BigDecimal area;

    private Integer buildDate;

    private Integer housePropertyRightsNum;

    private String isChildInPropertyRigths;

    private String isOldInPropertyRigths;

    private String isPropertyRightsClear;

    private String isOtherHouse;

    private String isBankFlow;

    private Integer bankFlowMonthAmount;

    private Integer houseMortgageMonthAmount;

    private Integer houseMortgagePaidNum;

    /**
     * 保险信息
     */
    private Integer insuranceInfoId;

    private String insuranceCompany;

    private String insuranceType;

    private String payType;

    private Integer yearPayAmount;

    private Integer monthPayAmount;
    private Integer effectDate;

    private String isSuspend;

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

    public Integer getCarInfoid() {
        return carInfoid;
    }

    public void setCarInfoid(Integer carInfoid) {
        this.carInfoid = carInfoid;
    }

    public Integer getCustomerInterviewBaseInfoId() {
        return customerInterviewBaseInfoId;
    }

    public void setCustomerInterviewBaseInfoId(Integer customerInterviewBaseInfoId) {
        this.customerInterviewBaseInfoId = customerInterviewBaseInfoId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getLicencePlateLocation() {
        return licencePlateLocation;
    }

    public void setLicencePlateLocation(String licencePlateLocation) {
        this.licencePlateLocation = licencePlateLocation;
    }

    public Integer getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Integer buyDate) {
        this.buyDate = buyDate;
    }

    public Integer getCarMortgagePaidNum() {
        return carMortgagePaidNum;
    }

    public void setCarMortgagePaidNum(Integer carMortgagePaidNum) {
        this.carMortgagePaidNum = carMortgagePaidNum;
    }

    public Integer getCarMortgageMonthAmount() {
        return carMortgageMonthAmount;
    }

    public void setCarMortgageMonthAmount(Integer carMortgageMonthAmount) {
        this.carMortgageMonthAmount = carMortgageMonthAmount;
    }

    public BigDecimal getPriceNow() {
        return priceNow;
    }

    public void setPriceNow(BigDecimal priceNow) {
        this.priceNow = priceNow;
    }

    public String getIsFullInsurance() {
        return isFullInsurance;
    }

    public void setIsFullInsurance(String isFullInsurance) {
        this.isFullInsurance = isFullInsurance;
    }

    public Integer getHouseInfoId() {
        return houseInfoId;
    }

    public void setHouseInfoId(Integer houseInfoId) {
        this.houseInfoId = houseInfoId;
    }

    public String getAccepthousearea() {
        return accepthousearea;
    }

    public void setAccepthousearea(String accepthousearea) {
        this.accepthousearea = accepthousearea;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getHousePropertyType() {
        return housePropertyType;
    }

    public void setHousePropertyType(String housePropertyType) {
        this.housePropertyType = housePropertyType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Integer getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Integer buildDate) {
        this.buildDate = buildDate;
    }

    public Integer getHousePropertyRightsNum() {
        return housePropertyRightsNum;
    }

    public void setHousePropertyRightsNum(Integer housePropertyRightsNum) {
        this.housePropertyRightsNum = housePropertyRightsNum;
    }

    public String getIsChildInPropertyRigths() {
        return isChildInPropertyRigths;
    }

    public void setIsChildInPropertyRigths(String isChildInPropertyRigths) {
        this.isChildInPropertyRigths = isChildInPropertyRigths;
    }

    public String getIsOldInPropertyRigths() {
        return isOldInPropertyRigths;
    }

    public void setIsOldInPropertyRigths(String isOldInPropertyRigths) {
        this.isOldInPropertyRigths = isOldInPropertyRigths;
    }

    public String getIsPropertyRightsClear() {
        return isPropertyRightsClear;
    }

    public void setIsPropertyRightsClear(String isPropertyRightsClear) {
        this.isPropertyRightsClear = isPropertyRightsClear;
    }

    public String getIsOtherHouse() {
        return isOtherHouse;
    }

    public void setIsOtherHouse(String isOtherHouse) {
        this.isOtherHouse = isOtherHouse;
    }

    public String getIsBankFlow() {
        return isBankFlow;
    }

    public void setIsBankFlow(String isBankFlow) {
        this.isBankFlow = isBankFlow;
    }

    public Integer getBankFlowMonthAmount() {
        return bankFlowMonthAmount;
    }

    public void setBankFlowMonthAmount(Integer bankFlowMonthAmount) {
        this.bankFlowMonthAmount = bankFlowMonthAmount;
    }

    public Integer getHouseMortgageMonthAmount() {
        return houseMortgageMonthAmount;
    }

    public void setHouseMortgageMonthAmount(Integer houseMortgageMonthAmount) {
        this.houseMortgageMonthAmount = houseMortgageMonthAmount;
    }

    public Integer getHouseMortgagePaidNum() {
        return houseMortgagePaidNum;
    }

    public void setHouseMortgagePaidNum(Integer houseMortgagePaidNum) {
        this.houseMortgagePaidNum = houseMortgagePaidNum;
    }

    public Integer getInsuranceInfoId() {
        return insuranceInfoId;
    }

    public void setInsuranceInfoId(Integer insuranceInfoId) {
        this.insuranceInfoId = insuranceInfoId;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getYearPayAmount() {
        return yearPayAmount;
    }

    public void setYearPayAmount(Integer yearPayAmount) {
        this.yearPayAmount = yearPayAmount;
    }

    public Integer getMonthPayAmount() {
        return monthPayAmount;
    }

    public void setMonthPayAmount(Integer monthPayAmount) {
        this.monthPayAmount = monthPayAmount;
    }

    public Integer getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Integer effectDate) {
        this.effectDate = effectDate;
    }

    public String getIsSuspend() {
        return isSuspend;
    }

    public void setIsSuspend(String isSuspend) {
        this.isSuspend = isSuspend;
    }
}
