package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.api.thea.MailDTO;
import com.fjs.cronus.dto.cronus.CommentDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.mappers.CommentMapper;
import com.fjs.cronus.model.Comment;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.util.DateUtils;
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
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    TheaClientService theaClientService;
    public boolean add(CommentDTO commentDTO, UserInfoDTO userInfoDTO, CommunicationLog communicationLog,String token){
        boolean flag = false;
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
        commentMapper.insert(comment);
        //发送消息
        try {
            MailDTO mailDTO = new MailDTO();
            CustomerInfo customerInfo = customerInfoService.findCustomerById(communicationLog.getCustomerId());
            String content = "团队长"+userInfoDTO.getName()  + "对您的客户"+ customerInfo.getCustomerName()+"的沟通日志进行了评论，请注意查看。";
            mailDTO.setContent(content);
            mailDTO.setCreateTime(date);
            mailDTO.setToId(communicationLog.getCreateUser());
            mailDTO.setFromId(communicationLog.getCreateUser());
            mailDTO.setStatus(0);
            theaClientService.sendMail(token,content,communicationLog.getCreateUser(),communicationLog.getCreateUser(),null,communicationLog.getCreateUser());
        }catch (Exception e){
            e.printStackTrace();
        }
        flag =true;
        return flag;
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
