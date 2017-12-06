package com.fjs.cronus.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：沉淀池模块实体类，负责页面与后台数据传输功能
 *
 * @author yinzf
 * @version 1.0 2017-10-31
 */
public class PrdCustomer implements Serializable{
					   	
	private Integer id;

	/**
	 * 客户姓名
	 */
	private String customerName;

	/**
	 * 客户性别
	 */
	private String sex;

	/**
	 * 客户类型(目标客户/意向客户/潜在客户)
	 */
	private String customerType;

	/**
	 * 手机号码
	 */
	private String telephonenumber;

	/**
	 * 意向贷款金额
	 */
	private BigDecimal loanAmount;

	/**
	 * 所在城市
 	 */
	private String city;			   	
	private String houseStatus;	   	
	private String level;

	/**
	 * 客户来源
	 */
	private String customerSource;

	/**
	 *渠道
	 */
	private String utmSource;

	/**
	 * 被查看时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date viewTime;

	/**
	 * 查看人
	 */
	private Integer viewUid;

	/**
	 * 沟通时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date communitTime;

	/**
	 * 沟通内容
	 */
	private String communitContent;

	/**
	 * 客户状态(-1-删除 1-重复 0-正常 2-转入成功)
	 */
	private Integer status;

	/**
	 * 创建人
 	 */
	private Integer createUser;

	/**
	 * 上次更新人
 	 */
	private Integer lastUpdateUser;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date lastUpdateTime;


	/**
	 * 是否删除(1:已删除, 0:未删除)
	 */
	private Integer isDeleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getTelephonenumber() {
		return telephonenumber;
	}

	public void setTelephonenumber(String telephonenumber) {
		this.telephonenumber = telephonenumber;
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

	public String getHouseStatus() {
		return houseStatus;
	}

	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
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

	public Date getViewTime() {
		return viewTime;
	}

	public void setViewTime(Date viewTime) {
		this.viewTime = viewTime;
	}

	public Integer getViewUid() {
		return viewUid;
	}

	public void setViewUid(Integer viewUid) {
		this.viewUid = viewUid;
	}

	public Date getCommunitTime() {
		return communitTime;
	}

	public void setCommunitTime(Date communitTime) {
		this.communitTime = communitTime;
	}

	public String getCommunitContent() {
		return communitContent;
	}

	public void setCommunitContent(String communitContent) {
		this.communitContent = communitContent;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "PrdCustomer{" +
				"id=" + id +
				", customerName='" + customerName + '\'' +
				", sex='" + sex + '\'' +
				", customerType='" + customerType + '\'' +
				", telephonenumber='" + telephonenumber + '\'' +
				", loanAmount=" + loanAmount +
				", city='" + city + '\'' +
				", houseStatus='" + houseStatus + '\'' +
				", level='" + level + '\'' +
				", customerSource='" + customerSource + '\'' +
				", utmSource='" + utmSource + '\'' +
				", viewTime=" + viewTime +
				", viewUid=" + viewUid +
				", communitTime=" + communitTime +
				", communitContent='" + communitContent + '\'' +
				", status=" + status +
				", createUser=" + createUser +
				", lastUpdateUser=" + lastUpdateUser +
				", createTime=" + createTime +
				", lastUpdateTime=" + lastUpdateTime +
				", isDeleted='" + isDeleted + '\'' +
				'}';
	}
}