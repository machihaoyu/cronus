package com.fjs.cronus.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class OcrDriverLicense {
    private Integer id;

    private Integer customerId;

    private String customerName;

    private String customerTelephone;

    private String driverName;

    private String driverNum;

    private String driverVehicleType;

    private String driverStartDate;

    private String driverEndDate;

    private Integer documentId;

    private Integer createUserId;

    private String createUserName;

    private Date createTime;

    private String status;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTelephone() {
        return customerTelephone;
    }

    public void setCustomerTelephone(String customerTelephone) {
        this.customerTelephone = customerTelephone;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverNum() {
        return driverNum;
    }

    public void setDriverNum(String driverNum) {
        this.driverNum = driverNum;
    }

    public String getDriverVehicleType() {
        return driverVehicleType;
    }

    public void setDriverVehicleType(String driverVehicleType) {
        this.driverVehicleType = driverVehicleType;
    }

    public String getDriverStartDate() {
        return driverStartDate;
    }

    public void setDriverStartDate(String driverStartDate) {
        this.driverStartDate = driverStartDate;
    }

    public String getDriverEndDate() {
        return driverEndDate;
    }

    public void setDriverEndDate(String driverEndDate) {
        this.driverEndDate = driverEndDate;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}