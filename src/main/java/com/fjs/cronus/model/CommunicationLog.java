package com.fjs.cronus.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：沟通日志模块
 *
 * @author yinzf
 * @version 1.0 2017-09-19
 */
public class CommunicationLog implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ApiModelProperty(value = "交易id", required = false)
	private Integer loanId;
	@ApiModelProperty(value = "客户id", required = false)
	private Integer customerId;
	@ApiModelProperty(value = "创建人id", required = false)
	private Integer createUser;
	@ApiModelProperty(value = "类型", required = false)
	private Integer type;
	@ApiModelProperty(value = "内容", required = false)
	private String content;
	@ApiModelProperty(value = "创建时间", required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	@ApiModelProperty(value = "有无房产", required = false)
	private String houseStatus;
	@ApiModelProperty(value = "意向贷款金额", required = false)
	private BigDecimal loanAmount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getHouseStatus() {
		return houseStatus;
	}

	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
	}

	public Integer getLoanId() {
		return loanId;
	}

	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	@Override
	public String toString() {
		return "CommunicationLog{" +
				"id=" + id +
				", loanId=" + loanId +
				", customerId=" + customerId +
				", createUser=" + createUser +
				", type=" + type +
				", content='" + content + '\'' +
				", createTime=" + createTime +
				", houseStatus='" + houseStatus + '\'' +
				", loanAmount=" + loanAmount +
				'}';
	}
}