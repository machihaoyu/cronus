package com.fjs.cronus.model;

import java.util.Date;

public class OcrHouseholdRegister extends BaseModel{
    private Integer id;

    private Integer customerId;

    private String customerName;

    private String customerTelephone;

    private String householdName;

    private String householdSex;

    private String householdNativePlace;

    private String householdBirthday;

    private String householdIdNumber;

    private String householdPeople;

    private String householdJob;

    private String householdMerriage;

    private String householdEducation;

    private String documentId;

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

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public String getHouseholdSex() {
        return householdSex;
    }

    public void setHouseholdSex(String householdSex) {
        this.householdSex = householdSex;
    }

    public String getHouseholdNativePlace() {
        return householdNativePlace;
    }

    public void setHouseholdNativePlace(String householdNativePlace) {
        this.householdNativePlace = householdNativePlace;
    }

    public String getHouseholdBirthday() {
        return householdBirthday;
    }

    public void setHouseholdBirthday(String householdBirthday) {
        this.householdBirthday = householdBirthday;
    }

    public String getHouseholdIdNumber() {
        return householdIdNumber;
    }

    public void setHouseholdIdNumber(String householdIdNumber) {
        this.householdIdNumber = householdIdNumber;
    }

    public String getHouseholdPeople() {
        return householdPeople;
    }

    public void setHouseholdPeople(String householdPeople) {
        this.householdPeople = householdPeople;
    }

    public String getHouseholdJob() {
        return householdJob;
    }

    public void setHouseholdJob(String householdJob) {
        this.householdJob = householdJob;
    }

    public String getHouseholdMerriage() {
        return householdMerriage;
    }

    public void setHouseholdMerriage(String householdMerriage) {
        this.householdMerriage = householdMerriage;
    }

    public String getHouseholdEducation() {
        return householdEducation;
    }

    public void setHouseholdEducation(String householdEducation) {
        this.householdEducation = householdEducation;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}