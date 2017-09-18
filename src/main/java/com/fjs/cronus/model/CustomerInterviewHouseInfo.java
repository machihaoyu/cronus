package com.fjs.cronus.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerInterviewHouseInfo extends BaseModel {
    private Integer id;

    private Integer customerInterviewBaseInfoId;

    private String accepthousearea;

    private String houseStatus;

    private String housePropertyType;

    private BigDecimal area;

    private Date buildDate;

    private Integer housePropertyRightsNum;

    private String isChildInPropertyRigths;

    private String isOldInPropertyRigths;

    private String isPropertyRightsClear;

    private String isOtherHouse;

    private String isBankFlow;

    private Integer bankFlowMonthAmount;

    private Integer houseMortgageMonthAmount;

    private Integer houseMortgagePaidNum;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerInterviewBaseInfoId() {
        return customerInterviewBaseInfoId;
    }

    public void setCustomerInterviewBaseInfoId(Integer customerInterviewBaseInfoId) {
        this.customerInterviewBaseInfoId = customerInterviewBaseInfoId;
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

    public Date getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Date buildDate) {
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
}