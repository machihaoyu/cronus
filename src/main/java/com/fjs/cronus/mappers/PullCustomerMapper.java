package com.fjs.cronus.mappers;

import com.fjs.cronus.model.PullCustomer;
import com.fjs.cronus.util.MyMapper;
import java.util.List;
import java.util.Map;

/**
 * Created by yinzf on 2017/10/24.
 */
public interface PullCustomerMapper extends MyMapper<PullCustomer> {

    public List<PullCustomer> listByCondition(Map<String, Object> map);

    public Integer countByCondition(Map<String, Object> map);

    public Integer update(PullCustomer pullCustomer);
}
