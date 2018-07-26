package com.fjs.cronus.entity;


import java.util.Date;

public class MediaCustomerCountEntity {

    private Integer id; // int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    private String sourceName; // varchar(255) DEFAULT NULL COMMENT '来源名称',
    private String mediaName; // varchar(255) DEFAULT NULL COMMENT '媒体名称',
    private Integer customerStock; // int(11) DEFAULT '0' COMMENT '商机池客户存量',
    private Integer  purchasedNumber; // int(11) DEFAULT '0' COMMENT '已购买量',
    private Date gmtCreate; // TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    private Date gmtModified; // TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    private Integer isDeleted; // tinyint(11) DEFAULT '0' COMMENT '是否删除(0:未删除,1:已删除)',


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public Integer getCustomerStock() {
        return customerStock;
    }

    public void setCustomerStock(Integer customerStock) {
        this.customerStock = customerStock;
    }

    public Integer getPurchasedNumber() {
        return purchasedNumber;
    }

    public void setPurchasedNumber(Integer purchasedNumber) {
        this.purchasedNumber = purchasedNumber;
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
