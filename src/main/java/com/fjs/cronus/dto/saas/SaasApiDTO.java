package com.fjs.cronus.dto.saas;

import com.fjs.cronus.dto.uc.FegionExceptionDTO;
import com.fjs.cronus.dto.uc.FegionResponseErrorDTO;
import com.fjs.cronus.util.StringAsciiUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/14 0014.
 */
public class SaasApiDTO<T> {

    private T data;
    private String message;
    private int result;

    public SaasApiDTO(){}

    public SaasApiDTO(int result, String message, T data) {
        this.result = result;
        if (StringUtils.isEmpty(message)) {
            this.message = "请求成功!";
        } else {
            Map<String, Object> map = StringAsciiUtil.fegionException(message);
            if (null != map && map.size() > 0) {
                if (map.containsKey("fegionExceptionDTO")) {
                    FegionExceptionDTO fegionExceptionDTO = (FegionExceptionDTO)map.get("fegionExceptionDTO");
                    this.message = fegionExceptionDTO.getError();
                } else {
                    FegionResponseErrorDTO responseError = (FegionResponseErrorDTO)map.get("responseError");
                    this.message = responseError.getMessage();
                }
            } else {
                this.message = message;
            }
        }
        if (null == data) {
            this.data = (T)"";
        } else {
            this.data = data;
        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
