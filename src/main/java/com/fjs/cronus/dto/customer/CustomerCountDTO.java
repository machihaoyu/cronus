package com.fjs.cronus.dto.customer;


import io.swagger.annotations.ApiModelProperty;

public class CustomerCountDTO {

    @ApiModelProperty(value = "今日分配客户数")
    private Integer todayAllocation;
    @ApiModelProperty(value = "分配客户历史总数")
    private Integer historyAllocation;
    @ApiModelProperty(value = "今日领取客户数")
    private Integer todayReceive;
    @ApiModelProperty(value = "领取客户历史总数")
    private Integer historyReceive;
    @ApiModelProperty(value = "今日沟通客户数")
    private Integer todayCommunicateCustomer;
    @ApiModelProperty(value = "沟通客户历史总数")
    private Integer historyCommunicateCustomer;
    @ApiModelProperty(value = "今日沟通次数")
    private Integer todayCommunicate;
    @ApiModelProperty(value = "沟通历史总数")
    private Integer historyCommunicate;


    public Integer getTodayAllocation() {
        return todayAllocation;
    }

    public void setTodayAllocation(Integer todayAllocation) {
        this.todayAllocation = todayAllocation;
    }

    public Integer getHistoryAllocation() {
        return historyAllocation;
    }

    public void setHistoryAllocation(Integer historyAllocation) {
        this.historyAllocation = historyAllocation;
    }

    public Integer getTodayReceive() {
        return todayReceive;
    }

    public void setTodayReceive(Integer todayReceive) {
        this.todayReceive = todayReceive;
    }

    public Integer getHistoryReceive() {
        return historyReceive;
    }

    public void setHistoryReceive(Integer historyReceive) {
        this.historyReceive = historyReceive;
    }

    public Integer getTodayCommunicateCustomer() {
        return todayCommunicateCustomer;
    }

    public void setTodayCommunicateCustomer(Integer todayCommunicateCustomer) {
        this.todayCommunicateCustomer = todayCommunicateCustomer;
    }

    public Integer getHistoryCommunicateCustomer() {
        return historyCommunicateCustomer;
    }

    public void setHistoryCommunicateCustomer(Integer historyCommunicateCustomer) {
        this.historyCommunicateCustomer = historyCommunicateCustomer;
    }

    public Integer getTodayCommunicate() {
        return todayCommunicate;
    }

    public void setTodayCommunicate(Integer todayCommunicate) {
        this.todayCommunicate = todayCommunicate;
    }

    public Integer getHistoryCommunicate() {
        return historyCommunicate;
    }

    public void setHistoryCommunicate(Integer historyCommunicate) {
        this.historyCommunicate = historyCommunicate;
    }
}
