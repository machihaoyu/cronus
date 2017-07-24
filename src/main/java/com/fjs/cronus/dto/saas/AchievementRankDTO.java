package com.fjs.cronus.dto.saas;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class AchievementRankDTO implements Serializable {

    private static final long serialVersionUID = 5568517059400483854L;

    private String ranking;  //9,
    private String user_id;  //14,
    private String user_name;  //丁,
    private String headPic;  //https;  ////www.baidu.com/img/baidu_jgylogo3.gif,
    private String achievementMoney;  //1520000

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getAchievementMoney() {
        return achievementMoney;
    }

    public void setAchievementMoney(String achievementMoney) {
        this.achievementMoney = achievementMoney;
    }
}
