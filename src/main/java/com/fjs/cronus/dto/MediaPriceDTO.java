package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class MediaPriceDTO {

    @ApiModelProperty(value = "id")
    private Integer id;
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
