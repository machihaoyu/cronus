package com.fjs.cronus.util.mqSpring;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;

/**
 * Created by feng on 2017/10/18.
 */
@Service
public class ProducerSpringService {
//    @Value("${mq.ak}")
    private String accessKey;

//    @Value("${mq.sk}")
    private String secretKey;

//    @Value("${mq.producerId}")
    private String producerId;

//    @Value("${mq.url}")
    private String url;

//    @Value("${mq.topic}")
    private String topic;

    @Resource
    ProducerBean producerBean;

    /**
     * 发送消息
     */
    public boolean send(String tag, String msgContent) {
        if (StringUtils.isBlank(tag) || StringUtils.isBlank(msgContent)) {
            //两个参数都不能为空或者null
            return false;
        }
        Properties properties = PropertiesUtils.initProperties(producerId, accessKey, secretKey);
        //设置topic
        properties.setProperty("Topic", topic);
        producerBean.setProperties(properties);
        producerBean.start();
        Message msg = new Message( //
                // Message所属的Topic
                topic,
                // Message Tag 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
                tag,
                // Message Body 可以是任何二进制形式的数据， MQ不做任何干预
                // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                msgContent.getBytes());
        // 设置代表消息的业务关键属性，请尽可能全局唯一
        // 以方便您在无法正常收到消息情况下，可通过MQ 控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
//            msg.setKey("ORDERID_100");
        // 发送消息，只要不抛异常就是成功
        try {
            SendResult sendResult = producerBean.send(msg);
            assert sendResult != null;
        } catch (ONSClientException e) {
            producerBean.shutdown();
            return false;
        }
        producerBean.shutdown();
        return true;
    }

    /**
     * 批量发送消息
     */
    public boolean sendBatch(String tag, List<String> msgList) {
        if (StringUtils.isBlank(tag)) {
            //两个参数都不能为空或者null
            return false;
        }
        if (null == msgList || msgList.size() == 0) {
            return false;
        }
        Properties properties = PropertiesUtils.initProperties(producerId, accessKey, secretKey);
        //设置topic
        properties.setProperty("Topic", topic);
        producerBean.setProperties(properties);
        producerBean.start();
        for (String msgStr : msgList) {
            Message msg = new Message( //
                    // Message所属的Topic
                    topic,
                    // Message Tag 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
                    tag,
                    // Message Body 可以是任何二进制形式的数据， MQ不做任何干预
                    // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                    msgStr.getBytes());
            // 设置代表消息的业务关键属性，请尽可能全局唯一
            // 以方便您在无法正常收到消息情况下，可通过MQ 控制台查询消息并补发
            // 注意：不设置也不会影响消息正常收发
//            msg.setKey("ORDERID_100");
            // 发送消息，只要不抛异常就是成功
            try {
                SendResult sendResult = producerBean.send(msg);
                assert sendResult != null;
            } catch (ONSClientException e) {
                producerBean.shutdown();
                return false;
            }
        }
        producerBean.shutdown();
        return true;
    }
}
