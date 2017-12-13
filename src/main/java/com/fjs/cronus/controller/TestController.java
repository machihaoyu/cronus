package com.fjs.cronus.controller;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.mq.consumer.HttpMQConsumer;
import com.fjs.cronus.util.mq.consumer.SimpleMessage;
import com.fjs.cronus.util.mq.producer.HttpMQProducer;
import com.fjs.cronus.util.mqSpring.ProducerSpringService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by msi on 2017/10/19.
 */
@Controller
@RequestMapping("/api/v1")
public class TestController {

    @Autowired
    HttpMQProducer httpMQProducer;
    @Autowired
    HttpMQConsumer httpMQConsumer;
    @Autowired
    ProducerSpringService producerSpringService;
    @Autowired
    UcService ucService;
    @Autowired
    CustomerInfoService customerInfoService;
    @ApiOperation(value="MQ发送测试", notes="HTTP模式发送MQ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msg", value = "消息", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "key", value = "密钥key", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tag", value = "分支", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/testSendMQ", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto testSendMQ(
            @RequestParam(required = false)String msg,
            @RequestParam (required = false)String key,
            @RequestParam (required = false)String tag){
        CronusDto resultDto = new CronusDto();
        Boolean flage = httpMQProducer.send(msg,tag,key,null);
        return resultDto;
    }

    @ApiOperation(value="MQ接收测试", notes="HTTP模式接收MQ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "密钥key", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tag", value = "分支", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/testReceiveMQ", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<SimpleMessage>> testReceiveMQ(
            @RequestParam (required = false)String key,
            @RequestParam (required = false)String tag){
        CronusDto resultDto = new CronusDto();
        List<SimpleMessage> list = httpMQConsumer.pull(tag, key);
        resultDto.setData(list);
        return resultDto;
    }

    @ApiOperation(value="MQ发送测试", notes="SPRING模式发送MQ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msgContent", value = "消息体", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tag", value = "分支", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/testSpringSendMQ", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto testSpringSendMQ(
            @RequestParam (required = true)String msgContent,
            @RequestParam (required = true)String tag
    ) {
        CronusDto resultDto = new CronusDto();
        producerSpringService.send(tag,msgContent);
        return resultDto;
    }
    @ApiOperation(value="测试得到用户信息", notes="测试得到用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/getuserInfo", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getuserInfo(
            @RequestHeader("Authorization") String token){
        CronusDto resultDto = new CronusDto();
        Integer user_id = ucService.getUserIdByToken(token);
        System.out.println(user_id);
        return resultDto;
    }
}
