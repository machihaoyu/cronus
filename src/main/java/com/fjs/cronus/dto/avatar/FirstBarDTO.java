package com.fjs.cronus.dto.avatar;

import io.swagger.annotations.ApiModelProperty;

public class FirstBarDTO {

    @ApiModelProperty(value = "主键id")
    private Integer id;
    @ApiModelProperty(value = "一级巴名称")
    private String firstBar;
    @ApiModelProperty(value = "一级巴城市")
    private String city;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstBar() {
        return firstBar;
    }

    public void setFirstBar(String firstBar) {
        this.firstBar = firstBar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
