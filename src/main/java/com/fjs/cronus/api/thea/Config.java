package com.fjs.cronus.api.thea;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class Config implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastUpdateTime;

    private Integer createUser;

    private Integer lastUpdateUser;

    private Integer isDeleted;

    private String conName;

    private String conTitle;

    private String conDescription;

    private String conValue;
    private Integer conType;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getConName() {
        return conName;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }

    public String getConTitle() {
        return conTitle;
    }

    public void setConTitle(String conTitle) {
        this.conTitle = conTitle;
    }

    public String getConDescription() {
        return conDescription;
    }

    public void setConDescription(String conDescription) {
        this.conDescription = conDescription;
    }

    public String getConValue() {
        return conValue;
    }

    public void setConValue(String conValue) {
        this.conValue = conValue;
    }

    public Integer getConType() {
        return conType;
    }

    public void setConType(Integer conType) {
        this.conType = conType;
    }

    @Override
    public String toString() {
        return "Config{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", createUser=" + createUser +
                ", lastUpdateUser=" + lastUpdateUser +
                ", isDeleted=" + isDeleted +
                ", conName='" + conName + '\'' +
                ", conTitle='" + conTitle + '\'' +
                ", conDescription='" + conDescription + '\'' +
                ", conValue='" + conValue + '\'' +
                '}';
    }
}
