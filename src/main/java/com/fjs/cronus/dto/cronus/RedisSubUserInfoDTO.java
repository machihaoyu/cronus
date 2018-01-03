package com.fjs.cronus.dto.cronus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by msi on 2018/1/3.
 */
public class RedisSubUserInfoDTO implements Serializable {

   private List<Integer> subCompanyId;

   private  List<String> canMangerMainCity ;

    public List<Integer> getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(List<Integer> subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public List<String> getCanMangerMainCity() {
        return canMangerMainCity;
    }

    public void setCanMangerMainCity(List<String> canMangerMainCity) {
        this.canMangerMainCity = canMangerMainCity;
    }
}
