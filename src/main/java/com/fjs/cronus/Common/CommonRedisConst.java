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
     * 交易自动分配Redis配置信息
     * 完整 "listAllocate上海"
     * 永久储存
     */
    public static String ALLOCATE_LIST ="listAllocate";

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

}
