package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by feng on 2017/11/2.
 */
public class EditAllocateDTO {

    @ApiModelProperty(value="业务员ID")
    private Integer userId;

    @ApiModelProperty(value="分配队列城市")
    private String city;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
