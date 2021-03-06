package com.fjs.cronus.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "user_month_info_detail")
public class UserMonthInfoDetail {

    private Integer id;
    private Date created;
    private Integer createid;
    private Integer updated;
    private Integer updateid;
    private Date deleted;
    private Integer deleteid;
    private Integer status;
    @Column(name = "effective_date")
    private String effectiveDate;
    private Integer companyid;
    private Integer mediaid;
    @Column(name = "customer_info")
    private String customerInfo;
    @Column(name = "user_month_info_id")
    private Integer userMonthInfoId;
    @Column(name = "user_id")
    private Integer userId;
    private Integer customerid;
    private Integer type;
    private Integer fromediaid;

    public Integer getFromediaid() {
        return fromediaid;
    }

    public void setFromediaid(Integer fromediaid) {
        this.fromediaid = fromediaid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserMonthInfoId() {
        return userMonthInfoId;
    }

    public void setUserMonthInfoId(Integer userMonthInfoId) {
        this.userMonthInfoId = userMonthInfoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getCreateid() {
        return createid;
    }

    public void setCreateid(Integer createid) {
        this.createid = createid;
    }

    public Integer getUpdated() {
        return updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    public Integer getUpdateid() {
        return updateid;
    }

    public void setUpdateid(Integer updateid) {
        this.updateid = updateid;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public Integer getDeleteid() {
        return deleteid;
    }

    public void setDeleteid(Integer deleteid) {
        this.deleteid = deleteid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public Integer getMediaid() {
        return mediaid;
    }

    public void setMediaid(Integer mediaid) {
        this.mediaid = mediaid;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }
}
