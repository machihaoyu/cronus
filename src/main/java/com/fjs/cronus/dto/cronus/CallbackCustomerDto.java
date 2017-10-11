package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by msi on 2017/10/11.
 */
public class CallbackCustomerDto {

    private Integer id;

    private String telephonenumber;

    private String customerName;

    private String city;

    private String customerLevel;

    private String callbackStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date callbackTime;

    private Integer subCompanyId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
