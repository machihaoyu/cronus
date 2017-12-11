package com.fjs.cronus.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yinzf on 2017/11/24.
 */
public class Comment implements Serializable{
    private Integer id;
    private Integer communicationLogId;
    private String content;
    private Date createTime;
    private Integer createUser;
    private String createUserName;
    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getCommunicationLogId() {
        return communicationLogId;
    }

    public void setCommunicationLogId(Integer communicationLogId) {
        this.communicationLogId = communicationLogId;
    }
}
