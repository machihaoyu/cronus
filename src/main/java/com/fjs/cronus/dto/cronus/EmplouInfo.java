package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2017/11/28.
 */
public class EmplouInfo implements Serializable{

    @ApiModelProperty(value = "企业名称",notes = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "成立年限",notes = "成立年限")
    private String years;

    @ApiModelProperty(value = "年流水",notes = "年流水")
    private String turnover;

    @ApiModelProperty(value = "注册资本",notes = "注册资本")
    private String registerMoney;

    @ApiModelProperty(value = "认缴资本",notes = "认缴资本")
    private String subscribedMoney;

    @ApiModelProperty(value = "角色1法人 2股东 3高管",notes = "角色1法人 2股东 3高管")
    private Integer roles;

    @ApiModelProperty(value = "角色2股东占股多少",notes = "角色2股东占股多少")
    private String shares;
    @ApiModelProperty(value = "角色3高管 职位",notes = "角色3高管 职位")
    private String position;

    @ApiModelProperty(value = "经营状态：1存续，2在业，3吊销，4注销，5迁出，6迁入，7停业，8清算",notes = "经营状态：1存续，2在业，3吊销，4注销，5迁出，6迁入，7停业，8清算")
    private Integer status;


}
