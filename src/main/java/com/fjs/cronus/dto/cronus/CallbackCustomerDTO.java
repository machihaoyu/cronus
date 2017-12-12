package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by msi on 2017/10/11.
 */
public class CallbackCustomerDTO {

    private Integer id;

    private String telephonenumber;

    private String customerName;

    private String city;

    private BigDecimal LoanAmount;

    private Integer ownUserId;

    private String ownUserName;

    private String customerType;

    private String callbackStatus;

    private Integer communicationOrder;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date callbackTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private String sub_company;


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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BigDecimal getLoanAmount() {
        return LoanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        LoanAmount = loanAmount;
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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
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

    public String getSub_company() {
        return sub_company;
    }

    public void setSub_company(String sub_company) {
        this.sub_company = sub_company;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCommunicationOrder() {
        return communicationOrder;
    }

    public void setCommunicationOrder(Integer communicationOrder) {
        this.communicationOrder = communicationOrder;
    }
}
