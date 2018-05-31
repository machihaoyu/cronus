package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class FindMediaAssignedCustomerNumItmDTO {

    @ApiModelProperty(value = "一级吧id",notes = "一级吧id", required = true)
    private Integer companyid;

    @ApiModelProperty(value = "来源id",notes = "来源id", required = true)
    private Integer sourceid;

    @ApiModelProperty(value = "媒体id",notes = "媒体id", required = true)
    private Integer mediaid;

    @ApiModelProperty(value = "月格式，yyyy-MM", notes = "月格式，yyyy-MM", required = true)
    private String month;

    @ApiModelProperty(value = "已分配数/实购数；响应数据时会使用,请求传参不用传", notes = "已分配数/实购数")
    private Integer assigned_customer_num;

    public FindMediaAssignedCustomerNumItmDTO() {
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public Integer getSourceid() {
        return sourceid;
    }

    public void setSourceid(Integer sourceid) {
        this.sourceid = sourceid;
    }

    public Integer getMediaid() {
        return mediaid;
    }

    public void setMediaid(Integer mediaid) {
        this.mediaid = mediaid;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getAssigned_customer_num() {
        return assigned_customer_num;
    }

    public void setAssigned_customer_num(Integer assigned_customer_num) {
        this.assigned_customer_num = assigned_customer_num;
    }
}
