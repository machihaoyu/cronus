package com.fjs.cronus.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：沉淀池日志模块实体类，负责页面与后台数据传输功能
 *
 * @author yinzf
 * @version 1.0 2017-10-31
 */
public class PrdOperationLog implements Serializable{
			   	
	private Integer id;

	/**
	 * 操作名称
 	 */
	private String operation;

	/**
	 * 客户id号
 	 */
	private Integer prdCustomerId;

	/**
	 * 结果
 	 */
	private String result;

	/**
	 * 创建人
	 */
	private Integer createUser;

	/**
	 * 创建时间
	 */
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

	public Integer getPrdCustomerId() {
		return prdCustomerId;
	}

	public void setPrdCustomerId(Integer prdCustomerId) {
		this.prdCustomerId = prdCustomerId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}