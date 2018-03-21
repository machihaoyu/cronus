package com.fjs.cronus.dto.uc;

/**
 * Created by yinzf on 2017/10/20.
 */
public class ThorQueryDto<T> {

    private   int errNum;
    private   String errMsg;
    private    T  retData;
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

    public T getRetData() {
        return retData;
    }

    public void setRetData(T retData) {
        this.retData = retData;
    }
    private String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
