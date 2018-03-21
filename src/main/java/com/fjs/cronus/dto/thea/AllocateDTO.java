package com.fjs.cronus.dto.thea;

import io.swagger.annotations.ApiModelProperty;

/**
 * 手动分配DTO
 * Created by yinzf on 2017/10/25.
 */
public class AllocateDTO {

    @ApiModelProperty(value = "客户id（必填）")
    private String ids;
    @ApiModelProperty(value = "业务员id（必填）")
    private Integer empId;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    @Override
    public String toString() {
        return "AllocateDTO{" +
                "ids='" + ids + '\'' +
                ", empId=" + empId +
                '}';
    }
}
