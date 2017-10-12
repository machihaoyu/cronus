package com.fjs.cronus.dto.cronus;

/**
 * Created by msi on 2017/10/12.
 */
public class PhoneResultDTO<T> {

    private String errNum;
    private String errMsg;
    private T retData;

    public String getErrNum() {
        return errNum;
    }

    public void setErrNum(String errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getRetData() {
        return retData;
    }

    public void setRetData(T retData) {
        this.retData = retData;
    }
}
