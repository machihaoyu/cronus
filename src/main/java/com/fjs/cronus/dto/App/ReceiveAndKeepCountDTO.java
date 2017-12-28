package com.fjs.cronus.dto.App;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2017/12/28.
 */
public class ReceiveAndKeepCountDTO implements Serializable {



    @ApiModelProperty(value = "分配数",notes = "分配数")
    private Integer allocateCount;

    @ApiModelProperty(value = "分配沟通数",notes = "分配沟通数")
    private Integer allocateCommunicationCount;

    @ApiModelProperty(value = "保留数",notes = "保留数")
    private Integer keepCount;

    @ApiModelProperty(value = "保留沟通数",notes = "保留沟通数")
    private Integer keepCommunicationCount;

    public Integer getAllocateCount() {
        return allocateCount;
    }

    public void setAllocateCount(Integer allocateCount) {
        this.allocateCount = allocateCount;
    }

    public Integer getAllocateCommunicationCount() {
        return allocateCommunicationCount;
    }

    public void setAllocateCommunicationCount(Integer allocateCommunicationCount) {
        this.allocateCommunicationCount = allocateCommunicationCount;
    }

    public Integer getKeepCount() {
        return keepCount;
    }

    public void setKeepCount(Integer keepCount) {
        this.keepCount = keepCount;
    }

    public Integer getKeepCommunicationCount() {
        return keepCommunicationCount;
    }

    public void setKeepCommunicationCount(Integer keepCommunicationCount) {
        this.keepCommunicationCount = keepCommunicationCount;
    }
}
