package com.fjs.cronus.api.thea;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易的DTO
 * Created by yinzf on 2017/10/12.
 */
public class LoanDTO {
    @ApiModelProperty(value = "交易id", required = false)
    private Integer loanId;
    @ApiModelProperty(value = "交易流水号", required = false)
    private String code;
    @ApiModelProperty(value = "客户姓名", required = false)
    private String customerName;
    @ApiModelProperty(value = "拟借款金额", required = true)
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "申请交易时间", required = false)
//    @JsonDeserialize(using = DateJsonDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "交易状态", required = false)
    private Integer status;
    @ApiModelProperty(value = "客户id", required = false)
    private Integer customerId;
    @ApiModelProperty(value = "电话", required = false)
    private String telephonenumber;
    @ApiModelProperty(value = "负责人", required = false)
    private String ownUserName;
    @ApiModelProperty(value = "负责人id", required = false)
    private Integer ownUserId;
    @ApiModelProperty(value = "渠道来源（必填）")
    private String utmSource;

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public Integer getOwnUserId() {
        return ownUserId;
    }

    public void setOwnUserId(Integer ownUserId) {
        this.ownUserId = ownUserId;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOwnUserName() {
        return ownUserName;
    }

    public void setOwnUserName(String ownUserName) {
        this.ownUserName = ownUserName;
    }

    @Override
    public String toString() {
        return "LoanDTO{" +
                "loanId=" + loanId +
                ", code='" + code + '\'' +
                ", customerName='" + customerName + '\'' +
                ", loanAmount=" + loanAmount +
                ", createTime=" + createTime +
                ", status=" + status +
                ", customerId=" + customerId +
                ", telephonenumber='" + telephonenumber + '\'' +
                ", ownUserName='" + ownUserName + '\'' +
                '}';
    }
}
