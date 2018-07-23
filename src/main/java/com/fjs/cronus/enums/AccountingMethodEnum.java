package com.fjs.cronus.enums;

public enum AccountingMethodEnum {

    PURCHASE_UNIT_PRICE(1,"预购单价"),
    COMMISSION_RATE(2,"佣金比例"),
    LOAN_TATE(3,"放款比例"),
    PURCHASE_PRICE_COMMISSION(4,"预购单价&佣金比例"),
    PURCHASE_PRICE_RATE(5,"预购单价&放款比例"),

    ;


    private final Integer code;
    private final String desc;


    AccountingMethodEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AccountingMethodEnum findByCode(Integer code) {
        for (AccountingMethodEnum penum : AccountingMethodEnum.values()) {
            if (penum.getCode().equals(code)) {
                return penum;
            }
        }
        return null;
    }

}
