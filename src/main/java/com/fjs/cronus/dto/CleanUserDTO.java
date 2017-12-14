package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yinzf on 2017/12/14.
 */
public class CleanUserDTO {
    @ApiModelProperty(value = "业务员id（必填）")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "cleanUserDTO{" +
                "userId=" + userId +
                '}';
    }
}
