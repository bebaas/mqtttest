package com.wusheng.mqtttest;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Subscribe implements ApplicationRunner {

    /**
     * 默认订阅的主题
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        MqttPushClient.subscribe("test01");

        MqttPushClient.subscribe("test02");

    }
}
