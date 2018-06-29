package com.fjs.cronus.dto.ourea;

import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yinzf on 2018/6/28.
 */
public class OureaDTO {

    private T data;

    private int result;

    private String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OureaDTO{" +
                "data=" + data +
                ", result=" + result +
                ", message='" + message + '\'' +
                '}';
    }
}
