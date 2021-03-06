package com.fjs.cronus.Common;

/**
 * Created by yinzf on 2017/9/14.
 */
public class CommonRedisConst {

    //唯一配置名
    //OCDC推送配置
//    public static String OCDC_PUSH_LOCK = "thea_ocdcPushLock";
    public static String OCDC_PUSH_LOCK_1 = "1";//系统推送中，OCDC暂时不需要推送
    public static String OCDC_PUSH_LOCK_0 = "0";//系统推送数据处理额按成，可继续推送


    /**
     * 自动分配：业务员queue redis key.
     *
     * key结构：listAllocate$一级吧id$mediaid$yyyyMM
     */
    public static String ALLOCATE_LIST ="listAllocate";

    /**
     * 自动分配：octc推送时，redis锁.
     */
    public static String ALLOCATE_LOCK ="AllocateLock";

    /**
     * 自动分配：十五分钟未沟通.
     */
    public static String ALLOCATE_DELAY ="allocate_delay";

    /**
     * 自动分配：十五分钟未沟通lock.
     */
    public static String ALLOCATE_DELAY_LOCK ="allocate_delay_lock";

    /**
     * 自动分配：一级吧queue redis key.
     *
     * key结构：listAllocateSubCompany$mediaid$城市名
     */
    public static String ALLOCATE_SUBCOMPANYID ="listAllocateSubCompany";

    /**
     * 每月员工后台分配数据，锁key.
     */
    public static String USERMONTHINFO_EDIT = "userMonthInfoEdit";

    /**
     * 每月员工后台分配数据copy，锁key.
     */
    public static String USERMONTHINFO_COPY = "userMonthInfoCOPY";

    /**
     * 用户下属人员的ID集
     * 完整key：MySubUserId_1
     * 备注:需设置缓存更新时间
     */
    public static String SUB_USER_IDS = "MySubUserId_";
    public static Integer SUB_USER_IDS_TIMEOUT = 300;

    /**
     * 员工信息
     * 完整key：userInfo_1
     * 备注：各系统退出时，应该删除对应的Redis信息
     */
    public static String USER_INFO_REDIS_KEY = "UserInfo_";
    public static Integer USER_INFO_REDIS_TIMEOUT = 86400000;

    /**
     * UC角色信息
     * 完整key：roleInfo_1
     * 备注:需设置缓存更新时间
     */
    public static String ROLE_INFO = "roleInfo_";

    public static String LOCK_WAITING_POOL_ALLOCATE="lock_waiting_pool_allocate";

    /**
     * EZUC key.
     */
    public static  final String EZUC_DURATION_KEY = "ezuc_duration_key";

    /**
     * 业务员面见次数（key后有时间指定，具体到天） key.
     */
    public static String EZUC_DURATION_MEETINGCOUNT = "ezuc_duration_meetingcount";

    /**
     * EZUC 定时任务，同步锁（目前是一小时同步一次） key.
     */
    public static  final String EZUC_DURATION_QUARTZ_SYNC_LOCK = "ezuc:duration:quartz_sync:lock";

    /**
     * EZUC 同步数据 锁key.
     */
    public static  final String EZUC_DURATION_SYNC_LOCK = "ezuc:duration:use:sync:lock";

    /**
     * 通话时长：天.
     */
    public static final String SALES_MAN_CALL_TIME_DAY = "salesman:call:time:day";
    /**
     * 通话时长：周.
     */
    public static final String SALES_MAN_CALL_TIME_WEEK = "salesman:call:time:week";
    /**
     * 通话时长：月.
     */
    public static final String SALES_MAN_CALL_TIME_MONTH = "salesman:call:time:month";

    /**
     * 通话次数：天.
     */
    public static final String SALES_MAN_CALL_NUM_DAY = "salesman:call:num:day";
    /**
     * 通话次数：周.
     */
    public static final String SALES_MAN_CALL_NUM_WEEK = "salesman:call:num:week";
    /**
     * 通话次数：月.
     */
    public static final String SALES_MAN_CALL_NUM_MONTH = "salesman:call:num:month";

    /**
     * 面见次数：天.
     */
    public static final String SALES_MAN_MEET_NUM_DAY = "salesman:meet:num:day";
    /**
     * 通话次数：周.
     */
    public static final String SALES_MAN_MEET_NUM_WEEK = "salesman:meet:num:week";
    /**
     * 通话次数：月.
     */
    public static final String SALES_MAN_MEET_NUM_MONTH = "salesman:meet:num:month";

}
