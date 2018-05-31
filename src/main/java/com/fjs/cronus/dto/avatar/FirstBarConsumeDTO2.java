package com.fjs.cronus.dto.avatar;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class FirstBarConsumeDTO2 {

    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "媒体id")
    private Integer media;
    @ApiModelProperty(value = "一级巴id")
    private Integer firstBarId;
    @ApiModelProperty(value = "分配数")
    private Integer allocate;

    private Date startTimeParse;
    private Date endTimeParse;

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

    public Integer getAllocate() {
        return allocate;
    }

    public void setAllocate(Integer allocate) {
        this.allocate = allocate;
    }

    public Date getStartTimeParse() {
        return startTimeParse;
    }

    public void setStartTimeParse(Date startTimeParse) {
        this.startTimeParse = startTimeParse;
    }

    public Date getEndTimeParse() {
        return endTimeParse;
    }

    public void setEndTimeParse(Date endTimeParse) {
        this.endTimeParse = endTimeParse;
    }

    @Override
    public String toString() {
        return "FirstBarConsumeDTO2{" +
                "id=" + id +
                ", media=" + media +
                ", firstBarId=" + firstBarId +
                ", allocate=" + allocate +
                ", startTimeParse=" + startTimeParse +
                ", endTimeParse=" + endTimeParse +
                '}';
    }
}
