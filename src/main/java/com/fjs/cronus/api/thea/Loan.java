package com.fjs.cronus.api.thea;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 描述：交易表模块实体类
 *
 * @author yinzf
 * @version 1.0 2017-09-12
 */
public class Loan implements Serializable{
	@ApiModelProperty(value = "id", required = false)
	private Integer id;
	@ApiModelProperty(value = "客户id", required = false)
	private Integer customerId;
	@ApiModelProperty(value = "交易状态", required = false)
	private Integer status;
	@ApiModelProperty(value = "放款金额", required = false)
	private BigDecimal loanAmount;
	@ApiModelProperty(value = "电话", required = false)
	private String telephonenumber;
	@ApiModelProperty(value = "客户姓名", required = false)
	private String customerName;
	@ApiModelProperty(value = "拥有人id", required = false)
	private Integer ownUserId;
	@ApiModelProperty(value = "拥有人姓名", required = false)
	private String ownUserName;
	@ApiModelProperty(value = "有无房产", required = false)
	private String houseStatus;
	@ApiModelProperty(value = "城市", required = false)
	private String city;
	@ApiModelProperty(value = "是否保留", required = false)
	private Integer remain;
	@ApiModelProperty(value = "领取时间", required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date receiveTime;
	@ApiModelProperty(value = "是否锁定", required = false)
	private Integer isLock;
	@ApiModelProperty(value = "被查看的最后时间", required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date viewTime;
	@ApiModelProperty(value = "最后查看的人", required = false)
	private Integer viewUid;
	@ApiModelProperty(value = "被查看的次数", required = false)
	private Integer viewCount;
	@ApiModelProperty(value = "是否自动分配", required = false)
	private Integer autostatus;
	@ApiModelProperty(value = "客户来源（必填）", required = false)
	private String customerSource;
	@ApiModelProperty(value = "渠道来源（必填）", required = false)
	private String utmSource;
	@ApiModelProperty(value = "手机分类（必填）", required = false)
	private String customerClassify;
	@ApiModelProperty(value = "沟通时间", required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date communicateTime;
	@ApiModelProperty(value = "首次沟通时间", required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date firstCommunicateTime;
	@ApiModelProperty(value = "首次分配时间", required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date firstAllocateTime;
	@ApiModelProperty(value = "合作状态", required = false)
	private String cooperationStatus;
	@ApiModelProperty(value = "确认状态", required = false)
	private Integer confirm;
	@ApiModelProperty(value = "有没有点击沟通按钮", required = false)
	private Integer clickCommunicateButton;
	@ApiModelProperty(value = "公司id", required = false)
	private Integer companyId;
	@ApiModelProperty(value = "创建时间", required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date lastUpdateTime;
	//创建人id
	private Integer createUser;
	//更新人
	private Integer lastUpdateUser;
	//删除标识 0正常1删除
	private Integer isDeleted;
	private String ext;
	private String code;
	private String customerType;
	private String level;

	@Transient
	private String createTimeStr;
	@Transient
	private String sort;
	@Transient
	private String order;
	@Transient
	private BigDecimal totalCommission;//总佣金
	@Transient
	private BigDecimal totalOutCome;//总支出
	@Transient
	private BigDecimal totalAchievement;//总业绩
	@Transient
	private String communicationOrder;
	@Transient
	private Integer ocdcDataId;
	@Transient
	private List<Integer> customerIds;
	@Transient
	private List<String> utmList;

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getHouseStatus() {
		return houseStatus;
	}

	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
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

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
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

	public String getCustomerClassify() {
		return customerClassify;
	}

	public void setCustomerClassify(String customerClassify) {
		this.customerClassify = customerClassify;
	}

	public Date getFirstAllocateTime() {
		return firstAllocateTime;
	}

	public void setFirstAllocateTime(Date firstAllocateTime) {
		this.firstAllocateTime = firstAllocateTime;
	}

	public String getCooperationStatus() {
		return cooperationStatus;
	}

	public void setCooperationStatus(String cooperationStatus) {
		this.cooperationStatus = cooperationStatus;
	}

	public Integer getConfirm() {
		return confirm;
	}

	public void setConfirm(Integer confirm) {
		this.confirm = confirm;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getCommunicateTime() {
		return communicateTime;
	}

	public void setCommunicateTime(Date communicateTime) {
		this.communicateTime = communicateTime;
	}

	public Date getFirstCommunicateTime() {
		return firstCommunicateTime;
	}

	public void setFirstCommunicateTime(Date firstCommunicateTime) {
		this.firstCommunicateTime = firstCommunicateTime;
	}

	public Integer getClickCommunicateButton() {
		return clickCommunicateButton;
	}

	public void setClickCommunicateButton(Integer clickCommunicateButton) {
		this.clickCommunicateButton = clickCommunicateButton;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getOwnUserId() {
		return ownUserId;
	}

	public void setOwnUserId(Integer ownUserId) {
		this.ownUserId = ownUserId;
	}

	public String getOwnUserName() {
		return ownUserName;
	}

	public void setOwnUserName(String ownUserName) {
		this.ownUserName = ownUserName;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAutostatus() {
		return autostatus;
	}

	public void setAutostatus(Integer autostatus) {
		this.autostatus = autostatus;
	}

	public Integer getRemain() {
		return remain;
	}

	public void setRemain(Integer remain) {
		this.remain = remain;
	}

	public Integer getIsLock() {
		return isLock;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public BigDecimal getTotalCommission() {
		return totalCommission;
	}

	public void setTotalCommission(BigDecimal totalCommission) {
		this.totalCommission = totalCommission;
	}

	public BigDecimal getTotalOutCome() {
		return totalOutCome;
	}

	public void setTotalOutCome(BigDecimal totalOutCome) {
		this.totalOutCome = totalOutCome;
	}

	public BigDecimal getTotalAchievement() {
		return totalAchievement;
	}

	public void setTotalAchievement(BigDecimal totalAchievement) {
		this.totalAchievement = totalAchievement;
	}

	public String getCommunicationOrder() {
		return communicationOrder;
	}

	public void setCommunicationOrder(String communicationOrder) {
		this.communicationOrder = communicationOrder;
	}

	public List<Integer> getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(List<Integer> customerIds) {
		this.customerIds = customerIds;
	}

	public Integer getOcdcDataId() {
		return ocdcDataId;
	}

	public void setOcdcDataId(Integer ocdcDataId) {
		this.ocdcDataId = ocdcDataId;
	}

	public String getTelephonenumber() {
		return telephonenumber;
	}

	public void setTelephonenumber(String telephonenumber) {
		this.telephonenumber = telephonenumber;
	}

	public List<String> getUtmList() {
		return utmList;
	}

	public void setUtmList(List<String> utmList) {
		this.utmList = utmList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}