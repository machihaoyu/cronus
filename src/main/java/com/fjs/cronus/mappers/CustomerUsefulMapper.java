package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerUseful;
import com.fjs.cronus.util.MyMapper;
import java.util.List;
import java.util.Map;

/**
 * Created by yinzf on 2017/10/13.
 */
public interface CustomerUsefulMapper extends MyMapper<CustomerUseful> {

    List<CustomerUseful> countByMap(Map<String, Object> map);
}
