package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by msi on 2017/12/16.
 */
public class CommunicationDTO implements Serializable {

    @ApiModelProperty(value = "主键id", required = false)
    private  Integer id;

    @ApiModelProperty(value = "负责人id", required = false)
    private Integer ownUserId;

    @ApiModelProperty(value = "负责人", required = false)
    private String ownUserName;

    @ApiModelProperty(value = "沟通内容(必填)", required = false)
    private String content;

    @ApiModelProperty(value = "沟通时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnUserId() {
        return ownUserId;
    }

    public void setOwnUserId(Integer ownUserId) {
        this.ownUserId = ownUserId;
    }

    public String getOwnUserName() {
        return ownUserName;
    }

    public void setOwnUserName(String ownUserName) {
        this.ownUserName = ownUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
