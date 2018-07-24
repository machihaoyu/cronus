package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class MediaCustomerCountDTO {

    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "来源")
    private String customerSource;
    @ApiModelProperty(value = "媒体")
    private String utmSource;
    @ApiModelProperty("商机池客户存量")
    private Integer customerStock;
    @ApiModelProperty("已购买量")
    private Integer purchasedNumber;
    @ApiModelProperty(value = "核算方法(1-预购单价, 2-佣金比例, 3-放款比例, 4-预购单价&佣金比例, 5-预购单价&放款比例)")
    private Integer accountingMethod;
    @ApiModelProperty(value = "预购单价")
    private BigDecimal prepurchasePrice;
    @ApiModelProperty(value = "佣金比例")
    private BigDecimal commissionRate;
    @ApiModelProperty(value = "放款比例")
    private BigDecimal loanRate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public Integer getCustomerStock() {
        return customerStock;
    }

    public void setCustomerStock(Integer customerStock) {
        this.customerStock = customerStock;
    }

    public Integer getPurchasedNumber() {
        return purchasedNumber;
    }

    public void setPurchasedNumber(Integer purchasedNumber) {
        this.purchasedNumber = purchasedNumber;
    }

    public Integer getAccountingMethod() {
        return accountingMethod;
    }

    public void setAccountingMethod(Integer accountingMethod) {
        this.accountingMethod = accountingMethod;
    }

    public BigDecimal getPrepurchasePrice() {
        return prepurchasePrice;
    }

    public void setPrepurchasePrice(BigDecimal prepurchasePrice) {
        this.prepurchasePrice = prepurchasePrice;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public BigDecimal getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
    }
}
