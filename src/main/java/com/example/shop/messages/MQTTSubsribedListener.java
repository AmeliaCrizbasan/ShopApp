package com.example.shop.messages;

import org.springframework.context.ApplicationListener;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;

public class MQTTSubsribedListener implements ApplicationListener<MqttSubscribedEvent> {
    @Override
    public void onApplicationEvent(MqttSubscribedEvent event) {
        System.out.println("bla bla ");
    }
}
