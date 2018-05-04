package com.fjs.cronus.Common;

/**
 * Created by yinzf on 2017/9/14.
 */
public class CommonConst {

    //删除标识
    public static int DATA_NORMAIL = 0;
    public static int DATA__DELETE = 1;


    public static String ID_NULL = "id不能为空";
    public static String STATUS_NULL = "status不能为空";
    public static String UNVALID_PARA = "非法的参数";

    //客户等级
    public static String CUSTOMER_TYPE_MIND = "意向客户";


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


    //主要城市配置名
    public static String MAIN_CITY = "MainCity";

    //异地城市配置名
    public static String REMOTE_CITY = "RemoteCity";

    public static String OBJECT_NULL = "无对应的记录";


    public static String NO_AUTHORIZE = "没有权限";
    public static String NO_AUTHORIZE_COMMUNICATE = "没有权限,只有本人才能添加沟通";
    //权限列表
    public static String SYSTEMNAME = "thea";//"thea"
    public static String ADD_CUSTOMER_URL = SYSTEMNAME + "/customer/addCRMCustomer";
    public static String EDIT_CUSTOMER_URL = SYSTEMNAME + "/customer/editCustomerOk";

    public static String PRDCUSTOMERADD = SYSTEMNAME + "/prdCustomer/prdImport";
    public static String PUBLICCUSTOMER = SYSTEMNAME + "/customer/publicImport";


    public static String UPDATE_PRDCUSTOMER_URL = SYSTEMNAME + "/PrdCustomer/updatePrdCustomer";

    //系统配置 Start

    //重复客户不走自动分配的客户手机分类
    public static String CAN_NOT_ALLOCATE_CUSTOMER_CLASSIFY = "CanNotAllocateCustomerClassify";
    //客户在设定的时间段内重复申请不进入系统(单位秒)
    public static String R_CUSTOMER_CANNOT_INTO_SALE_TIME = "RCustomerCannotIntoSaleTime";
    public static String ALLOCATE_TO_NO_USER_POOL = "allocateToNoUserPool";

    public static String ACTIVE_APPLICATION_CHANNEL = "activeApplicationChannel";
    public static String CAN_ALLOCATE_CITY = "canAllocateCity";

    public static String DELETE_PRDCUSTOMER_URL = SYSTEMNAME + "/PrdCustomer/deletePrdCustomer";

    //原始盘转入交易

    //默认客户名
    public static String DEFAULT_CUSTOMER_NAME = "客户";


    //不参加分配的权限（房速贷）
    public static String CUSTOMER_SOURCE_FANGSUDAI = "fangsudai";
    public static String UTM_SOURCE_FANGXIN = "fangxin";

    //领取客户
    public static String PULL_LOAN_URL = SYSTEMNAME + "/PublicOffer/pullPan";


    //系统名
    public static String SYSTEM_NAME = "房金所系统";

    //系统名
    public static String SYSTEM_NAME_ENGLISH = "thea";//"thea"
    //系统ID
    public static Integer SYSTEM_ID = 75;
    //分配客户
    public static String ALLOCATE_LOAN_URL = SYSTEMNAME + "/AllocateController/allocateLoan";

    //离职员工批量转移
    public static String REMOVE_CUSTOMER = SYSTEMNAME + "/customer/removeCustomer";
    //自动清洗状态配置名
    public static String AUTO_CLEAN_STATUS = "autoCleanStatus";

    public static String NON_COMMUNICATE_AGAIN_ALLOCATE = "nonCommunicateAgainAllocate";


    public static String CANPUUMAXCOUNT = "MaxReceiveCustomer";
    //沟通日志类型
    public static int COMMUNICATION_LOG_TYPE0 = 0;
    public static int COMMUNICATION_LOG_TYPE1 = 1;
    public static int COMMUNICATION_LOG_TYPE2 = 2;

    public static Integer REMAIN_MAX_NUM = 50;

    //用户初始化分配数
    public static int BASE_CUSTOMER_NUM = 80;
    public static int REWARD_CUSTOMER_NUM = 0;
    public static Integer COMPANY_MEDIA_QUEUE_COUNT = -1; // 总分配队列，媒体id值是-1

    //特殊渠道
    public static String SPECIAL_UTM_SOURCE = "SpecialUtmSource";

    //原始盘状态
    public static Integer PULL_CUSTOMER_STASTUS_NORMAL = 0;//正常
    public static Integer PULL_CUSTOMER_STASTUS_UNVALID = -2;//无效
    public static Integer PULL_CUSTOMER_STASTUS_TRANSFER = 1;//转入crm成功

    public static String UPDATE_TRANSFER = "已转入的原始盘不能修改";

    //原始盘操作
    public static String UPDATE_PULL_CUSTOMER = "更改数据";
    public static String STATUS_PULL_CUSTOMER = "修改状态";
    public static String TRANSFER_PULL_CUSTOMER1 = "转入成功修改状态";
    public static String TRANSFER__PULL_CUSTOMER = "转入手机重复修改状态";

    //市场推广盘操作
    public static String IMPORT_PRD_CUSTOMER = "导入客户数据";
    public static String EDIT_PRD_CUSTOMER = "编辑客户";
    public static String DELETE_PRD_CUSTOMER = "=删除客户";
    public static String TRANSFER_PRD_CUSTOMER = "保存并转入";
    //市场推广盘操作结果
    public static String IMPORT_PRD_RESULT = "导入成功";
    public static String EDIT_PRD_RESULT = "保存客户，转入失败";
    public static String DELETE_PRD_RESULT = "=删除成功";
    public static String TRANSFER_PRD_RESULT = "转入成功";

    public static String SERVICE_IS_NULL = "服务合同id为空";
    public static String BIGER_MONEY = "金额数据错误";

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


    public static String OPERARIONAll = "自动分配";

    public static String OPERARIONNO = "未沟通分配";

    public static String OPERATIONBYHAND = "手动分配";
    public static String REMOVECUSTOMERALL = "removeCustomerAll";

    public static String ALLOCATEALL = "allocateAll";

    public static String OPERATION = "领取客户";

    public static String OPERATIONSUCESS = "领取成功";

    public static String HaiDai_ChangPhone = "http://192.168.1.90/interface/changeCustPhone.do";

    //未沟通重新分配
    public static String FAIL_NON_COMMUNICATE_ALLOCATE_INFO = "fail_non_communicate_allocate";

    public static String PHPLOGINKEY = "phploginkey";

    public static String KEEPPARAMS = "MaxCustomerNum";

    public static String NOAUTHCUSTOMERMEET = "没有权限,只有本人才能添加面见信息";

    //反馈老系统
    public static String PHPSYS_CONNECT_STATUS = "phpSysConnectStatus";

    //B端附件信息 缓存
    public static String OCRDOCUMENTKEY = "OCRDOCUMENTKEY";
    //缓存ke

    public static String CANMANGERMAINCITY = "canMangerMainCity";

    public static String CANMANGERREMOTECITY = "canMangerMainCity";


    public static String DESCCODEKEY = "desede";

    public static String DESCCODEDESCPRETION = "/CBC/PKCS5Padding";

    public static final String THEA_IMG_OSS_FILE_SEPARATOR = "/";

    public static final String THEA_PERFEX = "Uploads/";

    public static final String ROLE_NAME = "VIP渠道管理员";

    public static final String SPECIALUTM_NAME = "specialLookAllUtm";

//    public static final String NO_INTENTION = "尊敬的客户，感谢您选择房金所，如后续有需求可联系热线：400-810-2999；更多资讯请关注官方微信：房金所";
    public static final String NO_INTENTION = "尊敬的客户，感谢您选择房金所，了解更多融资服务可拨打热线：400-810-2999；更多资讯请关注官方微信：房金所";

//    public static final String POOR_QUALIFICATIONS = "尊敬的客户，因个人资质不符无法为您办理而深感抱歉，感谢您对房金所的信赖，更多融资机会可关注官方微信：房金所";
    public static final String POOR_QUALIFICATIONS = "尊敬的客户，因不符合产品要求而无法为您办理而深表歉意，感谢您对房金所的信赖，查看具体原因可关注官方微信：房金所";

    public static final String NOT_YET_CONNECTED = "尊敬的客户，您的申请已受理，因未能与您取得联系，如需资金，可联系专属顾问{名字+手机号}，更多资讯请关注官方微信：房金所";

//    public static final String INTENTION_TO_TRACKED = "尊敬的客户，我们正在为您初步对接融资渠道，查看最新结果请关注官方微信：房金所";
    public static final String INTENTION_TO_TRACKED = "尊敬的客户，我们正在为您初步对接融资渠道，第一时间获取最新结果请关注官方微信：房金所";

    public static final String NEW_CUSTOMER_MESSAGE = "尊敬的客户您好，如您发现房金所员工出现服务不周或侵犯您利益的行为，请拨打投诉热线：400-810-2999，我们承诺，有投必应！";

    public static final String IMPORTNOUSERPOOLUTMSOURCE = "importNoUserPoolutmSource";

    public static final String SUCCESS = "success";

    public static final String SMS_SIGN = "【房金云】";

    public static String COMPANY_EXECUTIVES = "公司高管";
}

