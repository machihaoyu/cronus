package com.fjs.cronus.model;

import java.util.Date;

public class CustomerInterviewInsuranceInfo extends  BaseModel {
    private Integer id;

    private Integer customerInterviewBaseInfoId;

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

    public Integer getCustomerInterviewBaseInfoId() {
        return customerInterviewBaseInfoId;
    }

    public void setCustomerInterviewBaseInfoId(Integer customerInterviewBaseInfoId) {
        this.customerInterviewBaseInfoId = customerInterviewBaseInfoId;
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