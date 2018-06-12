package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class BasePagePram<T> {

    @ApiModelProperty(value = "每页item数", dataType = "Integer")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "页码", dataType = "Integer")
    private Integer pageNum = 1;

    private T PramEntity;


    public T getPramEntity() {
        return PramEntity;
    }

    public void setPramEntity(T pramEntity) {
        PramEntity = pramEntity;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null
                || pageSize < 1) {
            return;
        }
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum == null
                || pageNum < 1) {
            return;
        }
        this.pageNum = pageNum;
    }
}
