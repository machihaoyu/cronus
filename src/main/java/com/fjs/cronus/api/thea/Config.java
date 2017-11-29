package com.fjs.cronus.api.thea;

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

    private Date createTime;

    private Date lastUpdateTime;

    private Integer createUser;

    private Integer lastUpdateUser;

    private Integer isDeleted;

    private String name;

    private String title;

    private String description;

    private String value;
    private Integer type;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
