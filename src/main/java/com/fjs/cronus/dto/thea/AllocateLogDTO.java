package com.fjs.cronus.dto.thea;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 分配日志
 * Created by yinzf on 2017/10/23.
 */
public class AllocateLogDTO {
    @ApiModelProperty(value = "操作")
    private String operation;
    @ApiModelProperty(value="老负责人")
    private String oldOwnerName;
    @ApiModelProperty(value="新负责人")
    private String newOwnerName;
    @ApiModelProperty(value="操作人")
    private String createUserName;
    @ApiModelProperty(value="时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    private Integer oldOwnerId;
    private Integer newOwnerId;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOldOwnerName() {
        return oldOwnerName;
    }

    public void setOldOwnerName(String oldOwnerName) {
        this.oldOwnerName = oldOwnerName;
    }

    public String getNewOwnerName() {
        return newOwnerName;
    }

    public void setNewOwnerName(String newOwnerName) {
        this.newOwnerName = newOwnerName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOldOwnerId() {
        return oldOwnerId;
    }

    public void setOldOwnerId(Integer oldOwnerId) {
        this.oldOwnerId = oldOwnerId;
    }

    public Integer getNewOwnerId() {
        return newOwnerId;
    }

    public void setNewOwnerId(Integer newOwnerId) {
        this.newOwnerId = newOwnerId;
    }
}
