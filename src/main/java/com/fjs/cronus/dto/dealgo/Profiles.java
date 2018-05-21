package com.fjs.cronus.dto.dealgo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */
public class Profiles {
    private String message;
    private String number;
    private List<Profile> profile;
    private String uniqueId;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setProfile(List<Profile> profile) {
        this.profile = profile;
    }

    public List<Profile> getProfile() {
        return profile;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
