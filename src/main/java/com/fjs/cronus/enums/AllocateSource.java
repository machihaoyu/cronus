package com.fjs.cronus.enums;

public enum AllocateSource {


    OCDC("0","OCDC"),

    SERVICES("1", "客服"),

    WAITING("2", "待分配池");

    private String code;

    private String desc;


    AllocateSource(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AllocateSource getByCode(String code) {
        for (AllocateSource enumObj : AllocateSource.values()) {
            if (enumObj.getCode().equals(code)) {
                return enumObj;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }



}
