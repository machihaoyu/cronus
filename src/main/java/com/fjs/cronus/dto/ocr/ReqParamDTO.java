package com.fjs.cronus.dto.ocr;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by chenjie on 2017/8/17.
 */
public class ReqParamDTO implements Serializable{

    private static final long serialVersionUID = -4828778631655566175L;

    @ApiModelProperty(value = "客户ID", required = true)
    private Long customerId;

    @ApiModelProperty(value = "客户名称", required = true)
    private String customerName;

    @ApiModelProperty(value = "客户手机号码", required = true)
    private String customerTelephone;

    @ApiModelProperty(value = "业务员ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "业务员name", required = true)
    private String userName;

    @ApiModelProperty(value = "上传的附件ID", required = true)
    private Long attachmentId;

    @ApiModelProperty(value = "图片base64编码的字符串", required = true)
    private String imgBase64;

    @ApiModelProperty(value = "证件类型(1:身份证,2:户口薄,3:驾驶证,4:行驶证)", required = true)
    private String type;

    @ApiModelProperty(value = "如过证件类型为身份证时必传:反面-face;反面-back", required = false)
    private String side;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTelephone() {
        return customerTelephone;
    }

    public void setCustomerTelephone(String customerTelephone) {
        this.customerTelephone = customerTelephone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }
}
