package com.fjs.cronus.dto.api;

import com.fjs.cronus.dto.uc.UserInfoDTO;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/3 0003.
 */
public class PHPLoginDto {
    private Integer errNum;
    private String errMsg;
    private String access_token;
    private UserInfoDTO user_info;
    private String[] authority;
    HashMap[] menu;

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

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public UserInfoDTO getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoDTO user_info) {
        this.user_info = user_info;
    }

    public String[] getAuthority() {
        return authority;
    }

    public void setAuthority(String[] authority) {
        this.authority = authority;
    }

    public HashMap[] getMenu() {
        return menu;
    }

    public void setMenu(HashMap[] menu) {
        this.menu = menu;
    }
}
