package com.fjs.cronus.dto.ourea;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yinzf on 2018/6/28.
 */
public class CrmPushCustomerDTO {
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "客户Id")
    private Integer crmCusId;
    @ApiModelProperty(value = "客户拥有者业务员Id")
    private Integer crmUserId;
    @ApiModelProperty(value = "期望用款时间")
    private String expectTime;
    @ApiModelProperty(value = "资金用途(文本)")
    private String fundUse;
    @ApiModelProperty(value = "城市")
    private String houseCity;
    @ApiModelProperty(value = "户籍")
    private String householdReg;
    @ApiModelProperty(value = "贷款金额(万)")
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "期望贷款期限单位月")
    private Integer loanTerm;
    @ApiModelProperty(value = "客户姓名")
    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCrmCusId() {
        return crmCusId;
    }

    public void setCrmCusId(Integer crmCusId) {
        this.crmCusId = crmCusId;
    }

    public Integer getCrmUserId() {
        return crmUserId;
    }

    public void setCrmUserId(Integer crmUserId) {
        this.crmUserId = crmUserId;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getFundUse() {
        return fundUse;
    }

    public void setFundUse(String fundUse) {
        this.fundUse = fundUse;
    }

    public String getHouseCity() {
        return houseCity;
    }

    public void setHouseCity(String houseCity) {
        this.houseCity = houseCity;
    }

    public String getHouseholdReg() {
        return householdReg;
    }

    public void setHouseholdReg(String householdReg) {
        this.householdReg = householdReg;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OureaDTO{" +
                "age=" + age +
                ", crmCusId=" + crmCusId +
                ", crmUserId='" + crmUserId + '\'' +
                ", expectTime=" + expectTime +
                ", fundUse='" + fundUse + '\'' +
                ", houseCity='" + houseCity + '\'' +
                ", householdReg='" + householdReg + '\'' +
                ", loanAmount=" + loanAmount +
                ", loanTerm=" + loanTerm +
                ", name='" + name + '\'' +
                '}';
    }
}
