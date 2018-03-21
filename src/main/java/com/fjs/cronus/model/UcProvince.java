package com.fjs.cronus.model;

import java.util.Date;

public class UcProvince extends BaseModel {
    private Integer id;

    private String name;

    private Integer status;

    private Integer isDirectly;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDirectly() {
        return isDirectly;
    }

    public void setIsDirectly(Integer isDirectly) {
        this.isDirectly = isDirectly;
    }
}