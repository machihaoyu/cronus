package com.fjs.cronus.dto.api.crius;

import com.fjs.cronus.model.BaseModel;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by pc on 2017/9/13.
 */
public class ServiceContract extends BaseModel implements Serializable {

    @ApiModelProperty(value = "交易id")
    private Integer loanId;
    @ApiModelProperty(value = "客户id")
    private Integer customerId;
    @ApiModelProperty(value = "服务合同编号")
    private String number;
    @ApiModelProperty(value = "拟借款金额")
    private BigDecimal planMoney;
    @ApiModelProperty(value = "佣金")
    private BigDecimal commission;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "原始创建人")
    private Integer oldCreateUserId;
    @ApiModelProperty(value = "业务员对协议的额外备注信息")
    private String mark;
    @ApiModelProperty(value = "借款人")
    private String borrower;
    @ApiModelProperty(value = "产品类型")
    private String productType;
    @ApiModelProperty(value = "借款人身份证")
    private String identity;
    @ApiModelProperty(value = "费率")
    private BigDecimal rate;
    @ApiModelProperty(value = "电话")
    private String telephone;
    @ApiModelProperty(value = "预付款")
    private BigDecimal deposit;
    @ApiModelProperty(value = "支付方式(1-银行转账,2-现金,3-pos机,4-微信,5-支付宝,6其他)")
    private Integer payType;
    @ApiModelProperty(value = "支付日期")
    private Date payTime;
    @ApiModelProperty(value = "收款人")
    private String payee;
    @ApiModelProperty(value = "收款账户")
    private String payeeAccount;
    @ApiModelProperty(value = "是不是利差合同(0-不是,1是)")
    private Integer isSpecial;


    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getPlanMoney() {
        return planMoney;
    }

    public void setPlanMoney(BigDecimal planMoney) {
        this.planMoney = planMoney;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Integer getOldCreateUserId() {
        return oldCreateUserId;
    }

    public void setOldCreateUserId(Integer oldCreateUserId) {
        this.oldCreateUserId = oldCreateUserId;
    }


    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public Integer getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(Integer isSpecial) {
        this.isSpecial = isSpecial;
    }

    @Override
    public String toString() {
        return "ServiceContract{" +
                "loanId=" + loanId +
                ", customerId=" + customerId +
                ", number='" + number + '\'' +
                ", planMoney=" + planMoney +
                ", commission=" + commission +
                ", status=" + status +
                ", oldCreateUserId=" + oldCreateUserId +
                ", mark='" + mark + '\'' +
                ", borrower='" + borrower + '\'' +
                ", productType='" + productType + '\'' +
                ", identity='" + identity + '\'' +
                ", rate=" + rate +
                ", telephone='" + telephone + '\'' +
                ", deposit=" + deposit +
                ", payType=" + payType +
                ", payTime=" + payTime +
                ", payee='" + payee + '\'' +
                ", payeeAccount='" + payeeAccount + '\'' +
                ", isSpecial=" + isSpecial +
                '}';
    }
}
