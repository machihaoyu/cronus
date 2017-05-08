package com.fjs.cronus.dto;

import java.io.Serializable;

public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 4772200998371061226L;

    private Integer errNum;
    private String errMsg;
    private UserInfoDTO user_info;

    public Integer getErrNum() {
        return errNum;
    }

    public void setErrNum(Integer errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public UserInfoDTO getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoDTO user_info) {
        this.user_info = user_info;
    }

}
