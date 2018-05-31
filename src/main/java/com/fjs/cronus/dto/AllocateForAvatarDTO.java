package com.fjs.cronus.dto;

public class AllocateForAvatarDTO {

    /**
     * 一级吧id.
     */
    private Integer companyid;

    /**
     * 业务员id.
     */
    private Integer salesmanId;

    /**
     * 媒体id.
     */
    private Integer mediaid;

    /**
     * 从哪个媒体队列分配的.
     */
    private Integer frommediaid;

    /**
     * 商机情况：分配是否成功.
     */
    private boolean successOfAvatar;

    /**
     * 老顾客情况：是否分配成功.
     */
    private boolean successOfOldcustomer;

    public Integer getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(Integer salesmanId) {
        this.salesmanId = salesmanId;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public Integer getMediaid() {
        return mediaid;
    }

    public void setMediaid(Integer mediaid) {
        this.mediaid = mediaid;
    }

    public Integer getFrommediaid() {
        return frommediaid;
    }

    public void setFrommediaid(Integer frommediaid) {
        this.frommediaid = frommediaid;
    }

    public boolean getSuccessOfAvatar() {
        return successOfAvatar;
    }

    public void setSuccessOfAvatar(boolean successOfAvatar) {
        this.successOfAvatar = successOfAvatar;
    }

    public boolean getSuccessOfOldcustomer() {
        return successOfOldcustomer;
    }

    public void setSuccessOfOldcustomer(boolean successOfOldcustomer) {
        this.successOfOldcustomer = successOfOldcustomer;
    }
}
