package com.fjs.cronus.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class BusinessPool {

    @ApiModelProperty(value = "客户id")
    private Integer id;
    @ApiModelProperty(value = "客户姓名")
    private String customerName;
    @ApiModelProperty(value = "金额(万)")
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "房产")
    private String houseStatus;
    @ApiModelProperty(value = "来源")
    private String customerSource;
    @ApiModelProperty(value = "媒体")
    private String utmSource;
    @ApiModelProperty(value = "城市")
    private String city;
    @ApiModelProperty(value = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "媒体客户数量统计表id")
    private Integer mediaCustomerCountId;
    @ApiModelProperty(value = "核算方法(1-预购单价, 2-佣金比例, 3-放款比例, 4-预购单价&佣金比例, 5-预购单价&放款比例)")
    private BigDecimal accountingMethod;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getMediaCustomerCountId() {
        return mediaCustomerCountId;
    }

    public void setMediaCustomerCountId(Integer mediaCustomerCountId) {
        this.mediaCustomerCountId = mediaCustomerCountId;
    }

    public BigDecimal getAccountingMethod() {
        return accountingMethod;
    }

    public void setAccountingMethod(BigDecimal accountingMethod) {
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
