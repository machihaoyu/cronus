package com.fjs.cronus.dto;

import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */
public class UserAuthorityScope {
    private Integer userId;
    List<Integer> subCompanyIds = null;//自己能管理的分公司
    List<String> canMangerMainCity = null;//自己能管理的城市

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getSubCompanyIds() {
        return subCompanyIds;
    }

    public void setSubCompanyIds(List<Integer> subCompanyIds) {
        this.subCompanyIds = subCompanyIds;
    }

    public List<String> getCanMangerMainCity() {
        return canMangerMainCity;
    }

    public void setCanMangerMainCity(List<String> canMangerMainCity) {
        this.canMangerMainCity = canMangerMainCity;
    }
}
