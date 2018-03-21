package com.fjs.cronus.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by feng on 2017/9/22.
 */
public class UserMonthInfo implements Serializable{
            /*`id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
            `user_id` int(10) DEFAULT NULL COMMENT '业务员ID',
            `base_customer_num` int(10) DEFAULT '0' COMMENT '基础客户数',
            `reward_customer_num` int(10) DEFAULT '0' COMMENT '奖励客户数',
            `assigned_customer_num` int(10) DEFAULT '0' COMMENT '已分配客户数',
            `effective_customer_num` int(10) DEFAULT '0' COMMENT '有效客户数',
            `creat_time` int(11) DEFAULT NULL COMMENT '创建时间',
            `creat_user_id` int(10) DEFAULT NULL COMMENT '创建用户ID',
            `updata_time` int(11) DEFAULT NULL COMMENT '更新时间',
            `effective_date` varchar(20) DEFAULT NULL COMMENT '有效月份时段(如：201707)',*/

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", required = false)
    private Integer id;

    @ApiModelProperty(value = "用户ID", required = false)
    private Integer userId;

    @ApiModelProperty(value = "月申请数", required = false)
    private Integer baseCustomerNum;

    @ApiModelProperty(value = "月奖励数", required = false)
    private Integer rewardCustomerNum;

    @ApiModelProperty(value = "已分配数", required = false)
    private Integer assignedCustomerNum;

    @ApiModelProperty(value = "有效数", required = false)
    private Integer effectiveCustomerNum;

    @ApiModelProperty(value = "创建时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "最新保存时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastUpdateTime;

    @ApiModelProperty(value = "生效时间", required = false)

    private String effectiveDate;

    @ApiModelProperty(value = "最新保存用户ID", required = false)
    private Integer lastUpdateUser;

    @ApiModelProperty(value = "创建用户ID", required = false)
    private Integer createUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getAssignedCustomerNum() {
        return assignedCustomerNum;
    }

    public void setAssignedCustomerNum(Integer assignedCustomerNum) {
        this.assignedCustomerNum = assignedCustomerNum;
    }

    public Integer getEffectiveCustomerNum() {
        return effectiveCustomerNum;
    }

    public void setEffectiveCustomerNum(Integer effectiveCustomerNum) {
        this.effectiveCustomerNum = effectiveCustomerNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(Integer lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }
}
