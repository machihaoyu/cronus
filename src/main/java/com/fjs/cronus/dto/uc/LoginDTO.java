package com.fjs.cronus.dto.uc;

/**
 * Created by Administrator on 2017/7/28 0028.
 */
public class LoginDTO {

    public String username;
    private String password;
    private String system;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
