package com.fjs.cronus.dto.uc;

/**
 * Created by msi on 2017/7/6.
 */
public class AllUserDTO<T> {
    private String roleid;
    private T data;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
