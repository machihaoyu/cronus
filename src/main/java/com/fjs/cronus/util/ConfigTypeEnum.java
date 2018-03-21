package com.fjs.cronus.util;

/**
 * Created by pc on 2017/12/1.
 */
public enum ConfigTypeEnum {
    TYPE_ONE(1),TYPE_TWO(2),TYPE_THREE(3)
    ;
    private Integer type;

    ConfigTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
