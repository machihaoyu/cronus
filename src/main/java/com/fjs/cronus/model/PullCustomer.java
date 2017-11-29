package com.fjs.cronus.model;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：海贷魔方原始盘模块实体类
 *
 * @author yinzf
 * @version 1.0 2017-10-24
 */
public class PullCustomer implements Serializable{
			   	
	private Integer id;	
	private Integer saleId;  	
	private String name;
	private String telephone;
	/**
	 * 被修改时的上1次旧手机号
	 */
	private String oldTelephone;
	/**
	 * 意向贷款金额(单位元)
	 */
	private BigDecimal loanAmount;
	/**
	 * 负责城市
	 */
	private String city;
	/**
	 * 客户来源
	 */
	private String customerSource;
	/**
	 *渠道
	 */
	private String utmSource;
	/**
	 * 转入crm状态: 0正常， -1重复，-2无效,  1转入 crm成功
	 */
	private Integer status;
	/**
	 * json结构化的综合数据(海贷魔方那边的扩展字段)
	 */
	private String extendText;
	private Integer createUser;
	private Integer lastUpdateUser;
	private Date createTime;
	private Date lastUpdateTime;
	private Integer isDeleted;

	@Transient
	private String createTimeBegin;
	@Transient
	private String createTimeEnd;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getOldTelephone() {
		return oldTelephone;
	}

	public void setOldTelephone(String oldTelephone) {
		this.oldTelephone = oldTelephone;
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

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
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

	public String getExtendText() {
		return extendText;
	}

	public void setExtendText(String extendText) {
		this.extendText = extendText;
	}

	public String getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(String createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
}