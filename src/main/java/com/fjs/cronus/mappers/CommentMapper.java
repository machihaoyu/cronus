package com.fjs.cronus.mappers;



import com.fjs.cronus.model.Comment;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by yinzf on 2017/11/24.
 */
public interface CommentMapper extends MyMapper<Comment> {
    public List<Comment> listByCondition(Map<String, Object> map);

//    public Integer countByCondition(Map<String,Object> map);
}
