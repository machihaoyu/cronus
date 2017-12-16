package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by msi on 2017/12/16.
 */
public class PullStatusDTO {

    @ApiModelProperty(value = "id（必填）")
    private Integer id;

    @ApiModelProperty(value = "想要更改的状态 转入crm状态: 0正常， -1重复，-2无效,  1转入 crm成功 ")
    private Integer toStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getToStatus() {
        return toStatus;
    }

    public void setToStatus(Integer toStatus) {
        this.toStatus = toStatus;
    }
}
