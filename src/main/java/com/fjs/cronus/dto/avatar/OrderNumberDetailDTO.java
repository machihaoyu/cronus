package com.fjs.cronus.dto.avatar;

import io.swagger.annotations.ApiModelProperty;

public class OrderNumberDetailDTO {

    @ApiModelProperty(value = "媒体id")
    private Integer meidaId;
    @ApiModelProperty(value = "商机明细的订购数")
    private Integer orderNumber;


    public Integer getMeidaId() {
        return meidaId;
    }

    public void setMeidaId(Integer meidaId) {
        this.meidaId = meidaId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
