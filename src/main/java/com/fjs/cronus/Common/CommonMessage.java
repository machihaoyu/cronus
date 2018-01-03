package com.fjs.cronus.Common;

/**
 * Created by yinzf on 2017/10/14.
 */
public enum CommonMessage {
    SUCCESS(0,"成功"),
    FAIL(1,"失败"),

    ADD_SUCCESS(0,"添加成功"),
    ADD_FAIL(1,"添加失败"),

    UPDATE_SUCCESS(0,"更新成功"),
    UPDATE_FAIL(1,"更新失败"),
    UPDATE_FAIL_OWNER(2,"转入失败，该客户已经有负责人"),

    DELETE_SUCCESS(0,"删除成功"),
    DELETE_FAIL(1,"删除失败"),

    PULL_SUCCESS(0,"领取成功"),
    PULL_FAIL(1,"领取失败"),

    REMOVE_SUCCESS(0,"扔回成功"),
    REMOVE_FAIL(1,"扔回失败"),

    ALLOCATE_SUCCESS(0,"分配成功"),
    ALLOCATE_FAIL(1,"分配失败"),

    KEEP_SUCCESS(0,"保留客户成功"),
    KEEP_FAIL(1,"保留客户失败"),

    TRANSFER_SUCCESS(0,"原始盘转入交易成功"),
    TRANSFER_FAIL(1,"原始盘转入交易失败"),

    CHANGE_SUCCESS(0,"改变原始盘状态成功"),
    CHANGE_FAIL(1,"改变原始盘状态失败"),

    TRANSFER_MONEY_SUCCESS(0,"订转佣成功"),
    TRANSFER_MONEY_FAIL(1,"订转佣失败"),

    CANCEL_SUCCESS(0,"取消保留客户成功"),
    CANCEL_FAIL(1,"取消保留客户失败");



    /**
     * code值
     */
    private final Integer code;

    /**
     * code对应的描述
     */
    private final String codeDesc;

    CommonMessage(Integer code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    public Integer getCode() {
        return code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }
}
