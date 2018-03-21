package com.fjs.cronus.enums;

/**
 * Created by chenjie on 2017/12/19.
 */
public enum CategoryEnum {
    IDENTITY(1,"身份证"),
    HOUSEHOLDREGISTER(2,"户口簿"),
    PROPERTYCERTIFICATE(3,"房产证"),
    MARRIAGECERTIFICATE(4,"结婚证"),
    VOUCHER(5,"放款凭证"),
    ;

    private Integer code;
    private String name;

    CategoryEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static CategoryEnum getEnumByCode(Integer code){
        for (CategoryEnum e : CategoryEnum.values()){
            if (e.getCode().equals(code))
                return e;
        }
        return null;
    }

}
