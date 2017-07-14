package com.fjs.cronus.dto.saas;

/**
 * Created by Administrator on 2017/7/14 0014.
 */
public class SaasApiDTO {

    private Object data;

    public SaasApiDTO(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
