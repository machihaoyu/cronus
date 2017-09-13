package com.fjs.cronus.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：交易表模块实体类
 *
 * @author yinzf
 * @version 1.0 2017-09-12
 */
public class Deal {

	private Long id;
	/**
	 * 金额
	 **/
	private String amount;
	/**
	 * 资金类型，1意向资金
	 **/
	private BigDecimal type;
	/**
	 * 客户id
	 **/
	private BigDecimal cusId;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;

	/**
	 * 创建人id
	 **/
	private BigDecimal createUser;

	/**
	 * 更新人
	 **/
	private BigDecimal lastUpdateUser;

	/**
	 * 删除标识 0正常1删除
	 **/
	private Boolean isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public BigDecimal getType() {
		return type;
	}

	public void setType(BigDecimal type) {
		this.type = type;
	}

	public BigDecimal getCusId() {
		return cusId;
	}

	public void setCusId(BigDecimal cusId) {
		this.cusId = cusId;
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

	public BigDecimal getCreateUser() {
		return createUser;
	}

	public void setCreateUser(BigDecimal createUser) {
		this.createUser = createUser;
	}

	public BigDecimal getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(BigDecimal lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Boolean getDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean deleted) {
		isDeleted = deleted;
	}
}