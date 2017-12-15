package com.fjs.cronus.dto;

import com.fjs.cronus.model.UserMonthInfo;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by feng on 2017/10/27.
 */
public class UserMonthInfoDTO extends UserMonthInfo {

    @ApiModelProperty(value = "业务员名", required = false)
    private String name;

    @ApiModelProperty(value = "团队名", required = false)
    private String departmentName;

    @ApiModelProperty(value = "团队ID", required = false)
    private Integer departmentId;

    @ApiModelProperty(value = "是否在城市队列中", required = false)
    private Integer isSelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
