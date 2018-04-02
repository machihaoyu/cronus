package com.fjs.cronus.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */
public class SMSMessage {
    private String key;
    private List<String> mobile;
    private String message;
    public void setKey(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }

    public void setMobile(List<String> mobile) {
        this.mobile = mobile;
    }
    public List<String> getMobile() {
        return mobile;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
