package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class FindMediaAssignedCustomerNumDTO {

    @ApiModelProperty(value = "需查询的目标数据",notes = "需查询的目标数据")
    private List<FindMediaAssignedCustomerNumItmDTO> list;

    public List<FindMediaAssignedCustomerNumItmDTO> getList() {
        return list;
    }

    public void setList(List<FindMediaAssignedCustomerNumItmDTO> list) {
        this.list = list;
    }
}
