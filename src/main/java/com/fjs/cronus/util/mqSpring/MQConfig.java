package com.fjs.cronus.util.mqSpring;

import com.aliyun.openservices.ons.api.batch.BatchMessageListener;
import com.aliyun.openservices.ons.api.bean.BatchConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;


import com.fjs.cronus.util.mq.consumer.HttpMQConsumer;
import com.fjs.cronus.util.mq.producer.HttpMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by feng on 2017/10/18.
 */
@Configuration
public class MQConfig {

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

    @Value("${mq.tag}")
    private String tag;

    @Value("${mq.consumeMessageBatchMaxSize}")
    private String consumeMessageBatchMaxSize;

    @Bean
    HttpMQConsumer httpMQConsumer() {
        return new HttpMQConsumer();
    }

    @Bean
    HttpMQProducer httpMQProducer() {
        return new HttpMQProducer();
    }

    @Bean
    ProducerBean producerBean() {
        return new ProducerBean();
    }

    @Bean
    ConsumerMessageListener consumerMessageListener() {
        return new ConsumerMessageListener();
    }

    @Bean
    BatchConsumerBean batchConsumerBean() {
        BatchConsumerBean batchConsumerBean = new BatchConsumerBean();
        Properties properties = new Properties();
        properties.setProperty("ConsumerId", consumerId);
        properties.setProperty("AccessKey", ak);
        properties.setProperty("SecretKey", sk);
        properties.setProperty("ConsumeMessageBatchMaxSize", consumeMessageBatchMaxSize);
        Map<Subscription, BatchMessageListener> subscriptionTable = new HashMap<>();
        batchConsumerBean.setProperties(properties);
        Subscription subscription = new Subscription();
        subscription.setExpression(tag);
        subscription.setTopic(topic);
        subscriptionTable.put(subscription, new BatchDemoMessageListener());
        batchConsumerBean.setSubscriptionTable(subscriptionTable);
        batchConsumerBean.start();
        return batchConsumerBean;
    }

}
