package com.fjs.cronus.dto.saas;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/14 0014.
 */
public class MineAchievementDTO implements Serializable {

    private static final long serialVersionUID = -3117472887450759083L;

    private String myAchievement; //":110, 我的业绩
    private String myCommission;  //":220, 我的提成
    private String myPay;         //":330  我的支出

    public String getMyAchievement() {
        return myAchievement;
    }

    public void setMyAchievement(String myAchievement) {
        this.myAchievement = myAchievement;
    }

    public String getMyCommission() {
        return myCommission;
    }

    public void setMyCommission(String myCommission) {
        this.myCommission = myCommission;
    }

    public String getMyPay() {
        return myPay;
    }

    public void setMyPay(String myPay) {
        this.myPay = myPay;
    }
}
