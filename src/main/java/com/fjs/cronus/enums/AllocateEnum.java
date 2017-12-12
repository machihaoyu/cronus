package com.fjs.cronus.enums;

public enum AllocateEnum {

    PUBLIC("0","公盘"),
    ALLOCATE_TO_OWNER("1", "自动分配队列"),
    WAITING_POOL("2", "进入待分配池"),
    EXIST_OWNER("3", "已存在负责人"),
    TO_SERVICE_SYSTEM("4","推入客服系统");


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
