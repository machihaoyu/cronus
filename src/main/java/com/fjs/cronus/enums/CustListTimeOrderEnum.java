package com.fjs.cronus.enums;

public enum CustListTimeOrderEnum {

    CUS_ORDER_RECEIVE_TIME("receive_time","分配时间"),
    CUS_ORDER_CREATETIME("create_time","创建时间"),
    CUS_ORDER_LAST_UPDATE_TIME("last_update_time","跟进时间"),
    ;

    private String code;
    private String name;

    CustListTimeOrderEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static CustListTimeOrderEnum getEnumByCode(String code){
        for (CustListTimeOrderEnum e : CustListTimeOrderEnum.values()){
            if (e.getCode().equals(code))
                return e;
        }
        return null;
    }


}