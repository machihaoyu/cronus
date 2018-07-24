package com.fjs.cronus.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerPriceEntity {


    private Integer id; // '主键id',
    private Integer customerInfoId; // '客户信息表id',
    private Integer accountingMethod; // int(11) DEFAULT NULL COMMENT '核算方法(1-预购单价, 2-佣金比例, 3-放款比例, 4-预购单价&佣金比例, 5-预购单价&放款比例)',
    private BigDecimal prepurchasePrice; // decimal(12,2) DEFAULT NULL COMMENT '预购单价',
    private BigDecimal commissionRate; //decimal(12,2) DEFAULT NULL COMMENT '佣金比例',
    private BigDecimal loanRate; //decimal(12,2) DEFAULT NULL COMMENT '放款比例',
    private Integer createUser; //int(11) NOT NULL COMMENT '创建人id',
    private Integer lastUpdateUser; //int(11) NOT NULL COMMENT '更新人',
    private Date gmtCreate; //datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    private Date gmtModified; //datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    private Integer isDeleted; //tinyint(11) DEFAULT '0' COMMENT '是否删除(0:未删除,1:已删除)',
    private Integer isReceive; //tinyint(11) DEFAULT '0' COMMENT '是否删除(0:未领取,1:已领取)',
    private Date gmtReceive; //datetime DEFAULT NULL COMMENT '领取时间',
    private Integer isClose; //tinyint(11) DEFAULT '0' COMMENT '是否关闭(0:未关闭,1:已关闭)',

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerInfoId() {
        return customerInfoId;
    }

    public void setCustomerInfoId(Integer customerInfoId) {
        this.customerInfoId = customerInfoId;
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

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(Integer lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(Integer isReceive) {
        this.isReceive = isReceive;
    }

    public Date getGmtReceive() {
        return gmtReceive;
    }

    public void setGmtReceive(Date gmtReceive) {
        this.gmtReceive = gmtReceive;
    }

    public Integer getIsClose() {
        return isClose;
    }

    public void setIsClose(Integer isClose) {
        this.isClose = isClose;
    }

}
