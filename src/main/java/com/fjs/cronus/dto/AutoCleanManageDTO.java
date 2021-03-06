package com.fjs.cronus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 添加渠道，来源dto
 * Created by yinzf on 2017/12/14.
 */
public class AutoCleanManageDTO {
    private Integer id;

    @ApiModelProperty(value = "业务员id")
    private Integer userId;
    @ApiModelProperty(value = "电话")
    private String telephone;
    @ApiModelProperty(value = "来源")
    private String customerSource;
    @ApiModelProperty(value = "渠道")
    private String utmSource;
    @ApiModelProperty(value = "类型 1列表添加 2添加屏蔽规则")
    private Integer type;
    @ApiModelProperty(value = "创建时间（展开列表使用")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AutoCleanManageDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", telephone='" + telephone + '\'' +
                ", customerSource='" + customerSource + '\'' +
                ", utmSource='" + utmSource + '\'' +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
