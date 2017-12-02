package com.fjs.cronus.dto.thea;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户面谈DTO
 * Created by zl on 2017/10/14.
 */
public class CustomerMeetDTO implements Serializable {
    private Integer id;
    @ApiModelProperty(value = "客户id", required = false)
    private Integer customerId;
    @ApiModelProperty(value = "业务员id", required = false)
    private Integer userId;
    @ApiModelProperty(value = "业务员姓名", required = false)
    private String userName;
    @ApiModelProperty(value = "面见时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date meetTime;
    @ApiModelProperty(value = "创建时间", required = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "沟通内容", required = false)
    private String content;

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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getMeetTime() {
        return meetTime;
    }

    public void setMeetTime(Date meetTime) {
        this.meetTime = meetTime;
    }

    @Override
    public String toString() {
        return "CustomerMeetDTO{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", meetTime=" + meetTime +
                ", createTime=" + createTime +
                ", content='" + content + '\'' +
                '}';
    }
}
