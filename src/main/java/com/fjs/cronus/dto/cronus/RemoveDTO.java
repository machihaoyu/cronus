package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2017/12/4.
 */
public class RemoveDTO implements Serializable {

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
        return "RemoveDTO{" +
                "ids='" + ids + '\'' +
                ", empId=" + empId +
                '}';
    }

}
