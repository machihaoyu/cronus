package com.fjs.cronus.model;

import java.util.Date;

public class OcrIdentity extends  BaseModel{
    private Integer id;

    private Integer customerId;

    private String customerName;

    private String customerTelephone;

    private String cardName;

    private String cardSex;

    private String cardNation;

    private String cardBirth;

    private String cardAddress;

    private String cardNum;

    private String cardSignOrg;

    private String cardValidStart;

    private String cardValidEnd;

    private String crmAttachBackId;

    private String crmAttachFaceId;

    private String documentCategoryIds;

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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardSex() {
        return cardSex;
    }

    public void setCardSex(String cardSex) {
        this.cardSex = cardSex;
    }

    public String getCardNation() {
        return cardNation;
    }

    public void setCardNation(String cardNation) {
        this.cardNation = cardNation;
    }

    public String getCardBirth() {
        return cardBirth;
    }

    public void setCardBirth(String cardBirth) {
        this.cardBirth = cardBirth;
    }

    public String getCardAddress() {
        return cardAddress;
    }

    public void setCardAddress(String cardAddress) {
        this.cardAddress = cardAddress;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardSignOrg() {
        return cardSignOrg;
    }

    public void setCardSignOrg(String cardSignOrg) {
        this.cardSignOrg = cardSignOrg;
    }

    public String getCardValidStart() {
        return cardValidStart;
    }

    public void setCardValidStart(String cardValidStart) {
        this.cardValidStart = cardValidStart;
    }

    public String getCardValidEnd() {
        return cardValidEnd;
    }

    public void setCardValidEnd(String cardValidEnd) {
        this.cardValidEnd = cardValidEnd;
    }

    public String getCrmAttachBackId() {
        return crmAttachBackId;
    }

    public void setCrmAttachBackId(String crmAttachBackId) {
        this.crmAttachBackId = crmAttachBackId;
    }

    public String getCrmAttachFaceId() {
        return crmAttachFaceId;
    }

    public void setCrmAttachFaceId(String crmAttachFaceId) {
        this.crmAttachFaceId = crmAttachFaceId;
    }

    public String getDocumentCategoryIds() {
        return documentCategoryIds;
    }

    public void setDocumentCategoryIds(String documentCategoryIds) {
        this.documentCategoryIds = documentCategoryIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}