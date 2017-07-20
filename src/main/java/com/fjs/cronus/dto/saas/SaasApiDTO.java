package com.fjs.cronus.dto.saas;

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
        this.message = message;
        this.data = data;
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
