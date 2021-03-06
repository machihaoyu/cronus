package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by msi on 2017/9/14.
 */
public class CustomerDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "手机号码")
    private String telephonenumber;
    @ApiModelProperty(value = "客户姓名")
    private String customerName;
    @ApiModelProperty(value = "客户等级")
    private String customerLevel;
    @ApiModelProperty(value = "备用联系方式")
    private String sparePhone;
    @ApiModelProperty(value = "年龄")
    private String age;
    @ApiModelProperty(value = "婚姻")
    private String marriage;
    @ApiModelProperty(value = "身份证号码")
    private String idCard;
    @ApiModelProperty(value = "户籍")
    private String provinceHuji;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "客户类型")
    private String customerType;
    @ApiModelProperty(value = "客户街道地址")
    private String customerStreet;

    @ApiModelProperty(value = "客户地址")
    private String customerAddress;

    @ApiModelProperty(value = "有无房产")
    private String houseStatus;
    @ApiModelProperty(value = "几套房")
    private String houseAmount;
    @ApiModelProperty(value = "房产类型")
    private String houseType;
    @ApiModelProperty(value = "房产估值")
    private String houseValue;
    @ApiModelProperty(value = "房产面积")
    private String houseArea;
    @ApiModelProperty(value = "房龄")
    private String houseAge;
    @ApiModelProperty(value = "是否按揭")
    private String houseLoan;
    @ApiModelProperty(value = "是否备用房")
    private String houseAlone;
    @ApiModelProperty(value = "房产地址")
    private String houseLocation;
    @ApiModelProperty(value = "所在城市")
    private String city;
    @ApiModelProperty(value = "体现客户的状态")
    private String customerClassify;
    @ApiModelProperty(value = "客户回访状态")
    private String callbackStatus;
    @ApiModelProperty(value = "客户回访时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date callbackTime;
    @ApiModelProperty(value = "分公司id")
    private Integer subCompanyId;
    @ApiModelProperty(value = "备注")
    private String perDescription;
    @ApiModelProperty(value = "可贷金额")
    private String houseLoanValue;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "上回更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastUpdateTime;
    @ApiModelProperty(value = "创建用户")
    private Integer createUser;
    @ApiModelProperty(value = "上回更新用户")
    private Integer lastUpdateUser;
    @ApiModelProperty(value = "是否删除(1:已删除, 0:未删除)'")
    private Integer isDeleted;

    //新版本添加字段
    @ApiModelProperty(value = "期望用款时间",notes = "期望用款时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date expectMoneyTime;

    @ApiModelProperty(value = "期望贷款期限",notes = "期望贷款期限")
    private String expectLoanTime;

    @ApiModelProperty(value = "期望还款方式 0其他 1等额本息 2先息后本 3 等额本金",notes = "期望还款方式 0其他 1等额本息 2先息后本 3 等额本金")
    private Integer expectRepaymentWay;

    @ApiModelProperty(value = "是否清房 0否 1是",notes = "是否清房 0否 1是")
    private Integer houseClear;

    @ApiModelProperty(value = "产权人",notes = "产权人")
    private String houseOwner;

    @ApiModelProperty(value = "按揭金额",notes = "按揭金额")
    private String mortgageAmount;

    @ApiModelProperty(value = "按揭月供",notes = "按揭月供")
    private String mortgaeMonth;

    @ApiModelProperty(value = "是否有车(0:无，1有)",notes = "是否有车(0:无，1有)")
    private Integer isHavaCar;

    @ApiModelProperty(value = "车辆价值",notes = "车辆价值")
    private String carWorth;

    @ApiModelProperty(value = "车龄",notes = "车龄")
    private Integer carAge;

    @ApiModelProperty(value = "是否有保险(0:无，1有)",notes = "是否有保险(0:无，1有)")
    private Integer isHavaInsurance;

    @ApiModelProperty(value = "年缴费金额",notes = "年缴费金额")
    private String yearPayAmount;

    @ApiModelProperty(value = "已缴费时长",notes = "已缴费时长")
    private Integer insuranceTime;

    @ApiModelProperty(value = "是否负债(0无 1有)",notes = "是否负债(0无 1有)")
    private Integer isHavaDebt;

    @ApiModelProperty(value = "负债金额",notes = "负债金额")
    private String debtMoney;

    @ApiModelProperty(value = "负债月供",notes = "负债月供")
    private Integer debtMonth;

    @ApiModelProperty(value = "当前逾期(0 无 1 有）",notes = "当前逾期(0 无 1 有）")
    private Integer debtOverdue;

    @ApiModelProperty(value = "逾期金额",notes = "逾期金额")
    private String debtOverdueMoney;

    @ApiModelProperty(value = "负债时长",notes = "负债时长")
    private Integer debtTime;

    @ApiModelProperty(value = "工作状态(0：其他，1：授薪，2：自供，3：退休)",notes = "工作状态(0：其他，1：授薪，2：自供，3：退休)")
    private Integer workStatus;

    @ApiModelProperty(value = "打卡工资",notes = "打卡工资")
    private String wagerCard;

    @ApiModelProperty(value = "入职时长",notes = "入职时长")
    private Integer entryTime;

    @ApiModelProperty(value = "公司性质(0:一般，1：优质)",notes = "公司性质(0:一般，1：优质)")
    private Integer companyType;

    @ApiModelProperty(value = "社保(1有，0无)",notes = "社保(1有，0无)")
    private Integer socialSecurity;

    @ApiModelProperty(value = "每月缴多少",notes = "每月缴多少")
    private String socialMoney;

    @ApiModelProperty(value = "持续时间（月）",notes = "持续时间（月）")
    private Integer socialTime;

    @ApiModelProperty(value = "公积金(1有，0无)",notes = "公积金(1有，0无)")
    private Integer providentFund;

    @ApiModelProperty(value = "每月缴多少",notes = "每月缴多少")
    private String providentMoney;

    @ApiModelProperty(value = "持续时间（月）",notes = "持续时间（月）")
    private Integer providentTime;

    @ApiModelProperty(value = "公司名称",notes = "公司名称")
    private String compnyName;

    @ApiModelProperty(value = "固定电话(0无，1有)",notes = "固定电话(0无，1有)")
    private Integer fixedPhone;

    @ApiModelProperty(value = "自雇信息",notes = "自雇信息")
    private List<EmplouInfo> employedInfo;

    @ApiModelProperty(value = "退休金",notes = "退休金")
    private String retirementWages;

    @ApiModelProperty(value = "渠道",notes = "渠道")
    private String utmSource;

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    @ApiModelProperty(value = "负责人id",notes = "负责人id")
    private Integer ownerUserId;

    @ApiModelProperty(value = "负责人姓名",notes = "负责人姓名")
    private String ownUserName;

    @ApiModelProperty(value = "客户来源",notes = "客户来源")
    private String customerSource;

    @ApiModelProperty(value = "金额",notes = "金额")
    private BigDecimal loanAmount;

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getOwnUserName() {
        return ownUserName;
    }

    public void setOwnUserName(String ownUserName) {
        this.ownUserName = ownUserName;
    }

    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    @ApiModelProperty(value = "海贷魔方 扩展字段",notes = "海贷魔方 扩展字段")
    private String ext;

    @ApiModelProperty(value = "领取时间",notes = "领取时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @ApiModelProperty(value = "是否保留  0不保留1保留2已签合同",notes = "是否保留  0不保留1保留2已签合同")
    private Integer remain;

    private Integer ocdcId;

    /*   @ApiModelProperty(value = "海贷魔方 扩展字段",notes = "海贷魔方 扩展字段")
    private String ext;

    @ApiModelProperty(value = "客户状态 意向客户 协议客户 成交客户",notes = "客户状态 意向客户 协议客户 成交客户")
    private String level;

    @ApiModelProperty(value = "是否保留  0不保留1保留2已签合同",notes = "是否保留  0不保留1保留2已签合同")
    private Integer remain;

    @ApiModelProperty(value = "被查看的最后时间",notes = "被查看的最后时间")
    private Date viewTime;

    @ApiModelProperty(value = "最后查看的人",notes = "最后查看的人")
    private Integer viewUid;

    @ApiModelProperty(value = "被查看的次数",notes = "被查看的次数")
    private Integer viewCount;

    @ApiModelProperty(value = "是否自动分配：1自动分配，0没有自动分配",notes = "是否自动分配：1自动分配，0没有自动分配")
    private Integer autostatus;

    @ApiModelProperty(value = "首次沟通时间",notes = "首次沟通时间")
    private Date firstCommunicateTime;

    @ApiModelProperty(value = "首次分配时间",notes = "首次分配时间")
    private Date firstAllocateTime;

    @ApiModelProperty(value = "合作状态(暂未接通 无意向 有意向待跟踪 资质差无法操作 空号 外地 同行 内部员工 其他)",notes = "合作状态(暂未接通 无意向 有意向待跟踪 资质差无法操作 空号 外地 同行 内部员工 其他)")
    private String  cooperationStatus;

    @ApiModelProperty(value = "确认状态(1-没有确认,2-有效客户,3-无效客户,4-老客户无需确认)",notes = "确认状态(1-没有确认,2-有效客户,3-无效客户,4-老客户无需确认)")
    private Integer  confirm;

    @ApiModelProperty(value = "有没有点击沟通按钮(1-点击了  0-没有)",notes = "有没有点击沟通按钮(1-点击了  0-没有)")
    private Integer clickCommunicateButton;

    @ApiModelProperty(value = "沟通时间",notes = "沟通时间")
    private Date communicateTime;*/


    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

  /*

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
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

    public Date getCommunicateTime() {
        return communicateTime;
    }

    public void setCommunicateTime(Date communicateTime) {
        this.communicateTime = communicateTime;
    }
*/
    public Date getExpectMoneyTime() {
        return expectMoneyTime;
    }

    public void setExpectMoneyTime(Date expectMoneyTime) {
        this.expectMoneyTime = expectMoneyTime;
    }

    public String getExpectLoanTime() {
        return expectLoanTime;
    }

    public void setExpectLoanTime(String expectLoanTime) {
        this.expectLoanTime = expectLoanTime;
    }

    public Integer getExpectRepaymentWay() {
        return expectRepaymentWay;
    }

    public void setExpectRepaymentWay(Integer expectRepaymentWay) {
        this.expectRepaymentWay = expectRepaymentWay;
    }

    public Integer getHouseClear() {
        return houseClear;
    }

    public void setHouseClear(Integer houseClear) {
        this.houseClear = houseClear;
    }

    public String getHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(String houseOwner) {
        this.houseOwner = houseOwner;
    }

    public String getMortgageAmount() {
        return mortgageAmount;
    }

    public void setMortgageAmount(String mortgageAmount) {
        this.mortgageAmount = mortgageAmount;
    }

    public String getMortgaeMonth() {
        return mortgaeMonth;
    }

    public void setMortgaeMonth(String mortgaeMonth) {
        this.mortgaeMonth = mortgaeMonth;
    }

    public Integer getIsHavaCar() {
        return isHavaCar;
    }

    public void setIsHavaCar(Integer isHavaCar) {
        this.isHavaCar = isHavaCar;
    }

    public String getCarWorth() {
        return carWorth;
    }

    public void setCarWorth(String carWorth) {
        this.carWorth = carWorth;
    }

    public Integer getCarAge() {
        return carAge;
    }

    public void setCarAge(Integer carAge) {
        this.carAge = carAge;
    }

    public Integer getIsHavaInsurance() {
        return isHavaInsurance;
    }

    public void setIsHavaInsurance(Integer isHavaInsurance) {
        this.isHavaInsurance = isHavaInsurance;
    }

    public String getYearPayAmount() {
        return yearPayAmount;
    }

    public void setYearPayAmount(String yearPayAmount) {
        this.yearPayAmount = yearPayAmount;
    }

    public Integer getInsuranceTime() {
        return insuranceTime;
    }

    public void setInsuranceTime(Integer insuranceTime) {
        this.insuranceTime = insuranceTime;
    }

    public Integer getIsHavaDebt() {
        return isHavaDebt;
    }

    public void setIsHavaDebt(Integer isHavaDebt) {
        this.isHavaDebt = isHavaDebt;
    }

    public String getDebtMoney() {
        return debtMoney;
    }

    public void setDebtMoney(String debtMoney) {
        this.debtMoney = debtMoney;
    }

    public Integer getDebtMonth() {
        return debtMonth;
    }

    public void setDebtMonth(Integer debtMonth) {
        this.debtMonth = debtMonth;
    }

    public Integer getDebtOverdue() {
        return debtOverdue;
    }

    public void setDebtOverdue(Integer debtOverdue) {
        this.debtOverdue = debtOverdue;
    }

    public String getDebtOverdueMoney() {
        return debtOverdueMoney;
    }

    public void setDebtOverdueMoney(String debtOverdueMoney) {
        this.debtOverdueMoney = debtOverdueMoney;
    }

    public Integer getDebtTime() {
        return debtTime;
    }

    public void setDebtTime(Integer debtTime) {
        this.debtTime = debtTime;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public String getWagerCard() {
        return wagerCard;
    }

    public void setWagerCard(String wagerCard) {
        this.wagerCard = wagerCard;
    }

    public Integer getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Integer entryTime) {
        this.entryTime = entryTime;
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public Integer getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(Integer socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public String getSocialMoney() {
        return socialMoney;
    }

    public void setSocialMoney(String socialMoney) {
        this.socialMoney = socialMoney;
    }

    public Integer getSocialTime() {
        return socialTime;
    }

    public void setSocialTime(Integer socialTime) {
        this.socialTime = socialTime;
    }

    public Integer getProvidentFund() {
        return providentFund;
    }

    public void setProvidentFund(Integer providentFund) {
        this.providentFund = providentFund;
    }

    public String getProvidentMoney() {
        return providentMoney;
    }

    public void setProvidentMoney(String providentMoney) {
        this.providentMoney = providentMoney;
    }

    public Integer getProvidentTime() {
        return providentTime;
    }

    public void setProvidentTime(Integer providentTime) {
        this.providentTime = providentTime;
    }

    public String getCompnyName() {
        return compnyName;
    }

    public void setCompnyName(String compnyName) {
        this.compnyName = compnyName;
    }

    public Integer getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(Integer fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getHouseLoanValue() {
        return houseLoanValue;
    }

    public void setHouseLoanValue(String houseLoanValue) {
        this.houseLoanValue = houseLoanValue;
    }

    public String getCustomerStreet() {
        return customerStreet;
    }

    public void setCustomerStreet(String customerStreet) {
        this.customerStreet = customerStreet;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getSparePhone() {
        return sparePhone;
    }

    public void setSparePhone(String sparePhone) {
        this.sparePhone = sparePhone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getProvinceHuji() {
        return provinceHuji;
    }

    public void setProvinceHuji(String provinceHuji) {
        this.provinceHuji = provinceHuji;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getHouseAmount() {
        return houseAmount;
    }

    public void setHouseAmount(String houseAmount) {
        this.houseAmount = houseAmount;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getHouseValue() {
        return houseValue;
    }

    public void setHouseValue(String houseValue) {
        this.houseValue = houseValue;
    }

    public String getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public String getHouseAge() {
        return houseAge;
    }

    public void setHouseAge(String houseAge) {
        this.houseAge = houseAge;
    }

    public String getHouseLoan() {
        return houseLoan;
    }

    public void setHouseLoan(String houseLoan) {
        this.houseLoan = houseLoan;
    }

    public String getHouseAlone() {
        return houseAlone;
    }

    public void setHouseAlone(String houseAlone) {
        this.houseAlone = houseAlone;
    }

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomerClassify() {
        return customerClassify;
    }

    public void setCustomerClassify(String customerClassify) {
        this.customerClassify = customerClassify;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(String callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public Date getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getPerDescription() {
        return perDescription;
    }

    public void setPerDescription(String perDescription) {
        this.perDescription = perDescription;
    }

    public List<EmplouInfo> getEmployedInfo() {
        return employedInfo;
    }

    public void setEmployedInfo(List<EmplouInfo> employedInfo) {
        this.employedInfo = employedInfo;
    }

    public String getRetirementWages() {
        return retirementWages;
    }

    public void setRetirementWages(String retirementWages) {
        this.retirementWages = retirementWages;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public Integer getOcdcId() {
        return ocdcId;
    }

    public void setOcdcId(Integer ocdcId) {
        this.ocdcId = ocdcId;
    }
}
