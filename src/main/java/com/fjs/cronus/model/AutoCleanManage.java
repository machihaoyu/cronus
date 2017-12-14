package com.fjs.cronus.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by feng on 2017/10/12.
 */
public class AutoCleanManage {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", required = false)
    private Integer id;

    @ApiModelProperty(value = "业务员ID", required = false)
    private Integer userId;

    @ApiModelProperty(value = "来源", required = false)
    private String customerSource;

    @ApiModelProperty(value = "渠道", required = false)
    private String utmSource;

    @ApiModelProperty(value = "创建时间", required = false)
    private Date createTime;

    @ApiModelProperty(value = "上回更新时间", required = false)
    private Date lastUpdateTime;

    @ApiModelProperty(value = "创建用户", required = false)
    private Integer createUser;

    @ApiModelProperty(value = "上回更新用户", required = false)
    private Integer lastUpdateUser;

    @ApiModelProperty(value = "是否删除", required = false)
    private Integer isDeleted;

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

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(Integer lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
