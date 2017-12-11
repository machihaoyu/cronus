package com.fjs.cronus.controller;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CommentDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.Comment;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.service.CommentService;
import com.fjs.cronus.service.CommunicationLogService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinzf on 2017/11/24.
 */
@Controller
@Api(description = "评论控制器")
@RequestMapping("/api/v1")
public class CommentController {
    private  static  final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private UcService thorUcService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommunicationLogService communicationLogService;

    @ApiOperation(value="新增评论", notes="新增评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/insertComment", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public CronusDto insertComment(@Valid @RequestBody CommentDTO commentDTO, BindingResult result, HttpServletRequest request){
        logger.info("新增评论的数据：" + commentDTO.toString());
        CronusDto theaApiDTO = new CronusDto();
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        try{
            String token=request.getHeader("Authorization");
            UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
            Integer userId = null;
            if (userInfoDTO != null && StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
            if (commentDTO.getCommunicationLogId() == null){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.Communication_LOG_ID_NULL);
                return theaApiDTO;
            }
            //只有业务员的团队长才能评论
            CommunicationLog communicationLog = communicationLogService.getByPrimaryKey(commentDTO.getCommunicationLogId());
            if (communicationLog == null){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.CUSTOMER_NO_EXIST);
                return theaApiDTO;
            }
            Integer ownUserId = communicationLog.getCreateUser();
            List<String> idList = new ArrayList<String>();
            if (StringUtils.isNotEmpty(userInfoDTO.getData_type())) {
                Integer dataType = Integer.parseInt(userInfoDTO.getData_type());
                //查看下属
                if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                    idList= thorUcService.getSubUserByUserId(token, userId);
                    if (!idList.contains(ownUserId.toString())){
                        theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                        theaApiDTO.setMessage(CommonConst.AUTH_MESSAGE);
                        return theaApiDTO;
                    }
                }
            }
            if (StringUtils.isEmpty(commentDTO.getContent())){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.COMMENT_NULL);
                return theaApiDTO;
            }
            int addResult = commentService.add(commentDTO,userInfoDTO);
            if (addResult >0) {
                theaApiDTO.setResult(CommonMessage.ADD_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->insertComment创建评论失败");
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        }catch (Exception e){
            logger.error("-------------->insertComment新建评论失败",e);
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }

    @ApiOperation(value="根据沟通记录id获取评论内容", notes="根据沟通记录id获取评论内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "communicationLogId", value = "沟通记录id", required = true, paramType = "query",  dataType = "int"),
    })
    @RequestMapping(value = "/selectByCommennicationLogId", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto selectByCommennicationLogId(@RequestParam(required = true) Integer communicationLogId, HttpServletRequest request){
        CronusDto theaApiDTO = new CronusDto();
        List<Comment> commentList = null;
        String token=request.getHeader("Authorization");
        UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token,CommonConst.SYSTEMNAME);
        try{
            if (communicationLogId != null){
                commentList = commentService.getByCommunicationLogId(communicationLogId);
                theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            }else{
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
            }
        }catch (Exception e){
            logger.error("根据CommennicationLogId获取评论内容失败",e);
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        theaApiDTO.setData(commentList);
        return theaApiDTO;
    }
}
