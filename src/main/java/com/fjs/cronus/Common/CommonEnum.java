package com.fjs.cronus.Common;

/**
 * Created by yinzf on 2017/9/14.
 */
public enum CommonEnum {


    //是/否
    NO(0, "否"),
    YES(1, "是"),

    //调用uc
    ROLE_YES(0,"成功"),
    ROLE_NO(1,"失败"),

    //客户登记枚举
    CUSTOMER_LEVEL_0(0, "意向客户"),

    //性别枚举
    SEX_MALE(0, "男"),
    SEX_FEMALE(1, "女"),

    //是否按揭
    HOUSE_LOAN_0(0, "否"),
    HOUSE_LOAN_1(1, "是"),

    //是否有备用房
    HOUSE_ALONE_0(0, "否"),
    HOUSE_ALONE_1(1, "是"),

    //客户保留状态
    RETAIN_STATUE_0(0, "不保留"),
    RETAIN_STATUE_1(1, "保留"),
    RETAIN_STATUE_2(2, "已签合同"),

    //是否锁定
    IS_LOCK_0(0, "否"),
    IS_LOCK_1(1, "是"),

    LAIYUAN_0(0, "OCDC"),
    LAIYUAN_1(1, "客服"),

    //客户状态
    CUSTOMER_CLASSIFY_1(1, "正常"),


    //交易状态
    LOAN_STATUE_1(1, "申请"),
    LOAN_STATUE_2(2, "沟通"),
    LOAN_STATUE_3(3, "面见"),
    LOAN_STATUE_4(4, "审核"),
    LOAN_STATUE_5(5, "等待结案申请"),
    LOAN_STATUE_6(6, "完结"),
    LOAN_STATUE_7(7, "交易失败"),

    //客户类型
    CUSTOMER_TYPE_0(0, "意向客户"),
    CUSTOMER_TYPE_1(0, "协议客户"),
    CUSTOMER_TYPE_2(0, "成交客户"),

    //客户操作类型
    LOAN_OPERATION_TYPE_0(0, "自动分配"),
    LOAN_OPERATION_TYPE_1(1, "手动分配"),
    LOAN_OPERATION_TYPE_2(2, "领取客户"),
    LOAN_OPERATION_TYPE_3(3, "查看号码"),
    LOAN_OPERATION_TYPE_4(4, "未沟通分配"),
    LOAN_OPERATION_TYPE_5(5, "添加客户到公盘"),
    LOAN_OPERATION_TYPE_6(6, "新增交易"),
    LOAN_OPERATION_TYPE_7(7, "修改客户"),
    LOAN_OPERATION_TYPE_8(8, "自动清洗"),
    LOAN_OPERATION_TYPE_9(9, "扔回公盘"),
    LOAN_OPERATION_TYPE_10(10, "删除交易"),
    LOAN_OPERATION_TYPE_11(11, "保留客户"),
    LOAN_OPERATION_TYPE_12(12, "取消保留客户"),
    LOAN_OPERATION_TYPE_13(13, "添加交易"),

    //分配日志操作类型
    ALLOCATE_LOG_OPERATION_TYPE_1(1, "自动分配"),
    ALLOCATE_LOG_OPERATION_TYPE_2(2, "手动分配"),
    ALLOCATE_LOG_OPERATION_TYPE_3(3, "未沟通分配"),
    ALLOCATE_LOG_OPERATION_TYPE_4(4, "线下录入"),
    ALLOCATE_LOG_OPERATION_TYPE_5(5, "自动分配(带业务员)"),
    ALLOCATE_LOG_OPERATION_TYPE_6(6, "外地拓展分配"),
    ALLOCATE_LOG_OPERATION_TYPE_7(7, "其他分配"),
    ALLOCATE_LOG_OPERATION_TYPE_8(8, "自动清洗"),
    ALLOCATE_LOG_OPERATION_TYPE_9(9,"离职移交"),
    ALLOCATE_LOG_OPERATION_TYPE_10(10,"扔回公盘"),
    //再分配盘数据标记
    AGAIN_ALLOCATE_STATUS_0(0, "未再分配"),
    AGAIN_ALLOCATE_STATUS_1(1, "已再分配"),

    // entity 对应 status 字段的值
    entity_status0(0, "删除"),
    entity_status1(1, "正常"),


    //星期
    WEEK_OF_MONDAY(1,"星期一"),
    WEEK_OF_TUESDAY(2,"星期二"),
    WEEK_OF_WEDNESDAY(3,"星期三"),
    WEEK_OF_THURSDAY(4,"星期四"),
    WEEK_OF_FRIDAY(5,"星期五"),
    WEEK_OF_SATURDAY(6,"星期六"),
    WEEK_OF_SUNDAY(7,"星期日")

    ;

    /**
     * code值
     */
    private final Integer code;

    /**
     * code对应的描述
     */
    private final String codeDesc;

    CommonEnum(Integer code, String codeDesc) {
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
