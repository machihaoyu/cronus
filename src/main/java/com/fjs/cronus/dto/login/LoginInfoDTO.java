package com.fjs.cronus.dto.login;

import java.util.Date;
import java.util.List;

/**
 * Created by crm on 2017/5/9.
 */
public class LoginInfoDTO {

    private Integer user_id;// int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
    private String role_ids; // varchar(100) NOT NULL DEFAULT '0' COMMENT '用户角色',
    private Integer user_type;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户类型 0:普通员工 1:管理员 2:测试账号',
    private String name;//  varchar(50) NOT NULL COMMENT '用户姓名',
    private String password;//  char(32) NOT NULL COMMENT '用户密码',
    private String sex;//  enum('男','女') NOT NULL DEFAULT '男' COMMENT '性别',
    private String email;//  varchar(50) DEFAULT NULL COMMENT '用户邮箱',
    private String telephone;//  varchar(20) NOT NULL COMMENT '用户手机号码',
    private String scale;//  varchar(20) NOT NULL COMMENT '职级',
    private String level;//  varchar(200) DEFAULT NULL COMMENT '学历',
    private String residence;//  varchar(1000) DEFAULT NULL COMMENT '户籍',
    private String address;//  varchar(1000) NOT NULL COMMENT '用户联系地址',
    private Long last_login_time;//  int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户最后一次登录时间',
    private Long employment_time;//  int(10) unsigned NOT NULL DEFAULT '0' COMMENT '入职时间',
    private Long create_time;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户注册时间',
    private Integer status;// tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态 1:在职，2:停用，3:离职',
    private Integer sub_company_id;//  int(10) NOT NULL DEFAULT '0' COMMENT '分公司id',
    private Integer department_id;//  int(10) NOT NULL DEFAULT '0' COMMENT '部门id',
    private Integer tq_uin;//  int(10) DEFAULT NULL COMMENT 'tq用户名',
    private String tq_uin_pwd;//  varchar(20) DEFAULT NULL COMMENT 'tq密码',
    private Date update_time;//  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    private String openid;//  varchar(50) DEFAULT NULL COMMENT '微信openid',

    private Integer data_type; // 数据权限": "4",
    private Integer look_phone; //查看电话权限": "2",
    private List<RoleInfoDTO> role_info;   //角色列表

    private Integer contract_abnormal; //": 1,
    private Integer agreement_addToReceivable; //": 1,
    private String department_name; //": "上海总公司",
    private String sub_company_name; //": "上海总公司",
    private String redis_city; //": ""
    private String authority;
    private List<AuthorityDTO> authorityDTOS;


    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getRole_ids() {
        return role_ids;
    }

    public void setRole_ids(String role_ids) {
        this.role_ids = role_ids;
    }

    public Integer getUser_type() {
        return user_type;
    }

    public void setUser_type(Integer user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(Long last_login_time) {
        this.last_login_time = last_login_time;
    }

    public Long getEmployment_time() {
        return employment_time;
    }

    public void setEmployment_time(Long employment_time) {
        this.employment_time = employment_time;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSub_company_id() {
        return sub_company_id;
    }

    public void setSub_company_id(Integer sub_company_id) {
        this.sub_company_id = sub_company_id;
    }

    public Integer getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Integer department_id) {
        this.department_id = department_id;
    }

    public Integer getTq_uin() {
        return tq_uin;
    }

    public void setTq_uin(Integer tq_uin) {
        this.tq_uin = tq_uin;
    }

    public String getTq_uin_pwd() {
        return tq_uin_pwd;
    }

    public void setTq_uin_pwd(String tq_uin_pwd) {
        this.tq_uin_pwd = tq_uin_pwd;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getData_type() {
        return data_type;
    }

    public void setData_type(Integer data_type) {
        this.data_type = data_type;
    }

    public Integer getLook_phone() {
        return look_phone;
    }

    public void setLook_phone(Integer look_phone) {
        this.look_phone = look_phone;
    }

    public List<RoleInfoDTO> getRole_info() {
        return role_info;
    }

    public void setRole_info(List<RoleInfoDTO> role_info) {
        this.role_info = role_info;
    }

    public Integer getContract_abnormal() {
        return contract_abnormal;
    }

    public void setContract_abnormal(Integer contract_abnormal) {
        this.contract_abnormal = contract_abnormal;
    }

    public Integer getAgreement_addToReceivable() {
        return agreement_addToReceivable;
    }

    public void setAgreement_addToReceivable(Integer agreement_addToReceivable) {
        this.agreement_addToReceivable = agreement_addToReceivable;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getSub_company_name() {
        return sub_company_name;
    }

    public void setSub_company_name(String sub_company_name) {
        this.sub_company_name = sub_company_name;
    }

    public String getRedis_city() {
        return redis_city;
    }

    public void setRedis_city(String redis_city) {
        this.redis_city = redis_city;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public List<AuthorityDTO> getAuthorityDTOS() {
        return authorityDTOS;
    }

    public void setAuthorityDTOS(List<AuthorityDTO> authorityDTOS) {
        this.authorityDTOS = authorityDTOS;
    }
}
