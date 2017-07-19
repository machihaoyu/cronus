package com.fjs.cronus.dto.uc;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/19 0019.
 */
public class BaseUcDTO<T> implements Serializable {

    private static final long serialVersionUID = 1009503547913045801L;

    private String errMsg;
    private int errNum;
    private T retData;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public T getRetData() {
        return retData;
    }

    public void setRetData(T retData) {
        this.retData = retData;
    }
}
