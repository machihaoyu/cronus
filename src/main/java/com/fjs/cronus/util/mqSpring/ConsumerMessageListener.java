package com.fjs.cronus.util.mqSpring;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

/**
 * Created by feng on 2017/10/18.
 */
public class ConsumerMessageListener implements MessageListener {


    public Action consume(Message message, ConsumeContext context) {
        System.out.println("Receive: " + message);
        try {
            //消费消息处理业务逻辑
            return Action.ReconsumeLater;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
