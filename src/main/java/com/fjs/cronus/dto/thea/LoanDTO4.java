package com.fjs.cronus.dto.thea;

import io.swagger.annotations.ApiModelProperty;

/**
 * 客户系统dto
 * Created by yinzf on 2017/12/11.
 */
public class LoanDTO4 {
    @ApiModelProperty(value = "客户Id（单个）")
    private Integer customerId;
    @ApiModelProperty(value = "接手员工Id")
    private Integer newOwnnerId;
    @ApiModelProperty(value = "客户Id集")
    private String ids;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getNewOwnnerId() {
        return newOwnnerId;
    }

    public void setNewOwnnerId(Integer newOwnnerId) {
        this.newOwnnerId = newOwnnerId;
    }

    @Override
    public String toString() {
        return "LoanDTO4{" +
                "customerId=" + customerId +
                ", newOwnnerId=" + newOwnnerId +
                ", ids='" + ids + '\'' +
                '}';
    }
}
