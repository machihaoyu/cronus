package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by feng on 2017/11/2.
 */
public class EditUserMonthInfoDTO {

    @ApiModelProperty(value="业务员ID")
    private Integer userId;

    @ApiModelProperty(value="生效时间")
    private String effectiveDate;

    @ApiModelProperty(value="基础客户数")
    private Integer baseCustomerNum;

    @ApiModelProperty(value="奖励客户数")
    private Integer rewardCustomerNum;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getBaseCustomerNum() {
        return baseCustomerNum;
    }

    public void setBaseCustomerNum(Integer baseCustomerNum) {
        this.baseCustomerNum = baseCustomerNum;
    }

    public Integer getRewardCustomerNum() {
        return rewardCustomerNum;
    }

    public void setRewardCustomerNum(Integer rewardCustomerNum) {
        this.rewardCustomerNum = rewardCustomerNum;
    }
}
