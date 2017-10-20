package com.fjs.cronus.util.mq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 2017/10/16.
 */
@Service
public class ProducerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    HttpMQProducer httpMQProducer;

    @Value("${mq.ak}")
    private String ak;

    @Value("${mq.sk}")
    private String sk;

    @Value("${mq.url}")
    private String url;

    @Value("${mq.topic}")
    private String topic;

    @Value("${mq.producerId}")
    private String producerId;
    /**
     * 发送消息
     */
    public Boolean sendMQ() {
        Map<String,Integer> map =new HashMap<>();
        for(int i=0;i<2;i++){
            String msg = "simple test "+i+" http message,"+new Date();
            if (httpMQProducer.send(msg, "xf_2", "xf_2", null)) {
            } else {
                System.out.println(msg+" result: failed ");
            }
        }

        return null;
    }

}
