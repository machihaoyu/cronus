package com.fjs.cronus.api.thea;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by feng on 2017/9/13.
 */
public class Config implements Serializable{

    /*`id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(30) NOT NULL COMMENT '配置英文名称',
    `title` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '配置中文名',
    `description` text NOT NULL COMMENT '配置描述',
    `value` text NOT NULL COMMENT '配置值',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `last_update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `create_user` int(11) DEFAULT NULL COMMENT '创建人',
    `last_update_user` int(11) DEFAULT NULL COMMENT '更新人',
    `is_deleted` tinyint(11) DEFAULT NULL COMMENT '删除标识',*/

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
