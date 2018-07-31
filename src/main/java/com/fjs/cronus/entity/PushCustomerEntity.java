package com.fjs.cronus.entity;

import java.util.Date;

public class PushCustomerEntity {


    private Integer id; // int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    private Integer customerInfoId; // int(11) NOT NULL COMMENT '客户信息表id',
    private Integer subCompanyId; // int(11) NOT NULL COMMENT '分公司id(和uc同步)',
    private String subCompanyName; // varchar(255) NOT NULL COMMENT '分公司名称(和uc同步)',
    private Date gmtCreate; // datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间(创建时间)',
    private Date gmtModified; // datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    private Integer isDeleted; // tinyint(11) DEFAULT '0' COMMENT '是否删除(0:未删除,1:已删除)',


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerInfoId() {
        return customerInfoId;
    }

    public void setCustomerInfoId(Integer customerInfoId) {
        this.customerInfoId = customerInfoId;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSubCompanyName() {
        return subCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        this.subCompanyName = subCompanyName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
