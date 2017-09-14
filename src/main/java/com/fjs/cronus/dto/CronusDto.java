package com.fjs.cronus.dto;

/**
 * Created by msi on 2017/9/14.
 */
public class CronusDto<T> {
    private T data;

    private int result;

    private String message;

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
