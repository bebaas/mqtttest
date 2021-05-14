package com.wusheng.mqtttest;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MqttPushClient {
    private static Logger log = LoggerFactory.getLogger(MqttPushClient.class);

    @Autowired
    private PushCallback pushCallback;

    private static MqttClient client;

    public static MqttClient getClient() {
        return client;
    }

    public static void setClient(MqttClient client) {
        MqttPushClient.client = client;
    }

    private String host;
    private String clientid;
    private String username;
    private String password;
    private int timeout;
    private int keepalive;

    public void getParameters(String host,String clientid,String username,String password,int timeout,int keepalive) {
        this.host = host;
        this.clientid = clientid;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
        this.keepalive = keepalive;
    }

    /**
     * 设置mqtt连接参数
     *
     */
    public MqttConnectOptions setMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(timeout);
        options.setKeepAliveInterval(keepalive);
        options.setCleanSession(false);
        return options;
    }

    public void connect(){
        try {
            if (client == null) {
                client = new MqttClient(host, clientid, new MemoryPersistence());
                client.setCallback(pushCallback);
            }
            MqttConnectOptions mqttConnectOptions = setMqttConnectOptions();

            if (!client.isConnected()) {
                client.connect(mqttConnectOptions);
            } else {
                client.disconnect();
                client.connect(mqttConnectOptions);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void connect(String host, String clientID, String username, String password, int timeout, int keepalive){
        MqttClient client;
        try {
            client = new MqttClient(host, clientID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepalive);
            MqttPushClient.setClient(client);
            try {
                client.setCallback(pushCallback);
                client.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 发布，默认qos为0，非持久化
     * @param topic
     * @param pushMessage
     */
    public static void publish(String topic,String pushMessage){
        publish(0, false, topic, pushMessage);
    }

    /**
     * 发布
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public static void publish(int qos,boolean retained,String topic,String pushMessage){
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if(null == mTopic){
            log.error("topic not exist");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅某个主题，qos默认为0
     * @param topic
     */
    public static void subscribe(String topic){
        subscribe(topic,0);
    }

    /**
     * 订阅某个主题
     * @param topic
     * @param qos
     */
    public static void subscribe(String topic,int qos){
        try {
            MqttPushClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
