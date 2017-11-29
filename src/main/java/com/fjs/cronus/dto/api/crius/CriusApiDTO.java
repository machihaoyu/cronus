package com.fjs.cronus.dto.api.crius;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Administrator on 2017/6/5 0005.
 */
public class CriusApiDTO<T> {

    @ApiModelProperty(value = "返回状态码：0成功，其他失败")
    private int result;
    @ApiModelProperty(value = "返回信息：成功，失败")
    private String message;
    @ApiModelProperty(value = "结果集")
    private T data;

    public CriusApiDTO(int result, String message, T data) {
        this.data = data;
        this.result = result;
        this.message = message;
    }

    public CriusApiDTO() {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
