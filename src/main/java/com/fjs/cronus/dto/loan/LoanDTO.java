package com.fjs.cronus.dto.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fjs.cronus.dto.cronus.CustomerDTO;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by msi on 2017/10/11.
 */
public class LoanDTO {
    private Integer id;
    //客户id
    private Integer customerId;
    private Integer status;
    //意向金额
    private BigDecimal mindAmount;
    //放款金额
    private BigDecimal LoanAmount;
    private String phone;
    private String customerName;
    private Integer ownUserId;
    private String ownUserName;
    private String level;
    private String houseStatus;
    private String city;
    private Integer remain;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    private Integer isLock;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date viewTime;
    private Integer viewUid;
    private Integer viewCount;
    private Integer autostatus;
    private String customerSource;
    private String utmSource;
    private String customerClassify;
    private String customerType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date communicateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date firstCommunicateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date firstAllocateTime;
    private String cooperationStatus;
    private Integer confirm;
    private Integer clickCommunicateButton;
    private Integer companyId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;
    //创建人id
    private Integer createUser;
    //更新人
    private Integer lastUpdateUser;
    //删除标识 0正常1删除
    private Integer isDeleted;
    private String ext;
    @Transient
    private String createTimeBegin;
    @Transient
    private String createTimeEnd;
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
    private CustomerDTO customerDTO;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getMindAmount() {
        return mindAmount;
    }

    public void setMindAmount(BigDecimal mindAmount) {
        this.mindAmount = mindAmount;
    }

    public BigDecimal getLoanAmount() {
        return LoanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        LoanAmount = loanAmount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
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

    public Integer getAutostatus() {
        return autostatus;
    }

    public void setAutostatus(Integer autostatus) {
        this.autostatus = autostatus;
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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
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

    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }
}
