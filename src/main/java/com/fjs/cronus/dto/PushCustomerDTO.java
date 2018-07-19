package com.fjs.cronus.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PushCustomerDTO {

    private String customerName; //客户姓名
    private String mobile; //联系电话
    private String cityName; //所在城市
    private BigDecimal loanQuota; //借款额度
    private Date applyTime; //申请时间
    private String source; //渠道来源


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public BigDecimal getLoanQuota() {
        return loanQuota;
    }

    public void setLoanQuota(BigDecimal loanQuota) {
        this.loanQuota = loanQuota;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
