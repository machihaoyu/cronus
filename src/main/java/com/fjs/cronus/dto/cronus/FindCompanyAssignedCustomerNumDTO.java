package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class FindCompanyAssignedCustomerNumDTO {

    @ApiModelProperty(value = "需查询的目标数据",notes = "需查询的目标数据")
    private List<FindCompanyAssignedCustomerNumItmDTO> list;

    public List<FindCompanyAssignedCustomerNumItmDTO> getList() {
        return list;
    }

    public void setList(List<FindCompanyAssignedCustomerNumItmDTO> list) {
        this.list = list;
    }
}
