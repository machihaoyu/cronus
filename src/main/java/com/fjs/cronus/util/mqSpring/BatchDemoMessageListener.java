package com.fjs.cronus.util.mqSpring;


import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.batch.BatchMessageListener;

import java.util.List;

public class BatchDemoMessageListener implements BatchMessageListener {
    @Override
    public Action consume(final List<Message> messages, final ConsumeContext context) {
        try {
            //do something..
            for (Message message : messages) {
                switch (message.getTag()){
                    case CommonMQConst.THEA_HXF_01:
                        //这里是大家写对应的业务逻辑的地方！！！
                        byte[] objects  = message.getBody();
                        System.out.println(CommonMQConst.THEA_HXF_01);
                        System.out.println(new String(objects));
                        break;
                    default:
                        byte[] object  = message.getBody();
                        String str  = new String(object);

                        System.out.println(str);
                        break;
                }
            }
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
