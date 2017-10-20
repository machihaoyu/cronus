package com.fjs.cronus.util.mq.consumer;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by feng on 2017/10/16.
 */
@Service
public class ConsumerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    HttpMQConsumer httpMQConsumer;

    @Value("${mq.ak}")
    private String ak;

    @Value("${mq.sk}")
    private String sk;

    @Value("${mq.url}")
    private String url;

    @Value("${mq.topic}")
    private String topic;

    @Value("${mq.consumerId}")
    private String consumerId;

    public void receiveMQ(){
        List<SimpleMessage> list = httpMQConsumer.pull("xf_2", "xf_2");
        System.out.println("---------end pull message-----------"+new Date()+" size:"+list.size());
        if (list != null && list.size() > 0) {
            for (SimpleMessage simpleMessage : list) {
                System.out.println(JSON.toJSONString(simpleMessage));
                // 当消息处理成功后，需要进行delete，如果不及时delete将会导致重复消费此消息
                /*String msgHandle = simpleMessage.getMsgHandle();
                if (httpMQConsumer.delete(msgHandle)) {
                    System.out.println("delete success: " + msgHandle);
                } else {
                    System.out.println("delete failed: " + msgHandle);
                }*/
            }
        }
    }
}
