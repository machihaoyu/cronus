package com.fjs.cronus.dto.avatar;

import io.swagger.annotations.ApiModelProperty;

public class AvatarApiDTO<T> {

    @ApiModelProperty(value = "返回状态码：0成功，其他失败")
    private int result = 0;
    @ApiModelProperty(value = "返回信息：成功，失败")
    private String message;
    @ApiModelProperty(value = "结果集")
    private T data;

    public AvatarApiDTO(int result, String message,T data) {
        this.data = data;
        this.result = result;
        this.message = message;
    }

    public AvatarApiDTO() {
    }

    //成功调用构造
    public AvatarApiDTO(T data) {
        this.data = data;
    }

    //失败调用构造
    public AvatarApiDTO(int result, String message) {
        this.result = result;
        this.message = message;
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
