package com.wusheng.mqtttest;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PushCallback implements MqttCallback {

    private static Logger log = LoggerFactory.getLogger(PushCallback.class);

    @Autowired
    private MqttPushClient mqttPushClient;

    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        log.error("连接断开，尝试重连..");
        long reconnectTimes = 1;
        while (true) {
            try {
                if (mqttPushClient.getClient().isConnected()) {
                    log.info("mqtt reconnect success end");
                    return;
                }
                log.info("mqtt reconnect times = {} try again...", reconnectTimes++);
                mqttPushClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete: " + token.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + message.getQos());
        log.info("接收消息内容 : " + new String(message.getPayload()));

    }


}
