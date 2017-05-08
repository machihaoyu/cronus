package com.fjs.cronus.enums;

public enum ErrorNumEnum {

    SUCCESS("0","成功"),

    CRM_DATA_SUCCESS("1", "成功"),

    CRM_FILE_DATA_SUCCESS("1", "成功"),

    ERROR("-1","错误");

    private String code;

    private String desc;


    private ErrorNumEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ErrorNumEnum getByCode(String code) {
        for (ErrorNumEnum enumObj : ErrorNumEnum.values()) {
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
