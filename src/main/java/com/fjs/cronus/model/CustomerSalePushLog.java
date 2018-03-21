package com.fjs.cronus.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRawValue;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by feng on 2017/9/18.
 */
public class CustomerSalePushLog {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer loanId;
    private Integer customerId;
    private Integer ocdcId;
    private String telephonenumber;
    private String customerName;
    private Integer ownerUserId;
    private String ownerUserName;
    private Integer createrUserId;
    private String customerLevel;
    private BigDecimal loanAmount;
    private String sparePhone;
    private String age;
    private String marriage;
    private String idCard;
    private String provinceHuji;

    private String sex;

    private String customerAddress;

    private String perDescription;

    private String houseAmount;

    private String houseType;

    private String houseValue;

    private String houseArea;

    private String houseAge;

    private String houseLoan;

    private String houseAlone;

    private String houseLocation;

    private String city;

    private Integer retain;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date receiveTime;

    private Integer isLock;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date phoneViewTime;

    private Integer phoneViewUid;

    private Integer phoneViewCount;

    private Integer autostatus;

    private String utmSource;

    private String customerSource;

    private String customerClassify;

    private Integer laiyuan;

    @JsonRawValue
    private String ext;

    private String houseStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date repeatCallbackTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOcdcId() {
        return ocdcId;
    }

    public void setOcdcId(Integer ocdcId) {
        this.ocdcId = ocdcId;
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

    public Integer getCreaterUserId() {
        return createrUserId;
    }

    public void setCreaterUserId(Integer createrUserId) {
        this.createrUserId = createrUserId;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
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

    public String getPerDescription() {
        return perDescription;
    }

    public void setPerDescription(String perDescription) {
        this.perDescription = perDescription;
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

    public Integer getRetain() {
        return retain;
    }

    public void setRetain(Integer retain) {
        this.retain = retain;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public Date getPhoneViewTime() {
        return phoneViewTime;
    }

    public void setPhoneViewTime(Date phoneViewTime) {
        this.phoneViewTime = phoneViewTime;
    }

    public Integer getPhoneViewUid() {
        return phoneViewUid;
    }

    public void setPhoneViewUid(Integer phoneViewUid) {
        this.phoneViewUid = phoneViewUid;
    }

    public Integer getPhoneViewCount() {
        return phoneViewCount;
    }

    public void setPhoneViewCount(Integer phoneViewCount) {
        this.phoneViewCount = phoneViewCount;
    }

    public Integer getAutostatus() {
        return autostatus;
    }

    public void setAutostatus(Integer autostatus) {
        this.autostatus = autostatus;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getCustomerClassify() {
        return customerClassify;
    }

    public void setCustomerClassify(String customerClassify) {
        this.customerClassify = customerClassify;
    }

    public Integer getLaiyuan() {
        return laiyuan;
    }

    public void setLaiyuan(Integer laiyuan) {
        this.laiyuan = laiyuan;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public Date getRepeatCallbackTime() {
        return repeatCallbackTime;
    }

    public void setRepeatCallbackTime(Date repeatCallbackTime) {
        this.repeatCallbackTime = repeatCallbackTime;
    }

    @Override
    public String toString() {
        return "CustomerSalePushLogCopy{" +
                "id=" + id +
                ", loanId=" + loanId +
                ", customerId=" + customerId +
                ", ocdcId=" + ocdcId +
                ", telephonenumber='" + telephonenumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", ownerUserId=" + ownerUserId +
                ", ownerUserName='" + ownerUserName + '\'' +
                ", createrUserId=" + createrUserId +
                ", customerLevel='" + customerLevel + '\'' +
                ", loanAmount=" + loanAmount +
                ", sparePhone='" + sparePhone + '\'' +
                ", age='" + age + '\'' +
                ", marriage='" + marriage + '\'' +
                ", idCard='" + idCard + '\'' +
                ", provinceHuji='" + provinceHuji + '\'' +
                ", sex='" + sex + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", perDescription='" + perDescription + '\'' +
                ", houseAmount='" + houseAmount + '\'' +
                ", houseType='" + houseType + '\'' +
                ", houseValue='" + houseValue + '\'' +
                ", houseArea='" + houseArea + '\'' +
                ", houseAge='" + houseAge + '\'' +
                ", houseLoan='" + houseLoan + '\'' +
                ", houseAlone='" + houseAlone + '\'' +
                ", houseLocation='" + houseLocation + '\'' +
                ", city='" + city + '\'' +
                ", retain=" + retain +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", receiveTime=" + receiveTime +
                ", isLock=" + isLock +
                ", phoneViewTime=" + phoneViewTime +
                ", phoneViewUid=" + phoneViewUid +
                ", phoneViewCount=" + phoneViewCount +
                ", autostatus=" + autostatus +
                ", utmSource='" + utmSource + '\'' +
                ", customerSource='" + customerSource + '\'' +
                ", customerClassify='" + customerClassify + '\'' +
                ", laiyuan=" + laiyuan +
                ", ext='" + ext + '\'' +
                ", repeatCallbackTime=" + repeatCallbackTime +
                '}';
    }
}
