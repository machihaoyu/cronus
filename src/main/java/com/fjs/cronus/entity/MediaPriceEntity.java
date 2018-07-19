package com.fjs.cronus.entity;


import java.math.BigDecimal;
import java.util.Date;

public class MediaPriceEntity {

    private Integer id; //id
    private Integer mediaCustomerCountId; //媒体客户数量统计表id
    private Integer accountingMethod; //核算方法(1-预购单价, 2-佣金比例, 3-放款比例, 4-预购单价&佣金比例, 5-预购单价&放款比例)
    private BigDecimal prepurchasePrice; //预购单价
    private BigDecimal commissionRate; //佣金比例
    private BigDecimal loanRate; //放款比例
    private Integer createUser; // 创建人id
    private Integer lastUpdateUser; // 更新人
    private Date gmtCreate; //创建时间
    private Date gmtModified;//更新时间
    private Integer isDeleted; //是否删除(0:未删除,1:已删除)
    private Integer isClose; // 是否关闭(0:未关闭,1:已关闭)


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMediaCustomerCountId() {
        return mediaCustomerCountId;
    }

    public void setMediaCustomerCountId(Integer mediaCustomerCountId) {
        this.mediaCustomerCountId = mediaCustomerCountId;
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

    public Integer getIsClose() {
        return isClose;
    }

    public void setIsClose(Integer isClose) {
        this.isClose = isClose;
    }
}
