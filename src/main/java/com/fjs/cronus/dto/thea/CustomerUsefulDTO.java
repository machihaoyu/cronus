package com.fjs.cronus.dto.thea;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yinzf on 2017/10/13.
 */
public class CustomerUsefulDTO {
    private Integer id;
    @ApiModelProperty(value = "客户id（必填）", required = false)
    private Integer customerId;
    @ApiModelProperty(value = "有无房产", required = false)
    private String houseStatus;
    @ApiModelProperty(value = "拟贷款金额", required = false)
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "资金用途", required = false)
    private String purpose;
    @ApiModelProperty(value = "沟通内容(必填)", required = false)
    private String content;
    @ApiModelProperty(value = "合作状态（必填）", required = false)
    private String cooperationStatus;
    @ApiModelProperty(value = "确认时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "手机号码")
    private String telephonenumber;
    @ApiModelProperty(value = "是否面见")
    private Integer isMeet;
    @ApiModelProperty(value = "面见时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date meetTime;

    @ApiModelProperty(value = "下次沟通时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date nextContactTime;
    @ApiModelProperty(value = "用户的资金用途", required = false)
    private String purposeDescribe;

    public String getPurposeDescribe() {
        return purposeDescribe;
    }

    public void setPurposeDescribe(String purposeDescribe) {
        this.purposeDescribe = purposeDescribe;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCooperationStatus() {
        return cooperationStatus;
    }

    public void setCooperationStatus(String cooperationStatus) {
        this.cooperationStatus = cooperationStatus;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public Integer getIsMeet() {
        return isMeet;
    }

    public void setIsMeet(Integer isMeet) {
        this.isMeet = isMeet;
    }

    public Date getMeetTime() {
        return meetTime;
    }

    public void setMeetTime(Date meetTime) {
        this.meetTime = meetTime;
    }

    public Date getNextContactTime() {
        return nextContactTime;
    }

    public void setNextContactTime(Date nextContactTime) {
        this.nextContactTime = nextContactTime;
    }

    @Override
    public String toString() {
        return "CustomerUsefulDTO{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", houseStatus='" + houseStatus + '\'' +
                ", loanAmount=" + loanAmount +
                ", purpose='" + purpose + '\'' +
                ", content='" + content + '\'' +
                ", cooperationStatus='" + cooperationStatus + '\'' +
                ", createTime=" + createTime +
                ", telephonenumber='" + telephonenumber + '\'' +
                ", isMeet=" + isMeet +
                ", meetTime=" + meetTime +
                '}';
    }
}
