package com.fjs.cronus.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerInterviewCarInfo extends  BaseModel {
    private Integer id;

    private Integer customerInterviewBaseInfoId;

    private String carType;

    private String licencePlateLocation;

    private Integer buyDate;

    private Integer carMortgagePaidNum;

    private Integer carMortgageMonthAmount;

    private BigDecimal priceNow;

    private String isFullInsurance;

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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getLicencePlateLocation() {
        return licencePlateLocation;
    }

    public void setLicencePlateLocation(String licencePlateLocation) {
        this.licencePlateLocation = licencePlateLocation;
    }

    public Integer getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Integer buyDate) {
        this.buyDate = buyDate;
    }

    public Integer getCarMortgagePaidNum() {
        return carMortgagePaidNum;
    }

    public void setCarMortgagePaidNum(Integer carMortgagePaidNum) {
        this.carMortgagePaidNum = carMortgagePaidNum;
    }

    public Integer getCarMortgageMonthAmount() {
        return carMortgageMonthAmount;
    }

    public void setCarMortgageMonthAmount(Integer carMortgageMonthAmount) {
        this.carMortgageMonthAmount = carMortgageMonthAmount;
    }

    public BigDecimal getPriceNow() {
        return priceNow;
    }

    public void setPriceNow(BigDecimal priceNow) {
        this.priceNow = priceNow;
    }

    public String getIsFullInsurance() {
        return isFullInsurance;
    }

    public void setIsFullInsurance(String isFullInsurance) {
        this.isFullInsurance = isFullInsurance;
    }
}