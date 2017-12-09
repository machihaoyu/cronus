package com.fjs.cronus.enums;

public enum AllocateEnum {

    SUCCESS("0","公盘"),

    CRM_DATA_SUCCESS("1", "分配到负责人"),

    CRM_FILE_DATA_SUCCESS("2", "进入待分配池");


    private String code;

    private String desc;


    AllocateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AllocateEnum getByCode(String code) {
        for (AllocateEnum enumObj : AllocateEnum.values()) {
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
