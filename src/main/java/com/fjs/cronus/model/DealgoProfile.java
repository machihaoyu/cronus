package com.fjs.cronus.model;

import java.util.Date;

/**
 * Created by Administrator on 2018/5/17.
 */
public class DealgoProfile {

    private String id;
    private String dealid;
    private String bid;
    private String sid;
    private String cid;
    private String uniqueId;
    private String name;
    private String value;
    private String evidence;
    private String briefEvidence;
    private String mediumEvidence;
    private int score;
    private String number;
    private Date date;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBid() {
        return bid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setBriefEvidence(String briefEvidence) {
        this.briefEvidence = briefEvidence;
    }

    public String getBriefEvidence() {
        return briefEvidence;
    }

    public void setMediumEvidence(String mediumEvidence) {
        this.mediumEvidence = mediumEvidence;
    }

    public String getMediumEvidence() {
        return mediumEvidence;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDealid() {
        return dealid;
    }

    public void setDealid(String dealid) {
        this.dealid = dealid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
