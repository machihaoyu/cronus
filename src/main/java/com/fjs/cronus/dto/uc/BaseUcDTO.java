package com.fjs.cronus.dto.uc;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/19 0019.
 */
public class BaseUcDTO<T> implements Serializable {

    private static final long serialVersionUID = 1009503547913045801L;

    private String errMsg;
    private int errNum;
    private T retData;

    public BaseUcDTO() {}

    public BaseUcDTO(int errNum, String errMsg, T obj) {
        this.errNum = errNum;
        if (StringUtils.isEmpty(errMsg)) {
            this.errMsg = "请求成功!";
        } else {
            this.errMsg = errMsg;
        }
        this.retData = obj;
    }

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
