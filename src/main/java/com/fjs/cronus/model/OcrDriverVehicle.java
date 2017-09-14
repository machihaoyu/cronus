package com.fjs.cronus.model;

import java.util.Date;

public class OcrDriverVehicle extends  BaseModel {
    private Integer id;

    private Integer customerId;

    private String customerName;

    private String customerTelephone;

    private String driverOwner;

    private String driverPlateNum;

    private String driverVehicleType;

    private String driverVin;

    private String driverEngineNum;

    private String driverRegisterDate;

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

    public String getDriverOwner() {
        return driverOwner;
    }

    public void setDriverOwner(String driverOwner) {
        this.driverOwner = driverOwner;
    }

    public String getDriverPlateNum() {
        return driverPlateNum;
    }

    public void setDriverPlateNum(String driverPlateNum) {
        this.driverPlateNum = driverPlateNum;
    }

    public String getDriverVehicleType() {
        return driverVehicleType;
    }

    public void setDriverVehicleType(String driverVehicleType) {
        this.driverVehicleType = driverVehicleType;
    }

    public String getDriverVin() {
        return driverVin;
    }

    public void setDriverVin(String driverVin) {
        this.driverVin = driverVin;
    }

    public String getDriverEngineNum() {
        return driverEngineNum;
    }

    public void setDriverEngineNum(String driverEngineNum) {
        this.driverEngineNum = driverEngineNum;
    }

    public String getDriverRegisterDate() {
        return driverRegisterDate;
    }

    public void setDriverRegisterDate(String driverRegisterDate) {
        this.driverRegisterDate = driverRegisterDate;
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