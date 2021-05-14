package com.wusheng.mqtttest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PubController {

    @GetMapping("/pubtest")
    public String pubTest()
    {
        MqttPushClient.publish("test", "{\"a\":1, \"b\":2}");

        return "ok";
    }
}
