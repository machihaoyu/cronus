package com.fjs.cronus.dto.customer;

import io.swagger.annotations.ApiModelProperty;

/**
 * b端保留客户
 * Created by yinzf on 2018/7/13.
 */
public class CustomerDTO3 {
    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CustomerDTO3{" +
                "customerId=" + customerId +
                '}';
    }
}
