package com.fjs.cronus.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：有效客户表模块实体类
 *
 * @author yinzf
 * @version 1.0 2017-10-13
 */
public class CustomerUseful implements Serializable{

	private Integer id;		   	
	private Integer loanId;
	private Integer customerId;
	private String houseStatus;		   	
	private BigDecimal loanAmount;
	private Date usefulTime;
	private Integer createUser;
	private String purpose;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date lastUpdateTime;	 
	private Integer lastUpdateUser;
	private Integer isDeleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHouseStatus() {
		return houseStatus;
	}

	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Date getUsefulTime() {
		return usefulTime;
	}

	public void setUsefulTime(Date usefulTime) {
		this.usefulTime = usefulTime;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(Integer lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getLoanId() {
		return loanId;
	}

	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
}