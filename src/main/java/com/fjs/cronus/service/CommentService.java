package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.cronus.CommentDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.mappers.CommentMapper;
import com.fjs.cronus.model.Comment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinzf on 2017/11/24.
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public Integer add(CommentDTO commentDTO, UserInfoDTO userInfoDTO){
        Comment comment = copyProperty(commentDTO);
        Integer userId = null;
        if (userInfoDTO != null && StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
            userId = Integer.parseInt(userInfoDTO.getUser_id());
        }
        Date date = new Date();
        comment.setCreateTime(date);
        comment.setCreateUser(userId);
        comment.setCreateUserName(userInfoDTO.getName());
        comment.setIsDeleted(CommonConst.DATA_NORMAIL);
        return commentMapper.insert(comment);
    }

    public Comment copyProperty(CommentDTO commentDTO){
        Comment comment = new Comment();
        comment.setCommunicationLogId(commentDTO.getCommunicationLogId());
        comment.setContent(commentDTO.getContent());
        return comment;
    }

    public List<Comment> getByCommunicationLogId(Integer communicationLogId){
        Map map = new HashMap();
        map.put("communicationLogId",communicationLogId);
        return commentMapper.listByCondition(map);
    }
}
