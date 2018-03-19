package com.fjs.cronus.dto.thea;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 添加交易的DTO
 * Created by yinzf on 2018/3/13.
 */
public class LoanDTO6 {
    @ApiModelProperty(value = "拟借款金额", required = true)
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "客户id", required = false)
    private Integer customerId;
    @ApiModelProperty(value = "客户姓名", required = false)
    private String customerName;
    @ApiModelProperty(value = "电话", required = false)
    private String telephonenumber;
    @ApiModelProperty(value = "负责人id", required = false)
    private Integer ownUserId;

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getOwnUserId() {
        return ownUserId;
    }

    public void setOwnUserId(Integer ownUserId) {
        this.ownUserId = ownUserId;
    }

    @Override
    public String toString() {
        return "LoanDTO6{" +
                "loanAmount=" + loanAmount +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", telephonenumber='" + telephonenumber + '\'' +
                ", ownUserId=" + ownUserId +
                '}';
    }
}
