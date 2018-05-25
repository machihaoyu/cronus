package com.fjs.cronus.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by feng on 2017/9/22.
 */
@Table(name = "user_month_info")
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
    @Column(name = "user_id")
    private Integer userId;

    @ApiModelProperty(value = "月申请数", required = false)
    @Column(name = "base_customer_num")
    private Integer baseCustomerNum;

    @ApiModelProperty(value = "月奖励数", required = false)
    @Column(name = "reward_customer_num")
    private Integer rewardCustomerNum;

    @ApiModelProperty(value = "已分配数", required = false)
    @Column(name = "assigned_customer_num")
    private Integer assignedCustomerNum;

    @ApiModelProperty(value = "有效数", required = false)
    @Column(name = "effective_customer_num")
    private Integer effectiveCustomerNum;

    @ApiModelProperty(value = "创建时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "最新保存时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    @ApiModelProperty(value = "生效时间", required = false)
    @Column(name = "effective_date")
    private String effectiveDate;

    @ApiModelProperty(value = "最新保存用户ID", required = false)
    @Column(name = "last_update_user")
    private Integer lastUpdateUser;

    @ApiModelProperty(value = "创建用户ID", required = false)
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 一级吧.
     */
    private Integer companyid;

    /**
     * 媒体.
     */
    private Integer mediaid;


    /**
     * 数据状态；0-删除，1-正常
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public Integer getMediaid() {
        return mediaid;
    }

    public void setMediaid(Integer mediaid) {
        this.mediaid = mediaid;
    }

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
