package com.fjs.cronus.mappers;



import com.fjs.cronus.model.PrdCustomer;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by yinzf on 2017/11/1.
 */
public interface PrdCustomerMapper extends MyMapper<PrdCustomer> {

    public Integer update(PrdCustomer prdCustomer);

    /**
     * 根据条件查找市场推广列表
     * @param map（查询条件）
     * @return
     */
    public List<PrdCustomer> listByCondition(Map<String, Object> map);

    /**
     * 查询时获取总数
     * @param map
     * @return
     */
    public Integer countByCondition(Map<String, Object> map);


    public PrdCustomer findById(Map<String, Object> map);
}
