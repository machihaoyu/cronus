package com.fjs.cronus.model;

import java.util.Date;

/**
 * Created by feng on 2017/10/9.
 * 再次分配记录表
 */
public class AgainAllocateCustomer {

     private static final long serialVersionUID = 1L;

     private Integer Id;

     private Integer dataId;

     private String jsonData;

     private Integer status;

     private Date updateTime;

     private Date createTime;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
