package com.fjs.cronus.entity;

/**
 * 基础服务-登录响应
 * Created by crm on 2017/4/27.
 */
public class BaleLoginInfo {

    private String errNum;
    private String errMsg;
    private String user_info;

    public String getErrNum() {
        return errNum;
    }

    public void setErrNum(String errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

}
