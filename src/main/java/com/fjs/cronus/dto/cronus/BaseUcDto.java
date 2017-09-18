package com.fjs.cronus.dto.cronus;

import com.fjs.cronus.dto.uc.FegionExceptionDTO;
import com.fjs.cronus.dto.uc.FegionResponseErrorDTO;
import com.fjs.cronus.util.StringAsciiUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by msi on 2017/9/18.
 */
public class BaseUcDto<T> implements Serializable{
    private static final long serialVersionUID = 1009503547913045851L;

    private String errMsg;
    private int errNum;
    private T data;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public BaseUcDto() {}
    public BaseUcDto(int errNum, String errMsg, T obj) {
        this.errNum = errNum;
        if (StringUtils.isEmpty(errMsg)) {
            this.errMsg = "请求成功!";
        } else {
            Map<String, Object> map = StringAsciiUtil.fegionException(errMsg);
            if (null != map && map.size() > 0) {
                if (map.containsKey("fegionExceptionDTO")) {
                    FegionExceptionDTO fegionExceptionDTO = (FegionExceptionDTO)map.get("fegionExceptionDTO");
                    this.errMsg = fegionExceptionDTO.getError();
                } else {
                    FegionResponseErrorDTO responseError = (FegionResponseErrorDTO)map.get("responseError");
                    this.errMsg = responseError.getMessage();
                }
            } else {
                this.errMsg = errMsg;
            }
        }
        if (null == obj) {
            this.data = (T)"";
        } else {
            this.data = obj;
        }
    }
}
