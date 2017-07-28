package com.fjs.cronus.dto.saas;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class AchievementRankParamDTO implements Serializable {

    private static final long serialVersionUID = 2642107505155983673L;

    //业绩排行榜（净佣）
    public static final String QUERY_TYPE_ACHIEVE = "achieve";
    //有效客户协议率（有效分配客户到协议客户转化率）
    public static final String QUERY_TYPE_VALIDCUS_AGREEMRATE = "ValidCus_AgreemRate";


    private String userId;
    private String type; //":"1",
    private String page; //":"1",
    private String perpage; //":"1"
    private String queryType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
