package com.fjs.cronus.entity;

import com.fjs.cronus.enums.AllocateEnum;

/**
 * Created by Administrator on 2017/12/8 0008.
 */
public class AllocateEntity {
    private boolean success;
    private AllocateEnum allocateStatus;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AllocateEnum getAllocateStatus() {
        return allocateStatus;
    }

    public void setAllocateStatus(AllocateEnum allocateStatus) {
        this.allocateStatus = allocateStatus;
    }

    @Override
    public String toString() {
        return "AllocateEntity{" +
                "success=" + success +
                ", allocateStatus=" + allocateStatus +
                ", description='" + description + '\'' +
                '}';
    }
}
