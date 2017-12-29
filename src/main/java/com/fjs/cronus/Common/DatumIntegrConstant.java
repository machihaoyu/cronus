package com.fjs.cronus.Common;

/**
 * Created by chenjie on 2017/12/16.
 */
public interface DatumIntegrConstant {

    /** 身份证--借款人(正)分类名称 */
    String IDENTITY_BORROWER_FACE = "借款人(正)";

    /** 身份证--借款人(反)分类名称 */
    String IDENTITY_BORROWER_BACK = "借款人(反)";

    /** 身份证--其他抵押人(正)分类名称 */
    String IDENTITY_OTHER_FACE = "其他抵押人(正)";

    /** 身份证--其他抵押人(反)分类名称 */
    String IDENTITY_OTHER_BACK = "其他抵押人(反)";

    /** 身份证--借款人配偶(正)分类名称 */
    String IDENTITY_SPOUSE_FACE = "借款人配偶(正)";

    /** 身份证--借款人配偶(反)分类名称 */
    String IDENTITY_SPOUSE_BACK = "借款人配偶(反)";

    /** 房产证分类名称 */
    String HOUSEREGISTRATION = "房产证";

    /** 户口簿--借款人户口簿分类名称 */
    String HOUSEHOLDREGISTER_BORROWER = "借款人户口簿";

    /** 户口簿--借款人配偶户口簿分类名称 */
    String HOUSEHOLDREGISTER_SPOUSE = "借款人配偶户口簿";

    /** 户口簿--其他抵押人户口簿分类名称 */
    String HOUSEHOLDREGISTER_OTHER = "其他抵押人户口簿";

    /** 收入证明--借款人收入证明分类名称 */
    String PROOFOFEARNINGS_BORROWER = "借款人收入证明";

    /** 收入证明--配偶收入证明分类名称 */
    String PROOFOFEARNINGS_SPOUSE = "配偶收入证明";

    /** 婚姻证明（盖章页、内容页）--借款人婚姻证明分类名称 */
    String PROOFOFMARRIAGE_BORROWER = "借款人婚姻证明";

    /** 婚姻证明（盖章页、内容页）--其他抵押人婚姻证明分类名称 */
    String PROOFOFMARRIAGE_OTHER = "其他抵押人婚姻证明";

    /** 合同材料--放款凭证分类名称 */
    String VOUCHER = "放款凭证";

    /** 个人银行流水--借款人银行流水分类名称 */
    String BANK_STATEMENT_BORROWER = "借款人银行流水";

    /** 个人银行流水--配偶银行流水分类名称 */
    String BANK_STATEMENT_SPOUSE = "配偶银行流水";

    /** 个人资产证明--金融资产分类名称 */
    String FINANCIAL_ASSETS = "金融资产";

    /** 个人资产证明--房产信息（备用房）分类名称 */
    String SPARE_ROOM = "房产信息（备用房）";



    /** 缓存时间 */
    Integer REDIS_CLIENT_CASE_LOANPRODUCT_TIME = 3600;//TODO 要修改成 3600

    /** 身份证缓存key */
    String REDIS_CLIENT_IDENTITY = "category_info_identity";

    /** 户口簿缓存key */
    String REDIS_CLIENT_HOUSEHOLDREGISTER = "category_info_householdregister";

    /** 房产证缓存key */
    String REDIS_CLIENT_PROPERTYCERTIFICATE = "category_info_propertycertificate";

    /** 结婚证缓存key */
    String REDIS_CLIENT_MARRIAGECERTIFICATE = "category_info_marriagecertificate";

    /** 放款凭证缓存key */
    String REDIS_CLIENT_VOUCHER = "category_info_voucher";

    /** 收入证明缓存key */
    String REDIS_CLIENT_PROOFOFEARNINGS = "category_info_proofofearnings";

    /** 银行流水缓存key */
    String REDIS_CLIENT_BANK_STATEMENT = "category_info_bank_statement";

    /** 个人资产缓存key */
    String REDIS_CLIENT_FINANCIAL = "category_info_financial";

    /** 户口本重复显示数量 */
    Integer HOUSEHOLDREGISTER_AMOUNT = 3;

    /** 房产证重复显示数量 */
    Integer HOUSEREGISTRATION_AMOUNT = 4;

    /** 银行流水重复显示数量 */
    Integer STATEMENT_AMOUNT = 3;
}
