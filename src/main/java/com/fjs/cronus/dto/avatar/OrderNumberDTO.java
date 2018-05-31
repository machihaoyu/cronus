package com.fjs.cronus.dto.avatar;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class OrderNumberDTO {

    @ApiModelProperty(value = "一级巴订购总数")
    private Integer totalNumber;

    private List<OrderNumberDetailDTO> orderNumberList;

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<OrderNumberDetailDTO> getOrderNumberList() {
        return orderNumberList;
    }

    public void setOrderNumberList(List<OrderNumberDetailDTO> orderNumberList) {
        this.orderNumberList = orderNumberList;
    }
}
