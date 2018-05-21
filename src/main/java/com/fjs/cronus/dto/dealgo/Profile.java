package com.fjs.cronus.dto.dealgo;

/**
 * Created by Administrator on 2018/5/16.
 */
public class Profile {

    private String id;
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
}
