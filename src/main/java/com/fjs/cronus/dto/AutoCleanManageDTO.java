package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 添加渠道，来源dto
 * Created by yinzf on 2017/12/14.
 */
public class AutoCleanManageDTO {
    private Integer id;

    @ApiModelProperty(value = "来源")
    private String customerSource;
    @ApiModelProperty(value = "渠道")
    private String utmSource;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    @Override
    public String toString() {
        return "AutoCleanManageDTO{" +
                "id=" + id +
                ", customerSource='" + customerSource + '\'' +
                ", utmSource='" + utmSource + '\'' +
                '}';
    }
}
