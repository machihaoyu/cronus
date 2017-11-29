package com.fjs.cronus.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：原始盘更新日志模块实体类
 *
 * @author yinzf
 * @version 1.0 2017-10-26
 */
public class PullCustomerUpdateLog implements Serializable{
				   	
	private Integer id;

	/**
	 * 操作名称
	 */
	private String operation;				   	
	private Integer pullCustId;

	/**
	 * 业务员id
	 */
	private Integer saleId;

	/**
	 * 客户名
	 */
	private String name;

	/**
	 * 客户电话
	 */
	private String telephone;

	/**
	 * 意向贷款金额(单位元)
	 */
	private BigDecimal loanAmount;
	private String city;
	private String customerSource;
	private String utmSource;

	/**
	 * 转入crm状态: 0正常， -1重复，-2无效,  1转入 crm成功
 	 */
	private Integer status;
	private String exendText;
	private Integer createUser;
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Integer getPullCustId() {
		return pullCustId;
	}

	public void setPullCustId(Integer pullCustId) {
		this.pullCustId = pullCustId;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCustomerSource() {
		return customerSource;
	}

	public void setCustomerSource(String customerSource) {
		this.customerSource = customerSource;
	}

	public String getUtmSource() {
		return utmSource;
	}

	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getExendText() {
		return exendText;
	}

	public void setExendText(String exendText) {
		this.exendText = exendText;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
}