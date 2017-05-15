package com.fjs.cronus.dto.login;

/**
 * Created by crm on 2017/5/15.
 */
public class AuthorityDTO {

    private String authorUrl;
    private String actionTitle;
    private Integer enable;

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(String actionTitle) {
        this.actionTitle = actionTitle;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}
