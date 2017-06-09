package com.fjs.cronus.dto.customer;


import java.io.Serializable;
import java.util.List;

/**
 * Created by crm on 2017/4/13.
 */
public class CustomerCommunicationDTO implements Serializable {

    private static final long serialVersionUID = -1638378515152729130L;

    /**
     * 客户ID
     */
    private Integer customerId;

    //是否有房
    private String houseStatus;
    //确认贷款金额
    private String loanAmount;
    //手机
    private String telPhone;
    //沟通记录
    private List<String> communts;
    //面见记录
    private List<String> meets;
    //回访记录
    private List<String> visits;
    //沟通内容
    private String content;

    private String nextTime;

    //面见-是否面见(如果面见则存储到面见表)
    private String meetStatus;
    //面见-面见时间
    private String meetTime;

    //资金用途
    private String purpose;

    public String getNextTime() {
        return nextTime;
    }

    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public List<String> getCommunts() {
        return communts;
    }

    public void setCommunts(List<String> communts) {
        this.communts = communts;
    }

    public List<String> getMeets() {
        return meets;
    }

    public void setMeets(List<String> meets) {
        this.meets = meets;
    }

    public List<String> getVisits() {
        return visits;
    }

    public void setVisits(List<String> visits) {
        this.visits = visits;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMeetStatus() {
        return meetStatus;
    }

    public void setMeetStatus(String meetStatus) {
        this.meetStatus = meetStatus;
    }

    public String getMeetTime() {
        return meetTime;
    }

    public void setMeetTime(String meetTime) {
        this.meetTime = meetTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
