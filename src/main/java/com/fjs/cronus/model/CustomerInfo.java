package com.fjs.cronus.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CustomerInfo extends BaseModel {
    private Integer id;

    private String telephonenumber;

    private String customerName;

    private String customerType;

    private String customerLevel;

    private String sparePhone;

    private String age;

    private String marriage;

    private String idCard;

    private String provinceHuji;

    private String sex;

    private String customerAddress;

    private String customerStreet;

    private String houseStatus;

    private String houseAmount;

    private String houseType;

    private String houseValue;

    private String houseArea;

    private String houseAge;

    private String houseLoan;

    private String houseAlone;

    private String houseLocation;

    private String city;

    private String customerClassify;

    private String callbackStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date callbackTime;

    private Integer subCompanyId;

    private String houseLoanValue;

    private String perDescription;

    //新版本添加字段
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date expectMoneyTime;

    private String expectLoanTime;

    private Integer expectRepaymentWay;

    private Integer houseClear;

    private String houseOwner;

    private String mortgageAmount;

    private String mortgaeMonth;

    private Integer isHavaCar;

    private String carWorth;

    private Integer carAge;

    private Integer isHavaInsurance;

    private String yearPayAmount;

    private Integer insuranceTime;

    private Integer isHavaDebt;

    private String debtMoney;

    private Integer debtMonth;

    private Integer debtOverdue;

    private String debtOverdueMoney;

    private Integer debtTime;

    private Integer workStatus;

    private String wagerCard;

    private Integer entryTime;

    private Integer companyType;

    private Integer socialSecurity;

    private String socialMoney;

    private Integer socialTime;

    private Integer providentFund;

    private String providentMoney;

    private Integer providentTime;

    private String compnyName;

    private Integer fixedPhone;

    private BigDecimal loanAmount;

    private String customerSource;

    private String utmSource;

    private Integer ownUserId;

    private String ownUserName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date receiveTime;

    private Integer circle;

    private String employedInfo;

    private String retirementWages;

    private String ext;

    private String level;

    private Integer remain;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date viewTime;

    private Integer viewUid;

    private Integer viewCount;

    private Integer autostatus;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date firstCommunicateTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date firstAllocateTime;

    private String  cooperationStatus;

    private Integer  confirm;

    private Integer clickCommunicateButton;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date communicateTime;

    private Integer companyId;

    private Integer communicateId;

    private Integer receiveId;

    private Integer ocdcId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Date getCommunicateTime() {
        return communicateTime;
    }

    public void setCommunicateTime(Date communicateTime) {
        this.communicateTime = communicateTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public Date getViewTime() {
        return viewTime;
    }

    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
    }

    public Integer getViewUid() {
        return viewUid;
    }

    public void setViewUid(Integer viewUid) {
        this.viewUid = viewUid;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getAutostatus() {
        return autostatus;
    }

    public void setAutostatus(Integer autostatus) {
        this.autostatus = autostatus;
    }

    public Date getFirstCommunicateTime() {
        return firstCommunicateTime;
    }

    public void setFirstCommunicateTime(Date firstCommunicateTime) {
        this.firstCommunicateTime = firstCommunicateTime;
    }

    public Date getFirstAllocateTime() {
        return firstAllocateTime;
    }

    public void setFirstAllocateTime(Date firstAllocateTime) {
        this.firstAllocateTime = firstAllocateTime;
    }

    public String getCooperationStatus() {
        return cooperationStatus;
    }

    public void setCooperationStatus(String cooperationStatus) {
        this.cooperationStatus = cooperationStatus;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    public Integer getClickCommunicateButton() {
        return clickCommunicateButton;
    }

    public void setClickCommunicateButton(Integer clickCommunicateButton) {
        this.clickCommunicateButton = clickCommunicateButton;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getEmployedInfo() {
        return employedInfo;
    }

    public void setEmployedInfo(String employedInfo) {
        this.employedInfo = employedInfo;
    }

    public String getRetirementWages() {
        return retirementWages;
    }

    public void setRetirementWages(String retirementWages) {
        this.retirementWages = retirementWages;
    }

    public Integer getCircle() {
        return circle;
    }

    public void setCircle(Integer circle) {
        this.circle = circle;
    }

    public Integer getOwnUserId() {
        return ownUserId;
    }

    public void setOwnUserId(Integer ownUserId) {
        this.ownUserId = ownUserId;
    }

    public String getOwnUserName() {
        return ownUserName;
    }

    public void setOwnUserName(String ownUserName) {
        this.ownUserName = ownUserName;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public Date getExpectMoneyTime() {
        return expectMoneyTime;
    }

    public void setExpectMoneyTime(Date expectMoneyTime) {
        this.expectMoneyTime = expectMoneyTime;
    }

    public String getExpectLoanTime() {
        return expectLoanTime;
    }

    public void setExpectLoanTime(String expectLoanTime) {
        this.expectLoanTime = expectLoanTime;
    }

    public Integer getExpectRepaymentWay() {
        return expectRepaymentWay;
    }

    public void setExpectRepaymentWay(Integer expectRepaymentWay) {
        this.expectRepaymentWay = expectRepaymentWay;
    }

    public Integer getHouseClear() {
        return houseClear;
    }

    public void setHouseClear(Integer houseClear) {
        this.houseClear = houseClear;
    }

    public String getHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(String houseOwner) {
        this.houseOwner = houseOwner;
    }

    public String getMortgageAmount() {
        return mortgageAmount;
    }

    public void setMortgageAmount(String mortgageAmount) {
        this.mortgageAmount = mortgageAmount;
    }

    public String getMortgaeMonth() {
        return mortgaeMonth;
    }

    public void setMortgaeMonth(String mortgaeMonth) {
        this.mortgaeMonth = mortgaeMonth;
    }

    public Integer getIsHavaCar() {
        return isHavaCar;
    }

    public void setIsHavaCar(Integer isHavaCar) {
        this.isHavaCar = isHavaCar;
    }

    public String getCarWorth() {
        return carWorth;
    }

    public void setCarWorth(String carWorth) {
        this.carWorth = carWorth;
    }

    public Integer getCarAge() {
        return carAge;
    }

    public void setCarAge(Integer carAge) {
        this.carAge = carAge;
    }

    public Integer getIsHavaInsurance() {
        return isHavaInsurance;
    }

    public void setIsHavaInsurance(Integer isHavaInsurance) {
        this.isHavaInsurance = isHavaInsurance;
    }

    public String getYearPayAmount() {
        return yearPayAmount;
    }

    public void setYearPayAmount(String yearPayAmount) {
        this.yearPayAmount = yearPayAmount;
    }

    public Integer getInsuranceTime() {
        return insuranceTime;
    }

    public void setInsuranceTime(Integer insuranceTime) {
        this.insuranceTime = insuranceTime;
    }

    public Integer getIsHavaDebt() {
        return isHavaDebt;
    }

    public void setIsHavaDebt(Integer isHavaDebt) {
        this.isHavaDebt = isHavaDebt;
    }

    public String getDebtMoney() {
        return debtMoney;
    }

    public void setDebtMoney(String debtMoney) {
        this.debtMoney = debtMoney;
    }

    public Integer getDebtMonth() {
        return debtMonth;
    }

    public void setDebtMonth(Integer debtMonth) {
        this.debtMonth = debtMonth;
    }

    public Integer getDebtOverdue() {
        return debtOverdue;
    }

    public void setDebtOverdue(Integer debtOverdue) {
        this.debtOverdue = debtOverdue;
    }

    public String getDebtOverdueMoney() {
        return debtOverdueMoney;
    }

    public void setDebtOverdueMoney(String debtOverdueMoney) {
        this.debtOverdueMoney = debtOverdueMoney;
    }

    public Integer getDebtTime() {
        return debtTime;
    }

    public void setDebtTime(Integer debtTime) {
        this.debtTime = debtTime;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public String getWagerCard() {
        return wagerCard;
    }

    public void setWagerCard(String wagerCard) {
        this.wagerCard = wagerCard;
    }

    public Integer getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Integer entryTime) {
        this.entryTime = entryTime;
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public Integer getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(Integer socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public String getSocialMoney() {
        return socialMoney;
    }

    public void setSocialMoney(String socialMoney) {
        this.socialMoney = socialMoney;
    }

    public Integer getSocialTime() {
        return socialTime;
    }

    public void setSocialTime(Integer socialTime) {
        this.socialTime = socialTime;
    }

    public Integer getProvidentFund() {
        return providentFund;
    }

    public void setProvidentFund(Integer providentFund) {
        this.providentFund = providentFund;
    }

    public String getProvidentMoney() {
        return providentMoney;
    }

    public void setProvidentMoney(String providentMoney) {
        this.providentMoney = providentMoney;
    }

    public Integer getProvidentTime() {
        return providentTime;
    }

    public void setProvidentTime(Integer providentTime) {
        this.providentTime = providentTime;
    }

    public String getCompnyName() {
        return compnyName;
    }

    public void setCompnyName(String compnyName) {
        this.compnyName = compnyName;
    }

    public Integer getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(Integer fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getHouseLoanValue() {
        return houseLoanValue;
    }
   
    public void setHouseLoanValue(String houseLoanValue) {
        this.houseLoanValue = houseLoanValue;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getCustomerStreet() {
        return customerStreet;
    }

    public void setCustomerStreet(String customerStreet) {
        this.customerStreet = customerStreet;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getSparePhone() {
        return sparePhone;
    }

    public void setSparePhone(String sparePhone) {
        this.sparePhone = sparePhone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getProvinceHuji() {
        return provinceHuji;
    }

    public void setProvinceHuji(String provinceHuji) {
        this.provinceHuji = provinceHuji;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getHouseAmount() {
        return houseAmount;
    }

    public void setHouseAmount(String houseAmount) {
        this.houseAmount = houseAmount;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getHouseValue() {
        return houseValue;
    }

    public void setHouseValue(String houseValue) {
        this.houseValue = houseValue;
    }

    public String getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public String getHouseAge() {
        return houseAge;
    }

    public void setHouseAge(String houseAge) {
        this.houseAge = houseAge;
    }

    public String getHouseLoan() {
        return houseLoan;
    }

    public void setHouseLoan(String houseLoan) {
        this.houseLoan = houseLoan;
    }

    public String getHouseAlone() {
        return houseAlone;
    }

    public void setHouseAlone(String houseAlone) {
        this.houseAlone = houseAlone;
    }

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomerClassify() {
        return customerClassify;
    }

    public void setCustomerClassify(String customerClassify) {
        this.customerClassify = customerClassify;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(String callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public Date getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getPerDescription() {
        return perDescription;
    }

    public void setPerDescription(String perDescription) {
        this.perDescription = perDescription;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getCommunicateId() {
        return communicateId;
    }

    public void setCommunicateId(Integer communicateId) {
        this.communicateId = communicateId;
    }

    public Integer getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Integer receiveId) {
        this.receiveId = receiveId;
    }

    public Integer getOcdcId() {
        return ocdcId;
    }

    public void setOcdcId(Integer ocdcId) {
        this.ocdcId = ocdcId;
    }

    @Override
    public String toString() {
        return "CustomerInfo{" +
                "id=" + id +
                ", telephonenumber='" + telephonenumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerType='" + customerType + '\'' +
                ", customerLevel='" + customerLevel + '\'' +
                ", sparePhone='" + sparePhone + '\'' +
                ", age='" + age + '\'' +
                ", marriage='" + marriage + '\'' +
                ", idCard='" + idCard + '\'' +
                ", provinceHuji='" + provinceHuji + '\'' +
                ", sex='" + sex + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerStreet='" + customerStreet + '\'' +
                ", houseStatus='" + houseStatus + '\'' +
                ", houseAmount='" + houseAmount + '\'' +
                ", houseType='" + houseType + '\'' +
                ", houseValue='" + houseValue + '\'' +
                ", houseArea='" + houseArea + '\'' +
                ", houseAge='" + houseAge + '\'' +
                ", houseLoan='" + houseLoan + '\'' +
                ", houseAlone='" + houseAlone + '\'' +
                ", houseLocation='" + houseLocation + '\'' +
                ", city='" + city + '\'' +
                ", customerClassify='" + customerClassify + '\'' +
                ", callbackStatus='" + callbackStatus + '\'' +
                ", callbackTime=" + callbackTime +
                ", subCompanyId=" + subCompanyId +
                ", houseLoanValue='" + houseLoanValue + '\'' +
                ", perDescription='" + perDescription + '\'' +
                ", expectMoneyTime=" + expectMoneyTime +
                ", expectLoanTime='" + expectLoanTime + '\'' +
                ", expectRepaymentWay=" + expectRepaymentWay +
                ", houseClear=" + houseClear +
                ", houseOwner='" + houseOwner + '\'' +
                ", mortgageAmount='" + mortgageAmount + '\'' +
                ", mortgaeMonth='" + mortgaeMonth + '\'' +
                ", isHavaCar=" + isHavaCar +
                ", carWorth='" + carWorth + '\'' +
                ", carAge=" + carAge +
                ", isHavaInsurance=" + isHavaInsurance +
                ", yearPayAmount='" + yearPayAmount + '\'' +
                ", insuranceTime=" + insuranceTime +
                ", isHavaDebt=" + isHavaDebt +
                ", debtMoney='" + debtMoney + '\'' +
                ", debtMonth=" + debtMonth +
                ", debtOverdue=" + debtOverdue +
                ", debtOverdueMoney='" + debtOverdueMoney + '\'' +
                ", debtTime=" + debtTime +
                ", workStatus=" + workStatus +
                ", wagerCard='" + wagerCard + '\'' +
                ", entryTime=" + entryTime +
                ", companyType=" + companyType +
                ", socialSecurity=" + socialSecurity +
                ", socialMoney='" + socialMoney + '\'' +
                ", socialTime=" + socialTime +
                ", providentFund=" + providentFund +
                ", providentMoney='" + providentMoney + '\'' +
                ", providentTime=" + providentTime +
                ", compnyName='" + compnyName + '\'' +
                ", fixedPhone=" + fixedPhone +
                ", loanAmount=" + loanAmount +
                ", customerSource='" + customerSource + '\'' +
                ", utmSource='" + utmSource + '\'' +
                ", ownUserId=" + ownUserId +
                ", ownUserName='" + ownUserName + '\'' +
                ", receiveTime=" + receiveTime +
                ", circle=" + circle +
                ", employedInfo='" + employedInfo + '\'' +
                ", retirementWages='" + retirementWages + '\'' +
                ", ext='" + ext + '\'' +
                ", level='" + level + '\'' +
                ", remain=" + remain +
                ", viewTime=" + viewTime +
                ", viewUid=" + viewUid +
                ", viewCount=" + viewCount +
                ", autostatus=" + autostatus +
                ", firstCommunicateTime=" + firstCommunicateTime +
                ", firstAllocateTime=" + firstAllocateTime +
                ", cooperationStatus='" + cooperationStatus + '\'' +
                ", confirm=" + confirm +
                ", clickCommunicateButton=" + clickCommunicateButton +
                ", communicateTime=" + communicateTime +
                ", companyId=" + companyId +
                ", communicateId=" + communicateId +
                ", receiveId=" + receiveId +
                ", ocdcId=" + ocdcId +
                '}';
    }
}