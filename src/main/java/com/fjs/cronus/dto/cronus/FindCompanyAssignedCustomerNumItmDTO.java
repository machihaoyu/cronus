package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

public class FindCompanyAssignedCustomerNumItmDTO {

    @ApiModelProperty(value = "一级吧id",notes = "一级吧id", required = true)
    private Integer companyid;

    @ApiModelProperty(value = "月格式，yyyy-MM", notes = "月格式，yyyy-MM", required = true)
    private String month;

    @ApiModelProperty(value = "已分配数/实购数；响应数据时会使用,请求传参不用传", notes = "已分配数/实购数")
    private Integer assigned_customer_num;

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
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
