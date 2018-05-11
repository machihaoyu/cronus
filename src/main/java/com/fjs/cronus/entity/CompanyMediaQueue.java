package com.fjs.cronus.entity;

import java.util.Date;

/**
 * 单位、媒体的队列表；一级吧、媒体的队列表.
 */
public class CompanyMediaQueue {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 创建人
     */
    private Integer createid;
    /**
     * 修改时间
     */
    private Date updated;
    /**
     * 修改人
     */
    private Integer updateid;
    /**
     * 删除时间
     */
    private Date deleted;
    /**
     * 删除人
     */
    private Integer deleteid;
    /**
     * 数据状态；0-删除，1-正常
     */
    private Integer status;
    /**
     * 单位id
     */
    private Integer companyid;
    /**
     * 媒体id
     */
    private Integer mediaid;

    /**
     * 该数据所属月份，格式yyyyMM.
     */
    private String yearmonth;

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
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
}
