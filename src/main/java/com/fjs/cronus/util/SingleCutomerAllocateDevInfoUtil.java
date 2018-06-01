package com.fjs.cronus.util;


/**
 * 单个顾客分配时，收集相关信息.
 */
public class SingleCutomerAllocateDevInfoUtil {

    public static final ThreadLocal<SingleCutomerAllocateDevInfo> local = new ThreadLocal<>();

    private SingleCutomerAllocateDevInfoUtil() {
    }

    public static final String k1 = "进入老用户分支";
    public static final String k2 = "进入待分配池分支";
    public static final String k3 = "进入主动申请渠道分支";
    public static final String k4 = "无负责人，进入自动分配分支";
    public static final String k5 = "有负责人，创建交易";
    public static final String k6 = "进入非主动申请渠道分支";
    public static final String k7 = "三无客户";
    public static final String k8 = "发消息业务员，提醒跟进";
    public static final String k9 = "进入新用户分支";
    public static final String k10 = "[异常]自动分配失败";
    public static final String k11 = "获取自动分配的城市";
    public static final String k12 = "根据渠道获取媒体";
    public static final String k13 = "进入商机分支";
    public static final String k14 = "找到业务员";
    public static final String k15 = "有效城市范围内";
    public static final String k16 = "非有效城市范围内,进客服系统";
    public static final String k17 = "未找到业务员";
    public static final String k18 = "有指定业务员（外部传入）";
    public static final String k19 = "老客户去队列没找到业务员(手动报错记录信息)";
    public static final String k20 = "redis分布式锁超时";
    // ------------------ 商机找业务员规则 start ---------------------------------
    public static final String k21 = "根据城市、媒体查queue大小";
    public static final String k22 = "循环城市、媒体queue,";
    public static final String k23 = "获取某一级吧、某媒体、某月的已分配数,";
    public static final String k24 = "请求商机系统,";
    public static final String k25 = "获取关注的媒体,";
    public static final String k26 = "根据某一级吧、某媒体（特殊队列）、某月查queue大小,";
    public static final String k27 = "循环某一级吧、某媒体（特殊队列）、某月queue找业务员,";
    public static final String k28 = "根据一级吧、业务员、媒体（特殊队列）、月找对应分配数,";
    public static final String k29 = "根据一级吧、业务员、媒体（特殊队列）、月找总队列对应分配数,";
    public static final String k30 = "从queue找到业务员（特殊队列）,";
    public static final String k31 = "进入总分配队列分支，找业务员,";
    public static final String k32 = "获取城市、总队列、月queue大小,";
    public static final String k33 = "循环城市、总队列、月queue找业务员,";
    public static final String k34 = "根据城市、总队列、月queue中找的业务员,找分配数,";
    public static final String k35 = "进入在总队列中，判断特殊队列数据（有特殊队列，但未找到业务员）情况，";
    public static final String k36 = "进入在总队列中，判断特殊队列数据（有特殊队列，但未找到业务员）情况，找分配数，";
    public static final String k38 = "从queue找到业务员（总队列）,";
    public static final String k39 = "通过商机规则，找到业务员,";
    // ------------------ 商机找业务员规则 end ---------------------------------
    // ------------------ 老顾客，借用商机的总队列找业务员 start---------------------------------
    public static final String k40 = "老用户根据城市、媒体获取一级吧queue大小,";
    public static final String k41 = "老用户循环城市、媒体的一级吧queue找一级吧,";
    public static final String k42 = "老用户获取总队列queue大小,";
    public static final String k43 = "老用户循环queue找业务员,";
    public static final String k44 = "老用户循环queue,找到业务员,";
    // ------------------ 老顾客，借用商机的总队列找业务员 end---------------------------------

}
