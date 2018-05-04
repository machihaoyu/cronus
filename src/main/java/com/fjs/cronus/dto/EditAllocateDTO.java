package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by feng on 2017/11/2.
 */
public class EditAllocateDTO {

    @ApiModelProperty(value="业务员ID")
    private Integer userId;

    @ApiModelProperty(value="分配队列城市")
    private String city;

    @ApiModelProperty(value="一级吧id")
    private Integer companyid;

    @ApiModelProperty(value="媒体id")
    private Integer medialid;

    @ApiModelProperty(value="当前或下月的值")
    private String effectiveDate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public Integer getMedialid() {
        return medialid;
    }

    public void setMedialid(Integer medialid) {
        this.medialid = medialid;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
