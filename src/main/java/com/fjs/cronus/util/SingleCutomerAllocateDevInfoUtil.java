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
    public static final String k17 = "未找到业务员，进入商机池";
    public static final String k18 = "有指定业务员（外部传入）";
    public static final String k19 = "老客户去队列没找到业务员(手动报错记录信息)";
    public static final String k20 = "redis分布式锁超时";

    // ------------------ 商机找业务员规则 start ---------------------------------
    public static final String k21 = "找一级吧queue大小";
    public static final String k22 = "找一级吧,";
    public static final String k23 = "找一级吧已分配数,";
    public static final String k24 = "请求商机系统,";
    public static final String k25 = "获取关注的媒体,";
    public static final String k46 = "[特殊分配队列]进入特殊分配队列分支,";
    public static final String k26 = "[特殊分配队列]找业务员queue大小,";
    public static final String k27 = "[特殊分配队列]从queue找业务员,";
    public static final String k28 = "[特殊分配队列]找分配数,";
    public static final String k29 = "[特殊分配队列]找总队列分配数,";
    public static final String k30 = "[特殊分配队列]找到业务员,";
    public static final String k31 = "[总分配队列]进入总分配队列分支,";
    public static final String k32 = "[总分配队列]找业务员queue大小,";
    public static final String k33 = "[总分配队列]从queue找业务员,";
    public static final String k34 = "[总分配队列]找分配数,";
    public static final String k35 = "[总分配队列]进入判断特殊队列数据分支，";
    public static final String k36 = "[总分配队列]找分配数，";
    public static final String k38 = "[总分配队列]从queue找到业务员,";
    public static final String k39 = "通过商机规则，找到业务员,";
    // ------------------ 商机找业务员规则 end ---------------------------------

    // ------------------ 老顾客，借用商机的总队列找业务员 start ---------------------------------
    public static final String k40 = "老用户找一级吧queue大小,";
    public static final String k41 = "老用户从queue找一级吧,";
    public static final String k42 = "老用户找业务员总队列queue大小,";
    public static final String k43 = "老用户 未找业务员,";
    public static final String k44 = "老用户 找到业务员,";
    // ------------------ 老顾客，借用商机的总队列找业务员 end ---------------------------------

    public static final String k45 = "ocdcservice.addOcdcCustomer分配失败";
    public static final String k47 = "三无客户-指定时间内重复推送";
    public static final String k48 = "实购数=订购数，请求商机系统发送短信";

    // ------------------ 15分钟未沟通 start ---------------------------------
    public static final String k49 = "[15分钟未沟通]触发业务方法";
    public static final String k50 = "[15分钟未沟通]系统运行异常";
    public static final String k53 = "[15分钟未沟通]添加延迟处理业务";
    public static final String k54 = "[15分钟未沟通]调用autoAllocateService.autoAllocate分配方法";
    public static final String k55 = "[15分钟未沟通]取消再分配";
    // ------------------ 15分钟未沟通 end ---------------------------------

    public static final String k51 = "[短信]自动分配队列";
    public static final String k52 = "[短信]已存在负责人";
    public static final String k56 = "ocdc请求数据";
    public static final String k57 = "[短信]分配队列已满";
    public static final String k58 = "（商机老客户）老客户去队列没找到业务员,依然在商机池，不做处理";
    public static final String k59 = "老客户去队列没找到业务员，依然在公盘，不做处理";

}
