package com.fjs.cronus.entity;

import com.fjs.cronus.dto.CommunicationLogDTO;

import java.util.List;

public class CommunicationLog {

    private int errNum;

    private String errMsg;

    private List<CommunicationLogDTO> retData;

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<CommunicationLogDTO> getRetData() {
        return retData;
    }

    public void setRetData(List<CommunicationLogDTO> retData) {
        this.retData = retData;
    }
}
