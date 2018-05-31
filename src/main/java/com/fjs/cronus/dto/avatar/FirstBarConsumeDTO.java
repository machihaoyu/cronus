package com.fjs.cronus.dto.avatar;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class FirstBarConsumeDTO {

    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "媒体id")
    private Integer media;
    @ApiModelProperty(value = "一级巴id")
    private Integer firstBarId;
    @ApiModelProperty(value = "开始时间")
    private Long startTime;
    @ApiModelProperty(value = "结束时间")
    private Long endTime;
    @ApiModelProperty(value = "分配数")
    private Integer allocate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }

    public Integer getFirstBarId() {
        return firstBarId;
    }

    public void setFirstBarId(Integer firstBarId) {
        this.firstBarId = firstBarId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getAllocate() {
        return allocate;
    }

    public void setAllocate(Integer allocate) {
        this.allocate = allocate;
    }

    @Override
    public String toString() {
        return "FirstBarConsumeDTO{" +
                "id=" + id +
                ", media=" + media +
                ", firstBarId=" + firstBarId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", allocate=" + allocate +
                '}';
    }
}
