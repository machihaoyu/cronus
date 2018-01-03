package com.fjs.cronus.Common;

/**
 * Created by yinzf on 2017/9/14.
 */
public class CommonConst {
    //员工查看权限
    public static int USER_TYPE_SELF = 1;//只能查看自己
    public static int USER_TYPE_TEAM = 2;//团队长及下属
    public static int USER_TYPE_SUB = 3;//分公司
    public static int USER_TYPE_ALL = 4;//查看所有

    //删除标识
    public static int DATA_NORMAIL = 0;
    public static int DATA__DELETE = 1;

    //渠道利率状态
    public static int UTM_SOURCE_RATE_OK = 1;
    public static int UTM_SOURCE_RATE_CLOSE = 0;

    public static String ID_NULL = "id不能为空";
    public static String STATUS_NULL = "status不能为空";
    public static String UNVALID_PARA = "非法的参数";

    //业绩状态
    public static int ACHIEVEMENT_STASUS_NOCONFIRM = 0;//未确认
    public static int ACHIEVEMENT_STASUS_YESCONFIRM = 1;//已确认
    public static int ACHIEVEMENT_STASUS_ABONDON = 2;//废弃的
    public static int ACHIEVEMENT_STASUS_NOEFFICTIVE = 3;//未生效的

    //客户等级
    public static String CUSTOMER_TYPE_MIND = "意向客户";
    public static String CUSTOMER_TYPE_CONTRACT = "合同客户";
    public static String CUSTOMER_TYPE_TURNOVER = "成交客户";

    //业绩确认
    public static int CONFIRM_SUCC = 0;
    public static String CONFIRM_MESSAGE_SUCC = "确认成功";

    public static int CONFIRM_FAIL = 1;
    public static String CONFIRM_MESSAGE_FAIL = "确认失败";

    //交易状态
    public static int LOAN_STATUS_APPLY = 1;//已申请
    public static int LOAN_STATUS_MEET = 2;//已面见
    public static int LOAN_STATUS_SERVICE = 3;//已签协议
    public static int LOAN_STATUS_LOAN = 4;//已放款
    public static int LOAN_STATUS_WAIT = 5;//结案申请
    public static int LOAN_STATUS_END = 6;//结案完结
    public static int LOAN_STATUS_FAIL = 7;//已废弃

    public static int CONFIRM__STATUS_NO = 1;//未确认
    public static int CONFIRM__STATUS_EFFECT = 2;//有效客户
    public static int CONFIRM__STATUS_NOEFFECT = 3;//无效客户
    public static int CONFIRM__STATUS_OLD = 4;//老客户

    //客户保留状态
    public static int REMAIN_STATUS_NO = 0;//不保留
    public static int REMAIN_STATUS_YES = 1;//保留
    public static int REMAIN_STATUS_CONTRACT = 2;//已签合同

    //是否面见
    public static int IS_MEET_NO = 0;
    public static int IS_MEET__YES = 1;//面见

    // OCDC推送数据字段名
    public static String[] CUSTOMER_SALE_PUSH_LOG = {"id", "customer_id", "telephonenumber", "customer_name",
            "owner_user_id", "owner_user_name", "creater_user_id", "customer_level", "loan_amount", "spare_phone", "age",
            "marriage", "id_card", "province_huji", "sex", "customer_address", "per_description", "house_amount",
            "house_type", "house_value", "house_area", "house_age", "house_loan", "house_alone", "house_location",
            "city", "retain", "create_time", "update_time", "receive_time", "is_lock", "phone_view_time", "phone_view_uid",
            "phone_view_count", "autostatus", "utm_source", "customer_source", "customer_classify", "laiyuan", "ext",
            "repeat_callback_time"};

    //默认客户名
    //客户来源
    public static Integer LAI_YUAN_OCDC = 0;


    //主要城市配置名
    public static String MAIN_CITY = "MainCity";

    //异地城市配置名
    public static String REMOTE_CITY ="RemoteCity";

    public static String OBJECT_NULL ="无对应的记录";



    public static String NO_AUTHORIZE = "没有权限";
    public static String NO_AUTHORIZE_COMMUNICATE = "没有权限,只有本人才能添加沟通";
    //权限列表
    public static String SYSTEMNAME="sale";
    public static String ADD_COMM_LOG_URL=SYSTEMNAME+"communicationLog/add";
    public static String ADD_CUSTOMER_URL=SYSTEMNAME+"/Customer/add";
    public static String EDIT_CUSTOMER_URL = SYSTEMNAME+"/Customer/edit";

    public static String PRDCUSTOMERADD = SYSTEMNAME +"/Prd/import";
    public static String PUBLICCUSTOMER = SYSTEMNAME +"/Prd/import";
    public static String REMOVECUSTOMER = SYSTEMNAME +"/Customer/removeCustomerAll";

    public static String ADD_RECE_URL=SYSTEMNAME+"rece/add";
    public static String ADD_ACHIVEMENT_URL=SYSTEMNAME+"achievement/add";
    public static String ADD_KEY_URL=SYSTEMNAME+"keyManager/add";
    public static String ADD_API_URL=SYSTEMNAME+"apiData/add";
    public static String ADD_UTMSOURCERATE_URL=SYSTEMNAME+"utmSourceRate/add";
    public static String ADD_CONFIG_URL=SYSTEMNAME+"config/add";

    public static String UPDATE_LOAN_URL=SYSTEMNAME+"/Customer/update";
    public static String UPDATE_KEY_URL=SYSTEMNAME+"keyManager/update";
    public static String UPDATE_UTMSOURCERATE_URL=SYSTEMNAME+"utmSourceRate/update";
    public static String UPDATE_CONFIG_URL=SYSTEMNAME+"config/update";
    public static String UPDATE_PULL_CUSTOMER_URL=SYSTEMNAME+"/pullCustomer/update";
    public static String UPDATE_PRDCUSTOMER_URL=SYSTEMNAME+"/prdCustomer/update";

    //系统配置 Start

    //重复客户不走自动分配的客户手机分类
    public static String CAN_NOT_ALLOCATE_CUSTOMER_CLASSIFY = "CanNotAllocateCustomerClassify";
    //客户在设定的时间段内重复申请不进入系统(单位秒)
    public static String R_CUSTOMER_CANNOT_INTO_SALE_TIME = "RCustomerCannotIntoSaleTime";
    public static String ALLOCATE_TO_NO_USER_POOL = "allocateToNoUserPool";

    public static String ACTIVE_APPLICATION_CHANNEL = "activeApplicationChannel";
    public static String CAN_ALLOCATE_CITY = "canAllocateCity";

    //系统配置end
    //业绩确认
    public static String CONFIRM_ACHIVEMENT_URL=SYSTEMNAME+"achievement/confirm";

    public static String CLOSE_UTMSOURCERATE_URL=SYSTEMNAME+"utmSourceRate/close";
    public static String OPEN_UTMSOURCERATE_URL=SYSTEMNAME+"utmSourceRate/open";

    public static String DELETE_UTMSOURCERATE_URL=SYSTEMNAME+"utmSourceRate/delete";
    public static String DELETE_PRDCUSTOMER_URL=SYSTEMNAME+"/prdCustomer/delete";

    //原始盘转入交易
    public static String TRANSFER_PULL_CUSTOMER_URL=SYSTEMNAME+"/pullCustomer/transfer";
    //默认客户名
    public static String DEFAULT_CUSTOMER_NAME = "客户";


    //不参加分配的权限（房速贷）
    public static String CUSTOMER_SOURCE_FANGSUDAI = "fangsudai";
    public static String UTM_SOURCE_FANGXIN = "fangxin";

    //领取客户
    public static String PULL_LOAN_URL=SYSTEMNAME+"loan/pull";

    //订转佣
    public static String TRANSFER_LOAN_URL=SYSTEMNAME+"loan/selectByLoanIds";

    //系统名
    public static String SYSTEM_NAME = "房金所系统";

    //系统名
    public static String SYSTEM_NAME_ENGLISH = "sale";
    //系统ID
    public static Integer SYSTEM_ID = 75;
    //扔回客户
    public static String REMOVE_LOAN_URL=SYSTEMNAME+"loan/remove";
    //分配客户
    public static String ALLOCATE_LOAN_URL=SYSTEMNAME+"/Customer/allocateAll";
    //保留客户
    public static String KEEP_LOAN_URL=SYSTEMNAME+"loan/keep";
    //取消保留客户
    public static String CANCEL_LOAN_URL=SYSTEMNAME+"loan/cancel";
    //离职员工批量转移
    public static String REMOVE_CUSTOMER = SYSTEMNAME+"/Customer/removeCustomerAll";
    //自动清洗状态配置名
    public static String AUTO_CLEAN_STATUS = "autoCleanStatus";
    //需要屏蔽自动清洗的分公司
    public static String CAN_NOT_CLEAN_CUSTOMER_COMPANY = "CanNotCleanCustomerCompany";
    //需要屏蔽自动清洗的用户ID
    public static String CAN_NOT_CLEAN_CUSTOMER_USER_ID = "CanNotCleanCustomerUserId";

    public static String CANPUUMAXCOUNT = "MaxReceiveCustomer";
    //沟通日志类型
    public static int COMMUNICATION_LOG_TYPE0 = 0;
    public static int COMMUNICATION_LOG_TYPE1 = 1;
    public static int COMMUNICATION_LOG_TYPE2 = 2;

    public static Integer REMAIN_MAX_NUM = 50;

    //用户初始化分配数
    public static int BASE_CUSTOMER_NUM = 80;
    public static int REWARD_CUSTOMER_NUM = 0;

    //特殊渠道
    public static String SPECIAL_UTM_SOURCE = "SpecialUtmSource";

    //原始盘状态
    public static Integer PULL_CUSTOMER_STASTUS_NORMAL=0;//正常
    public static Integer PULL_CUSTOMER_STASTUS_UNVALID=-2;//无效
    public static Integer PULL_CUSTOMER_STASTUS_TRANSFER=1;//转入crm成功

    public static String UPDATE_TRANSFER="已转入的原始盘不能修改";

    //原始盘操作
    public static String UPDATE_PULL_CUSTOMER="更改数据";
    public static String STATUS_PULL_CUSTOMER="修改状态";
    public static String TRANSFER_PULL_CUSTOMER1="转入成功修改状态";
    public static String TRANSFER__PULL_CUSTOMER="转入手机重复修改状态";

    //市场推广盘操作
    public static String IMPORT_PRD_CUSTOMER="导入客户数据";
    public static String EDIT_PRD_CUSTOMER="编辑客户";
    public static String DELETE_PRD_CUSTOMER="=删除客户";
    public static String TRANSFER_PRD_CUSTOMER="保存并转入";
    //市场推广盘操作结果
    public static String IMPORT_PRD_RESULT="导入成功";
    public static String EDIT_PRD_RESULT="保存客户，转入失败";
    public static String DELETE_PRD_RESULT="=删除成功";
    public static String TRANSFER_PRD_RESULT="转入成功";

    public static String SERVICE_IS_NULL="服务合同id为空";
    public static String BIGER_MONEY="金额数据错误";

    //市场推广盘状态
    public static int PRD_NORMAIL = 0;//正常
    public static int PRD__REPEAT = 1;//重复
    public static int PRD_DELETE = -1;//删除
    public static int PRD__TRANSFER = 2;//转入成功

    public static String Communication_LOG_ID_NULL = "客户id为空";
    public static String CONTENT_NULL = "沟通内容为空";
    public static String COMMENT_NULL = "评论内容为空";
    public static String CUSTOMER_NO_EXIST = "客户不存在";
    public static String COOPERATION_STATUS_NULL = "跟进状态为空";
    public static String AUTH_MESSAGE = "只有该业务员的团队长才能评论";

    public static String SUB_COMPANY_ID_NULL = "分公司id为空";

    public static String SUB_COMPANY_EXIST = "分公司已屏蔽";
    public static String EXIST = "已存在";

    public static String USE_TIME_NULL = "期望用款时间为空";
    public static String DEADLINE_NULL = "期望贷款期限为空";
    public static String REPAYMENT_TYPE_NULL = "期望还款方式为空";


    public static String  OPERARIONAll = "自动分配";

    public static String OPERARIONNO = "未沟通分配";

    public static String OPERATIONBYHAND = "手动分配";
    public static String REMOVECUSTOMERALL ="removeCustomerAll";

    public static String ALLOCATEALL ="allocateAll";

    public static String OPERATION = "领取客户";

    public static String OPERATIONSUCESS = "领取成功";

    public static String HaiDai_ChangPhone ="http://192.168.1.90/interface/changeCustPhone.do";

    //未沟通重新分配
    public static String FAIL_NON_COMMUNICATE_ALLOCATE_INFO = "fail_non_communicate_allocate";

    public static String PHPLOGINKEY = "phploginkey";

    public static String KEEPPARAMS = "MaxCustomerNum";

    public static String NOAUTHCUSTOMERMEET ="没有权限,只有本人才能添加面见信息";

    //反馈老系统
    public static String PHPSYS_CONNECT_STATUS = "phpSysConnectStatus";

    //B端附件信息 缓存
    public static String OCRDOCUMENTKEY = "OCRDOCUMENTKEY";
    //缓存ke

    public static String CANMANGERMAINCITY = "canMangerMainCity";

    public static String CANMANGERREMOTECITY = "canMangerMainCity";

}

