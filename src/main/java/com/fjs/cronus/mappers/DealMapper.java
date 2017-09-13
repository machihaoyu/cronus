package com.fjs.cronus.mappers;


import com.fjs.cronus.model.Deal;
import com.fjs.cronus.util.MyMapper;

/**
 * 描述：交易表模块dao接口，提供数据库操作方法
 *
 * @author yinzf
 * @version 1.0 2017-09-12
 */
public interface DealMapper extends MyMapper<Deal> {

	/**
	 * 新增交易表方法
	 * @param deal Deal:实体类
	 */
//	void add(Deal deal);
	
	/**
	 * 删除交易表方法
	 * @param key String:多个由“，”分割开的id字符串
	 */
//	void deleteByKey(String key);
	
	/**
	 * 根据主键查找交易表实体方法
	 * @param key String：实体主键（查询条件）
	 * @return Deal: 实体
	 */
//	public Deal getByPrimaryKey(String key);
	
	/**
	 * 根据条件查找交易表列表方法
	 * @param Deal deal：实体对象（查询条件）
	 * @return List<Deal>: 实体对象的list
	 */
//	public List<Deal>  listByCondition(Deal deal);
	
	/**
	 * 修改交易表方法
	 * @param deal Deal：实体对象
	 */	
//	public void update(Deal deal);
}