package com.fjs.cronus.dto.Echo;

import java.io.Serializable;

/**
 * Created by msi on 2018/1/17.
 */
public class StationMsgReqDTO implements Serializable {

    private String msgClassify;

    private String msgContent;

    private String msgTitle;

    private String source;

    private String userPhone;

    public String getMsgClassify() {
        return msgClassify;
    }

    public void setMsgClassify(String msgClassify) {
        this.msgClassify = msgClassify;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Override
    public String toString(){

        return "StationMsgReqDTO ={" +
                " msgClassify = " + msgClassify +
                " msgContent = " + msgClassify +
                " msgTitle" + msgTitle +
                " source =" +
                source +
                " userPhone = "
                + userPhone +
                "}";
    }
}
