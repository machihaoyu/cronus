package com.fjs.cronus.dto.uc;

import java.util.HashMap;

/**
 * Created by msi on 2017/7/6.
 */
public class SwitchSystemDTO {

    private Integer errNum;
    private String errMsg;
    private SwitchSystemInfoDTO user_info;
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

    public SwitchSystemInfoDTO getUser_info() {
        return user_info;
    }

    public void setUser_info(SwitchSystemInfoDTO user_info) {
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
