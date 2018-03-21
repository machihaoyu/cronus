package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by msi on 2017/12/16.
 */
public class AddCustomerDTO implements Serializable {

    @ApiModelProperty(value = "客户姓名")
    private String customerName;
    @ApiModelProperty(value = "手机号码")
    private String telephonenumber;
    @ApiModelProperty(value = "金额")
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "客户来源")
    private String customerSource;
    @ApiModelProperty(value = "渠道")
    private String utmSource;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
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
}
