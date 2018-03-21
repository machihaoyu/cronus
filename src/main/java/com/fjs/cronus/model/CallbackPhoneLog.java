package com.fjs.cronus.model;

import java.util.Date;

public class CallbackPhoneLog extends BaseModel {
    private Integer id;

    private Integer operatUserId;

    private String operatUserName;

    private Integer customerId;

    private String status;

    private String description;



    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOperatUserId() {
        return operatUserId;
    }

    public void setOperatUserId(Integer operatUserId) {
        this.operatUserId = operatUserId;
    }

    public String getOperatUserName() {
        return operatUserName;
    }

    public void setOperatUserName(String operatUserName) {
        this.operatUserName = operatUserName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}