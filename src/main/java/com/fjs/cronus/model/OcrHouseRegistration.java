package com.fjs.cronus.model;

import java.util.Date;

public class OcrHouseRegistration extends BaseModel {
    private Integer id;

    private Integer customerId;

    private String customerName;

    private String customerTelephone;

    private String houseOwnner;

    private String houseAddress;

    private String housePurpose;

    private String houseUsageTerm;

    private String houseArea;

    private String houseType;

    private String houseCompletionDate;

    private Integer documentId;

    private String status;

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

    public String getHouseOwnner() {
        return houseOwnner;
    }

    public void setHouseOwnner(String houseOwnner) {
        this.houseOwnner = houseOwnner;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getHousePurpose() {
        return housePurpose;
    }

    public void setHousePurpose(String housePurpose) {
        this.housePurpose = housePurpose;
    }

    public String getHouseUsageTerm() {
        return houseUsageTerm;
    }

    public void setHouseUsageTerm(String houseUsageTerm) {
        this.houseUsageTerm = houseUsageTerm;
    }

    public String getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getHouseCompletionDate() {
        return houseCompletionDate;
    }

    public void setHouseCompletionDate(String houseCompletionDate) {
        this.houseCompletionDate = houseCompletionDate;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}