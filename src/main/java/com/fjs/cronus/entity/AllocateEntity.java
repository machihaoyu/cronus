package com.fjs.cronus.entity;

import com.fjs.cronus.enums.AllocateEnum;

/**
 * Created by Administrator on 2017/12/8 0008.
 */
public class AllocateEntity {
    private boolean success;
    private AllocateEnum allocateStatus;

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
}
