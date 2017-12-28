package com.fjs.cronus.dto.App;

import java.io.Serializable;

/**
 * Created by msi on 2017/12/28.
 */
public class ReceiveAndKeepCountDTO implements Serializable {



    private Integer communicationCount;

    private Integer keepCount;

    public Integer getCommunicationCount() {
        return communicationCount;
    }

    public void setCommunicationCount(Integer communicationCount) {
        this.communicationCount = communicationCount;
    }

    public Integer getKeepCount() {
        return keepCount;
    }

    public void setKeepCount(Integer keepCount) {
        this.keepCount = keepCount;
    }
}
